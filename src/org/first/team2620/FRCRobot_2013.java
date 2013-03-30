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
import org.first.team2620.subsystems.Climber;
import org.first.team2620.subsystems.Shooter;


public class FRCRobot_2013 extends SimpleRobot 
{
    public Shooter shooter;
    public Climber climber;
    public boolean ManualClimb = false;
    public SendableChooser auton;
    
    
    public int Teleop_MoveToPosition = 0;
    public boolean Teleop_Move = false;
    public boolean Teleop_MoveUp = true;
    public int threshold = 10; 
    public int[] ShooterPositions = new int[] { /* back left */ 291, /* back right */ 291, /* front left */ 301, /* front right */ 301 };
        
        
    public void robotInit()
    {
        auton = new SendableChooser();
        auton.addDefault("Back Left", (Object)String.valueOf(ShooterPositions[0]));
        auton.addObject("Back Right", (Object)String.valueOf(ShooterPositions[1]));
        auton.addObject("Front Left", (Object)String.valueOf(ShooterPositions[2]));
        auton.addObject("Front Right", (Object)String.valueOf(ShooterPositions[3]));
        SmartDashboard.putData("Autonomous Mode", auton);
        
        shooter = new Shooter();
        climber = new Climber();
    }
    
    public void autonomous() 
    {
            
        RobotMap.ShooterWheel.set(1);
        RobotMap.DiskInsert.set(0.1);
        
        int valueReq = Integer.parseInt(auton.getSelected().toString()); //291;
        System.out.println("Auton Angle Needed: " + valueReq);
        
        int shootCount = 0;
        int totalShootCount = 6;
        
        while(isAutonomous())
        {
            getWatchdog().feed();
            
            RobotMap.drive.tankDrive(0, 0);
            //System.out.println("VALUE: " + RobotMap.ShooterAngle.getValue());
            
            
            int angle = RobotMap.ShooterAngle.getValue();
            boolean belowThreshold = valueReq > (angle-(threshold/2));
            boolean aboveThreshold = (angle+(threshold/2)) > valueReq;
            
            if(belowThreshold)
            {
                //System.out.println("Lift Up");
                shooter.liftUp();
            }
            else
            {
                /*if(aboveThreshold)
                {
                    System.out.println("Lift Down");
                    shooter.liftDown();
                }
                else
                {*/
                    shooter.stopLift();
                    
                    if(shootCount < totalShootCount)
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
                //}
            }
        }
    }
    // 243
    // 397

    public void moveToPosition(int pos)
    {
    /*  0 = Back Left
         *   1 = Back Right
         *   2 = Front Left
         *   3 = Front Right
         */
        int valueReq = pos;
        
        int angle = RobotMap.ShooterAngle.getValue();
        boolean belowThreshold = valueReq > (angle-(threshold/2));
        boolean aboveThreshold = (angle+(threshold/2)) > valueReq;
        
        if(Teleop_MoveUp)
        {
            if(belowThreshold)
            {
                //System.out.println("Lift Up");
                shooter.liftUp();
            }
            else
            {
                shooter.stopLift();
            }
        }
        else
        {
            if(aboveThreshold)
            {
                //System.out.println("Lift Down");
                shooter.liftDown();
            }
            else
            {
                shooter.stopLift();
            }
        }
        
    }
    
    public void operatorControl() 
    {
        climber.newMatch();
        shooter.newMatch();
        
        while(isOperatorControl() && isEnabled())
        {
            getWatchdog().feed();
            
            //System.out.println("SHOOTER VALUE  ENCODER: " + RobotMap.ShooterAngle.getValue());
            //System.out.println("SHOOTER ANALOG ENCODER: " + RobotMap.ShooterAngle.getVoltage());
            
            // Drive
            drive();
            
            // Shooter Lift
            
            if(RobotMap.Joystick2.getRawButton(6) || RobotMap.Joystick2.getRawButton(7) ||
                    RobotMap.Joystick2.getRawButton(11) || RobotMap.Joystick2.getRawButton(10))
            {
                Teleop_Move = true;
                if(RobotMap.Joystick2.getRawButton(6))
                {
                    Teleop_MoveToPosition = ShooterPositions[0];
                }
                else if(RobotMap.Joystick2.getRawButton(7))
                {
                    Teleop_MoveToPosition = ShooterPositions[1];
                }
                else if(RobotMap.Joystick2.getRawButton(10))
                {
                    Teleop_MoveToPosition = ShooterPositions[2];
                }
                else if(RobotMap.Joystick2.getRawButton(11))
                {
                    Teleop_MoveToPosition = ShooterPositions[3];
                }
                
                if(RobotMap.ShooterAngle.getValue() > Teleop_MoveToPosition)
                {
                    Teleop_MoveUp = false;
                }
                else
                {
                    Teleop_MoveUp = true;
                }
            }
            
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
                moveToPosition(Teleop_MoveToPosition);
            }
            
            if(RobotMap.Joystick2.getRawButton(4))
            {
                RobotMap.DiskInsert.set(1);
            }
            else
            {
                RobotMap.DiskInsert.set(0.1);
            }
            
            if(RobotMap.Joystick2.getRawButton(1))
            {
                shooter.speedUp();
            }
            else
            {
                shooter.stop();
            }

            manualConveyorControl();
            
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
            
            drive();
           manualConveyorControl();
            
            if(RobotMap.Joystick2.getRawButton(2)) {
                RobotMap.ShooterWheel.set(0.75);
            } else {
                RobotMap.ShooterWheel.set(0);
            }
            
            Timer.delay(0.05);
        }
        RobotMap.DiskInsert.stopLiveWindowMode();
    }

    
    public void manualConveyorControl()
    {
        
        boolean legNotUp = RobotMap.LegUp.get();
        boolean legNotDown = RobotMap.LegDown.get();
        if(RobotMap.Joystick2.getRawButton(8)) {
            System.out.println("LEG UP: " + legNotUp);
            if(legNotUp)
            {
                RobotMap.Leg.set(1);
            }
        }
        else
        {
            if(RobotMap.Joystick2.getRawButton(9)) {
                System.out.println("LEG DOWN: " + legNotDown);
                if(legNotDown)
                {
                    RobotMap.Leg.set(-1);
                }
            }
            else {
                RobotMap.Leg.set(0);
            }
        }

        /*double ConveyorSpeed = 1;
        if(RobotMap.Joystick1.getRawButton(11))
        {
            RobotMap.RHConveyor.set(-ConveyorSpeed);
        }
        else 
        {
            if(RobotMap.Joystick1.getRawButton(10)) 
            {
                RobotMap.RHConveyor.set(ConveyorSpeed);
            }
            else
            {
                RobotMap.RHConveyor.set(0);
            }
        }
        
        if(RobotMap.Joystick1.getRawButton(6)) 
        {
            RobotMap.LHConveyor.set(ConveyorSpeed);
        }
        else
        {
            if(RobotMap.Joystick1.getRawButton(7)) 
            {
                RobotMap.LHConveyor.set(-ConveyorSpeed);
            }
            else
            {
                RobotMap.LHConveyor.set(0);
            }
        }*/
    }
    
    public void drive()
    {
        // RobotMap.drive.tankDrive(-RobotMap.Joystick1.getY(), -RobotMap.Joystick2.getY());
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        RobotMap.drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        
        RobotMap.drive.arcadeDrive(RobotMap.Joystick2);
    }


}
