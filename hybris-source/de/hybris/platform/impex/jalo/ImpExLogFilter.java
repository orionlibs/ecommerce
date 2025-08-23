package de.hybris.platform.impex.jalo;

import de.hybris.platform.util.logging.HybrisLogFilter;
import de.hybris.platform.util.logging.HybrisLoggingEvent;
import org.apache.log4j.Level;

public class ImpExLogFilter implements HybrisLogFilter
{
    private static final ThreadLocal<LocationProvider> ACTIVE_THREADS = new ThreadLocal<>();


    public void registerLocationProvider(LocationProvider locProvider)
    {
        ACTIVE_THREADS.set(locProvider);
    }


    public void unregisterLocationProvider()
    {
        ACTIVE_THREADS.remove();
    }


    public HybrisLoggingEvent filterEvent(HybrisLoggingEvent event)
    {
        LocationProvider locProvider = ACTIVE_THREADS.get();
        if(locProvider != null && event.getLevel() != Level.INFO)
        {
            event.setMessage(extendMessage(event.getMessage()));
        }
        return event;
    }


    private Object extendMessage(Object message)
    {
        if(message instanceof String)
        {
            return extendMessage((String)message);
        }
        return message;
    }


    public String extendMessage(String message)
    {
        LocationProvider locProvider = ACTIVE_THREADS.get();
        if(locProvider != null)
        {
            String location = locProvider.getCurrentLocation();
            if(location.length() > 0)
            {
                return location + ": " + location;
            }
        }
        return message;
    }
}
