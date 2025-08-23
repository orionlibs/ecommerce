/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zk.ui.sys.PropertiesRenderer;

public class CockpitUIListener implements PropertiesRenderer
{
    private static final String YTEST_ID_ATTRIBUTE_FLAG = "cockpit.ytestid.enabled";
    private static final String YTEST_ID_ATTRIBUTE_KEY = "ytestid";


    private List<RenderProperty> getPropertyList()
    {
        final List<RenderProperty> propertyList = new ArrayList<>();
        propertyList.add(new RenderProperty(YTEST_ID_ATTRIBUTE_KEY, isConfigEnabled(YTEST_ID_ATTRIBUTE_FLAG)));
        return propertyList;
    }


    private boolean isConfigEnabled(final String configName)
    {
        var isConfigEnabled = false;
        final CockpitProperties props = (CockpitProperties)SpringUtil.getBean("cockpitProperties", CockpitProperties.class);
        if(props != null)
        {
            isConfigEnabled = props.getBoolean(configName, false);
        }
        return isConfigEnabled;
    }


    @Override
    public void renderProperties(final Component component, final ContentRenderer renderer) throws IOException
    {
        if(component.getDesktop() == null)
        {
            return;
        }
        Map<String, String> cockpitUIAttributes = new HashMap();
        final List<RenderProperty> propertyList = getPropertyList();
        for(int i = 0; i < propertyList.size(); i++)
        {
            final RenderProperty renderProperty = propertyList.get(i);
            if(renderProperty.isEnabled())
            {
                var propertyName = renderProperty.getPropertyName();
                var attributeValue = Objects.toString(component.getAttribute(propertyName), StringUtils.EMPTY);
                if(propertyName.equals(YTEST_ID_ATTRIBUTE_KEY) && StringUtils.isBlank(attributeValue)
                                && StringUtils.isNotBlank(component.getId()))
                {
                    attributeValue = component.getId();
                }
                if(StringUtils.isNotBlank(attributeValue))
                {
                    final String finalValue = attributeValue;
                    component.setClientAttribute(propertyName, finalValue);
                    cockpitUIAttributes = component.getWidgetAttributeNames().stream()
                                    .collect(Collectors.toMap(key -> key, component::getClientAttribute));
                    cockpitUIAttributes.computeIfAbsent(propertyName, k -> finalValue);
                }
            }
        }
        if(cockpitUIAttributes.size() > 0)
        {
            renderClientAttributes(component, renderer, cockpitUIAttributes);
        }
    }


    private void renderClientAttributes(final Component component, final ContentRenderer renderer,
                    final Map<String, String> clientAttributes)
    {
        if(!clientAttributes.isEmpty())
        {
            if(component instanceof AbstractTag)
            {
                clientAttributes.forEach((propertyName, propertyValue) -> renderer.renderWidgetListeners(Collections
                                .singletonMap("onBind", String.format("this.$n().setAttribute(`%s`, `%s`)", propertyName, propertyValue))));
            }
            else
            {
                renderer.renderClientAttributes(clientAttributes);
            }
        }
    }


    static final class RenderProperty
    {
        private String propertyName;
        private boolean enabled;


        private RenderProperty(final String propertyName, final boolean enabled)
        {
            this.propertyName = propertyName;
            this.enabled = enabled;
        }


        private String getPropertyName()
        {
            return this.propertyName;
        }


        private boolean isEnabled()
        {
            return this.enabled;
        }
    }
}
