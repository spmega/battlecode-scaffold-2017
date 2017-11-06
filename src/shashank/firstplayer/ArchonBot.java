package shashank.firstplayer;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class ArchonBot {
	private RobotController rc;
	private int numOfHiredGardeners = 0;
	
	public ArchonBot(RobotController rc) {
		super();
		this.rc = rc;
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
		
		RobotCommon.tryMove(rc, RobotCommon.randomDirection());
		hireGardener();
		considerDonating();
		RobotCommon.debug("Bytecodes used: " + Clock.getBytecodeNum());
	}
	
	private void hireGardener() throws GameActionException{
		Direction rndDir = RobotCommon.randomDirection();
		if(rc.canHireGardener(rndDir) 
				&& rc.isBuildReady() 
				&& rc.readBroadcastFloat(BroadCaster.GARDENER_POSITION_CHANNEL_MAX - 1) == 0
				&& rc.readBroadcastFloat(BroadCaster.GARDENER_POSITION_CHANNEL_MAX) == 0) {
			rc.hireGardener(rndDir);
			numOfHiredGardeners++;
			RobotCommon.debug("hired gardeners: " + numOfHiredGardeners);
		}
	}
	
	private void considerDonating() throws GameActionException{
		if(rc.getTeamBullets() > 2000 && rc.getTeamBullets() - 2000 >= 200){
			RobotCommon.debug("Team bullets: " + rc.getTeamBullets());
			float amountToDonate = rc.getTeamBullets()-2000;
			RobotCommon.debug("Team bullets: " + rc.getTeamBullets());
			RobotCommon.debug("Donating bullets: " + amountToDonate);
			rc.donate(amountToDonate);
		}
	}
}
