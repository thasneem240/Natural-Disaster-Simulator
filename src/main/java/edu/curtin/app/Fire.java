package edu.curtin.app;

import edu.curtin.app.states.EmergencyState;
import edu.curtin.app.states.FireLowIntensity;
import edu.curtin.responders.ResponderComm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Fire implements Emergency
{
    private static final Logger LOGGER = Logger.getLogger(Fire.class.getName());
    public static final long FIRE_LOW_TO_HIGH_TIME = 5;
    public static final long FIRE_LOW_CLEANP_TIME = 5;
    public static final long FIRE_HIGH_TO_LOW_TIME = 5;
    public static final int FIRE_LOW_CASUALTY_PROB = 25; // 25%
    public static final int FIRE_LOW_DAMAGE_PROB = 30; // 30%
    public static final int FIRE_HIGH_CASUALTY_PROB = 75; // 75%
    public static final int FIRE_HIGH_DAMAGE_PROB = 70; // 70%

    private long startTime;
    private long fireLowToHighTime;
    private long fireHighToLowTime;
    private long fireLowCleanupTime;

    private long casualty;
    private long damage;
    private ResponderComm responders;

    private String emerType;
    private String location;

    /* Always start at FireLowIntensity State */
    private EmergencyState state = new FireLowIntensity(this);

    private Set<Observer> obsSet = new HashSet<>();

    public Fire(long time, String emerType, String location, ResponderComm responders)
    {
        startTime = time;
        this.emerType = emerType;
        this.location = location;
        fireLowToHighTime = 0;
        fireLowCleanupTime = 0;
        fireHighToLowTime = 0;
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
    public long getFireLowToHighTime()
    {
        return fireLowToHighTime;
    }

    @Override
    public long getFireLowCleanupTime()
    {
        return fireLowCleanupTime;
    }

    @Override
    public long getFireHighToLowTime()
    {
        return fireHighToLowTime;
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
    public EmergencyState getState()
    {
        return state;
    }

    @Override
    public ResponderComm getResponders()
    {
        return responders;
    }

    @Override
    public void setFireLowCleanupTime(long fireLowCleanupTime)
    {
        this.fireLowCleanupTime = fireLowCleanupTime;
    }

    @Override
    public void setFireHighToLowTime(long fireHighToLowTime)
    {
        this.fireHighToLowTime = fireHighToLowTime;
    }

    @Override
    public void setFireLowToHighTime(long fireLowToHighTime)
    {
        this.fireLowToHighTime = fireLowToHighTime;
    }

    @Override
    public void setState(EmergencyState state)
    {
        this.state = state;
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
    public long getFloodEndTime()
    {
        return 0;
    }

    @Override
    public void setFloodEndTime(long floodEndTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public long getChemCleanupTime()
    {
        return 0;
    }

    @Override
    public void setChemCleanupTime(long chemCleanupTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public long getContam()
    {
        return 0;
    }

    @Override
    public void setContam(long contam)
    {
        /* Do nothing just for the interface */
    }

}
