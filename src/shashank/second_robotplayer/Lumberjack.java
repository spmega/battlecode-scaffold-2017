package shashank.second_robotplayer;

import battlecode.common.*;

import static shashank.second_robotplayer.RobotPlayer.randomDirection;
import static shashank.second_robotplayer.RobotPlayer.rc;
import static shashank.second_robotplayer.RobotPlayer.tryMove;

public class Lumberjack {

    public static void loop(){
        while (true){
            try{
                //RobotPlayer.tryMove(RobotPlayer.randomDirection());

                cutDownTrees();

            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }

    private static void cutDownTrees() throws GameActionException {

        MapLocation targetTree = getTargetTree();

        if (targetTree != null){
            if(rc.canChop(targetTree)){
                rc.chop(targetTree);
            } else if (rc.canMove(targetTree)){
                rc.move(targetTree);
            }
        } else {
            tryMove(randomDirection());
        }
    }

    private static MapLocation getTargetTree() {
        RobotInfo[] nearbyAllyRobots = rc.senseNearbyRobots(-1, rc.getTeam());
        TreeInfo[] nearbyTrees = rc.senseNearbyTrees(-1, Team.NEUTRAL);

        if(nearbyTrees.length > 0)
            return nearbyTrees[0].location;

        for (TreeInfo neutralTree: nearbyTrees){
            boolean validTreeLoc = true;
            int i = 0;
            for ( ; i < nearbyAllyRobots.length; i++){
                //make sure its a lumberjack thats near the tree
                if(nearbyAllyRobots[i].type != RobotType.LUMBERJACK)
                    continue;

                MapLocation robotLoc = nearbyAllyRobots[i].location;
                //check if this tree is already being cut down
                float dist = robotLoc.distanceTo(neutralTree.location);
                if(dist-neutralTree.radius < 2){
                    validTreeLoc = false;
                    break;
                }

            }

            if(validTreeLoc && i == nearbyAllyRobots.length - 1)
                return neutralTree.location;
        }


        return null;
    }

}
