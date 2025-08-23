package de.hybris.platform.impex.session.impl;

import de.hybris.platform.core.Constants;
import de.hybris.platform.jalo.Item;

public abstract class AbstractMultiThreadedProcessorSessionExecutionBody extends AbstractProcessorSessionExecutionBody<Item, Exception>
{
    protected void prepareSession()
    {
        super.prepareSession();
        if(isParallelMode())
        {
            getSessionService().setAttribute("all.attributes.use.ta", Boolean.FALSE);
            getSessionService().setAttribute(Constants.DISABLE_CYCLIC_CHECKS, Boolean.TRUE);
        }
    }


    protected abstract boolean isParallelMode();
}
