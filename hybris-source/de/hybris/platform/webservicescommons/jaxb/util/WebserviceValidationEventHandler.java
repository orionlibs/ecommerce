package de.hybris.platform.webservicescommons.jaxb.util;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class WebserviceValidationEventHandler implements ValidationEventHandler
{
    public boolean handleEvent(ValidationEvent event)
    {
        if(event == null)
        {
            throw new IllegalArgumentException();
        }
        switch(event.getSeverity())
        {
            case 0:
                retVal = true;
                return retVal;
            case 1:
                retVal = false;
                return retVal;
            case 2:
                retVal = false;
                return retVal;
        }
        boolean retVal = false;
        return retVal;
    }
}
