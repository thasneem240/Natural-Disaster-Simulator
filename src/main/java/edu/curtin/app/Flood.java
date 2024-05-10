package edu.curtin.app;

import edu.curtin.app.states.EmergencyState;
import edu.curtin.app.states.FloodIntensity;
import edu.curtin.responders.ResponderComm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Flood implements Emergency
{
    private static final Logger LOGGER = Logger.getLogger(Flood.class.getName());
    public static final int FLOOD_CASUALTY_PROB = 75; // 75%
    public static final int FLOOD_DAMAGE_PROB = 70; // 70%
    public static final long FLOOD_END_TIME = 10;

    private long startTime;
    private String emerType;
    private String location;
    private long floodEndTime;
    private long casualty;
    private long damage;
    private ResponderComm responders;

    /* Always start at FloodIntensity State */
    private EmergencyState state = new FloodIntensity(this);

    private Set<Observer> obsSet = new HashSet<>();


    public Flood(long time, String emerType, String location, ResponderComm responders)
    {
        startTime = time;
        this.emerType = emerType;
        this.location = location;
        floodEndTime = 0;
        casualty = 0;
        damage = 0;

        /* use of Dependency Injection */
        this.responders = responders;
    }

    @Override
    public String getEmerType()
    {
        return emerType;
    }

    @Override
    public String getLocation()
    {
        return location;
    }

    @Override
    public long getFloodEndTime()
    {
        return floodEndTime;
    }

    @Override
    public long getCasualty()
    {
        return casualty;
    }

    @Override
    public long getDamage()
    {
        return damage;
    }

    @Override
    public ResponderComm getResponders()
    {
        return responders;
    }

    @Override
    public EmergencyState getState()
    {
        return state;
    }

    @Override
    public void setState(EmergencyState state)
    {
        this.state = state;
    }

    @Override
    public void setFloodEndTime(long floodEndTime)
    {
        this.floodEndTime = floodEndTime;
    }

    @Override
    public void setCasualty(long casualty)
    {
        this.casualty = casualty;
    }

    @Override
    public void setDamage(long damage)
    {
        this.damage = damage;
    }

    @Override
    public void addObserver(Observer obs)
    {
        obsSet.add(obs);
    }

    public void notifyObservers(List<String> pollMessage )
    {
        for (Observer obs:obsSet)
        {
            obs.emergencyHappened(pollMessage);
        }
    }

    @Override
    public void doEmergencies(Long time, List<String> pollMessage)
    {
        /* Reported back to responders */
        if(startTime == time)
        {
            String message = String.format("%s %s %s",emerType,"start",location);
            String logMessage = "Emergency started" + message;
            LOGGER.info(logMessage);

            responders.send(message);
        }

        if (startTime <= time)
        {
            LOGGER.info("Notified Observers about the Emergency");

            notifyObservers(pollMessage);
        }
    }

    @Override
    public long getChemCleanupTime()
    {
        return 0;
    }


    @Override
    public long getContam()
    {
        return 0;
    }

    @Override
    public long getFireLowToHighTime()
    {
        return 0;
    }

    @Override
    public long getFireLowCleanupTime()
    {
        return 0;
    }

    @Override
    public long getFireHighToLowTime()
    {
        return 0;
    }

    @Override
    public void setChemCleanupTime(long chemCleanupTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public void setFireLowCleanupTime(long fireLowCleanupTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public void setFireLowToHighTime(long fireLowToHighTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public void setFireHighToLowTime(long fireHighToLowTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public void setContam(long contam)
    {
        /* Do nothing just for the interface */
    }
}
