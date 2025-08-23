/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.preview;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.zk.ui.util.Clients;

public class PreviewController extends DefaultWidgetController
{
    protected static final String JS_FUNCTION_PATTERN = "previewTheme('%s')";
    protected static final String SOCKET_IN_THEME_VARIABLES_CHANGED = "themeVariablesChanged";


    @SocketEvent(socketId = SOCKET_IN_THEME_VARIABLES_CHANGED)
    public void onThemeChanged(final Map<String, String> data)
    {
        if(data instanceof Map)
        {
            final String vars = data.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(";"));
            Clients.evalJavaScript(String.format(JS_FUNCTION_PATTERN, vars));
        }
    }
}
