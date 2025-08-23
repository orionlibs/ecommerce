package de.hybris.platform.product.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.ConfigurationCategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractConfiguratorSettingModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractConfiguratorSetting";
    public static final String _CONFIGURATIONCATEGORY2CONFIGURATORSETTINGSRELATION = "ConfigurationCategory2ConfiguratorSettingsRelation";
    public static final String ID = "id";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String CONFIGURATORTYPE = "configuratorType";
    public static final String QUALIFIER = "qualifier";
    public static final String CONFIGURATIONCATEGORYPOS = "configurationCategoryPOS";
    public static final String CONFIGURATIONCATEGORY = "configurationCategory";


    public AbstractConfiguratorSettingModel()
    {
    }


    public AbstractConfiguratorSettingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractConfiguratorSettingModel(CatalogVersionModel _catalogVersion, ConfigurationCategoryModel _configurationCategory, ConfiguratorType _configuratorType, String _id, String _qualifier)
    {
        setCatalogVersion(_catalogVersion);
        setConfigurationCategory(_configurationCategory);
        setConfiguratorType(_configuratorType);
        setId(_id);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractConfiguratorSettingModel(CatalogVersionModel _catalogVersion, ConfigurationCategoryModel _configurationCategory, ConfiguratorType _configuratorType, String _id, ItemModel _owner, String _qualifier)
    {
        setCatalogVersion(_catalogVersion);
        setConfigurationCategory(_configurationCategory);
        setConfiguratorType(_configuratorType);
        setId(_id);
        setOwner(_owner);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "configurationCategory", type = Accessor.Type.GETTER)
    public ConfigurationCategoryModel getConfigurationCategory()
    {
        return (ConfigurationCategoryModel)getPersistenceContext().getPropertyValue("configurationCategory");
    }


    @Accessor(qualifier = "configuratorType", type = Accessor.Type.GETTER)
    public ConfiguratorType getConfiguratorType()
    {
        return (ConfiguratorType)getPersistenceContext().getPropertyValue("configuratorType");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "configurationCategory", type = Accessor.Type.SETTER)
    public void setConfigurationCategory(ConfigurationCategoryModel value)
    {
        getPersistenceContext().setPropertyValue("configurationCategory", value);
    }


    @Accessor(qualifier = "configuratorType", type = Accessor.Type.SETTER)
    public void setConfiguratorType(ConfiguratorType value)
    {
        getPersistenceContext().setPropertyValue("configuratorType", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
