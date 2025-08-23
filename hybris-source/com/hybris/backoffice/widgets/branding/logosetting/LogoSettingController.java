/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.logosetting;

import com.hybris.backoffice.media.BackofficeMediaConstants;
import com.hybris.backoffice.media.MediaUtil;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;

public class LogoSettingController extends DefaultWidgetController
{
    protected static final String SOCKET_OUTPUT_LOGO_CHANGED = "logoChanged";
    protected static final String NOTIFICATION_AREA = "logoSetting";
    protected static final String NOTIFICATION_TYPE_LOGO_CHANGED = "logoChanged";
    protected static final String SAVE_BUTTON = "saveButton";
    protected static final String CANCEL_BUTTON = "cancelButton";
    private static final String CONFIRM_UNSAVED_MSG = "logosetting.unsaved.msg";
    private static final String CONFIRM_UNSAVED_TITLE = "logosetting.unsaved.title";
    @WireVariable
    private transient UserService userService;
    @WireVariable
    private transient MediaUtil backofficeMediaUtil;
    @WireVariable
    private transient NotificationService notificationService;
    protected List<LogoSettingItem> logoSettingItems;
    @Wire
    protected Div logoSettingDiv;
    @Wire
    protected Button saveButton;
    @Wire
    protected Button cancelButton;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final LogoSettingItem loginLogo = new LogoSettingItem(logoSettingDiv,
                        BackofficeMediaConstants.BACKOFFICE_LOGINPAGE_LOGO_CODE,
                        String.format("%s%s", getWidgetRoot(), "/images/logo_transparent.png"), getLabel("logo.setting.loginpage.label"),
                        this);
        final LogoSettingItem shellBarLogo = new LogoSettingItem(logoSettingDiv,
                        BackofficeMediaConstants.BACKOFFICE_SHELLBAR_LOGO_CODE,
                        String.format("%s%s", getWidgetRoot(), "/images/logo-sap.png"), getLabel("logo.setting.shellbar.label"), this);
        logoSettingItems = Arrays.asList(loginLogo, shellBarLogo);
        enableSave(false);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = SAVE_BUTTON)
    public void onSave()
    {
        if(!getUserService().isAdmin(getUserService().getCurrentUser()))
        {
            return;
        }
        final boolean isSuccess = logoSettingItems.stream().allMatch(LogoSettingItem::save);
        getNotificationService().notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_LOGO_CHANGED,
                        isSuccess ? NotificationEvent.Level.SUCCESS : NotificationEvent.Level.FAILURE);
        if(isSuccess)
        {
            enableSave(false);
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


    protected void reset()
    {
        logoSettingItems.forEach(LogoSettingItem::reset);
        enableSave(false);
    }


    protected void enableSave(final boolean enabled)
    {
        saveButton.setDisabled(!enabled);
        cancelButton.setDisabled(!enabled);
    }


    protected void onLogoSaved(final String logoCode, final MediaModel mediaModel)
    {
        if(BackofficeMediaConstants.BACKOFFICE_SHELLBAR_LOGO_CODE.equals(logoCode))
        {
            sendOutput(SOCKET_OUTPUT_LOGO_CHANGED, mediaModel);
        }
    }


    public MediaUtil getBackofficeMediaUtil()
    {
        return backofficeMediaUtil;
    }


    public void setBackofficeMediaUtil(final MediaUtil backofficeMediaUtil)
    {
        this.backofficeMediaUtil = backofficeMediaUtil;
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
}

