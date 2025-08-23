/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.surveylauncher;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.theme.BackofficeThemeService;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

public class SurveyLauncherController extends DefaultWidgetController
{
    private static final String QTX_INTERCEPT_URL = "intercept_url";
    private static final String QTX_CONTEXT_PARAMS = "context_params";
    private static final String FIORI3_COMPATIBLE = "fiori3_compatible";
    private static final String JS_FUNCTION_PATTERN = "updateQtxContextParams('%s')";
    private static final String PERSPECTIVE_CHOOSER_WIDGET_CODE = "com.hybris.backoffice.perspectiveChooser";
    private static final String QUALTRICS_URL = "https://zn8wg0tz71we2lgrq-sapinsights.siteintercept.qualtrics.com/SIE/?Q_ZID=ZN_8wG0Tz71wE2LgrQ";
    private static final String QUALTRICS_URL_KEY = "backoffice.survey.qualtricsURL";
    private static final String STRING_FORMAT = "%s.%s";
    @WireVariable
    private transient ConfigurationService configurationService;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    private transient BackofficeThemeService backofficeThemeService;


    @Override
    public void preInitialize(final Component comp)
    {
        if(!Config.isCloudEnvironment())
        {
            comp.getParent().removeChild(comp);
            return;
        }
        super.preInitialize(comp);
        getModel().put(QTX_INTERCEPT_URL, getConfigurationService().getConfiguration().getString(QUALTRICS_URL_KEY, QUALTRICS_URL));
        getModel().put(FIORI3_COMPATIBLE, isFiori3Compatible());
        getModel().put(QTX_CONTEXT_PARAMS, buildContextParams());
    }


    @SocketEvent(socketId = "perspectiveSelected")
    public void perspectiveSelected(final NavigationNode navigationNode)
    {
        if(navigationNode != null)
        {
            final String appId = String.format(STRING_FORMAT, PERSPECTIVE_CHOOSER_WIDGET_CODE, navigationNode.getNameLocKey()) != null
                            ? String.format(STRING_FORMAT, PERSPECTIVE_CHOOSER_WIDGET_CODE, navigationNode.getNameLocKey())
                            : navigationNode.getName();
            final String appTitle = Labels
                            .getLabel(String.format(STRING_FORMAT, PERSPECTIVE_CHOOSER_WIDGET_CODE, navigationNode.getNameLocKey())) != null
                            ? Labels.getLabel(String.format(STRING_FORMAT, PERSPECTIVE_CHOOSER_WIDGET_CODE, navigationNode.getNameLocKey()))
                            .replace("'", "\\'")
                            : navigationNode.getName();
            Clients.evalJavaScript(String.format(JS_FUNCTION_PATTERN, String.format("[[\"%s\",\"%s\"],[\"%s\",\"%s\"]]", "appTitle", appTitle, "appId", appId)));
        }
    }


    protected String buildContextParams()
    {
        final Map<String, String> map = new HashMap<>();
        map.put("productName", getProperty("backoffice.cockpitng.appTitle"));
        map.put("appFrameworkId", "1");
        map.put("appFrameworkVersion", WebApps.getCurrent().getVersion());
        map.put("language", getCockpitLocaleService().getCurrentLocale().toString());
        map.put("theme", getCurrentThemeCode());
        map.put("appId", "-");
        map.put("appTitle", "-");
        map.put("appVersion", getProperty("build.version.api"));
        map.put("tenantId", String.format("%s-%s-%s", getProperty("modelt.customer.code"), getProperty("modelt.project.code"),
                        getProperty("modelt.environment.code")));
        map.put("tenantRole", getProperty("modelt.environment.type"));
        return "{" + map.entrySet().stream().map(e -> "\"" + e.getKey() + "\":\"" + e.getValue() + "\"")
                        .collect(Collectors.joining(", ")) + "}";
    }


    protected String getProperty(final String key)
    {
        return getConfigurationService().getConfiguration().getString(key);
    }


    protected boolean isFiori3Compatible()
    {
        //should return false if current theme is Horizon.
        return true;
    }


    protected String getCurrentThemeCode()
    {
        final var theme = getBackofficeThemeService().getCurrentTheme();
        return theme.getCode();
    }


    protected ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    protected BackofficeThemeService getBackofficeThemeService()
    {
        return backofficeThemeService;
    }
}
