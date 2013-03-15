/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.first.team2620;


import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.first.team2620.subsystems.Climber;
import org.first.team2620.subsystems.Shooter;


public class FRCRobot_2013 extends SimpleRobot 
{
    public Shooter shooter;
    public Climber climber;
    public boolean ManualClimb = false;
        
    public void robotInit()
    {
        shooter = new Shooter();
        climber = new Climber();
    }
    
    public void autonomous() 
    {
        while(isAutonomous())
        {
            getWatchdog().feed();
        }
    }

    
    public void operatorControl() 
    {
        climber.newMatch();
        shooter.newMatch();
        
        while(isOperatorControl() && isEnabled())
        {
            getWatchdog().feed();
            
            // Drive
            drive();
            
            // Shooter Lift
            if(RobotMap.Joystick2.getRawButton(2))
            {
                shooter.liftUp();
            } 
            else
            {
                if(RobotMap.Joystick2.getRawButton(3))
                {
                    shooter.liftDown();
                } 
                else
                {
                    shooter.stopLift();
                }
            }
            
            // Shooter
            if(RobotMap.Joystick2.getRawButton(1)) {
                if(shooter.upToSpeed()) {
                    shooter.shoot();  
                } else {
                    shooter.speedUp();
                }
            } else {
                shooter.stop();
            }
            
            // Climber
            if(RobotMap.Joystick1.getRawButton(8))
            {
                if(RobotMap.Joystick1.getRawButton(8) & RobotMap.Joystick1.getRawButton(9)) {
                    // Manual override of conveyors in case of emergencies. 
                    ManualClimb = true;
                    climber.overRideClimb();
                    manualConveyorControl();
                } else {
                    if(ManualClimb == false)
                    {
                        climber.climb();
                    }
                    else
                    {
                        climber.overRideClimb();
                        manualConveyorControl();
                    }
                }
            }
            
            Timer.delay(0.05);
        }
    }
    
    
    public void test() 
    {
        LiveWindow.setEnabled(false);
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
    }
    
    
    public void manualConveyorControl()
    {
        if(RobotMap.Joystick1.getRawButton(6)) {
            RobotMap.Leg.set(1);
        }
        else
        {
            if(RobotMap.Joystick1.getRawButton(7)) {
                RobotMap.Leg.set(-1);
            }
            else {
                RobotMap.Leg.set(0);
            }
        }

        double ConveyorSpeed = (RobotMap.Joystick1.getThrottle() + 1) / 2; // from (-1 to 1) to (1, 0)
        if(RobotMap.Joystick1.getRawButton(11)) {
            RobotMap.LHConveyor.set(ConveyorSpeed);
            RobotMap.RHConveyor.set(-ConveyorSpeed);
            //System.out.println("Conveyor Speed: " + ConveyorSpeed);
        }
        else
        {
            if(RobotMap.Joystick1.getRawButton(10)) {
                RobotMap.LHConveyor.set(-ConveyorSpeed);
                RobotMap.RHConveyor.set(ConveyorSpeed);
                //System.out.println("Conveyor Speed: " + ConveyorSpeed);
            }
            else
            {
                RobotMap.LHConveyor.set(0);
                RobotMap.RHConveyor.set(0);
            }
        }
    }

    
    public void drive()
    {
        /*if(RobotMap.Joystick1.getRawButton(4)) {
            RobotMap.DriveDirection = 1;
        }

        if(RobotMap.Joystick1.getRawButton(5)) {
            RobotMap.DriveDirection = -1;
        }

        RobotMap.drive.tankDrive(RobotMap.Joystick1.getY() * RobotMap.DriveDirection, RobotMap.Joystick2.getY() * RobotMap.DriveDirection);*/
        RobotMap.drive.tankDrive(RobotMap.Joystick1.getY(), RobotMap.Joystick2.getY());
    }


}
