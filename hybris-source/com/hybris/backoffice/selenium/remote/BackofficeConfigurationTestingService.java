/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.selenium.remote;

import java.io.IOException;

/**
 * Interface used for remote method invocation (via Spring's HTTP Invoker)
 */
public interface BackofficeConfigurationTestingService
{
    void importImpex(String content);


    void reloadValidationEngine();


    void resetCockpitConfig();


    void resetConfigurationCache();


    void applyTestConfigurationToConfigurationCache(String fileContent, String moduleName) throws IOException;


    void clearAdditionalWidgetConfig();


    void resetWidgetConfig();


    void applyTestWidgetConfig(String configFileName) throws IOException;
}
