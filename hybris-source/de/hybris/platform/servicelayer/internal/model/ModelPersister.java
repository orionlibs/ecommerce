package de.hybris.platform.servicelayer.internal.model;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;

public interface ModelPersister
{
    Collection<ModelWrapper> persist(Collection<ModelWrapper> paramCollection) throws ModelSavingException;
}
