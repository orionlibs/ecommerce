/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common;

import com.google.common.base.Supplier;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.HyperlinkFallbackLabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldContext;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.ListViewCollectionBrowserMoldStrategy;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRendererFactory;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererFactory;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.Collection;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.spel.SpelEvaluationException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;

public abstract class AbstractMoldStrategy<I extends Component, C, D> implements CollectionBrowserMoldStrategy
{
    public static final int EMPTY_PAGE_SIZE = 0;
    public static final int NO_SELECTION_INDEX = -1;
    public static final String ATTRIBUTE_HYPERLINK_CANDIDATE = "hyperlink-candidate";
    public static final String MODEL_COLUMNS_CONFIG = "columnConfig";
    public static final String KEY_LABEL_EMPTY_LIST = "emptylist";
    public static final NavigationItemSelectorContext EMPTY_NAVIGATION_ITEM_SELECTOR_CONTEXT = new NavigationItemSelectorContext(
                    AbstractMoldStrategy.EMPTY_PAGE_SIZE, AbstractMoldStrategy.NO_SELECTION_INDEX);
    public static final String SETTING_COCKPIT_COLLECTION_BROWSER_ENTITY_SPECIFIC_PREVIEW_RESOLUTION_ENABLED = "cockpit.collection.browser.entity.specific.preview.resolution.enabled";
    protected static final String SCLASS_CELL_HYPERLINK = "yw-coll-browser-hyperlink";
    protected static final String SCLASS_CELL_FOCUSED = "yw-coll-browser-focused";
    private static final String ATTRIBUTE_HYPERLINK = "hyperlink-component";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractMoldStrategy.class);
    private PermissionFacade permissionFacade;
    private BackofficeTypeUtils backofficeTypeUtils;
    private HyperlinkFallbackLabelProvider hyperlinkFallbackLabelProvider;
    private LabelService labelService;
    private NotificationService notificationService;
    private CockpitProperties cockpitProperties;
    private String typeCode;
    private int order;
    private CollectionBrowserMoldContext context;
    private volatile NotifyingWidgetComponentRenderer<I, C, D> renderer;
    private NotifyingWidgetComponentRendererFactory<I, C, D> rendererFactory;


    @Override
    public void handleObjectUpdateEvent(final CockpitEvent event)
    {
        if(canHandle(event))
        {
            handleCollectionUpdate(event.getDataAsCollection());
        }
    }


    @Override
    public void handleObjectsUpdateEvent(final CockpitEvent event)
    {
        if(canHandle(event))
        {
            handleCollectionUpdate(event.getDataAsCollection());
        }
    }


    protected abstract void handleCollectionUpdate(final Collection<Object> eventDataAsCollection);


    protected boolean canHandle(final CockpitEvent event)
    {
        return event != null && event.getData() != null && event.getDataAsCollection() != null;
    }


    protected String getRendererSetting()
    {
        throw new UnsupportedOperationException("Renderer setting name not set. Override #getRendererSetting() method!");
    }


    /**
     * @deprecated since 6.5
     * @see #initializeRenderer(NotifyingWidgetComponentRenderer)
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected void initializeRenderer(final WidgetComponentRenderer<I, C, D> renderer)
    {
        // do nothing
    }


    protected void initializeRenderer(final NotifyingWidgetComponentRenderer<I, C, D> renderer)
    {
        initializeRenderer((WidgetComponentRenderer)renderer);
    }


    /**
     * Returns the list view renderer based on the value of the
     * {@value ListViewCollectionBrowserMoldStrategy#SETTING_ITEM_RENDERER} widget setting.
     *
     * If the setting value contains one or more '.' this method will assume that it's a class and try instantiate the
     * renderer accordingly. If there are no '.' it will assume that it's a Spring bean ID.
     *
     * @return list view renderer
     */
    public WidgetComponentRenderer<I, C, D> getRenderer()
    {
        if(this.renderer == null)
        {
            final String configuredRenderer = getContext().getWidgetInstanceManager().getWidgetSettings()
                            .getString(getRendererSetting());
            final NotifyingWidgetComponentRenderer<I, C, D> newRenderer = rendererFactory
                            .createWidgetComponentRenderer(configuredRenderer);
            initializeRenderer(newRenderer);
            synchronized(this)
            {
                if(this.renderer == null && newRenderer != null)
                {
                    this.renderer = newRenderer;
                }
            }
        }
        return this.renderer;
    }


    /**
     * Sets the list view renderer used for rendering rows.
     *
     * @param renderer
     *           list view renderer
     * @deprecated since 6.5
     * @see #setMoldRendererFactory(NotifyingWidgetComponentRendererFactory)
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public void setRenderer(final WidgetComponentRenderer<I, C, D> renderer)
    {
        if(renderer instanceof NotifyingWidgetComponentRenderer)
        {
            initializeRenderer((NotifyingWidgetComponentRenderer)renderer);
            this.renderer = (NotifyingWidgetComponentRenderer<I, C, D>)renderer;
        }
    }


    protected void addLink(final HtmlBasedComponent component, final Supplier<Object> value)
    {
        final HtmlBasedComponent link;
        final Component candidate = component.query(String.format("[%s=true]", ATTRIBUTE_HYPERLINK_CANDIDATE));
        if(candidate instanceof HtmlBasedComponent)
        {
            link = (HtmlBasedComponent)candidate;
        }
        else
        {
            link = component;
        }
        link.addEventListener(Events.ON_CLICK, event -> {
            try
            {
                getContext().notifyHyperlinkClicked(value.get());
            }
            catch(final SpelEvaluationException ex)
            {
                LOG.error(ex.getLocalizedMessage(), ex);
                getNotificationService().notifyUser(context.getWidgetInstanceManager(), NotificationEventTypes.EVENT_TYPE_GENERAL,
                                Level.FAILURE);
            }
        });
        link.setAttribute(ATTRIBUTE_HYPERLINK, Boolean.TRUE);
        UITools.modifySClass(link, SCLASS_CELL_HYPERLINK, true);
        if(link instanceof Label)
        {
            final Label label = (Label)link;
            label.setValue(getHyperlinkFallbackLabelProvider().getFallback(label.getValue()));
        }
    }


    protected boolean isLink(final Component component)
    {
        final Object hyperlinkAttribute = component.getAttribute(ATTRIBUTE_HYPERLINK);
        return hyperlinkAttribute instanceof Boolean && ((Boolean)hyperlinkAttribute).booleanValue();
    }


    @Override
    public void previousItemSelectorInvocation()
    {
        final SinglePage singlePage = getContext().getCurrentPage();
        final int indexOfFocusedItem = getIndexOf(getContext().getFocusedItem());
        if(singlePage != null && singlePage.isListPresent() && indexOfFocusedItem > 0)
        {
            final Object itemToFocus = singlePage.getList().get(indexOfFocusedItem - 1);
            handleChangeFocus(itemToFocus);
        }
    }


    @Override
    public void nextItemSelectorInvocation()
    {
        final SinglePage singlePage = getContext().getCurrentPage();
        if(singlePage != null && singlePage.isListPresent())
        {
            final int indexOfFocusedItem = getIndexOf(getContext().getFocusedItem());
            final boolean isItemNotLast = indexOfFocusedItem < singlePage.getList().size() - 1;
            final boolean itemsExist = singlePage.isListPresent();
            if(isItemNotLast && itemsExist)
            {
                final Object itemToFocus = singlePage.getList().get(indexOfFocusedItem + 1);
                handleChangeFocus(itemToFocus);
            }
        }
    }


    /**
     * @param element
     *           to lookup in the UI model
     * @return the index of the element or -1 if not found
     */
    protected int getIndexOf(final Object element)
    {
        if(element == null)
        {
            return -1;
        }
        for(int index = 0; index < getUiModelSize(); index++)
        {
            final Optional<Object> optionalUiElement = getUiElementAt(index);
            if(optionalUiElement.isPresent() && element.equals(optionalUiElement.get()))
            {
                return index;
            }
        }
        return -1;
    }


    @Override
    public void setPage(final SinglePage singlePage)
    {
        Validate.notNull("Page may not be null", singlePage);
    }


    protected String chooseEmptyMessageToDisplayFor(final SinglePage singlePage)
    {
        if(getPermissionFacade().canReadType(singlePage.getTypeCode()))
        {
            return getContext().getWidgetInstanceManager().getLabel(KEY_LABEL_EMPTY_LIST);
        }
        return getLabelService().getAccessDeniedLabel(null);
    }


    /**
     * @return true in case when the preview should be resolved based on single entity type rather than the type defined in
     *         the context (usually super-type)
     * @see AbstractMoldStrategy#SETTING_COCKPIT_COLLECTION_BROWSER_ENTITY_SPECIFIC_PREVIEW_RESOLUTION_ENABLED
     */
    protected boolean isEntitySpecificPreviewResolutionEnabled()
    {
        return getCockpitProperties().getBoolean(SETTING_COCKPIT_COLLECTION_BROWSER_ENTITY_SPECIFIC_PREVIEW_RESOLUTION_ENABLED,
                        false);
    }


    /**
     * @param data
     *           entity for which the type should be resolved
     * @return actual data type in case {@link #isEntitySpecificPreviewResolutionEnabled()} returns true, otherwise the type
     *         is taken from context
     */
    protected DataType getDataType(final Object data)
    {
        if(isEntitySpecificPreviewResolutionEnabled())
        {
            try
            {
                final String type = getBackofficeTypeUtils().getTypeFacade().getType(data);
                return getBackofficeTypeUtils().getTypeFacade().load(type);
            }
            catch(final TypeNotFoundException e)
            {
                LOG.info(String.format("Could not resolve type of entity: %s. Using fallback type.", data));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(String.format("Could not resolve type of entity: %s. Using fallback type.", data), e);
                }
            }
        }
        return getDataType();
    }


    /**
     * @return data type of the collection as defined in the context
     */
    protected DataType getDataType()
    {
        return getContext().getCurrentType();
    }


    protected int getUiModelSize()
    {
        return 0;
    }


    protected Optional<Object> getUiElementAt(final int index)
    {
        return Optional.empty();
    }


    protected void handleChangeFocus(final Object itemToFocus)
    {
        getContext().notifyItemClicked(itemToFocus);
    }


    protected CollectionBrowserMoldContext getContext()
    {
        return context;
    }


    @Override
    public void setContext(final CollectionBrowserMoldContext context)
    {
        this.context = context;
    }


    public String getTypeCode()
    {
        return typeCode;
    }


    public void setTypeCode(final String typeCode)
    {
        this.typeCode = typeCode;
    }


    public boolean isHandlingObjectEvents(final String typeCode)
    {
        return getTypeCode() != null && getBackofficeTypeUtils().isAssignableFrom(getTypeCode(), typeCode);
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


    protected Optional<Object> getFocusedObjectFromModel()
    {
        return Optional.ofNullable(getContext().getFocusedItem());
    }


    protected WidgetModel getWidgetModel()
    {
        return getContext().getWidgetInstanceManager().getModel();
    }


    public int getOrder()
    {
        return order;
    }


    @Required
    public void setOrder(final int order)
    {
        this.order = order;
    }


    public BackofficeTypeUtils getBackofficeTypeUtils()
    {
        return backofficeTypeUtils;
    }


    @Required
    public void setBackofficeTypeUtils(final BackofficeTypeUtils backofficeTypeUtils)
    {
        this.backofficeTypeUtils = backofficeTypeUtils;
    }


    public void setMoldRendererFactory(final NotifyingWidgetComponentRendererFactory<I, C, D> rendererFactory)
    {
        this.rendererFactory = rendererFactory;
    }


    /**
     * @deprecated since 6.5
     * @see #setMoldRendererFactory(NotifyingWidgetComponentRendererFactory)
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public void setRendererFactory(final WidgetComponentRendererFactory<I, C, D> rendererFactory)
    {
        if(rendererFactory instanceof NotifyingWidgetComponentRendererFactory)
        {
            setMoldRendererFactory((NotifyingWidgetComponentRendererFactory)rendererFactory);
        }
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


    public HyperlinkFallbackLabelProvider getHyperlinkFallbackLabelProvider()
    {
        return hyperlinkFallbackLabelProvider;
    }


    @Required
    public void setHyperlinkFallbackLabelProvider(final HyperlinkFallbackLabelProvider hyperlinkFallbackLabelProvider)
    {
        this.hyperlinkFallbackLabelProvider = hyperlinkFallbackLabelProvider;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }
}
