package shashank.secondplayer;

import battlecode.common.*;

public class Gardener extends BaseRobot{
    private RobotController rc;
    private boolean settledDown = false;
    private boolean firstGardener = false;

    public Gardener(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    void loop() {

        try {
            int numOfGardeners = rc.readBroadcast(10);
            if(numOfGardeners == 0) firstGardener = true;
            rc.broadcast(10, numOfGardeners + 1);
        } catch (GameActionException e) {
            e.printStackTrace();
        }

        while (true){
            try {

                if(settledDown){
                    maintainTrees();
                } else if(!settledDown && !rc.isCircleOccupiedExceptByThisRobot(rc.getLocation(), 3) && rc.onTheMap(rc.getLocation(), 3)){
                    rc.plantTree(new Direction((float) Math.toRadians(135)));
                    rc.plantTree(new Direction((float) Math.toRadians(90)));
                    rc.plantTree(new Direction((float) Math.toRadians(45)));
                    rc.plantTree(new Direction((float) Math.toRadians(0)));
                    rc.plantTree(new Direction((float) Math.toRadians(315)));
                    rc.plantTree(new Direction((float) Math.toRadians(270)));
                    rc.plantTree(new Direction((float) Math.toRadians(240)));
                    settledDown = true;
                } else {
                    tryMove(randomDirection());

                    TreeInfo[] treeInfos = rc.senseNearbyTrees(5);

                    if(treeInfos.length > 0 & rc.getTeamBullets() > 100){
                        build(RobotType.LUMBERJACK);
                    }
                }

            } catch (GameActionException e) {
                e.printStackTrace();
            }
                /*
                if((rc.getHealth()/rc.getType().maxHealth) < 0.1){
                    rc.broadcast(10, rc.readBroadcast(10) - 1);
                    rc.disintegrate();
                }*/

                /*
                if (!settledDown){
                    settleDown();
                } else {
                    maintainTrees();
                }
                */

            Clock.yield();
        }

    }

    private void maintainTrees() {
        TreeInfo[] treeInfos = rc.senseNearbyTrees(3    , rc.getTeam());
        for(TreeInfo treeInfo: treeInfos){
            if(treeInfo.health < 46){
                if(rc.canWater(treeInfo.location)){
                    try {
                        rc.water(treeInfo.location);
                    } catch (GameActionException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
    }

    private void settleDown() {
        //first check if you are the first gardener
        if(firstGardener){
            //find an empty spot
            //if there are trees everywhere, then release a couple of lumberjacks
            TreeInfo[] treeInfos = rc.senseNearbyTrees();
            if(treeInfos.length > 0){
                for(TreeInfo treeInfo: treeInfos){
                    build(RobotType.LUMBERJACK, treeInfo.location.directionTo(rc.getLocation()) );
                }
            }

            //find a free space
            try {
                if(!rc.isCircleOccupiedExceptByThisRobot(rc.getLocation(), 2) && rc.onTheMap(rc.getLocation(), 3)){
                    rc.plantTree(new Direction((float) Math.toRadians(135)));
                    rc.plantTree(new Direction((float) Math.toRadians(90)));
                    rc.plantTree(new Direction((float) Math.toRadians(45)));
                    rc.plantTree(new Direction((float) Math.toRadians(0)));
                    rc.plantTree(new Direction((float) Math.toRadians(315)));
                    rc.plantTree(new Direction((float) Math.toRadians(270)));
                    rc.plantTree(new Direction((float) Math.toRadians(240)));
                    settledDown = true;
                } else {
                    tryMove(randomDirection());
                }
            } catch (GameActionException e) {
                e.printStackTrace();
            }
        } else {
            tryMove(randomDirection());
        }

    }

    private boolean build(RobotType type, Direction direction){
        if(type != null && direction != null){
            if(rc.canBuildRobot(type, direction)){
                try {
                    rc.buildRobot(type, direction);
                    return true;
                } catch (GameActionException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void build(RobotType type){
        while( !build(type, randomDirection()) ){
            //keep looping until you can build a robot
        }
    }
}
