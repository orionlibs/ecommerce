/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility methods for operating on Workflow Designer nodes.
 */
public class NodeTypeService
{
    private static final Set<String> ACTION_GROUPS = Set.of(WorkflowDesignerGroup.ACTION.getValue(),
                    WorkflowDesignerGroup.START_ACTION.getValue(), WorkflowDesignerGroup.END_ACTION.getValue());
    private static final String UNSAVED_ACTION_FORMAT = "action:unsaved[code=%s]";
    private static final String SAVED_ACTION_FORMAT = "action:saved[pk=%s,code=%s]";
    private static final String SAVED_DECISION_FORMAT = "decision:saved[pk=%s,code=%s]";
    private static final String UNSAVED_DECISION_FORMAT = "decision:unsaved[code=%s]";
    private static final String CODE_EXTRACTING_REGEX = "(action|decision):(saved|unsaved)\\[.*code=(?<code>.*)]";
    private static final Pattern CODE_EXTRACTING_PATTERN = Pattern.compile(CODE_EXTRACTING_REGEX);


    /**
     * Checks if given node is a Workflow Action
     *
     * @param node
     *           node to check
     * @return true if it's an Action node
     */
    public boolean isAction(final Node node)
    {
        return ACTION_GROUPS.contains(node.getGroup());
    }


    /**
     * Checks if given node is a Workflow Decision
     *
     * @param node
     *           node to check
     * @return true it it's a Decision node
     */
    public boolean isDecision(final Node node)
    {
        return Objects.equals(node.getGroup(), WorkflowDesignerGroup.DECISION.getValue());
    }


    /**
     * Checks if given Node is an And node
     *
     * @param node
     *           node to check
     * @return true if it's an And node
     */
    public boolean isAnd(final Node node)
    {
        return Objects.equals(node.getGroup(), WorkflowDesignerGroup.AND.getValue());
    }


    /**
     * Checks if given {@link Node} corresponds to the given {@link WorkflowActionTemplateModel}. For equality the
     * following attributes are compared:
     * <ul>
     * <li>type</li>
     * <li>state (saved or unsaved)</li>
     * <li>code (in case of unsaved state)</li>
     * <li>pk (in case of saved state)</li>
     * </ul>
     *
     * @param action
     *           to be compared
     * @param node
     *           to be compared
     * @return true if node corresponds to the given action
     */
    public boolean isSameAction(final WorkflowActionTemplateModel action, final Node node)
    {
        return Objects.equals(generateNodeData(action), node.getData());
    }


    /**
     * Generates action representation for node to be easily identified with model. For unsaved models the representation
     * contains the type and code of action, for saved -- type, pk and persistence version.
     *
     * @param action
     *           to generate the node data for
     * @return node data
     */
    public String generateNodeData(final AbstractWorkflowActionModel action)
    {
        if(action.getPk() != null)
        {
            return String.format(SAVED_ACTION_FORMAT, action.getPk(), action.getCode());
        }
        return String.format(UNSAVED_ACTION_FORMAT, action.getCode());
    }


    /**
     * Checks if given {@link Node} corresponds to the given {@link WorkflowDecisionTemplateModel}. For equality the
     * following attributes are compared:
     * <ul>
     * <li>type</li>
     * <li>state (saved or unsaved)</li>
     * <li>code (in case of unsaved state)</li>
     * <li>pk (in case of saved state)</li>
     * </ul>
     *
     * @param decision
     *           to be compared
     * @param node
     *           to be compared
     * @return true if node corresponds to the given decision
     */
    public boolean isSameDecision(final WorkflowDecisionTemplateModel decision, final Node node)
    {
        return Objects.equals(generateNodeData(decision), node.getData());
    }


    /**
     * Generates action representation for node to be easily identified with model. For unsaved models the representation
     * contains the type and code of decision, for saved -- type, pk and persistence version.
     *
     * @param decision
     *           to generate the node data for
     * @return node data
     */
    public String generateNodeData(final AbstractWorkflowDecisionModel decision)
    {
        if(decision.getPk() != null)
        {
            return String.format(SAVED_DECISION_FORMAT, decision.getPk(), decision.getCode());
        }
        return String.format(UNSAVED_DECISION_FORMAT, decision.getCode());
    }


    /**
     * Checks if node contains given code
     *
     * @param node
     *           that contains code to be checked
     * @param code
     *           code
     * @return true if node contains given code, false otherwise
     */
    public boolean hasCode(final Node node, final String code)
    {
        final Matcher matcher = CODE_EXTRACTING_PATTERN.matcher(String.valueOf(node.getData()));
        if(matcher.matches())
        {
            return StringUtils.equals(code, matcher.group("code"));
        }
        return false;
    }


    /**
     * Returns the node display name.
     *
     * @param node
     *           input node
     * @return node's display name
     */
    public String getNodeName(final Node node)
    {
        return StringUtils.firstNonBlank(node.getLabel(), node.getTitle());
    }
}
