package shashank.firstplayer;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.TreeInfo;

public class GardenerBot {
	private RobotController rc;
	private boolean hasAlreadyPlantedTrees = false;


	/*****
	 * 
	 * This is the Gardener class. Strategies is to build a FOREST or the "flower" construction.
	 * FOREST: Easier to build, but harder to maintain, also provides confusion for the enemy team
	 * FLOWER: Harder to build, but easier to maintain, as well as protection
	 * 
	 * 
	 */
	
	public GardenerBot(RobotController rc) {
		super();
		this.rc = rc;
	}

	public void run(){
		try {
			int channel = 0;
			
			while(rc.readBroadcastFloat(channel) != 0 && rc.readBroadcastFloat(channel + 1) != 0){
				channel = channel + 2;
			}
			
			rc.broadcastFloat(channel, rc.getLocation().x);
			rc.broadcastFloat(channel+1, rc.getLocation().y);
			
			while(true){
				loop();
				Clock.yield();
			}
			
		} catch (GameActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loop() throws GameActionException{
		if(rc.canBuildRobot(RobotType.SOLDIER, Direction.WEST) 
				&& rc.hasRobotBuildRequirements(RobotType.SOLDIER)
				&& rc.getBuildCooldownTurns() == 0
				&& rc.readBroadcastInt(1000) < 40){
			rc.buildRobot(RobotType.SOLDIER, Direction.WEST);
			rc.broadcast(1000, rc.readBroadcastInt(1000)+1);
		}
		
		if(!hasAlreadyPlantedTrees){
			//first go to a location that has enough space for the gardener and the trees around it
			//if nothing is sensed, go ahead and start planting
			if(rc.isCircleOccupied(rc.getLocation(), RobotType.GARDENER.sensorRadius) 
					&& rc.onTheMap(rc.getLocation(), RobotType.GARDENER.sensorRadius)
					&& !checkForCollision()){
								
				plantTrees();
				hasAlreadyPlantedTrees = true;
				
			} else {
				//RobotCommon.debug("gardener trying to move");
				RobotCommon.randomMove(rc);
			}
			
		} else {
			plantTrees();
			maintainTrees();
		}
		
		//RobotCommon.debug("Bytecodes used: " + Clock.getBytecodeNum());
	}
	
	private void plantTrees() throws GameActionException{
		plantTree(new Direction(Direction.WEST.radiansBetween(Direction.NORTH)));
		plantTree(Direction.NORTH);
		plantTree(new Direction(Direction.NORTH.radiansBetween(Direction.EAST)));
		plantTree(Direction.EAST);
		plantTree(new Direction(Direction.EAST.radiansBetween(Direction.SOUTH)));
		plantTree(Direction.SOUTH);
		plantTree(new Direction(Direction.SOUTH.radiansBetween(Direction.WEST)));
	}
	
	private void plantTree(Direction dir) throws GameActionException{
		if(rc.canPlantTree(dir)) rc.plantTree(dir);
	}
	
	private void maintainTrees() throws GameActionException{
		TreeInfo[] nearbyTrees = rc.senseNearbyTrees(2.0F);
		//RobotCommon.debug("Nearby trees: " + nearbyTrees.length);
		for(TreeInfo tree: nearbyTrees){
			if(rc.canWater() && rc.canWater(tree.location) && tree.getMaxHealth() == 50 && tree.getHealth() < 46){
				//RobotCommon.debug("Watering tree: " + tree.ID);
				rc.water(tree.location);
			}
		}
	}
	
	private boolean checkForCollision() throws GameActionException{
		/*
		for(int i = BroadCaster.GARDENER_POSITION_CHANNEL_MIN; i < BroadCaster.GARDENER_POSITION_CHANNEL_MAX; i = i + 2){
			float positionX = BroadCaster.readBroadcastFloat(rc, i);
			float positionY = BroadCaster.readBroadcastFloat(rc, i+1);
			//RobotCommon.debug("Robot Position: " + positionX + " , " + positionY);
			
			if(positionX == 0 && positionY == 0) continue;
			
			MapLocation gardenerLoc = new MapLocation(positionX, positionY);
			if(MapLocation.doCirclesCollide(rc.getLocation(), 2.0F, gardenerLoc, 10.0F)) return true;
		}
		
		return false;
		*/
		RobotInfo[] nearbyRobots = rc.senseNearbyRobots(5.0F);
		for(int i = 0; i < nearbyRobots.length; i++){
			float positionX = nearbyRobots[i].getLocation().x;
			float positionY = nearbyRobots[i].getLocation().y;
			//RobotCommon.debug("Robot Position: " + positionX + " , " + positionY);
			
			if(positionX == 0 && positionY == 0) continue;
			
			MapLocation robotLoc = new MapLocation(positionX, positionY);
			if(MapLocation.doCirclesCollide(rc.getLocation(), 5.0F, robotLoc, 5.0F)) return true;
		}
		
		return false;
	}
	
}

