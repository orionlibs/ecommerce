package de.hybris.platform.cms2.cloning.service.predicate;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@Deprecated(since = "2105", forRemoval = true)
public class MediaForCatalogVersionAttributePresetPredicate implements BiPredicate<ItemModel, String>, Supplier<CatalogVersionModel>
{
    private CatalogVersionModel targetCatalogVersion;


    public MediaForCatalogVersionAttributePresetPredicate(CatalogVersionModel targetCatalogVersion)
    {
        this.targetCatalogVersion = targetCatalogVersion;
    }


    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.core.model.media.MediaModel && "catalogVersion".equals(qualifier));
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
