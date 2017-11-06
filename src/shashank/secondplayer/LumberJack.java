package shashank.secondplayer;

import battlecode.common.*;

public class LumberJack extends BaseRobot{
    private RobotController rc;
    private MapLocation targetArchonLoc = null;
    private MapLocation targetTreeLocation = null;

    public LumberJack(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    void loop() {
        while (true){
            //tryMove(randomDirection());
            chopTrees();
            Clock.yield();
        }
    }

    private void chopTrees() {
        //first check for defenceless archons
        /*
        RobotInfo[] robotInfos = rc.senseNearbyRobots(RobotType.LUMBERJACK.sensorRadius, rc.getTeam());
        MapLocation archonLocation = null;
        for(RobotInfo robotInfo: robotInfos){
            if(robotInfo.type == RobotType.ARCHON){
                archonLocation = robotInfo.location;
            } else if(archonLocation != null){

                for(RobotInfo info: robotInfos){
                    if(robotInfos.length < 2){
                        targetArchonLoc = archonLocation;
                        try {
                            rc.broadcastFloat(5, targetArchonLoc.x);
                            rc.broadcastFloat(6, targetArchonLoc.y);
                        } catch (GameActionException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    break;
                }

                break;
            }
        }
        */

        /*
        if(targetTreeLocation == null){
            MapLocation targetTree = null;
            TreeInfo[] treeInfos = rc.senseNearbyTrees();
            RobotInfo[] friendliesInfos = rc.senseNearbyRobots(RobotType.LUMBERJACK.sensorRadius, rc.getTeam());
            for(TreeInfo treeInfo: treeInfos){
                boolean nextTree = false;

                for(RobotInfo friendlyInfo: friendliesInfos){
                    if(friendlyInfo.type == RobotType.LUMBERJACK
                            && friendlyInfo.location.distanceTo(treeInfo.location) <= 2){

                        nextTree = true;
                        break;
                    }
                }

                if(nextTree) continue;

                if(rc.canChop(treeInfo.location)){
                    try {
                        rc.chop(treeInfo.location );
                    } catch (GameActionException e) {
                        e.printStackTrace();
                    }
                } else {
                    tryMove(treeInfo.location);
                }

                targetTreeLocation = treeInfo.location;

                break;
            }
        } else {
            if(rc.canChop(targetTreeLocation)){
                try {
                    rc.chop(targetTreeLocation);
                } catch (GameActionException e) {
                    e.printStackTrace();
                }
            } else {
                tryMove(targetTreeLocation);
            }
        }*/

        if(targetTreeLocation == null){
            MapLocation targetTree = null;
            TreeInfo[] treeInfos = rc.senseNearbyTrees();
            if(treeInfos.length > 0){
                targetTreeLocation = treeInfos[0].location;
            } else {
                tryMove(randomDirection());
            }

        } else if(rc.senseNearbyTrees(targetTreeLocation, .1F,  Team.NEUTRAL).length == 0){

            targetTreeLocation = null;

        } else {

            if(rc.canChop(targetTreeLocation)){
                try {
                    rc.chop(targetTreeLocation);
                } catch (GameActionException e) {
                    e.printStackTrace();
                }
            } else {
                tryMove(targetTreeLocation);
            }

        }

    }
}
