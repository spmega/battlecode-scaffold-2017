package shashank.secondplayer;

import battlecode.common.*;

import java.util.Arrays;

public class LumberJack extends BaseRobot{
    private RobotController rc;

    public LumberJack(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    void loop() {
        Team enemy = rc.getTeam().opponent();

        try {
            rc.broadcast(600, rc.readBroadcast(500) + 1);
        } catch (GameActionException e) {
            e.printStackTrace();
        }

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                //if less than 15% of health, consider spawning more one more soldier
                if(rc.getHealth()/rc.getType().maxHealth < 0.15){
                    rc.broadcast(600, rc.readBroadcast(500) - 1);
                }

                //read the broadcast, in case you dont find any trees
                /*
                float x = rc.readBroadcastFloat(200);
                float y = rc.readBroadcastFloat(201);
                MapLocation possibleTreeLoc = new MapLocation(x, y);

                //check if we are in the area
                if(rc.canSenseLocation(possibleTreeLoc)){
                    //then check if a tree really is there
                    TreeInfo possibleTreeInfo = rc.senseTreeAtLocation(possibleTreeLoc);
                    if(possibleTreeInfo == null){
                        possibleTreeLoc = null;
                    } else {
                        possibleTreeLoc = possibleTreeInfo.location;
                    }
                } else if(x == 0 && y == 0){
                    possibleTreeLoc = null;
                }
                */

                // See if there are any enemy robots within striking range (distance 1 from lumberjack's radius)
                RobotInfo[] robots = rc.senseNearbyRobots(RobotType.LUMBERJACK.bodyRadius+GameConstants.LUMBERJACK_STRIKE_RADIUS, enemy);

                if(robots.length > 0 && !rc.hasAttacked()) {
                    // Use strike() to hit all nearby robots!
                    rc.strike();
                } else {
                    // No close robots, so search for robots within sight radius
                    robots = rc.senseNearbyRobots(-1,enemy);

                    // If there is a robot, move towards it
                    if(robots.length > 0) {
                        MapLocation myLocation = rc.getLocation();
                        MapLocation enemyLocation = robots[0].getLocation();
                        Direction toEnemy = myLocation.directionTo(enemyLocation);

                        tryMove(toEnemy);

                        rc.broadcastFloat(400, enemyLocation.x);
                        rc.broadcastFloat(401, enemyLocation.y);
                    } else {
                        // Move Randomly
                        //tryMove(randomDirection());

                        //Since there are no robots, search for trees
                        TreeInfo[] treeInfos = rc.senseNearbyTrees(-1);
                        TreeInfo tree = null;

                        for(TreeInfo treeInfo: treeInfos){
                            //if the tree is not ours, chop it
                            if(!treeInfo.team.equals( rc.getTeam() ) ){
                                tree = treeInfo;
                                break;
                            }
                        }

                        if(tree != null){
                            // if the a tree is close by, then chop it
                            if(rc.canChop(tree.location)){
                                rc.chop(tree.location);
                            } else {
                                //move toward the nearest tree
                                if(rc.canMove(tree.location)){
                                    rc.move(tree.location);
                                } else {
                                    for(TreeInfo treeInfo: treeInfos){
                                        //if the tree is not ours, chop it
                                        if( rc.canMove(treeInfo.location)){
                                            rc.move(treeInfo.location);
                                            break;
                                        }
                                    }
                                }
                            }

                            // notify others of the location of the tree
                            // only if the broadcasted location has nothing
                            /*
                            if(possibleTreeLoc == null){
                                rc.broadcastFloat(200, tree.location.x);
                                rc.broadcastFloat(201, tree.location.y);
                            }
                            */

                        } else {
                            //if there are no trees sensed, move randomly
                            //tryMove(randomDirection());

                            //if there are no trees sensed, the try to move towards a tree that does exist
                            /*
                            if(possibleTreeLoc != null){
                                tryMove(possibleTreeLoc);
                            } else {
                                //if there are no trees available, then just move
                                tryMove(randomDirection());
                            }
                            */

                            Direction randDir = randomDirection();
                            while ( !rc.canMove(randDir) ) randDir = randomDirection();

                            tryMove(randDir);
                        }
                    }
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Lumberjack Exception");
                e.printStackTrace();
            }
        }
    }
}
