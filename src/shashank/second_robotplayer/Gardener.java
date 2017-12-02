package shashank.second_robotplayer;

import battlecode.common.*;

import java.util.Arrays;

import static shashank.second_robotplayer.RobotPlayer.randomDirection;
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

    private static boolean foundEmptySpot = false;

    public static void loop(){
        while (true){
            try{
                System.out.println("East radians" + Direction.EAST);

                //RobotPlayer.tryMove(RobotPlayer.randomDirection());

                //search for empty spot to take up residence
                if(!foundEmptySpot)
                    findSpot();
                else {
                    //if we have found a spot start planting
                    plantTrees();
                    //maintain the trees around you
                    maintainTrees();
                }

                Clock.yield();
            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }

    private static void findSpot() throws GameActionException {
        /* Ways to find empty spots:
         *  - Just build some lumberjacks to free up some space
         *  - Wander around until we find some empty spots
         *     (Not feasible since it will depend on luck
        */

        // find a direction to build the lumberjack in
        Direction buildDir = randomDirection();

        // First, try intended direction
        if (rc.getBuildCooldownTurns() == 0 && rc.canBuildRobot(RobotType.LUMBERJACK, buildDir) ) {
            rc.canBuildRobot(RobotType.LUMBERJACK, buildDir);
            return;
        }

        int checksPerSide = 180;
        int degreeOffset = 1;
        int currentCheck = 1;

        while ( rc.getBuildCooldownTurns() == 0 && currentCheck<=checksPerSide){

            // Try the offset of the left side
            if(rc.canBuildRobot(RobotType.LUMBERJACK, buildDir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                rc.buildRobot(RobotType.LUMBERJACK, buildDir.rotateLeftDegrees(degreeOffset*currentCheck));
                break;
            }
            // Try the offset on the right side
            if(rc.canBuildRobot(RobotType.LUMBERJACK, buildDir.rotateRightDegrees(degreeOffset*currentCheck))) {
                rc.buildRobot(RobotType.LUMBERJACK, buildDir.rotateRightDegrees(degreeOffset*currentCheck));
                break;
            }
            // No move performed, try slightly further
            currentCheck++;
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

    private static void maintainTrees() throws GameActionException {
        TreeInfo[] allyTreeInfo = rc.senseNearbyTrees(-1, rc.getTeam());

        //organize the trees by least to greatest health first
        if (allyTreeInfo.length >= 2){
            Arrays.sort(allyTreeInfo, (TreeInfo tree1, TreeInfo tree2) -> {
                if(tree1.health < tree2.health)
                    return -1;
                else if (tree1.health > tree2.health)
                    return 1;

                return 0;
            });

            if (rc.canWater() && rc.canWater(allyTreeInfo[0].ID))
                rc.water(allyTreeInfo[0].ID);

        } else if (allyTreeInfo.length == 1){
            if (rc.canWater() && rc.canWater(allyTreeInfo[0].ID))
                rc.water(allyTreeInfo[0].ID);
        }


    }
}
