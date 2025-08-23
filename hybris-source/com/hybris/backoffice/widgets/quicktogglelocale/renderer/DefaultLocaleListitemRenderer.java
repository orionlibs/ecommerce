/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.quicktogglelocale.renderer;

import com.hybris.backoffice.widgets.quicktogglelocale.controller.QuickToggleLocaleController;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class DefaultLocaleListitemRenderer implements ListitemRenderer<Locale>
{
    private final Locale currentLocale;
    private final QuickToggleLocaleController controller;


    public DefaultLocaleListitemRenderer(final QuickToggleLocaleController controller, final Locale currentLocale)
    {
        this.controller = controller;
        this.currentLocale = currentLocale;
    }


    @Override
    public void render(final Listitem listitem, final Locale locale, final int index)
    {
        String label = controller.getLabel(locale.toString());
        if(StringUtils.isBlank(label))
        {
            label = locale.getDisplayName(currentLocale);
        }
        listitem.setValue(locale);
        listitem.setLabel(label);
        listitem.setSelected(controller.isLocaleEnabled(locale));
    }
}
