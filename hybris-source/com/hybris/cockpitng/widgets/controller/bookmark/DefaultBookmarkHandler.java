/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller.bookmark;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.BookmarkEvent;

/**
 * Default handler for bookmark events. Matches bookmark of pattern: '#open(pk)'.
 */
public class DefaultBookmarkHandler implements BookmarkHandler
{
    public static final String OUTPUT_SOCKET_DEFAULT_ENTITY = "defaultEntity";
    public static final String BOOKMARK_OBJECT_ACCESS = "BookmarkObjectAccess";
    private static final Pattern OPEN_PK_PATTERN = Pattern.compile("open\\((?<pk>\\S+)\\)");
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBookmarkHandler.class);
    private ObjectFacade objectFacade;
    private PermissionFacade permissionFacade;
    private NotificationService notificationService;
    private CockpitUserService cockpitUserService;


    @Override
    public void handleBookmark(final BookmarkEvent data, final WidgetInstanceManager wim)
    {
        final String bookmark = data.getBookmark();
        if(StringUtils.isBlank(bookmark))
        {
            return;
        }
        final Matcher matcher = OPEN_PK_PATTERN.matcher(bookmark.trim());
        if(matcher.matches())
        {
            final String pk = matcher.group("pk");
            try
            {
                final Object object = loadObject(pk);
                if(object != null && permissionFacade.canReadInstance(object))
                {
                    wim.sendOutput(OUTPUT_SOCKET_DEFAULT_ENTITY, object);
                }
                else
                {
                    LOG.warn("User {} does not have access to entity: {}", getCockpitUserService().getCurrentUser(), pk);
                    getNotificationService().notifyUser(wim, BOOKMARK_OBJECT_ACCESS, Level.WARNING);
                }
            }
            catch(final ObjectNotFoundException e)
            {
                LOG.warn("Object with pk {} does not exist.", pk);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Bookmark could not be opened", e);
                }
                getNotificationService().notifyUser(wim, BOOKMARK_OBJECT_ACCESS, Level.WARNING);
            }
        }
    }


    public <T> T loadObject(String pk) throws ObjectNotFoundException
    {
        return getObjectFacade().load(pk);
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    @Required
    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }
}
