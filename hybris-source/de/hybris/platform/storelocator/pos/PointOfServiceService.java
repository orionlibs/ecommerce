package de.hybris.platform.storelocator.pos;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.pojo.StoreCountInfo;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.util.List;

public interface PointOfServiceService
{
    PointOfServiceModel getPointOfServiceForName(String paramString) throws UnknownIdentifierException, IllegalArgumentException;


    List<StoreCountInfo> getPointOfServiceCounts(BaseStoreModel paramBaseStoreModel);
}
