/**
 * Represents a success mini-screen displayed upon game completion.
 * Features a fade-in animation with a background and a congratulatory message.
 * Allows the user to return to the main menu.
 * Implements `GameView` for integration with the game's UI system.
 */

package ui.swing;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class SuccessMiniScreen implements GameView {
   private JPanel overlayPanel;
   private JPanel contentPanel;
   private JButton mainMenuButton;
   private float opacity = 0.0f;
   private Timer fadeInTimer;
   private final int fade_step = 50; // time interval for each step in fade-in animation
   private final float fade_opacity_step= 0.01f; // delta value for opacity increase per step


   public SuccessMiniScreen() {
   }


   public void initialize() {
       ImageIcon backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("assets/images/gameSuccessBg.jpeg"));
       // semi-transparent shading overlay panel to appear on top of the current play mode screen + adds content panel
       overlayPanel = new JPanel() {
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               Graphics2D g2d = (Graphics2D) g;
               g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));


               if (opacity < 1.0f) {
                   g2d.setColor(Color.BLACK);
                   g2d.fillRect(0, 0, getWidth(), getHeight());
               } else {
                   Image img = backgroundImage.getImage();
                   g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
               }
           }
       };
       overlayPanel.setLayout(new GridBagLayout());
       overlayPanel.setOpaque(false);


       contentPanel = new JPanel();
       contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
       contentPanel.setBackground(new Color(0, 0, 0, 0));
       contentPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));


       ImageIcon gameOverImage = new ImageIcon(getClass().getResource("/assets/images/youwin.png"));


       Image img = gameOverImage.getImage();
       JLabel gameOverLabel = new JLabel(new ImageIcon(img));
       gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


       mainMenuButton = new JButton("Return to Main Menu");

       contentPanel.add(gameOverLabel);
       mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
       contentPanel.add(mainMenuButton);
       contentPanel.add(Box.createVerticalStrut(10));


       contentPanel.add(Box.createVerticalGlue());
       contentPanel.add(Box.createVerticalStrut(20));


       overlayPanel.add(contentPanel);


       contentPanel.setVisible(false);
   }


   @Override
   public void render() {
       overlayPanel.revalidate();
       overlayPanel.repaint();
   }


   public void teardown() {
       if (mainMenuButton != null) {
           for (ActionListener listener : mainMenuButton.getActionListeners()) {
               mainMenuButton.removeActionListener(listener);
           }
       }
   }


   public JPanel getPanel() {
       return overlayPanel;
   }


   public void setMainMenuAction(ActionListener listener) {
       mainMenuButton.addActionListener(listener);
   }

   public void onGameSuccess() {
       fadeInOverlay();
   }


   // animation: fade effect until background image is revealed
   private void fadeInOverlay() {
       fadeInTimer = new Timer(fade_step, e -> {
           opacity += fade_opacity_step;
           if (opacity >= 1.0f) {
               opacity = 1.0f;
               fadeInTimer.stop();
               showContentPanel();
           }
           overlayPanel.repaint();
       });
       fadeInTimer.start();
   }


   private void showContentPanel() {
       contentPanel.setVisible(true);
       contentPanel.repaint();
   }
}


