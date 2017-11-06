package shashank.firstplayer;

import java.util.LinkedList;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.TreeInfo;

public class ScoutBot {
	private RobotController rc;
	private MapLocation targetLocation;
	private LinkedList<Integer> treeIdList = new LinkedList<Integer>();
	
	public ScoutBot(RobotController rc) {
		super();
		this.rc = rc;
	}

	public void run(){
		try{
			while(true){
				
				loop();
				
				Clock.yield();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void loop() throws GameActionException{
		System.out.println("Bytecodes used: " + Clock.getBytecodeNum());
		if(targetLocation == null){
			TreeInfo[] treeInfo = rc.senseNearbyTrees();
			if(treeInfo.length == 0){
				RobotCommon.tryMove(rc, RobotCommon.randomDirection());
			} else {
				
				this.targetLocation = treeInfo[0].location;
				MapLocation rcLoc = rc.getLocation();
				Direction targetDirection = rcLoc.directionTo(targetLocation);
				float dist = rcLoc.distanceTo(targetLocation) - 1;
				
				if(!rc.hasMoved()){
					if(rc.canMove(targetDirection)){
						rc.move(targetDirection, dist);
					}
				}
				
			}			
		} else if(targetLocation != null && rc.getLocation().distanceTo(targetLocation) - 1 > 1){
			MapLocation rcLoc = rc.getLocation();
			Direction targetDirection = rcLoc.directionTo(targetLocation);
			float dist = rcLoc.distanceTo(targetLocation) - 1;
			
			if(!rc.hasMoved()){
				if(rc.canMove(targetDirection)){
					rc.move(targetDirection, dist);
				}
			}
		} else {
			if(rc.canShake(targetLocation)){
				rc.shake(targetLocation);
			}
			
			TreeInfo[] treesInfo = rc.senseNearbyTrees();
			this.treeIdList.addFirst(treesInfo[0].ID);
			if(this.treeIdList.size() > 100) this.treeIdList.remove(100);
			
			if(treesInfo.length < 2){
				RobotCommon.tryMove(rc, RobotCommon.randomDirection());
			} else {
				TreeInfo targetTree = null;
				for(TreeInfo info: treesInfo){
					if(!treeIdList.contains(info.ID)) {
						targetTree = info;
						break;
					}
				}
				
				if(targetTree == null){
					RobotCommon.tryMove(rc, RobotCommon.randomDirection());
				} else {
					
					targetLocation = targetTree.getLocation();
					MapLocation rcLoc = rc.getLocation();
					Direction targetDirection = rcLoc.directionTo(targetLocation);
					float dist = rcLoc.distanceTo(targetLocation) - 1;
					
					if(!rc.hasMoved()){
						if(rc.canMove(targetDirection)){
							rc.move(targetDirection, dist);
						}
					}					
				}
				
			}
		}
		
	}
}
