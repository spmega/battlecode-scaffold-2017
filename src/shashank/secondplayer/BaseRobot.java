package shashank.secondplayer;

import battlecode.common.*;

/*
 * Basic Actions that robots of all types need:
 * 1. Moving
 * 2. Shooting
 * 3. Sensing - however, sensing can be done by each of the robots as they need
 */

public abstract class BaseRobot {

    RobotController rc;

    public BaseRobot(RobotController rc) {
        this.rc = rc;
    }

    public RobotController getRc() {
        return rc;
    }

    //Movement
    private void tryMove(Direction direction, MapLocation mapLocation, float distance){

        try {

            if(mapLocation != null && rc.canMove(mapLocation)){
                rc.move(mapLocation);
            } else if(direction != null && distance != 0) {
                rc.move(direction, distance);
            } else if (direction != null){
                rc.move(direction);
            } else {
                DebugLogger.printWarning(rc.getType().name() + " : " + rc.getID() + "did not move because there were no valid parameters given");
            }

        } catch (GameActionException e) {
            DebugLogger.printError(e);
        }

    }

    public void tryMove(MapLocation mapLocation){
        tryMove(null, mapLocation, 0);
    }

    public void tryMove(Direction direction){
        tryMove(direction, null, 0);
    }

    public void tryMove(Direction direction, float distance){
        tryMove(direction, null, distance);
    }

    //Shooting
    private void shoot(boolean triad, boolean pentad, Direction direction){

        try {
            if( ( (!triad && !pentad) && direction != null ) && rc.canFireSingleShot()){
                rc.fireSingleShot(direction);
            } else if ( ( (triad && !pentad) && direction != null ) && rc.canFireTriadShot()){
                rc.fireTriadShot(direction);
            } else if ( ( (!triad && pentad) && direction != null ) && rc.canFirePentadShot()){
                rc.firePentadShot(direction);
            }
        } catch (GameActionException e) {
            DebugLogger.printError(e);
        }

    }

    public void shootSingleShot(Direction direction){
        shoot(false, false, direction);
    }

    public void shootTriadShot(Direction direction){
        shoot(true, false, direction);
    }

    public void shootPentadShot(Direction direction){
        shoot(false, true, direction);
    }

    public Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

    /**
     * A slightly more complicated example function, this returns true if the given bullet is on a collision
     * course with the current robot. Doesn't take into account objects between the bullet and this robot.
     *
     * @param bullet The bullet in question
     * @return True if the line of the bullet's path intersects with this robot's current position.
     */
    public boolean bulletWillCollideWithMe(BulletInfo bullet) {
        MapLocation myLocation = rc.getLocation();

        // Get relevant bullet information
        Direction propagationDirection = bullet.dir;
        MapLocation bulletLocation = bullet.location;

        // Calculate bullet relations to this robot
        Direction directionToRobot = bulletLocation.directionTo(myLocation);
        float distToRobot = bulletLocation.distanceTo(myLocation);
        float theta = propagationDirection.radiansBetween(directionToRobot);

        // If theta > 90 degrees, then the bullet is traveling away from us and we can break early
        if (Math.abs(theta) > Math.PI/2) {
            return false;
        }

        // distToRobot is our hypotenuse, theta is our angle, and we want to know this length of the opposite leg.
        // This is the distance of a line that goes from myLocation and intersects perpendicularly with propagationDirection.
        // This corresponds to the smallest radius circle centered at our location that would intersect with the
        // line that is the path of the bullet.
        float perpendicularDist = (float)Math.abs(distToRobot * Math.sin(theta)); // soh cah toa :)

        return (perpendicularDist <= rc.getType().bodyRadius);
    }


    abstract void loop();

}
