package shashank.firstplayer;

import battlecode.common.Clock;
import battlecode.common.RobotController;

public class TankBot {
	private RobotController rc;
	
	public TankBot(RobotController rc) {
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
