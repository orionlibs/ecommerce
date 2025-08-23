/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.js;

import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DefaultJsWidgetSessionInfoCollector implements JsWidgetSessionInfoCollector
{
    private CockpitUserService cockpitUserService;
    private CockpitLocaleService cockpitLocaleService;
    private List<JsWidgetSessionInfoDecorator> decorators = new ArrayList<>();


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    @Override
    public JsWidgetSessionDTO gatherSessionInfo()
    {
        JsWidgetSessionDTO dto = new JsWidgetSessionDTO();
        final String currentUser = cockpitUserService.getCurrentUser();
        dto.setCurrentUser(currentUser);
        dto.setAdmin(cockpitUserService.isAdmin(currentUser));
        dto.setCurrentLocale(cockpitLocaleService.getCurrentLocale());
        dto.setEnabledDataLocales(cockpitLocaleService.getEnabledDataLocales(currentUser).toArray(new Locale[0]));
        if(decorators != null)
        {
            for(final JsWidgetSessionInfoDecorator jsWidgetSessionInfoDecorator : decorators)
            {
                dto = jsWidgetSessionInfoDecorator.decorate(dto);
            }
        }
        return dto;
    }


    public List<JsWidgetSessionInfoDecorator> getDecorators()
    {
        return decorators;
    }


    public void setDecorators(final List<JsWidgetSessionInfoDecorator> decorators)
    {
        this.decorators = decorators;
    }
}
