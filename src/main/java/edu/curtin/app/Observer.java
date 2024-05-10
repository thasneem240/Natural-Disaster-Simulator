package edu.curtin.app;

import java.util.List;

public interface Observer
{
    void emergencyHappened(List<String> pollMessage);

    void setup();
}
