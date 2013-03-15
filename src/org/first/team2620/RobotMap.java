package org.first.team2620;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import org.first.team2620.subsystems.ConveyorProxSwitch;

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
    
    
    // Climbing
    public static Jaguar LHConveyor = new Jaguar(3);
    public static ConveyorProxSwitch LHTop = new ConveyorProxSwitch(1);
    public static ConveyorProxSwitch LHMiddle = new ConveyorProxSwitch(2); //new DigitalInput(0);
    
    public static Jaguar RHConveyor = new Jaguar(4);
    public static ConveyorProxSwitch RHTop = new ConveyorProxSwitch(3); //new DigitalInput(0);
    public static ConveyorProxSwitch RHMiddle = new ConveyorProxSwitch(4); //new DigitalInput(0);
    
    public static Jaguar Leg = new Jaguar(6);
    
    public static double ClimbPower = prefs.getDouble("ClimbPower", 1);
    public static double LegPower = prefs.getDouble("LegPower", 1);
    public static DigitalInput LegUp = new DigitalInput(14);
    public static DigitalInput LegDown = new DigitalInput(5);
    
    
    // Shooter
    public static Servo DiskInsert = null;//new Servo(1);
    
    public static Victor ShooterWheel = new Victor(5);
    public static Relay shooterLift = new Relay(2);
    public static Encoder ShooterWheelEncoder = new Encoder(6,7);
    
    public static int FullCourtShotRpm = prefs.getInt("FullCourtShotRpm", 50);
    public static double ShooterPower = prefs.getDouble("ShooterPower", 1);

    
    // Controls
    public static Joystick Joystick1 = new Joystick(1);
    public static Joystick Joystick2 = new Joystick(2);
    public static Joystick Joystick3 = null; //new Joystick(3);
    
    // public static NetworkTable cameraTable = NetworkTable.getTable("camera");
}
