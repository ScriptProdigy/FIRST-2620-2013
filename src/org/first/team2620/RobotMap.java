package org.first.team2620;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 * @author frc2620
 */
public class RobotMap {
    
    // Drive
    public static Jaguar LHDrive = new Jaguar(1);
    public static Jaguar RHDrive = new Jaguar(2);
    public static int DriveDirection = 1; // 1, or -1
    public static RobotDrive drive = new RobotDrive(LHDrive, RHDrive);
    
    
    // Climbing
    public static Jaguar LHConveyor = new Jaguar(3);
    public static DigitalInput LHTopHooked = null; //new DigitalInput(0);
    public static DigitalInput LHMiddleHooked = null; //new DigitalInput(0);
    
    public static Jaguar RHConveyor = new Jaguar(4);
    public static DigitalInput RHTopHooked = null; //new DigitalInput(0);
    public static DigitalInput RHMiddleHooked = null; //new DigitalInput(0);
    
    public static Jaguar Leg = new Jaguar(6);
    
    public static double ClimbPower = 1;
    public static double LegPower = 1;
    
    
    // Shooter
    public static Relay DiskInsert = null; //new Relay(1);
    public static Relay DiskFeeder = null; //new Relay(2);
    public static Jaguar ShooterWheel = new Jaguar(5);
    public static Jaguar ShooterLift = new Jaguar(7);

    
    // Controls
    public static Joystick Joystick1 = new Joystick(1);
    public static Joystick Joystick2 = new Joystick(2);
    public static Joystick Joystick3 = null; //new Joystick(3);
    
    // public static NetworkTable cameraTable = NetworkTable.getTable("camera");
}
