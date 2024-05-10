package edu.curtin.app.states;

import edu.curtin.app.Emergency;

public class EndEmergency extends EmergencyState
{
    public EndEmergency(Emergency emergency)
    {
        super(emergency);
    }

    @Override
    public void respondStatusEvent(boolean isArrived)
    {
       /* Do nothing */
    }

    @Override
    public void respondLossEvent(boolean isArrived)
    {
        /* Do nothing */
    }

    @Override
    public String toString()
    {
        return "EndEmergency";
    }
}
