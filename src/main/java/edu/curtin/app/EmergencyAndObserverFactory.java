package edu.curtin.app;

import edu.curtin.responders.ResponderComm;

import java.util.List;
import java.util.logging.Logger;

/* To create Emergency and Observer Object by factory and Dependency Injection */
public class EmergencyAndObserverFactory
{
    private static final Logger LOGGER = Logger.getLogger(EmergencyAndObserverFactory.class.getName());

    /* Make Emergency(Subject) Objects */
    public static void makeEmergency(EmergencyDetail emergencyDetail, List<Emergency> emergencyList,ResponderComm responders)
    {
        Emergency emergency;
        Observer observer;

        long startTime = emergencyDetail.getTime();
        String emerName = emergencyDetail.getEmerName();
        String emerLocation = emergencyDetail.getEmerLocation();


        if(emerName.equals("fire"))
        {
            emergency = new Fire(startTime,emerName,emerLocation,responders);
            String logMessage =String.format( "\n  emergency = new Fire(%d,%s,%s,responders) Object Created",startTime,emerName,emerLocation);

            LOGGER.info(logMessage);
        }
        else
        {
            if(emerName.equals("flood"))
            {
                emergency = new Flood(startTime,emerName,emerLocation,responders);
                String logMessage = (String.format("\n  emergency = new Flood(%d,%s,%s,responders) Object Created",startTime,emerName,emerLocation));

                LOGGER.info(logMessage);
            }
            else
            {
                emergency = new Chemical(startTime,emerName,emerLocation,responders);
                String logMessage = (String.format("\n  emergency = new Chemical(%d,%s,%s,responders) Object Created",startTime,emerName,emerLocation));

                LOGGER.info(logMessage);
            }
        }

        /* Create the correct Observer for the given emergency */
        observer = makeObserver(emerName,emergency);

        /* set the connection to emergency object by itself */
        observer.setup();

        /* Finally, add the emergency object into emergencyList */
        emergencyList.add(emergency);

    }

    /* Make Observer Objects */
    public static Observer makeObserver(String emerName, Emergency emergency)
    {
        Observer observer;

        if(emerName.equals("fire"))
        {
            observer = new FireObserver(emergency);
            LOGGER.info("\n  observer = new FireObserver(emergency) Object Created");
        }
        else
        {
            if(emerName.equals("flood"))
            {
                observer = new FloodObserver(emergency);
                LOGGER.info("\n  observer = new FloodObserver(emergency) Object Created");
            }
            else
            {
                observer = new ChemObserver(emergency);
                LOGGER.info("\n  observer = new ChemObserver(emergency) Object Created");
            }
        }

        return observer;
    }
}
