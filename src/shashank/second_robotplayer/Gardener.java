package shashank.second_robotplayer;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;

import static shashank.second_robotplayer.RobotPlayer.rc;

/*
 * NOTES:
 * - building a tree has a ten turn cooldown
 * - have 60 degrees between each of the trees
 */

public class Gardener {

    private static Direction NORTH_DIR = new Direction((float) Math.toRadians(120));
    private static Direction NORTHEAST_DIR = new Direction((float) Math.toRadians(60));
    private static Direction SOUTHEAST_DIR = new Direction((float) Math.toRadians(-60));
    private static Direction SOUTH_DIR = new Direction((float) Math.toRadians(-120));

    public static void loop(){
        while (true){
            try{
                System.out.println("East radians" + Direction.EAST);

                //RobotPlayer.tryMove(RobotPlayer.randomDirection());

                //search for empty spot to take up residence

                //if we have found a spot start planting
                plantTrees();
                //maintain the trees around you


                Clock.yield();
            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }

    private static void plantTrees() throws GameActionException {
        //RobotPlayer.tryMove(RobotPlayer.randomDirection());
        if(rc.hasTreeBuildRequirements() && rc.getBuildCooldownTurns() == 0){
            if(rc.canPlantTree(Direction.EAST)){
                rc.plantTree(Direction.EAST);
            } else if(rc.canPlantTree( NORTHEAST_DIR )){
                rc.plantTree( NORTHEAST_DIR );
            } else if(rc.canPlantTree( NORTH_DIR )){
                rc.plantTree( NORTH_DIR );
            } else if(rc.canPlantTree( SOUTHEAST_DIR )){
                rc.plantTree( SOUTHEAST_DIR );
            } else if(rc.canPlantTree( SOUTH_DIR )){
                rc.plantTree( SOUTH_DIR );
            }
        }
    }
}
