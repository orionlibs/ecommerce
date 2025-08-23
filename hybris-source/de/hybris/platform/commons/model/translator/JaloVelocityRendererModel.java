package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class JaloVelocityRendererModel extends ItemModel
{
    public static final String _TYPECODE = "JaloVelocityRenderer";
    public static final String _TRANSLATORCONFIG2RENDERERS = "TranslatorConfig2Renderers";
    public static final String NAME = "name";
    public static final String TEMPLATE = "template";
    public static final String TRANSLATORCONFIGURATIONPOS = "translatorConfigurationPOS";
    public static final String TRANSLATORCONFIGURATION = "translatorConfiguration";


    public JaloVelocityRendererModel()
    {
    }


    public JaloVelocityRendererModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JaloVelocityRendererModel(String _name)
    {
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JaloVelocityRendererModel(String _name, ItemModel _owner)
    {
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "template", type = Accessor.Type.GETTER)
    public String getTemplate()
    {
        return (String)getPersistenceContext().getPropertyValue("template");
    }


    @Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.GETTER)
    public JaloTranslatorConfigurationModel getTranslatorConfiguration()
    {
        return (JaloTranslatorConfigurationModel)getPersistenceContext().getPropertyValue("translatorConfiguration");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "template", type = Accessor.Type.SETTER)
    public void setTemplate(String value)
    {
        getPersistenceContext().setPropertyValue("template", value);
    }


    @Accessor(qualifier = "translatorConfiguration", type = Accessor.Type.SETTER)
    public void setTranslatorConfiguration(JaloTranslatorConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("translatorConfiguration", value);
    }
}
