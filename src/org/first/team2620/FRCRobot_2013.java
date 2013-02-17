/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.first.team2620;


import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.first.team2620.subsystems.CameraDistanceCalc;
import org.first.team2620.subsystems.Climber;
import org.first.team2620.subsystems.Shooter;


public class FRCRobot_2013 extends SimpleRobot 
{
    
    public CameraDistanceCalc distanceCalc;
    public Shooter shooter;

    public Climber climber;
        
    public void robotInit()
    {
        distanceCalc = null; //new CameraDistanceCalc("10.26.20.11");
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
        while(this.isOperatorControl() && isEnabled())
        {
            getWatchdog().feed();
            RobotMap.drive.tankDrive(RobotMap.Joystick1.getY(), RobotMap.Joystick2.getY());
            shooter.insertShot();
            
            if(RobotMap.Joystick1.getRawButton(1))
            {
                // TODO: get correct equation.
                shooter.shoot(SmartDashboard.getNumber("range", 50));  
            }
            
            if(RobotMap.Joystick1.getRawButton(8))
            {
                climber.climb();
            }
        }
    }
    
    
    public void test() 
    {
        LiveWindow.setEnabled(true);
        
        while(this.isTest() && isEnabled())
        {
            getWatchdog().feed();
            RobotMap.drive.tankDrive(RobotMap.Joystick1.getY() * RobotMap.DriveDirection, RobotMap.Joystick2.getY() * RobotMap.DriveDirection);
            
            if(RobotMap.Joystick1.getRawButton(11))
            {
                RobotMap.Leg.set(1);
            }
            else
            {
                if(RobotMap.Joystick1.getRawButton(10))
                {
                    RobotMap.Leg.set(-1);
                }
                else
                {
                    RobotMap.Leg.set(0);
                }
            }
            
            if(RobotMap.Joystick1.getRawButton(6)) {
                RobotMap.DriveDirection = 1;
            }
            
            if(RobotMap.Joystick1.getRawButton(7)) {
                RobotMap.DriveDirection = -1;
            }
            
            double ConveyorSpeed = (RobotMap.Joystick1.getThrottle() + 1) / 2; // from (-1 to 1) to (1, 0)
            if(RobotMap.Joystick1.getRawButton(1)) {
                RobotMap.LHConveyor.set(ConveyorSpeed);
                RobotMap.RHConveyor.set(-ConveyorSpeed);
                System.out.println("Conveyor Speed: " + ConveyorSpeed);
            }
            else
            {
                if(RobotMap.Joystick2.getRawButton(1)) {
                    RobotMap.LHConveyor.set(-ConveyorSpeed);
                    RobotMap.RHConveyor.set(ConveyorSpeed);
                    System.out.println("Conveyor Speed: " + ConveyorSpeed);
                }
                else
                {
                    RobotMap.LHConveyor.set(0);
                    RobotMap.RHConveyor.set(0);
                }
            }
            
            if(RobotMap.Joystick2.getRawButton(8)) {
                RobotMap.ShooterWheel.set(0.75);
            }
        }
        LiveWindow.setEnabled(false);
    }
}
