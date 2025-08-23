/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.renderer;

import static com.hybris.cockpitng.util.notifications.event.NotificationEvent.Level;

import com.google.common.collect.Maps;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.components.validation.EditorValidation;
import com.hybris.cockpitng.components.validation.ValidatableContainer;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.config.jaxb.wizard.AbstractActionType;
import com.hybris.cockpitng.config.jaxb.wizard.AdditionalParamType;
import com.hybris.cockpitng.config.jaxb.wizard.BackType;
import com.hybris.cockpitng.config.jaxb.wizard.CancelType;
import com.hybris.cockpitng.config.jaxb.wizard.ComposedHandlerType;
import com.hybris.cockpitng.config.jaxb.wizard.ContentType;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.config.jaxb.wizard.InfoType;
import com.hybris.cockpitng.config.jaxb.wizard.Parameter;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyListType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyType;
import com.hybris.cockpitng.config.jaxb.wizard.Renderer;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.renderers.attributedescription.AttributeDescriptionIconRenderer;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.CockpitComponentsUtils;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitorFactory;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowDefinitions;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import com.hybris.cockpitng.widgets.configurableflow.listener.EditorChangeListener;
import com.hybris.cockpitng.widgets.configurableflow.listener.TransitionListener;
import com.hybris.cockpitng.widgets.configurableflow.listener.TransitionListenerFactory;
import com.hybris.cockpitng.widgets.configurableflow.util.ConfigurableFlowExpressions;
import com.hybris.cockpitng.widgets.configurableflow.util.ConfigurableFlowLabelService;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationResultsPopup;
import com.hybris.cockpitng.widgets.configurableflow.validation.ValidationAwareCustomViewRenderer;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

/**
 * Configurable Flow renderer
 */
public class ConfigurableFlowRenderer
{
    public static final String WIZARD_STYLE_BR_LABEL = "yw-wizard-breadcrumb-label";
    public static final String WIZARD_STYLE_BR_S_LABEL = "yw-wizard-breadcrumb-sublabel";
    public static final String WIZARD_STYLE_BR_SP = "yw-wizard-breadcrumb-spacer";
    public static final String WIZARD_STYLE_BR_SP_ARROW = "yw-wizard-breadcrumb-spacerarrow";
    public static final String WIZARD_STYLE_BR_ARROW = "yw-wizard-breadcrumb-arrow";
    public static final String WIZARD_STYLE_BR_PREFIX = "yw-wizard-breadcrumb";
    public static final String WIZARD_STYLE_BEFORE = "-before";
    public static final String WIZARD_STYLE_ACTIVE = "-active";
    public static final String WIZARD_STYLE_AFTER = "-after";
    public static final String WIZARD_STYLE_Z_INDEX = "z-index: ";
    public static final String WIZARD_STYLE_LAYOUT = "yw-wizard-layout";
    public static final String WIZARD_STYLE_CONTENT = "yw-wizard-content";
    public static final String WIZARD_STYLE_COLUMN_CONTENT = "yw-wizard-column-content";
    public static final String WIZARD_STYLE_INFO = "yw-info-box";
    public static final String WIZARD_STYLE_NAVI = "yw-wizard-navigation";
    public static final String WIZARD_STYLE_LEFT_PACK = "yw-left-pack";
    public static final String WIZARD_STYLE_RIGHT_PACK = "yw-right-pack";
    public static final String WIZARD_STYLE_BTN_BLUE = "yw-btn-primary";
    public static final String WIZARD_STYLE_BTN_SECONDARY = "y-btn-secondary";
    public static final String WIZARD_STYLE_PROPERTY = "yw-wizard-property";
    public static final String WIZARD_STYLE_PROPERTY_LABEL = "yw-wizard-property-label";
    public static final String WIZARD_STYLE_PROPERTY_LABEL_MANDATORY = "yw-wizard-property-label-mandatory";
    public static final String WIZARD_STYLE_READ_ONLY_VALUE = "yw-wizard-readonly-value";
    public static final String WIZARD_STYLE_NONE = "none";
    public static final String WIZARD_STYLE_LEFT = "left";
    public static final String WIZARD_STYLE_RIGHT = "right";
    public static final String WIZARD_STYLE_TOP = "top";
    public static final String WIZARD_RESULT_MSG_SUCCESS_TILE = "succeedResultMsgTile";
    public static final String WIZARD_RESULT_MSG_FAIL_TILE = "failureResultMsgTile";
    public static final String WIZARD_RESULT_MSG_TITLE = "resultMsgTitle";
    public static final String WIZARD_RESULT_MSG_TEXT = "resultMsgMessage";
    public static final String ZUL = "zul";
    public static final String HUNDRED_PERCENT_S = "100%";
    public static final String WIZARD_PREFIX = "wiz_";
    public static final String SEPARATOR = ".";
    public static final int HUNDRED_PERCENT = 100;
    public static final String CURRENT_OBJECT = "currentObject";
    public static final String IS_NESTED_OBJECT_CREATION_DISABLED_SETTING = "isNestedObjectCreationDisabled";
    public static final String STEP_INITIALIZED = "stepInitialized";
    public static final String STEP_ID_ATTRIBUTE = "stepId";
    public static final String SCLASS_COMPONENT_WRAPPER = "yw-component-wrapper";
    protected static final String NO_READ_ACCESS = "data.not.visible";
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableFlowRenderer.class);
    public static final String ON_FORCE_UPDATE = "onForceUpdate";
    protected final ConfigurableFlowExpressions cfExpressions = new ConfigurableFlowExpressions();
    protected boolean showBreadcrumb = true;
    private EditorChangeListener editorChangeListener;
    private Div contentDiv;
    private Div breadcrumbDiv;
    private Div msgAreaDiv;
    private Div navigationDiv;
    private WidgetInstanceManager widgetInstanceManager;
    private List<Locale> activeLocales;
    private TypeFacade typeFacade;
    private List<StepType> allSteps;
    private TransitionListenerFactory transitionListenerFactory;
    private ConfigurableFlowLabelService configurableFlowLabelService;
    private PermissionFacade permissionFacade;
    private AttributeDescriptionIconRenderer attributeDescriptionIconRenderer;
    private CockpitLocaleService cockpitLocaleService;
    private CockpitUserService cockpitUserService;
    private LabelService labelService;
    private ComponentsVisitorFactory componentsVisitorFactory;
    private ComponentsVisitor componentsVisitor;
    private ValidationRenderer validationRenderer;
    private ConfigurableFlowValidationResultsPopup validationResultsPopup;
    private ValidationHandler validationHandler;
    private ValidatableContainer validatableContainer;
    private NotificationService notificationService;


    public ConfigurableFlowRenderer()
    {
        // do nothing
    }


    private static Optional<String> extractLocalizedType(final PropertyType property)
    {
        final Matcher matcher = EditorUtils.getLocalizedEditorPattern().matcher(property.getType());
        return matcher.find() && matcher.groupCount() > 0 ? Optional.ofNullable(matcher.group(1)) : Optional.empty();
    }


    private static String getVisibleExpression(final BackType backButtonConfig)
    {
        return backButtonConfig == null ? null : backButtonConfig.getVisible();
    }


    private static String extractTextContent(final List<Serializable> mixedContent)
    {
        final StringBuilder textContent = new StringBuilder();
        for(final Serializable serializable : mixedContent)
        {
            if(serializable instanceof String)
            {
                textContent.append(serializable);
            }
        }
        return textContent.toString();
    }


    private static Optional<Renderer> extractRenderer(final List<Serializable> mixedContent)
    {
        Renderer ret = null;
        for(final Serializable serializable : mixedContent)
        {
            if(serializable instanceof JAXBElement && ((JAXBElement)serializable).getValue() instanceof Renderer)
            {
                ret = (Renderer)((JAXBElement)serializable).getValue();
            }
        }
        return Optional.ofNullable(ret);
    }


    private static void renderByZulFile(final Component parent, final ViewType customView)
    {
        try
        {
            Executions.createComponents(customView.getSrc(), parent, null);
        }
        catch(final RuntimeException e)
        {
            LOG.error(e.getMessage(), e);
        }
    }


    private static Map<String, String> extractParameters(final List<Parameter> parameterList)
    {
        final Map<String, String> parameters = Maps.newHashMap();
        if(parameterList != null)
        {
            for(final Parameter parameter : parameterList)
            {
                parameters.put(parameter.getName(), parameter.getValue());
            }
        }
        return parameters;
    }


    public void setValidatableContainer(final ValidatableContainer validatableContainer)
    {
        this.validatableContainer = validatableContainer;
    }


    public void setEditorChangeListener(final EditorChangeListener editorChangeListener)
    {
        this.editorChangeListener = editorChangeListener;
    }


    public List<Locale> getActiveLocales()
    {
        if(activeLocales == null)
        {
            activeLocales = cockpitLocaleService.getAvailableDataLocales(cockpitUserService.getCurrentUser());
        }
        return activeLocales;
    }


    public void setActiveLocales(final List<Locale> activeLocales)
    {
        this.activeLocales = activeLocales;
    }


    protected Component createPropertyLine(final String prefix, final PropertyType property)
    {
        boolean localized = false;
        boolean readOnly = BooleanUtils.isTrue(property.isReadonly());
        boolean canRead = true;
        final MutableBoolean optionalSetFlag = new MutableBoolean(false);
        final MutableBoolean optionalValue = new MutableBoolean(true);
        final DataType definedDataType;
        final Object currentObject;
        String valueType = property.getType();
        String attributeDataDescription = null;
        String labelText = null;
        // "newObject.name"
        final String fullPath = normalizePropertyQualifier(prefix, property);
        // "newObject"
        final String itemPath = extractTypeQualifier(fullPath);
        // "name"
        final String attributeQualifier = normalizeQualifier(fullPath);
        if(StringUtils.isNotBlank(itemPath))
        {
            currentObject = widgetInstanceManager.getModel().getValue(itemPath, Object.class);
            if(currentObject == null)
            {
                LOG.warn(
                                "Unable to determine a root item for {} as a part of {}. Check if provided qualifier is correct and remove any . if not required!",
                                itemPath, fullPath);
            }
            final DataType dataType = loadDataType(currentObject);
            if(dataType == null)
            {
                localized = isTypeLocalized(property);
                if(localized)
                {
                    definedDataType = extractLocalizedType(property).map(this::loadDataTypeInternal).orElse(null);
                }
                else
                {
                    definedDataType = StringUtils.isNotBlank(property.getType()) ? loadDataTypeInternal(property.getType()) : null;
                }
                labelText = configurableFlowLabelService.getQualifierLabel(prefix, fullPath);
                if(StringUtils.isBlank(labelText))
                {
                    labelText = property.getDescription();
                }
            }
            else
            {
                final DataAttribute attributeData = dataType.getAttribute(attributeQualifier);
                if(attributeData == null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Cannot load dataAttribute for '{}'", attributeQualifier);
                    }
                    return prepareErrorRow(widgetInstanceManager.getLabel("qualifier.could.not.be.resolved.qualifier", new Object[]
                                    {attributeQualifier}));
                }
                else
                {
                    attributeDataDescription = attributeData.getDescription(cockpitLocaleService.getCurrentLocale());
                    localized = attributeData.isLocalized();
                    readOnly = readOnly || !isAttributeWritable(attributeData, attributeQualifier, dataType.getCode());
                    valueType = resolveType(attributeData);
                    canRead = permissionFacade.canReadProperty(dataType.getCode(), attributeQualifier);
                    definedDataType = attributeData.getValueType();
                    optionalSetFlag.setTrue();
                    optionalValue.setValue(!attributeData.isMandatory());
                }
            }
        }
        else
        {
            definedDataType = null;
            currentObject = null;
        }
        final Div propertyDiv = new Div();
        propertyDiv.setSclass(WIZARD_STYLE_PROPERTY);
        if(StringUtils.isNotBlank(property.getLabel()))
        {
            labelText = Optional.ofNullable(widgetInstanceManager.getLabel(property.getLabel()))
                            .orElse(Labels.getLabel(property.getLabel()));
        }
        if(labelText == null)
        {
            labelText = configurableFlowLabelService.getQualifierLabel(itemPath, attributeQualifier);
        }
        labelText += ":";
        if(!localized)
        {
            final Label label = new Label(labelText);
            UITools.modifySClass(label, WIZARD_STYLE_PROPERTY_LABEL_MANDATORY, Optional.ofNullable(currentObject)
                            .map(obj -> typeFacade.getAttribute(obj, attributeQualifier)).map(DataAttribute::isMandatory).orElse(false));
            UITools.addSClass(label, WIZARD_STYLE_PROPERTY_LABEL);
            propertyDiv.appendChild(label);
            attributeDescriptionIconRenderer.renderDescriptionIcon(getPropertyDescription(attributeDataDescription, property),
                            propertyDiv);
        }
        if(!canRead)
        {
            final Label label = new Label(getLabelService().getAccessDeniedLabel(currentObject));
            propertyDiv.appendChild(label);
        }
        else
        {
            final Boolean nocDisabledEditor = isNestedObjectCreationDisabled(property.getEditorParameter());
            final EditorBuilder editorBuilder = new EditorBuilder(widgetInstanceManager)
                            .when(currentObject != null && definedDataType != null && StringUtils.isNotBlank(itemPath))
                            .then(builder -> builder.configure(itemPath, attributeQualifier))
                            .otherwiseApply(builder -> builder.attach(fullPath)).when(definedDataType != null)
                            .thenApply(builder -> builder.configure(definedDataType))
                            .addParameters(property.getEditorParameter().stream(), Parameter::getName, Parameter::getValue).setLabel(labelText)
                            .setDescription(getPropertyDescription(attributeDataDescription, property)).setReadOnly(readOnly)
                            .setValueType(valueType).useEditor(property.getEditor()).setMultilingual(localized, currentObject)
                            .setValueCreationEnabled(
                                            nocDisabledEditor == null ? !isNestedObjectCreationDisabled() : !nocDisabledEditor.booleanValue())
                            .apply(editor -> editor.addEventListener(Editor.ON_VALUE_CHANGED, editorChangeListener)).apply(editor -> {
                                if(optionalSetFlag.isTrue())
                                {
                                    editor.setOptional(optionalValue.getValue());
                                }
                            }).apply(editor -> editor.addEventListener(EditorValidation.ON_VALIDATION_CHANGED, e -> {
                                final ValidationResult validationResult = (ValidationResult)e.getData();
                                final String validationSclass = validationRenderer
                                                .getSeverityStyleClass(validationResult.getHighestNotConfirmedSeverity());
                                validationRenderer.cleanAllValidationCss(propertyDiv);
                                if(StringUtils.isNotBlank(validationSclass))
                                {
                                    UITools.modifySClass(propertyDiv, validationSclass, true);
                                }
                            }));
            if(StringUtils.isNotBlank(itemPath) && StringUtils.isNotBlank(attributeQualifier) && definedDataType != null
                            && BooleanUtils.isNotFalse(property.isValidate()))
            {
                editorBuilder.enableValidation(itemPath, attributeQualifier, validatableContainer);
            }
            editorBuilder.getEditor().setAttribute("parentObject", currentObject);
            final Editor editor = buildEditor(editorBuilder, widgetInstanceManager);
            propertyDiv.appendChild(editor);
        }
        return propertyDiv;
    }


    protected Editor buildEditor(final EditorBuilder editorBuilder, final WidgetInstanceManager widgetInstanceManager)
    {
        final Editor editor = editorBuilder.build();
        YTestTools.modifyYTestId(editor, WIZARD_PREFIX + editor.getProperty());
        return editor;
    }


    protected boolean isTypeLocalized(final PropertyType property)
    {
        final String propertyType = property.getType();
        if(propertyType == null)
        {
            return false;
        }
        else
        {
            return EditorUtils.getLocalizedEditorPattern().matcher(propertyType).matches();
        }
    }


    protected Boolean isNestedObjectCreationDisabled(final List<Parameter> editorParameter)
    {
        for(final Parameter parameter : editorParameter)
        {
            if(IS_NESTED_OBJECT_CREATION_DISABLED_SETTING.equalsIgnoreCase(parameter.getName()))
            {
                return Boolean.valueOf(parameter.getValue());
            }
        }
        return Boolean.valueOf(isNestedObjectCreationDisabled());
    }


    protected Component prepareErrorRow(final String label)
    {
        final Div propertyDiv = new Div();
        propertyDiv.setSclass(WIZARD_STYLE_PROPERTY);
        final Div readonlyDiv = new Div();
        readonlyDiv.setSclass(WIZARD_STYLE_READ_ONLY_VALUE);
        propertyDiv.appendChild(readonlyDiv);
        final Label notFoundLabel = new Label(label);
        notFoundLabel.setSclass("ye-qualifier-not-found-error");
        readonlyDiv.appendChild(notFoundLabel);
        YTestTools.modifyYTestId(readonlyDiv, "qualifier-not-found");
        return propertyDiv;
    }


    protected boolean isNestedObjectCreationDisabled()
    {
        return widgetInstanceManager.getWidgetSettings().getBoolean(IS_NESTED_OBJECT_CREATION_DISABLED_SETTING);
    }


    protected boolean isAttributeWritable(final DataAttribute attributeData, final String normalizedQualifier,
                    final String normalizedTypeQualifier)
    {
        final boolean typeSystemWritable = attributeData.isWritable() || attributeData.isWritableOnCreation();
        final boolean attributeWritable = (typeSystemWritable
                        && permissionFacade.canChangeProperty(normalizedTypeQualifier, normalizedQualifier));
        if(!attributeWritable && attributeData.isMandatory() && attributeData.getDefaultValue() == null)
        {
            final String source = getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager());
            getNotificationService().notifyUser(source, NotificationEventTypes.EVENT_TYPE_PERMISSIONS,
                            Level.WARNING, attributeData);
        }
        return attributeWritable;
    }


    protected String resolveType(final DataAttribute attributeData)
    {
        return EditorUtils.getEditorType(attributeData);
    }


    protected String normalizeQualifier(final String propertyQualifier)
    {
        String result = propertyQualifier;
        final int index = propertyQualifier.indexOf(SEPARATOR);
        if(index > 0)
        {
            result = propertyQualifier.substring(index + 1);
        }
        return result;
    }


    protected String normalizePropertyQualifier(final String prefix, final PropertyType property)
    {
        return (StringUtils.isNotBlank(prefix) ? (prefix + SEPARATOR) : StringUtils.EMPTY) + property.getQualifier();
    }


    private DataType loadDataTypeInternal(final String typeCode)
    {
        DataType result = null;
        try
        {
            result = typeFacade.load(typeCode);
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Type %s not found!", typeCode), e);
            }
        }
        return result;
    }


    protected DataType loadDataType(final Object currentObject)
    {
        DataType result = null;
        if(currentObject == null)
        {
            LOG.warn("Can not load data type, since passed object is null. Returning null.");
        }
        else
        {
            final String type = typeFacade.getType(currentObject);
            result = loadDataTypeInternal(type);
        }
        return result;
    }


    protected String extractTypeQualifier(final String qualifier)
    {
        String result = StringUtils.EMPTY;
        final int index = qualifier.indexOf(SEPARATOR);
        if(index > 0)
        {
            result = qualifier.substring(0, index);
        }
        return result;
    }


    protected HtmlBasedComponent createBreadcrumbEntry(final StepType step)
    {
        final Div ret = new Div();
        final String label = step.getLabel();
        final String sublabel = step.getSublabel();
        final Div labelCnt = new Div();
        ret.appendChild(labelCnt);
        labelCnt.appendChild(new Label(configurableFlowLabelService.getLabel(label)));
        labelCnt.setSclass(WIZARD_STYLE_BR_LABEL);
        YTestTools.modifyYTestId(labelCnt, WIZARD_PREFIX + step.getId());
        final Div sublabelCnt = new Div();
        ret.appendChild(sublabelCnt);
        sublabelCnt.appendChild(new Label(configurableFlowLabelService.getLabel(sublabel)));
        sublabelCnt.setSclass(WIZARD_STYLE_BR_S_LABEL);
        YTestTools.modifyYTestId(sublabelCnt, WIZARD_PREFIX + step.getId());
        final Div spacerDiv = new Div();
        spacerDiv.setSclass(WIZARD_STYLE_BR_SP);
        ret.appendChild(spacerDiv);
        final Div spacerArrowDiv = new Div();
        spacerArrowDiv.setSclass(WIZARD_STYLE_BR_SP_ARROW);
        ret.appendChild(spacerArrowDiv);
        final Div arrowDiv = new Div();
        arrowDiv.setSclass(WIZARD_STYLE_BR_ARROW);
        ret.appendChild(arrowDiv);
        ret.setAttribute(STEP_INITIALIZED, Boolean.TRUE);
        ret.setAttribute(STEP_ID_ATTRIBUTE, step.getId());
        return ret;
    }


    protected void renderBreadcrumb(final StepType currentStep, final List<StepType> stepList)
    {
        breadcrumbDiv.getChildren().clear();
        if(currentStep.getHideBreadcrumbs())
        {
            return;
        }
        final int nrSteps = stepList.size();
        final int currentStepIndex = stepList.indexOf(currentStep);
        int index = 0;
        for(final StepType step : stepList)
        {
            final String stepID = step.getId();
            final HtmlBasedComponent entry = createBreadcrumbEntry(step);
            breadcrumbDiv.appendChild(entry);
            final String prefix = WIZARD_STYLE_BR_PREFIX;
            if(index < currentStepIndex)
            {
                final StepType targetStepOfFlow = getStep(stepID);
                final StepType currentStepOfFlow = getWidgetInstanceManager().getModel()
                                .getValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP, StepType.class);
                entry.setSclass(prefix + " " + prefix);
                if(isStepChangePossible(currentStepOfFlow, targetStepOfFlow))
                {
                    entry.setSclass(prefix + " " + prefix + WIZARD_STYLE_BEFORE);
                    entry.addEventListener(Events.ON_CLICK, event -> {
                        getWidgetInstanceManager().getModel().setValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP,
                                        targetStepOfFlow);
                        renderCurrentStep(targetStepOfFlow);
                        entry.setAttribute(STEP_INITIALIZED, Boolean.TRUE);
                        entry.setAttribute(STEP_ID_ATTRIBUTE, targetStepOfFlow.getId());
                    });
                }
            }
            else if(index == currentStepIndex)
            {
                entry.setSclass(prefix + " " + prefix + WIZARD_STYLE_ACTIVE);
            }
            else
            {
                entry.setSclass(prefix + " " + prefix + WIZARD_STYLE_AFTER);
            }
            entry.setStyle(WIZARD_STYLE_Z_INDEX + (nrSteps - index));
            index++;
        }
    }


    protected boolean isStepChangePossible(final StepType currentStep, final StepType targetStep)
    {
        final List<StepType> steps = getAllSteps();
        int indexOfStepToCheck = steps.indexOf(currentStep);
        final int indexOfTargetStep = steps.indexOf(targetStep);
        while(indexOfTargetStep < indexOfStepToCheck)
        {
            final StepType stepToCheck = steps.get(indexOfStepToCheck);
            final BackType backButtonConfig = stepToCheck.getNavigation() == null ? null : stepToCheck.getNavigation().getBack();
            if(!isBackButtonEnabled(backButtonConfig))
            {
                return false;
            }
            indexOfStepToCheck--;
        }
        return true;
    }


    private boolean isBackButtonEnabled(final BackType backButtonConfig)
    {
        final String visibleExpr = getVisibleExpression(backButtonConfig);
        if(visibleExpr != null)
        {
            return cfExpressions
                            .evaluatedExpression2Boolean(cfExpressions.evalExpression(getWidgetInstanceManager().getModel(), visibleExpr));
        }
        return true;
    }


    public void renderCurrentStep(final StepType currentStep)
    {
        clearMessages();
        if(showBreadcrumb)
        {
            renderBreadcrumb(currentStep, allSteps);
        }
        unregisterComponentsFromVisitor();
        contentDiv.getChildren().clear();
        final Vlayout vlayout = new Vlayout();
        vlayout.setSclass(WIZARD_STYLE_LAYOUT);
        vlayout.setHeight(HUNDRED_PERCENT_S);
        vlayout.setSpacing(WIZARD_STYLE_NONE);
        contentDiv.appendChild(vlayout);
        final Div stepContent = new Div();
        stepContent.setSclass(WIZARD_STYLE_CONTENT);
        stepContent.setVflex("1");
        stepContent.setHflex("1");
        vlayout.appendChild(stepContent);
        final Optional<InfoType> optionalInfo = Optional.ofNullable(currentStep.getInfo());
        optionalInfo.ifPresent(info -> {
            final Div infoDiv = new Div();
            infoDiv.setSclass(WIZARD_STYLE_INFO);
            final String labelKey = StringUtils.isNotBlank(info.getLabel()) ? info.getLabel() : info.getValue();
            final String infoLabel = configurableFlowLabelService.getLabel(labelKey);
            final Html html = new Html(infoLabel);
            infoDiv.appendChild(html);
            final String size = info.getSize();
            if(WIZARD_STYLE_RIGHT.equals(info.getPosition()) || WIZARD_STYLE_LEFT.equals(info.getPosition()))
            {
                final Hlayout hlayout = new Hlayout();
                hlayout.setWidth(HUNDRED_PERCENT_S);
                hlayout.setHeight(HUNDRED_PERCENT_S);
                hlayout.setSclass(WIZARD_STYLE_LAYOUT);
                vlayout.appendChild(hlayout);
                infoDiv.setHeight(HUNDRED_PERCENT_S);
                if(StringUtils.isNotBlank(size))
                {
                    if(size.contains("%"))
                    {
                        final int percent = Integer.parseInt(size.split("%")[0]);
                        infoDiv.setHflex(Integer.toString(percent));
                        stepContent.setHflex(Integer.toString(HUNDRED_PERCENT - percent));
                    }
                    else
                    {
                        infoDiv.setWidth(size);
                    }
                }
                if(WIZARD_STYLE_RIGHT.equals(info.getPosition()))
                {
                    hlayout.appendChild(stepContent);
                    hlayout.appendChild(infoDiv);
                }
                else
                {
                    hlayout.appendChild(infoDiv);
                    hlayout.appendChild(stepContent);
                }
            }
            else
            {
                if(StringUtils.isNotBlank(size))
                {
                    if(size.contains("%"))
                    {
                        final int percent = Integer.parseInt(size.split("%")[0]);
                        infoDiv.setVflex(Integer.toString(percent));
                        stepContent.setVflex(Integer.toString(HUNDRED_PERCENT - percent));
                    }
                    else
                    {
                        infoDiv.setHeight(size);
                    }
                }
                if(WIZARD_STYLE_TOP.equals(info.getPosition()))
                {
                    vlayout.insertBefore(infoDiv, stepContent);
                }
                else
                {
                    vlayout.appendChild(infoDiv);
                }
            }
        });
        final ContentType content = currentStep.getContent();
        if(content != null)
        {
            final List<ContentType> cols = content.getColumn();
            if(CollectionUtils.isNotEmpty(cols))
            {
                if(CollectionUtils.isNotEmpty(content.getPropertyOrPropertyListOrCustomView()))
                {
                    LOG.error("Error: you can not mix content and columns.");
                    return;
                }
                final Hbox hbox = new Hbox();
                hbox.setWidth(HUNDRED_PERCENT_S);
                stepContent.appendChild(hbox);
                for(final ContentType column : cols)
                {
                    final Div colContent = new Div();
                    colContent.setSclass(WIZARD_STYLE_COLUMN_CONTENT);
                    hbox.appendChild(colContent);
                    renderContent(colContent, column);
                }
            }
            else
            {
                renderContent(stepContent, content);
            }
        }
        navigationDiv = new Div();
        navigationDiv.setSclass(WIZARD_STYLE_NAVI);
        vlayout.appendChild(navigationDiv);
        renderNavigation(currentStep);
        refreshView();
        applyComponentsVisitor();
    }


    protected void applyComponentsVisitor()
    {
        final String typeCode = getTypeCodeFromWizardCtx();
        if(StringUtils.isNotEmpty(typeCode))
        {
            if(componentsVisitor == null)
            {
                componentsVisitor = componentsVisitorFactory.createVisitor(typeCode, widgetInstanceManager);
            }
            componentsVisitor.register(contentDiv);
        }
    }


    protected void unregisterComponentsFromVisitor()
    {
        if(componentsVisitor != null)
        {
            componentsVisitor.cleanUp();
        }
    }


    public void cleanUpRendererObservers()
    {
        unregisterComponentsFromVisitor();
    }


    protected String getTypeCodeFromWizardCtx()
    {
        final Map currentContext = widgetInstanceManager.getModel().getValue(ConfigurableFlowController.WIZARD_CURRENT_CONTEXT,
                        Map.class);
        if(currentContext != null)
        {
            return (String)currentContext.get(ConfigurableFlowContextParameterNames.TYPE_CODE.getName());
        }
        return null;
    }


    protected void renderContent(final Component parent, final ContentType content)
    {
        final List propertyOrPropertyListOrCustomView = content.getPropertyOrPropertyListOrCustomView();
        if(propertyOrPropertyListOrCustomView == null)
        {
            return;
        }
        for(final Object contentElement : propertyOrPropertyListOrCustomView)
        {
            renderContent(parent, contentElement);
        }
    }


    private void renderContent(final Component parent, final Object contentElement)
    {
        if(contentElement instanceof ViewType)
        {
            renderCustom(parent, (ViewType)contentElement);
        }
        else if(contentElement instanceof PropertyType)
        {
            final Div propDiv = new Div();
            propDiv.appendChild(createPropertyLine(StringUtils.EMPTY, (PropertyType)contentElement));
            parent.appendChild(propDiv);
        }
        else if(contentElement instanceof PropertyListType)
        {
            final PropertyListType propList = (PropertyListType)contentElement;
            final Boolean readonly = propList.isReadonly();
            final String root = propList.getRoot();
            for(final PropertyType prop : propList.getProperty())
            {
                if(prop.isReadonly() == null)
                {
                    prop.setReadonly(readonly);
                }
                final Div propDiv = new Div();
                propDiv.appendChild(createPropertyLine(root, prop));
                parent.appendChild(propDiv);
            }
        }
    }


    private void renderCustom(final Component parent, final ViewType customView)
    {
        parent.addEventListener(Editor.ON_VALUE_CHANGED, editorChangeListener);
        if(extractRenderer(customView.getContent()).isPresent())
        {
            renderByRendererBean(parent, customView);
        }
        else
        {
            if(ZUL.equals(customView.getLang()))
            {
                if(StringUtils.isNotBlank(customView.getSrc()))
                {
                    renderByZulFile(parent, customView);
                }
                else if(StringUtils.isNotBlank(extractTextContent(customView.getContent())))
                {
                    // render by zul code
                    Executions.createComponentsDirectly(extractTextContent(customView.getContent()), null, parent, null);
                }
            }
        }
    }


    private void renderByRendererBean(final Component parent, final ViewType customView)
    {
        final Optional<Renderer> customRenderer = extractRenderer(customView.getContent());
        customRenderer.ifPresent(renderer -> {
            if(StringUtils.isNotBlank(renderer.getSpringBean()))
            {
                final Object bean = BackofficeSpringUtil.getBean(renderer.getSpringBean());
                final String typeCode = getTypeCodeFromWizardCtx();
                final DataType dataType = StringUtils.isNotEmpty(typeCode) ? loadDataTypeInternal(typeCode) : null;
                if(bean instanceof ValidationAwareCustomViewRenderer)
                {
                    final ValidationAwareCustomViewRenderer widgetComponentRenderer = (ValidationAwareCustomViewRenderer)bean;
                    widgetComponentRenderer.render(parent, validatableContainer, customView,
                                    extractParameters(renderer.getParameter()), dataType, widgetInstanceManager);
                }
                else if(bean instanceof WidgetComponentRenderer)
                {
                    final WidgetComponentRenderer widgetComponentRenderer = (WidgetComponentRenderer)bean;
                    widgetComponentRenderer.render(parent, customView, extractParameters(renderer.getParameter()), dataType,
                                    widgetInstanceManager);
                }
                else
                {
                    LOG.warn("Could not load renderer bean with id '{}'.", renderer.getSpringBean());
                }
            }
        });
    }


    private void renderNavigation(final StepType currentStep)
    {
        final Div leftPack = new Div();
        leftPack.setSclass(ConfigurableFlowRenderer.WIZARD_STYLE_LEFT_PACK);
        final Div rightPack = new Div();
        rightPack.setSclass(ConfigurableFlowRenderer.WIZARD_STYLE_RIGHT_PACK);
        navigationDiv.getChildren().clear();
        navigationDiv.appendChild(leftPack);
        navigationDiv.appendChild(rightPack);
        for(final EventListener<? extends Event> listener : navigationDiv.getEventListeners(ON_FORCE_UPDATE))
        {
            navigationDiv.removeEventListener(ON_FORCE_UPDATE, listener);
        }
        if(currentStep.getNavigation() == null)
        {
            return;
        }
        final Map<String, Button> validationPopupAnchors = Maps.newHashMap();
        final Button cancelButton = currentStep.getNavigation().getCancel() != null ? createAndAppendButton(rightPack,
                        "flow.cancel", currentStep, currentStep.getNavigation().getCancel(), ConfigurableFlowDefinitions.WIZARD_CANCEL)
                        : null;
        final Button nextButton = currentStep.getNavigation().getNext() != null
                        ? createNextButton(currentStep, rightPack, validationPopupAnchors)
                        : null;
        final Button doneButton = currentStep.getNavigation().getDone() != null
                        ? createButton(currentStep, rightPack, validationPopupAnchors)
                        : null;
        final Button backButton = currentStep.getNavigation().getBack() != null ? createAndAppendButton(leftPack, "flow.back",
                        currentStep, currentStep.getNavigation().getBack(), ConfigurableFlowDefinitions.WIZARD_BACK) : null;
        final Button customButton = currentStep.getNavigation().getCustom() != null
                        ? createCustomButton(currentStep, leftPack, rightPack, validationPopupAnchors)
                        : null;
        if(!validationPopupAnchors.isEmpty())
        {
            validationResultsPopup.updateValidationPopupAnchor(validationPopupAnchors);
        }
        else
        {
            validationResultsPopup.updateValidationPopupAnchor(rightPack);
        }
        adjustClosableModalWindow(currentStep);
        navigationDiv.addEventListener(ON_FORCE_UPDATE, event -> {
            refreshButtonState(nextButton, currentStep.getNavigation().getNext());
            refreshButtonState(doneButton, currentStep.getNavigation().getDone());
            refreshButtonState(backButton, currentStep.getNavigation().getBack());
            refreshButtonState(customButton, currentStep.getNavigation().getCustom());
            refreshButtonState(cancelButton, currentStep.getNavigation().getCancel());
        });
    }


    private Button createCustomButton(final StepType currentStep, final Div leftPack, final Div rightPack,
                    final Map<String, Button> validationPopupAnchors)
    {
        final CustomType customType = currentStep.getNavigation().getCustom();
        return updateCustomNavigation(currentStep, leftPack, rightPack, validationPopupAnchors, customType);
    }


    private Button createButton(final StepType currentStep, final Div rightPack,
                    final Map<String, Button> validationPopupAnchors)
    {
        final Button doneButton = createAndAppendButton(rightPack, "flow.done", currentStep, currentStep.getNavigation().getDone(),
                        ConfigurableFlowDefinitions.WIZARD_DONE);
        doneButton.setSclass(ConfigurableFlowRenderer.WIZARD_STYLE_BTN_BLUE);
        toggleButtonActiveState(doneButton, currentStep.getNavigation().getDone());
        validationPopupAnchors.put(ConfigurableFlowDefinitions.WIZARD_DONE, doneButton);
        return doneButton;
    }


    private Button createNextButton(final StepType currentStep, final Div rightPack,
                    final Map<String, Button> validationPopupAnchors)
    {
        final Button nextButton = createAndAppendButton(rightPack, "flow.next", currentStep, currentStep.getNavigation().getNext(),
                        ConfigurableFlowDefinitions.WIZARD_NEXT);
        toggleButtonActiveState(nextButton, currentStep.getNavigation().getNext());
        validationPopupAnchors.put(ConfigurableFlowDefinitions.WIZARD_NEXT, nextButton);
        return nextButton;
    }


    void refreshButtonState(final Button button, final AbstractActionType action)
    {
        if(button != null && action != null)
        {
            applyVisibilityRestriction(button, action);
            toggleButtonActiveState(button, action);
        }
    }


    /**
     * @deprecated since 2005
     * @see #updateNavigation()
     */
    @Deprecated(since = "2005", forRemoval = true)
    public void updateNavigation(final StepType currentStep)
    {
        updateNavigation();
    }


    public void updateNavigation()
    {
        if(navigationDiv == null)
        {
            throw new IllegalStateException("Cannot update navigation on non-existing navigation bar");
        }
        Events.sendEvent(ON_FORCE_UPDATE, navigationDiv, null);
    }


    private Button updateCustomNavigation(final StepType currentStep, final Div leftPack, final Div rightPack,
                    final Map<String, Button> validationPopupAnchors, final CustomType customType)
    {
        String buttonLabel = Labels.getLabel(customType.getLabel());
        if(StringUtils.isEmpty(buttonLabel))
        {
            buttonLabel = customType.getLabel();
        }
        final Button customButton = new Button(buttonLabel);
        applyVisibilityRestriction(customButton, customType);
        toggleButtonActiveState(customButton, customType);
        if(WIZARD_STYLE_LEFT.equals(customType.getAlign()))
        {
            wrapChildAndAppend(leftPack, customButton);
        }
        else
        {
            wrapChildAndAppend(rightPack, customButton);
        }
        final TransitionListener transitionListener = getTransitionListenerFactory().create(currentStep,
                        ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST);
        customButton.addEventListener(Events.ON_CLICK, event -> {
            try
            {
                validationResultsPopup.updateValidationPopupAnchor((Button)event.getTarget(),
                                ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST);
                transitionListener.onEvent(event);
            }
            catch(final RuntimeException exc)
            {
                LOG.warn("Unexpected error during custom transition", exc);
            }
        });
        YTestTools.modifyYTestId(customButton, WIZARD_PREFIX + customType.getLabel());
        if(BooleanUtils.isNotFalse(customType.getPrimary()))
        {
            UITools.addSClass(customButton, ConfigurableFlowRenderer.WIZARD_STYLE_BTN_BLUE);
        }
        validationPopupAnchors.put(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST, customButton);
        return customButton;
    }


    private void adjustClosableModalWindow(final StepType currentStep)
    {
        CockpitComponentsUtils.findClosestComponent(getWidgetInstanceManager().getWidgetslot(), Window.class,
                        ConfigurableFlowController.SCLASS_YW_MODAL_CONFIGURABLEFLOW).ifPresent((final Window window) -> {
            final CancelType cancel = currentStep.getNavigation().getCancel();
            final boolean closeable = cancel != null;
            window.setClosable(closeable);
        });
    }


    /**
     * Calls custom step handler.
     *
     * @param currentStep
     *           current step
     * @param customType
     *           custom type navigation
     * @param composedHandlers
     *           collection of {@link ComposedHandlerType} defined for this flow
     */
    public void doCustom(final StepType currentStep, final CustomType customType,
                    final Map<String, ComposedHandlerType> composedHandlers)
    {
        if(currentStep == null || customType == null)
        {
            return;
        }
        final ComposedHandlerType composedHandler = composedHandlers.get(customType.getComposedHandler());
        final String handlerBeanId = findHandlerBean(composedHandler, customType);
        if(StringUtils.isBlank(handlerBeanId))
        {
            return;
        }
        final FlowActionHandler wizardActionHandler = BackofficeSpringUtil.getBean(handlerBeanId, FlowActionHandler.class);
        final FlowActionHandlerAdapter flowActionHandlerAdapter = createActionHandlerAdapter(currentStep,
                        new Event(Events.ON_CLICK));
        if(wizardActionHandler != null)
        {
            try
            {
                final Map<String, String> actionParameters = extractParameters(customType.getParameter());
                if(composedHandler != null)
                {
                    final Map<String, String> additionalParameters = composedHandler.getAdditionalParams().stream()
                                    .collect(Collectors.toMap(AdditionalParamType::getKey, AdditionalParamType::getValue));
                    actionParameters.putAll(additionalParameters);
                }
                wizardActionHandler.perform(customType, flowActionHandlerAdapter, actionParameters);
            }
            catch(final RuntimeException exc)
            {
                getNotificationService().notifyUser(widgetInstanceManager, NotificationEventTypes.EVENT_TYPE_GENERAL,
                                Level.FAILURE, exc);
                throw exc;
            }
        }
        else
        {
            LOG.warn("Given flow action handler bean was not found: {}", handlerBeanId);
        }
    }


    protected String findHandlerBean(final ComposedHandlerType composedHandler, final CustomType customType)
    {
        final boolean hasComposedHandlerBean = composedHandler != null && StringUtils.isNotBlank(composedHandler.getHandlerBean());
        return hasComposedHandlerBean ? composedHandler.getHandlerBean() : customType.getHandler();
    }


    /**
     * Calls custom step handler.
     *
     * @param currentStep
     *           current step
     * @param customType
     *           custom type navigation
     */
    public void doCustom(final StepType currentStep, final CustomType customType)
    {
        doCustom(currentStep, customType, Collections.emptyMap());
    }


    protected Button createAndAppendButton(final Component buttonsContainer, final String labelKey, final StepType currentStep,
                    final AbstractActionType actionType, final String actionId)
    {
        final Button button = new Button(widgetInstanceManager.getLabel(labelKey));
        applyVisibilityRestriction(button, actionType);
        wrapChildAndAppend(buttonsContainer, button);
        YTestTools.modifyYTestId(button, WIZARD_PREFIX + actionId);
        final TransitionListener transitionListener = transitionListenerFactory.create(currentStep, actionId);
        button.addEventListener(Events.ON_CLICK, event -> {
            validationResultsPopup.updateValidationPopupAnchor((Button)event.getTarget(), actionId);
            transitionListener.onEvent(event);
        });
        return button;
    }


    protected void wrapChildAndAppend(final Component parent, final Component child)
    {
        final Div childWrapper = new Div();
        childWrapper.setSclass(SCLASS_COMPONENT_WRAPPER);
        childWrapper.appendChild(child);
        parent.appendChild(childWrapper);
    }


    protected FlowActionHandlerAdapter createActionHandlerAdapter(final StepType currentStep, final Event event)
    {
        return new FlowActionHandlerAdapter(this.widgetInstanceManager)
        {
            @Override
            public void cancel()
            {
                transitionListenerFactory.create(currentStep, ConfigurableFlowDefinitions.WIZARD_CANCEL).onEvent(event);
            }


            @Override
            public void done()
            {
                transitionListenerFactory.create(currentStep, ConfigurableFlowDefinitions.WIZARD_DONE).onEvent(event);
            }


            @Override
            public void back()
            {
                transitionListenerFactory.create(currentStep, ConfigurableFlowDefinitions.WIZARD_BACK).onEvent(event);
            }


            @Override
            public void next()
            {
                transitionListenerFactory.create(currentStep, ConfigurableFlowDefinitions.WIZARD_NEXT).onEvent(event);
            }


            @Override
            public void custom()
            {
                transitionListenerFactory.create(currentStep, ConfigurableFlowDefinitions.WIZARD_CUSTOM).onEvent(event);
            }
        };
    }


    protected void toggleButtonActiveState(final Button button, final AbstractActionType actionType)
    {
        final String visibleExpr = actionType.getVisible();
        if(visibleExpr != null)
        {
            button.setDisabled(!cfExpressions
                            .evaluatedExpression2Boolean(cfExpressions.evalExpression(widgetInstanceManager.getModel(), visibleExpr)));
        }
    }


    protected void applyVisibilityRestriction(final Component component, final AbstractActionType actionType)
    {
        final String visibleExpr = actionType.getVisible();
        if(visibleExpr != null)
        {
            final boolean visibilityRestriction = cfExpressions
                            .evaluatedExpression2Boolean(cfExpressions.evalExpression(widgetInstanceManager.getModel(), visibleExpr));
            component.setVisible(visibilityRestriction);
        }
    }


    public void clearMessages()
    {
        msgAreaDiv.getChildren().clear();
    }


    public void refreshView()
    {
        refreshView(contentDiv);
        validationResultsPopup.recalculatePosition();
    }


    protected void refreshView(final Component comp)
    {
        CockpitComponentsUtils.findClosestComponent(comp, Window.class, ConfigurableFlowController.SCLASS_YW_MODAL_CONFIGURABLEFLOW)
                        .ifPresent(Window::invalidate);
    }


    protected StepType getStep(final String id)
    {
        for(final StepType step : getAllSteps())
        {
            if(step.getId().equals(id))
            {
                return step;
            }
        }
        return null;
    }


    protected List<StepType> getAllSteps()
    {
        return allSteps;
    }


    public void setAllSteps(final List<StepType> allSteps)
    {
        this.allSteps = allSteps;
    }


    protected String getPropertyDescription(final String attributeDataDescription, final PropertyType propertyType)
    {
        if(StringUtils.isNotBlank(propertyType.getDescription()))
        {
            return Labels.getLabel(propertyType.getDescription());
        }
        else if(propertyType.getDescription() != null)
        {
            return StringUtils.EMPTY;
        }
        else
        {
            return attributeDataDescription;
        }
    }


    public Div getContentDiv()
    {
        return contentDiv;
    }


    public void setContentDiv(final Div contentDiv)
    {
        this.contentDiv = contentDiv;
    }


    public Div getBreadcrumbDiv()
    {
        return breadcrumbDiv;
    }


    public void setBreadcrumbDiv(final Div breadcrumbDiv)
    {
        this.breadcrumbDiv = breadcrumbDiv;
    }


    public Div getMsgAreaDiv()
    {
        return msgAreaDiv;
    }


    public void setMsgAreaDiv(final Div msgAreaDiv)
    {
        this.msgAreaDiv = msgAreaDiv;
    }


    public Div getNavigationDiv()
    {
        return navigationDiv;
    }


    public WidgetInstanceManager getWidgetInstanceManager()
    {
        return widgetInstanceManager;
    }


    public void setWidgetInstanceManager(final WidgetInstanceManager widgetInstanceManager)
    {
        this.widgetInstanceManager = widgetInstanceManager;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Autowired
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public TransitionListenerFactory getTransitionListenerFactory()
    {
        return transitionListenerFactory;
    }


    public void setTransitionListenerFactory(final TransitionListenerFactory transitionListenerFactory)
    {
        this.transitionListenerFactory = transitionListenerFactory;
    }


    public ConfigurableFlowLabelService getConfigurableFlowLabelService()
    {
        return configurableFlowLabelService;
    }


    public void setConfigurableFlowLabelService(final ConfigurableFlowLabelService configurableFlowLabelService)
    {
        this.configurableFlowLabelService = configurableFlowLabelService;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Autowired
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public AttributeDescriptionIconRenderer getAttributeDescriptionIconRenderer()
    {
        return attributeDescriptionIconRenderer;
    }


    public void setAttributeDescriptionIconRenderer(final AttributeDescriptionIconRenderer attributeDescriptionIconRenderer)
    {
        this.attributeDescriptionIconRenderer = attributeDescriptionIconRenderer;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Autowired
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    @Autowired
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    @Required
    public void setComponentsVisitorFactory(final ComponentsVisitorFactory componentsVisitorFactory)
    {
        this.componentsVisitorFactory = componentsVisitorFactory;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public void setValidationRenderer(final ValidationRenderer validationRenderer)
    {
        this.validationRenderer = validationRenderer;
    }


    public void setValidationResultsPopup(final ConfigurableFlowValidationResultsPopup validationResultsPopup)
    {
        this.validationResultsPopup = validationResultsPopup;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public ValidationHandler getValidationHandler()
    {
        return validationHandler;
    }


    public void setValidationHandler(final ValidationHandler validationHandler)
    {
        this.validationHandler = validationHandler;
    }


    public boolean isShowBreadcrumb()
    {
        return showBreadcrumb;
    }


    public void setShowBreadcrumb(final boolean showBreadcrumb)
    {
        this.showBreadcrumb = showBreadcrumb;
    }
}
