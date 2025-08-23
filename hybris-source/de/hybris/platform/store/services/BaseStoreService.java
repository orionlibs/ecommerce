package de.hybris.platform.store.services;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

public interface BaseStoreService
{
    List<BaseStoreModel> getAllBaseStores();


    BaseStoreModel getBaseStoreForUid(String paramString) throws AmbiguousIdentifierException, UnknownIdentifierException;


    BaseStoreModel getCurrentBaseStore();
}
