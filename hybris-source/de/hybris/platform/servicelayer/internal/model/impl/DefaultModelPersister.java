package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ModelPersister;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;
import java.util.Collections;

public class DefaultModelPersister implements ModelPersister
{
    public Collection<ModelWrapper> persist(Collection<ModelWrapper> wrappers) throws ModelSavingException
    {
        if(!wrappers.isEmpty())
        {
            for(ModelWrapper wr : wrappers)
            {
                if(wr.isNew())
                {
                    wr.save(Collections.EMPTY_SET, false);
                }
            }
            for(ModelWrapper wr : wrappers)
            {
                if(!wr.isNew())
                {
                    wr.save(Collections.EMPTY_SET, false);
                }
            }
        }
        return wrappers;
    }
}
