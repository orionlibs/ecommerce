package de.hybris.platform.cms2.cloning.service.preset.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.cloning.service.preset.AttributePresetHandler;
import de.hybris.platform.core.model.ItemModel;

public class ComponentForCatalogVersionAttributePresetHandler implements AttributePresetHandler<CatalogVersionModel>
{
    private CatalogVersionModel targetCatalogVersion;


    public ComponentForCatalogVersionAttributePresetHandler(CatalogVersionModel targetCatalogVersion)
    {
        this.targetCatalogVersion = targetCatalogVersion;
    }


    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel && "catalogVersion".equals(qualifier));
    }


    public CatalogVersionModel get()
    {
        return getTargetCatalogVersion();
    }


    protected CatalogVersionModel getTargetCatalogVersion()
    {
        return this.targetCatalogVersion;
    }


    public void setTargetCatalogVersion(CatalogVersionModel targetCatalogVersion)
    {
        this.targetCatalogVersion = targetCatalogVersion;
    }
}
