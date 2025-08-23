package de.hybris.platform.jdbcwrapper;

import java.util.concurrent.atomic.AtomicBoolean;

public class JUnitConnectionStatus extends ConnectionStatus
{
    private final AtomicBoolean forceHasConnectionErrors = new AtomicBoolean(false);


    public boolean hadError()
    {
        return super.hadError();
    }


    public void setPoolHasConnectionErrors(boolean hasErrors)
    {
        this.forceHasConnectionErrors.set(hasErrors);
    }
}
