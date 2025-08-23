package de.hybris.platform.personalizationservices.handlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PersonalizationHandler
{
    void handlePersonalization(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
