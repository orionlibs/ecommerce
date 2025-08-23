package com.hybris.backoffice.jalo;

import com.hybris.backoffice.constants.GeneratedBackofficeConstants;
import com.hybris.backoffice.jalo.user.BackofficeRole;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.processengine.jalo.DynamicProcessDefinition;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.workflow.jalo.AbstractWorkflowAction;
import de.hybris.platform.workflow.jalo.AbstractWorkflowDecision;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedBackofficeManager extends Extension
{
    protected static final OneToManyHandler<BackofficeObjectSpecialCollection> USER2BACKOFFICEOBJECTCOLLECTIONRELATIONCOLLECTIONPKSHANDLER = new OneToManyHandler(GeneratedBackofficeConstants.TC.BACKOFFICEOBJECTSPECIALCOLLECTION, true, "user", null, false, true, 0);
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("backOfficeLoginDisabled", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.security.Principal", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("visualisationX", Item.AttributeMode.INITIAL);
        tmp.put("visualisationY", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.workflow.jalo.AbstractWorkflowAction", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("visualisationX", Item.AttributeMode.INITIAL);
        tmp.put("visualisationY", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.workflow.jalo.AbstractWorkflowDecision", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("visualisationX", Item.AttributeMode.INITIAL);
        tmp.put("visualisationY", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.link.Link", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("themeForBackoffice", Item.AttributeMode.INITIAL);
        tmp.put("avatar", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.User", Collections.unmodifiableMap(tmp));
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


    public Media getAvatar(SessionContext ctx, User item)
    {
        return (Media)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.User.AVATAR);
    }


    public Media getAvatar(User item)
    {
        return getAvatar(getSession().getSessionContext(), item);
    }


    public void setAvatar(SessionContext ctx, User item, Media value)
    {
        (new Object(this, item))
                        .setValue(ctx, value);
    }


    public void setAvatar(User item, Media value)
    {
        setAvatar(getSession().getSessionContext(), item, value);
    }


    public Boolean isBackOfficeLoginDisabled(SessionContext ctx, Principal item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.Principal.BACKOFFICELOGINDISABLED);
    }


    public Boolean isBackOfficeLoginDisabled(Principal item)
    {
        return isBackOfficeLoginDisabled(getSession().getSessionContext(), item);
    }


    public boolean isBackOfficeLoginDisabledAsPrimitive(SessionContext ctx, Principal item)
    {
        Boolean value = isBackOfficeLoginDisabled(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isBackOfficeLoginDisabledAsPrimitive(Principal item)
    {
        return isBackOfficeLoginDisabledAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setBackOfficeLoginDisabled(SessionContext ctx, Principal item, Boolean value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.Principal.BACKOFFICELOGINDISABLED, value);
    }


    public void setBackOfficeLoginDisabled(Principal item, Boolean value)
    {
        setBackOfficeLoginDisabled(getSession().getSessionContext(), item, value);
    }


    public void setBackOfficeLoginDisabled(SessionContext ctx, Principal item, boolean value)
    {
        setBackOfficeLoginDisabled(ctx, item, Boolean.valueOf(value));
    }


    public void setBackOfficeLoginDisabled(Principal item, boolean value)
    {
        setBackOfficeLoginDisabled(getSession().getSessionContext(), item, value);
    }


    public Collection<BackofficeObjectSpecialCollection> getCollectionPks(SessionContext ctx, User item)
    {
        return USER2BACKOFFICEOBJECTCOLLECTIONRELATIONCOLLECTIONPKSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<BackofficeObjectSpecialCollection> getCollectionPks(User item)
    {
        return getCollectionPks(getSession().getSessionContext(), item);
    }


    public void setCollectionPks(SessionContext ctx, User item, Collection<BackofficeObjectSpecialCollection> value)
    {
        USER2BACKOFFICEOBJECTCOLLECTIONRELATIONCOLLECTIONPKSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCollectionPks(User item, Collection<BackofficeObjectSpecialCollection> value)
    {
        setCollectionPks(getSession().getSessionContext(), item, value);
    }


    public void addToCollectionPks(SessionContext ctx, User item, BackofficeObjectSpecialCollection value)
    {
        USER2BACKOFFICEOBJECTCOLLECTIONRELATIONCOLLECTIONPKSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCollectionPks(User item, BackofficeObjectSpecialCollection value)
    {
        addToCollectionPks(getSession().getSessionContext(), item, value);
    }


    public void removeFromCollectionPks(SessionContext ctx, User item, BackofficeObjectSpecialCollection value)
    {
        USER2BACKOFFICEOBJECTCOLLECTIONRELATIONCOLLECTIONPKSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCollectionPks(User item, BackofficeObjectSpecialCollection value)
    {
        removeFromCollectionPks(getSession().getSessionContext(), item, value);
    }


    public BackofficeObjectCollectionItemReference createBackofficeObjectCollectionItemReference(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.BACKOFFICEOBJECTCOLLECTIONITEMREFERENCE);
            return (BackofficeObjectCollectionItemReference)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeObjectCollectionItemReference : " + e.getMessage(), 0);
        }
    }


    public BackofficeObjectCollectionItemReference createBackofficeObjectCollectionItemReference(Map attributeValues)
    {
        return createBackofficeObjectCollectionItemReference(getSession().getSessionContext(), attributeValues);
    }


    public BackofficeObjectSpecialCollection createBackofficeObjectSpecialCollection(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.BACKOFFICEOBJECTSPECIALCOLLECTION);
            return (BackofficeObjectSpecialCollection)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeObjectSpecialCollection : " + e.getMessage(), 0);
        }
    }


    public BackofficeObjectSpecialCollection createBackofficeObjectSpecialCollection(Map attributeValues)
    {
        return createBackofficeObjectSpecialCollection(getSession().getSessionContext(), attributeValues);
    }


    public BackofficeRole createBackofficeRole(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.BACKOFFICEROLE);
            return (BackofficeRole)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeRole : " + e.getMessage(), 0);
        }
    }


    public BackofficeRole createBackofficeRole(Map attributeValues)
    {
        return createBackofficeRole(getSession().getSessionContext(), attributeValues);
    }


    public DynamicProcessDefinition createBackofficeThemeConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.BACKOFFICETHEMECONFIG);
            return (DynamicProcessDefinition)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating BackofficeThemeConfig : " + e.getMessage(), 0);
        }
    }


    public DynamicProcessDefinition createBackofficeThemeConfig(Map attributeValues)
    {
        return createBackofficeThemeConfig(getSession().getSessionContext(), attributeValues);
    }


    public CustomTheme createCustomTheme(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.CUSTOMTHEME);
            return (CustomTheme)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating CustomTheme : " + e.getMessage(), 0);
        }
    }


    public CustomTheme createCustomTheme(Map attributeValues)
    {
        return createCustomTheme(getSession().getSessionContext(), attributeValues);
    }


    public ExcelImportCronJob createExcelImportCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.EXCELIMPORTCRONJOB);
            return (ExcelImportCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ExcelImportCronJob : " + e.getMessage(), 0);
        }
    }


    public ExcelImportCronJob createExcelImportCronJob(Map attributeValues)
    {
        return createExcelImportCronJob(getSession().getSessionContext(), attributeValues);
    }


    public ExcelImportJob createExcelImportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.EXCELIMPORTJOB);
            return (ExcelImportJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating ExcelImportJob : " + e.getMessage(), 0);
        }
    }


    public ExcelImportJob createExcelImportJob(Map attributeValues)
    {
        return createExcelImportJob(getSession().getSessionContext(), attributeValues);
    }


    public Theme createTheme(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedBackofficeConstants.TC.THEME);
            return (Theme)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating Theme : " + e.getMessage(), 0);
        }
    }


    public Theme createTheme(Map attributeValues)
    {
        return createTheme(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "backoffice";
    }


    public Theme getThemeForBackoffice(SessionContext ctx, User item)
    {
        return (Theme)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.User.THEMEFORBACKOFFICE);
    }


    public Theme getThemeForBackoffice(User item)
    {
        return getThemeForBackoffice(getSession().getSessionContext(), item);
    }


    public void setThemeForBackoffice(SessionContext ctx, User item, Theme value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.User.THEMEFORBACKOFFICE, value);
    }


    public void setThemeForBackoffice(User item, Theme value)
    {
        setThemeForBackoffice(getSession().getSessionContext(), item, value);
    }


    public Integer getVisualisationX(SessionContext ctx, AbstractWorkflowAction item)
    {
        return (Integer)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowAction.VISUALISATIONX);
    }


    public Integer getVisualisationX(AbstractWorkflowAction item)
    {
        return getVisualisationX(getSession().getSessionContext(), item);
    }


    public int getVisualisationXAsPrimitive(SessionContext ctx, AbstractWorkflowAction item)
    {
        Integer value = getVisualisationX(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVisualisationXAsPrimitive(AbstractWorkflowAction item)
    {
        return getVisualisationXAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setVisualisationX(SessionContext ctx, AbstractWorkflowAction item, Integer value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowAction.VISUALISATIONX, value);
    }


    public void setVisualisationX(AbstractWorkflowAction item, Integer value)
    {
        setVisualisationX(getSession().getSessionContext(), item, value);
    }


    public void setVisualisationX(SessionContext ctx, AbstractWorkflowAction item, int value)
    {
        setVisualisationX(ctx, item, Integer.valueOf(value));
    }


    public void setVisualisationX(AbstractWorkflowAction item, int value)
    {
        setVisualisationX(getSession().getSessionContext(), item, value);
    }


    public Integer getVisualisationX(SessionContext ctx, AbstractWorkflowDecision item)
    {
        return (Integer)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowDecision.VISUALISATIONX);
    }


    public Integer getVisualisationX(AbstractWorkflowDecision item)
    {
        return getVisualisationX(getSession().getSessionContext(), item);
    }


    public int getVisualisationXAsPrimitive(SessionContext ctx, AbstractWorkflowDecision item)
    {
        Integer value = getVisualisationX(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVisualisationXAsPrimitive(AbstractWorkflowDecision item)
    {
        return getVisualisationXAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setVisualisationX(SessionContext ctx, AbstractWorkflowDecision item, Integer value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowDecision.VISUALISATIONX, value);
    }


    public void setVisualisationX(AbstractWorkflowDecision item, Integer value)
    {
        setVisualisationX(getSession().getSessionContext(), item, value);
    }


    public void setVisualisationX(SessionContext ctx, AbstractWorkflowDecision item, int value)
    {
        setVisualisationX(ctx, item, Integer.valueOf(value));
    }


    public void setVisualisationX(AbstractWorkflowDecision item, int value)
    {
        setVisualisationX(getSession().getSessionContext(), item, value);
    }


    public Integer getVisualisationX(SessionContext ctx, Link item)
    {
        return (Integer)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.VISUALISATIONX);
    }


    public Integer getVisualisationX(Link item)
    {
        return getVisualisationX(getSession().getSessionContext(), item);
    }


    public int getVisualisationXAsPrimitive(SessionContext ctx, Link item)
    {
        Integer value = getVisualisationX(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVisualisationXAsPrimitive(Link item)
    {
        return getVisualisationXAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setVisualisationX(SessionContext ctx, Link item, Integer value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.VISUALISATIONX, value);
    }


    public void setVisualisationX(Link item, Integer value)
    {
        setVisualisationX(getSession().getSessionContext(), item, value);
    }


    public void setVisualisationX(SessionContext ctx, Link item, int value)
    {
        setVisualisationX(ctx, item, Integer.valueOf(value));
    }


    public void setVisualisationX(Link item, int value)
    {
        setVisualisationX(getSession().getSessionContext(), item, value);
    }


    public Integer getVisualisationY(SessionContext ctx, AbstractWorkflowAction item)
    {
        return (Integer)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowAction.VISUALISATIONY);
    }


    public Integer getVisualisationY(AbstractWorkflowAction item)
    {
        return getVisualisationY(getSession().getSessionContext(), item);
    }


    public int getVisualisationYAsPrimitive(SessionContext ctx, AbstractWorkflowAction item)
    {
        Integer value = getVisualisationY(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVisualisationYAsPrimitive(AbstractWorkflowAction item)
    {
        return getVisualisationYAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setVisualisationY(SessionContext ctx, AbstractWorkflowAction item, Integer value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowAction.VISUALISATIONY, value);
    }


    public void setVisualisationY(AbstractWorkflowAction item, Integer value)
    {
        setVisualisationY(getSession().getSessionContext(), item, value);
    }


    public void setVisualisationY(SessionContext ctx, AbstractWorkflowAction item, int value)
    {
        setVisualisationY(ctx, item, Integer.valueOf(value));
    }


    public void setVisualisationY(AbstractWorkflowAction item, int value)
    {
        setVisualisationY(getSession().getSessionContext(), item, value);
    }


    public Integer getVisualisationY(SessionContext ctx, AbstractWorkflowDecision item)
    {
        return (Integer)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowDecision.VISUALISATIONY);
    }


    public Integer getVisualisationY(AbstractWorkflowDecision item)
    {
        return getVisualisationY(getSession().getSessionContext(), item);
    }


    public int getVisualisationYAsPrimitive(SessionContext ctx, AbstractWorkflowDecision item)
    {
        Integer value = getVisualisationY(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVisualisationYAsPrimitive(AbstractWorkflowDecision item)
    {
        return getVisualisationYAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setVisualisationY(SessionContext ctx, AbstractWorkflowDecision item, Integer value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.AbstractWorkflowDecision.VISUALISATIONY, value);
    }


    public void setVisualisationY(AbstractWorkflowDecision item, Integer value)
    {
        setVisualisationY(getSession().getSessionContext(), item, value);
    }


    public void setVisualisationY(SessionContext ctx, AbstractWorkflowDecision item, int value)
    {
        setVisualisationY(ctx, item, Integer.valueOf(value));
    }


    public void setVisualisationY(AbstractWorkflowDecision item, int value)
    {
        setVisualisationY(getSession().getSessionContext(), item, value);
    }


    public Integer getVisualisationY(SessionContext ctx, Link item)
    {
        return (Integer)item.getProperty(ctx, GeneratedBackofficeConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.VISUALISATIONY);
    }


    public Integer getVisualisationY(Link item)
    {
        return getVisualisationY(getSession().getSessionContext(), item);
    }


    public int getVisualisationYAsPrimitive(SessionContext ctx, Link item)
    {
        Integer value = getVisualisationY(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getVisualisationYAsPrimitive(Link item)
    {
        return getVisualisationYAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setVisualisationY(SessionContext ctx, Link item, Integer value)
    {
        item.setProperty(ctx, GeneratedBackofficeConstants.Attributes.WorkflowActionTemplateLinkTemplateRelation.VISUALISATIONY, value);
    }


    public void setVisualisationY(Link item, Integer value)
    {
        setVisualisationY(getSession().getSessionContext(), item, value);
    }


    public void setVisualisationY(SessionContext ctx, Link item, int value)
    {
        setVisualisationY(ctx, item, Integer.valueOf(value));
    }


    public void setVisualisationY(Link item, int value)
    {
        setVisualisationY(getSession().getSessionContext(), item, value);
    }
}
