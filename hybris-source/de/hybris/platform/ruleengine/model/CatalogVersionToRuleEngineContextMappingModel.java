package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CatalogVersionToRuleEngineContextMappingModel extends ItemModel
{
    public static final String _TYPECODE = "CatalogVersionToRuleEngineContextMapping";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CONTEXT = "context";


    public CatalogVersionToRuleEngineContextMappingModel()
    {
    }


    public CatalogVersionToRuleEngineContextMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionToRuleEngineContextMappingModel(CatalogVersionModel _catalogVersion, AbstractRuleEngineContextModel _context)
    {
        setCatalogVersion(_catalogVersion);
        setContext(_context);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionToRuleEngineContextMappingModel(CatalogVersionModel _catalogVersion, AbstractRuleEngineContextModel _context, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setContext(_context);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "context", type = Accessor.Type.GETTER)
    public AbstractRuleEngineContextModel getContext()
    {
        return (AbstractRuleEngineContextModel)getPersistenceContext().getPropertyValue("context");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "context", type = Accessor.Type.SETTER)
    public void setContext(AbstractRuleEngineContextModel value)
    {
        getPersistenceContext().setPropertyValue("context", value);
    }
}
