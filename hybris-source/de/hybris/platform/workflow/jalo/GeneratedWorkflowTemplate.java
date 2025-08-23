package de.hybris.platform.workflow.jalo;

import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedWorkflowTemplate extends Job
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ACTIVATIONSCRIPT = "activationScript";
    public static final String ACTIONS = "actions";
    public static final String VISIBLEFORPRINCIPALS = "visibleForPrincipals";
    protected static String WORKFLOWTEMPLATE2PRINCIPALRELATION_SRC_ORDERED = "relation.WorkflowTemplate2PrincipalRelation.source.ordered";
    protected static String WORKFLOWTEMPLATE2PRINCIPALRELATION_TGT_ORDERED = "relation.WorkflowTemplate2PrincipalRelation.target.ordered";
    protected static String WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED = "relation.WorkflowTemplate2PrincipalRelation.markmodified";
    protected static final OneToManyHandler<WorkflowActionTemplate> ACTIONSHANDLER = new OneToManyHandler(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTEMPLATE, true, "workflow", "workflowPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Job.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("activationScript", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<WorkflowActionTemplate> getActions(SessionContext ctx)
    {
        return (List<WorkflowActionTemplate>)ACTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public List<WorkflowActionTemplate> getActions()
    {
        return getActions(getSession().getSessionContext());
    }


    public void setActions(SessionContext ctx, List<WorkflowActionTemplate> value)
    {
        ACTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setActions(List<WorkflowActionTemplate> value)
    {
        setActions(getSession().getSessionContext(), value);
    }


    public void addToActions(SessionContext ctx, WorkflowActionTemplate value)
    {
        ACTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToActions(WorkflowActionTemplate value)
    {
        addToActions(getSession().getSessionContext(), value);
    }


    public void removeFromActions(SessionContext ctx, WorkflowActionTemplate value)
    {
        ACTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromActions(WorkflowActionTemplate value)
    {
        removeFromActions(getSession().getSessionContext(), value);
    }


    public String getActivationScript(SessionContext ctx)
    {
        return (String)getProperty(ctx, "activationScript");
    }


    public String getActivationScript()
    {
        return getActivationScript(getSession().getSessionContext());
    }


    public void setActivationScript(SessionContext ctx, String value)
    {
        setProperty(ctx, "activationScript", value);
    }


    public void setActivationScript(String value)
    {
        setActivationScript(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflowTemplate.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflowTemplate.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflowTemplate.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedWorkflowTemplate.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Collection<Principal> getVisibleForPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, "Principal", null, false, false);
        return items;
    }


    public Collection<Principal> getVisibleForPrincipals()
    {
        return getVisibleForPrincipals(getSession().getSessionContext());
    }


    public long getVisibleForPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, "Principal", null);
    }


    public long getVisibleForPrincipalsCount()
    {
        return getVisibleForPrincipalsCount(getSession().getSessionContext());
    }


    public void setVisibleForPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void setVisibleForPrincipals(Collection<Principal> value)
    {
        setVisibleForPrincipals(getSession().getSessionContext(), value);
    }


    public void addToVisibleForPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToVisibleForPrincipals(Principal value)
    {
        addToVisibleForPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromVisibleForPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, true, GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WORKFLOWTEMPLATE2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromVisibleForPrincipals(Principal value)
    {
        removeFromVisibleForPrincipals(getSession().getSessionContext(), value);
    }
}
