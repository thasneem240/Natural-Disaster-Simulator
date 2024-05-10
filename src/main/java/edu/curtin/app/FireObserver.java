package edu.curtin.app;

import java.util.List;
import java.util.logging.Logger;

public class FireObserver implements Observer
{
    private static final Logger LOGGER = Logger.getLogger(FireObserver.class.getName());

    private Emergency fire; // Back reference to subject(Emergency)
    private boolean isArrived = false;
    private boolean isDeparted = false;

    public FireObserver(Emergency fire)
    {
        this.fire = fire;
    }

    @Override
    public void setup()
    {
        /* Use of Dependency Injection */

        fire.addObserver(this);
    }

    @Override
    public void emergencyHappened(List<String> pollMessage)
    {

        checkArrivalOrDepart(pollMessage);

        /* State Pattern: To check And Update State */
        fire.getState().respondStatusEvent(isArrived);

        /* Reset All the cleanup time When responders left */
        if(isDeparted)
        {
            LOGGER.info(" Reset fire cleanup Times");
            fire.setFireLowCleanupTime(0);
            fire.setFireHighToLowTime(0);
        }

        /* State Pattern: To respond the events */
        fire.getState().respondLossEvent(isArrived);

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

                /* change the String into to uppercase to compare */
                String emergencyType = splitLine[0].toUpperCase();
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

                if(emergencyType.equals(fire.getEmerType().toUpperCase()) && arrivalOrDepart.equals("+")
                        && emerLocation.equals(fire.getLocation().toUpperCase()))
                {
                    String logMessage = String.format("Fire Fighters Team Arrived at %s",fire.getLocation());
                    LOGGER.info(logMessage);

                    isArrived = true;
                    isDeparted = false;
                    break;
                }
                else
                {
                    if(emergencyType.equals(fire.getEmerType().toUpperCase()) && arrivalOrDepart.equals("-")
                            && emerLocation.equals(fire.getLocation().toUpperCase()))
                    {
                        String logMessage = String.format("Fire Fighters Team Departed from %s",fire.getLocation());
                        LOGGER.info(logMessage);

                        isArrived = false;
                        isDeparted = true;
                        break;
                    }

                }
            }
        }

    }

}
