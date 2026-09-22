package autofighter.ui.util;

import com.osrsbots.orb.api.util.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class ListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ((JLabel) c).setText((value).toString());

        // Set alternating background colors
        if (isSelected) {
            c.setBackground(ColorScheme.DARK_SLATE_BLUE);
        } else if (index % 2 == 0) {
            c.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        } else {
            c.setBackground(ColorScheme.DARK_GRAY_COLOR);
        }

        ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
        return c;
    }
}