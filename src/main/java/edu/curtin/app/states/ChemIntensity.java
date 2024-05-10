package edu.curtin.app.states;

import edu.curtin.app.Chemical;
import edu.curtin.app.Emergency;

public class ChemIntensity extends EmergencyState
{
    public ChemIntensity(Emergency emergency)
    {
        super(emergency);
    }

    @Override
    public void respondStatusEvent(boolean isArrived)
    {
        if(emergency.getChemCleanupTime() == Chemical.CHEM_CLEANUP_TIME)
        {
            emergency.setState(new EndEmergency(emergency));
            emergency.setChemCleanupTime(0);

            /* Reported back to responders */

            String message = String.format("%s %s %s",emergency.getEmerType(),"end",emergency.getLocation());
            String logMessage = "Emergency Ended: " + message;
            LOGGER.info(logMessage);

            emergency.getResponders().send(message);
        }
    }

    @Override
    public void respondLossEvent(boolean isArrived)
    {
        long chemCleanupTime = emergency.getChemCleanupTime();

        /* Attended by a Chemical cleanup Team */
        if(isArrived)
        {
            chemCleanupTime++;
            emergency.setChemCleanupTime(chemCleanupTime);
        }


        /* evaluate whether the event Hospitalizing someone actually happens or not by using random numbers */

        if(evaluateOccurrenceOfAnEvent(Chemical.CHEM_CASUALTY_PROB))
        {
            long casualty = emergency.getCasualty();
            casualty++;

            /* Update damage count*/
            emergency.setCasualty(casualty);

            /* Reported back to responders */

            String message = String.format("%s %s %d %s",emergency.getEmerType(),"casualty",casualty,emergency.getLocation());
            LOGGER.info(message);

            emergency.getResponders().send(message);
        }


        /* evaluate whether the event causing environmental contamination. */

        if(evaluateOccurrenceOfAnEvent(Chemical.CHEM_CONTAM_PROB))
        {
            long contam = emergency.getContam();
            contam++;

            /* Update damage count*/
            emergency.setContam(contam);

            /* Reported back to responders */

            String message = String.format("%s %s %d %s",emergency.getEmerType(),"contam",contam,emergency.getLocation());
            LOGGER.info(message);

            emergency.getResponders().send(message);
        }
    }

    @Override
    public String toString()
    {
        return "ChemIntensity";
    }
}
