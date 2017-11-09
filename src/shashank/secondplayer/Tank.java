package shashank.secondplayer;

import battlecode.common.Clock;
import battlecode.common.RobotController;

public class Tank extends BaseRobot{
    private RobotController rc;

    public Tank(RobotController rc) {
        super(rc);
        this.rc = rc;
    }

    @Override
    void loop() {
        while (true){
            tryMove(randomDirection());

            Clock.yield();
        }
    }
}
