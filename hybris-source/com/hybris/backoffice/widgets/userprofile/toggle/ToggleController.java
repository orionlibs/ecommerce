/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */

package com.hybris.backoffice.widgets.userprofile.toggle;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.quicktogglelocale.controller.IndexedLanguagesResolver;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;

public class ToggleController extends DefaultWidgetController
{
    protected static final String SOCKET_INPUT_AVATAR_CHANGED = "avatarChanged";
    protected static final String SOCKET_OUTPUT_OPEN_PROFILE_SETTINGS = "openProfileSettings";
    protected static final String SIGNOUT_BTN = "signOutBtn";
    protected static final String OPEN_PROFILE_SETTINGS_BTN = "openSettingsBtn";
    private static final String NOTIFICATION_EVENT_TYPE_SESSION_LANGUAGE_NOT_INDEXED = "sessionLanguageNotIndexed";
    private transient CockpitSessionService cockpitSessionService;
    @WireVariable
    private transient UserService userService;
    @Wire
    protected Label userNameLabel;
    @Wire
    protected Toolbarbutton userProfileBtn;
    private transient NotificationService notificationService;
    private transient CockpitLocaleService cockpitLocaleService;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final var isoCode = this.getCockpitLocaleService().getCurrentLocale().toString();
        this.triggerLanguageNotIndexedNotification(isoCode, true, comp);
        final UserModel currentUser = getUserService().getCurrentUser();
        final MediaModel userAvatar = currentUser.getAvatar();
        if(userAvatar != null)
        {
            userProfileBtn.setImage(userAvatar.getURL());
        }
        userNameLabel
                        .setValue(StringUtils.isNotBlank(currentUser.getDisplayName()) ? currentUser.getDisplayName() : currentUser.getUid());
    }


    protected void triggerLanguageNotIndexedNotification(final String languageCode, final Boolean isPostponeMode,
                    final Component comp)
    {
        final Map<String, IndexedLanguagesResolver> indexedLanguagesResolverMap = BackofficeSpringUtil
                        .getAllBeans(IndexedLanguagesResolver.class);
        if(null != indexedLanguagesResolverMap && !indexedLanguagesResolverMap.isEmpty())
        {
            indexedLanguagesResolverMap.values().stream().forEach(languagesResolver -> {
                if(!languagesResolver.isIndexed(languageCode))
                {
                    if(Boolean.TRUE.equals(isPostponeMode))
                    {
                        UITools.postponeExecution(comp, this::sendSessionLanguageNotIndexedNotification);
                    }
                    else
                    {
                        this.sendSessionLanguageNotIndexedNotification();
                    }
                }
            });
        }
    }


    protected void sendSessionLanguageNotIndexedNotification()
    {
        this.getNotificationService().notifyUser(this.getWidgetInstanceManager(),
                        NOTIFICATION_EVENT_TYPE_SESSION_LANGUAGE_NOT_INDEXED, NotificationEvent.Level.WARNING, new Object());
    }


    @ViewEvent(componentID = OPEN_PROFILE_SETTINGS_BTN, eventName = Events.ON_CLICK)
    public void openProfileSettings()
    {
        sendOutput(SOCKET_OUTPUT_OPEN_PROFILE_SETTINGS, (Object)null);
    }


    @ViewEvent(componentID = SIGNOUT_BTN, eventName = Events.ON_CLICK)
    public void logout()
    {
        getCockpitSessionService().logout();
    }


    @SocketEvent(socketId = SOCKET_INPUT_AVATAR_CHANGED)
    public void avatarChanged(final MediaModel media)
    {
        if(media != null)
        {
            final String url = media.getURL();
            if(StringUtils.isNotEmpty(url))
            {
                userProfileBtn.setImage(url);
            }
        }
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return this.cockpitSessionService;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return this.cockpitLocaleService;
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }
}
