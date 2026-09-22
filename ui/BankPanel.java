package autofighter.ui;

import com.osrsbots.orb.api.interactables.world.locations.Banks;
import com.osrsbots.orb.api.util.ColorScheme;
import com.osrsbots.orb.api.util.Effects;
import autofighter.ui.util.ListCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class BankPanel {


    public static void init(final UserInterface ui) {
        ui.bankLbl.setForeground(ColorScheme.RUSTY_RED);
        ui.addBankBtn.setForeground(ColorScheme.BRAND_GREEN);

        initBanksChoice(ui);

        ui.addBankBtn.addActionListener(e -> {
            final String itemName = ui.bankItemName.getText();

            if (itemName != null && itemName.length() > 2) {
                ui.bankListModel.addElement(itemName);
                ui.bankItemName.setText(null);

                updateLbl(ui);
            } else {
                Effects.shakeComponent(ui.bankItemName);
            }
        });

        ui.bankList.setModel(ui.bankListModel);
        ui.bankList.setCellRenderer(new ListCellRenderer());

        ui.bankList.addListSelectionListener(e -> {
            if (ui.bankList.getSelectedIndex() != -1) {
                ui.removeBankBtn.setForeground(ColorScheme.BRAND_GREEN);
            } else {
                ui.removeBankBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
            }
        });

        final Cursor c = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        ui.bankList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                final Point point = e.getPoint();
                final int index = ui.bankList.locationToIndex(point);

                if (index != -1 && ui.bankList.getCellBounds(index, index).contains(point)) {
                    ui.bankList.setCursor(c);
                } else {
                    ui.bankList.setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        ui.bankList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectedIndex(index0)) {
                    clearSelection();
                } else {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });

        ui.removeBankBtn.addActionListener(e -> {
            final String itemName = ui.bankList.getSelectedValue();

            if (itemName != null && !itemName.isBlank()) {
                final int size = ui.bankListModel.getSize();


                for (int i = 0; i < size; i++) {
                    if (ui.bankListModel.getElementAt(i).equals(itemName)) {
                        ui.bankListModel.removeElementAt(i);
                        ui.removeBankBtn.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
                        updateLbl(ui);
                        break;
                    }
                }
            }
        });

        ui.bg.add(ui.bankNoFoodBtn);
        ui.bg.add(ui.bankInvFullBtn);

        ui.useBankBtn.addActionListener(e -> {
            if (ui.useBankBtn.isSelected()) {
                ui.bankNoFoodBtn.setEnabled(true);
                ui.bankOrLbl.setEnabled(true);
                ui.bankInvFullBtn.setEnabled(true);

                ui.bankNoFoodBtn.setSelected(true);
                ui.bankInvFullBtn.setSelected(false);
            } else {
                ui.bg.clearSelection();

                ui.bankNoFoodBtn.setEnabled(false);
                ui.bankOrLbl.setEnabled(false);
                ui.bankInvFullBtn.setEnabled(false);
            }
        });
    }


    public static void updateLbl(final UserInterface ui) {
        final int i = ui.bankListModel.getSize();
        ui.bankLbl.setText(i + " Item" + (i == 1 ? "" : "s") + " To Withdraw");

        if (i < 1) {
            ui.bankLbl.setForeground(ColorScheme.RUSTY_RED);
        } else {
            ui.bankLbl.setForeground(ColorScheme.DEFAULT_WHITE_COLOR);
        }
    }

    private static void initBanksChoice(final UserInterface ui) {
        DefaultComboBoxModel<Banks.Location> comboModel = new DefaultComboBoxModel<>();

        for (Banks.Location loc : Banks.Location.values()) {
            comboModel.addElement(loc);
        }

        ui.bankChoice.setModel(comboModel);
    }

}
