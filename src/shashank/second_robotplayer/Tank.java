package shashank.second_robotplayer;

import battlecode.common.GameActionException;

public class Tank {

    public static void loop(){
        while (true){
            try{
                RobotPlayer.tryMove(RobotPlayer.randomDirection());

            } catch (GameActionException e) {
                e.printStackTrace();
            }
        }
    }
}
