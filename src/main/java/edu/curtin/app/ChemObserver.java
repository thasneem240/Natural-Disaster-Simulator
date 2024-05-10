package edu.curtin.app;

import java.util.List;
import java.util.logging.Logger;

public class ChemObserver implements Observer
{
    private static final Logger LOGGER = Logger.getLogger(ChemObserver.class.getName());

    private Emergency chemical; // Back reference to subject(Emergency)
    private boolean isArrived = false;
    private boolean isDeparted = false;

    public ChemObserver(Emergency chemical)
    {
        /* Use of Dependency Injection */

        this.chemical = chemical;
    }

    @Override
    public void setup()
    {
        chemical.addObserver(this);
    }

    @Override
    public void emergencyHappened(List<String> pollMessage)
    {
        /* Check Arrival or Depart of responders */
        checkArrivalOrDepart(pollMessage);

        /* State Pattern: To check And Update State */
        chemical.getState().respondStatusEvent(isArrived);

        /* Reset chemical cleanup time When responders left */
        if(isDeparted)
        {
            LOGGER.info(" Reset Chemical cleanup Time");
            chemical.setChemCleanupTime(0);
        }


        /* State Pattern: To respond the events */
        chemical.getState().respondLossEvent(isArrived);

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

                if(emergencyType.equals(chemical.getEmerType().toUpperCase()) && arrivalOrDepart.equals("+")
                        && emerLocation.equals(chemical.getLocation().toUpperCase()))
                {
                    String logMessage = String.format("Chemical Cleanup Team Arrived at %s",chemical.getLocation());
                    LOGGER.info(logMessage);

                    isArrived = true;
                    isDeparted = false;
                    break;
                }
                else
                {
                    if(emergencyType.equals(chemical.getEmerType().toUpperCase()) && arrivalOrDepart.equals("-")
                            && emerLocation.equals(chemical.getLocation().toUpperCase()))
                    {
                        String logMessage = String.format("Chemical Cleanup Team Departed From %s",chemical.getLocation());
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
