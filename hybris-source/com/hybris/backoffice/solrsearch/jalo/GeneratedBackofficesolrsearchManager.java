package com.hybris.backoffice.solrsearch.jalo;

import com.hybris.backoffice.solrsearch.constants.GeneratedBackofficesolrsearchConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficesolrsearchManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("backofficeDisplayName", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty", Collections.unmodifiableMap(tmp));
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


    public String getBackofficeDisplayName(SessionContext ctx, SolrIndexedProperty item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSolrIndexedProperty.getBackofficeDisplayName requires a session language", 0);
        }
        return (String)item.getLocalizedProperty(ctx, GeneratedBackofficesolrsearchConstants.Attributes.SolrIndexedProperty.BACKOFFICEDISPLAYNAME);
    }


    public String getBackofficeDisplayName(SolrIndexedProperty item)
    {
        return getBackofficeDisplayName(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllBackofficeDisplayName(SessionContext ctx, SolrIndexedProperty item)
    {
        return item.getAllLocalizedProperties(ctx, GeneratedBackofficesolrsearchConstants.Attributes.SolrIndexedProperty.BACKOFFICEDISPLAYNAME, C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllBackofficeDisplayName(SolrIndexedProperty item)
    {
        return getAllBackofficeDisplayName(getSession().getSessionContext(), item);
    }


    public void setBackofficeDisplayName(SessionContext ctx, SolrIndexedProperty item, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedSolrIndexedProperty.setBackofficeDisplayName requires a session language", 0);
        }
        item.setLocalizedProperty(ctx, GeneratedBackofficesolrsearchConstants.Attributes.SolrIndexedProperty.BACKOFFICEDISPLAYNAME, value);
    }


    public void setBackofficeDisplayName(SolrIndexedProperty item, String value)
    {
        setBackofficeDisplayName(getSession().getSessionContext(), item, value);
    }


    public void setAllBackofficeDisplayName(SessionContext ctx, SolrIndexedProperty item, Map<Language, String> value)
    {
        item.setAllLocalizedProperties(ctx, GeneratedBackofficesolrsearchConstants.Attributes.SolrIndexedProperty.BACKOFFICEDISPLAYNAME, value);
    }


    public void setAllBackofficeDisplayName(SolrIndexedProperty item, Map<Language, String> value)
    {
        setAllBackofficeDisplayName(getSession().getSessionContext(), item, value);
    }


    public BackofficeIndexedTypeToSolrFacetSearchConfig createBackofficeIndexedTypeToSolrFacetSearchConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficesolrsearchConstants.TC.BACKOFFICEINDEXEDTYPETOSOLRFACETSEARCHCONFIG);
            return (BackofficeIndexedTypeToSolrFacetSearchConfig)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeIndexedTypeToSolrFacetSearchConfig : " + e.getMessage(), 0);
        }
    }


    public BackofficeIndexedTypeToSolrFacetSearchConfig createBackofficeIndexedTypeToSolrFacetSearchConfig(Map attributeValues)
    {
        return createBackofficeIndexedTypeToSolrFacetSearchConfig(getSession().getSessionContext(), attributeValues);
    }


    public BackofficeSolrIndexerCronJob createBackofficeSolrIndexerCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficesolrsearchConstants.TC.BACKOFFICESOLRINDEXERCRONJOB);
            return (BackofficeSolrIndexerCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeSolrIndexerCronJob : " + e.getMessage(), 0);
        }
    }


    public BackofficeSolrIndexerCronJob createBackofficeSolrIndexerCronJob(Map attributeValues)
    {
        return createBackofficeSolrIndexerCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SolrModifiedItem createSolrModifiedItem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficesolrsearchConstants.TC.SOLRMODIFIEDITEM);
            return (SolrModifiedItem)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating SolrModifiedItem : " + e.getMessage(), 0);
        }
    }


    public SolrModifiedItem createSolrModifiedItem(Map attributeValues)
    {
        return createSolrModifiedItem(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "backofficesolrsearch";
    }
}
