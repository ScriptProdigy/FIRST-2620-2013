package org.first.team2620;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author frc2620
 */
public class RobotMap {
    
    public static Preferences prefs = Preferences.getInstance();
    
    // Drive
    public static Jaguar LHDrive = new Jaguar(1);
    public static Jaguar RHDrive = new Jaguar(2);
    public static int DriveDirection = 1; // 1, or -1
    public static RobotDrive drive = new RobotDrive(LHDrive, RHDrive);
    

    public static Jaguar Leg = new Jaguar(6);
    public static double LegPower = prefs.getDouble("LegPower", 1);
    public static DigitalInput LegUp = new DigitalInput(13);
    public static DigitalInput LegDown = new DigitalInput(5);
    
    
    // Shooter
    public static Servo DiskInsert = new Servo(8);
    
    public static Victor ShooterWheel = new Victor(5);
    public static Relay shooterLift = new Relay(2);
    public static AnalogChannel ShooterAngle = new AnalogChannel(1);
     
    public static Encoder ShooterWheelEncoder = new Encoder(6,7);
    
    public static int FullCourtShotRpm = prefs.getInt("FullCourtShotRpm", 50);
    public static double ShooterPower = prefs.getDouble("ShooterPower", 1);

    
    // Controls
    public static Joystick Joystick2 = new Joystick(1);
}
