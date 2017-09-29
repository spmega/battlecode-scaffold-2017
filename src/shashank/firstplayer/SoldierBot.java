package shashank.firstplayer;

import battlecode.common.Clock;
import battlecode.common.RobotController;

public class SoldierBot {
	private RobotController rc;
	
	public SoldierBot(RobotController rc) {
		super();
		this.rc = rc;
	}

	public void run(){
		while(true){
			
			loop();
			
			Clock.yield();
		}
	}
	
	private void loop(){
		System.out.println("Bytecodes used: " + Clock.getBytecodeNum());
	}
	
}
