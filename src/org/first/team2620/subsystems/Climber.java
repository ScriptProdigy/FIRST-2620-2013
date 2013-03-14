package org.first.team2620.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import org.first.team2620.RobotMap;

/**
 *
 * @author frc2620
 */
public class Climber {
    
    private int LevelCount_ = 0;
    private int StopClimbLevel_ = 3;
    
    private boolean previouslyReversed = false;
    
    private double upLeft_ = RobotMap.ClimbPower;
    private double upRight_ = -RobotMap.ClimbPower;
    private double downLeft_ = -RobotMap.ClimbPower;
    private double downRight_ = RobotMap.ClimbPower;
    private double LHClimbPower_ = upLeft_;
    private double RHClimbPower_ = upRight_;
    
    private double LegPower_ = RobotMap.LegPower;
    private boolean LHClimb_ = true;
    private boolean RHClimb_ = true;
    
    private Thread ConveyorsThread_ = null;
    private Thread LegThread_ = null;
    
    private boolean End_ = false;
    
    private boolean Climbing_ = false;

    public void climb()
    {
        if(Climbing_ == false)
        {
            Climbing_ = true;
            
            // Conveyors Movement Thread
            ConveyorsThread_ = new Thread(new Runnable() {

                public void run() {
                    
                    DriverStation inst = DriverStation.getInstance();

                    RobotMap.RHConveyor.set(1);
                    
                    Timer.delay(0.2);
                    
                    RobotMap.RHConveyor.set(0);
                    
                    System.out.println("Conveyors unlatched, press 8 again to start auton climb");
                    while(!RobotMap.Joystick1.getRawButton(8) && End_ == false) {
                        Timer.delay(0.1);
                    }
                    
                    System.out.println("Autonomous climb is beg");
                    
                    while(LevelCount_ <= StopClimbLevel_ && End_ == false && inst.isEnabled())
                    {
                        boolean leftTop = RobotMap.LHTop.get();
                        boolean leftMid = RobotMap.LHMiddle.get();
                        
                        boolean rightTop = RobotMap.RHTop.get();
                        boolean rightMid = RobotMap.RHMiddle.get();
                        
                        if(leftTop && rightTop) // Both are on top hook
                        {
                            LHClimb_ = true;
                            RHClimb_ = true;

                            if(previouslyReversed == false)
                            {
                                previouslyReversed = true;
                                LHClimbPower_ = upLeft_;
                                RHClimbPower_ = upRight_;
                                LevelCount_ += 1;
                            }
                        }
                        else
                        {
                            if(leftMid && rightMid) // Both holding on middle
                            {

                                LHClimb_ = true;
                                RHClimb_ = true;

                                if(previouslyReversed == false)
                                {
                                    previouslyReversed = true;
                                    LHClimbPower_ = downLeft_;
                                    RHClimbPower_ = downRight_;
                                }
                                
                                if(LevelCount_ == StopClimbLevel_)
                                {
                                    LHClimb_ = false;
                                    RHClimb_ = false;
                                } 
                            }
                            else // Middle of climbing, lets keep this bad boy level
                            {
                                previouslyReversed = false;
                                
                                // Level each side out at top
                                if(leftTop && !rightTop) // Left hand is ready to stop, right hand keep climbing to top
                                {
                                    LHClimb_ = false;
                                } 
                                else 
                                {
                                    if(rightTop && !leftTop) // Right hand is ready to stop, left hand keep climbing to top
                                    {
                                        RHClimb_ = false;
                                    }
                                    else
                                    {
                                        // Level each side out at middle
                                        if(leftMid && !rightMid) // Left hand is ready to stop, right hand keep climbing to top
                                        {
                                            LHClimb_ = false;
                                        } 
                                        else 
                                        {
                                            if(rightMid && !leftMid) // Right hand is ready to stop, left hand keep climbing to top
                                            {
                                                RHClimb_ = false;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if(LHClimb_) {
                            RobotMap.LHConveyor.set(LHClimbPower_);
                        } else {
                            RobotMap.LHConveyor.set(0);
                        }

                        if(RHClimb_) {
                            RobotMap.RHConveyor.set(RHClimbPower_);
                        } else {
                            RobotMap.RHConveyor.set(0);
                        }
                        
                        
                        if(leftTop) {
                            System.out.println("LH Top: " + leftTop);
                        }
                        
                        if(leftMid) {
                            System.out.println("LH Middle: " + leftMid);
                        }
                        
                        if(rightTop) {
                            System.out.println("RH Top: " + rightTop);
                        }
                        
                        if(rightMid) {
                            System.out.println("RH Middle: " + rightMid);
                        }
                        Timer.delay(0.1);
                    }
                    
                    System.out.println("!!! Finished climbing! ");

                    // Just to make sure we stop climbing
                    RobotMap.LHConveyor.set(0);
                    RobotMap.RHConveyor.set(0);
                }

            });


            // Leg Movement Thread
            LegThread_ = new Thread(new Runnable() {

                public void run() {

                    boolean bringDown = false;
                    boolean bringUp = false;

                    while(LevelCount_ < StopClimbLevel_ && End_ == false)
                    {
                        if(RobotMap.LHMiddle.get() && RobotMap.RHMiddle.get()) // Both holding on middle, bring leg down
                        {
                            bringDown = true;
                        }

                        if(RobotMap.LHTop.get() && RobotMap.RHTop.get()) // Both are set on top, bring leg up to get out of corners way
                        {
                            bringUp = true;
                        }

                        if(bringDown)
                        {
                            if(RobotMap.LegDown.get() == false)
                            {
                                RobotMap.Leg.set(LegPower_);
                            }
                            else
                            {
                                RobotMap.Leg.set(0);
                                bringDown = false;
                            }
                        }

                        if(bringUp)
                        {
                            if(RobotMap.LegUp.get() == false)
                            {
                                RobotMap.Leg.set(-LegPower_);
                            }
                            else
                            {
                                RobotMap.Leg.set(0);
                                bringDown = false;
                            }
                        }
                        
                        
                        Timer.delay(0.05);
                    }

                    // Bring leg all the way up after it climbs all the way
                    while(!RobotMap.LegUp.get() == false && End_ == false)
                    {
                        RobotMap.Leg.set(-LegPower_);
                        Timer.delay(0.05);
                    }
                    RobotMap.Leg.set(0);
                }
            });


            ConveyorsThread_.start();
            LegThread_.start();
        }
    }
    
    public void overRideClimb()
    {
        End_ = true;
    }
    
    
    public void newMatch()
    {        
        previouslyReversed = false;
        
        upLeft_ = RobotMap.ClimbPower;
        upRight_ = -RobotMap.ClimbPower;
        downLeft_ = -RobotMap.ClimbPower;
        downRight_ = RobotMap.ClimbPower;
        
        LHClimbPower_ = upLeft_;
        RHClimbPower_ = upRight_;
        
        LevelCount_ = 0;
        StopClimbLevel_ = 3;
        
        LegPower_ = RobotMap.LegPower;
        LHClimb_ = true;
        RHClimb_ = true;

        ConveyorsThread_ = null;
        LegThread_ = null;

        End_ = false;

        Climbing_ = false;

    }

}
