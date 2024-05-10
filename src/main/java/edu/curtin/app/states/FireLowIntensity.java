package edu.curtin.app.states;

import edu.curtin.app.Emergency;
import edu.curtin.app.Fire;

public class FireLowIntensity extends EmergencyState
{
    public FireLowIntensity(Emergency emergency)
    {
        super(emergency);
    }

    @Override
    public void respondStatusEvent(boolean isArrived)
    {
        if(emergency.getFireLowCleanupTime() == Fire.FIRE_LOW_CLEANP_TIME)
        {
            emergency.setState(new EndEmergency(emergency));
            emergency.setFireLowCleanupTime(0);

            /* Reported back to responders */

            String message = String.format("%s %s %s",emergency.getEmerType(),"end",emergency.getLocation());
            emergency.getResponders().send(message);

            String logMessage = "Emergency Ended: " + message;
            LOGGER.info(logMessage);
        }
        else
        {
            /* fire has been burning at low-intensity for FIRE_LOW_TO_HIGH_TIME(seconds),unattended */
            if(emergency.getFireLowToHighTime() == Fire.FIRE_LOW_TO_HIGH_TIME && !(isArrived) )
            {
                emergency.setState(new FireHighIntensity(emergency));
                emergency.setFireLowToHighTime(0);

                /* Reported back to responders */

                String message = String.format("%s %s %s",emergency.getEmerType(),"high",emergency.getLocation());
                emergency.getResponders().send(message);

                String logMessage = "Emergency changed to High: " + message;
                LOGGER.info(logMessage);
            }
        }

    }

    @Override
    public void respondLossEvent(boolean isArrived)
    {
        long fireLowCleanupTime = emergency.getFireLowCleanupTime();
        long fireLowToHighTime = emergency.getFireLowToHighTime();

        /* Attended by a Fire Fighting Team */
        if(isArrived)
        {
            fireLowCleanupTime++;
            emergency.setFireLowCleanupTime(fireLowCleanupTime);
        }
        else
        {
            fireLowToHighTime++;
            emergency.setFireLowToHighTime(fireLowToHighTime);
        }

        /* evaluate whether the event Hospitalizing someone actually happens or not by using random numbers */

        if(evaluateOccurrenceOfAnEvent(Fire.FIRE_LOW_CASUALTY_PROB))
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


        /* evaluate whether the event Destroying a Property actually happens or not by using random numbers */

        if(evaluateOccurrenceOfAnEvent(Fire.FIRE_LOW_DAMAGE_PROB))
        {
            long damage = emergency.getDamage();
            damage++;

            /* Update damage count*/
            emergency.setDamage(damage);

            /* Reported back to responders */

            String message = String.format("%s %s %d %s",emergency.getEmerType(),"damage",damage,emergency.getLocation());
            LOGGER.info(message);

            emergency.getResponders().send(message);
        }

    }

    @Override
    public String toString()
    {
        return "FireLowIntensity";
    }
}
