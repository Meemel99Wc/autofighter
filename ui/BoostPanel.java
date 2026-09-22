package autofighter.ui;

import autofighter.ui.util.ListCellRenderer;
import com.osrsbots.orb.api.util.ColorScheme;
import com.osrsbots.orb.api.util.Effects;
import net.runelite.api.Prayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Arrays;

public class BoostPanel {


    public static void init(final UserInterface ui) {
        ui.boostLbl.setForeground(ColorScheme.RUSTY_RED);
        ui.usePotionsBtn.setForeground(ColorScheme.RUSTY_RED);
        ui.addPrayerBtn.setForeground(ColorScheme.BRAND_GREEN);

        ui.addPrayerBtn.addActionListener(e -> {
            final String prayerName = ui.prayerNameTxt.getText();

            if (prayerName != null && prayerName.length() > 2) {
                final Prayer prayer = Arrays.stream(
                        Prayer.values()
                ).filter(
                        p -> p.name().replaceAll("_", " ").equalsIgnoreCase(prayerName)
                ).findFirst().orElse(null);

                if (prayer != null) {
                    ui.prayerListModel.addElement(prayer.name().replace("_", " "));
                    ui.prayerNameTxt.setText(null);

                    updateLbl(ui);
                    return;
                }
            }

            Effects.shakeComponent(ui.prayerNameTxt);

        });

        ui.prayerList.setModel(ui.prayerListModel);
        ui.prayerList.setCellRenderer(new ListCellRenderer());

        ui.prayerList.addListSelectionListener(e -> {
            if (ui.prayerList.getSelectedIndex() != -1) {
                ui.removePrayerBtn.setForeground(ColorScheme.BRAND_GREEN);
            } else {
                ui.removePrayerBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
            }
        });

        final Cursor c = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        ui.prayerList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                final Point point = e.getPoint();
                final int index = ui.prayerList.locationToIndex(point);

                if (index != -1 && ui.prayerList.getCellBounds(index, index).contains(point)) {
                    ui.prayerList.setCursor(c);
                } else {
                    ui.prayerList.setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        ui.prayerList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectedIndex(index0)) {
                    clearSelection();
                } else {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });

        ui.removePrayerBtn.addActionListener(e -> {
            final String itemName = ui.prayerList.getSelectedValue();

            if (itemName != null && !itemName.isBlank()) {
                final int size = ui.prayerListModel.getSize();


                for (int i = 0; i < size; i++) {
                    if (ui.prayerListModel.getElementAt(i).equals(itemName)) {
                        ui.prayerListModel.removeElementAt(i);
                        ui.removePrayerBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
                        updateLbl(ui);
                        break;
                    }
                }
            }
        });


        ui.usePotionsBtn.addActionListener(e -> updatePotionText(ui));

    }

    public static void updatePotionText(final UserInterface ui) {
        if (ui.usePotionsBtn.isSelected()) {
            ui.usePotionsBtn.setFont(ui.usePotionsBtn.getFont().deriveFont(Font.PLAIN));
            ui.usePotionsBtn.setText("Enabled");
            ui.usePotionsBtn.setForeground(ColorScheme.BRAND_GREEN);
        } else {
            ui.usePotionsBtn.setFont(ui.usePotionsBtn.getFont().deriveFont(Font.ITALIC));
            ui.usePotionsBtn.setText("Disabled");
            ui.usePotionsBtn.setForeground(ColorScheme.RUSTY_RED);

        }
    }


    public static void updateLbl(final UserInterface ui) {
        final int i = ui.prayerListModel.getSize();
        ui.boostLbl.setText("Using " + i + " Prayer" + (i == 1 ? "" : "s"));

        if (i < 1) {
            ui.boostLbl.setForeground(ColorScheme.RUSTY_RED);
        } else {
            ui.boostLbl.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
        }
    }

}
