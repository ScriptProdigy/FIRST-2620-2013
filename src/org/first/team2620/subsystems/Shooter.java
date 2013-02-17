package org.first.team2620.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import org.first.team2620.RobotMap;

/**
 *
 * @author frc2620
 */
public class Shooter {
    
    public void shoot()
    {
        new Thread(new Runnable() 
        {
            public void run() 
            {
                try
                {
                    // Uncomment below and comment the code below that to enable running
                    // by rpm of shooter instead of constant percentage to motor
//                    boolean inThreshold = false;
//                    double Speed = 0;
//                    
//                    while(!inThreshold)
//                    {
//                        inThreshold = (RobotMap.ShooterWheelEncoder.getRate() < (RobotMap.FullCourtShotRpm) + 10) 
//                            && (RobotMap.ShooterWheelEncoder.getRate() > (RobotMap.FullCourtShotRpm) - 10);
//                    
//                        if(RobotMap.ShooterWheelEncoder.getRate() < RobotMap.FullCourtShotRpm) {
//                            Speed += 0.1;
//                        } else {
//                            Speed += 0.1;
//                        }
//                        Timer.delay(0.01);
//                        
//                        RobotMap.ShooterWheel.set(Speed);
//                    }

                    RobotMap.ShooterWheel.set(RobotMap.ShooterPower);
                    Thread.sleep(200);

                    insertShot();

                    RobotMap.ShooterWheel.set(0);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
