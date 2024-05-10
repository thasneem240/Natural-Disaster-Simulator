package edu.curtin.app;

import edu.curtin.app.states.ChemIntensity;
import edu.curtin.app.states.EmergencyState;
import edu.curtin.responders.ResponderComm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Chemical implements Emergency
{
    private static final Logger LOGGER = Logger.getLogger(Chemical.class.getName());
    public static final int CHEM_CASUALTY_PROB = 80; // 80%
    public static final int CHEM_CONTAM_PROB = 70; // 70%
    public static final long CHEM_CLEANUP_TIME = 12;

    private long startTime;
    private String emerType;
    private String location;
    private long chemCleanupTime;
    private long casualty;
    private long contam;
    private ResponderComm responders;

    /* Always start at ChemIntensity State */
    private EmergencyState state = new ChemIntensity(this);

    private Set<Observer> obsSet = new HashSet<>();


    public Chemical(long time, String emerType, String location, ResponderComm responders)
    {
        startTime = time;
        this.emerType = emerType;
        this.location = location;
        chemCleanupTime = 0;
        casualty = 0;
        contam = 0;

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
    public long getChemCleanupTime()
    {
        return chemCleanupTime;
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
    public void setState(EmergencyState state)
    {
        this.state = state;
    }

    @Override
    public void setChemCleanupTime(long chemCleanupTime)
    {
        this.chemCleanupTime = chemCleanupTime;
    }

    @Override
    public void setCasualty(long casualty)
    {
        this.casualty = casualty;
    }

    @Override
    public void setContam(long contam)
    {
        this.contam = contam;
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
    public long getFloodEndTime()
    {
        return 0;
    }

    @Override
    public long getCasualty()
    {
        return casualty;
    }

    @Override
    public long getContam()
    {
        return contam;
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
    public void setFloodEndTime(long floodEndTime)
    {
        /* Do nothing just for the interface */
    }

    @Override
    public long getDamage()
    {
        return 0;
    }

    @Override
    public void setDamage(long damage)
    {
        /* Do nothing just for the interface */
    }

}