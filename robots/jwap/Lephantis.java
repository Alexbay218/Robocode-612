package jwap;
import robocode.*;
import java.awt.*;

/**
 * Lephantis - concieved by Jeffrey Wang, germinated for d-YOU ARE OUR VOICE
 */
public class Lephantis extends AdvancedRobot
{
	private double firstScan; //upon exiting spinbot mode, correct angle with this
	private double lastScanBearing; //Record last scanned robot position for main
	private int targetFocus; //Incremented every radar sweep, reset ever successful scan. Tolerance of 3 misses before spinbot mode.
	private boolean radarTurn; //Define direction to swipe radar in main, alternating
	private boolean justFocused; //Tells main to use firstScan if targetFocus was just reset from spinbot mode
	private int hitStage; //boss phase
	private double projectedAngle; //carries number through circular targeting
	private int[] accuracy;
	private boolean advTargeting;
	private final double FIREPOWER = 3.0;
	
	public void run() {
		
		//Genetic blueprint
		firstScan = 0;
		targetFocus = 5;
		hitStage = 0;
		setColors(new Color(148, 122, 114), new Color(250, 100, 160), new Color(255, 100, 150), new Color(255, 255, 255), new Color(180, 0, 100));
		System.out.println("WE WERE ENDLESS. CONSUME US. BE REBORN.");
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		accuracy = new int[2]; //slot 1 numhits; slot 2 nummissed
		advTargeting = true;
		// Main nerve

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
			
			if (getTurnRemaining() == 0) {
				if (targetFocus <= 3) 
				{
					if (advTargeting) {
						setTurnGunLeft(subtractBearing(getGunBearing(), projectedAngle));
					} else {
						setTurnGunLeft(subtractBearing(getGunBearing(), lastScanBearing));
					setFire(FIREPOWER); 
					}
					if (subtractBearing(Math.abs(lastScanBearing), 90) > 20) {
						if (lastScanBearing > 0) {
							setTurnRight(10);
						} else {
							setTurnLeft(10);
						}
					}
				}
			}
			
			if (getDistanceRemaining() == 0) {
				double moveNum = Math.random();
				if (moveNum > 0.65) setAhead(Math.random() * 60);
				else if (moveNum < 0.35) setBack(Math.random() * 60);
			}
			
			if (accuracy[1] > 9 && (double) accuracy[0] / accuracy[1] < 0.10) {
				advTargeting = !advTargeting;
				if (advTargeting) setGunColor(new Color(250, 100, 160));
				else  setGunColor(new Color(207, 0, 120));
			}
			
			targetFocus++;
		}
	}

	/**
	 * onScannedRobot: Nerve pattern on sensed prey
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		if (targetFocus > 3) {
			firstScan = getRadarTurnRemaining();
			justFocused = true;
		} else {
			justFocused = false;
		}
		
		projectedAngle = e.getBearing(); 
		if (getDistanceRemaining() == 0 && getTurnRemaining() == 0) {
			double projection;
				if (targetFocus == 0) projection = ((double)e.getDistance() / Rules.getBulletSpeed(FIREPOWER)) * subtractBearing(e.getBearing(), lastScanBearing);
				else if (targetFocus <= 3) projection = ((double)e.getDistance() / Rules.getBulletSpeed(FIREPOWER)) * (subtractBearing(e.getBearing(),lastScanBearing) / targetFocus);
		/*	if (e.getDistance() < 400) {
				if (targetFocus == 0) projectedAngle = e.getBearing() + 
						((double)e.getDistance() / Rules.getBulletSpeed(FIREPOWER)) * subtractBearing(e.getBearing(), lastScanBearing);
				else if (targetFocus <= 3) projectedAngle = e.getBearing() + 
						((double)e.getDistance() / Rules.getBulletSpeed(FIREPOWER)) * (subtractBearing(e.getBearing(),lastScanBearing) / targetFocus);
				//				  Number of ticks to reach target                   x               Average dAngle / tick since last scan
			}
			else if (e.getDistance() < 1000){
				if (targetFocus == 0) projectedAngle = e.getBearing() + 
						((1000 - e.getDistance()) / 600) * ((double)e.getDistance() / Rules.getBulletSpeed(FIREPOWER)) * subtractBearing(e.getBearing(), lastScanBearing);
				else if (targetFocus <= 3) projectedAngle = e.getBearing() + 
						((1000 - e.getDistance()) / 600) * ((double)e.getDistance() / Rules.getBulletSpeed(FIREPOWER)) * (subtractBearing(e.getBearing(),lastScanBearing) / targetFocus);
			}*/
		}
		
		
		if (targetFocus <= 3) {
			setTurnGunLeft(subtractBearing(getGunBearing(), projectedAngle));
			setFire(FIREPOWER);
		}
		lastScanBearing = e.getBearing();
		targetFocus = 0;
	}

	/**
	 * onHitByBullet: Response nerve in case of defilement
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		
		if (getEnergy() < 50 && hitStage == 0) {
			System.out.println("WE ARE YOUR FLESH. WHY DO YOU DEFILE US?");
			hitStage++;
		}
	}
	
	/**
	 * onHitWall: Response to restraint
	 */
	public void onHitWall(HitWallEvent e) {
		if (Math.abs(e.getBearing()) < 90) setBack(200);
		else setAhead(200);
	}	
	
	public void onBulletHit(BulletHitEvent event)
	{
		accuracy[0]++;
	}
	
	public void onBulletMissed(BulletMissedEvent event)
	{
		accuracy[1]++;
	}
	
	//Evolved routines
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
