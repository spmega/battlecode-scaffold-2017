package shashank.firstplayer;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class SoldierBot {
	private RobotController rc;
	private MapLocation targetLocation;
	private int count = 0;
	
	public SoldierBot(RobotController rc) {
		super();
		this.rc = rc;
		targetLocation = rc.getInitialArchonLocations(rc.getTeam().equals(Team.A)? Team.B : Team.A)[count];
	}

	public void run(){
		while(true){
			
			try {
				loop();
			} catch (GameActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Clock.yield();
		}
	}
	
	private void loop() throws GameActionException{
		MapLocation rcLoc = rc.getLocation();
		
		RobotInfo[] enemyInfo = rc.senseNearbyRobots(RobotType.SOLDIER.sensorRadius, rc.getTeam().equals(Team.A)? Team.B : Team.A);
		if(rc.canFireSingleShot() && enemyInfo.length > 0){
			rc.fireSingleShot(rcLoc.directionTo(enemyInfo[0].location));
		}
			
		if(targetLocation != null && !rcLoc.isWithinDistance(targetLocation, 5)){
			RobotCommon.tryMove(rc, rcLoc.directionTo(targetLocation));
		} else {
			count++;
			if(count < rc.getInitialArchonLocations(rc.getTeam().equals(Team.A)? Team.B : Team.A).length){
				targetLocation = rc.getInitialArchonLocations(rc.getTeam().equals(Team.A)? Team.B : Team.A)[count];
			} else {
				RobotCommon.randomMove(rc);
				targetLocation = null;
			}
		}
		
		System.out.println("Bytecodes used: " + Clock.getBytecodeNum());
		
	}
	
}
