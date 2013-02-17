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
    
    public void shoot(final double distance)
    {
        if(Shooting == false)
        {
            new Thread(new Runnable() {

                public void run() {
                    try
                    {
                        Shooting = true;

                        RobotMap.ShooterWheel.set(1);
                        Thread.sleep(200);
                        
                        insertShot();

                        RobotMap.ShooterWheel.set(0);

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
        pushRelayThenReverse(RobotMap.DiskInsert, 100, 100);
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
}
