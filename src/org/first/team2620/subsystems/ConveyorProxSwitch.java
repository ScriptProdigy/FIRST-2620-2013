package org.first.team2620.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author frc2620
 */
public class ConveyorProxSwitch 
{
    private DigitalInput dio;
    public ConveyorProxSwitch(int DIO)
    {
        dio = new DigitalInput(DIO);
    }
    
    public boolean get()
    {
        return !dio.get();
    }
    
}
