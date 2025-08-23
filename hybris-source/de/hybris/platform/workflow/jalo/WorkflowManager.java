package de.hybris.platform.workflow.jalo;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowManager extends GeneratedWorkflowManager
{
    private static final Logger LOG = Logger.getLogger(WorkflowManager.class);


    public static final WorkflowManager getInstance()
    {
        return (WorkflowManager)ExtensionManager.getInstance().getExtension("workflow");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getNextWorkflowNumber()
    {
        NumberSeriesManager manager = NumberSeriesManager.getInstance();
        try
        {
            manager.getNumberSeries("ext.workflow.workflow");
        }
        catch(JaloInvalidParameterException e)
        {
            manager.createNumberSeries("ext.workflow.workflow", "000000", 0, 6);
        }
        return manager.getUniqueNumber("ext.workflow.workflow", 6);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Link> getLinks(WorkflowDecision decision, AbstractWorkflowAction action)
    {
        Collection<Link> results;
        Map<Object, Object> params = new HashMap<>();
        params.put("desc", decision);
        params.put("act", action);
        if(decision == null && action == null)
        {
            throw new IllegalArgumentException("Decision and action cannot both be null");
        }
        if(action == null)
        {
            results = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION + "} where {" + GeneratedCoreConstants.Attributes.Link.SOURCE + "}=?desc", params, Link.class).getResult();
        }
        else if(decision == null)
        {
            results = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION + "} where {" + GeneratedCoreConstants.Attributes.Link.TARGET + "}=?act", params, Link.class).getResult();
        }
        else
        {
            results = FlexibleSearch.getInstance()
                            .search("SELECT {" + Item.PK + "} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONLINKRELATION + "} where {" + GeneratedCoreConstants.Attributes.Link.SOURCE + "}=?desc AND {" + GeneratedCoreConstants.Attributes.Link.TARGET + "}=?act", params, Link.class)
                            .getResult();
            if(results.size() != 1)
            {
                LOG.error("There is more than one WorkflowActionTemplateLinkTemplateRelation for source '" + decision.getCode() + "' and target '" + action
                                .getCode() + "'");
            }
        }
        return results;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Link> getLinkTemplates(AbstractWorkflowDecision decision, AbstractWorkflowAction action)
    {
        Collection<Link> results;
        Map<Object, Object> params = new HashMap<>();
        params.put("desc", decision);
        params.put("act", action);
        if(decision == null && action == null)
        {
            throw new IllegalArgumentException("Decision and action cannot both be null");
        }
        if(action == null)
        {
            results = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION + "} where {" + GeneratedCoreConstants.Attributes.Link.SOURCE + "}=?desc", params, Link.class).getResult();
        }
        else if(decision == null)
        {
            results = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION + "} where {" + GeneratedCoreConstants.Attributes.Link.TARGET + "}=?act", params, Link.class).getResult();
        }
        else
        {
            results = FlexibleSearch.getInstance()
                            .search("SELECT {" + Item.PK + "} from {" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION + "} where {" + GeneratedCoreConstants.Attributes.Link.SOURCE + "}=?desc AND {" + GeneratedCoreConstants.Attributes.Link.TARGET + "}=?act", params,
                                            Link.class).getResult();
            if(results.size() != 1)
            {
                LOG.error("There is more than one WorkflowActionTemplateLinkTemplateRelation for source '" + decision.getCode() + "' and target '" + action
                                .getCode() + "'");
            }
        }
        return results;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getNextActionNumber()
    {
        NumberSeriesManager manager = NumberSeriesManager.getInstance();
        try
        {
            manager.getNumberSeries("ext.workflow.action");
        }
        catch(JaloInvalidParameterException e)
        {
            manager.createNumberSeries("ext.workflow.action", "00000000", 0, 8);
        }
        return manager.getUniqueNumber("ext.workflow.action", 8);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getNextAttachmentNumber()
    {
        NumberSeriesManager manager = NumberSeriesManager.getInstance();
        try
        {
            manager.getNumberSeries("ext.workflow.attachment");
        }
        catch(JaloInvalidParameterException e)
        {
            manager.createNumberSeries("ext.workflow.attachment", "000000", 0, 6);
        }
        return manager.getUniqueNumber("ext.workflow.attachment", 6);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getNextDecisionNumber()
    {
        NumberSeriesManager manager = NumberSeriesManager.getInstance();
        try
        {
            manager.getNumberSeries("ext.workflow.decision");
        }
        catch(JaloInvalidParameterException e)
        {
            manager.createNumberSeries("ext.workflow.decision", "00000000", 0, 8);
        }
        return manager.getUniqueNumber("ext.workflow.decision", 8);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<WorkflowTemplate> getAllWorkflowTemplates()
    {
        String code = TypeManager.getInstance().getComposedType(WorkflowTemplate.class).getCode();
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + code + "}", null,
                        Collections.singletonList(WorkflowTemplate.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<Workflow> getWorkflowsByTemplate(WorkflowTemplate template)
    {
        String code = TypeManager.getInstance().getComposedType(Workflow.class).getCode();
        Map<Object, Object> params = new HashMap<>();
        params.put("user", getSession().getUser());
        params.put("job", template);
        String query = "SELECT {" + Item.PK + "} FROM {" + code + "} WHERE {" + Workflow.OWNER + "} = ?user";
        query = query + " AND {job} = ?job";
        SearchResult res = getSession().getFlexibleSearch().search(query, params, Collections.singletonList(Workflow.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<WorkflowAction> getAllUserWorkflowActionsWithAttachment(String attachmentTypeCodeOrClassName)
    {
        ComposedType type = getTypeFromTypeCodeOrClassName(attachmentTypeCodeOrClassName);
        if(type != null)
        {
            return getAllUserWorkflowActionsWithAttachment(type);
        }
        return new ArrayList<>();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<WorkflowAction> getAllUserWorkflowActionsWithAttachment(ComposedType attachmentType)
    {
        return getAllUserWorkflowActionsWithAttachments(Collections.singletonList(attachmentType.getJaloClass().getName()));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<WorkflowAction> getAllUserWorkflowActionsWithAttachments(List<String> attachments)
    {
        return getAllUserWorkflowActionsWithAttachments(attachments, Collections.EMPTY_LIST);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List<WorkflowAction> getAllUserWorkflowActionsWithAttachments(List<String> attachments, Collection<EnumerationValue> actionStatuses)
    {
        Map<Object, Object> params = new HashMap<>();
        if(actionStatuses == null || actionStatuses.isEmpty())
        {
            params.put("status", Collections.singletonList(WorkflowAction.getActiveStatus()));
        }
        else
        {
            params.put("status", actionStatuses);
        }
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            StringBuilder typeCondition = new StringBuilder();
            List<ComposedType> types = new ArrayList<>();
            if(!attachments.isEmpty())
            {
                for(String attachmentClassName : attachments)
                {
                    ComposedType type = getTypeFromTypeCodeOrClassName(attachmentClassName);
                    if(type != null)
                    {
                        types.add(type);
                        types.addAll(type.getAllSubTypes());
                    }
                }
                params.put("types", types);
                typeCondition.append(" AND {attachment:typeOfItem} IN (?types) ");
            }
            SearchResult res = getSession().getFlexibleSearch()
                            .search("SELECT DISTINCT {actions:" + WorkflowAction.PK + "}, {actions:" + WorkflowAction.CREATION_TIME + "} FROM {" + GeneratedWorkflowConstants.TC.WORKFLOWACTION + " as actions JOIN " + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION
                                                            + "* AS rel ON {rel:" + GeneratedCoreConstants.Attributes.Link.SOURCE + "}={actions:" + WorkflowAction.PK + "} JOIN " + GeneratedWorkflowConstants.TC.WORKFLOWITEMATTACHMENT + " AS attachment ON {rel:" + GeneratedCoreConstants.Attributes.Link.TARGET
                                                            + "}={attachment:" + WorkflowItemAttachment.PK + "} } WHERE {actions:status} IN (?status) AND ({actions:principalAssigned}=?session.user OR {actions:principalAssigned} IN (?session.user.allGroups)) AND {rel:"
                                                            + GeneratedCoreConstants.Attributes.Link.QUALIFIER + "} = '" + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION + "' AND {rel:" + GeneratedCoreConstants.Attributes.Link.LANGUAGE + "} IS NULL " + typeCondition + "ORDER BY {actions:"
                                                            + WorkflowAction.CREATION_TIME + "} ASC", params,
                                            Collections.singletonList(WorkflowAction.class), true, true, 0, -1);
            return res.getResult();
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
    }


    private ComposedType getTypeFromTypeCodeOrClassName(String typeCodeOrClassName)
    {
        if(!StringUtils.isWhitespace(typeCodeOrClassName))
        {
            try
            {
                return TypeManager.getInstance().getComposedType(typeCodeOrClassName.trim());
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.debug("The type " + typeCodeOrClassName + " could not be found. Trying to find by ClassName instead.");
                try
                {
                    Class<?> typeClass = Class.forName(typeCodeOrClassName.trim());
                    return TypeManager.getInstance().getComposedType(typeClass);
                }
                catch(ClassNotFoundException classNotFoundException)
                {
                    LOG.error("The class " + typeCodeOrClassName + " could not be found. Please check if the class name is the fully qualified name for the class.", classNotFoundException);
                }
            }
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getOwnerName(SessionContext ctx, Link item)
    {
        WorkflowDecisionTemplate dec = (WorkflowDecisionTemplate)item.getSource();
        return dec.getActionTemplate().getName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setOwnerName(SessionContext ctx, Link item, String value)
    {
        WorkflowDecisionTemplate dec = (WorkflowDecisionTemplate)item.getSource();
        dec.getActionTemplate().setName(ctx, value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public WorkflowAction createWorkflowAction(SessionContext ctx, Map attributeValues, ComposedType type)
    {
        try
        {
            return (WorkflowAction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ? (RuntimeException)cause : new JaloSystemException(cause, cause
                            .getMessage(), e
                            .getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating WorkflowAction : " + e.getMessage(), 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public WorkflowAction createWorkflowAction(Map attributeValues, ComposedType type)
    {
        return createWorkflowAction(getSession().getSessionContext(), attributeValues, type);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map<Language, String> getAllOwnerName(SessionContext ctx, Link item)
    {
        WorkflowDecisionTemplate dec = (WorkflowDecisionTemplate)item.getSource();
        return dec.getActionTemplate().getAllName(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllOwnerName(SessionContext ctx, Link item, Map<Language, String> value)
    {
        WorkflowDecisionTemplate dec = (WorkflowDecisionTemplate)item.getSource();
        dec.getActionTemplate().setAllName(ctx, value);
    }
}
