package de.hybris.platform.servicelayer.internal.model.extractor;

import de.hybris.platform.servicelayer.internal.model.impl.PersistenceType;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.Collection;

public interface PersistenceTypeService
{
    PersistenceType getPersistenceType(Collection<ModelWrapper> paramCollection);
}
