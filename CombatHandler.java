package autofighter;

import autofighter.ui.util.CombatTarget;
import com.osrsbots.orb.api.RuneLite;
import com.osrsbots.orb.api.interactables.combat.Health;
import com.osrsbots.orb.api.interactables.entities.Items;
import com.osrsbots.orb.api.interactables.entities.Loot;
import com.osrsbots.orb.api.interactables.entities.Npcs;
import com.osrsbots.orb.api.interactables.entities.Objects;
import com.osrsbots.orb.api.interactables.types.RSItem;
import com.osrsbots.orb.api.interactables.types.RSLoot;
import com.osrsbots.orb.api.interactables.types.RSNpc;
import com.osrsbots.orb.api.interactables.types.RSObject;
import com.osrsbots.orb.api.interactables.world.Traverse;
import com.osrsbots.orb.api.path.LocalCollisionPath;
import com.osrsbots.orb.api.path.Path;
import com.osrsbots.orb.api.util.ClientUI;
import com.osrsbots.orb.api.util.Delay;
import com.osrsbots.orb.api.util.Random;
import com.osrsbots.orb.scripts.Scripts;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CombatHandler {

    // TODO - use areas
    final AutoFighter script;


    volatile RSNpc target, ignoreNpc;

    WorldPoint targetPoint, lootSearchPoint;

    public boolean isMultiZone, buriedBones;

    List<PotionHandler.Potion> potions;

    // Randomly loot bones after every X kills
    int boneLookerKillCount = Random.ints(2, 4);

    boolean lookForBones;

    boolean isInFight, moveToTarget, ignoreAttacker;

    int playerIdles, playerIdleLimit, failQueries;

    boolean usePotions = true;

    final HashMap<Skill, Integer> boostedSkills = new HashMap<>();

    // Track when to 'Randomly' Bury Bones
    int randomBuryBones = -1;

    public CombatHandler(final AutoFighter script) {
        this.script = script;
    }

    public int execute() {
        // Heal
        if (Health.shouldConsume()) {
            script.log("Player has low health...");
            final Boolean consume = Health.consume();

            if (consume == null) {
                // Out of food
                script.log("Player is out of food!");
                script.state = AutoFighter.State.TO_BANK;

                // Track count + update UI
                script.stats.bankTrips++;
                SwingUtilities.invokeLater(() -> script.ui.bankTrips.setText(String.valueOf(script.stats.bankTrips)));

                return 0;
            }

            if (Boolean.TRUE.equals(consume)) {
                // Track count + update UI
                script.stats.foodConsumed++;
                SwingUtilities.invokeLater(() -> script.ui.foodConsumed.setText(String.valueOf(script.stats.foodConsumed)));
            }

            return Random.nextInt(0, 1999);
        }


        // Loot (Bones)
        if (lookForBones) {
            if (Items.isInventoryFull()) {
                log.info("Interrupted looking for bones, inventory is full!");
                lookForBones = false;
            } else {
                final String[] bonesToLoot = script.ui.getLootItems().stream().filter(
                        s -> s.toLowerCase().contains("bones")
                ).toArray(String[]::new);


                final RSLoot bones = Loot.query().names(true, bonesToLoot)
                        .maxDistance(8).reachable(true).results().nearestToPlayer();

                if (bones != null) {
                    return loot(bones);
                } else {
                    log.info("No bones found to loot!");
                    lookForBones = false;
                }
            }
        }


        // Potions
        if (usePotions && potions != null) {
            potions.forEach((p) -> p.boostedSkills.forEach(s -> {
                boostedSkills.put(s, RuneLite.client.getRealSkillLevel(s));
            }));

            final List<String> potionNames = potions.stream().map(p -> p.name).collect(Collectors.toList());
            log.info("Using potions (" + potionNames.size() + ")");

            while (!potionNames.isEmpty()) {
                if (Thread.interrupted() || Scripts.isPaused() || !Scripts.isRunning()) {
                    log.info("Interrupted while using potions!");
                    break;
                }

                final int size = potions.size();
                if (size == 0) {
                    log.info("Finished drinking potions!");
                    break;
                }

                final int randomIndex = (size == 1 ? 0 : Random.ints(size - 1));
                final PotionHandler.Potion randomPotion = potions.get(randomIndex);
                final String potionName = randomPotion.name;
                log.info("Looking for potion: " + potionName + " - (" + randomIndex + " / " + size + ")");

                final RSItem potion = Items.query().nameContains(potionName).results().nearestToMouse();

                if (potion != null) {
                    script.log("Using " + potion);

                    if (potion.interact()) {
                        potionNames.remove(potionName);
                    }
                } else {
                    potionNames.remove(potionName);
                    log.info("Did not find potion in inventory: " + potionName);
                }
            }

            usePotions = false;
        }

        if (moveToTarget) {
            final Boolean result = Traverse.to(targetPoint);

            // Failed to build path
            if (result == null) {
                log.info("Failed to find path to unreachable target: " + target);

                moveToTarget = false;
                target = null;
                return 0;
            }

            final List<WorldPoint> targetPath = Traverse.getTargetPath();

            // Walked path
            if (result.equals(Boolean.TRUE) || (targetPath != null && targetPath.size() <= 4)) {


                moveToTarget = false;

                Delay.wait(0, 999);

                if (attack()) {
                    return Random.nextInt(99, 999);
                }

                return Random.nextInt(0, 300);
            }

            // Following path... traversal in progress
            return Random.nextInt(0, 600);
        }

        // In fight?
        if (isInFight) {

            // Stolen Target
            if (!isMultiZone) {
                final Actor interacting = target.getInteracting();

                if (interacting != null) {
                    final String name = interacting.getName();

                    if (name != null && !name.equals(script.player.getName())) {
                        script.log("Target stolen by " + name);

                        ClientUI.highlightEntity(null);
                        isInFight = false;
                        target = null;
                        playerIdles = 0;

                        // Track count + update UI
                        script.stats.targetsStolen++;
                        SwingUtilities.invokeLater(() -> script.ui.targetsStolen.setText(String.valueOf(script.stats.targetsStolen)));

                        return -1;
                    }
                }
            }

            // Dead Target
            if (target.isDead()) {
                target = null;
                ignoreNpc = null;
                isInFight = false;
                script.log("Target is dead!");

                // Track kill count + update UI
                script.stats.kills++;

                if (boneLookerKillCount-- <= 0) {
                    boneLookerKillCount = Random.nextInt(1, 5);
                    lookForBones = true;
                }

                final int perHr = (int) (((double) script.stats.kills / ((double) Scripts.getRunTimeMinutes() + 1D)) * 60D);
                SwingUtilities.invokeLater(() -> script.ui.kills.setText(script.stats.kills + " (" + perHr + " per/Hr)"));

                return Random.nextInt(0, 999);
            }

            final int anim = script.player.getAnimation();

            if (anim == -1) {
                if (playerIdles++ >= playerIdleLimit) {
                    script.log("Player has reached idle limit: " + playerIdleLimit);
                    playerIdleLimit = Random.nextInt(7, 15);
                    ClientUI.highlightEntity(null);
                    ignoreAttacker = true;
                    isInFight = false;
                    target = null;
                    playerIdles = 0;
                    return 0;
                }
            } else {

                if (buriedBones) {
                    if (script.tickCount == 0) {
                        buriedBones = false;
                    }
                } else
                // Bury bones
                if (anim != 827) {
                    if (script.attackSpeedInTicks != -1 && script.tickCount == 0) {
                        // Bury Bones
                        final RSItem bones = Items.query().nameContains(true, "bones").results().randomNearMouse();

                        if (bones != null) {
                            buriedBones = bones.interact();
                            Delay.ticks(1);
                        }
                    }
                }

                playerIdles = 0;
            }

            return Random.nextInt(0, 600);
        }

        // Find target
        if (target == null) {
            if (noTarget()) {
                script.log("Failed to find target! (Attempt #" + failQueries + ")");
                return Random.nextInt(200, 1000 * Math.min(60, failQueries + 1));
            } else if (isInFight) return Random.nextInt(0, 900);
        }

        // Reachable?
        if (!target.isReachable()) {
            log.info("Target is NOT reachable!");
            targetPoint = target.getWorldLocation();

            final List<WorldPoint> worldPoints = LocalCollisionPath.find(
                    script.player.getWorldLocation(), targetPoint, true
            );

            if (worldPoints == null) {
                log.info("Unable to find path to target!");
                ignoreNpc = target;
                target = null;
            } else {
                moveToTarget = true;
                Traverse.setPath(new Path(worldPoints), targetPoint);
            }
            return 0;
        }

        // Attack target
        attack();

        return Random.nextInt(0, 1999);
    }

    private boolean attack() {
        if (target == null) {
            isInFight = false;
            return false;
        }

        // Attack target
        script.log("Fighting " + target.getName() + " (Lvl-" + target.getCombatLevel() + ")");

        if (target.interact("Attack")) {
            Delay.whileMoving(1500, 9000);
            script.tickCount = 0;
            isInFight = true;
            return true;
        }

        return false;
    }

    final DecimalFormat decimalFormat = new DecimalFormat("0.00");


    private int loot(final RSLoot loot) {
        final int exchangePrice = loot.getExchangePrice() * loot.getQuantity();
        log.info("[LOOT] Found: " + loot + "  | Value: " + exchangePrice + "gp");

        final int has = Items.count(loot.getId());

        // Interact
        loot.interact();

        // Delay
        final int dis = Math.min(loot.distanceToPlayer(), 10);

        if (dis > 0) {
            script.log("Moving towards loot...");

            Delay.untilMoving(1000, 2000);
            Delay.whileMoving(dis * 500, dis * 1500);

            script.log("Waiting to obtain loot!");
        }

        if (Delay.until(() -> Items.count(loot.getId()) > has, 0, 900, 1999)) {
            script.log("Looted " + loot.getName());

            // Track count + update UI
            script.stats.itemsLooted++;
            script.stats.wealthLooted += exchangePrice;

            SwingUtilities.invokeLater(() -> {
                final double value = script.stats.wealthLooted / 1000D;
                script.ui.itemsLooted.setText(script.stats.itemsLooted + " (" + decimalFormat.format(value) + "K)");
            });

            return Random.nextInt(0, 349);
        } else {
            log.info("[LOOT] Failed to take item!");
        }

        // TODO - Add/Check loot.isValid() or something?
        return Random.nextInt(0, 699);
    }


    private boolean noTarget() {
        final String playerName = script.player.getName();

        if (playerName == null) {
            log.info("Failed to get player's name: " + script.player);
            return true;
        }

        final WorldPoint playerPos = script.player.getWorldLocation();
        final ArrayList<CombatTarget> targets = script.ui.getCombatTargets();
        final String[] targetNames = targets.stream().map(CombatTarget::getName).toArray(String[]::new);

        // Query
        final List<RSNpc> results = Npcs.query().names(targetNames).actions("Attack").dead(false).results().stream().filter(n -> {
            if (java.util.Objects.equals(ignoreNpc, n)) {
                return false;
            }

            // Enforce level req
            for (CombatTarget ct : targets) {
                if (ct.getName().equals(n.getName())) {
                    if (ct.getLevel() != n.getCombatLevel()) return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        // Check if we have an attacker
        if (ignoreAttacker) {
            ignoreAttacker = false;
        } else {

            final Iterator<RSNpc> iterator = results.iterator();

            while (iterator.hasNext()) {
                if (Scripts.isPaused() || Thread.interrupted()) {
                    log.info("Failed to look for target - Interrupted!");
                    return true;
                }

                final RSNpc n = iterator.next();
                final Actor interacting = n.getInteracting();
                if (interacting == null) continue;

                // Ensure npc is either not in fight or is targeting us already
                if (playerName.equals(interacting.getName())) {
                    if (n.isReachable()) {
                        log.info("[FIGHT] Detected player has an existing attacker!");
                        ClientUI.highlightEntity(n);
                        target = n;

                            /*
                                1024 - North
                                1536 - East
                                512 - West
                                0 - South
                             */

                        final WorldPoint tPos = target.getWorldLocation();
                        WorldPoint pointInFrontOfTarget = tPos;
                        final int currentOrientation = target.getCurrentOrientation();

                        switch (currentOrientation) {
                            case 1024: // North
                                pointInFrontOfTarget = pointInFrontOfTarget.dy(1);
                                break;
                            case 0: // South
                                pointInFrontOfTarget = pointInFrontOfTarget.dy(-1);
                                break;
                            case 1536: // East
                                pointInFrontOfTarget = pointInFrontOfTarget.dx(1);
                                break;
                            case 512: // West
                                pointInFrontOfTarget = pointInFrontOfTarget.dx(-1);
                                break;
                        }

                        if (Objects.query().types(RSObject.Type.NORMAL).on(pointInFrontOfTarget).results().isPresent()) {
                            log.info("Detected target may be obstructed by an object!");
                            return false;
                        }

                        if (target.getAnimation() == -1 && !target.isMoving()
                                && Objects.query().types(RSObject.Type.WALL).on(tPos).results().isPresent()) {

                            log.info("Detected target may be obstructed by a wall!");
                            return false;
                        }

                        // Allow attacker to engage in combat with us
                        isInFight = true;
                        return false;
                    }
                } else if (!isMultiZone) {
                    iterator.remove();
                }
            }
        }

        // Check if result has eligible target
        target = results.stream().min(
                Comparator.comparingInt(n -> n.getWorldLocation().distanceTo(playerPos))
        ).orElse(null);

        if (target == null) {
            // Fail-safe... stop script if we can't find a target for a long time
            if (failQueries++ > 600) {
                script.stopMsg = "Repeatedly failed to find a target! Attempts="
                        + failQueries + " | Targets=" + script.ui.getCombatTargets();

                script.state = AutoFighter.State.STOP;
            }

            return true;
        } else {
            // Success
            failQueries = 0;
            ClientUI.highlightEntity(target);
            return false;
        }
    }
}
