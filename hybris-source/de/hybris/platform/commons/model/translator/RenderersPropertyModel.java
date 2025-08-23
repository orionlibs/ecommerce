package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class RenderersPropertyModel extends ItemModel
{
    public static final String _TYPECODE = "RenderersProperty";
    public static final String _TRANSLATORCONFIG2RENDERPROPERTIES = "TranslatorConfig2RenderProperties";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
    public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";


    public RenderersPropertyModel()
    {
    }


    public RenderersPropertyModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RenderersPropertyModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "key", type = Accessor.Type.GETTER)
    public String getKey()
    {
        return (String)getPersistenceContext().getPropertyValue("key");
    }


    @Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
    public JaloTranslatorConfigurationModel getTranslatorConfiguration()
    {
        return (JaloTranslatorConfigurationModel)getPersistenceContext().getPropertyValue("translatorConfiguration");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public String getValue()
    {
        return (String)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "key", type = Accessor.Type.SETTER)
    public void setKey(String value)
    {
        getPersistenceContext().setPropertyValue("key", value);
    }


    @Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
    public void setTranslatorConfiguration(JaloTranslatorConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("translatorConfiguration", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(String value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
