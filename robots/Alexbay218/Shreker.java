package Alexbay218;
import robocode.*;
import java.awt.Color;
/**
 * Shreker - a robot by Shaun Wu
 */
public class Shreker extends AdvancedRobot
{
	/**
	 * run: Shreker`s default behavior
	 */
	private int direction = 1;
	private double enemyLife = 100;
	private double angle = 90;
	public void run() {
		System.out.println("Shrek is love, ");
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setColors(Color.green,Color.green,Color.green); // body,gun,radar
		while(true) {
			turnRadarLeft(30);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double turn = (getHeading() + e.getBearing() - getGunHeading());
		double radarTurn = (getHeading() + e.getBearing()) - getRadarHeading();
		if(turn > 180) {
			turn = turn - 360;
		}
		else if(turn < -180) {
			turn = 360 + turn;
		}
		if(radarTurn > 180) {
			radarTurn = radarTurn - 360;
		}
		else if(radarTurn < -180) {
			radarTurn = 360 + radarTurn;
		}
		turnRadarRight(radarTurn+30);
		turnGunRight(turn);
		if(getEnergy() + 10 >= e.getEnergy()) {
			turnGunRight(Math.toDegrees(Math.asin(e.getVelocity()*Math.cos(Math.toRadians(getHeading()-e.getHeading()))/11)));
		}
		fire(3);
		angle = 90 - direction * e.getDistance()/180;
		turnRight(e.getBearing()+angle);
		setAhead(30*direction);
		if(enemyLife > e.getEnergy()) {
			enemyLife = e.getEnergy();
			setColors(Color.green,Color.yellow,Color.green);
			direction *= -1;
		}
		else {
			setColors(Color.green,Color.green,Color.green);
		}
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		direction *= -1;
		enemyLife += e.getPower()*3;
	}
	
	public void onHitWall(HitWallEvent e) {
		direction *= -1;
	}
	
	public void onBulletHit(BulletHitEvent e) {
		enemyLife -= 16;
	}
	
	public void onWin(WinEvent e) {
		System.out.println("Shrek is life.");
		System.out.println("c,_.--.,y\n  7 a.a(\n (   ,_Y)\n :  '---;\n.'|.  - (\n");
		setColors(Color.green,Color.green,Color.yellow);
	}
}
