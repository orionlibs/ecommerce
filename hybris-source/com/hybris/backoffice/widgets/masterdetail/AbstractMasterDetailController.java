/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.masterdetail;

import com.hybris.backoffice.masterdetail.MDMasterLogic;
import com.hybris.backoffice.masterdetail.MasterDetailService;
import com.hybris.backoffice.masterdetail.SettingButton;
import com.hybris.backoffice.masterdetail.SettingItem;
import com.hybris.backoffice.widgets.viewswitcher.ViewsSwitchedData;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.engine.impl.ListContainerCloseListener;
import com.hybris.cockpitng.util.CockpitComponentsUtils;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public abstract class AbstractMasterDetailController extends DefaultWidgetController implements MDMasterLogic
{
    protected static final String SOCKET_OUTPUT_CLOSE = "close";
    protected static final String SOCKET_OUTPUT_SELECT_VIEW = "selectView";
    protected static final String SOCKET_INPUT_VIEW_SWITCHED = "viewSwitched";
    protected static final String SAVE_BUTTON = "saveButton";
    protected static final String SAVE_AND_CLOSE_BUTTON = "saveAndCloseButton";
    protected static final String CLOSE_BUTTON = "closeButton";
    protected static final String CANCEL_BUTTON = "cancelButton";
    protected static final String SCLASS_YW_MODAL_MASTER_DETAIL = "yw-modal-masterdetail";
    private static final String CONFIRM_UNSAVED_MSG = "masterdetail.unsaved.msg";
    private static final String CONFIRM_UNSAVED_TITLE = "masterdetail.unsaved.title";
    private static final String MASTER_DETAIL_TITLE = "masterdetail.widget.title";
    private static final String Label_SAVE_BUTTON = "masterdetail.button.save";
    private static final String Label_SAVE_AND_CLOSE_BUTTON = "masterdetail.button.saveclose";
    private static final String Label_CANCEL_BUTTON = "masterdetail.button.cancel";
    private static final String Label_CLOSE_BUTTON = "masterdetail.button.close";
    @Wire
    protected Button saveButton;
    @Wire
    protected Button saveAndCloseButton;
    @Wire
    protected Button cancelButton;
    @Wire
    protected Button closeButton;
    @Wire
    protected Div itemContainer;
    private List<NavigationItem> navigationItems = new ArrayList<>();
    private NavigationItem currentNavItem;
    protected MasterDetailService masterDetailService;
    @WireVariable
    private transient ListContainerCloseListener listContainerCloseListener;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        findTemplateWindow().ifPresent(window -> {
            window.setClosable(false);
            if(window.getCaption() != null)
            {
                window.getCaption().setLabel(Labels.getLabel(MASTER_DETAIL_TITLE));
            }
            window.addSclass(SCLASS_YW_MODAL_MASTER_DETAIL);
        });
        saveButton.setLabel(Labels.getLabel(Label_SAVE_BUTTON));
        saveAndCloseButton.setLabel(Labels.getLabel(Label_SAVE_AND_CLOSE_BUTTON));
        cancelButton.setLabel(Labels.getLabel(Label_CANCEL_BUTTON));
        closeButton.setLabel(Labels.getLabel(Label_CLOSE_BUTTON));
        getMasterDetailService().registerMaster(this);
    }


    public void addItems(final List<SettingItem> settingItemList)
    {
        if(CollectionUtils.isEmpty(settingItemList))
        {
            return;
        }
        settingItemList.stream().forEach(data -> {
            final NavigationItem navigationItem = new NavigationItem(itemContainer, data);
            navigationItem.addEventListener(Events.ON_CLICK, event -> onClick(navigationItem));
            navigationItems.add(navigationItem);
        });
        final NavigationItem defaultActiveItem = navigationItems.stream().filter(NavigationItem::isActive).findFirst()
                        .orElse(navigationItems.get(0));
        selectView(defaultActiveItem);
    }


    private void onClick(final NavigationItem navigationItem)
    {
        if(currentNavItem != navigationItem)
        {
            confirmBeforeLeavingView(() -> selectView(navigationItem));
        }
    }


    private void selectView(final NavigationItem navigationItem)
    {
        sendOutput(SOCKET_OUTPUT_SELECT_VIEW, navigationItem.getId());
    }


    @SocketEvent(socketId = SOCKET_INPUT_VIEW_SWITCHED)
    public void viewSwitched(final ViewsSwitchedData viewsSwitchedData)
    {
        if(viewsSwitchedData == null || CollectionUtils.isEmpty(navigationItems))
        {
            return;
        }
        if(currentNavItem != null)
        {
            currentNavItem.removeSelectedStyle();
            getMasterDetailService().resetDetail(currentNavItem.getId());
        }
        final String selectedView = viewsSwitchedData.getSelectedViews().stream().findFirst().orElse(null);
        if(selectedView != null)
        {
            final NavigationItem navigationItem = navigationItems.stream().filter(item -> selectedView.equals(item.getId()))
                            .findFirst().orElse(null);
            if(navigationItem != null)
            {
                navigationItem.setSelectedStyle();
                initialButtons(navigationItem.getSettingButtons());
                currentNavItem = navigationItem;
            }
            else
            {
                final NavigationItem defaultActiveItem = navigationItems.stream().filter(NavigationItem::isActive).findFirst()
                                .orElse(navigationItems.get(0));
                selectView(defaultActiveItem);
            }
        }
    }


    protected void initialButtons(final List<SettingButton> buttons)
    {
        hideAllButtons();
        if(buttons == null || buttons.isEmpty())
        {
            return;
        }
        buttons.stream().forEach(settingBtn -> {
            final Button btn;
            switch(settingBtn.getType())
            {
                case SAVE:
                    btn = saveButton;
                    break;
                case SAVE_AND_CLOSE:
                    btn = saveAndCloseButton;
                    break;
                case CANCEL:
                    btn = cancelButton;
                    break;
                default:
                    btn = closeButton;
                    break;
            }
            btn.setVisible(settingBtn.isVisible());
            btn.setDisabled(settingBtn.isDisabled());
        });
    }


    public void enableSave(final boolean canSave)
    {
        saveButton.setDisabled(!canSave);
        saveAndCloseButton.setDisabled(!canSave);
    }


    public void updateItem(final SettingItem data)
    {
        if(currentNavItem != null)
        {
            currentNavItem.updateItem(data);
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = SAVE_BUTTON)
    public void onSave()
    {
        performSave(false);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = SAVE_AND_CLOSE_BUTTON)
    public void onSaveAndClose()
    {
        performSave(true);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CANCEL_BUTTON)
    public void onCancel()
    {
        confirmBeforeLeavingView(this::onClose);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CLOSE_BUTTON)
    public void onClose()
    {
        getMasterDetailService().reset();
        sendOutput(SOCKET_OUTPUT_CLOSE, (Object)null);
    }


    protected void performSave(final boolean closeAfterSave)
    {
        if(currentNavItem != null)
        {
            final boolean needRefresh = getMasterDetailService().needRefreshUI(currentNavItem.getId());
            if(!getMasterDetailService().saveDetail(currentNavItem.getId()))
            {
                return;
            }
            if(closeAfterSave && needRefresh)
            {
                findTemplateWindow().ifPresent(window -> {
                    final var closeEvent = new Event(Events.ON_CLOSE, window);
                    getListContainerCloseListener().onClose(closeEvent, getWidgetslot().getWidgetInstance());
                    getMasterDetailService().reset();
                    window.onClose();
                    doRefreshTheUI();
                });
                return;
            }
            if(closeAfterSave)
            {
                onClose();
            }
            if(needRefresh)
            {
                doRefreshTheUI();
            }
        }
    }


    protected void hideAllButtons()
    {
        saveButton.setVisible(false);
        saveAndCloseButton.setVisible(false);
        cancelButton.setVisible(false);
        closeButton.setVisible(false);
    }


    protected void confirmBeforeLeavingView(final Executable onIgnore)
    {
        if(currentNavItem != null && getMasterDetailService().isDetailDataChanged(currentNavItem.getId()))
        {
            Messagebox.show(Labels.getLabel(CONFIRM_UNSAVED_MSG), Labels.getLabel(CONFIRM_UNSAVED_TITLE), new Messagebox.Button[]
                            {Messagebox.Button.YES, Messagebox.Button.CANCEL}, Messagebox.QUESTION, event -> {
                if(Messagebox.Button.YES == event.getButton())
                {
                    onIgnore.execute();
                }
            });
        }
        else
        {
            onIgnore.execute();
        }
    }


    @Required
    public void setMasterDetailService(final MasterDetailService masterDetailService)
    {
        this.masterDetailService = masterDetailService;
    }


    protected abstract MasterDetailService getMasterDetailService();


    public ListContainerCloseListener getListContainerCloseListener()
    {
        return listContainerCloseListener;
    }


    public void setListContainerCloseListener(final ListContainerCloseListener listContainerCloseListener)
    {
        this.listContainerCloseListener = listContainerCloseListener;
    }


    protected List<NavigationItem> getNavigationItems()
    {
        return this.navigationItems;
    }


    protected NavigationItem getCurrentNavItem()
    {
        return currentNavItem;
    }


    protected void setCurrentNavItem(final NavigationItem currentNavItem)
    {
        this.currentNavItem = currentNavItem;
    }


    /**
     * Tries to find parent window for this template widget
     *
     * @return window object if the widget is a template, {@link Optional#empty()} otherwise
     */
    protected Optional<Window> findTemplateWindow()
    {
        return CockpitComponentsUtils.findClosestComponent(getWidgetslot(), Window.class,
                        getWidgetSettings().getString(SCLASS_YW_MODAL_MASTER_DETAIL));
    }


    protected void doRefreshTheUI()
    {
        Clients.showBusy(null);
        Executions.sendRedirect(null);
    }
}
