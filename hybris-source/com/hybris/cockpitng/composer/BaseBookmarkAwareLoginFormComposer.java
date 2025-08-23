/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.composer;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.web.authorization.BackofficeAuthenticationSuccessHandler;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.Events;

/**
 * Abstract login form composer which stores and cleans bookmarks.
 */
public abstract class BaseBookmarkAwareLoginFormComposer extends ViewAnnotationAwareComposer
{
    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        clearBookmarkCookies();
    }


    protected HttpServletResponse getServletResponse()
    {
        return (HttpServletResponse)Executions.getCurrent().getNativeResponse();
    }


    public void clearBookmarkCookies()
    {
        final Cookie cookie = new Cookie(BackofficeAuthenticationSuccessHandler.BO_LOGIN_BOOKMARK, StringUtils.EMPTY);
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        getServletResponse().addCookie(cookie);
    }


    @ViewEvent(eventName = Events.ON_BOOKMARK_CHANGE)
    public void storeBookmarksOnLoginPage(final BookmarkEvent event)
    {
        final String bookmark = event.getBookmark();
        final String encodedBookmark = Base64.getEncoder().encodeToString(bookmark.getBytes());
        final Cookie cookie = new Cookie(BackofficeAuthenticationSuccessHandler.BO_LOGIN_BOOKMARK, encodedBookmark);
        cookie.setSecure(true);
        cookie.setMaxAge(-1);
        cookie.setHttpOnly(true);
        getServletResponse().addCookie(cookie);
    }
}
