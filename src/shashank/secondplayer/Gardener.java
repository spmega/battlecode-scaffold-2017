package shashank.secondplayer;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Comparator;

public class Gardener extends BaseRobot{
    private RobotController rc;
    private boolean foundEmptySpot = false;

    public Gardener(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    void loop() {

        try {
            rc.broadcast(100, rc.readBroadcast(100) + 1);
        } catch (GameActionException e) {
            e.printStackTrace();
        }

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                if(aboutToDie()){
                    rc.broadcast(100, rc.readBroadcast(100) - 1);
                    rc.disintegrate();
                }

                //is the robot has not started to plant yet, look for opportunities
                if(!foundEmptySpot) {
                    //if we found a spot, plant a bunch of trees and stop moving
                    float circleRadius = rc.getType().bodyRadius + 2*GameConstants.BULLET_TREE_RADIUS;
                    if (!rc.isCircleOccupiedExceptByThisRobot(rc.getLocation(), circleRadius)
                            && rc.onTheMap(rc.getLocation(), circleRadius)) {
                        if (rc.canPlantTree(Direction.NORTH)) {
                            rc.plantTree(Direction.NORTH);
                        }

                        foundEmptySpot = true;
                    }

                    Direction randomDir = randomDirection();
                    while (!rc.hasMoved() && !rc.canMove(randomDir)) randomDir = randomDirection();

                    tryMove(randomDir);
                }

                if(foundEmptySpot){
                    //once we settled, maintain the trees around us

                    //sort the array by the putting the trees with less health first
                    TreeInfo[] teamTrees = rc.senseNearbyTrees(-1, rc.getTeam());
                    Arrays.sort(teamTrees, new Comparator<TreeInfo>() {
                        @Override
                        public int compare(TreeInfo o1, TreeInfo o2) {
                            return o1.health == o2.health ? 0 : (o1.health < o2.health ? -1 : 1);
                        }
                    });

                    if(teamTrees.length > 0){
                        //if we havent watered a tree yet
                        if(rc.canWater(teamTrees[0].ID)){
                            //then water it
                            rc.water(teamTrees[0].ID);
                        }
                    }


                    if(rc.canPlantTree(Direction.NORTH)){
                        rc.plantTree(Direction.NORTH);
                    } else if(rc.canPlantTree(Direction.EAST)){
                        rc.plantTree(Direction.EAST);
                    } else if(rc.canPlantTree(Direction.SOUTH)){
                        rc.plantTree(Direction.SOUTH);
                    }

                    //keep spawning units

                    if(rc.getBuildCooldownTurns() == 0){
                        // See whether there are less soldiers or less lumberjacks and build that
                        double percentOfSoldiersBuilt = rc.readBroadcast(500)/20F;
                        double percentOfLumberJacksBuilt = rc.readBroadcast(600)/30F;

                        if(percentOfSoldiersBuilt < percentOfLumberJacksBuilt && percentOfSoldiersBuilt <= 1){
                            Direction rndDir = randomDirection();
                            while ( !rc.canBuildRobot(RobotType.SOLDIER, rndDir) ) rndDir = randomDirection();
                            rc.buildRobot(RobotType.SOLDIER, rndDir);
                        } else if (percentOfLumberJacksBuilt <= 1) {
                            Direction rndDir = randomDirection();
                            while ( !rc.canBuildRobot(RobotType.LUMBERJACK, rndDir) ) rndDir = randomDirection();
                            rc.buildRobot(RobotType.LUMBERJACK, rndDir);
                        }
                    }


                } else {
                    //if we havent settled down yet, then we keep spawning and moving

                    // Listen for home archon's location
                    int xPos = rc.readBroadcast(0);
                    int yPos = rc.readBroadcast(1);
                    MapLocation archonLoc = new MapLocation(xPos,yPos);

                    if(rc.getBuildCooldownTurns() == 0){
                        // See whether there are less soldiers or less lumberjacks and build that
                        double percentOfSoldiersBuilt = rc.readBroadcast(500)/20F;
                        double percentOfLumberJacksBuilt = rc.readBroadcast(600)/30F;

                        if(percentOfSoldiersBuilt < percentOfLumberJacksBuilt && percentOfSoldiersBuilt <= 1){
                            Direction rndDir = randomDirection();
                            while ( !rc.canBuildRobot(RobotType.SOLDIER, rndDir) ) rndDir = randomDirection();
                            rc.buildRobot(RobotType.SOLDIER, rndDir);
                        } else if (percentOfLumberJacksBuilt <= 1) {
                            Direction rndDir = randomDirection();
                            while ( !rc.canBuildRobot(RobotType.LUMBERJACK, rndDir) ) rndDir = randomDirection();
                            rc.buildRobot(RobotType.LUMBERJACK, rndDir);
                        }
                    }


                    // Move randomly
                    Direction randomDir = randomDirection();
                    while (!rc.hasMoved() && !rc.canMove(randomDir)) randomDir = randomDirection();

                    tryMove(randomDir);
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Gardener Exception");
                e.printStackTrace();
            }
        }

    }

    private boolean aboutToDie() {
        return rc.getHealth() < 7;
    }
}
