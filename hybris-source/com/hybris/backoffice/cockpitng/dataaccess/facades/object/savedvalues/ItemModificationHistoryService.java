/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.hmc.model.SavedValuesModel;
import java.util.List;

/**
 * Interface responsible for reading/writing SavedValues objects which represents modification performed on objects.
 */
public interface ItemModificationHistoryService
{
    /**
     * Creates ItemModificationInfo object which could be later passed to
     * {@link #logItemModification(ItemModificationInfo)}. Should be called after changes made on object, but before
     * saving it.
     *
     * @param item changed item
     * @return ItemModificationInfo
     * @see ItemModificationInfo
     */
    ItemModificationInfo createModificationInfo(final ItemModel item);


    /**
     * Persist modification info
     *
     * @param modificationInfo info to log
     * @see ItemModificationInfo
     */
    void logItemModification(final ItemModificationInfo modificationInfo);


    /**
     * Gets the saved values
     *
     * @param item item for which we looking for SavedValues
     * @return List of all SavedValues for given <code>item</code>
     */
    List<SavedValuesModel> getSavedValues(final ItemModel item);
}
