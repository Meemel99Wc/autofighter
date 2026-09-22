package autofighter.ui;

import autofighter.AutoFighter;
import autofighter.FightProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.osrsbots.orb.api.RuneLite;
import com.osrsbots.orb.api.interactables.world.locations.Banks;
import com.osrsbots.orb.api.util.ClientUI;
import com.osrsbots.orb.api.util.ColorScheme;
import com.osrsbots.orb.api.util.Effects;
import com.osrsbots.orb.scripts.managers.FileManager;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class StartPanel {

    public static void init(final UserInterface ui) {
        ui.profileChoice.setModel(ui.profileChoiceModel);

        final List<String> savedFiles = FileManager.getFiles();

        if (savedFiles != null) {
            for (String savedFile : savedFiles) {
                final Object o = FileManager.readObject(FightProfile.class, savedFile);

                if (o instanceof FightProfile) {
                    ui.profileChoiceModel.addElement((FightProfile) o);
                }
            }
        }


        ui.saveProfileBtn.addActionListener(e -> {
            if (misConfigured(ui)) return;

            final String name = ui.newProfileName.getText();

            if (name != null && name.length() > 2) {
                final Gson gson = new GsonBuilder().setPrettyPrinting().create();


                final FightProfile profile = new FightProfile(
                        name,
                        ui.usePotionsBtn.isSelected(),
                        ui.lootInCmb.isSelected(),
                        ui.buryBones.getSelectedIndex(),
                        ui.getCombatZoneDistance(),
                        ui.location,
                        ui.getCombatTargets()
                );

                // World Hop
                if (ui.tooManyPlayersBtn.isSelected()) {
                    profile.setHopIfTooManyPlayers(true);
                    profile.setHopIfXPlayers(ui.tooManyPlayersSlider.getValue());
                }

                if (ui.stolenTargetsBtn.isSelected()) {
                    profile.setHopIfStolenTargets(true);
                    profile.setHopIfXStolen(ui.stolenTargetsSlider.getValue());
                }

                if (ui.noTargetBtn.isSelected()) {
                    profile.setHopIfNoTargets(true);
                    profile.setHopIfXNotFound(ui.noTargetSlider.getValue());
                }

                if (ui.periodicBtn.isSelected()) {
                    profile.setHopIfTimeHasPassed(true);
                    profile.setHopIfXTime(ui.periodicSlider.getValue());
                }

                // Prayers
                if (!ui.prayerListModel.isEmpty()) {
                    profile.setPrayerList(ui.getModelList(ui.prayerListModel));
                }

                // Loot
                if (!ui.lootListModel.isEmpty()) {
                    profile.setLootList(ui.getModelList(ui.lootListModel));
                }

                // Bank
                if (!ui.bankListModel.isEmpty()) {
                    profile.setBankList(ui.getModelList(ui.bankListModel));
                }

                if (ui.useBankBtn.isSelected()) {
                    profile.setBank((Banks.Location) ui.bankChoice.getSelectedItem());
                    profile.setUseBankFullInv(ui.bankInvFullBtn.isSelected());
                }

                final String json = gson.toJson(profile);

                if (FileManager.write(name, json.getBytes(StandardCharsets.UTF_8))) {
                    final int size = ui.profileChoiceModel.getSize();

                    for (int i = 0; i < size; i++) {
                        if (ui.profileChoiceModel.getElementAt(i).getName().equals(name)) {
                            ui.profileChoice.remove(i);
                            break;
                        }
                    }

                    ui.profileChoiceModel.addElement(profile);
                    ui.profileChoiceModel.setSelectedItem(profile);
                    Effects.shakeComponent(ui.profileChoice);
                }
            } else {
                Effects.shakeComponent(ui.newProfileName);
            }

        });

        ui.loadProfileBtn.addActionListener(e -> {
            final Object o = ui.profileChoiceModel.getSelectedItem();

            if (o instanceof FightProfile) {
                resetSetTargetButton(ui);

                // Has selection
                final FightProfile profile = (FightProfile) o;
                ui.newProfileName.setText(profile.getName());

                // BOOST
                ui.usePotionsBtn.setSelected(profile.isUsePotions());
                BoostPanel.updatePotionText(ui);

                ui.prayerListModel.clear();
                if (profile.getPrayerList() != null)
                    profile.getPrayerList().forEach(ui.prayerListModel::addElement);
                BoostPanel.updateLbl(ui);

                // LOOT
                ui.lootListModel.clear();
                if (profile.getLootList() != null)
                    profile.getLootList().forEach(ui.lootListModel::addElement);
                LootPanel.updateLbl(ui);

                ui.lootInCmb.setSelected(profile.isLootInCombat());
                ui.buryBones.setSelectedIndex(profile.getBuryBones());

                // BANK
                ui.bankListModel.clear();
                if (profile.getBankList() != null)
                    profile.getBankList().forEach(ui.bankListModel::addElement);
                BankPanel.updateLbl(ui);

                final Banks.Location bank = profile.getBank();

                if (bank == null) {
                    ui.bg.clearSelection();

                    ui.bankNoFoodBtn.setEnabled(false);
                    ui.bankOrLbl.setEnabled(false);
                    ui.bankInvFullBtn.setEnabled(false);

                    ui.useBankBtn.setSelected(false);
                } else {
                    ui.useBankBtn.setSelected(true);
                    ui.bankChoice.setSelectedItem(bank);

                    ui.bankNoFoodBtn.setEnabled(true);
                    ui.bankOrLbl.setEnabled(true);
                    ui.bankInvFullBtn.setEnabled(true);

                    if (profile.isUseBankFullInv()) {
                        ui.bankNoFoodBtn.setSelected(false);
                        ui.bankInvFullBtn.setSelected(true);
                    } else {
                        ui.bankNoFoodBtn.setSelected(true);
                        ui.bankInvFullBtn.setSelected(false);
                    }
                }

                // COMBAT
                ui.combatZone.setValue(profile.getDistance());
                CombatPanel.updateDisLbl(ui);

                ui.location = profile.getLocation();
                CombatPanel.updateLocLbl(ui);

                ui.targetListModel.clear();
                profile.getTargetList().forEach(ui.targetListModel::addElement);

                final int i = ui.targetListModel.size();
                ui.combatTargetLbl.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
                ui.combatTargetLbl.setText(i + " Target" + (i == 1 ? "" : "s"));

                ui.setTargetsBtn.setText("Set Target(s)");
                ui.setTargetsBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);

                // WORLD HOP
                ui.tooManyPlayersBtn.setSelected(profile.isHopIfTooManyPlayers());
                ui.tooManyPlayersSlider.setValue(profile.getHopIfXPlayers());

                ui.stolenTargetsBtn.setSelected(profile.isHopIfStolenTargets());
                ui.stolenTargetsSlider.setValue(profile.getHopIfXStolen());

                ui.noTargetBtn.setSelected(profile.isHopIfNoTargets());
                ui.noTargetSlider.setValue(profile.getHopIfXNotFound());

                ui.periodicBtn.setSelected(profile.isHopIfTimeHasPassed());
                ui.periodicSlider.setValue(profile.getHopIfXTime());

            }
        });

        ui.startBtn.setForeground(ColorScheme.BRAND_GREEN);

        ui.startBtn.addActionListener(e -> {
            if (misConfigured(ui)) return;

            togglePanels(ui, false);

            ui.tabs.setSelectedComponent(ui.statPanel);

            ClientUI.toggleUserInput(false);

            resetSetTargetButton(ui);

            ui.script.state = AutoFighter.State.START;

            ui.script.overlay.started = true;
        });
    }

    static void resetSetTargetButton(UserInterface ui) {
        // Force-stop set targets  (users probably will not click button to stop)
        ui.setTargets = false;
        ui.timer.cancelAllTasks();
        ui.setTargetsBtn.setText("Set Target(s)");
    }

    public static void togglePanels(final UserInterface ui, boolean enabled) {
        ui.tabs.setEnabled(enabled);
        for (Component component : ui.startPanel.getComponents()) {
            component.setEnabled(enabled);

            if (component instanceof JPanel) {
                final JPanel panel = (JPanel) component;

                for (Component pc : panel.getComponents()) {
                    pc.setEnabled(enabled);
                }
            }
        }
    }

    private static boolean misConfigured(final UserInterface ui) {
        if (ui.location == null) {
            ui.tabs.setSelectedComponent(ui.combatPanel);
            Effects.shakeComponent(ui.setLocBtn);
            return true;
        }

        if (ui.targetListModel.isEmpty()) {
            ui.tabs.setSelectedComponent(ui.combatPanel);
            Effects.shakeComponent(ui.setTargetsBtn);
            return true;
        }

        return false;
    }
}
