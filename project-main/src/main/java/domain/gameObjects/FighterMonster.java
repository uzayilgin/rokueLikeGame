/**
 * The FighterMonster class represents a monster that attacks the player and moves to random locations.
 */
package domain.gameObjects;

import domain.utilities.Constants;
import technicalServices.logging.LogManager;

import java.awt.*;
import java.util.ArrayList;

public class FighterMonster extends Monster{


    public FighterMonster(int x, int y) {
        super(x, y);
    }
    /**
     * Attacks the player if the player is adjacent to the FighterMonster.
     * A stabbing attack reduces the player's life count by 1.
     *
     * @param player the player being attacked
     */
    //if player is right next to the monster, it will attack the player by stabbing
    @Override
    public void attack(Player player) {
        LogManager.logInfo("FighterMonster is stabbing the player. Player Life: " + player.getLifeCount() + "[from class:FighterMonster, method:attack]");
        System.out.println("FighterMonster is stabbing the player. Player Life: " + player.getLifeCount());
        if(Monster.calculateDistance(this.getPosition(),player.getPosition())<=1){
            //I assumed stabbing only takes 1 life
            player.setLifeCount(player.getLifeCount()-1);
        }

    }
    /**
     * Moves the FighterMonster to a random empty location in the hall.
     * The behavior can be influenced by enchantments like the Luring Gem.
     *
     * @param emptyLocations a list of empty locations in the hall
     */
    @Override
    public void move(ArrayList<Point> emptyLocations) {
        //here when we do enchantments, luring gem will change the behaviour of the fighter monster
        this.getPosition().setLocation(Monster.findRandomLocation(emptyLocations));
        LogManager.logInfo("FighterMonster is moving to [x="+ this.getPosition().getX()+",y="+this.getPosition().getY()+"]. [from class:FighterMonster, method:move]");
        System.out.println("FighterMonster is moving to [x="+ this.getPosition().getX()+",y="+this.getPosition().getY()+"].");
    }

    @Override
    public void interact() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        Point pos = getPosition();
        g.fillRect(pos.x * 20, pos.y * 20, 20, 20);
    }
    @Override
    public Constants.GameObjectsInHall getType() {
        return Constants.GameObjectsInHall.FIGHTER;
    }
}
