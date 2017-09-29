package shashank.firstplayer;

import battlecode.common.Clock;
import battlecode.common.RobotController;

public class ScoutBot {
	private RobotController rc;
	
	public ScoutBot(RobotController rc) {
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
