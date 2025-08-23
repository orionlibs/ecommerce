/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.populators;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_UUID;

import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * This populator sets unique identifiers to CMSItemModels.
 */
public class CMSItemUniqueIdentifierAttributePopulator extends AbstractCMSItemPopulator
{
    // --------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------
    private UniqueItemIdentifierService uniqueItemIdentifierService;


    // --------------------------------------------------------------------------
    // Public Methods
    // --------------------------------------------------------------------------
    @Override
    public void populate(ItemModel source, Map<String, Object> objectMap)
    {
        if(isAttributeAllowed(source, FIELD_UUID))
        {
            getUniqueItemIdentifierService().getItemData(source) //
                            .ifPresent(itemData -> objectMap.put(FIELD_UUID, itemData.getItemId()));
        }
    }


    // --------------------------------------------------------------------------
    // Getters/Setters
    // --------------------------------------------------------------------------
    protected UniqueItemIdentifierService getUniqueItemIdentifierService()
    {
        return uniqueItemIdentifierService;
    }


    @Required
    public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
    {
        this.uniqueItemIdentifierService = uniqueItemIdentifierService;
    }
}
