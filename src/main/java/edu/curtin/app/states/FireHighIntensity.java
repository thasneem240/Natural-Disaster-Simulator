package edu.curtin.app.states;

import edu.curtin.app.Emergency;
import edu.curtin.app.Fire;

public class FireHighIntensity extends EmergencyState
{
    public FireHighIntensity(Emergency emergency)
    {
        super(emergency);
    }

    @Override
    public void respondStatusEvent(boolean isArrived)
    {
        if(emergency.getFireHighToLowTime() == Fire.FIRE_HIGH_TO_LOW_TIME)
        {
            emergency.setState(new FireLowIntensity(emergency));
            emergency.setFireHighToLowTime(0);

            /* Reported back to responders */

            String message = String.format("%s %s %s",emergency.getEmerType(),"low",emergency.getLocation());
            String logMessage = "Emergency changed to Low: " + message;
            LOGGER.info(logMessage);

            emergency.getResponders().send(message);
        }
    }

    @Override
    public void respondLossEvent(boolean isArrived)
    {
        long fireHighToLowTime = emergency.getFireHighToLowTime();

        if(isArrived)
        {
            fireHighToLowTime++;
            emergency.setFireHighToLowTime(fireHighToLowTime);
        }

        /* evaluate whether the event Hospitalizing someone actually happens or not by using random numbers */

        if(evaluateOccurrenceOfAnEvent(Fire.FIRE_HIGH_CASUALTY_PROB))
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

        if(evaluateOccurrenceOfAnEvent(Fire.FIRE_HIGH_DAMAGE_PROB))
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
        return "FireHighIntensity";
    }
}
