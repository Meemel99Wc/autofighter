package autofighter.ui;

import com.osrsbots.orb.api.util.ColorScheme;
import com.osrsbots.orb.api.util.Effects;
import autofighter.ui.util.ListCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class LootPanel {


    public static void init(final UserInterface ui) {
        ui.lootLbl.setForeground(ColorScheme.RUSTY_RED);
        ui.addLootBtn.setForeground(ColorScheme.BRAND_GREEN);
        ui.lootList.setModel(ui.lootListModel);

        ui.addLootBtn.addActionListener(e -> {
            final String itemName = ui.lootItemName.getText();

            if (itemName != null && itemName.length() > 2) {
                ui.lootListModel.addElement(itemName);
                ui.lootItemName.setText(null);

                updateLbl(ui);
            } else {
                Effects.shakeComponent(ui.lootItemName);
            }
        });

        ui.lootList.setCellRenderer(new ListCellRenderer());

        ui.lootList.addListSelectionListener(e -> {
            if (ui.lootList.getSelectedIndex() != -1) {
                ui.removeLootBtn.setForeground(ColorScheme.BRAND_GREEN);
            } else {
                ui.removeLootBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
            }
        });

        final Cursor c = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        ui.lootList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                final Point point = e.getPoint();
                final int index = ui.lootList.locationToIndex(point);

                if (index != -1 && ui.lootList.getCellBounds(index, index).contains(point)) {
                    ui.lootList.setCursor(c);
                } else {
                    ui.lootList.setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        ui.lootList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectedIndex(index0)) {
                    clearSelection();
                } else {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });

        ui.removeLootBtn.addActionListener(e -> {
            final String itemName = ui.lootList.getSelectedValue();

            if (itemName != null && !itemName.isBlank()) {
                final int size = ui.lootListModel.getSize();


                for (int i = 0; i < size; i++) {
                    if (ui.lootListModel.getElementAt(i).equals(itemName)) {
                        ui.lootListModel.removeElementAt(i);
                        ui.removeLootBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
                        updateLbl(ui);
                        break;
                    }
                }
            }
        });
    }

    public static void updateLbl(final UserInterface ui) {
        final int i = ui.lootListModel.getSize();
        ui.lootLbl.setText(i + " Lootable" + (i == 1 ? "" : "s"));

        if (i < 1) {
            ui.lootLbl.setForeground(ColorScheme.RUSTY_RED);
        } else {
            ui.lootLbl.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
        }
    }
}
