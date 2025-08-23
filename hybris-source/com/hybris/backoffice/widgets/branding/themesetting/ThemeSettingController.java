/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.themesetting;

import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.BackofficeThemeLevel;
import com.hybris.backoffice.theme.BackofficeThemeService;
import com.hybris.backoffice.theme.ThemeNotFound;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.servicelayer.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;

public class ThemeSettingController extends DefaultWidgetController
{
    private static final Logger LOG = LoggerFactory.getLogger(ThemeSettingController.class);
    protected static final String SOCKET_INPUT_CUSTOM_THEME_CHANGED = "customThemeChanged";
    protected static final String SAVE_BUTTON = "saveButton";
    protected static final String CANCEL_BUTTON = "cancelButton";
    protected static final String NOTIFICATION_AREA = "themeSetting";
    protected static final String NOTIFICATION_TYPE_THEME_CHANGED = "themeChanged";
    private static final String CONFIRM_UNSAVED_MSG = "themesetting.unsaved.msg";
    private static final String CONFIRM_UNSAVED_TITLE = "themesetting.unsaved.title";
    protected transient ListModelList<ThemeModel> availableSystemThemes;
    protected transient ListModelList<ThemeModel> availableUserThemes;
    protected transient ThemeModel currentSystemTheme;
    protected transient ThemeModel currentUserTheme;
    protected BackofficeThemeLevel currentThemeLevel;
    @WireVariable
    private transient BackofficeThemeService backofficeThemeService;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient UserService userService;
    @Wire
    protected Listbox systemThemeList;
    @Wire
    protected Listbox userThemeList;
    @Wire
    protected Div systemThemeContent;
    @Wire
    protected Div userThemeContent;
    @Wire
    protected Radiogroup themeLevelRadioGroup;
    @Wire
    protected Radio systemRadio;
    @Wire
    protected Radio userRadio;
    @Wire
    protected Button saveButton;
    @Wire
    protected Button cancelButton;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        availableSystemThemes = new ListModelList<>();
        systemThemeList.setModel(availableSystemThemes);
        availableUserThemes = new ListModelList<>();
        userThemeList.setModel(availableUserThemes);
        reset();
    }


    protected void reset()
    {
        currentThemeLevel = getBackofficeThemeService().getThemeLevel();
        currentSystemTheme = getBackofficeThemeService().getSystemTheme();
        currentUserTheme = getBackofficeThemeService().getUserLevelDefaultTheme();
        final var isSystemLevel = BackofficeThemeLevel.SYSTEM == currentThemeLevel;
        themeLevelRadioGroup.setSelectedItem(isSystemLevel ? systemRadio : userRadio);
        systemThemeContent.setVisible(isSystemLevel);
        userThemeContent.setVisible(!isSystemLevel);
        resetThemeList(availableSystemThemes, currentSystemTheme);
        resetThemeList(availableUserThemes, currentUserTheme);
        enableSave(false);
    }


    private void resetThemeList(final ListModelList<ThemeModel> themeListModel, final ThemeModel selectedTheme)
    {
        if(themeListModel == null)
        {
            return;
        }
        themeListModel.clear();
        themeListModel.clearSelection();
        themeListModel.addAll(getBackofficeThemeService().getAvailableThemes());
        themeListModel.addToSelection(selectedTheme);
    }


    private void enableSave(final boolean enabled)
    {
        saveButton.setDisabled(!enabled);
        cancelButton.setDisabled(!enabled);
    }


    public void onThemeClick(final ThemeModel theme)
    {
        if((BackofficeThemeLevel.SYSTEM == currentThemeLevel && theme == currentSystemTheme)
                        || (BackofficeThemeLevel.USER == currentThemeLevel && theme == currentUserTheme))
        {
            return;
        }
        enableSave(true);
    }


    public void onThemeLevelCheck(final String levelName)
    {
        currentThemeLevel = BackofficeThemeLevel.valueOf(levelName);
        systemThemeContent.setVisible(BackofficeThemeLevel.SYSTEM == currentThemeLevel);
        userThemeContent.setVisible(BackofficeThemeLevel.USER == currentThemeLevel);
        enableSave(true);
    }


    @SocketEvent(socketId = SOCKET_INPUT_CUSTOM_THEME_CHANGED)
    public void onCustomThemeChanged()
    {
        reset();
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = SAVE_BUTTON)
    public void onSave()
    {
        if(!getUserService().isAdmin(getUserService().getCurrentUser()))
        {
            return;
        }
        try
        {
            getBackofficeThemeService().setThemeLevel(currentThemeLevel);
            if(BackofficeThemeLevel.SYSTEM == currentThemeLevel)
            {
                final ThemeModel selectedTheme = systemThemeList.getSelectedItem().getValue();
                getBackofficeThemeService().setSystemTheme(selectedTheme.getCode());
            }
            else
            {
                final ThemeModel selectedTheme = userThemeList.getSelectedItem().getValue();
                getBackofficeThemeService().setUserLevelDefaultTheme(selectedTheme.getCode());
            }
            getNotificationService().notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_THEME_CHANGED, NotificationEvent.Level.SUCCESS);
            doRefreshTheUI();
        }
        catch(final ThemeNotFound e)
        {
            LOG.error("Could not save backoffice theme", e);
            getNotificationService().notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_THEME_CHANGED, NotificationEvent.Level.FAILURE);
        }
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = CANCEL_BUTTON)
    public void onCancel()
    {
        Messagebox.show(getLabel(CONFIRM_UNSAVED_MSG), getLabel(CONFIRM_UNSAVED_TITLE), new Messagebox.Button[]
                        {Messagebox.Button.YES, Messagebox.Button.CANCEL}, Messagebox.QUESTION, event -> {
            if(Messagebox.Button.YES == event.getButton())
            {
                reset();
            }
        });
    }


    protected void doRefreshTheUI()
    {
        Clients.showBusy(null);
        Executions.sendRedirect(null);
    }


    public BackofficeThemeService getBackofficeThemeService()
    {
        return this.backofficeThemeService;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public UserService getUserService()
    {
        return userService;
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public void setBackofficeThemeService(final BackofficeThemeService backofficeThemeService)
    {
        this.backofficeThemeService = backofficeThemeService;
    }
}
