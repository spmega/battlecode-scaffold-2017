package shashank.secondplayer;

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
    private int maxGardeners = 5;

    public Archon(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    public void loop(){
        while (true){
            tryMove(randomDirection());
            maintainGardeners();

            Clock.yield();
        }
    }

    private void buildGardener(Direction direction){
        if((direction != null && rc.getBuildCooldownTurns() == 0) && rc.canBuildRobot(RobotType.GARDENER, direction)){
            try {
                rc.buildRobot(RobotType.GARDENER, direction);
            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }

    public void maintainGardeners(){

        int message = 0;

        try {
            message = rc.readBroadcast(10);
        } catch (GameActionException e) {
            e.printStackTrace();
        }

        if(message < maxGardeners){
            //DebugLogger.printInfo("channel " + i + ": " + message);
            if( rc.getBuildCooldownTurns() == 0){
                buildGardener(findOpenDirectionToBuild());
            }
        }
    }

    private Direction findOpenDirectionToBuild() {
        Direction direction = randomDirection();
        while(!rc.canBuildRobot(RobotType.GARDENER, direction)){
            direction = randomDirection();
        }

        return direction;
    }
}
