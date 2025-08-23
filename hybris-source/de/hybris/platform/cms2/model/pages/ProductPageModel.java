package de.hybris.platform.cms2.model.pages;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProductPageModel extends AbstractPageModel
{
    public static final String _TYPECODE = "ProductPage";


    public ProductPageModel()
    {
    }


    public ProductPageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setOwner(_owner);
        setUid(_uid);
    }
}
