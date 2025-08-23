/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.search.data.SortData;
import com.hybris.cockpitng.search.data.pageable.FullTextSearchPageable;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.CollectionBrowserDelegateController;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

public class PagingDelegateController implements CollectionBrowserDelegateController
{
    public static final String MODEL_SINGLE_PAGE = "singlePage";
    public static final String SETTING_ASYNC_LOADING = "asyncLoading";
    public static final String OPERATION_DONE_EVENT = "onOperationDone";
    private static final Logger LOG = LoggerFactory.getLogger(PagingDelegateController.class);
    private CollectionBrowserController controller;
    private NotificationService notificationService;


    public PagingDelegateController()
    {
    }


    /**
     * @deprecated since 6.7 - not used anymore, please use @link{{@link #PagingDelegateController()}}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public PagingDelegateController(final CollectionBrowserController controller)
    {
        this.controller = controller;
    }


    public void initializeOnPagingListener()
    {
        controller.getPaging().addEventListener(ZulEvents.ON_PAGING, (final PagingEvent event) -> {
            onPagingHandler(event);
            controller.getSelectAndFocusDelegateController().handlePaging(event);
        });
    }


    public void onPagingHandler(final PagingEvent event)
    {
        final org.zkoss.zul.ext.Pageable zulPageable = event.getPageable();
        if(zulPageable != null)
        {
            final Pageable<?> currentPageable = getCurrentPageable();
            currentPageable.setPageNumber(zulPageable.getActivePage());
            loadPage(currentPageable);
        }
    }


    public void loadPage(final Pageable<?> pageable)
    {
        loadPage(pageable, null);
    }


    public <E> void loadPage(final Pageable<E> pageable, final Consumer<Pageable<E>> callback)
    {
        fetchPage(pageable, event -> displayFetchedData(pageable, callback));
    }


    protected <E> void displayFetchedData(final Pageable<E> pageable, final Consumer<Pageable<E>> callback)
    {
        final WidgetModel model = controller.getModel();
        model.setValue(CollectionBrowserController.MODEL_PAGEABLE, pageable);
        final String previousTypeCode = controller.getCurrentTypeCode();
        final String typeCode = pageable.getTypeCode();
        if(ObjectUtils.notEqual(previousTypeCode, typeCode))
        {
            controller.initializeDataType(typeCode);
            onTypeChanged();
        }
        if(pageable instanceof FullTextSearchPageable)
        {
            ((FullTextSearchPageable)pageable).onPageLoaded();
        }
        final List<?> currentPage = pageable.getCurrentPage();
        final SortData sortData = pageable.getSortData();
        final int pageSize = pageable.getPageSize();
        final int totalCount = pageable.getTotalCount();
        final SinglePage singlePage = new SinglePage(currentPage, typeCode, sortData, pageSize);
        model.setValue(MODEL_SINGLE_PAGE, singlePage);
        model.setValue(CollectionBrowserController.MODEL_LAST_ACTIVE_PAGE_NUMBER, pageable.getPageNumber());
        if(CollectionBrowserController.EMPTY_MOLD.equals(controller.getActiveMold()))
        {
            resetActionSlot();
            resetPaging();
        }
        else
        {
            buildPaging(pageable);
        }
        if(callback != null)
        {
            callback.accept(pageable);
        }
        controller.getActiveMold().setPage(getCurrentSinglePage());
        controller.getTitleDelegateController().updateTitle(typeCode, totalCount);
    }


    protected void resetActionSlot()
    {
        controller.getActionSlot().setConfig(null);
        controller.getActionSlot().reload();
    }


    public int getPageableCurrentPageSize()
    {
        final Pageable<?> pageable = getCurrentPageable();
        if(pageable != null && pageable.getCurrentPage() != null)
        {
            return pageable.getCurrentPage().size();
        }
        else
        {
            return 0;
        }
    }


    public void fetchPage(final Pageable<?> pageable, final EventListener<Event> operationDoneListener)
    {
        if(controller.getWidgetSettings().getBoolean(SETTING_ASYNC_LOADING))
        {
            controller.callExecuteOperation(new Operation()
            {
                @Override
                public boolean isTerminable()
                {
                    return false;
                }


                @Override
                public Progress.ProgressType getProgressType()
                {
                    return Progress.ProgressType.FAKED;
                }


                @Override
                public String getLabel()
                {
                    return controller.getLabel("loading");
                }


                @Override
                public Object execute(final Progress progress)
                {
                    return pageable == null ? null : pageable.getCurrentPage();
                }
            }, operationDoneListener, null);
        }
        else
        {
            try
            {
                final List<?> currentPage = pageable == null ? null : pageable.getCurrentPage();
                final Event event = new Event(OPERATION_DONE_EVENT, null, currentPage);
                operationDoneListener.onEvent(event);
            }
            catch(final Exception e)
            {
                LOG.error("Could not fetch record page.", e);
            }
        }
    }


    public SinglePage getCurrentSinglePage()
    {
        final WidgetModel widgetModel = controller.getModel();
        final SinglePage singlePage = widgetModel.getValue(MODEL_SINGLE_PAGE, SinglePage.class);
        return (singlePage == null) ? SinglePage.EMPTY : singlePage;
    }


    /**
     * Gets current single page with type code. In case returned single page was empty then a new instance is being returned
     * with type code passed as a parameter.
     *
     * @param typeCode
     *           to pass to a new instance of single page in case returned one was empty
     * @return single page initialized with type code
     */
    public SinglePage getCurrentSinglePageWithTypeCode(final String typeCode)
    {
        final SinglePage originalSinglePage = getCurrentSinglePage();
        if(SinglePage.EMPTY.equals(originalSinglePage))
        {
            return new SinglePage(originalSinglePage.getList(), typeCode, originalSinglePage.getSortData(),
                            originalSinglePage.getPageSize());
        }
        return originalSinglePage;
    }


    /**
     * Builds up the paging control.
     *
     * @param pageable
     *           the pageable object containing paging info
     */
    public void buildPaging(final Pageable<?> pageable)
    {
        if(pageable != null && (pageable.hasNextPage() || pageable.hasPreviousPage()))
        {
            notifyPaging(pageable);
        }
        else
        {
            resetPaging();
        }
    }


    protected void resetPaging()
    {
        controller.getPaging().setVisible(false);
        controller.getBrowserContainer().setVflex("1");
        Clients.resize(controller.getBrowserContainer());
    }


    protected void notifyPaging(final Pageable<?> pageable)
    {
        final int firstPageIndex = 0;
        controller.getPaging().setVisible(true);
        controller.getPaging().setPageSize(pageable.getPageSize());
        final int pageNumber = pageable.getPageNumber();
        final int totalCount = pageable.getTotalCount();
        controller.getPaging().setTotalSize(totalCount);
        if(totalCount < pageNumber)
        {
            controller.getPaging().setActivePage(firstPageIndex);
            pageable.setPageNumber(firstPageIndex);
            refreshBrowser(pageable);
        }
        else
        {
            controller.getPaging().setActivePage(pageNumber);
        }
        controller.getBrowserContainer().setVflex("1");
        Clients.resize(controller.getBrowserContainer());
    }


    public void refreshBrowser(final Pageable<?> pageable)
    {
        refreshBrowser(pageable, null);
    }


    public <E> void refreshBrowser(final Pageable<E> pageable, final Consumer<Pageable<E>> callback)
    {
        pageable.refresh();
        loadPage(pageable, callback);
    }


    public void renderAndProcessCurrentPageable()
    {
        final Div browserContainer = controller.getBrowserContainer();
        final Pageable pageable = getCurrentPageable();
        final String typeCode = pageable != null ? pageable.getTypeCode() : StringUtils.EMPTY;
        final SinglePage singlePage = getCurrentSinglePageWithTypeCode(typeCode);
        controller.getActiveMold().render(browserContainer, singlePage);
        process(pageable);
    }


    public void process(final Pageable<?> pageable)
    {
        process(pageable, null);
    }


    public <E> void process(final Pageable<E> pageable, final Consumer<Pageable<E>> callback)
    {
        if(pageable != null)
        {
            try
            {
                restorePageNumberIfNecessary(pageable);
                loadPage(pageable, callback);
            }
            catch(final RuntimeException re)
            {
                handleRuntimeException(re);
                try
                {
                    controller.getActiveMold().setPage(SinglePage.EMPTY);
                }
                catch(final RuntimeException e)
                {
                    LOG.debug("RuntimeException has been thrown while EMPTY page was being set", e);
                }
            }
        }
        else
        {
            controller.reset();
        }
    }


    protected void handleRuntimeException(final RuntimeException re)
    {
        try
        {
            getNotificationService().notifyUser(getNotificationSource(), NotificationEventTypes.EVENT_TYPE_GENERAL,
                            NotificationEvent.Level.FAILURE, re);
        }
        finally
        {
            LOG.error(re.getLocalizedMessage(), re);
        }
    }


    /**
     * @deprecated since 6.7, use
     *             {@link NotificationService#getWidgetNotificationSource(com.hybris.cockpitng.engine.WidgetInstanceManager)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource()
    {
        return getNotificationService().getWidgetNotificationSource(controller.getWidgetInstanceManager());
    }


    protected void onTypeChanged()
    {
        controller.releaseAllMolds();
        controller.loadAvailableMolds();
        controller.initializeMoldSelector();
        controller.initializeActionSlot();
        controller.getSelectAndFocusDelegateController().handleTypeChange();
        final Div browserContainer = controller.getBrowserContainer();
        final Pageable pageable = getCurrentPageable();
        final String typeCode = pageable != null ? pageable.getTypeCode() : StringUtils.EMPTY;
        controller.getActiveMold().render(browserContainer, new SinglePage(typeCode));
    }


    public void restorePageNumberIfNecessary(final Pageable pageable)
    {
        final int lastActivePageNumber = pageable.getPageNumber();
        if(lastActivePageNumber < 0 || pageable.getTotalCount() < 1)
        {
            return;
        }
        final int numberOfPages = (int)Math.ceil((pageable.getTotalCount() / (double)pageable.getPageSize()));
        final int currentLastPageNumber = numberOfPages - 1;
        final boolean lastActivePageNoLongerExists = lastActivePageNumber > currentLastPageNumber;
        if(lastActivePageNoLongerExists)
        {
            pageable.setPageNumber(currentLastPageNumber);
        }
        else
        {
            pageable.setPageNumber(lastActivePageNumber);
        }
    }


    protected void adjustPageSize(final int currentPageSize, final int definedPageSize)
    {
        if(getCurrentPageable() != null && currentPageSize != definedPageSize)
        {
            getCurrentPageable().setPageNumber(0);
            getCurrentPageable().setPageSize(definedPageSize);
        }
    }


    public Pageable getCurrentPageable()
    {
        return controller.getValue(CollectionBrowserController.MODEL_PAGEABLE, Pageable.class);
    }


    protected CollectionBrowserController getController()
    {
        return controller;
    }


    @Override
    public void setController(final CollectionBrowserController controller)
    {
        this.controller = controller;
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
}
