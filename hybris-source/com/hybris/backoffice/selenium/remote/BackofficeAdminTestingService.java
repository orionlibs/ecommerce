package com.hybris.backoffice.selenium.remote;

public interface BackofficeAdminTestingService
{
    void resetToDefaults();


    void applyWidgetsConfiguration(String paramString1, String paramString2);


    void applyCockpitConfiguration(String paramString);


    void importImpex(String paramString);
}
