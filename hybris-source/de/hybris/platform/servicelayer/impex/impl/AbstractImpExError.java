package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExError;
import de.hybris.platform.servicelayer.impex.ProcessMode;

public abstract class AbstractImpExError implements ImpExError
{
    public ProcessMode getMode()
    {
        ProcessMode processMode;
        EnumerationValue mode = getInternalMode();
        switch(mode.getCode())
        {
            case "insert":
                processMode = ProcessMode.INSERT;
                return processMode;
            case "update":
                processMode = ProcessMode.UPDATE;
                return processMode;
            case "remove":
                processMode = ProcessMode.REMOVE;
                return processMode;
            case "insert_update":
                processMode = ProcessMode.INSERT_UPDATE;
                return processMode;
        }
        throw new SystemException("mode " + mode.getCode() + " not supported");
    }


    protected abstract EnumerationValue getInternalMode();
}
