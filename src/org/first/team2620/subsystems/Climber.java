package org.first.team2620.subsystems;

import org.first.team2620.RobotMap;

/**
 *
 * @author frc2620
 */
public class Climber {
    
    private int LevelCount_ = 0;
    private int StopClimbLevel_ = 3;
    private double LHClimbPower = RobotMap.ClimbPower;
    private double RHClimbPower = -RobotMap.ClimbPower;
    private double LegPower = RobotMap.LegPower;
    private boolean LHClimb = true;
    private boolean RHClimb = true;

    public void climb()
    {
        // Conveyors Movement Thread
        new Thread(new Runnable() {

            public void run() {
                
                while(LevelCount_ <= StopClimbLevel_)
                {
                    if(RobotMap.LHTopHooked.get() && RobotMap.RHTopHooked.get()) // Both are on top hook
                    {
                        LHClimb = true;
                        RHClimb = true;
                        
                        LHClimbPower *= -1;
                        RHClimbPower *= -1;
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
                                LHClimbPower *= -1;
                                RHClimbPower *= -1;
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
                        RobotMap.LHConveyor.set(LHClimbPower);
                    } else {
                        RobotMap.LHConveyor.set(0);
                    }
                    
                    if(RHClimb) {
                        RobotMap.RHConveyor.set(RHClimbPower);
                    } else {
                        RobotMap.RHConveyor.set(0);
                    }
                }
                
                // Just to make sure we stop climbing
                RobotMap.LHConveyor.set(0);
                RobotMap.RHConveyor.set(0);
            }
            
        }).start();
    
        // Leg Movement Thread
        new Thread(new Runnable() {

            public void run() {
                boolean bringDown = false;
                boolean bringUp = false;
                while(LevelCount_ < StopClimbLevel_)
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
                while(RobotMap.LegUp.get() == false)
                {
                    RobotMap.Leg.set(-LegPower);
                }
                RobotMap.Leg.set(0);
            }
        } ).start();
    }

}
