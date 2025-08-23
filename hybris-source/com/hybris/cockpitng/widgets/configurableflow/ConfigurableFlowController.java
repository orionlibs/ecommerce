/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

import static com.hybris.cockpitng.util.notifications.event.NotificationEvent.Level;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.config.jaxb.wizard.AbstractActionType;
import com.hybris.cockpitng.config.jaxb.wizard.AssignType;
import com.hybris.cockpitng.config.jaxb.wizard.BackType;
import com.hybris.cockpitng.config.jaxb.wizard.CancelType;
import com.hybris.cockpitng.config.jaxb.wizard.ComposedHandlerType;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.config.jaxb.wizard.DoneType;
import com.hybris.cockpitng.config.jaxb.wizard.Flow;
import com.hybris.cockpitng.config.jaxb.wizard.IfType;
import com.hybris.cockpitng.config.jaxb.wizard.InitializeType;
import com.hybris.cockpitng.config.jaxb.wizard.NextType;
import com.hybris.cockpitng.config.jaxb.wizard.Parameter;
import com.hybris.cockpitng.config.jaxb.wizard.PrepareType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyListType;
import com.hybris.cockpitng.config.jaxb.wizard.PropertyType;
import com.hybris.cockpitng.config.jaxb.wizard.Renderer;
import com.hybris.cockpitng.config.jaxb.wizard.Size;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.config.jaxb.wizard.SubflowType;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.renderers.attributedescription.AttributeDescriptionIconRenderer;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.CockpitComponentsUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.validation.LocalizationAwareValidationHandler;
import com.hybris.cockpitng.validation.LocalizedQualifier;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.validation.impl.DefaultValidationContext;
import com.hybris.cockpitng.validation.impl.ValidationInfoFactoryWithPrefix;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.configurableflow.delegate.ConfigurableFlowControllerPersistDelegate;
import com.hybris.cockpitng.widgets.configurableflow.delegate.DefaultConfigurableFlowControllerPersistDelegate;
import com.hybris.cockpitng.widgets.configurableflow.listener.EditorChangeListener;
import com.hybris.cockpitng.widgets.configurableflow.listener.TransitionListenerFactory;
import com.hybris.cockpitng.widgets.configurableflow.renderer.ConfigurableFlowRenderer;
import com.hybris.cockpitng.widgets.configurableflow.util.ConfigurableFlowExpressions;
import com.hybris.cockpitng.widgets.configurableflow.util.ConfigurableFlowLabelService;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidatable;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationRenderer;
import com.hybris.cockpitng.widgets.configurableflow.validation.ConfigurableFlowValidationResultsPopup;
import com.hybris.cockpitng.widgets.configurableflow.validation.LocalizedValidationAwareCustomViewRenderer;
import com.hybris.cockpitng.widgets.configurableflow.validation.ValidationAwareCustomViewRenderer;
import com.hybris.cockpitng.widgets.util.ReferenceModelProperties;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

/**
 * Default <code>ConfigurableFlowController</code> implementation.
 * There is returned a Map&lt;String, Object&gt; object in ConfigurableFlowResult.data on wizard output. The map contains all
 * persistence object candidates.
 */
public class ConfigurableFlowController extends DefaultWidgetController
{
    public static final String ATTRIBUTE_FLOW = "flow";
    public static final String WIZARD_CONTEXT = "ctx";
    public static final String WIZARD_CURRENT_CONTEXT = "currentContext";
    public static final String SOCKET_IN_CTX = "context";
    public static final String SOCKET_IN_CONTEXT_TYPE = "contextType";
    public static final String SOCKET_WIZARD_RESULT = "wizardResult";
    public static final String FUNCTION_NAME_EMPTY = "empty";
    public static final String FUNCTION_IS_BLANK = "isBlank";
    public static final String SCLASS_YW_MODAL_CONFIGURABLEFLOW = "yw-modal-configurableFlow";
    public static final String SCLASS_YW_MODAL_CONFIGURABLEFLOW_SMALL = SCLASS_YW_MODAL_CONFIGURABLEFLOW + "-small";
    public static final String SCLASS_YW_MODAL_CONFIGURABLEFLOW_MEDIUM = SCLASS_YW_MODAL_CONFIGURABLEFLOW + "-medium";
    public static final String SCLASS_YW_MODAL_CONFIGURABLEFLOW_LARGE = SCLASS_YW_MODAL_CONFIGURABLEFLOW + "-large";
    public static final String SETTING_CONFIGURABLE_FLOW_CONFIG_CTX = "configurableFlowConfigCtx";
    public static final String FLOW_CONFIGURATION = "_flowConfiguration";
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableFlowController.class);
    @WireVariable
    private transient CockpitConfigurationService cockpitConfigurationService;
    @WireVariable
    private transient ObjectFacade objectFacade;
    @WireVariable
    private transient FlowCancelActionHandler flowCancelActionHandler;
    /**
     * @deprecated since 1811, not used anymore in favour of {@link #localizationAwareValidationHandler}
     */
    @Deprecated(since = "1811", forRemoval = true)
    @WireVariable
    private transient ValidationHandler validationHandler;
    @WireVariable
    private transient LocalizationAwareValidationHandler localizationAwareValidationHandler;
    @WireVariable(value = "configurableFlowRenderer")
    private transient ConfigurableFlowRenderer renderer;
    private transient LabelService labelService;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    private transient CockpitUserService cockpitUserService;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient PermissionFacade permissionFacade;
    @WireVariable
    private transient AttributeDescriptionIconRenderer attributeDescriptionIconRenderer;
    @WireVariable("configurableFlowValidationRenderer")
    private transient ConfigurableFlowValidationRenderer validationRenderer;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient ConfigurableFlowControllerPersistDelegate configurableFlowPersistDelegate;
    @WireVariable
    private transient ReferenceModelProperties referenceModelProperties;
    private transient ConfigurableFlowLabelService configurableFlowLabelService;
    private transient ConfigurableFlowExpressions configurableFlowExpressions;
    private transient List<InitializeType> initializeList;
    private transient List<String> nonPersistablePropertiesList;
    private Div contentDiv;
    private Div breadcrumbDiv;
    private Div msgAreaDiv;
    private transient ConfigurableFlowValidatable configurableFlowValidatable;
    private ConfigurableFlowValidationResultsPopup validationViolationsPopup;


    @InextensibleMethod
    private static ValidationSeverity getHighestNotConfirmedValidationSeverity(final List<ValidationInfo> validationInfos)
    {
        ValidationSeverity ret = ValidationSeverity.NONE;
        if(CollectionUtils.isNotEmpty(validationInfos))
        {
            ret = new ValidationResult(validationInfos).getHighestNotConfirmedSeverity();
        }
        return ret;
    }


    @Override
    public void doBeforeComposeChildren(final Component comp) throws Exception
    {
        super.doBeforeComposeChildren(comp);
        comp.setAttribute(ATTRIBUTE_FLOW, new ConfigurableFlowNavigationFacade(this));
    }


    @Override
    public void initialize(final Component comp)
    {
        referenceModelProperties.initialize(getModel());
        prepareValidationResultModel();
        prepareValidationResultsPopup(comp);
        if(getValue(WIZARD_CONTEXT, Object.class) == null)
        {
            initializeContext();
        }
        final Map<String, Object> ctx = getValue(WIZARD_CURRENT_CONTEXT, Map.class);
        if(ctx != null)
        {
            final String typeCode = (String)ctx.get(ConfigurableFlowContextParameterNames.TYPE_CODE.getName());
            final Flow flowConfiguration = getValue(typeCode + FLOW_CONFIGURATION, Flow.class);
            if(flowConfiguration != null)
            {
                final PrepareType prepare = flowConfiguration.getPrepare();
                initializeList = prepare == null ? Collections.emptyList() : prepare.getInitialize();
                getConfigurableFlowLabelService().setInitializeList(getInitializeList());
                applyWindowSize(flowConfiguration.getSize());
                getRenderer().setShowBreadcrumb(flowConfiguration.isShowBreadcrumb());
            }
            updateWidgetTitle();
            initConfigurableFlowRenderer();
            getRenderer().renderCurrentStep(getCurrentStep());
        }
        initializeDelegateController();
    }


    protected void initializeDelegateController()
    {
        getConfigurableFlowPersistDelegate().setController(this);
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectsUpdatedEvent(final CockpitEvent event)
    {
        handleUpdate(event);
    }


    protected void handleUpdate(final CockpitEvent event)
    {
        final Set<String> properties = referenceModelProperties.getReferencedModelProperties(event.getData());
        if(CollectionUtils.isNotEmpty(properties))
        {
            event.getDataAsCollection()//
                            .forEach(data -> referenceModelProperties//
                                            .updateReferenceProperties(properties, data, this::handleObjectNotFoundException));
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectDeletedEvent(final CockpitEvent event)
    {
        referenceModelProperties.handleReferencedObjectDeletedEvent(event.getData());
    }


    @InextensibleMethod
    private void handleObjectNotFoundException(final ObjectNotFoundException exception, final Object objectInput)
    {
        if(LOG.isWarnEnabled())
        {
            LOG.warn("Object could not be found: " + objectInput, exception);
        }
        getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                        NotificationEventTypes.EVENT_TYPE_OBJECT_LOAD, Level.FAILURE, Collections.singletonMap(objectInput, exception));
    }


    @SocketEvent(socketId = SOCKET_IN_CONTEXT_TYPE)
    public void displayConfig(final String contextType)
    {
        if(StringUtils.isBlank(contextType))
        {
            LOG.warn("Context type is null.");
            return;
        }
        displayConfig(Collections.singletonMap(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), contextType));
    }


    @SocketEvent(socketId = SOCKET_IN_CTX)
    public void displayConfig(final Map<String, Object> context)
    {
        if(context == null)
        {
            LOG.warn("context is null");
            return;
        }
        cleanUpRendererModelObservers();
        setValue(WIZARD_CURRENT_CONTEXT, context);
        if(context.get(ConfigurableFlowContextParameterNames.TYPE_CODE.getName()) instanceof String)
        {
            final String typeCode = (String)context.get(ConfigurableFlowContextParameterNames.TYPE_CODE.getName());
            try
            {
                final Object contextFlowConfig = context.get(SETTING_CONFIGURABLE_FLOW_CONFIG_CTX);
                final String configurableFlowConfigCtx;
                if(contextFlowConfig instanceof String && StringUtils.isNotBlank((String)contextFlowConfig))
                {
                    configurableFlowConfigCtx = (String)contextFlowConfig;
                }
                else
                {
                    configurableFlowConfigCtx = getWidgetSettings().getString(SETTING_CONFIGURABLE_FLOW_CONFIG_CTX);
                }
                final Flow flowConfiguration = getWidgetInstanceManager()
                                .loadConfiguration(new DefaultConfigContext(configurableFlowConfigCtx, typeCode), Flow.class);
                if(flowConfiguration == null)
                {
                    return;
                }
                nonPersistablePropertiesList = (List<String>)context
                                .get(ConfigurableFlowContextParameterNames.NON_PERSISTABLE_PROPERTIES_LIST.getName());
                prefillContext(context);
                initFlow(flowConfiguration);
                getConfigurableFlowLabelService().setInitializeList(getInitializeList());
                setValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_CONF, flowConfiguration);
                setValue(typeCode + FLOW_CONFIGURATION, flowConfiguration);
                addStepOrSubflows(flowConfiguration.getStepOrSubflow());
                final Iterator<StepType> stepIterator = getAllSteps().iterator();
                if(!stepIterator.hasNext())
                {
                    throw new CockpitConfigurationException("No steps defined");
                }
                setValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP, stepIterator.next());
                updateWidgetTitle();
                initConfigurableFlowRenderer();
                getRenderer().setShowBreadcrumb(flowConfiguration.isShowBreadcrumb());
                getRenderer().renderCurrentStep(getCurrentStep());
                applyWindowSize(flowConfiguration.getSize());
                registerListeners();
                doInitialValidationOnFirstWizardDisplay();
            }
            catch(final CockpitConfigurationException e)
            {
                LOG.warn(e.getMessage(), e);
            }
        }
    }


    protected void registerListeners()
    {
        CockpitComponentsUtils
                        .findClosestComponent(getWidgetslot(), Window.class, ConfigurableFlowController.SCLASS_YW_MODAL_CONFIGURABLEFLOW)
                        .ifPresent((final Window window) -> window.addEventListener(Events.ON_CLOSE, e -> doCancel(getCurrentStep())));
    }


    protected void applyWindowSize(final Size size)
    {
        CockpitComponentsUtils.findClosestComponent(getWidgetslot(), Window.class, SCLASS_YW_MODAL_CONFIGURABLEFLOW)
                        .ifPresent(window -> {
                            cleanWindowSizeStyling(window);
                            switch(size)
                            {
                                case SMALL:
                                    UITools.addSClass(window, SCLASS_YW_MODAL_CONFIGURABLEFLOW_SMALL);
                                    break;
                                case MEDIUM:
                                    UITools.addSClass(window, SCLASS_YW_MODAL_CONFIGURABLEFLOW_MEDIUM);
                                    break;
                                case LARGE:
                                    UITools.addSClass(window, SCLASS_YW_MODAL_CONFIGURABLEFLOW_LARGE);
                                    break;
                                default:
                                    break;
                            }
                        });
    }


    protected void cleanWindowSizeStyling(final Window window)
    {
        if(window.getWidth() != null)
        {
            window.setWidth(null);
        }
        if(window.getHeight() != null)
        {
            window.setHeight(null);
        }
    }


    protected void doInitialValidation()
    {
        hideValidationPopUp();
        final Collection<LocalizedQualifier> qualifiersWithLocales = getQualifiersWithLocalesForCurrentStep(getCurrentStep());
        validateProperties(qualifiersWithLocales);
    }


    protected void doInitialValidationOnFirstWizardDisplay()
    {
        hideValidationPopUp();
        final Collection<LocalizedQualifier> qualifiersWithLocales = getQualifiersWithLocalesForCurrentStep(getCurrentStep(),
                        this::isInitialValidationEnabled);
        validateProperties(qualifiersWithLocales);
    }


    @InextensibleMethod
    private boolean isInitialValidationEnabled(final PropertyType property)
    {
        final Predicate<Parameter> disableInitialValidation = param -> Objects.equals(param.getName(), "disableInitialValidation")
                        && Objects.equals(param.getValue(), "true");
        return Optional.ofNullable(property).map(PropertyType::getEditorParameter)
                        .map(paramList -> paramList.stream().noneMatch(disableInitialValidation)).orElse(true);
    }


    @InextensibleMethod
    private void cleanUpRendererModelObservers()
    {
        if(renderer != null)
        {
            renderer.cleanUpRendererObservers();
        }
    }


    protected void initFlow(final Flow flow)
    {
        Validate.notNull("Flow may not be null", flow);
        final PrepareType prepare = flow.getPrepare();
        if(prepare == null)
        {
            return;
        }
        initializeList = prepare.getInitialize();
        if(initializeList != null)
        {
            for(final InitializeType init : initializeList)
            {
                final String property = init.getProperty();
                Validate.notBlank("Initialize: property may not be null", property);
                Validate.assertTrue(String.format("Property %s should have specified template-bean or type", property),
                                StringUtils.isNotBlank(init.getTemplateBean()) || StringUtils.isNotBlank(init.getType()));
                if(init.getTemplateBean() != null)
                {
                    initializeObjectFromTemplateBean(property, init.getTemplateBean());
                }
                else
                {
                    initializeProperty(property, init.getType(), null);
                }
            }
        }
        assignProperties(prepare.getAssign());
        if(StringUtils.isNotBlank(prepare.getHandler()))
        {
            final FlowPrepareHandler prepareHandler = BackofficeSpringUtil.getBean(prepare.getHandler(), FlowPrepareHandler.class);
            prepareHandler.prepareFlow(prepare, getWidgetInstanceManager());
        }
    }


    protected void assignProperties(final List<AssignType> toAssign)
    {
        if(CollectionUtils.isEmpty(toAssign))
        {
            return;
        }
        for(final AssignType assign : toAssign)
        {
            final String property = assign.getProperty();
            if(property != null)
            {
                if(StringUtils.isNotBlank(assign.getLang()))
                {
                    initializeLocalizedProperty(property, assign.getLang(), assign.getValue());
                }
                else
                {
                    initializeProperty(property, assign.getType(), assign.getValue());
                }
            }
        }
    }


    public List<StepType> getAllSteps()
    {
        List ret = getValue(ConfigurableFlowDefinitions.WIZARD_ALL_STEPS, List.class);
        if(ret == null)
        {
            ret = new ArrayList<>();
            setValue(ConfigurableFlowDefinitions.WIZARD_ALL_STEPS, ret);
        }
        return ret;
    }


    public String getCurrentTitle()
    {
        final Flow flowConfiguration = getValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_CONF, Flow.class);
        if(flowConfiguration != null)
        {
            return flowConfiguration.getTitle();
        }
        return null;
    }


    public StepType getCurrentStep()
    {
        return getValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP, StepType.class);
    }


    protected void setCurrentStep(final StepType step)
    {
        setValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP, step);
        updateWidgetTitle();
        getRenderer().renderCurrentStep(getCurrentStep());
    }


    public void updateNavigation()
    {
        getRenderer().updateNavigation();
    }


    public StepType getStep(final String id)
    {
        final List<StepType> allSteps = getAllSteps();
        for(final StepType step : allSteps)
        {
            if(step.getId().equals(id))
            {
                return step;
            }
        }
        return null;
    }


    public StepType getNextStep(final StepType currentStep)
    {
        final NextType nextButtonConfig = currentStep.getNavigation() != null ? currentStep.getNavigation().getNext() : null;
        final int defaultIndexShift = 1;
        return getTargetStep(currentStep, nextButtonConfig, defaultIndexShift);
    }


    public void doNext(final StepType stepType, final boolean shouldValidate)
    {
        if(canPerformAction(stepType, shouldValidate))
        {
            doNext(getCurrentStep());
        }
    }


    public void doNext(final StepType currentStep)
    {
        final boolean isSuccess = persistProperties(currentStep.getNavigation().getNext());
        if(isSuccess)
        {
            setCurrentStep(getNextStep(currentStep));
            doInitialValidation();
        }
    }


    /**
     * @deprecated since 2005, please use
     *             {@link DefaultConfigurableFlowControllerPersistDelegate#persistProperties(AbstractActionType)}
     */
    @Deprecated(since = "2005", forRemoval = true)
    public boolean persistProperties(final AbstractActionType actionType)
    {
        return getConfigurableFlowPersistDelegate().persistProperties(actionType);
    }


    protected boolean canPerformAction(final StepType currentStep, final boolean shouldValidate)
    {
        if(shouldValidate && currentStep != null)
        {
            showValidationPopUp();
            final Collection<LocalizedQualifier> qualifiers = getQualifiersWithLocalesForCurrentStep(currentStep);
            return validationPassed(validateProperties(qualifiers));
        }
        else
        // validation disabled
        {
            return currentStep != null;
        }
    }


    public Collection<LocalizedQualifier> getQualifiersForAllSteps()
    {
        return getAllSteps().stream().map(this::getQualifiersWithLocalesForCurrentStep).flatMap(Collection::stream)
                        .collect(Collectors.toSet());
    }


    protected Collection<LocalizedQualifier> getQualifiersWithLocalesForCurrentStep(final StepType currentStep)
    {
        return getQualifiersWithLocalesForCurrentStep(currentStep, property -> true);
    }


    @InextensibleMethod
    private Collection<LocalizedQualifier> getQualifiersWithLocalesForCurrentStep(final StepType currentStep,
                    final Predicate<PropertyType> additionalPropertyFilter)
    {
        if(currentStep.getContent() == null)
        {
            return Collections.emptyList();
        }
        final Collection<LocalizedQualifier> qualifiersWithLocales = new HashSet<>();
        for(final Object propertyOrPropertyListOrCustomView : currentStep.getContent().getPropertyOrPropertyListOrCustomView())
        {
            final Predicate<PropertyType> validable = p -> BooleanUtils.isNotFalse(p.isValidate());
            if(propertyOrPropertyListOrCustomView instanceof PropertyListType)
            {
                final PropertyListType propertyList = (PropertyListType)propertyOrPropertyListOrCustomView;
                qualifiersWithLocales.addAll(propertyList.getProperty().stream().filter(validable).filter(additionalPropertyFilter)
                                .map(PropertyType::getQualifier).map(each -> String.format("%s.%s", propertyList.getRoot(), each))
                                .map(LocalizedQualifier::new).collect(Collectors.toSet()));
            }
            else if(propertyOrPropertyListOrCustomView instanceof PropertyType)
            {
                final PropertyType property = (PropertyType)propertyOrPropertyListOrCustomView;
                if(validable.and(additionalPropertyFilter).test(property))
                {
                    qualifiersWithLocales
                                    .add(new LocalizedQualifier(((PropertyType)propertyOrPropertyListOrCustomView).getQualifier()));
                }
            }
            else if(propertyOrPropertyListOrCustomView instanceof ViewType)
            {
                qualifiersWithLocales
                                .addAll(getViewTypeValidationPropertiesWithLocales((ViewType)propertyOrPropertyListOrCustomView));
            }
        }
        return qualifiersWithLocales;
    }


    /**
     * @deprecated since 1811, use {@link #getQualifiersWithLocalesForCurrentStep(StepType)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Set<String> getQualifiersForCurrentStep(final StepType currentStep)
    {
        if(currentStep.getContent() == null)
        {
            return Collections.emptySet();
        }
        final Set<String> qualifiers = new HashSet<>();
        for(final Object propertyOrPropertyListOrCustomView : currentStep.getContent().getPropertyOrPropertyListOrCustomView())
        {
            final Predicate<PropertyType> validable = p -> BooleanUtils.isNotFalse(p.isValidate());
            if(propertyOrPropertyListOrCustomView instanceof PropertyListType)
            {
                final PropertyListType propertyList = (PropertyListType)propertyOrPropertyListOrCustomView;
                qualifiers.addAll(propertyList.getProperty().stream().filter(validable).map(PropertyType::getQualifier)
                                .map(each -> String.format("%s.%s", propertyList.getRoot(), each)).collect(Collectors.toList()));
            }
            else if(propertyOrPropertyListOrCustomView instanceof PropertyType)
            {
                final PropertyType property = (PropertyType)propertyOrPropertyListOrCustomView;
                if(validable.test(property))
                {
                    qualifiers.add(((PropertyType)propertyOrPropertyListOrCustomView).getQualifier());
                }
            }
            else if(propertyOrPropertyListOrCustomView instanceof ViewType)
            {
                qualifiers.addAll(getViewTypeValidationProperties((ViewType)propertyOrPropertyListOrCustomView));
            }
        }
        return qualifiers;
    }


    protected Collection<LocalizedQualifier> getViewTypeValidationPropertiesWithLocales(final ViewType viewType)
    {
        final Collection<LocalizedQualifier> qualifiersWithLocales = new LinkedList<>();
        viewType.getContent().stream().filter(
                                        serializable -> serializable instanceof JAXBElement && ((JAXBElement)serializable).getValue() instanceof Renderer)
                        .map(serializable -> (Renderer)((JAXBElement)serializable).getValue())
                        .forEach(customRenderer -> qualifiersWithLocales
                                        .addAll(getValidationPropertiesWithLocalesFromCustomRenderer(customRenderer)));
        return qualifiersWithLocales;
    }


    protected Collection<LocalizedQualifier> getValidationPropertiesWithLocalesFromCustomRenderer(final Renderer customRenderer)
    {
        final String handler = customRenderer.getSpringBean();
        final Object bean = getSpringBean(handler);
        if(bean instanceof LocalizedValidationAwareCustomViewRenderer)
        {
            return ((LocalizedValidationAwareCustomViewRenderer)bean).getValidationPropertiesWithLocales(getWidgetInstanceManager(),
                            customRenderer.getParameter().stream()
                                            .collect(Collectors.toMap(Parameter::getName, Parameter::getValue, (a, b) -> a)));
        }
        return Collections.emptyList();
    }


    protected Object getSpringBean(final String handler)
    {
        return BackofficeSpringUtil.getBean(handler);
    }


    /**
     * @deprecated since 1811, use {@link #getViewTypeValidationPropertiesWithLocales(ViewType)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Set<String> getViewTypeValidationProperties(final ViewType viewType)
    {
        final Set<String> qualifiers = new HashSet<>();
        viewType.getContent().stream().filter(
                                        serializable -> serializable instanceof JAXBElement && ((JAXBElement)serializable).getValue() instanceof Renderer)
                        .map(serializable -> (Renderer)((JAXBElement)serializable).getValue()).forEach(customRenderer -> {
                            final String handler = customRenderer.getSpringBean();
                            final Object bean = BackofficeSpringUtil.getBean(handler);
                            if(bean instanceof ValidationAwareCustomViewRenderer)
                            {
                                final Set<String> validationProperties = ((ValidationAwareCustomViewRenderer)bean)
                                                .getValidationProperties(getWidgetInstanceManager(), customRenderer.getParameter().stream()
                                                                .collect(Collectors.toMap(Parameter::getName, Parameter::getValue, (a, b) -> a)));
                                if(CollectionUtils.isNotEmpty(validationProperties))
                                {
                                    qualifiers.addAll(validationProperties);
                                }
                            }
                        });
        return qualifiers;
    }


    public void doCustomPersist(final StepType currentStep, final boolean shouldValidate)
    {
        try
        {
            final boolean shouldProceed = currentStep != null && (!shouldValidate || validateCustomStep(currentStep));
            if(shouldProceed && persistProperties(currentStep.getNavigation().getCustom()))
            {
                final Map<String, ComposedHandlerType> handlerMap = getFlowConfiguration().getHandler().stream()
                                .collect(Collectors.toMap(ComposedHandlerType::getHandlerId, Function.identity()));
                renderer.doCustom(currentStep, currentStep.getNavigation().getCustom(), handlerMap);
            }
        }
        catch(final RuntimeException re)
        {
            LOG.warn("Custom handler failed to process", re);
        }
    }


    public boolean validateCustomStep(final StepType currentStep)
    {
        return currentStep.getNavigation().getCustom().isValidateVisibleOnly()
                        ? validationPassed(validateProperties(getQualifiersForAllSteps()))
                        : validationPassed(validateCurrentObject());
    }


    public void doCustom(final StepType currentStep, final boolean shouldValidate)
    {
        if(canPerformAction(currentStep, shouldValidate))
        {
            doCustom(currentStep);
        }
    }


    public void doCustom(final StepType currentStep)
    {
        final CustomType customType = currentStep.getNavigation().getCustom();
        final boolean isSuccess = persistProperties(customType);
        if(isSuccess && customType != null)
        {
            final StepType targetStep = getStep(customType.getDefaultTarget());
            if(targetStep != null)
            {
                setCurrentStep(targetStep);
            }
        }
    }


    public void doBack(final StepType currentStep)
    {
        final boolean isSuccess = persistProperties(currentStep.getNavigation().getBack());
        if(isSuccess)
        {
            setCurrentStep(getPreviousStep(currentStep));
            doInitialValidation();
        }
    }


    protected StepType getPreviousStep(final StepType currentStep)
    {
        final BackType backButtonConfig = currentStep.getNavigation() != null ? currentStep.getNavigation().getBack() : null;
        final int defaultIndexShift = -1;
        return getTargetStep(currentStep, backButtonConfig, defaultIndexShift);
    }


    protected StepType getTargetStep(final StepType currentStep, final AbstractActionType clickedButtonConfig,
                    final int defaultIndexShift)
    {
        if(clickedButtonConfig != null)
        {
            final StepType target = getTargetStepType(clickedButtonConfig);
            if(target != null)
            {
                return target;
            }
        }
        // default behaviour - next/previous step
        final List<StepType> allSteps = getAllSteps();
        final int index = allSteps.indexOf(currentStep);
        final int targetIndex = index + defaultIndexShift;
        if(0 <= targetIndex && targetIndex < allSteps.size())
        {
            return getAllSteps().get(targetIndex);
        }
        throw new IllegalStateException(String.format("Step %d doesn't exist", targetIndex));
    }


    protected StepType getTargetStepType(final AbstractActionType clickedButtonConfig)
    {
        Validate.notNull("The action type may not be null", clickedButtonConfig);
        // check the 'if' definitions
        if(CollectionUtils.isNotEmpty(clickedButtonConfig.getIf()))
        {
            for(final IfType ifElement : clickedButtonConfig.getIf())
            {
                final String expr = ifElement.getExpression();
                final String target = ifElement.getTarget();
                if(getConfigurableFlowExpressions()
                                .evaluatedExpression2Boolean(getConfigurableFlowExpressions().evalExpression(getModel(), expr)))
                {
                    return getStep(target);
                }
            }
        }
        // check the 'default-target' definition
        final String defaultTarget = clickedButtonConfig.getDefaultTarget();
        if(StringUtils.isNotBlank(defaultTarget))
        {
            return getStep(defaultTarget);
        }
        return null;
    }


    public void doCancel(final StepType currentStep)
    {
        try
        {
            final boolean isSuccess = persistProperties(currentStep.getNavigation().getCancel());
            if(isSuccess && isTemplate())
            {
                closeWindow();
            }
            else if(isSuccess && !isTemplate())
            {
                getRenderer().refreshView();
            }
        }
        finally
        {
            revertProperties(currentStep.getNavigation().getCancel());
        }
    }


    /**
     * @deprecated since 2005, please use
     *             {@link DefaultConfigurableFlowControllerPersistDelegate#revertProperties(CancelType)}}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void revertProperties(final CancelType cancel)
    {
        getConfigurableFlowPersistDelegate().revertProperties(cancel);
    }


    public void doDone(final StepType currentStep, final boolean shouldValidate)
    {
        /*
         * The whole object already was validated in <code>doCustomPersist()<code> method if done operation is triggered
         * by custom logic handler and the <code>validate-visible-only<code> parameter is set to false.
         */
        final boolean isWholeObjectValidatedInCustomPersistent = currentStep.getNavigation().getCustom() != null
                        && (!currentStep.getNavigation().getCustom().isValidateVisibleOnly());
        if(isWholeObjectValidatedInCustomPersistent || !shouldValidate || validationPassed(validateCurrentObject()))
        {
            doDone(currentStep);
        }
    }


    public void doDone(final StepType currentStep)
    {
        final DoneType done = currentStep.getNavigation().getDone();
        if(done != null)
        {
            assignProperties(done.getAssign());
        }
        // persist properties
        final boolean isSuccess = persistProperties(currentStep.getNavigation().getDone());
        final Map<String, Object> retObjects = new LinkedHashMap<>();
        for(final InitializeType init : getInitializeList())
        {
            final String property = init.getProperty();
            if(property != null)
            {
                final Object propertyValue = getValue(property, Object.class);
                if(propertyValue != null)
                {
                    retObjects.put(property, propertyValue);
                }
            }
        }
        updateWidgetTitle();
        getRenderer().renderCurrentStep(currentStep);
        if(isSuccess)
        {
            sendOutput(SOCKET_WIZARD_RESULT, retObjects);
        }
    }


    protected boolean validationPassed(final ValidationResult validationResult)
    {
        return ValidationSeverity.WARN.isHigherThan(validationResult.getHighestNotConfirmedSeverity());
    }


    /**
     * @deprecated since 2005, please use
     *             {@link DefaultConfigurableFlowControllerPersistDelegate#showSuccessNotification(Object)}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void showSuccessNotification(final Object persistedObject)
    {
        getConfigurableFlowPersistDelegate().showSuccessNotification(persistedObject);
    }


    /**
     * @deprecated since 2005, please use
     *             {@link DefaultConfigurableFlowControllerPersistDelegate#showFailureNotification(ObjectSavingException)}}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void showFailureNotification(final ObjectSavingException failureException)
    {
        getConfigurableFlowPersistDelegate().showFailureNotification((Throwable)failureException);
    }


    /**
     * @deprecated since 2005, please use
     *             {@link DefaultConfigurableFlowControllerPersistDelegate#showFailureNotification(Throwable)}}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected void showFailureNotification(final Throwable failureException)
    {
        getConfigurableFlowPersistDelegate().showFailureNotification(failureException);
    }


    protected void addStepOrSubflows(final List objects)
    {
        if(objects == null)
        {
            return;
        }
        for(final Object stepOrSubflow : objects)
        {
            if(stepOrSubflow instanceof StepType)
            {
                getAllSteps().add((StepType)stepOrSubflow);
            }
            else if(stepOrSubflow instanceof SubflowType)
            {
                addStepOrSubflows(((SubflowType)stepOrSubflow).getStepOrSubflow());
            }
        }
    }


    protected void initializeContext()
    {
        if(getValue(WIZARD_CONTEXT, Object.class) == null)
        {
            final Map<String, Object> ctx = new HashMap<>();
            setValue(WIZARD_CONTEXT, ctx);
        }
    }


    protected void prefillContext(final Map<String, Object> context)
    {
        initializeContext();
        final Map<String, Object> ctx = (Map<String, Object>)getValue(WIZARD_CONTEXT, Object.class);
        if(context != null)
        {
            for(final Map.Entry<String, Object> entry : context.entrySet())
            {
                ctx.put(entry.getKey(), entry.getValue());
            }
        }
    }


    protected void initializeLocalizedProperty(final String property, final String lang, final String valueExpression)
    {
        Validate.notBlank("Error: Need property, lang, valueExpression", property, lang, valueExpression);
        final Object newInstance = getConfigurableFlowExpressions().evalExpression(getModel(), valueExpression);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Result of localizedMap label {}", newInstance);
        }
        final Map<Locale, Object> localizedMap = getValue(property, Map.class);
        localizedMap.put(Locale.forLanguageTag(lang), newInstance);
        setValue(property, localizedMap);
    }


    protected void initializeProperty(final String property, final String type, final String valueExpression)
    {
        final Object newInstance;
        Validate.atLeastOneNotNull("Error: Need type or value.", type, valueExpression);
        if(valueExpression != null)
        {
            newInstance = createInstanceByExpression(valueExpression);
        }
        else
        {
            // dynamicTypeName - type name passed in context e.g. ctx.TYPE_CODE=Product
            final Object dynamicTypeName = getConfigurableFlowExpressions().evalExpression(getModel(), type);
            final String finalTypeName = dynamicTypeName instanceof String ? (String)dynamicTypeName : type;
            final Class clazz = BackofficeSpringUtil.loadClass(finalTypeName);
            if(clazz == null)
            {
                newInstance = createInstanceByTypeCode(finalTypeName);
            }
            else
            {
                newInstance = createInstanceByClass(clazz);
            }
        }
        setValue(property, newInstance);
    }


    @InextensibleMethod
    private Object createInstanceByExpression(final String expression)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Instantiating object by expression {}", expression);
        }
        return getConfigurableFlowExpressions().evalExpression(getModel(), expression);
    }


    @InextensibleMethod
    private Object createInstanceByClass(final Class clazz)
    {
        final Object newInstance;
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Instantiating object by class: {}", clazz);
            }
            newInstance = clazz.getDeclaredConstructor().newInstance();
        }
        catch(final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            if(LOG.isErrorEnabled())
            {
                LOG.error(e.getMessage(), e);
            }
            throw new IllegalArgumentException(e);
        }
        return newInstance;
    }


    @InextensibleMethod
    private Object createInstanceByTypeCode(final String code)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Instantiating object by type code: {}", code);
            }
            return objectFacade.create(code);
        }
        catch(final ObjectCreationException e)
        {
            LOG.debug(e.getMessage(), e);
        }
        return null;
    }


    protected void initializeObjectFromTemplateBean(final String property, final String templateBean)
    {
        final Object newInstance = BackofficeSpringUtil.getBean(templateBean);
        if(newInstance == null)
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn("Cannot initialize object: {} - template bean '{}' does not exist", property, templateBean);
            }
            return;
        }
        if(BackofficeSpringUtil.getApplicationContext().isSingleton(templateBean))
        {
            if(LOG.isWarnEnabled())
            {
                LOG.warn("Cannot initialize object: {} - template bean '{}' cannot be a singleton", property, templateBean);
            }
            return;
        }
        setValue(property, newInstance);
    }


    /**
     * @deprecated since 2005, please use
     *             {@link DefaultConfigurableFlowControllerPersistDelegate#persistWidgetProperty(String)}
     */
    @Deprecated(since = "2005", forRemoval = true)
    protected Object persistWidgetProperty(final String property) throws ObjectSavingException
    {
        return getConfigurableFlowPersistDelegate().persistWidgetProperty(property);
    }


    protected void closeWindow()
    {
        getFlowCancelActionHandler().perform();
        sendOutput("cancel", null);
    }


    protected boolean isTemplate()
    {
        final WidgetInstance currentInstance = getWidgetslot().getWidgetInstance();
        return currentInstance != null && currentInstance.getWidget().isTemplate();
    }


    protected void updateWidgetTitle()
    {
        setWidgetTitle(getConfigurableFlowLabelService().getLabel(getCurrentTitle()));
    }


    protected void initConfigurableFlowRenderer()
    {
        renderer.setContentDiv(contentDiv);
        renderer.setBreadcrumbDiv(breadcrumbDiv);
        renderer.setMsgAreaDiv(msgAreaDiv);
        renderer.setWidgetInstanceManager(getWidgetInstanceManager());
        renderer.setEditorChangeListener(new EditorChangeListener(this));
        renderer.setTransitionListenerFactory(new TransitionListenerFactory(this));
        renderer.setAllSteps(getAllSteps());
        renderer.setConfigurableFlowLabelService(getConfigurableFlowLabelService());
        renderer.setAttributeDescriptionIconRenderer(attributeDescriptionIconRenderer);
        renderer.setValidationHandler(localizationAwareValidationHandler);
    }


    public ConfigurableFlowRenderer getRenderer()
    {
        return renderer;
    }


    protected ConfigurableFlowLabelService getConfigurableFlowLabelService()
    {
        if(configurableFlowLabelService == null)
        {
            configurableFlowLabelService = new ConfigurableFlowLabelService(getWidgetInstanceManager(), labelService, typeFacade);
        }
        return configurableFlowLabelService;
    }


    public List<InitializeType> getInitializeList()
    {
        if(initializeList == null)
        {
            initializeList = Collections.emptyList();
        }
        return initializeList;
    }


    /**
     * List of properties to exclude from persistence
     *
     * @return list of non-persistable properties
     */
    public List<String> getNonPersistablePropertiesList()
    {
        if(nonPersistablePropertiesList == null)
        {
            nonPersistablePropertiesList = Collections.emptyList();
        }
        return nonPersistablePropertiesList;
    }


    protected ConfigurableFlowExpressions getConfigurableFlowExpressions()
    {
        if(configurableFlowExpressions == null)
        {
            configurableFlowExpressions = new ConfigurableFlowExpressions();
        }
        return configurableFlowExpressions;
    }


    public CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    public Div getContentDiv()
    {
        return contentDiv;
    }


    public Div getBreadcrumbDiv()
    {
        return breadcrumbDiv;
    }


    public Div getMsgAreaDiv()
    {
        return msgAreaDiv;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public AttributeDescriptionIconRenderer getAttributeDescriptionIconRenderer()
    {
        return attributeDescriptionIconRenderer;
    }


    public LocalizationAwareValidationHandler getLocalizationAwareValidationHandler()
    {
        return localizationAwareValidationHandler;
    }


    protected ValidationResult validateCurrentObject()
    {
        final List<ValidationInfo> validationInfos = collectValidationInfosForCurrentObject();
        final List<ValidationInfo> errorResults = validationInfos.stream()
                        .filter(i -> ValidationSeverity.ERROR.equals(i.getValidationSeverity())).collect(Collectors.toList());
        return doValidate(ListUtils.union(errorResults, validationInfos));
    }


    protected List<ValidationInfo> collectValidationInfosForCurrentObject()
    {
        final List<ValidationInfo> validation = new ArrayList<>();
        for(final String modelContext : getModelContexts())
        {
            final List<ValidationInfo> partialValidationInfos = Lists.newArrayList();
            partialValidationInfos.addAll(getLocalizationAwareValidationHandler().validate(getCurrentObject(modelContext),
                            createValidationContext(modelContext)));
            validation.addAll(ValidationInfoFactoryWithPrefix.addPrefix(partialValidationInfos, modelContext));
        }
        return validation;
    }


    protected ValidationResult validateProperties(final Collection<LocalizedQualifier> localizedQualifiers)
    {
        final List<ValidationInfo> validationInfos = new ArrayList<>();
        final Map<String, Collection<LocalizedQualifier>> qualifiersWithPrefixes = new HashMap<>();
        for(final LocalizedQualifier localizedQualifier : localizedQualifiers)
        {
            final String currentPrefix = configurableFlowValidatable.getCurrentPrefix(localizedQualifier.getName());
            final String qualifier = configurableFlowValidatable.getCurrentObjectPath(localizedQualifier.getName());
            qualifiersWithPrefixes.computeIfAbsent(currentPrefix, prefix -> new HashSet<>())
                            .add(new LocalizedQualifier(qualifier, localizedQualifier.getLocales()));
        }
        for(final Map.Entry<String, Collection<LocalizedQualifier>> entry : qualifiersWithPrefixes.entrySet())
        {
            final String currentPrefix = entry.getKey();
            if(StringUtils.isNotBlank(currentPrefix))
            {
                final List<ValidationInfo> partialValidationInfos = getLocalizationAwareValidationHandler()
                                .validate(getCurrentObject(currentPrefix), entry.getValue(), createValidationContext(currentPrefix));
                validationInfos.addAll(ValidationInfoFactoryWithPrefix.addPrefix(partialValidationInfos, currentPrefix));
            }
        }
        return pickValidationPopupVisibility(validationInfos);
    }


    protected ValidationResult pickValidationPopupVisibility(final List<ValidationInfo> validationInfos)
    {
        final ValidationSeverity highestValidationSeverity = getHighestNotConfirmedValidationSeverity(validationInfos);
        final boolean infoTheHighest = ValidationSeverity.INFO.equals(highestValidationSeverity);
        try
        {
            if(infoTheHighest)
            {
                hideValidationPopUp();
            }
            return doValidate(validationInfos);
        }
        finally
        {
            if(infoTheHighest)
            {
                showValidationPopUp();
            }
        }
    }


    /**
     * @deprecated since 1811, use {@link #validateProperties(Collection)} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected ValidationResult validateProperties(final Set<String> fullPathQualifiers)
    {
        final List<ValidationInfo> validationInfos = new ArrayList<>();
        final Map<String, Set<String>> qualifiersWithPrefixes = new HashMap<>();
        for(final String fullPathQualifier : fullPathQualifiers)
        {
            final String currentPrefix = configurableFlowValidatable.getCurrentPrefix(fullPathQualifier);
            final String qualifier = configurableFlowValidatable.getCurrentObjectPath(fullPathQualifier);
            qualifiersWithPrefixes.computeIfAbsent(currentPrefix, prefix -> new HashSet<>()).add(qualifier);
        }
        for(final Map.Entry<String, Set<String>> entry : qualifiersWithPrefixes.entrySet())
        {
            final String currentPrefix = entry.getKey();
            if(StringUtils.isNotBlank(currentPrefix))
            {
                final List<ValidationInfo> partialValidationInfos = getValidationHandler().validate(getCurrentObject(currentPrefix),
                                Lists.newArrayList(entry.getValue()), createValidationContext(currentPrefix));
                validationInfos.addAll(ValidationInfoFactoryWithPrefix.addPrefix(partialValidationInfos, currentPrefix));
            }
        }
        return pickValidationPopupVisibility(validationInfos);
    }


    protected ValidationResult doValidate(final List<ValidationInfo> validations)
    {
        final ValidationResult validationResult = getCurrentValidationResult();
        final Set<ValidationInfo> withConfirmed = new HashSet<>(validationResult.getConfirmed().collect());
        withConfirmed.addAll(validations);
        validationResult.setValidationInfo(withConfirmed);
        showValidationPopUp();
        return validationResult;
    }


    protected void hideValidationPopUp()
    {
        configurableFlowValidatable.setPreventBroadcastValidationChange(true);
        validationViolationsPopup.setVisible(false);
    }


    protected void showValidationPopUp()
    {
        configurableFlowValidatable.setPreventBroadcastValidationChange(false);
    }


    protected ValidationContext createValidationContext(final String modelContext)
    {
        final DefaultValidationContext validationContext = new DefaultValidationContext();
        final ValidationResult validationResult = getCurrentValidationResult().get(modelContext).wrap();
        if(validationResult != null)
        {
            validationContext.setConfirmed(validationResult.getConfirmed().wrap());
        }
        return validationContext;
    }


    public ValidationResult getCurrentValidationResult()
    {
        return getValue(StandardModelKeys.VALIDATION_RESULT_KEY, ValidationResult.class);
    }


    public Object getCurrentObject(final String modelContext)
    {
        return getValue(modelContext, Object.class);
    }


    @Override
    public <T> T getValue(final String key, final Class<T> expectedClass)
    {
        return getWidgetInstanceManager().getModel().getValue(key, expectedClass);
    }


    protected void prepareValidationResultModel()
    {
        ValidationResult validationResultToSet = getModel().getValue(StandardModelKeys.VALIDATION_RESULT_KEY,
                        ValidationResult.class);
        if(validationResultToSet == null)
        {
            validationResultToSet = new ValidationResult();
        }
        getModel().setValue(StandardModelKeys.VALIDATION_RESULT_KEY, validationResultToSet);
    }


    protected void prepareValidationResultsPopup(final Component parent)
    {
        configurableFlowValidatable = new ConfigurableFlowValidatable(this, parent);
        validationViolationsPopup = validationRenderer.createValidationViolationsPopup(configurableFlowValidatable,
                        (actionId, validationResults) -> {
                            final ValidationResult validationResult = getCurrentValidationResult();
                            final ValidationSeverity validationSeverity = validationResult.getHighestNotConfirmedSeverity();
                            if(ValidationSeverity.WARN.equals(validationSeverity))
                            {
                                validationResult.getNotConfirmed(ValidationSeverity.WARN).collect().forEach(info -> info.setConfirmed(true));
                            }
                            if(validationSeverity.isLowerThan(ValidationSeverity.ERROR))
                            {
                                final boolean shouldValidate = false;
                                if(ConfigurableFlowDefinitions.WIZARD_NEXT.equals(actionId))
                                {
                                    doNext(getCurrentStep(), shouldValidate);
                                }
                                else if(ConfigurableFlowDefinitions.WIZARD_DONE.equals(actionId))
                                {
                                    doDone(getCurrentStep(), shouldValidate);
                                }
                                else if(ConfigurableFlowDefinitions.WIZARD_CUSTOM.equals(actionId))
                                {
                                    doCustom(getCurrentStep(), shouldValidate);
                                }
                                else if(ConfigurableFlowDefinitions.WIZARD_CURRENT_STEP_PERSIST.equals(actionId))
                                {
                                    doCustomPersist(getCurrentStep(), shouldValidate);
                                }
                            }
                        });
        getRenderer().setValidationResultsPopup(validationViolationsPopup);
        getRenderer().setValidatableContainer(configurableFlowValidatable);
    }


    public Flow getFlowConfiguration()
    {
        return getValue(ConfigurableFlowDefinitions.WIZARD_CURRENT_CONF, Flow.class);
    }


    public Set<String> getModelContexts()
    {
        final Set<ObjectValuePath> modelContexts = new HashSet<>();
        final Flow configuration = getFlowConfiguration();
        if(configuration != null && configuration.getPrepare() != null)
        {
            if(CollectionUtils.isNotEmpty(configuration.getPrepare().getInitialize()))
            {
                configuration.getPrepare().getInitialize().stream().map(InitializeType::getProperty)
                                .filter(each -> !getNonPersistablePropertiesList().contains(each)).map(ObjectValuePath::parse)
                                .forEach(modelContexts::add);
            }
            if(CollectionUtils.isNotEmpty(configuration.getPrepare().getAssign()))
            {
                configuration.getPrepare().getAssign().stream().map(AssignType::getProperty)
                                .filter(property -> !getNonPersistablePropertiesList().contains(property)).map(ObjectValuePath::parse)
                                .filter(property -> modelContexts.stream().noneMatch(property::startsWith)).forEach(modelContexts::add);
            }
        }
        return modelContexts.stream().map(ObjectValuePath::buildPath).collect(Collectors.toSet());
    }


    public ConfigurableFlowValidationResultsPopup getValidationViolationsPopup()
    {
        return validationViolationsPopup;
    }


    public void setValidationViolationsPopup(final ConfigurableFlowValidationResultsPopup validationViolationsPopup)
    {
        this.validationViolationsPopup = validationViolationsPopup;
    }


    public void jumpToStep(final StepType stepType)
    {
        Preconditions.checkNotNull(stepType);
        setCurrentStep(stepType);
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    /**
     * @deprecated since 1811, use {@link #getLocalizationAwareValidationHandler()} instead
     */
    @Deprecated(since = "1811", forRemoval = true)
    public ValidationHandler getValidationHandler()
    {
        return validationHandler;
    }


    protected ConfigurableFlowControllerPersistDelegate getConfigurableFlowPersistDelegate()
    {
        return configurableFlowPersistDelegate;
    }


    public FlowCancelActionHandler getFlowCancelActionHandler()
    {
        return flowCancelActionHandler;
    }


    public void setFlowCancelActionHandler(final FlowCancelActionHandler flowCancelActionHandler)
    {
        this.flowCancelActionHandler = flowCancelActionHandler;
    }
}
