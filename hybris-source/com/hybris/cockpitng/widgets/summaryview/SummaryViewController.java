/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.config.summaryview.jaxb.AbstractSection;
import com.hybris.cockpitng.config.summaryview.jaxb.Section;
import com.hybris.cockpitng.config.summaryview.jaxb.SummaryView;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererListener;
import com.hybris.cockpitng.widgets.util.ObjectNotFoundExceptionHandler;
import com.hybris.cockpitng.widgets.util.ReferenceModelProperties;
import com.hybris.cockpitng.widgets.util.impl.DefaultReferenceModelProperties;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.fest.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class SummaryViewController extends DefaultWidgetController
{
    public static final String MODEL_CURRENT_OBJECT = StandardModelKeys.CONTEXT_OBJECT;
    public static final String MODEL_CURRENT_DATA_TYPE = "dataType";
    /** @deprecated since 6.6 - use {@link DefaultReferenceModelProperties#MODEL_ALL_REFERENCED_OBJECTS} */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String MODEL_ALL_REFERENCED_OBJECTS = "referencedObjects";
    public static final String SETTING_DEFAULT_SECTION_RENDERER = "defaultSectionRenderer";
    public static final String SETTING_DEFAULT_IMAGE_RENDERER = "defaultThumbnailRenderer";
    public static final String SETTING_DEFAULT_ACTIONS_RENDERER = "defaultActionsRenderer";
    public static final String SETTING_CONFIG_CONTEXT = "summaryViewConfigCtx";
    public static final String DEFAULT_CONFIG_CONTEXT = "summary-view";
    public static final String SOCKET_OUTPUT_FOCUS = "focusAttribute";
    protected static final String DEFAULT_SECTION_RENDERER_BEAN = "summaryViewSectionRenderer";
    protected static final String DEFAULT_THUMBNAIL_RENDERER_BEAN = "summaryViewThumbnailRenderer";
    protected static final String SOCKET_IN_INPUT_OBJECT = "inputObject";
    protected static final String SCLASS_EMPTY_MESSAGE_CONTENT = "yw-summary-content-noitem";
    protected static final String SCLASS_EMPTY_MESSAGE_LABEL = "yw-summaryview-label-noitem";
    protected static final String LABEL_EMPTY_MESSAGE = "emptymessage";
    private static final Logger LOG = LoggerFactory.getLogger(SummaryViewController.class);
    private Div thumbnailContainer;
    private Div titleContainer;
    private Div content;
    private Label emptyMessageLabel;
    private Set<Actions> actionSlots;
    private transient TypeFacade typeFacade;
    private transient LabelService labelService;
    private transient ReferenceModelProperties referenceModelProperties;
    private transient NotifyingWidgetComponentRenderer<HtmlBasedComponent, SummaryView, Object> summaryViewThumbnailRenderer;
    private transient NotifyingWidgetComponentRenderer<Component, AbstractSection, Object> summaryViewSectionRenderer;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        prepareEmptyMessageLabel();
        prepareReferenceModelProperties();
        reload();
    }


    protected void prepareEmptyMessageLabel()
    {
        final Label label = new Label(getLabel(LABEL_EMPTY_MESSAGE));
        label.setSclass(SCLASS_EMPTY_MESSAGE_LABEL);
        setEmptyMessageLabel(label);
    }


    @InextensibleMethod
    protected final void prepareReferenceModelProperties()
    {
        referenceModelProperties.initialize(getModel());
    }


    protected void reload()
    {
        final WidgetComponentRendererListener<Component, AbstractSection, Object> actionsListener = createActionsRendererListener();
        try
        {
            clearContent();
            actionSlots = new HashSet<>();
            getSectionRenderer().addRendererListener(actionsListener);
            if(getCurrentObject().isPresent())
            {
                UITools.modifySClass(getMainLayoutComponent(), SCLASS_EMPTY_MESSAGE_CONTENT, false);
                getCurrentDataType().map(DataType::getCode).ifPresent(type -> {
                    renderMainContent(type);
                    renderThumbnail(type);
                    renderTitle(type);
                });
            }
            else
            {
                UITools.modifySClass(getMainLayoutComponent(), SCLASS_EMPTY_MESSAGE_CONTENT, true);
                content.appendChild(getEmptyMessageLabel());
            }
            reloadThumbnail();
            reloadActions();
        }
        finally
        {
            getSectionRenderer().removeRendererListener(actionsListener);
        }
    }


    protected void clearContent()
    {
        content.getChildren().clear();
        thumbnailContainer.getChildren().clear();
        titleContainer.getChildren().clear();
    }


    /**
     * @deprecated since 6.5, use Use {@link #renderTitle(String)} instead.
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void renderTitle()
    {
        renderTitle(null);
    }


    protected void renderTitle(final String dataType)
    {
        titleContainer.getChildren().clear();
        final SummaryView configuration = loadConfiguration(dataType);
        if(configuration.isDisplayTitle())
        {
            titleContainer.setVisible(true);
            final String titleLabel = labelService.getObjectLabel(getCurrentObject().orElse(null));
            final Label title = new Label(titleLabel);
            title.setTooltiptext(titleLabel);
            titleContainer.appendChild(title);
        }
        else
        {
            titleContainer.setVisible(false);
        }
    }


    @InextensibleMethod
    private HtmlBasedComponent getMainLayoutComponent()
    {
        return (HtmlBasedComponent)content.getParent();
    }


    protected WidgetComponentRendererListener<Component, AbstractSection, Object> createActionsRendererListener()
    {
        return event -> {
            if(event.getSource() instanceof com.hybris.cockpitng.components.Actions)
            {
                actionSlots.add((com.hybris.cockpitng.components.Actions)event.getSource());
            }
        };
    }


    protected SummaryView loadConfiguration(final String typecode)
    {
        final String componentConfigContext = getConfigContext();
        try
        {
            return getWidgetInstanceManager().loadConfiguration(new DefaultConfigContext(componentConfigContext, typecode),
                            SummaryView.class);
        }
        catch(final CockpitConfigurationException e)
        {
            LOG.warn("Could not load Summary View widget configuration for type {}", typecode, e);
            return new SummaryView();
        }
    }


    protected void renderThumbnail(final String dataType)
    {
        thumbnailContainer.getChildren().clear();
        final SummaryView configuration = loadConfiguration(dataType);
        if(configuration.isDisplayThumbnail())
        {
            thumbnailContainer.setVisible(true);
            getThumbnailRenderer().render(thumbnailContainer, configuration, getCurrentObject().orElse(null),
                            getCurrentDataType().orElse(null), getWidgetInstanceManager());
        }
        else
        {
            thumbnailContainer.setVisible(false);
        }
    }


    protected void renderMainContent(final String dataType)
    {
        final SummaryView configuration = loadConfiguration(dataType);
        final List<Section> sections = configuration.getCustomSectionOrSection();
        sections.forEach(section -> getSectionRenderer().render(content, section, getCurrentObject().orElse(null),
                        getCurrentDataType().orElse(null), getWidgetInstanceManager()));
    }


    @SocketEvent(socketId = SOCKET_IN_INPUT_OBJECT)
    public void setInputObject(final Object inputObject)
    {
        if(inputObject == null || isDataTypeChanging(inputObject))
        {
            reset();
        }
        if(inputObject != null)
        {
            processInputObject(inputObject);
        }
    }


    protected boolean isDataTypeChanging(final Object inputObject)
    {
        final String inputObjectType = getTypeFacade().getType(inputObject);
        return !Objects.areEqual(getCurrentDataType().map(DataType::getCode).orElse(null), inputObjectType);
    }


    protected void reset()
    {
        setValue(MODEL_CURRENT_OBJECT, null);
        setValue(MODEL_CURRENT_DATA_TYPE, null);
        setValue(MODEL_ALL_REFERENCED_OBJECTS, null);
        reload();
    }


    protected void processInputObject(final Object inputObject)
    {
        setValue(MODEL_CURRENT_OBJECT, inputObject);
        final String typeCode = typeFacade.getType(inputObject);
        try
        {
            final DataType inputObjectDataType = typeFacade.load(typeCode);
            if(ObjectUtils.notEqual(getCurrentDataType().orElse(null), inputObjectDataType))
            {
                setValue(MODEL_CURRENT_DATA_TYPE, inputObjectDataType);
            }
            reload();
        }
        catch(final TypeNotFoundException e)
        {
            LOG.error("Error loading type", e);
            reset();
        }
    }


    protected void reloadThumbnail()
    {
        thumbnailContainer.getChildren().clear();
        final Optional<DataType> currentDataType = getCurrentDataType();
        thumbnailContainer.setVisible(currentDataType.isPresent());
        currentDataType.map(DataType::getCode).ifPresent(this::renderThumbnail);
    }


    protected void reloadActions()
    {
        final Object inputObject = getCurrentObject().orElse(null);
        for(final Actions actionSlot : actionSlots)
        {
            actionSlot.setInputValue(inputObject);
            actionSlot.reload();
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void onUpdate(final CockpitEvent event)
    {
        for(final Object data : event.getDataAsCollection())
        {
            if(referenceModelProperties.isEdited(data, getCurrentObject().orElse(null)))
            {
                processInputObject(data);
            }
            else
            {
                final Set<String> properties = referenceModelProperties.getReferencedModelProperties(data);
                if(CollectionUtils.isNotEmpty(properties))
                {
                    referenceModelProperties.updateReferenceProperties(properties, data, getObjectNotFoundExceptionHandler());
                }
            }
        }
    }


    protected ObjectNotFoundExceptionHandler getObjectNotFoundExceptionHandler()
    {
        return (e, updatedObject) -> {
            if(LOG.isWarnEnabled())
            {
                LOG.warn("Object could not be found: {}", updatedObject);
            }
        };
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void onDelete(final CockpitEvent event)
    {
        final boolean objectAndDataTypeArePresent = getCurrentObject().isPresent() && getCurrentDataType().isPresent();
        if(!objectAndDataTypeArePresent)
        {
            return;
        }
        getCurrentObject().ifPresent(currentObject -> {
            if(referenceModelProperties.isEdited(event.getData(), currentObject))
            {
                reset();
            }
            else
            {
                referenceModelProperties.handleReferencedObjectDeletedEvent(currentObject);
            }
        });
    }


    protected String getConfigContext()
    {
        return StringUtils.defaultIfBlank(getWidgetSettings().getString(SETTING_CONFIG_CONTEXT), DEFAULT_CONFIG_CONTEXT);
    }


    protected Optional<DataType> getCurrentDataType()
    {
        return Optional.ofNullable(getValue(MODEL_CURRENT_DATA_TYPE, DataType.class));
    }


    protected Optional<Object> getCurrentObject()
    {
        return Optional.ofNullable(getValue(MODEL_CURRENT_OBJECT, Object.class));
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @WireVariable
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    @WireVariable
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected Div getContent()
    {
        return content;
    }


    @Wire
    protected void setContent(final Div content)
    {
        this.content = content;
    }


    protected Div getTitleContainer()
    {
        return titleContainer;
    }


    @Wire
    protected void setTitleContainer(final Div titleContainer)
    {
        this.titleContainer = titleContainer;
    }


    protected Div getThumbnailContainer()
    {
        return thumbnailContainer;
    }


    @Wire
    protected void setThumbnailContainer(final Div thumbnailContainer)
    {
        this.thumbnailContainer = thumbnailContainer;
    }


    protected Label getEmptyMessageLabel()
    {
        return emptyMessageLabel;
    }


    protected void setEmptyMessageLabel(final Label label)
    {
        emptyMessageLabel = label;
    }


    protected Set<Actions> getActionSlots()
    {
        return actionSlots;
    }


    public ReferenceModelProperties getReferenceModelProperties()
    {
        return referenceModelProperties;
    }


    @Required
    public void setReferenceModelProperties(final ReferenceModelProperties referenceModelProperties)
    {
        this.referenceModelProperties = referenceModelProperties;
    }


    protected NotifyingWidgetComponentRenderer<Component, AbstractSection, Object> getSectionRenderer()
    {
        return (NotifyingWidgetComponentRenderer<Component, AbstractSection, Object>)getSummaryViewSectionRenderer();
    }


    /**
     * @deprecated since 6.5
     * @see #getSectionRenderer()
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected WidgetComponentRenderer<Component, AbstractSection, Object> getSummaryViewSectionRenderer()
    {
        if(summaryViewSectionRenderer == null)
        {
            final String sectionRenderer = (String)getWidgetSettings().getOrDefault(SETTING_DEFAULT_SECTION_RENDERER,
                            DEFAULT_SECTION_RENDERER_BEAN);
            summaryViewSectionRenderer = CustomRendererClassUtil.createRenderer(sectionRenderer, sectionRenderer,
                            NotifyingWidgetComponentRenderer.class);
        }
        return summaryViewSectionRenderer;
    }


    protected NotifyingWidgetComponentRenderer<HtmlBasedComponent, SummaryView, Object> getThumbnailRenderer()
    {
        return (NotifyingWidgetComponentRenderer<HtmlBasedComponent, SummaryView, Object>)getSummaryViewThumbnailRenderer();
    }


    /**
     * @deprecated since 6.5
     * @see #getSectionRenderer()
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected WidgetComponentRenderer<HtmlBasedComponent, SummaryView, Object> getSummaryViewThumbnailRenderer()
    {
        if(summaryViewThumbnailRenderer == null)
        {
            final String imageRenderer = (String)getWidgetSettings().getOrDefault(SETTING_DEFAULT_IMAGE_RENDERER,
                            DEFAULT_THUMBNAIL_RENDERER_BEAN);
            summaryViewThumbnailRenderer = CustomRendererClassUtil.createRenderer(imageRenderer, imageRenderer,
                            NotifyingWidgetComponentRenderer.class);
        }
        return summaryViewThumbnailRenderer;
    }
}
