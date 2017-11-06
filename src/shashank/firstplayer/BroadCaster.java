package shashank.firstplayer;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class BroadCaster {
	
	public static final int GARDENER_POSITION_CHANNEL_MIN = 0;
	public static final int GARDENER_POSITION_CHANNEL_MAX = 20;
	
	public static void broadcast(RobotController rc, int channel, int data) throws GameActionException{
		rc.broadcast(channel, data);
	}
	
	public static int readBroadcastInt(RobotController rc, int channel) throws GameActionException{
		return rc.readBroadcast(channel);
	}
	
	public static boolean readBroadcastBoolean(RobotController rc, int channel) throws GameActionException{
		return rc.readBroadcastBoolean(channel);
	}
	
	public static float readBroadcastFloat(RobotController rc, int channel) throws GameActionException{
		return rc.readBroadcastFloat(channel);
	}
	
	public static int[] readComposedBroadcast(RobotController rc, int channel) throws GameActionException{
		return decomposeData(rc.readBroadcast(channel));
	}
	
	//Integer max number is about 2  000   000   000
	//                              data1 data2 data3
	public static int composeData (int data1, int data2, int data3){
		int result = 2 * 1000000000;
		result = result + data1 * 1000000; 
		result = result + data2 * 1000;
		result = result + data3 * 1;
		return result;
	}
	
	//Integer max number is about 2  000   000   000
	//                              data1 data2 data3
	public static int[] decomposeData (int data){
		int[] resultData = new int[3];
		int data3 = data % 1000;
		int data2 = (data % 1000000) - data3;
		int data1 = (data % 1000000000) - data3 - data2; 
		
		resultData[0] = data1;
		resultData[1] = data2;
		resultData[2] = data3;
		
		return resultData;
	}
	
}
