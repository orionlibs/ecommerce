package de.hybris.platform.commons.model.translator;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class JaloTranslatorConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "JaloTranslatorConfiguration";
    public static final String CODE = "code";
    public static final String RENDERERS = "renderers";
    public static final String RENDERERSPROPERTIES = "renderersProperties";
    public static final String PARSERPROPERTIES = "parserProperties";


    public JaloTranslatorConfigurationModel()
    {
    }


    public JaloTranslatorConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JaloTranslatorConfigurationModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public JaloTranslatorConfigurationModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "parserProperties", type = Accessor.Type.GETTER)
    public List<ParserPropertyModel> getParserProperties()
    {
        return (List<ParserPropertyModel>)getPersistenceContext().getPropertyValue("parserProperties");
    }


    @Accessor(qualifier = "renderers", type = Accessor.Type.GETTER)
    public List<JaloVelocityRendererModel> getRenderers()
    {
        return (List<JaloVelocityRendererModel>)getPersistenceContext().getPropertyValue("renderers");
    }


    @Accessor(qualifier = "renderersProperties", type = Accessor.Type.GETTER)
    public List<RenderersPropertyModel> getRenderersProperties()
    {
        return (List<RenderersPropertyModel>)getPersistenceContext().getPropertyValue("renderersProperties");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "parserProperties", type = Accessor.Type.SETTER)
    public void setParserProperties(List<ParserPropertyModel> value)
    {
        getPersistenceContext().setPropertyValue("parserProperties", value);
    }


    @Accessor(qualifier = "renderers", type = Accessor.Type.SETTER)
    public void setRenderers(List<JaloVelocityRendererModel> value)
    {
        getPersistenceContext().setPropertyValue("renderers", value);
    }


    @Accessor(qualifier = "renderersProperties", type = Accessor.Type.SETTER)
    public void setRenderersProperties(List<RenderersPropertyModel> value)
    {
        getPersistenceContext().setPropertyValue("renderersProperties", value);
    }
}
