package de.hybris.platform.commons.jalo;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.commons.jalo.translator.JaloTranslatorConfiguration;
import de.hybris.platform.commons.jalo.translator.JaloVelocityRenderer;
import de.hybris.platform.commons.jalo.translator.ParserProperty;
import de.hybris.platform.commons.jalo.translator.RenderersProperty;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCommonsManager extends Extension
{
    protected static String FORMAT2COMTYPREL_SRC_ORDERED = "relation.Format2ComTypRel.source.ordered";
    protected static String FORMAT2COMTYPREL_TGT_ORDERED = "relation.Format2ComTypRel.target.ordered";
    protected static String FORMAT2COMTYPREL_MARKMODIFIED = "relation.Format2ComTypRel.markmodified";
    protected static final OneToManyHandler<Document> ITEMDOCRRELATIONALLDOCUMENTSHANDLER = new OneToManyHandler(GeneratedCommonsConstants.TC.DOCUMENT, true, "sourceItem", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Collection<Document> getAllDocuments(SessionContext ctx, Item item)
    {
        return ITEMDOCRRELATIONALLDOCUMENTSHANDLER.getValues(ctx, item);
    }


    public Collection<Document> getAllDocuments(Item item)
    {
        return getAllDocuments(getSession().getSessionContext(), item);
    }


    public void setAllDocuments(SessionContext ctx, Item item, Collection<Document> value)
    {
        ITEMDOCRRELATIONALLDOCUMENTSHANDLER.setValues(ctx, item, value);
    }


    public void setAllDocuments(Item item, Collection<Document> value)
    {
        setAllDocuments(getSession().getSessionContext(), item, value);
    }


    public void addToAllDocuments(SessionContext ctx, Item item, Document value)
    {
        ITEMDOCRRELATIONALLDOCUMENTSHANDLER.addValue(ctx, item, (Item)value);
    }


    public void addToAllDocuments(Item item, Document value)
    {
        addToAllDocuments(getSession().getSessionContext(), item, value);
    }


    public void removeFromAllDocuments(SessionContext ctx, Item item, Document value)
    {
        ITEMDOCRRELATIONALLDOCUMENTSHANDLER.removeValue(ctx, item, (Item)value);
    }


    public void removeFromAllDocuments(Item item, Document value)
    {
        removeFromAllDocuments(getSession().getSessionContext(), item, value);
    }


    public CustomOrder2XML createCustomOrder2XML(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.CUSTOMORDER2XML);
            return (CustomOrder2XML)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CustomOrder2XML : " + e.getMessage(), 0);
        }
    }


    public CustomOrder2XML createCustomOrder2XML(Map attributeValues)
    {
        return createCustomOrder2XML(getSession().getSessionContext(), attributeValues);
    }


    public Document createDocument(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.DOCUMENT);
            return (Document)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Document : " + e.getMessage(), 0);
        }
    }


    public Document createDocument(Map attributeValues)
    {
        return createDocument(getSession().getSessionContext(), attributeValues);
    }


    public FOPFormatter createFOPFormatter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.FOPFORMATTER);
            return (FOPFormatter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating FOPFormatter : " + e.getMessage(), 0);
        }
    }


    public FOPFormatter createFOPFormatter(Map attributeValues)
    {
        return createFOPFormatter(getSession().getSessionContext(), attributeValues);
    }


    public Format createFormat(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.FORMAT);
            return (Format)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Format : " + e.getMessage(), 0);
        }
    }


    public Format createFormat(Map attributeValues)
    {
        return createFormat(getSession().getSessionContext(), attributeValues);
    }


    public JaloTranslatorConfiguration createJaloTranslatorConfiguration(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.JALOTRANSLATORCONFIGURATION);
            return (JaloTranslatorConfiguration)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating JaloTranslatorConfiguration : " + e.getMessage(), 0);
        }
    }


    public JaloTranslatorConfiguration createJaloTranslatorConfiguration(Map attributeValues)
    {
        return createJaloTranslatorConfiguration(getSession().getSessionContext(), attributeValues);
    }


    public JaloVelocityRenderer createJaloVelocityRenderer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.JALOVELOCITYRENDERER);
            return (JaloVelocityRenderer)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating JaloVelocityRenderer : " + e.getMessage(), 0);
        }
    }


    public JaloVelocityRenderer createJaloVelocityRenderer(Map attributeValues)
    {
        return createJaloVelocityRenderer(getSession().getSessionContext(), attributeValues);
    }


    public ParserProperty createParserProperty(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.PARSERPROPERTY);
            return (ParserProperty)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ParserProperty : " + e.getMessage(), 0);
        }
    }


    public ParserProperty createParserProperty(Map attributeValues)
    {
        return createParserProperty(getSession().getSessionContext(), attributeValues);
    }


    public RenderersProperty createRenderersProperty(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.RENDERERSPROPERTY);
            return (RenderersProperty)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RenderersProperty : " + e.getMessage(), 0);
        }
    }


    public RenderersProperty createRenderersProperty(Map attributeValues)
    {
        return createRenderersProperty(getSession().getSessionContext(), attributeValues);
    }


    public RendererTemplate createRendererTemplate(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.RENDERERTEMPLATE);
            return (RendererTemplate)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RendererTemplate : " + e.getMessage(), 0);
        }
    }


    public RendererTemplate createRendererTemplate(Map attributeValues)
    {
        return createRendererTemplate(getSession().getSessionContext(), attributeValues);
    }


    public VelocityFormatter createVelocityFormatter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.VELOCITYFORMATTER);
            return (VelocityFormatter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VelocityFormatter : " + e.getMessage(), 0);
        }
    }


    public VelocityFormatter createVelocityFormatter(Map attributeValues)
    {
        return createVelocityFormatter(getSession().getSessionContext(), attributeValues);
    }


    public XMLTransformFormatter createXMLTransformFormatter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCommonsConstants.TC.XMLTRANSFORMFORMATTER);
            return (XMLTransformFormatter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating XMLTransformFormatter : " + e.getMessage(), 0);
        }
    }


    public XMLTransformFormatter createXMLTransformFormatter(Map attributeValues)
    {
        return createXMLTransformFormatter(getSession().getSessionContext(), attributeValues);
    }


    public Collection<Format> getFormats(SessionContext ctx, ComposedType item)
    {
        List<Format> items = item.getLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, "Format", null, false, false);
        return items;
    }


    public Collection<Format> getFormats(ComposedType item)
    {
        return getFormats(getSession().getSessionContext(), item);
    }


    public long getFormatsCount(SessionContext ctx, ComposedType item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, "Format", null);
    }


    public long getFormatsCount(ComposedType item)
    {
        return getFormatsCount(getSession().getSessionContext(), item);
    }


    public void setFormats(SessionContext ctx, ComposedType item, Collection<Format> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null, value, false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED));
    }


    public void setFormats(ComposedType item, Collection<Format> value)
    {
        setFormats(getSession().getSessionContext(), item, value);
    }


    public void addToFormats(SessionContext ctx, ComposedType item, Format value)
    {
        item.addLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED));
    }


    public void addToFormats(ComposedType item, Format value)
    {
        addToFormats(getSession().getSessionContext(), item, value);
    }


    public void removeFromFormats(SessionContext ctx, ComposedType item, Format value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(FORMAT2COMTYPREL_MARKMODIFIED));
    }


    public void removeFromFormats(ComposedType item, Format value)
    {
        removeFromFormats(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "commons";
    }
}
