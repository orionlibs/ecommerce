/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.renderer;

import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

/**
 * Contains common methods for Action renderers.
 */
public abstract class AbstractActionRenderer implements NetworkEntityRenderer
{
    static final String ASSIGNED_USER_LABEL = "workflow.visualization.details.assigneduser";
    static final String LAST_UPDATE_LABEL = "workflow.visualization.details.lastupdate";
    private static final String DETAILS_ENTRY_DELIMITER = "<br>";
    private static final String DETAILS_ENTRY_FORMAT = "<strong>%s</strong>: %s";
    private KeyGenerator keyGenerator;
    private NodeTypeService nodeTypeService;
    private WorkflowEntityImageCreator workflowEntityImageCreator;
    private LabelService labelService;


    /**
     * Retrieves the status of given workflow and converts it to text-based format
     *
     * @param model
     *           to retrieve status from
     * @return text-based formatted workflow status
     */
    Optional<String> getActionStatus(final AbstractWorkflowActionModel model)
    {
        return Optional.of(model).filter(WorkflowActionModel.class::isInstance).map(WorkflowActionModel.class::cast)
                        .map(WorkflowActionModel::getStatus).map(AbstractActionRenderer::convertWorkflowStatusCodeToCamelCase);
    }


    private static String convertWorkflowStatusCodeToCamelCase(final WorkflowActionStatus workflowActionStatus)
    {
        return convertSnakeCaseToCamelCase("action_" + workflowActionStatus.getCode());
    }


    private static String convertSnakeCaseToCamelCase(final String value)
    {
        final String camelCaseValue = WordUtils.capitalizeFully(value.toLowerCase(Locale.ENGLISH), new char[]
                        {'_'}).replaceAll("_", StringUtils.EMPTY);
        return WordUtils.uncapitalize(camelCaseValue);
    }


    private String createDetailsMessage(final WorkflowActionModel action)
    {
        final Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put(ASSIGNED_USER_LABEL, action.getPrincipalAssigned());
        detailsMap.put(LAST_UPDATE_LABEL, action.getModifiedtime());
        return detailsMap.entrySet().stream().filter(entry -> Objects.nonNull(entry.getValue())).map(this::createDetailsEntry)
                        .collect(Collectors.joining(DETAILS_ENTRY_DELIMITER));
    }


    private String createDetailsEntry(final Map.Entry<String, Object> entry)
    {
        return String.format(DETAILS_ENTRY_FORMAT, Labels.getLabel(entry.getKey()), labelService.getObjectLabel(entry.getValue()));
    }


    String getTitle(final AbstractWorkflowActionModel model)
    {
        return model instanceof WorkflowActionModel ? createDetailsMessage((WorkflowActionModel)model) : getLabel(model);
    }


    String getLabel(final AbstractWorkflowActionModel model)
    {
        return StringUtils.isEmpty(model.getName()) ? String.format("[%s]", model.getCode()) : model.getName();
    }


    String getCssClass(final AbstractWorkflowActionModel model)
    {
        return Optional.of(model).filter(WorkflowActionModel.class::isInstance).map(WorkflowActionModel.class::cast)
                        .map(WorkflowActionModel::getStatus).map(WorkflowActionStatus::getCode).map(e -> StringUtils.replace(e, "_", "-"))
                        .orElse(StringUtils.EMPTY);
    }


    @Required
    public void setKeyGenerator(final KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    public KeyGenerator getKeyGenerator()
    {
        return keyGenerator;
    }


    @Required
    public void setNodeTypeService(final NodeTypeService nodeTypeService)
    {
        this.nodeTypeService = nodeTypeService;
    }


    public NodeTypeService getNodeTypeService()
    {
        return nodeTypeService;
    }


    @Required
    public void setWorkflowEntityImageCreator(final WorkflowEntityImageCreator workflowEntityImageCreator)
    {
        this.workflowEntityImageCreator = workflowEntityImageCreator;
    }


    public WorkflowEntityImageCreator getWorkflowEntityImageCreator()
    {
        return workflowEntityImageCreator;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }
}
