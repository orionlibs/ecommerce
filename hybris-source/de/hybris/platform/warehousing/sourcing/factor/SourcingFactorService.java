package de.hybris.platform.warehousing.sourcing.factor;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactor;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactorIdentifiersEnum;
import java.util.Set;

public interface SourcingFactorService
{
    SourcingFactor getSourcingFactor(SourcingFactorIdentifiersEnum paramSourcingFactorIdentifiersEnum, BaseStoreModel paramBaseStoreModel);


    Set<SourcingFactor> getAllSourcingFactorsForBaseStore(BaseStoreModel paramBaseStoreModel);
}
