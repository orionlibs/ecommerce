package de.hybris.platform.servicelayer.web.session.stale;

import javax.servlet.http.HttpServletRequest;

public interface StaleSessionDetector
{
    Detection beginDetection(HttpServletRequest paramHttpServletRequest);
}
