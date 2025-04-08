/**
 * Abstract class representing a game object within the game world.
 * This serves as a base class for all specific types of game objects.
 */
package domain.gameObjects;

import domain.utilities.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.net.URL;

public abstract class GameObject {
    private Point position;
    private String name;
    private String imagePath;
    private String type;
    private ImageIcon icon;
    private int x;
    private int y;
    private boolean isEmpty = true; 

    public GameObject(int x, int y) {
        this.position = new Point(x, y);
    }

    public GameObject(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null.");
        }
        this.position = position;
    }
    //this is for enchanments, they dont have a position sometimes (especially when they are collected by the player)
    public GameObject() {
    }
    /**
     * Constructs a GameObject with a name, image path, and type.
     *
     * @param name      the name of the object
     * @param imagePath the path to the object's image
     * @param type      the type of the object
     */
    public GameObject(String name, String imagePath, String type) {
        this.name = name;
        this.imagePath = imagePath;
        this.type = type;
        this.icon = loadIcon(imagePath);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    /**
     * Loads an image icon from the specified path.
     *
     * @param imagePath the path to the image
     * @return the loaded ImageIcon or null if the image cannot be found
     */
    public ImageIcon loadIcon(String imagePath) {
        try {
            URL imgURL = getClass().getClassLoader().getResource(imagePath);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Image not found: " + imagePath);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Point getPosition() {
        return this.position;
    }
    public void setPosition(int x, int y) {
       this.position.setLocation(x, y);
    }

    public void interact() {
        // nothing is done here, this method will be overridden by the subclasses if needed
    }

    public void update() {}
    public void render(Graphics g) {}
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.EMPTY;
    }
}

