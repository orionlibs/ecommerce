package de.hybris.platform.cockpit.daos;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface SynchronizationServiceDao
{
    SearchResult<Object> getSyncSources(ItemModel paramItemModel);


    SearchResult<Object> getSyncTargets(ItemModel paramItemModel);


    SearchResult<Object> getSyncSourcesAndTargets(ItemModel paramItemModel);
}
