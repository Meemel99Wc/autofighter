package autofighter.ui;

import com.osrsbots.orb.api.interactables.entities.Player;
import com.osrsbots.orb.api.interactables.types.RSPlayer;
import com.osrsbots.orb.api.util.ColorScheme;
import net.runelite.api.ChatMessageType;
import autofighter.ui.util.ListCellRenderer;
import autofighter.ui.util.NoSelectionModel;

import javax.swing.*;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class CombatPanel {


    public static void init(final UserInterface ui) {
        ui.setLocBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
        ui.setTargetsBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);

        ui.combatLocLbl.setForeground(ColorScheme.RUSTY_RED);
        ui.combatTargetLbl.setForeground(ColorScheme.RUSTY_RED);

        ui.targetsList.setSelectionModel(new NoSelectionModel());
        ui.targetsList.setModel(ui.targetListModel);
        ui.targetsList.setCellRenderer(new ListCellRenderer());

        ui.setLocBtn.addActionListener(e -> {
            final RSPlayer rsp = Player.get();
            if (rsp != null) {
                ui.location = rsp.getWorldLocation();
               updateLocLbl(ui);
            }
        });

        ui.combatZone.addChangeListener(e -> updateDisLbl(ui));

        ui.clearTargetsBtn.addActionListener(e -> {
            ui.targetListModel.clear();

            ui.combatTargetLbl.setForeground(ColorScheme.RUSTY_RED);
            ui.combatTargetLbl.setText("No Targets");
        });

        ui.setTargetsBtn.addActionListener(e -> {
            if (ui.setTargets) {
                StartPanel.resetSetTargetButton(ui);
                return;
            }

            Player.sendMessage(ChatMessageType.BROADCAST, "ORB", "Left-Click Desired Target(s)");
            ui.setTargetsBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
            ui.setTargets = true;

            final AtomicInteger dotCount = new AtomicInteger(0);
            final AtomicInteger msgCycle = new AtomicInteger(0);

            ui.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int c = msgCycle.get();
                    String msg;

                    switch (c / 12) {
                        case 1:
                            msg = "Click Here To Stop";
                            break;
                        case 0:
                        default:
                            msg = "Left-Click Desired Target(s)";
                            break;
                    }

                    final int i = (dotCount.get() + 1) % 4;
                    dotCount.set(i);

                    SwingUtilities.invokeLater(() -> {
                        ui.setTargetsBtn.setText(msg + ".".repeat(i));
                    });

                    if (c++ >= 48) {
                        c = 0;
                        dotCount.set(0);
                    }

                    msgCycle.set(c);
                }
            }, 0, 500);  // Start immediately, repeat every 500ms
        });
    }

    public static void updateDisLbl(UserInterface ui) {
        ui.combatZoneLbl.setText("Combat Zone - " + ui.combatZone.getValue() + " Tiles");
    }

    public static void updateLocLbl(UserInterface ui) {
        ui.combatLocLbl.setText(
                "X: " + ui.location.getX() + " / Y: " + ui.location.getY() + " / Z: " + ui.location.getPlane()
        );
    }
}
