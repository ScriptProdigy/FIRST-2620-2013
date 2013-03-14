package org.first.team2620.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import org.first.team2620.RobotMap;

/**
 *
 * @author frc2620
 */
public class Shooter {
    
    
    public void speedUp()
    {
        if(upToSpeed())
        {
            RobotMap.ShooterWheel.set(0);
        } else {
            RobotMap.ShooterWheel.set(1);
        }
    }
    
    public boolean upToSpeed()
    {
        return (RobotMap.ShooterWheelEncoder.get() >= RobotMap.FullCourtShotRpm);
    }
    
    public void shoot()
    {
        insertShot();
        
        /*new Thread(new Runnable() 
        {
            public void run() 
            {
                try
                {
                    // Uncomment below and comment the code below that to enable running
                    // by rpm of shooter instead of constant percentage to motor

                    // Bang-Bang Control
                    while(RobotMap.ShooterWheelEncoder.get() < RobotMap.FullCourtShotRpm)
                    {
                        RobotMap.ShooterWheel.set(1);
                        Timer.delay(0.01);
                    }
                    
                    //RobotMap.ShooterWheel.set(RobotMap.ShooterPower);
                    insertShot();

                    RobotMap.ShooterWheel.set(0);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
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
                        
                        Thread.sleep(delayOut);
                        relay.set(Relay.Value.kOff);
                        
                        relay.set(Relay.Value.kReverse);
                        
                        Thread.sleep(delayIn);
                        relay.set(Relay.Value.kOff);
                        
                        
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
    }

    public void newMatch()
    {
        
    }
}
