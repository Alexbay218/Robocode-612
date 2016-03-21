package eatme;
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
import java.awt.geom.*;
// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Ilike - a robot by (your name here)
 */
public class Ilike extends AdvancedRobot{
/*if (getX() > Rules.getBattlefieldWidth() - 18 || getX() < 18 || getY() > getBattlefieldHeight() - 18 || getY() < 18)
{*/
private Point2D.Double cen;

//private double turnRate;
private final double MAX_RADAR_TRACKING_AMOUNT = Math.PI / 4;
	/**
	 * run: Ilike's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		cen = new Point2D.Double (getBattleFieldWidth() / 2, getBattleFieldHeight() / 2);
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:
		setAdjustRadarForGunTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setColors(new Color (255, 255, 255),Color.red,Color.red); // body,gun,radar
		setTurnRadarRight(360);
//	int random = 0;
		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			//getBattleFieldWidth()  getBattleFieldHeight() 
				 
				
			 if(getDistanceRemaining() == 0 && getTurnRemaining() == 0){	
			setTurnRight(Math.random()* (50 - 5) + 5);	
			 setAhead (Math.random() * (130) + 20);
			}
			execute();
		}
		
	}
				public void walls(ScannedRobotEvent e){
				if (getX() > getBattleFieldWidth() - 75 || getX() <75 || getY() > getBattleFieldHeight() - 75 || getY() < 75)
			{
				Point2D.Double me = new Point2D.Double (getX(), getY());
				//double dist = me.distance (cen);
				double rX = cen.x - me.x;
				double rY = cen.y - me.y;
				double tAngle = Math.toDegrees (Math.atan2 (rX, rY));
				//turnRight (0); 	
				//ahead(0);
				setTurnRight (tAngle - getHeading());
				setAhead (300);
				
						// i enjoy commenting on everything 
				}
				else if( getDistanceRemaining() == 0 && getTurnRemaining() == 0){	
					
					setTurnRight(Math.random()* (50 - 5) + 5);	
			 		setAhead (Math.random() * (130) + 20);
						
			 }
			 			// else if (e.getHeading() + getBearing () < 5){ahead(-30);} // i d not know why this doent work kil me now
			 
				}
				
	public void LinerThings(ScannedRobotEvent e, double bp){

	
double bulletPower = Math.min(3.0,getEnergy());
double myX = getX();
double myY = getY();
double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
double enemyHeading = e.getHeadingRadians();
double enemyVelocity = e.getVelocity();
 
 
double deltaTime = 0;
double battleFieldHeight = getBattleFieldHeight(), 
       battleFieldWidth = getBattleFieldWidth();
double predictedX = enemyX, predictedY = enemyY;
while((++deltaTime) * (20.0 - 3.0 * bulletPower) < 
      Point2D.Double.distance(myX, myY, predictedX, predictedY)){		
	predictedX += Math.sin(enemyHeading) * enemyVelocity;	
	predictedY += Math.cos(enemyHeading) * enemyVelocity;
	if(	predictedX < 18.0 
		|| predictedY < 18.0
		|| predictedX > battleFieldWidth - 18.0
		|| predictedY > battleFieldHeight - 18.0){
		predictedX = Math.min(Math.max(18.0, predictedX), 
                    battleFieldWidth - 18.0);	
		predictedY = Math.min(Math.max(18.0, predictedY), 
                    battleFieldHeight - 18.0);
		break;
	}
}
double theta = Utils.normalAbsoluteAngle(Math.atan2(
    predictedX - getX(), predictedY - getY()));
 

setTurnGunRightRadians(Utils.normalRelativeAngle(theta - getGunHeadingRadians()));
fire(bulletPower);
 


	}
/*double theta = Utils.normalAbsoluteAngle(Math.atan2(
    predictedX - getX(), predictedY - getY()));
setTurnGunRightRadians(Utils.normalRelativeAngle(theta - getGunHeadingRadians())));
if( getEnergy() > 10){setfire(bulletPower);}
*/


	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		focusRadar(e);
		walls(e);
LinerThings(e, 2);
//setFire(2);
		//if(e.gunHeat() == 0}{setAhead(-10)}
		// Replace the next line with any behavior you would like
	//	while (!(getGunTurnRemaining() == 0)){}
	
	}


 

	

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */

	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		//turnLeft(45);		
		//ahead(-100);
	
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
/*	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		turnRight(90);
		ahead(100);
	}*/	
public void focusRadar(ScannedRobotEvent e) {
    	double radarBearingOffset = 
    		Utils.normalRelativeAngle(getRadarHeadingRadians() - 
    			(e.getBearingRadians() + getHeadingRadians()));
    	int d;
		if (radarBearingOffset > 0)
			d = 1;
		else
			d = -1;
    	setTurnRadarLeftRadians(radarBearingOffset +
    		(d *
    			(MAX_RADAR_TRACKING_AMOUNT / 2)));
    }
}
