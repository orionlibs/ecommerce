package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;

interface PersistenceObjectInternal extends PersistenceObject
{
    boolean isBackedByJaloItem();


    PersistenceObjectInternal getLatest();


    Item getCorrespondingItem();
}
