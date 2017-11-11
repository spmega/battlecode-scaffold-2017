package shashank.robotplayer;

import battlecode.common.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

public class Gardener extends BaseRobot{
    private RobotController rc;
    private boolean foundEmptySpot = false;
    private int roundNum = 0;

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
            considerDonating();

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                roundNum = rc.getRoundNum();

                if(aboutToDie()){
                    rc.broadcast(100, rc.readBroadcast(100) - 1);
                    rc.disintegrate();
                }

                //reserve an empty spot
                findEmptySpot();

                if(!foundEmptySpot){
                    moveRandomly();
                    buildRobots();
                }

                //we found an empty spot!
                if(foundEmptySpot) {
                    keepCircleOfTrees();
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

    private void moveRandomly(){
        // Move randomly
        Direction randomDir = randomDirection();
        Direction originalRandDir = randomDir;
        boolean canMove = true;

        while (!rc.hasMoved() && !rc.canMove(randomDir)) {

            randomDir = randomDir.rotateLeftDegrees(5);

            if(originalRandDir.equals(randomDir, 0.0349066F)){
                //give up on moving
                canMove = false;
                break;
            }
        }

        if(canMove && !rc.hasMoved()) try {
            rc.move(randomDir);
        } catch (GameActionException e) {
            DebugLogger.printError(e);
        }
    }

    private void findEmptySpot(){
        try {
            //if the robot has not started to plant yet, look for opportunities
            if(!foundEmptySpot) {
                //if we found a spot, plant a bunch of trees and stop moving
                //float circleRadius = rc.getType().bodyRadius + 2*GameConstants.BULLET_TREE_RADIUS;
                float circleRadius = 5.0F;
                MapLocation myLoc = rc.getLocation();

                //check if we are in an empty area
                if (!rc.isCircleOccupiedExceptByThisRobot(myLoc, circleRadius) && rc.onTheMap(myLoc, circleRadius)) {

                    //start planting
                    if (rc.canPlantTree(Direction.NORTH)) {

                        rc.plantTree(Direction.NORTH);
                    }

                    //move on to next stage of the code
                    foundEmptySpot = true;
                } else {
                    //keep searching for an empty spot
                    foundEmptySpot = false;
                }

            }
        } catch (GameActionException e) {
            e.printStackTrace();
        }
    }

    private void buildRobots(){
        try {
            //keep building units
            RobotType typeToBuild = null;
            if(rc.getBuildCooldownTurns() == 0){
                // See whether there are less soldiers or less lumberjacks and build that
                int numOfSoldiers = rc.readBroadcast(500);
                int numOfLumberjacks = rc.readBroadcast(600);

                float percentOfSoldiersBuilt = numOfSoldiers/20F;
                float percentOfLumberJacksBuilt = numOfLumberjacks/30F;

                if(percentOfSoldiersBuilt < percentOfLumberJacksBuilt && percentOfSoldiersBuilt <= 1F){
                    typeToBuild = RobotType.SOLDIER;
                } else if (percentOfLumberJacksBuilt <= 1F) {
                    typeToBuild = RobotType.LUMBERJACK;
                }
            }

            if(typeToBuild != null){

                Direction randomDir = randomDirection();
                Direction originalRandDir = randomDir;
                boolean canBuild = true;

                while (!rc.canBuildRobot(typeToBuild, randomDir)) {

                    randomDir = randomDir.rotateLeftDegrees(5F);

                    if(originalRandDir.equals(randomDir, 0.0174533F)){
                        //give up on moving
                        canBuild = false;
                        break;
                    }
                }

                if(canBuild) {
                    rc.buildRobot(typeToBuild, randomDir);
                }

            }

        } catch (GameActionException e) {
            e.printStackTrace();
        }
    }

    private void keepCircleOfTrees(){
        try {
            //once we settled, maintain the trees around us, and plant trees in the empty area around us

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

            //plant trees around us if there empty area around us
            if(rc.canPlantTree(Direction.NORTH)){
                rc.plantTree(Direction.NORTH);
            } else if(rc.canPlantTree(Direction.EAST)){
                rc.plantTree(Direction.EAST);
            } else if(rc.canPlantTree(Direction.SOUTH)){
                rc.plantTree(Direction.SOUTH);
            }
            //keep spawning units if we have sufficient trees around us
            else if(rc.getBuildCooldownTurns() == 0){
                // See whether there are less soldiers or less lumberjacks and build that
                buildRobots();
            }
        } catch (GameActionException e) {
            e.printStackTrace();
        }
    }
}
