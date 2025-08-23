package de.hybris.platform.category.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class ConfigurationCategoryModel extends CategoryModel
{
    public static final String _TYPECODE = "ConfigurationCategory";
    public static final String CONFIGURATORSETTINGS = "configuratorSettings";


    public ConfigurationCategoryModel()
    {
    }


    public ConfigurationCategoryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConfigurationCategoryModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConfigurationCategoryModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "configuratorSettings", type = Accessor.Type.GETTER)
    public List<AbstractConfiguratorSettingModel> getConfiguratorSettings()
    {
        return (List<AbstractConfiguratorSettingModel>)getPersistenceContext().getPropertyValue("configuratorSettings");
    }


    @Accessor(qualifier = "configuratorSettings", type = Accessor.Type.SETTER)
    public void setConfiguratorSettings(List<AbstractConfiguratorSettingModel> value)
    {
        getPersistenceContext().setPropertyValue("configuratorSettings", value);
    }
}
