package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;
import java.util.Iterator;
import java.util.Set;

public class ItemIdentityProvider implements IdentityProvider<ItemModel>
{
    private static final long serialVersionUID = 1L;
    private CatalogTypeService catalogTypeService;
    private ModelService modelService;


    public String getIdentifier(IndexConfig indexConfig, ItemModel item)
    {
        String identifier;
        if(item instanceof ProductModel)
        {
            ProductModel product = (ProductModel)item;
            CatalogVersionModel catalogVersion = product.getCatalogVersion();
            String code = product.getCode();
            identifier = catalogVersion.getCatalog().getId() + "/" + catalogVersion.getCatalog().getId() + "/" + catalogVersion.getVersion();
        }
        else if(this.catalogTypeService.isCatalogVersionAwareModel(item))
        {
            identifier = prepareCatalogAwareItemIdentifier(item);
        }
        else
        {
            identifier = item.getPk().getLongValueAsString();
        }
        return identifier;
    }


    protected String prepareCatalogAwareItemIdentifier(ItemModel item)
    {
        Set<String> catalogVersionUniqueKeyAttributes = this.catalogTypeService.getCatalogVersionUniqueKeyAttribute(item.getItemtype());
        String catalogVersionContainerAttribute = this.catalogTypeService.getCatalogVersionContainerAttribute(item.getItemtype());
        CatalogVersionModel catalogVersion = (CatalogVersionModel)this.modelService.getAttributeValue(item, catalogVersionContainerAttribute);
        Iterator<String> catalogVersionUniqueKeyIterator = catalogVersionUniqueKeyAttributes.iterator();
        StringBuilder itemKey = new StringBuilder("");
        while(catalogVersionUniqueKeyIterator.hasNext())
        {
            String codePart = catalogVersionUniqueKeyIterator.next();
            itemKey.append("/").append(this.modelService.getAttributeValue(item, codePart).toString());
        }
        return catalogVersion.getCatalog().getId() + "/" + catalogVersion.getCatalog().getId() + catalogVersion.getVersion();
    }


    public CatalogTypeService getCatalogTypeService()
    {
        return this.catalogTypeService;
    }


    public void setCatalogTypeService(CatalogTypeService catalogTypeService)
    {
        this.catalogTypeService = catalogTypeService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
