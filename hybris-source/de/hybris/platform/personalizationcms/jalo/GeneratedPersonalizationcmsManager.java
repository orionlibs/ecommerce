package de.hybris.platform.personalizationcms.jalo;

import de.hybris.platform.cms2.jalo.preview.PreviewData;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.personalizationcms.constants.GeneratedPersonalizationcmsConstants;
import de.hybris.platform.personalizationservices.jalo.CxSegment;
import de.hybris.platform.personalizationservices.jalo.CxVariation;
import de.hybris.platform.personalizationservices.jalo.config.CxConfig;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedPersonalizationcmsManager extends Extension
{
    protected static String PREVIEWDATATOCXVARIATION_SRC_ORDERED = "relation.PreviewDataToCxVariation.source.ordered";
    protected static String PREVIEWDATATOCXVARIATION_TGT_ORDERED = "relation.PreviewDataToCxVariation.target.ordered";
    protected static String PREVIEWDATATOCXVARIATION_MARKMODIFIED = "relation.PreviewDataToCxVariation.markmodified";
    protected static String PREVIEWDATATOCXSEGMENT_SRC_ORDERED = "relation.PreviewDataToCxSegment.source.ordered";
    protected static String PREVIEWDATATOCXSEGMENT_TGT_ORDERED = "relation.PreviewDataToCxSegment.target.ordered";
    protected static String PREVIEWDATATOCXSEGMENT_MARKMODIFIED = "relation.PreviewDataToCxSegment.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("containerCleanupEnabled", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.personalizationservices.jalo.config.CxConfig", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.cms2.jalo.preview.PreviewData", Collections.unmodifiableMap(tmp));
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


    public String getCode(SessionContext ctx, PreviewData item)
    {
        return (String)item.getProperty(ctx, GeneratedPersonalizationcmsConstants.Attributes.PreviewData.CODE);
    }


    public String getCode(PreviewData item)
    {
        return getCode(getSession().getSessionContext(), item);
    }


    public void setCode(SessionContext ctx, PreviewData item, String value)
    {
        item.setProperty(ctx, GeneratedPersonalizationcmsConstants.Attributes.PreviewData.CODE, value);
    }


    public void setCode(PreviewData item, String value)
    {
        setCode(getSession().getSessionContext(), item, value);
    }


    public Boolean isContainerCleanupEnabled(SessionContext ctx, CxConfig item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedPersonalizationcmsConstants.Attributes.CxConfig.CONTAINERCLEANUPENABLED);
    }


    public Boolean isContainerCleanupEnabled(CxConfig item)
    {
        return isContainerCleanupEnabled(getSession().getSessionContext(), item);
    }


    public boolean isContainerCleanupEnabledAsPrimitive(SessionContext ctx, CxConfig item)
    {
        Boolean value = isContainerCleanupEnabled(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isContainerCleanupEnabledAsPrimitive(CxConfig item)
    {
        return isContainerCleanupEnabledAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setContainerCleanupEnabled(SessionContext ctx, CxConfig item, Boolean value)
    {
        item.setProperty(ctx, GeneratedPersonalizationcmsConstants.Attributes.CxConfig.CONTAINERCLEANUPENABLED, value);
    }


    public void setContainerCleanupEnabled(CxConfig item, Boolean value)
    {
        setContainerCleanupEnabled(getSession().getSessionContext(), item, value);
    }


    public void setContainerCleanupEnabled(SessionContext ctx, CxConfig item, boolean value)
    {
        setContainerCleanupEnabled(ctx, item, Boolean.valueOf(value));
    }


    public void setContainerCleanupEnabled(CxConfig item, boolean value)
    {
        setContainerCleanupEnabled(getSession().getSessionContext(), item, value);
    }


    public CxCmsAction createCxCmsAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationcmsConstants.TC.CXCMSACTION);
            return (CxCmsAction)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CxCmsAction : " + e.getMessage(), 0);
        }
    }


    public CxCmsAction createCxCmsAction(Map attributeValues)
    {
        return createCxCmsAction(getSession().getSessionContext(), attributeValues);
    }


    public CxCmsComponentContainer createCxCmsComponentContainer(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationcmsConstants.TC.CXCMSCOMPONENTCONTAINER);
            return (CxCmsComponentContainer)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CxCmsComponentContainer : " + e.getMessage(), 0);
        }
    }


    public CxCmsComponentContainer createCxCmsComponentContainer(Map attributeValues)
    {
        return createCxCmsComponentContainer(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "personalizationcms";
    }


    public Collection<PreviewData> getPreviews(SessionContext ctx, CxVariation item)
    {
        List<PreviewData> items = item.getLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, "PreviewData", null, false, false);
        return items;
    }


    public Collection<PreviewData> getPreviews(CxVariation item)
    {
        return getPreviews(getSession().getSessionContext(), item);
    }


    public long getPreviewsCount(SessionContext ctx, CxVariation item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, "PreviewData", null);
    }


    public long getPreviewsCount(CxVariation item)
    {
        return getPreviewsCount(getSession().getSessionContext(), item);
    }


    public void setPreviews(SessionContext ctx, CxVariation item, Collection<PreviewData> value)
    {
        item.setLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXVARIATION_MARKMODIFIED));
    }


    public void setPreviews(CxVariation item, Collection<PreviewData> value)
    {
        setPreviews(getSession().getSessionContext(), item, value);
    }


    public void addToPreviews(SessionContext ctx, CxVariation item, PreviewData value)
    {
        item.addLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXVARIATION_MARKMODIFIED));
    }


    public void addToPreviews(CxVariation item, PreviewData value)
    {
        addToPreviews(getSession().getSessionContext(), item, value);
    }


    public void removeFromPreviews(SessionContext ctx, CxVariation item, PreviewData value)
    {
        item.removeLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXVARIATION_MARKMODIFIED));
    }


    public void removeFromPreviews(CxVariation item, PreviewData value)
    {
        removeFromPreviews(getSession().getSessionContext(), item, value);
    }


    public Collection<PreviewData> getPreviews(SessionContext ctx, CxSegment item)
    {
        List<PreviewData> items = item.getLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, "PreviewData", null, false, false);
        return items;
    }


    public Collection<PreviewData> getPreviews(CxSegment item)
    {
        return getPreviews(getSession().getSessionContext(), item);
    }


    public long getPreviewsCount(SessionContext ctx, CxSegment item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, "PreviewData", null);
    }


    public long getPreviewsCount(CxSegment item)
    {
        return getPreviewsCount(getSession().getSessionContext(), item);
    }


    public void setPreviews(SessionContext ctx, CxSegment item, Collection<PreviewData> value)
    {
        item.setLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXSEGMENT_MARKMODIFIED));
    }


    public void setPreviews(CxSegment item, Collection<PreviewData> value)
    {
        setPreviews(getSession().getSessionContext(), item, value);
    }


    public void addToPreviews(SessionContext ctx, CxSegment item, PreviewData value)
    {
        item.addLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXSEGMENT_MARKMODIFIED));
    }


    public void addToPreviews(CxSegment item, PreviewData value)
    {
        addToPreviews(getSession().getSessionContext(), item, value);
    }


    public void removeFromPreviews(SessionContext ctx, CxSegment item, PreviewData value)
    {
        item.removeLinkedItems(ctx, false, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXSEGMENT_MARKMODIFIED));
    }


    public void removeFromPreviews(CxSegment item, PreviewData value)
    {
        removeFromPreviews(getSession().getSessionContext(), item, value);
    }


    public Collection<CxSegment> getSegments(SessionContext ctx, PreviewData item)
    {
        List<CxSegment> items = item.getLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, "CxSegment", null, false, false);
        return items;
    }


    public Collection<CxSegment> getSegments(PreviewData item)
    {
        return getSegments(getSession().getSessionContext(), item);
    }


    public long getSegmentsCount(SessionContext ctx, PreviewData item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, "CxSegment", null);
    }


    public long getSegmentsCount(PreviewData item)
    {
        return getSegmentsCount(getSession().getSessionContext(), item);
    }


    public void setSegments(SessionContext ctx, PreviewData item, Collection<CxSegment> value)
    {
        item.setLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXSEGMENT_MARKMODIFIED));
    }


    public void setSegments(PreviewData item, Collection<CxSegment> value)
    {
        setSegments(getSession().getSessionContext(), item, value);
    }


    public void addToSegments(SessionContext ctx, PreviewData item, CxSegment value)
    {
        item.addLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXSEGMENT_MARKMODIFIED));
    }


    public void addToSegments(PreviewData item, CxSegment value)
    {
        addToSegments(getSession().getSessionContext(), item, value);
    }


    public void removeFromSegments(SessionContext ctx, PreviewData item, CxSegment value)
    {
        item.removeLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXSEGMENT, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXSEGMENT_MARKMODIFIED));
    }


    public void removeFromSegments(PreviewData item, CxSegment value)
    {
        removeFromSegments(getSession().getSessionContext(), item, value);
    }


    public Collection<CxVariation> getVariations(SessionContext ctx, PreviewData item)
    {
        List<CxVariation> items = item.getLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, "CxVariation", null, false, false);
        return items;
    }


    public Collection<CxVariation> getVariations(PreviewData item)
    {
        return getVariations(getSession().getSessionContext(), item);
    }


    public long getVariationsCount(SessionContext ctx, PreviewData item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, "CxVariation", null);
    }


    public long getVariationsCount(PreviewData item)
    {
        return getVariationsCount(getSession().getSessionContext(), item);
    }


    public void setVariations(SessionContext ctx, PreviewData item, Collection<CxVariation> value)
    {
        item.setLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXVARIATION_MARKMODIFIED));
    }


    public void setVariations(PreviewData item, Collection<CxVariation> value)
    {
        setVariations(getSession().getSessionContext(), item, value);
    }


    public void addToVariations(SessionContext ctx, PreviewData item, CxVariation value)
    {
        item.addLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXVARIATION_MARKMODIFIED));
    }


    public void addToVariations(PreviewData item, CxVariation value)
    {
        addToVariations(getSession().getSessionContext(), item, value);
    }


    public void removeFromVariations(SessionContext ctx, PreviewData item, CxVariation value)
    {
        item.removeLinkedItems(ctx, true, GeneratedPersonalizationcmsConstants.Relations.PREVIEWDATATOCXVARIATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCXVARIATION_MARKMODIFIED));
    }


    public void removeFromVariations(PreviewData item, CxVariation value)
    {
        removeFromVariations(getSession().getSessionContext(), item, value);
    }
}
