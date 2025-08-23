package de.hybris.platform.personalizationservices.occ;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CxOccAttributesStrategy
{
    void setPersonalizationId(String paramString, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);


    Optional<String> readPersonalizationId(HttpServletRequest paramHttpServletRequest);


    void setPersonalizationCalculationTime(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);


    Optional<Long> readPersonalizationCalculationTime(HttpServletRequest paramHttpServletRequest);
}
