package de.hybris.platform.personalizationservices.strategies.impl;

import de.hybris.platform.personalizationservices.strategies.CxStorefrontReportingStrategy;

public class DefaultCxStorefrontReportingStrategy implements CxStorefrontReportingStrategy
{
    private boolean reportingActive = true;


    public boolean isReportingActive()
    {
        return this.reportingActive;
    }


    public void setReportingActive(boolean reportingActive)
    {
        this.reportingActive = reportingActive;
    }
}
