package edu.curtin.app;

public class EmergencyDetail
{
    private int time;
    private String emerName;
    public String emerLocation;


    public EmergencyDetail(int time, String emerName, String emerLocation)
    {
        this.time = time;
        this.emerName = emerName;
        this.emerLocation = emerLocation;
    }

    public int getTime()
    {
        return time;
    }

    public String getEmerName()
    {
        return emerName;
    }

    public String getEmerLocation()
    {
        return emerLocation;
    }

    @Override
    public String toString()
    {
        return "EmergencyDetail{" +
                "time=" + time +
                ", emerName='" + emerName + '\'' +
                ", emerLocation='" + emerLocation + '\'' +
                '}';
    }

}
