package edu.curtin.app.states;

import edu.curtin.app.Emergency;

import java.util.Random;
import java.util.logging.Logger;

public abstract class EmergencyState
{
    protected static final Logger LOGGER = Logger.getLogger(EmergencyState.class.getName());

    /* static random class to Generate Random Numbers */
    protected static final Random RAND = new Random();

    protected Emergency emergency;

    /* Using Dependency Injection */
    public EmergencyState(Emergency emergency)
    {
        this.emergency = emergency;
    }

    /* Evaluate the Occurrence of an Event */
    public boolean evaluateOccurrenceOfAnEvent(int probability)
    {
        boolean isOccur = false;

        /* Generate Random Number to evaluate the event occurence */
        /* It will generate numbers between 1-100 */
        int randomNumber = 1 + RAND.nextInt(100);

        if(randomNumber <= probability)
        {
            isOccur = true;
        }

        return isOccur;
    }

    /* Abstract methods */
    public abstract void respondStatusEvent(boolean isArrived);

    public abstract void respondLossEvent(boolean isArrived);


}
