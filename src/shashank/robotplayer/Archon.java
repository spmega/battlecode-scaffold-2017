package shashank.robotplayer;

import battlecode.common.*;


/*
 *
 * Archon stuff:
 *
 * Archons will maintain a squad of gardeners based on map, ranging from 5 gardeners to 25
 * Archons can find number gardeners dead pr alive by number of broadcasting robots
 * NOTE: Archon will ONLY build gardeners if not under fire
 *
 *
 * Gardeners will broadcast a negative number when less than 10% help, and suicide,
 * after the one of the archons will build the gardeners again
 * 0 means spot is open in squad
 * 1 means alive and (hopefully) well
 *
 * Stuff to do:
 * Determine map size
 * Deploy initial 5 gardeners
 */
public class Archon extends BaseRobot{
    private RobotController rc;
    private int maxGardeners = 7;

    public Archon(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    public void loop(){
        /*
        while (true){
            tryMove(randomDirection());
            maintainGardeners();

            Clock.yield();
        }
        */

        while (true) {
            considerDonating();
            // Try/catch blocks stop unhandled exceptions, which cause your robot to explode
            try {
                BulletInfo[] bulletInfos = rc.senseNearbyBullets();
                for(int i = 0; i < bulletInfos.length; i++){
                    if(bulletWillCollideWithMe(bulletInfos[i])){
                        rc.broadcastFloat(300, rc.getLocation().x);
                        rc.broadcastFloat(301, rc.getLocation().y);
                        break;
                    } else if(i == bulletInfos.length-1){
                        rc.broadcastFloat(300, 0);
                        rc.broadcastFloat(301, 0);
                    }
                }

                int numOfGardeners = rc.readBroadcast(100);

                // Generate a random direction
                Direction dir = randomDirection();

                // Randomly attempt to build a gardener in this direction
                while(rc.getBuildCooldownTurns() == 0 && !rc.canHireGardener(dir)){
                    dir = randomDirection();
                }
                if (rc.canHireGardener(dir) && numOfGardeners <= maxGardeners) {
                    rc.hireGardener(dir);
                }

                // Move randomly
                tryMove(randomDirection());

                // Broadcast archon's location for other robots on the team to know
                MapLocation myLocation = rc.getLocation();
                rc.broadcast(0,(int)myLocation.x);
                rc.broadcast(1,(int)myLocation.y);

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println("Archon Exception");
                e.printStackTrace();
            }
        }
    }
}
