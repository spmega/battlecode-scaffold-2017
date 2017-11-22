package shashank.second_robotplayer;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Broadcaster {
    private RobotController rc = RobotPlayer.rc;

    // max 3 archons
    private final static int maxArchons = 3;
    private final static int ARCHON_BROADCAST_CHANNEL_START = 0;
    private final static int ARCHON_BROADCAST_CHANNEL_STOP = 12;

    // max 10 gardeners
    private final static int maxGardeners = 10;
    private final static int GARDENER_BROADCAST_CHANNEL_START = 20;
    private final static int GARDENER_BROADCAST_CHANNEL_STOP = 59;

    // max 50 soldiers
    private final static int maxSoldiers = 50;
    private final static int SOLDIER_BROADCAST_CHANNEL_START = 60;
    private final static int SOLDIER_BROADCAST_CHANNEL_STOP = 259;

    // max 50 lumberjacks
    private final static int maxLumberjacks = 50;
    private final static int LUMBERJACK_BROADCAST_CHANNEL_START = 260;
    private final static int LUMBERJACK_BROADCAST_CHANNEL_STOP = 459;

    // max 15 tanks
    private final static int maxTanks = 15;
    private final static int TANK_BROADCAST_CHANNEL_START = 460;
    private final static int TANK_BROADCAST_CHANNEL_STOP = 519;

    // max 5 scouts
    private final static int maxScouts = 5;
    private final static int SCOUT_BROADCAST_CHANNEL_START = 520;
    private final static int SCOUT_BROADCAST_CHANNEL_STOP = 539;

    /*
    private List<AlliesRobotInfo> archons = new ArrayList<>(maxArchons);
    private List<AlliesRobotInfo> gardeners = new ArrayList<>(maxGardeners);
    private List<AlliesRobotInfo> scouts = new ArrayList<>(maxSoldiers);
    private List<AlliesRobotInfo> lumberjacks = new ArrayList<>(maxLumberjacks);
    private List<AlliesRobotInfo> soldiers = new ArrayList<>(maxTanks);
    private List<AlliesRobotInfo> tanks = new ArrayList<>(maxScouts);

    //class will take up 4 channels per robot
    public class AlliesRobotInfo{
        public int id;
        public boolean inDanger;
        public float x;
        public float y;
        public short numOfRoundsNotMoved = 0;

        public AlliesRobotInfo(int id, boolean inDanger, float x, float y) {
            this.inDanger = inDanger;
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    public static void updateAllyRobotInfo(){
        try {
            updateArchons();
            updateGardeners();
            updaeScout();
            updateSoldiers();
            updaeLumberjack();
            updaeTank();
        } catch (GameActionException e) {
            e.printStackTrace();
        }
    }

    private static void updateGardeners() {
        for (int i = ARCHON_BROADCAST_CHANNEL_START; i < ARCHON_BROADCAST_CHANNEL_STOP; i = i + 4){
            if(archons.get(i / 4) == null){
                int id = rc.readBroadcastInt(i);
                boolean inDanger = rc.readBroadcastBoolean(i + 1);
                float x = rc.readBroadcastFloat(i + 2);
                float y = rc.readBroadcastFloat((i + 3);

                AlliesRobotInfo alliesRobotInfo = new AlliesRobotInfo(id, inDanger, x, y);

                archons.add(alliesRobotInfo);
            } else {
                AlliesRobotInfo alliesRobotInfo = archons.get(i / 4);

                boolean inDanger = rc.readBroadcastBoolean(i + 1);
                float x = rc.readBroadcastFloat(i + 2);
                float y = rc.readBroadcastFloat((i + 3);

                if(alliesRobotInfo.x == x && alliesRobotInfo.y == y)
                    alliesRobotInfo.numOfRoundsNotMoved++;
                else
                    alliesRobotInfo.numOfRoundsNotMoved = 0;

                alliesRobotInfo.inDanger = inDanger;
                alliesRobotInfo.x = x;
                alliesRobotInfo.x = x;
            }
        }
    }

    private static void updateArchons() throws GameActionException {
        for (int i = ARCHON_BROADCAST_CHANNEL_START; i < ARCHON_BROADCAST_CHANNEL_STOP; i = i + 4){
            if(archons.get(i / 4) == null){
                int id = rc.readBroadcastInt(i);
                boolean inDanger = rc.readBroadcastBoolean(i + 1);
                float x = rc.readBroadcastFloat(i + 2);
                float y = rc.readBroadcastFloat((i + 3);

                AlliesRobotInfo alliesRobotInfo = new AlliesRobotInfo(id, inDanger, x, y);

                archons.add(alliesRobotInfo);
            } else {
                AlliesRobotInfo alliesRobotInfo = archons.get(i / 4);

                boolean inDanger = rc.readBroadcastBoolean(i + 1);
                float x = rc.readBroadcastFloat(i + 2);
                float y = rc.readBroadcastFloat((i + 3);

                if(alliesRobotInfo.x == x && alliesRobotInfo.y == y)
                    alliesRobotInfo.numOfRoundsNotMoved++;
                else
                    alliesRobotInfo.numOfRoundsNotMoved = 0;

                alliesRobotInfo.inDanger = inDanger;
                alliesRobotInfo.x = x;
                alliesRobotInfo.x = x;
            }
        }
    }
    */

}
