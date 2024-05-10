package edu.curtin.app.states;

import edu.curtin.app.Emergency;
import edu.curtin.app.Flood;

public class FloodIntensity extends EmergencyState
{
    public FloodIntensity(Emergency emergency)
    {
        super(emergency);
    }

    @Override
    public void respondStatusEvent(boolean isArrived)
    {
        if(emergency.getFloodEndTime() == Flood.FLOOD_END_TIME)
        {
            emergency.setState(new EndEmergency(emergency));
            emergency.setFloodEndTime(0);

            /* Reported back to responders */

            String message = String.format("%s %s %s",emergency.getEmerType(),"end",emergency.getLocation());
            emergency.getResponders().send(message);

            String logMessage = "Emergency Ended: " + message;
            LOGGER.info(logMessage);
        }
    }

    @Override
    public void respondLossEvent(boolean isArrived)
    {
        long floodEndTime = emergency.getFloodEndTime();

        /* flood emergency will dissipate on its own in FLOOD_END_TIME time */
        floodEndTime++;
        emergency.setFloodEndTime(floodEndTime);

        /* evaluate whether the event Destroying a Property actually happens or not by using random numbers */

        if(evaluateOccurrenceOfAnEvent(Flood.FLOOD_DAMAGE_PROB) )
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

        /*  When flood rescuers are absent there is a FLOOD_CASUALTY_PROB probability of
                    someone needing hospitalisation */
        if(!isArrived)
        {
            /* evaluate whether the event Hospitalizing someone actually happens or not by using random numbers */

            if(evaluateOccurrenceOfAnEvent(Flood.FLOOD_CASUALTY_PROB))
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

        }

    }

    @Override
    public String toString()
    {
        return "FloodIntensity";
    }
}
