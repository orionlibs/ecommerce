package de.hybris.platform.servicelayer.web.session.stale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface StaleSessionStrategy
{
    Action onStaleSession(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
