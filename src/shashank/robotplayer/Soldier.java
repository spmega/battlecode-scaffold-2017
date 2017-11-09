package shashank.robotplayer;

import battlecode.common.*;

public class Soldier extends BaseRobot{
    private RobotController rc;

    public Soldier(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    void loop() {
        Team enemy = rc.getTeam().opponent();
        try {
            rc.broadcast(500, rc.readBroadcast(500) + 1);
        } catch (GameActionException e) {
            e.printStackTrace();
        }

        // The code you want your robot to perform every round should be in this loop
        while (true) {

            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                MapLocation myLocation = rc.getLocation();
                //if less than 15% of health, consider spawning more one more soldier
                if(rc.getHealth()/rc.getType().maxHealth < 0.15){
                    rc.broadcast(500, rc.readBroadcast(500) - 1);
                }

                // See if there are any nearby enemy robots
                RobotInfo[] robots = rc.senseNearbyRobots(-1, enemy);

                // If there are some...
                if (robots.length > 0) {
                    // And we have enough bullets, and haven't attacked yet this turn...
                    if (rc.getTeamBullets() > 300 && rc.canFireTriadShot()) {
                        // ...Then fire a bullet in the direction of the enemy.
                        rc.fireTriadShot(rc.getLocation().directionTo(robots[0].location));
                    } else if (rc.canFireSingleShot()){
                        rc.fireSingleShot(rc.getLocation().directionTo(robots[0].location));
                    }

                    rc.broadcastFloat(400, robots[0].location.x);
                    rc.broadcastFloat(401, robots[0].location.y);
                } else {
                    float x = rc.readBroadcastFloat(400);
                    float y = rc.readBroadcastFloat(401);
                    MapLocation possibleEnemyLoc = new MapLocation(x, y);

                    if(x == 0 && y == 0){
                        Direction randDir = randomDirection();
                        while ( !rc.canMove(randDir) ) randDir = randomDirection();
                        tryMove(randDir);

                    } else if(rc.getLocation().distanceTo(possibleEnemyLoc) <= 1 && rc.senseNearbyRobots(-1, rc.getTeam().opponent()).length == 0){
                        rc.broadcastFloat(400, 0);
                        rc.broadcastFloat(401, 0);

                        Direction randDir = randomDirection();
                        while ( !rc.canMove(randDir) ) randDir = randomDirection();
                        tryMove(randDir);
                    } else {
                        tryMove(possibleEnemyLoc);
                    }
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Soldier Exception");
                e.printStackTrace();
            }
        }
    }
}
