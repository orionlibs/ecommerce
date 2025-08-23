package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.servicelayer.internal.converter.ModelConverter;

public interface ModelModificationListener
{
    void notifyModelUpdated(Object paramObject, ModelConverter paramModelConverter);
}
