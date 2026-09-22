package autofighter;

import autofighter.ui.StartPanel;
import autofighter.ui.UserInterface;
import autofighter.ui.util.CombatTarget;
import autofighter.ui.util.Overlay;
import autofighter.ui.util.Stats;
import com.osrsbots.orb.api.RuneLite;
import com.osrsbots.orb.api.events.LevelUpListener;
import com.osrsbots.orb.api.input.mouse.Mouse;
import com.osrsbots.orb.api.interact.Interaction;
import com.osrsbots.orb.api.interactables.combat.Combat;
import com.osrsbots.orb.api.interactables.entities.Items;
import com.osrsbots.orb.api.interactables.entities.Player;
import com.osrsbots.orb.api.interactables.types.RSItem;
import com.osrsbots.orb.api.interactables.types.RSPlayer;
import com.osrsbots.orb.api.interactables.variables.VarBits;
import com.osrsbots.orb.api.interactables.widgets.Bank;
import com.osrsbots.orb.api.interactables.widgets.DepositBox;
import com.osrsbots.orb.api.interactables.widgets.Widgets;
import com.osrsbots.orb.api.interactables.world.Traverse;
import com.osrsbots.orb.api.util.*;
import com.osrsbots.orb.scripts.Scripts;
import com.osrsbots.orb.scripts.framework.ScriptMeta;
import com.osrsbots.orb.scripts.framework.loop.LoopingScript;
import com.osrsbots.orb.scripts.managers.LoginManager;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@SuppressWarnings("deprecation")
@ScriptMeta(name = "AutoFighter", author = "ORB", version = 1.2)
public class AutoFighter implements LoopingScript, LevelUpListener {

    public enum State {
        CONFIG, START, COMBAT, BURY_BONES, TO_COMBAT,
        OPEN_BANK, DEPOSIT, WITHDRAW, CLOSE_BANK, TO_BANK, STOP
    }

    public State state;

    public Stats stats;

    public Overlay overlay;

    public UserInterface ui;

    public RSPlayer player;

    public String stopMsg;

    private int randomBuryAllSeed;

    // Handlers
    public CombatHandler combat;

    public void log(final String msg) {
        log.info(msg);
        SwingUtilities.invokeLater(() -> ui.statusLbl.setText(msg));
    }

    public static Map<Predicate<RSItem>, Integer> requiredItems;

    public static List<String> itemNames;

    boolean hasInvalidBankItem() {
        requiredItems = new HashMap<>();
        itemNames = new ArrayList<>();

        for (String s : ui.getBankItems()) {
            String[] parts;

            // Support both - make it as easy a possible on user
            if (s.contains(":")) {
                parts = s.split(":");
            } else if (s.contains(";")) {
                parts = s.split(";");
            } else {
                // Assume 1 quantity
                log.info("[BANK] x1 " + s);
                requiredItems.put(item -> item.getName().startsWith(s), 1);
                itemNames.add(s);
                continue;
            }

            int amount;

            try {
                amount = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                stopMsg = "Failed to format quantity for required item: " + s;
                log.info(stopMsg, e);
                state = State.STOP;
                return true;
            }

            final String name = parts[1];
            log.info("[BANK] x" + amount + " " + name);
            requiredItems.put(item -> item.getName().startsWith(name), amount);
            itemNames.add(name);
        }

        return false;
    }

    @Override
    public int loop() {
        switch (state) {
            case CONFIG:
                return 1000;
            case START:
                if (!LoginManager.loggedIn()) {
                    return Random.nextInt(200, 2000);
                }

                if (!Combat.isAutoRetaliateEnabled() && !Combat.toggleRetaliate(true)) {
                    log.info("Failed to enable AutoRetaliate!");
                    // TODO - check & stop if X tries
                    return Random.nextInt(-4, 0);
                }

                init();
                return 200;
            case COMBAT:
                return combat.execute();
            case BURY_BONES:
                final List<RSItem> bones = Items.query().inventoryActions("Bury").nameContains(
                        true, "bone"
                ).results().get();

                if (bones.isEmpty()) {
                    log.info("no bone present");
                    state = State.COMBAT;
                    return Random.nextInt(0, 399);
                }

                final boolean buryAll = (
                        ui.buryBones() == 1 || combat.randomBuryBones == 1 || Random.nextInt(99) < randomBuryAllSeed
                );

                if (buryAll) {
                    log("Burying all bones...");
                    if (buryAllBones(bones)) {
                        if (combat.randomBuryBones != -1) {
                            combat.randomBuryBones = Random.ints(1, 4);
                            log.info("[BURY] Random=" + combat.randomBuryBones);
                        }
                        state = State.COMBAT;
                        return Random.nextInt(0, 899);
                    }
                } else {
                    // Bury one
                    log("Burying bone...");
                    final int i = bones.size();
                    final RSItem bone = bones.get(i == 1 ? 0 : Random.ints(0, i - 1));

                    if (bone.interact("Bury")) {

                        if (combat.randomBuryBones != -1)
                            combat.randomBuryBones = Random.ints(1, 4);

                        state = State.COMBAT;

                        // Track count + update UI
                        stats.bonesBuried++;
                        SwingUtilities.invokeLater(() -> ui.bonesBuried.setText(String.valueOf(stats.bonesBuried)));

                        return Random.nextInt(0, 499);
                    }
                }
                break;
            case TO_BANK:
                final WorldPoint bank = ui.getBank();

                if (bank != null) {
                    final Boolean travelToBank = Traverse.to(bank);

                    if (travelToBank == null) {
                        stopMsg = "Unable to find path to bank!";
                        state = State.STOP;
                    } else if (travelToBank.equals(Boolean.TRUE) || bank.distanceTo(player.getWorldLocation()) < 10) {
                        log("Opening Bank");
                        state = State.OPEN_BANK;
                    }
                } else {
                    stopMsg = "Unable to travel to bank, is null: " + bank;
                    state = State.STOP;
                }
                break;
            case OPEN_BANK:
                if (Bank.isOpen() || Bank.openNearest()) {
                    log("Depositing un-needed items");
                    state = State.DEPOSIT;
                }
                break;
            case DEPOSIT:
                if (Bank.depositAllExcept(itemNames.toArray(String[]::new))) {
                    log.info("Withdrawing needed items");
                    state = State.WITHDRAW;
                }
                break;
            case WITHDRAW:
                if (Bank.isOpen()) {
                    final Boolean withdrewItems = Bank.withdrawAllFilter(requiredItems);
                    log.info("withdrew=" + withdrewItems);

                    if (withdrewItems == null) {
                        // Missing item
                        stopMsg = "Missing required item(s) from bank!";
                        state = State.STOP;
                    } else if (Boolean.TRUE.equals(withdrewItems)) {
                        log("Closing bank...");
                        state = State.CLOSE_BANK;
                    }
                } else {
                    log("Opening bank...");
                    Bank.openNearest();
                }

                return Random.nextInt(0, 1200);
            case CLOSE_BANK:
                if (!Bank.isOpen() || Bank.close()) {
                    log("Traveling to combat zone...");
                    state = State.TO_COMBAT;
                }
                return Random.nextInt(0, 3000);
            case TO_COMBAT:
                final Boolean travelToCombat = Traverse.to(ui.location);

                if (travelToCombat == null) {
                    stopMsg = "Unable to find path to combat zone!";
                    state = State.STOP;
                } else if (travelToCombat.equals(Boolean.TRUE)) {
                    state = State.COMBAT;
                    combat.isMultiZone = VarBits.get(Varbits.MULTICOMBAT_AREA) == 1;
                }
                break;
            case STOP:
                if (Bank.isOpen() || DepositBox.isOpen()) {
                    Widgets.close();
                } else if (Player.logout()) {
                    if (stopMsg != null) {
                        stop(stopMsg);
                    } else {
                        stop();
                    }
                }
                break;
        }

        return Random.nextInt(0, 1000);
    }


    // TODO - add support to API mass item use (name, action)
    private boolean buryAllBones(final List<RSItem> rsItems) {
        net.runelite.api.Point rPos;
        while (Scripts.isRunning()) {
            if (Thread.interrupted() || Scripts.isPaused()) {
                break;
            }

            /* Update mouse position */
            rPos = RuneLite.client.getMouseCanvasPosition();
            final java.awt.Point mPos = new java.awt.Point(rPos.getX(), rPos.getY());

            /* Find the closest item to mouse */
            final HashMap<RSItem, Double> results = new HashMap<>(rsItems.size());

            // Cache distance for each item to mouse pos
            rsItems.forEach(i -> {
                final Rectangle bounds = i.getClickbox();
                if (bounds != null) results.put(i, mPos.distance(Interaction.centerPointIn(bounds)));
            });

            // Done
            if (results.isEmpty()) {
                Mouse.setOverride(null);
                return true;
            }

            final List<RSItem> closestItems = results.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue()) // Sort by value in ascending order
                    .limit(2) // Limit to the first three entries
                    .map(Map.Entry::getKey) // Get the widget from each entry
                    .collect(Collectors.toList());

            final RSItem random = closestItems.get(closestItems.size() == 1 ? 0 : Random.nextInt(0, 1));

            // Drop
            if (random.interact("Bury")) {
                rsItems.remove(random);
                Delay.wait(100, 300);
            }
        }

        return rsItems.isEmpty();
    }

    private void init() {

        attackSpeedInTicks = 0;
        totalXp = RuneLite.client.getOverallExperience();

        if (hasInvalidBankItem()) {
            log.info("Player has invalid bank item configuration!");
            return;
        }

        log.info("Initializing....");

        stats = new Stats();
        combat = new CombatHandler(this);

        if (ui.usePotions()) {
            PotionHandler.init();

            // Analyze bank items - determine potion(s) to use
            for (PotionHandler.Potion potion : PotionHandler.mixedPotions) {
                final String match = itemNames.stream().filter(
                        itemName -> potion.name.toLowerCase().startsWith(itemName.toLowerCase())
                ).findFirst().orElse(null);

                if (match != null) {
                    combat.potions = List.of(potion);
                    break;
                }
            }

            // If we did not find a mixed potion, look for regular potions
            if (combat.potions == null) {
                final List<PotionHandler.Potion> potions = new ArrayList<>();

                for (PotionHandler.Potion potion : PotionHandler.potions) {
                    final String match = itemNames.stream().filter(
                            itemName -> potion.name.toLowerCase().startsWith(itemName.toLowerCase())
                    ).findFirst().orElse(null);

                    if (match != null) {
                        potions.add(potion);
                        break;
                    }
                }

                if (!potions.isEmpty()) {
                    combat.potions = potions;
                }
            }

            if (combat.potions == null) {
                log.info("[POTIONS] Did not find any boost available!");
                Player.sendMessage(ChatMessageType.PUBLICCHAT, "AutoFighter", "Did not find any boost available!");
            } else {
                final String potions = combat.potions.stream().map(p -> p.name).collect(Collectors.toList()).toString();
                log.info("[POTIONS] " + potions);
                Player.sendMessage(ChatMessageType.PUBLICCHAT, "AutoFighter", potions);
            }
        }

        if ((player = Player.get()) != null) {
            if (ui.bankOnFullInv() && Items.isInventoryFull() && !(
                    ui.buryBones() > 0 && Items.query().inventoryActions("Bury").nameContains(true, "bone").results().isPresent()
            )) {
                log("Player has full inventory, need to bank, traveling...");
                state = State.TO_BANK;

                // Track count + update UI
                stats.bankTrips++;
                SwingUtilities.invokeLater(() -> ui.bankTrips.setText(String.valueOf(stats.bankTrips)));
            } else {
                if (ui.location.distanceTo(player.getWorldLocation()) > ui.getCombatZoneDistance()) {
                    if (ui.useBank() && !ui.bankOnFullInv() && !Items.query().consumable(true).results().isPresent()) {
                        log("Player needs food, going to bank...");
                        state = State.TO_BANK;

                        // Track count + update UI
                        stats.bankTrips++;
                        SwingUtilities.invokeLater(() -> ui.bankTrips.setText(String.valueOf(stats.bankTrips)));
                    } else {
                        log("Player is too far from combat zone, traveling...");
                        state = State.TO_COMBAT;
                    }
                } else {
                    log("Player is ready for combat...");
                    state = State.COMBAT;

                    combat.isMultiZone = VarBits.get(Varbits.MULTICOMBAT_AREA) == 1;
                }
            }
        } else {
            log("Player is null - unable to start!");
        }

        randomBuryAllSeed = Random.nextInt(5, 25);
        combat.playerIdleLimit = Random.nextInt(5, 20);
    }

    @Override
    public void onStart(String[] args) {
        if (!ClientUI.applyCustomSettings(
                ui = new UserInterface(this)
        )) {
            stop("Failed to applyCustomSettings(ui)");
            return;
        }

        state = State.CONFIG;
        overlay = new Overlay(this);

        Fatigue.setEnabled(true);
        ClientUI.toggleUserInput(true);
        RuneLite.overlayManager.add(overlay);

        ScriptUtil.addEventListener(this);
        ScriptUtil.addSubscriptionListener(this);
    }

    @Override
    public void onPause() {
        if (state != State.CONFIG)
            StartPanel.togglePanels(ui, true);
    }

    @Override
    public void onResume() {
        if (state != State.CONFIG) {
            StartPanel.togglePanels(ui, false);
        } else {
            ClientUI.toggleUserInput(true);
        }
    }

    @Override
    public void onStop() {
        ui.timer.cancelAllTasks();
        RuneLite.overlayManager.remove(overlay);

        ScriptUtil.removeEventListener(this);
        ScriptUtil.removeSubscriptionListener(this);
    }

    @Override
    public void onPlayerLogin(RSPlayer player) {
    }

    @Override
    public void onPlayerLogout() {
    }

    @Override
    public void shouldTakeBreak(int minutes) {
        readyForBreak();
    }

    /* This is used when selecting targets in the GUI */
    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked e) {
        if (ui.setTargets) {
            final MenuEntry me = e.getMenuEntry();
            final NPC npc = me.getNpc();

            if (npc != null) {
                final String option = e.getMenuOption().toLowerCase();
                if (option.contains("attack") || option.contains("cast")) {
                    e.consume();

                    final String name = npc.getName();
                    final int level = npc.getCombatLevel();
                    final int size = ui.targetListModel.getSize();
                    boolean add = true;

                    for (int i = 0; i < size; i++) {
                        final CombatTarget ct = ui.targetListModel.getElementAt(i);

                        if (ct.getLevel() == level && ct.getName().equals(name)) {
                            add = false;
                            break;
                        }
                    }

                    if (add) {
                        ui.targetListModel.addElement(new CombatTarget(name, npc.getCombatLevel()));

                        if (ui.combatTargetLbl.isVisible()) {
                            final int i = size + 1;
                            SwingUtilities.invokeLater(() -> {
                                ui.combatTargetLbl.setText(i + " Target" + (i == 1 ? "" : "s"));
                                ui.combatTargetLbl.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
                            });
                        }
                    }
                }
            }
        }
    }

    public volatile long totalXp = -1;

    public int tickCount;

    public int attackSpeedInTicks = -1;

    int checkCount = 0;

//    @Subscribe
//    public void onStatChanged(StatChanged e) {
//
//    }

    @Subscribe
    public void onGameTick(GameTick e) {
        final long xp = RuneLite.client.getOverallExperience();
        tickCount++;

        if (totalXp != xp) {
            if (attackSpeedInTicks != tickCount) {
                if (checkCount++ > 3) {
                    checkCount = 0;
                    attackSpeedInTicks = tickCount;
                    log.info("[AttackSpeed] Identified as " + tickCount + " tick(s)");
                }
            } else {
                checkCount = 0;
            }

            totalXp = xp;
            tickCount = 0;
        }


//        if (ui.usePotions() && combat != null) {
//            for (Skill skill : combat.boostedSkills.keySet()) {
//                if (RuneLite.client.getBoostedSkillLevel(skill) <= RuneLite.client.getRealSkillLevel(skill)) {
//                    log.info("[" + skill.name() + "] has degraded to, or below, original level!");
//                    combat.boostedSkills.clear();
//                    combat.usePotions = true;
//                    break;
//                }
//            }
//
//        }
    }

    @Override
    public void onLevelUp(final Skill skill, final int lvl) {
        // Track count + update UI
        stats.levelsGained++;
        SwingUtilities.invokeLater(() -> ui.levelsGained.setText(String.valueOf(stats.levelsGained)));
    }
}
