/**
 * A UI component that displays a countdown timer for the game.
 * Handles the display of remaining time and updates in real time.
 * Notifies listeners when the timer reaches zero.
 */

package ui.swing;

import domain.gameCore.GameSession;
import domain.gameCore.GameState;
import domain.gameObjects.Hall;
import domain.threads.CustomTimer;

import javax.swing.*;
import java.awt.*;

public class TimerDisplay {
    private JPanel timerPanel;
    private int remainingTime;  // Remaining time for the game (in seconds)
    private Hall hall;
    private CustomTimer timer;  // Swing timer to update the display periodically
    private JLabel timerLabel;
    private TimerListener listener;
    private GameState gameState;
    public TimerDisplay(int initialTime, GameState gameState) {
        this.remainingTime = initialTime;
        this.gameState = gameState;
        this.hall = gameState.getHall();
        // Create the panel to display the timer
        timerPanel = new JPanel();
        timerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));  // Position to the right
        timerPanel.setPreferredSize(new Dimension(200, 50));  // Customize size of the timer panel

        // Create the label to show the time
        timerLabel = new JLabel(formatTime(remainingTime));
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));  // Set font size
        timerLabel.setForeground(Color.RED);  // Set the color of the text
        timerPanel.add(timerLabel);

        // Set up the timer to update every second
        timer = new CustomTimer(initialTime, 1000, () -> updateTime());
        gameState.setTimer(timer);

    }

    public JPanel getPanel() {
        return timerPanel;
    }

    // Start the countdown timer
    public void startTimer() {
        if (!timer.isRunning()) {
            timer._continue();
        }
        timer.start();
    }

    public void stopTimer() {
        System.out.println("Stopping timer...");
        timer.stop();
    }

    public void kill() {
        timer.kill();
        this.remainingTime = 0;
    }

    // Update the remaining time and update the label
    private void updateTime() {
        if (gameState.getPlayer().getLifeCount() == 0) {
            kill();
        }
        if (timer.getTimeRemaining() > 0) {
            remainingTime--;
            hall.setTimeRemaining(remainingTime);
            timerLabel.setText(formatTime(remainingTime));  // Update label with the formatted time
        } else {
            stopTimer();  // Stop the timer when time runs out
            if (listener != null) {
                listener.onTimeUp();  // Notify the listener that the time is up
            }
            System.out.println("Time's up!");
        }
    }

    // Format the time into MM:SS format
    private String formatTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Update the timer with a new value (in case the time changes during the game)
    public void setTime(int newTime) {
        this.remainingTime = newTime;
        timer.setTimeRemaining(newTime);
        timerLabel.setText(formatTime(timer.getTimeRemaining()));  // Update label immediately
    }

    public void setListener(TimerListener listener) {
        this.listener = listener;
        timer.setListener(listener);
    }

    // Timer listener interface
    public interface TimerListener {
        void onTimeUp();
    }

}

