package shashank.second_robotplayer;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotType;
import static shashank.second_robotplayer.RobotPlayer.rc;

public class Archon {

    public static void loop(){

        while (true){
            try{
                RobotPlayer.tryMove(RobotPlayer.randomDirection());

                if(Math.random() < 0.3){
                    System.out.print("Archon " + rc.getID() + " is building");

                    RobotPlayer.CheckConditionForDirection condition = (Direction direction) -> {
                        return rc.canBuildRobot(RobotType.GARDENER, direction);
                    };

                    Direction direction = RobotPlayer.mustGetRandDir(condition, 1, 180);
                    if(direction != null)
                        rc.buildRobot(RobotType.GARDENER, direction);
                }

                Clock.yield();

            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }
}
