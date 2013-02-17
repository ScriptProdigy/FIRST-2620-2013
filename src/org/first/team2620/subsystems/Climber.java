package org.first.team2620.subsystems;

import org.first.team2620.RobotMap;

/**
 *
 * @author frc2620
 */
public class Climber {
    
    private int LevelCount_ = 0;
    private int StopClimbLevel_ = 3;
    private double LHClimbPower_ = RobotMap.ClimbPower;
    private double RHClimbPower_ = -RobotMap.ClimbPower;
    private double LegPower = RobotMap.LegPower;
    private boolean LHClimb = true;
    private boolean RHClimb = true;
    
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

                    while(LevelCount_ <= StopClimbLevel_ && End_ == false)
                    {
                        if(RobotMap.LHTopHooked.get() && RobotMap.RHTopHooked.get()) // Both are on top hook
                        {
                            LHClimb = true;
                            RHClimb = true;

                            LHClimbPower_ *= -1;
                            RHClimbPower_ *= -1;
                            LevelCount_ += 1;
                        }
                        else
                        {
                            if(RobotMap.LHMiddleHooked.get() && RobotMap.RHMiddleHooked.get()) // Both holding on middle
                            {
                                LHClimb = true;
                                RHClimb = true;

                                if(LevelCount_ == StopClimbLevel_)
                                {
                                    LHClimb = false;
                                    RHClimb = false;
                                } 
                                else
                                {
                                    LHClimbPower_ *= -1;
                                    RHClimbPower_ *= -1;
                                }
                            }
                            else // Middle of climbing, lets keep this bad boy level
                            {
                                // Level each side out at top
                                if(RobotMap.LHTopHooked.get() && (RobotMap.RHTopHooked.get() == false)) // Left hand is ready to stop, right hand keep climbing to top
                                {
                                    LHClimb = false;
                                } 
                                else if(RobotMap.RHTopHooked.get() && (RobotMap.LHTopHooked.get() == false)) // Right hand is ready to stop, left hand keep climbing to top
                                {
                                    RHClimb = false;
                                }

                                // Level each side out at middle
                                if(RobotMap.LHMiddleHooked.get() && (RobotMap.RHMiddleHooked.get() == false)) // Left hand is ready to stop, right hand keep climbing to top
                                {
                                    LHClimb = false;
                                } 
                                else if(RobotMap.RHMiddleHooked.get() && (RobotMap.LHMiddleHooked.get() == false)) // Right hand is ready to stop, left hand keep climbing to top
                                {
                                    RHClimb = false;
                                }
                            }
                        }

                        if(LHClimb) {
                            RobotMap.LHConveyor.set(LHClimbPower_);
                        } else {
                            RobotMap.LHConveyor.set(0);
                        }

                        if(RHClimb) {
                            RobotMap.RHConveyor.set(RHClimbPower_);
                        } else {
                            RobotMap.RHConveyor.set(0);
                        }
                    }

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
                        if(RobotMap.LHMiddleHooked.get() && RobotMap.RHMiddleHooked.get()) // Both holding on middle, bring leg down
                        {
                            bringDown = true;
                        }

                        if(RobotMap.LHTopHooked.get() && RobotMap.RHTopHooked.get()) // Both are set on top, bring leg up to get out of corners way
                        {
                            bringUp = true;
                        }

                        if(bringDown)
                        {
                            if(RobotMap.LegDown.get() == false)
                            {
                                RobotMap.Leg.set(LegPower);
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
                                RobotMap.Leg.set(-LegPower);
                            }
                            else
                            {
                                RobotMap.Leg.set(0);
                                bringDown = false;
                            }
                        }

                    }

                    // Bring leg all the way up after it climbs all the way
                    while(RobotMap.LegUp.get() == false && End_ == false)
                    {
                        RobotMap.Leg.set(-LegPower);
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

}
