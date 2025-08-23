package de.hybris.platform.workflow.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.localization.Localization;
import java.io.Serializable;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class AbstractWorkflowAction extends GeneratedAbstractWorkflowAction
{
    private static final Logger LOG = Logger.getLogger(AbstractWorkflowAction.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(allAttributes.get("code") == null)
        {
            allAttributes.put("code", WorkflowManager.getInstance().getNextActionNumber());
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    protected JaloPropertyContainer getInitialProperties(JaloSession jaloSession, Item.ItemAttributeMap allAttributes)
    {
        JaloPropertyContainer props = jaloSession.createPropertyContainer();
        props.setProperty("code", allAttributes.get("code"));
        return props;
    }


    @ForceJALO(reason = "something else")
    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap ret = super.getNonInitialAttributes(ctx, allAttributes);
        ret.remove("code");
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public String getPredecessorsStr(SessionContext ctx)
    {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(AbstractWorkflowAction action : getPredecessors(ctx))
        {
            if(first)
            {
                first = false;
            }
            else
            {
                builder.append(", ");
            }
            builder.append(action.getName());
        }
        return builder.toString();
    }


    @ForceJALO(reason = "something else")
    public void setPrincipalAssigned(SessionContext ctx, Principal value)
    {
        if(!TypeManager.getInstance().getComposedType(WorkflowAction.class).checkPermission(value, AccessManager.getInstance().getOrCreateUserRightByCode("read")))
        {
            throw new JaloSystemException(Localization.getLocalizedString("error.workflowaction.principalassigned.readaccess", new Object[] {value
                            .getUID()}));
        }
        if(value instanceof User)
        {
            User user = (User)value;
            if(user.isLoginDisabledAsPrimitive() || (user
                            .getDeactivationDate() != null && user.getDeactivationDate().toInstant().isBefore(Instant.now())))
            {
                throw new JaloSystemException(Localization.getLocalizedString("error.workflowaction.principalassigned.hmcaccess", new Object[] {value
                                .getUID()}));
            }
        }
        super.setPrincipalAssigned(ctx, value);
    }


    private Map<String, Serializable> createWorkflowActionCommentMap(String comment, User user)
    {
        Map<String, Serializable> workflowActionCommentMap = new HashMap<>();
        workflowActionCommentMap.put("comment", comment);
        workflowActionCommentMap.put("user", user);
        return workflowActionCommentMap;
    }


    public void writeAutomatedComment(WorkflowAction action, String message, String[] messageParams) throws JaloSystemException
    {
        action.addToWorkflowActionComments(WorkflowManager.getInstance().createWorkflowActionComment(
                        createWorkflowActionCommentMap(
                                        MessageFormat.format(Localization.getLocalizedString(message, (Object[])messageParams), (Object[])null), null)));
    }


    public void writeAutomatedComment(WorkflowAction action, String message) throws JaloSystemException
    {
        action.addToWorkflowActionComments(WorkflowManager.getInstance()
                        .createWorkflowActionComment(
                                        createWorkflowActionCommentMap(MessageFormat.format(
                                                        Localization.getLocalizedString(message), (Object[])null), null)));
    }
}
