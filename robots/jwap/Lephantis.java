package jwap;
import robocode.*;
import java.awt.*;

/**
 * Lephantis - concieved by Jeffrey Wang, germinated for d-YOU ARE OUR VOICE
 */
public class Lephantis extends AdvancedRobot
{
	private double firstScan;
	private double lastScanBearing;
	private int targetFocus;
	private boolean radarTurn;
	private boolean justFocused;
	private int hitStage;
	
	
	public void run() {
		
		firstScan = 0;
		targetFocus = 5;
		hitStage = 0;
		setColors(new Color(148, 122, 114), new Color(207, 0, 120), new Color(255, 100, 150), new Color(255, 255, 255), new Color(180, 0, 100));
		System.out.println("WE WERE ENDLESS. CONSUME US. BE REBORN.");
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		// Robot main loop

		while(true) {
			if (targetFocus > 3) {
				turnRadarRight(45);
				setScanColor(new Color(180, 0, 100));
			}
			if (justFocused) {
				turnRadarLeft(firstScan - 20);
				radarTurn = true;
			}
			if (targetFocus <= 3) {
				if (radarTurn) {
					if (getRadarBearing() - lastScanBearing > 20) turnRadarLeft(45);
					else turnRadarLeft(2 * (subtractBearing(getRadarBearing(), lastScanBearing) + 5));
					radarTurn = false;
				} else {
					if (lastScanBearing - getRadarBearing() > 20) turnRadarRight(45);
					else turnRadarRight(2 * (subtractBearing(lastScanBearing, getRadarBearing()) + 5));
					radarTurn = true;
				}
				setScanColor(new Color(220, 70, 150));
			}
			turnGunLeft(subtractBearing(getGunBearing(), lastScanBearing));
			
			
			if (targetFocus < 3) {
				setFire(3.0);
				if (Math.random() < 0.05) {
					setTurnLeft(20);
					setAhead(100);
				} else if (Math.random() < 0.05) {
					setTurnRight(90);
				}
			}	
			
			if (getDistanceRemaining() == 0) targetFocus++;
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		if (targetFocus > 3) {
			firstScan = getRadarTurnRemaining();
			justFocused = true;
		} else {
			justFocused = false;
		}
		lastScanBearing = e.getBearing();
		targetFocus = 0;
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
		if (getEnergy() < 50 && hitStage == 0) {
			System.out.println("WE ARE YOUR FLESH. WHY DO YOU DEFILE US?");
			hitStage++;
		}
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	
	
	private double getGunBearing() {
		double gunBearing = (getGunHeading() - getHeading());
		if (gunBearing > 180) gunBearing -= 360;
		if (gunBearing < -180) gunBearing += 360;
		return gunBearing;
	}
	
	private double getRadarBearing() {
		double radarBearing = (getRadarHeading() - getHeading());
		if (radarBearing > 180) radarBearing -= 360;
		if (radarBearing < -180) radarBearing += 360;
		return radarBearing;
	}
	
	private double subtractBearing(double angle1, double angle2) {
		if (angle1 > 90 && angle2 < -90) angle2 += 360;
		if (angle1 < -90 && angle2 > 90) angle2 -= 360;
		angle1 -= angle2;
		return angle1;
	}
}
