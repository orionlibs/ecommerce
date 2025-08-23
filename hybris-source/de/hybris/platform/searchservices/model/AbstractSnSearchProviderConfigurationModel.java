package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class AbstractSnSearchProviderConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractSnSearchProviderConfiguration";
    public static final String _SNINDEXCONFIGURATION2SEARCHPROVIDERCONFIGURATION = "SnIndexConfiguration2SearchProviderConfiguration";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LISTENERS = "listeners";
    public static final String INDEXCONFIGURATIONS = "indexConfigurations";


    public AbstractSnSearchProviderConfigurationModel()
    {
    }


    public AbstractSnSearchProviderConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractSnSearchProviderConfigurationModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractSnSearchProviderConfigurationModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "indexConfigurations", type = Accessor.Type.GETTER)
    public List<SnIndexConfigurationModel> getIndexConfigurations()
    {
        return (List<SnIndexConfigurationModel>)getPersistenceContext().getPropertyValue("indexConfigurations");
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
    public List<String> getListeners()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("listeners");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "indexConfigurations", type = Accessor.Type.SETTER)
    public void setIndexConfigurations(List<SnIndexConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("indexConfigurations", value);
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
    public void setListeners(List<String> value)
    {
        getPersistenceContext().setPropertyValue("listeners", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }
}
