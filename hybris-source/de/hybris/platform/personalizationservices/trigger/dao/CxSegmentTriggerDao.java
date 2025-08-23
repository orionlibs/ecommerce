package de.hybris.platform.personalizationservices.trigger.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

public interface CxSegmentTriggerDao extends Dao
{
    Collection<CxVariationModel> findApplicableVariations(UserModel paramUserModel, BigDecimal paramBigDecimal, CatalogVersionModel paramCatalogVersionModel);


    default Collection<CxVariationModel> findApplicableVariations(Collection<CxSegmentModel> segments, CatalogVersionModel catalogVersion)
    {
        return Collections.emptyList();
    }
}
