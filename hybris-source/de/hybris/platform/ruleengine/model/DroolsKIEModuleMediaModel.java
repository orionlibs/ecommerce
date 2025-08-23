package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DroolsKIEModuleMediaModel extends CatalogUnawareMediaModel
{
    public static final String _TYPECODE = "DroolsKIEModuleMedia";
    public static final String KIEMODULENAME = "kieModuleName";
    public static final String RELEASEID = "releaseId";


    public DroolsKIEModuleMediaModel()
    {
    }


    public DroolsKIEModuleMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIEModuleMediaModel(CatalogVersionModel _catalogVersion, String _code, String _kieModuleName, String _releaseId)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setKieModuleName(_kieModuleName);
        setReleaseId(_releaseId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIEModuleMediaModel(CatalogVersionModel _catalogVersion, String _code, String _kieModuleName, ItemModel _owner, String _releaseId)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setKieModuleName(_kieModuleName);
        setOwner(_owner);
        setReleaseId(_releaseId);
    }


    @Accessor(qualifier = "kieModuleName", type = Accessor.Type.GETTER)
    public String getKieModuleName()
    {
        return (String)getPersistenceContext().getPropertyValue("kieModuleName");
    }


    @Accessor(qualifier = "releaseId", type = Accessor.Type.GETTER)
    public String getReleaseId()
    {
        return (String)getPersistenceContext().getPropertyValue("releaseId");
    }


    @Accessor(qualifier = "kieModuleName", type = Accessor.Type.SETTER)
    public void setKieModuleName(String value)
    {
        getPersistenceContext().setPropertyValue("kieModuleName", value);
    }


    @Accessor(qualifier = "releaseId", type = Accessor.Type.SETTER)
    public void setReleaseId(String value)
    {
        getPersistenceContext().setPropertyValue("releaseId", value);
    }
}
