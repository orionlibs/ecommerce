package de.hybris.platform.commons.model.renderer;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class RendererTemplateModel extends ItemModel
{
    public static final String _TYPECODE = "RendererTemplate";
    public static final String CODE = "code";
    public static final String DESCRIPTION = "description";
    public static final String DEFAULTCONTENT = "defaultContent";
    public static final String CONTENT = "content";
    public static final String CONTEXTCLASS = "contextClass";
    public static final String OUTPUTMIMETYPE = "outputMimeType";
    public static final String RENDERERTYPE = "rendererType";
    public static final String DEFAULTTEMPLATESCRIPT = "defaultTemplateScript";
    public static final String TEMPLATESCRIPT = "templateScript";
    public static final String CONTEXTCLASSDESCRIPTION = "contextClassDescription";


    public RendererTemplateModel()
    {
    }


    public RendererTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RendererTemplateModel(String _code, RendererTypeEnum _rendererType)
    {
        setCode(_code);
        setRendererType(_rendererType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RendererTemplateModel(String _code, ItemModel _owner, RendererTypeEnum _rendererType)
    {
        setCode(_code);
        setOwner(_owner);
        setRendererType(_rendererType);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public MediaModel getContent()
    {
        return getContent(null);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public MediaModel getContent(Locale loc)
    {
        return (MediaModel)getPersistenceContext().getLocalizedValue("content", loc);
    }


    @Accessor(qualifier = "contextClass", type = Accessor.Type.GETTER)
    public String getContextClass()
    {
        return (String)getPersistenceContext().getPropertyValue("contextClass");
    }


    @Accessor(qualifier = "contextClassDescription", type = Accessor.Type.GETTER)
    public String getContextClassDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("contextClassDescription");
    }


    @Accessor(qualifier = "defaultContent", type = Accessor.Type.GETTER)
    public MediaModel getDefaultContent()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("defaultContent");
    }


    @Accessor(qualifier = "defaultTemplateScript", type = Accessor.Type.GETTER)
    public String getDefaultTemplateScript()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "defaultTemplateScript");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "outputMimeType", type = Accessor.Type.GETTER)
    public String getOutputMimeType()
    {
        return (String)getPersistenceContext().getPropertyValue("outputMimeType");
    }


    @Accessor(qualifier = "rendererType", type = Accessor.Type.GETTER)
    public RendererTypeEnum getRendererType()
    {
        return (RendererTypeEnum)getPersistenceContext().getPropertyValue("rendererType");
    }


    @Accessor(qualifier = "templateScript", type = Accessor.Type.GETTER)
    public String getTemplateScript()
    {
        return getTemplateScript(null);
    }


    @Accessor(qualifier = "templateScript", type = Accessor.Type.GETTER)
    public String getTemplateScript(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("templateScript", loc);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(MediaModel value)
    {
        setContent(value, null);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(MediaModel value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("content", loc, value);
    }


    @Accessor(qualifier = "contextClass", type = Accessor.Type.SETTER)
    public void setContextClass(String value)
    {
        getPersistenceContext().setPropertyValue("contextClass", value);
    }


    @Accessor(qualifier = "defaultContent", type = Accessor.Type.SETTER)
    public void setDefaultContent(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("defaultContent", value);
    }


    @Accessor(qualifier = "defaultTemplateScript", type = Accessor.Type.SETTER)
    public void setDefaultTemplateScript(String value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "defaultTemplateScript", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "outputMimeType", type = Accessor.Type.SETTER)
    public void setOutputMimeType(String value)
    {
        getPersistenceContext().setPropertyValue("outputMimeType", value);
    }


    @Accessor(qualifier = "rendererType", type = Accessor.Type.SETTER)
    public void setRendererType(RendererTypeEnum value)
    {
        getPersistenceContext().setPropertyValue("rendererType", value);
    }


    @Accessor(qualifier = "templateScript", type = Accessor.Type.SETTER)
    public void setTemplateScript(String value)
    {
        setTemplateScript(value, null);
    }


    @Accessor(qualifier = "templateScript", type = Accessor.Type.SETTER)
    public void setTemplateScript(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("templateScript", loc, value);
    }
}
