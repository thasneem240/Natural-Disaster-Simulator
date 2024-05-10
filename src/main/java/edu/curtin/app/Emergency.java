package edu.curtin.app;

import edu.curtin.app.states.EmergencyState;
import edu.curtin.responders.ResponderComm;

import java.util.List;

/* Subject interface*/
public interface Emergency
{
    EmergencyState getState();
    String getEmerType();
    String getLocation();
    long getFireLowToHighTime();
    long getFireLowCleanupTime();
    long getFireHighToLowTime();
    long getFloodEndTime();
    long getChemCleanupTime();
    long getCasualty();
    long getDamage();
    long getContam();
    void setFireLowCleanupTime(long fireLowCleanupTime);
    void setFireLowToHighTime(long fireLowToHighTime);
    void setFireHighToLowTime(long fireHighToLowTime);
    void setFloodEndTime(long floodEndTime);
    void setChemCleanupTime(long chemCleanupTime);
    void setState(EmergencyState state);
    void setCasualty(long casualty);
    void setDamage(long damage);
    void setContam(long contam);
    void addObserver(Observer obs);
    void doEmergencies(Long time, List<String> pollMessage);
    ResponderComm getResponders();
}
