package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.SessionContext;

public class JobMedia extends GeneratedJobMedia
{
    @ForceJALO(reason = "abstract method implementation")
    public Boolean isLocked(SessionContext ctx)
    {
        return Boolean.FALSE.equals(isRemovable()) ? Boolean.TRUE : Boolean.FALSE;
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setLocked(SessionContext ctx, Boolean value)
    {
        setRemovable((value == null || Boolean.FALSE.equals(value)) ? Boolean.TRUE : Boolean.FALSE);
    }
}
