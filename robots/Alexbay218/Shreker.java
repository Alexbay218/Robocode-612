package Alexbay218;
import robocode.*;
import java.awt.Color;
/**
 * Shreker - a robot by Shaun Wu
 */
public class Shreker extends AdvancedRobot //AdvancedRobot has set methods (different then regular methods)
{
	/**
	 * run: Shreker`s default behavior
	 */
	private int direction = 1;
	private double enemyLife = 100;
	private double angle = 90;
	public void run() {
		System.out.println("Shrek is love, "); //Shrek is life
		setAdjustGunForRobotTurn(true); //AdvancedRobot only
		setAdjustRadarForRobotTurn(true); //AdvancedRobot only
		setAdjustRadarForGunTurn(true); //AdvancedRobot only
		setColors(Color.green,Color.green,Color.green); // body,gun,radar
		while(true) {
			turnRadarLeft(30);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double turn = (getHeading() + e.getBearing() - getGunHeading()); //used for gun + body turning
		double radarTurn = (getHeading() + e.getBearing()) - getRadarHeading(); //used for radar turning
		if(turn > 180) { //prevent full circles
			turn = turn - 360;
		}
		else if(turn < -180) {
			turn = 360 + turn;
		}
		if(radarTurn > 180) { //prevent full circles
			radarTurn = radarTurn - 360;
		}
		else if(radarTurn < -180) {
			radarTurn = 360 + radarTurn;
		}
		turnRadarRight(radarTurn+30); //points at robot direction + 30 degrees
		turnGunRight(turn);
		if(getEnergy() + 10 >= e.getEnergy()) { //determines if linear targeting is needed
			turnGunRight(Math.toDegrees(Math.asin(e.getVelocity()*Math.cos(Math.toRadians(getHeading()-e.getHeading()))/11)));
		}
		fire(3); //FIRE!!!
		angle = 90 - direction * e.getDistance()/180; //Calculate what angle for body to be at
		turnRight(e.getBearing()+angle); //Almost always at perpendicular
		setAhead(30*direction); //AdvancedRobot only
		if(enemyLife > e.getEnergy()) 
		{ //Bullet prediction
			enemyLife = e.getEnergy();
			setColors(Color.green,Color.yellow,Color.green); //Feed back to human eyes
			direction *= -1;
		}
		else {
			setColors(Color.green,Color.green,Color.green);
		}
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		direction *= -1; //Dodging (reverse direction)
		enemyLife += e.getPower()*3; //account for enemy energy gain
	}
	
	public void onHitWall(HitWallEvent e) {
		direction *= -1; //Dodging (reverse direction)
	}
	
	public void onBulletHit(BulletHitEvent e) {
		enemyLife -= 16; //account for enemy energy loss
	}
	
	public void onWin(WinEvent e) { //When opposing enemy just got SHREKED!
		System.out.println("Shrek is life."); //Shrek wins
		System.out.println("c,_.--.,y\n  7 a.a(\n (   ,_Y)\n :  '---;\n.'|.  - (\n"); //SHREK!
		setColors(Color.green,Color.green,Color.yellow); //Celebrate
	}
}
