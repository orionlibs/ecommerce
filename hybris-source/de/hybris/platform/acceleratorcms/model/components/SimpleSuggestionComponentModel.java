package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SimpleSuggestionComponentModel extends ProductReferencesComponentModel
{
    public static final String _TYPECODE = "SimpleSuggestionComponent";
    public static final String FILTERPURCHASED = "filterPurchased";


    public SimpleSuggestionComponentModel()
    {
    }


    public SimpleSuggestionComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleSuggestionComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SimpleSuggestionComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "filterPurchased", type = Accessor.Type.GETTER)
    public boolean isFilterPurchased()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("filterPurchased"));
    }


    @Accessor(qualifier = "filterPurchased", type = Accessor.Type.SETTER)
    public void setFilterPurchased(boolean value)
    {
        getPersistenceContext().setPropertyValue("filterPurchased", toObject(value));
    }
}
