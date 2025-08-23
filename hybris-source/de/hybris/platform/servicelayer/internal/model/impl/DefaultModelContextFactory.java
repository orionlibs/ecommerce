package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.ModelContextFactory;

public class DefaultModelContextFactory implements ModelContextFactory
{
    public ModelContext createModelContext()
    {
        return (ModelContext)new DefaultModelContext();
    }
}
