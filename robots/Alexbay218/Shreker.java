package Alexbay218;
import robocode.*;
import java.awt.Color;
/**
 * Shreker - a robot by Shaun Wu
 */
public class Shreker extends AdvancedRobot
{
	/**
	 * run: Shreker's default behavior
	 */
	public void run() {
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setColors(Color.green,Color.green,Color.green); // body,gun,radar
		turnRadarRight(360);
		while(true) {
			turnRadarRight(5);
			turnRadarLeft(10);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double turn = (getHeading() + e.getBearing() - getGunHeading());
		double radarTurn = getHeading() + e.getBearing() - getRadarHeading();
		if(turn > 180) {
			turn = 360 - turn;
		}
		if(radarTurn > 180) {
			radarTurn = 360 - radarTurn;
		}
		turnRadarRight(radarTurn);
		turnGunRight(turn);
		fire(1);
	}	
}
