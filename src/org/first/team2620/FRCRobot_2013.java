/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.first.team2620;


import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.first.team2620.subsystems.Shooter;


public class FRCRobot_2013 extends SimpleRobot 
{
    public Shooter shooter;
    public SendableChooser auton;
    
    
    public int Teleop_MoveToPosition = 0;
    public boolean Teleop_Move = false;
    public boolean Teleop_MoveUp = true;
    public int threshold = 2; 
    public int[] ShooterPositions = new int[] { RobotMap.Shooter_BackLeft, RobotMap.Shooter_BackRight, RobotMap.Shooter_FrontLeft, RobotMap.Shooter_FrontRight, RobotMap.Shooter_HangingTwo};
        
        
    public void robotInit()
    {
        auton = new SendableChooser();
        auton.addDefault("Back Left", (Object)String.valueOf(0));
        auton.addObject("Back Right", (Object)String.valueOf(1));
        auton.addObject("Front Left", (Object)String.valueOf(2));
        auton.addObject("Front Right", (Object)String.valueOf(3));
        SmartDashboard.putData("Autonomous Mode", auton);
        
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        
        shooter = new Shooter();
    }
    
    public void autonomous() 
    {
            
        RobotMap.ShooterWheel.set(1);
        RobotMap.DiskInsert.set(0.1);
        
        int posValue = Integer.parseInt(auton.getSelected().toString());
        
        int shootCount = 0;
        int totalShootCount = 6;
        
        while(isAutonomous())
        {
            getWatchdog().feed();
            
            RobotMap.drive.tankDrive(0, 0);
            
            if(shooterMoveToPosition(posValue))
            {
                if((shootCount < totalShootCount))
                {
                    try {

                        Thread.sleep(1000);
                        RobotMap.DiskInsert.set(1);
                        Thread.sleep(1000);
                        RobotMap.DiskInsert.set(0.1);

                        shootCount += 1;

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                else
                {
                    RobotMap.ShooterWheel.set(0);
                }
            }
            
            Timer.delay(0.05);
        }
           
    }

    public boolean shooterMoveToPosition(int pos) // Returns true if at position
    {
        
        RobotMap.Shooter_BackLeft = RobotMap.prefs.getInt("Shooter_BackLeft", 291);
        RobotMap.Shooter_BackRight = RobotMap.prefs.getInt("Shooter_BackRight", 291);
        RobotMap.Shooter_FrontLeft = RobotMap.prefs.getInt("Shooter_FrontLeft", 301);
        RobotMap.Shooter_FrontRight = RobotMap.prefs.getInt("Shooter_FrontRight", 301);
        RobotMap.Shooter_HangingTwo = RobotMap.prefs.getInt("Shooter_HangingTwo", 300);
        ShooterPositions = new int[] { RobotMap.Shooter_BackLeft, RobotMap.Shooter_BackRight, 
                                        RobotMap.Shooter_FrontLeft, RobotMap.Shooter_FrontRight, 
                                        RobotMap.Shooter_HangingTwo};
        

        int valueReq = ShooterPositions[pos];
        
        int angle = RobotMap.ShooterAngle.getValue();
        boolean belowThreshold = valueReq > (angle-(threshold/2));
        boolean aboveThreshold = (angle+(threshold/2)) > valueReq;
        
        if(Teleop_MoveUp) {
            if(belowThreshold) {
                //System.out.println("belowThreshold");
                shooter.liftUp();
                return false;
            } else {
                //System.out.println("stopLift");
                shooter.stopLift();
                return true;
            }
        } else {
            if(aboveThreshold) {
                //System.out.println("aboveThreshold");
                shooter.liftDown();
                return false;
            } else {
                //System.out.println("stopLift ( above )");
                shooter.stopLift();
                return true;
            }
        }
        
    }
    
    public void operatorControl() 
    {
        shooter.newMatch();
        
        while(isOperatorControl() && isEnabled())
        {
            getWatchdog().feed();
            
            // Drive
            RobotMap.drive.arcadeDrive(RobotMap.Joystick2);
            
            // Shooter Lift
            
            if(RobotMap.Joystick2.getRawButton(6) || RobotMap.Joystick2.getRawButton(7) ||
                    RobotMap.Joystick2.getRawButton(11) || RobotMap.Joystick2.getRawButton(10)
                    || RobotMap.Joystick2.getRawButton(5))
            {
                Teleop_Move = true;
                if(RobotMap.Joystick2.getRawButton(6))
                {
                    Teleop_MoveToPosition = 2;
                }
                else if(RobotMap.Joystick2.getRawButton(7))
                {
                    Teleop_MoveToPosition = 0;
                }
                else if(RobotMap.Joystick2.getRawButton(10))
                {
                    Teleop_MoveToPosition = 1;
                }
                else if(RobotMap.Joystick2.getRawButton(11))
                {
                    Teleop_MoveToPosition = 3;
                }
                else if(RobotMap.Joystick2.getRawButton(5))
                {
                    Teleop_MoveToPosition = 4;
                }
                
                if(RobotMap.ShooterAngle.getValue() > ShooterPositions[Teleop_MoveToPosition])
                {
                    Teleop_MoveUp = false;
                }
                else
                {
                    Teleop_MoveUp = true;
                }
            }
            
            //System.out.println("SHOOTER VALUE: " + RobotMap.ShooterAngle.getValue());
            if(RobotMap.Joystick2.getRawButton(3))
            {
                shooter.liftUp();
                Teleop_Move = false;
            } 
            else
            {
                if(RobotMap.Joystick2.getRawButton(2))
                {
                    shooter.liftDown();
                    Teleop_Move = false;
                } 
                else
                {
                    shooter.stopLift();
                }
            }
            
            if(Teleop_Move)
            {
                System.out.println("Teleop_Move: true, " + Teleop_MoveToPosition);
                shooterMoveToPosition(Teleop_MoveToPosition);
            }
            
            if(RobotMap.Joystick2.getRawButton(4)) { // Disk Insert
                RobotMap.DiskInsert.set(1);
            } else {
                RobotMap.DiskInsert.set(0.1);
            }
            
            if(RobotMap.Joystick2.getRawButton(1)) { // Spin up shooter
                shooter.speedUp();
            } else {
                shooter.stop();
            }

            legControl();
            Timer.delay(0.05);
        }
    }
    
    
    public void test() 
    {
        RobotMap.DiskInsert.startLiveWindowMode();
        LiveWindow.addSensor("Shooter", "Servo", RobotMap.DiskInsert);
        LiveWindow.setEnabled(true);
        while(isTest() && isEnabled())
        {
            getWatchdog().feed();
            
            RobotMap.drive.arcadeDrive(RobotMap.Joystick2);
            legControl();
            
            Timer.delay(0.05);
        }
        RobotMap.DiskInsert.stopLiveWindowMode();
    }

    
    public void legControl()
    {
        boolean legNotUp = RobotMap.LegUp.get();
        System.out.println("LEG NOT UP : " + legNotUp);
        
        boolean legNotDown = RobotMap.LegDown.get();
        System.out.println("LEG NOT DOWN : " + legNotDown);
        
        
        if(RobotMap.Joystick2.getRawButton(8)) {
            if(legNotUp)
            {
                RobotMap.Leg.set(1);
            }
        }
        else
        {
            if(RobotMap.Joystick2.getRawButton(9)) {
                if(legNotDown)
                {
                    RobotMap.Leg.set(-1);
                }
            }
            else {
                RobotMap.Leg.set(0);
            }
        }

    }
}
