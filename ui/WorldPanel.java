package autofighter.ui;

import java.text.DecimalFormat;

public class WorldPanel {


    public static void init(final UserInterface ui) {
        // Sliders
        ui.tooManyPlayersSlider.addChangeListener(e -> {
            ui.tooManyPlayersBtn.setText("If " + ui.tooManyPlayersSlider.getValue() + " or more players are nearby");
        });

        ui.stolenTargetsSlider.addChangeListener(e -> {
            ui.stolenTargetsBtn.setText("More than " + ui.stolenTargetsSlider.getValue() + " targets are stolen");
        });

        final DecimalFormat format = new DecimalFormat("##.##");

        ui.noTargetSlider.addChangeListener(e -> {
            final int seconds = ui.noTargetSlider.getValue();
            String time;

            if (seconds <= 60) {
                time = seconds + "s";
            } else {
                // Convert to minutes in ##.## format
                final double minutes = (double) seconds / 60.0;
                time = format.format(minutes) + "m";
            }

            ui.noTargetBtn.setText("Unable to find a target after " + time);
        });

        ui.periodicSlider.addChangeListener(e -> {
            ui.periodicBtn.setText("After about " + ui.periodicSlider.getValue() + "m");
        });

    }

}
