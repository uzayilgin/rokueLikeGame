/**
 * The main game window class for the Rokue-Like game.
 * Extends JFrame to provide a custom game window with timer support and view management.
 * Includes methods for setting views, managing the timer, and window operations.
 */

package ui.swing;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameWindow extends JFrame {

    private TimerDisplay timerDisplay;

    public GameWindow() {
        super("Rokue-Like");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setUndecorated(false);
        getContentPane().setBackground(new Color(0x4A313C));
        setVisible(true);

//        timerDisplay = new TimerDisplay(30, getState());
    }

    public void setView(JPanel panel) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.setBackground(new Color(0x4A313C));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x7A5937), 3)
        ));

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }
    public void closeW() {
        dispose();
    }

    public void startTimer() {
        timerDisplay.startTimer();
    }

    public void stopTimer() {
        timerDisplay.stopTimer();
    }

    public void updateTimer(int newTime) {
        timerDisplay.setTime(newTime);
    }
}
