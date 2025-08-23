package de.hybris.platform.adaptivesearch.daos;

import de.hybris.platform.adaptivesearch.model.AsSearchProfileActivationSetModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import java.util.List;
import java.util.Optional;

public interface AsSearchProfileActivationSetDao
{
    List<AsSearchProfileActivationSetModel> findAllSearchProfileActivationSets();


    Optional<AsSearchProfileActivationSetModel> findSearchProfileActivationSetByIndexType(CatalogVersionModel paramCatalogVersionModel, String paramString);


    List<AsSearchProfileActivationSetModel> findSearchProfileActivationSetsByCatalogVersionsAndIndexType(List<CatalogVersionModel> paramList, String paramString);
}
