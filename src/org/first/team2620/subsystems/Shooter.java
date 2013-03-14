package org.first.team2620.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
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
        pushServoThenReverse(RobotMap.DiskInsert, 1.0, 100, 0.0);
    }
    
    private void pushServoThenReverse(final Servo servo, final double valueOut, final int delayOut, final double valueIn)
    {
        new Thread(new Runnable() {

                public void run() {
                    try {
                        
                        servo.set(valueOut);
                        
                        Thread.sleep(delayOut);
                        
                        servo.set(valueIn);
                        
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
