/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util.impl;

import com.hybris.cockpitng.util.impl.DefaultCockpitSessionService;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.session.SessionService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

/**
 * Enhanced version of {@link DefaultCockpitSessionService} that does proper hybris session logout handling.
 */
public class DefaultPlatformCockpitSessionService extends DefaultCockpitSessionService
{
    private transient SessionService sessionService;


    @Override
    public void logout()
    {
        deleteLoginTokenCookie();
        sessionService.closeCurrentSession();
        super.logout();
    }


    private void deleteLoginTokenCookie()
    {
        final Execution execution = Executions.getCurrent();
        final HttpServletRequest request = (HttpServletRequest)execution.getNativeRequest();
        final HttpServletResponse response = (HttpServletResponse)execution.getNativeResponse();
        UserManager.getInstance().deleteLoginTokenCookie(request, response);
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }
}
