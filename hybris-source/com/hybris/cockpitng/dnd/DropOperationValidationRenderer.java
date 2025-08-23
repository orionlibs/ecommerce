/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

public class DropOperationValidationRenderer
{
    public static final String SCLASS_Y_DND_VALIDATION_POPUP = "y-dnd-validation-popup";
    public static final String SCLASS_Y_CONTROL_CONTAINER = "y-control-container";
    public static final String SCLASS_Y_SUMMARY_ERROR_INFO = "y-summary-error-info";
    public static final String SCLASS_Y_CONTENT_LIST = "y-content-list";
    public static final String SCLASS_Y_WINDOW_CONTAINER = "y-window-container";
    public static final String SCLASS_Y_MULTI_STATE_CHECK = "y-multi-state-check";
    //Deprecated
    public static final String SCLASS_Y_CANCEL = "y-cancel";
    public static final String SCLASS_Y_IGNORE_SAVE = "y-ignore-save y-btn-attention";
    public static final String SCLASS_Y_MULTI_SELECT = "y-multi-select";
    public static final String LABEL_DROP_OPERATION_VALIDATION_TITLE_ERROR = "drop.operation.validation.title.error";
    public static final String LABEL_DROP_OPERATION_VALIDATION_TITLE_ERRORS = "drop.operation.validation.title.errors";
    public static final String LABEL_DROP_OPERATION_VALIDATION_TITLE_WARNING = "drop.operation.validation.title.warning";
    public static final String LABEL_DROP_OPERATION_VALIDATION_TITLE_WARNINGS = "drop.operation.validation.title.warnings";
    public static final String LABEL_DROP_OPERATION_VALIDATION_SUMMARY_ERRORS = "drop.operation.validation.summary.errors";
    public static final String LABEL_DROP_OPERATION_VALIDATION_BTN_CANCEL = "drop.operation.validation.btn.cancel";
    public static final String LABEL_DROP_OPERATION_VALIDATION_BTN_IGNORE_AND_SAVE = "drop.operation.validation.btn.ignore.and.save";
    public static final String LABEL_DROP_OPERATION_VALIDATION_NO_TO_SAVE = "drop.operation.validation.no.to.save";
    private static final Logger LOG = LoggerFactory.getLogger(DropOperationValidationRenderer.class);
    protected static final Comparator<DropOperationValidationData> VALIDATION_SEVERITY_COMPARATOR = (left, right) -> {
        final int leftErrorCount = left.getValidationInfo(ValidationSeverity.ERROR).size();
        final int rightErrorCount = right.getValidationInfo(ValidationSeverity.ERROR).size();
        if(leftErrorCount == rightErrorCount)
        {
            final int leftWarnCount = left.getValidationInfo(ValidationSeverity.WARN).size();
            final int rightWarnCount = right.getValidationInfo(ValidationSeverity.WARN).size();
            return leftWarnCount - rightWarnCount;
        }
        else
        {
            return rightErrorCount - leftErrorCount;
        }
    };
    public static final String SCLASS_Y_ODD_ROW = "y-odd-row";
    private ListitemRenderer listItemRenderer;


    @Required
    public void setListItemRenderer(final ListitemRenderer listItemRenderer)
    {
        this.listItemRenderer = listItemRenderer;
    }


    public void askForConfirmation(final Component dropComponent, final List<DropOperationValidationData> validateResults,
                    final Consumer<Collection<DropOperationData>> confirmedAction,
                    final Consumer<Collection<DropOperationData>> cancelAction)
    {
        final Window window = createWindow();
        registerOnCloseListener(window, validateResults, cancelAction);
        final int totalErrorCount = computeTotalErrorCount(validateResults);
        final int totalWarningCount = computeTotalWarningCount(validateResults);
        window.setTitle(createTitleText(totalErrorCount, totalWarningCount));
        final Div windowContainer = new Div();
        windowContainer.setSclass(SCLASS_Y_WINDOW_CONTAINER);
        if(totalErrorCount > 0)
        {
            windowContainer.appendChild(createSummaryComponent());
        }
        final List<DropOperationValidationData> validationInfo = getValidationInfoList(validateResults);
        final Checkbox bulkSelectCheckbox = createBulkSelectCheckbox(validationInfo);
        windowContainer.appendChild(bulkSelectCheckbox);
        bulkSelectCheckbox.setVisible(isSavePossible(validateResults));
        windowContainer.appendChild(createListComponent(validateResults, bulkSelectCheckbox, validationInfo));
        windowContainer.appendChild(createControls(validateResults, confirmedAction, cancelAction, window));
        window.appendChild(windowContainer);
        applyParentComponentToWindow(window, dropComponent);
    }


    private Checkbox createBulkSelectCheckbox(final List<DropOperationValidationData> validationInfo)
    {
        final Checkbox checkbox = new Checkbox(computeCheckboxLabel(validationInfo));
        checkbox.setSclass(SCLASS_Y_MULTI_SELECT);
        checkbox.setChecked(true);
        checkbox.setDisabled(
                        validationInfo.stream().noneMatch(info -> CollectionUtils.isEmpty(info.getValidationInfo(ValidationSeverity.ERROR))
                                        && CollectionUtils.isNotEmpty(info.getValidationInfo(ValidationSeverity.WARN))));
        checkbox.addEventListener(Events.ON_CHECK, (CheckEvent check) -> {
            validationInfo.forEach(data -> data.setIgnoreValidation(check.isChecked(), checkbox));
            checkbox.setLabel(computeCheckboxLabel(validationInfo));
            UITools.removeSClass(checkbox, SCLASS_Y_MULTI_STATE_CHECK);
        });
        return checkbox;
    }


    protected String computeCheckboxLabel(final List<DropOperationValidationData> data)
    {
        final int total = data.size();
        final int ignored = findDataForSave(data).size();
        return Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_NO_TO_SAVE, new Object[] {ignored, total});
    }


    protected HtmlBasedComponent createControls(final List<DropOperationValidationData> validateResults,
                    final Consumer<Collection<DropOperationData>> confirmedAction,
                    final Consumer<Collection<DropOperationData>> cancelAction, final Window window)
    {
        final Div buttonsContainer = new Div();
        buttonsContainer.setSclass(SCLASS_Y_CONTROL_CONTAINER);
        buttonsContainer.appendChild(createCancelButton(window, validateResults, cancelAction));
        if(isSavePossible(validateResults))
        {
            buttonsContainer.appendChild(createSaveButton(window, validateResults, confirmedAction));
        }
        return buttonsContainer;
    }


    protected Window createWindow()
    {
        final Window window = new Window();
        window.setSclass(SCLASS_Y_DND_VALIDATION_POPUP);
        window.setMode("overlapped");
        window.setPosition("center,center");
        window.setBorder(true);
        window.setClosable(true);
        window.setSizable(true);
        return window;
    }


    protected void registerOnCloseListener(final Window window, final List<DropOperationValidationData> validateResults,
                    final Consumer<Collection<DropOperationData>> cancelAction)
    {
        window.addEventListener(Events.ON_CLOSE, event -> {
            window.setVisible(false);
            final Collection<DropOperationData> dataForRefresh = findAllValidationDataForRefresh(validateResults);
            cancelAction.accept(dataForRefresh);
        });
    }


    protected void applyParentComponentToWindow(final Window window, final Component component)
    {
        if(component != null)
        {
            try
            {
                window.setParent(component);
                window.doModal();
            }
            catch(final Exception exception)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Error while applying parent component to window", exception);
                }
                applyParentComponentToWindow(window, component.getParent());
            }
        }
    }


    protected int computeTotalErrorCount(final List<DropOperationValidationData> validateResults)
    {
        return validateResults.stream().mapToInt(data -> data.getValidationInfo(ValidationSeverity.ERROR).size()).sum();
    }


    protected int computeTotalWarningCount(final List<DropOperationValidationData> validateResults)
    {
        return validateResults.stream().mapToInt(data -> data.getValidationInfo(ValidationSeverity.WARN).size()).sum();
    }


    protected String createTitleText(final int errorCount, final int warningCount)
    {
        if(errorCount == 1)
        {
            return Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_TITLE_ERROR);
        }
        else if(errorCount > 1)
        {
            return Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_TITLE_ERRORS);
        }
        else if(warningCount == 1)
        {
            return Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_TITLE_WARNING);
        }
        else
        {
            return Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_TITLE_WARNINGS);
        }
    }


    protected HtmlBasedComponent createSummaryComponent()
    {
        final Div container = new Div();
        container.setSclass(SCLASS_Y_SUMMARY_ERROR_INFO);
        final Label label = new Label(Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_SUMMARY_ERRORS));
        container.appendChild(label);
        return container;
    }


    protected HtmlBasedComponent createListComponent(final List<DropOperationValidationData> validateResults,
                    final Checkbox bulkSelectCheckbox, final List<DropOperationValidationData> validationInfo)
    {
        final Listbox listBox = new Listbox();
        listBox.setSclass(SCLASS_Y_CONTENT_LIST);
        listBox.setOddRowSclass(SCLASS_Y_ODD_ROW);
        listBox.setDisabled(true);
        listBox.setNonselectableTags("*");
        final ListModelArray<Object> listModel = new ListModelArray<>(validationInfo);
        listBox.setModel(listModel);
        listBox.setItemRenderer(listItemRenderer);
        validateResults.forEach(data -> data.addIgnoreValidationChangedListener(o -> {
            if(bulkSelectCheckbox != o)
            {
                final boolean isMultiState;
                if(data.isIgnoreValidation())
                {
                    isMultiState = validateResults.stream()
                                    .filter(result -> !result.hasIssuesOfSeverity(ValidationSeverity.ERROR) && !result.isIgnoreValidation())
                                    .count() > 0;
                }
                else
                {
                    isMultiState = validateResults.stream()
                                    .filter(result -> !result.hasIssuesOfSeverity(ValidationSeverity.ERROR) && result.isIgnoreValidation())
                                    .count() > 0;
                }
                if(isMultiState)
                {
                    bulkSelectCheckbox.setChecked(false);
                    UITools.addSClass(bulkSelectCheckbox, SCLASS_Y_MULTI_STATE_CHECK);
                }
                else
                {
                    bulkSelectCheckbox.setChecked(data.isIgnoreValidation());
                    UITools.removeSClass(bulkSelectCheckbox, SCLASS_Y_MULTI_STATE_CHECK);
                }
                bulkSelectCheckbox.setLabel(computeCheckboxLabel(validateResults));
            }
        }));
        return listBox;
    }


    protected Button createCancelButton(final Window window, final List<DropOperationValidationData> validateResults,
                    final Consumer<Collection<DropOperationData>> cancelAction)
    {
        final Button button = new Button(Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_BTN_CANCEL));
        button.addEventListener(Events.ON_CLICK, click -> {
            window.setVisible(false);
            final Collection<DropOperationData> dataForRefresh = findAllValidationDataForRefresh(validateResults);
            cancelAction.accept(dataForRefresh);
        });
        return button;
    }


    protected Button createSaveButton(final Window window, final List<DropOperationValidationData> validateResults,
                    final Consumer<Collection<DropOperationData>> confirmedAction)
    {
        final Button button = new Button(Labels.getLabel(LABEL_DROP_OPERATION_VALIDATION_BTN_IGNORE_AND_SAVE));
        button.setSclass(SCLASS_Y_IGNORE_SAVE);
        button.addEventListener(Events.ON_CLICK, e -> {
            window.setVisible(false);
            final Collection<DropOperationData> dataForSave = findDataForSave(validateResults);
            confirmedAction.accept(dataForSave);
        });
        validateResults.forEach(
                        data -> data.addIgnoreValidationChangedListener(e -> button.setDisabled(!canAnyItemBeSaved(validateResults))));
        return button;
    }


    protected boolean canAnyItemBeSaved(final List<DropOperationValidationData> validateResults)
    {
        return validateResults.stream().anyMatch(this::canSaveItem);
    }


    protected boolean canSaveItem(final DropOperationValidationData data)
    {
        final boolean hasIgnoredWarnings = data.hasIssuesOfSeverity(ValidationSeverity.WARN) && data.isIgnoreValidation();
        return hasIgnoredWarnings && !data.hasIssuesOfSeverity(ValidationSeverity.ERROR);
    }


    protected boolean isSavePossible(final List<DropOperationValidationData> validateResults)
    {
        final long itemsWithError = validateResults.stream().filter(data -> data.hasIssuesOfSeverity(ValidationSeverity.ERROR))
                        .count();
        return validateResults.size() > itemsWithError;
    }


    protected List<DropOperationData> findDataForSave(
                    final List<DropOperationValidationData> validateResults)
    {
        return validateResults.stream()
                        .filter(result -> result.isIgnoreValidation() && result.getValidationInfo(ValidationSeverity.ERROR).isEmpty())
                        .map(DropOperationValidationData::getDropOperationData).collect(Collectors.toList());
    }


    protected List<DropOperationData> findAllValidationDataForRefresh(
                    final List<DropOperationValidationData> validationResults)
    {
        return validationResults.stream()
                        .map(DropOperationValidationData::getDropOperationData).collect(Collectors.toList());
    }


    protected List<DropOperationValidationData> getValidationInfoList(final List<DropOperationValidationData> validateResults)
    {
        return validateResults.stream().filter(hasValidationSeverityToDisplay())
                        .sorted(getValidationSeverityComparator()).collect(Collectors.toList());
    }


    protected Predicate<DropOperationValidationData> hasValidationSeverityToDisplay()
    {
        return result -> !result.getValidationInfo(ValidationSeverity.ERROR).isEmpty()
                        || !result.getValidationInfo(ValidationSeverity.WARN).isEmpty();
    }


    protected Comparator<DropOperationValidationData> getValidationSeverityComparator()
    {
        return VALIDATION_SEVERITY_COMPARATOR;
    }
}
