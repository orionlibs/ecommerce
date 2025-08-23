package de.hybris.platform.platformbackoffice.jalo;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.platformbackoffice.constants.GeneratedPlatformbackofficeConstants;
import de.hybris.platform.platformbackoffice.validation.jalo.constraints.HybrisEnumValueCodeConstraint;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedPlatformbackofficeManager extends Extension
{
    protected static String BACKOFFICESAVEDQUERY2USERGROUPRELATION_SRC_ORDERED = "relation.BackofficeSavedQuery2UserGroupRelation.source.ordered";
    protected static String BACKOFFICESAVEDQUERY2USERGROUPRELATION_TGT_ORDERED = "relation.BackofficeSavedQuery2UserGroupRelation.target.ordered";
    protected static String BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED = "relation.BackofficeSavedQuery2UserGroupRelation.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("enableWysiwygEditor", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment", Collections.unmodifiableMap(tmp));
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


    public BackofficeSavedQuery createBackofficeSavedQuery(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPlatformbackofficeConstants.TC.BACKOFFICESAVEDQUERY);
            return (BackofficeSavedQuery)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeSavedQuery : " + e.getMessage(), 0);
        }
    }


    public BackofficeSavedQuery createBackofficeSavedQuery(Map attributeValues)
    {
        return createBackofficeSavedQuery(getSession().getSessionContext(), attributeValues);
    }


    public BackofficeSearchCondition createBackofficeSearchCondition(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPlatformbackofficeConstants.TC.BACKOFFICESEARCHCONDITION);
            return (BackofficeSearchCondition)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeSearchCondition : " + e.getMessage(), 0);
        }
    }


    public BackofficeSearchCondition createBackofficeSearchCondition(Map attributeValues)
    {
        return createBackofficeSearchCondition(getSession().getSessionContext(), attributeValues);
    }


    public HybrisEnumValueCodeConstraint createHybrisEnumValueCodeConstraint(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPlatformbackofficeConstants.TC.HYBRISENUMVALUECODECONSTRAINT);
            return (HybrisEnumValueCodeConstraint)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating HybrisEnumValueCodeConstraint : " + e.getMessage(), 0);
        }
    }


    public HybrisEnumValueCodeConstraint createHybrisEnumValueCodeConstraint(Map attributeValues)
    {
        return createHybrisEnumValueCodeConstraint(getSession().getSessionContext(), attributeValues);
    }


    public Boolean isEnableWysiwygEditor(SessionContext ctx, ClassAttributeAssignment item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedPlatformbackofficeConstants.Attributes.ClassAttributeAssignment.ENABLEWYSIWYGEDITOR);
    }


    public Boolean isEnableWysiwygEditor(ClassAttributeAssignment item)
    {
        return isEnableWysiwygEditor(getSession().getSessionContext(), item);
    }


    public boolean isEnableWysiwygEditorAsPrimitive(SessionContext ctx, ClassAttributeAssignment item)
    {
        Boolean value = isEnableWysiwygEditor(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEnableWysiwygEditorAsPrimitive(ClassAttributeAssignment item)
    {
        return isEnableWysiwygEditorAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setEnableWysiwygEditor(SessionContext ctx, ClassAttributeAssignment item, Boolean value)
    {
        item.setProperty(ctx, GeneratedPlatformbackofficeConstants.Attributes.ClassAttributeAssignment.ENABLEWYSIWYGEDITOR, value);
    }


    public void setEnableWysiwygEditor(ClassAttributeAssignment item, Boolean value)
    {
        setEnableWysiwygEditor(getSession().getSessionContext(), item, value);
    }


    public void setEnableWysiwygEditor(SessionContext ctx, ClassAttributeAssignment item, boolean value)
    {
        setEnableWysiwygEditor(ctx, item, Boolean.valueOf(value));
    }


    public void setEnableWysiwygEditor(ClassAttributeAssignment item, boolean value)
    {
        setEnableWysiwygEditor(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "platformbackoffice";
    }


    public Collection<BackofficeSavedQuery> getSavedQueries(SessionContext ctx, UserGroup item)
    {
        List<BackofficeSavedQuery> items = item.getLinkedItems(ctx, false, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, "BackofficeSavedQuery", null, false, false);
        return items;
    }


    public Collection<BackofficeSavedQuery> getSavedQueries(UserGroup item)
    {
        return getSavedQueries(getSession().getSessionContext(), item);
    }


    public long getSavedQueriesCount(SessionContext ctx, UserGroup item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, "BackofficeSavedQuery", null);
    }


    public long getSavedQueriesCount(UserGroup item)
    {
        return getSavedQueriesCount(getSession().getSessionContext(), item);
    }


    public void setSavedQueries(SessionContext ctx, UserGroup item, Collection<BackofficeSavedQuery> value)
    {
        item.setLinkedItems(ctx, false, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED));
    }


    public void setSavedQueries(UserGroup item, Collection<BackofficeSavedQuery> value)
    {
        setSavedQueries(getSession().getSessionContext(), item, value);
    }


    public void addToSavedQueries(SessionContext ctx, UserGroup item, BackofficeSavedQuery value)
    {
        item.addLinkedItems(ctx, false, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED));
    }


    public void addToSavedQueries(UserGroup item, BackofficeSavedQuery value)
    {
        addToSavedQueries(getSession().getSessionContext(), item, value);
    }


    public void removeFromSavedQueries(SessionContext ctx, UserGroup item, BackofficeSavedQuery value)
    {
        item.removeLinkedItems(ctx, false, GeneratedPlatformbackofficeConstants.Relations.BACKOFFICESAVEDQUERY2USERGROUPRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(BACKOFFICESAVEDQUERY2USERGROUPRELATION_MARKMODIFIED));
    }


    public void removeFromSavedQueries(UserGroup item, BackofficeSavedQuery value)
    {
        removeFromSavedQueries(getSession().getSessionContext(), item, value);
    }
}
