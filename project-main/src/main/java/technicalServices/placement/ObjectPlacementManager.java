/**
 * Manages the placement of objects in a grid-like environment, tracking their positions and counts.
 * Ensures placement constraints are respected and provides utilities for managing object placement.
 */

package technicalServices.placement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectPlacementManager {
    private Map<String, Integer> objectCounts;
    private Map<Point, String> placedObjects;
    private int requiredObjects;

    public ObjectPlacementManager(int requiredObjects) {
        this.requiredObjects = requiredObjects;
        this.objectCounts = new HashMap<>();
        this.placedObjects = new HashMap<>();
    }

    public int getRequiredObjects() {
        return requiredObjects;
    }

    public boolean placeObject(Point point, String objectName) {
        if (placedObjects.containsKey(point)) {
            return false;
        }

        placedObjects.put(point, objectName);
        objectCounts.put(objectName, objectCounts.getOrDefault(objectName, 0) + 1);
        return true;
    }

    public boolean removeObject(Point point) {
        if (!placedObjects.containsKey(point)) {
            return false;
        }

        String objectName = placedObjects.remove(point);
        objectCounts.put(objectName, objectCounts.get(objectName) - 1);
        return true;
    }

    public boolean isValidPlacement() {
        return placedObjects.size() >= requiredObjects;
    }

    public int getPlacedObjectCount() {
        return placedObjects.size();
    }

    public int getObjectCount(String objectName) {
        return objectCounts.getOrDefault(objectName, 0);
    }

    public boolean canPlaceObject(String objectName) {
        return getObjectCount(objectName) < requiredObjects;
    }
}

