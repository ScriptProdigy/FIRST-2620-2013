package org.first.team2620.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import org.first.team2620.RobotMap;

/**
 *
 * @author frc2620
 */
public class Shooter {

    public boolean Shooting = false;
     Encoder ShooterAngle_;

    
    public double distanceToAngle(double distance)
    {
        // "-0.5714285714x + 55.7142857143";  x = distance
        double m = -0.5714285714;
        double b = 55.7142857143;
        double angle = (m * distance) + b;
        return angle;
    }
    
    public double angleToDistance(double angle)
    {
        double m = -0.5714285714;
        double b = 55.7142857143;
        double distance = (angle - b) / m;
        return distance;
    }
    
    private void pushRelayThenReverse(final Relay relay, final int delayOut, final int delayIn)
    {
        new Thread(new Runnable() {

                public void run() {
                    try {
                        relay.setDirection(Relay.Direction.kBoth);
                        
                        relay.set(Relay.Value.kForward);
                        relay.set(Relay.Value.kOn);
                        
                        Thread.sleep(delayOut);
                        relay.set(Relay.Value.kOff);
                        
                        relay.set(Relay.Value.kReverse);
                        relay.set(Relay.Value.kOn);
                        
                        Thread.sleep(delayIn);
                        relay.set(Relay.Value.kOff);
                        
                        
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
    }
    
    public void shoot(final double distance)
    {
        if(Shooting == false)
        {
            new Thread(new Runnable() {

                public void run() {
                    try
                    {
                        Shooting = true;
                        
                        double liftAngleNeeded = distanceToAngle(distance);
                        
                        if(liftAngleNeeded < 90 && liftAngleNeeded > 0)
                        {
                            RobotMap.ShooterWheel.set(0.75); // Startup the shooter so that it has time to spin up
                        
                            boolean liftWithinThreshold = false;
                            while(liftWithinThreshold == false)
                            {
                                if(ShooterAngle_.get() > liftAngleNeeded) {
                                    RobotMap.ShooterWheel.set(0.5);
                                }

                                if(ShooterAngle_.get() < liftAngleNeeded) {
                                    RobotMap.ShooterWheel.set(-0.5);
                                }
                                
                                if(ShooterAngle_.get() > (liftAngleNeeded - 0.2) && ShooterAngle_.get() < (liftAngleNeeded + 0.2)) {
                                    liftWithinThreshold = true;
                                }
                            }
                            
                            RobotMap.ShooterWheel.set(0);

                            // TODO: Implement the correct out and in times
                            pushRelayThenReverse(RobotMap.DiskFeeder, 1000, 1000);  

                            Thread.sleep(250);
                            RobotMap.ShooterWheel.set(0);
                        }
                        
                        Shooting = false;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    
    public void insertShot()
    {
        // TODO: Implement the correct out and in times
        pushRelayThenReverse(RobotMap.DiskInsert, 1000, 1000);
    }
}
