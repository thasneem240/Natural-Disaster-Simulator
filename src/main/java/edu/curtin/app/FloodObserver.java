package edu.curtin.app;

import java.util.List;
import java.util.logging.Logger;

public class FloodObserver implements Observer
{
    private static final Logger LOGGER = Logger.getLogger(FloodObserver.class.getName());

    private Emergency flood; // Back reference to subject(Emergency)
    private boolean isArrived = false;

    public FloodObserver(Emergency flood)
    {
        /* Use of Dependency Injection */

        this.flood = flood;
    }

    /* setting up the relationship by itself */
    @Override
    public void setup()
    {
        flood.addObserver(this);
    }

    @Override
    public void emergencyHappened(List<String> pollMessage)
    {
        checkArrivalOrDepart(pollMessage);

        /* State Pattern: To check And Update State */
        flood.getState().respondStatusEvent(isArrived);

        /* State Pattern: To respond the events */
        flood.getState().respondLossEvent(isArrived);

    }


    private void checkArrivalOrDepart(List<String> pollMessage)
    {
        /* check the list is empty or not */
        if(!pollMessage.isEmpty())
        {
            /* Iterate through poll message */
            for (String str: pollMessage)
            {
                String[] splitLine = str.split(" ");

                String emergencyType = splitLine[0].toUpperCase(); // change the String into to uppercase to compare
                String arrivalOrDepart = splitLine[1];
                String emerLocation = "";

                /* Get the emerLocation */
                for (int i = 2; i < splitLine.length; i++)
                {

                    if(i != 2)
                    {
                        emerLocation = emerLocation + " ";
                    }
                    emerLocation = emerLocation + splitLine[i];
                }

                emerLocation = emerLocation.toUpperCase();

                if(emergencyType.equals(flood.getEmerType().toUpperCase()) && arrivalOrDepart.equals("+")
                        && emerLocation.equals(flood.getLocation().toUpperCase()))
                {
                    String logMessage = String.format("Flood rescuers Team Arrived at %s",flood.getLocation());
                    LOGGER.info(logMessage);

                    isArrived = true;
                    break;
                }
                else
                {
                    if(emergencyType.equals(flood.getEmerType().toUpperCase()) && arrivalOrDepart.equals("-")
                            && emerLocation.equals(flood.getLocation().toUpperCase()))
                    {
                        String logMessage = String.format("Flood rescuers Team Departed from %s",flood.getLocation());
                        LOGGER.info(logMessage);

                        isArrived = false;
                        break;
                    }

                }
            }
        }

    }

}
