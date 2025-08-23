/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Li;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class ValidationResultListItemRenderer implements ListitemRenderer<DropOperationValidationData>
{
    public static final String SCLASS_Y_MESSAGE = "y-message";
    public static final String SCLASS_Y_TITLE = "y-title";
    public static final String SCLASS_Y_VALIDATION_ICON_CELL = "y-validation-icon-cell";
    public static final String SCLASS_Y_DETAILS_CELL = "y-details-cell";
    public static final String SCLASS_Y_SHOW_DETAILS_BTN = "y-show-details-btn";
    public static final String SCLASS_Y_SELECT_CELL = "y-select-cell";
    public static final String SCLASS_Y_VALIDATION_DETAILS_CONTAINER = "y-validation-details-container";
    public static final String SCLASS_Y_ERROR = "y-error";
    public static final String SCLASS_Y_WARN = "y-warn";
    public static final String SCLASS_Y_VALIDATION_OVERVIEW_CONTAINER = "y-validation-overview-container";
    public static final String SCLASS_Y_ROW_SELECTED = "y-row-selected";
    public static final String LABEL_DROP_OPERATION_VALIDATION_TOOLTIP_ERRORS = "drop.operation.validation.tooltip.errors";
    public static final String LABEL_DROP_OPERATION_VALIDATION_TOOLTIP_WARNINGS = "drop.operation.validation.tooltip.warnings";
    public static final String LABEL_DROP_OPERATION_VALIDATION_ITEM_MESSAGE_ERRORS = "drop.operation.validation.item.message.errors";
    public static final String LABEL_DROP_OPERATION_VALIDATION_ITEM_MESSAGE_WARNINGS = "drop.operation.validation.item.message.warnings";
    public static final String LABEL_DROP_OPERATION_VALIDATION_ITEM_MESSAGE_ERRORS_WARNINGS = "drop.operation.validation.item.message.errors&warnings";
    private LabelService labelService;


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    @Override
    public void render(final Listitem listItem, final DropOperationValidationData validationResult, final int index)
    {
        final Listcell selectCell = createSelectListcell(validationResult, listItem);
        listItem.appendChild(selectCell);
        final Listcell infoCell = new Listcell();
        final Div validationIOverview = new Div();
        validationIOverview.setSclass(SCLASS_Y_VALIDATION_OVERVIEW_CONTAINER);
        validationIOverview.appendChild(createTitleComponent(validationResult));
        final Label messageComponent = createValidationMessageComponent(validationResult);
        validationIOverview.appendChild(messageComponent);
        infoCell.appendChild(validationIOverview);
        final Div validationDetails = new Div();
        validationDetails.setSclass(SCLASS_Y_VALIDATION_DETAILS_CONTAINER);
        validationDetails.setVisible(false);
        infoCell.appendChild(validationDetails);
        listItem.appendChild(infoCell);
        final Listcell iconCell = createIconListcell(validationResult);
        listItem.appendChild(iconCell);
        final Listcell detailsCell = createDetailsListcell(validationResult, messageComponent, validationDetails);
        listItem.appendChild(detailsCell);
        listItem.setTooltiptext(createTooltipForCell(validationResult));
    }


    private Listcell createSelectListcell(final DropOperationValidationData validationResult, final Listitem listItem)
    {
        final Listcell selectCell = new Listcell();
        UITools.modifySClass(listItem, SCLASS_Y_ROW_SELECTED, validationResult.isIgnoreValidation() && !validationResult.hasIssuesOfSeverity(ValidationSeverity.ERROR));
        selectCell.setSclass(SCLASS_Y_SELECT_CELL);
        if(!validationResult.hasIssuesOfSeverity(ValidationSeverity.ERROR))
        {
            final Checkbox ruleSelector = new Checkbox();
            ruleSelector.setChecked(validationResult.isIgnoreValidation());
            ruleSelector.addEventListener(Events.ON_CHECK, (CheckEvent event) -> {
                validationResult.setIgnoreValidation(event.isChecked(), ValidationResultListItemRenderer.this);
                UITools.modifySClass(listItem, SCLASS_Y_ROW_SELECTED, event.isChecked());
            });
            validationResult.addIgnoreValidationChangedListener(source -> {
                if(ValidationResultListItemRenderer.this != source)
                {
                    ruleSelector.setChecked(validationResult.isIgnoreValidation());
                    UITools.modifySClass(listItem, SCLASS_Y_ROW_SELECTED, validationResult.isIgnoreValidation());
                }
            });
            selectCell.appendChild(ruleSelector);
        }
        return selectCell;
    }


    private Listcell createIconListcell(final DropOperationValidationData validationResult)
    {
        final Listcell iconCell = new Listcell();
        iconCell.setSclass(SCLASS_Y_VALIDATION_ICON_CELL);
        final Div iconContainer = new Div();
        UITools.addSClass(iconContainer, findPessimisticValidationSclass(validationResult));
        iconCell.appendChild(iconContainer);
        return iconCell;
    }


    private Listcell createDetailsListcell(final DropOperationValidationData validationResult, final Label messageComponent,
                    final Div validationDetails)
    {
        final Listcell detailsCell = new Listcell();
        detailsCell.setSclass(SCLASS_Y_DETAILS_CELL);
        if(validationResult.getTotalViolations() > 1)
        {
            final A details = new A("");
            details.setSclass(SCLASS_Y_SHOW_DETAILS_BTN);
            detailsCell.appendChild(details);
            details.addEventListener(Events.ON_CLICK, e -> {
                if(CollectionUtils.isEmpty(validationDetails.getChildren()))
                {
                    validationDetails.appendChild(prepareValidationDetails(validationResult));
                }
                final boolean visible = !validationDetails.isVisible();
                validationDetails.setVisible(visible);
                messageComponent.setVisible(!visible);
                UITools.modifySClass(details, "y-expanded-details", visible);
            });
        }
        return detailsCell;
    }


    protected HtmlBasedComponent prepareValidationDetails(final DropOperationValidationData validationData)
    {
        final Div container = new Div();
        displayAsList(container, validationData, ValidationSeverity.ERROR);
        displayAsList(container, validationData, ValidationSeverity.WARN);
        return container;
    }


    protected void displayAsList(final Div container, final DropOperationValidationData validationData,
                    final ValidationSeverity validationSeverity)
    {
        final List<ValidationInfo> infoList = validationData.getValidationInfo(validationSeverity);
        if(CollectionUtils.isNotEmpty(infoList))
        {
            infoList.forEach(warning -> {
                final Li list = new Li();
                container.appendChild(list);
                final Div iconCell = new Div();
                iconCell.setSclass(SCLASS_Y_VALIDATION_ICON_CELL);
                list.appendChild(iconCell);
                final Div iconContainer = new Div();
                UITools.addSClass(iconContainer, findValidationSclass(warning.getValidationSeverity()));
                iconCell.appendChild(iconContainer);
                final Label errorLabel = new Label(warning.getValidationMessage());
                list.appendChild(errorLabel);
            });
        }
    }


    protected String createTooltipForCell(final DropOperationValidationData result)
    {
        final StringBuilder sb = new StringBuilder();
        if(!result.getValidationInfo(ValidationSeverity.ERROR).isEmpty())
        {
            sb.append(Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_TOOLTIP_ERRORS));
            sb.append(getNewLine());
            result.getValidationInfo(ValidationSeverity.ERROR).forEach(data -> {
                sb.append(data.getValidationMessage());
                sb.append(getNewLine());
            });
            sb.append(getNewLine());
        }
        if(!result.getValidationInfo(ValidationSeverity.WARN).isEmpty())
        {
            sb.append(Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_TOOLTIP_WARNINGS));
            sb.append(getNewLine());
            result.getValidationInfo(ValidationSeverity.WARN).forEach(data -> {
                sb.append(data.getValidationMessage());
                sb.append(getNewLine());
            });
            sb.append(getNewLine());
        }
        return sb.toString();
    }


    protected String getNewLine()
    {
        return System.getProperty("line.separator");
    }


    private String findPessimisticValidationSclass(final DropOperationValidationData validationResult)
    {
        return validationResult.hasIssuesOfSeverity(ValidationSeverity.ERROR) ? SCLASS_Y_ERROR : SCLASS_Y_WARN;
    }


    private String findValidationSclass(final ValidationSeverity validationSeverity)
    {
        if(ValidationSeverity.ERROR.equals(validationSeverity))
        {
            return SCLASS_Y_ERROR;
        }
        else
        {
            return SCLASS_Y_WARN;
        }
    }


    private Label createValidationMessageComponent(final DropOperationValidationData validationData)
    {
        final Label label = new Label(createValidationMessage(validationData));
        label.setSclass(SCLASS_Y_MESSAGE);
        return label;
    }


    private Label createTitleComponent(final DropOperationValidationData result)
    {
        final String titleText = labelService.getObjectLabel(result.getDropOperationData().getDragged());
        final Label titleLabel = new Label(titleText);
        titleLabel.setSclass(SCLASS_Y_TITLE);
        return titleLabel;
    }


    protected String createValidationMessage(final DropOperationValidationData validationData)
    {
        final int errorCount = validationData.getValidationInfo(ValidationSeverity.ERROR).size();
        final int warnCount = validationData.getValidationInfo(ValidationSeverity.WARN).size();
        final String validationMessage;
        if(errorCount == 1 && warnCount == 0)
        {
            validationMessage = validationData.getValidationInfo(ValidationSeverity.ERROR).get(0).getValidationMessage();
        }
        else if(errorCount == 0 && warnCount == 1)
        {
            validationMessage = validationData.getValidationInfo(ValidationSeverity.WARN).get(0).getValidationMessage();
        }
        else if(errorCount > 0 && warnCount == 0)
        {
            validationMessage = Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_ITEM_MESSAGE_ERRORS, new Object[]
                            {errorCount});
        }
        else if(errorCount == 0 && warnCount > 0)
        {
            validationMessage = Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_ITEM_MESSAGE_WARNINGS, new Object[]
                            {warnCount});
        }
        else
        {
            validationMessage = Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_ITEM_MESSAGE_ERRORS_WARNINGS, new Object[]
                            {errorCount, warnCount});
        }
        return validationMessage;
    }
}
