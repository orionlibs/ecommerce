/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing;

import com.hybris.cockpitng.core.util.CockpitProperties;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zk.ui.sys.PropertiesRenderer;

/**
 * Life cycle listener to manage test IDs used to test the UI (ytestid attribute). The IDs should be set as custom
 * attributes in zul files using ca namespace (<code>xmlns:ca="client/attribute"</code>). If no ytestid is provided or
 * it's value is blank than by default component's id attribute's value is set as ytestid's value. Example:
 * <code>
 * 	&lt;widget xmlns="http://www.zkoss.org/2005/zul" xmlns:ca="client/attribute"&gt;
 * 		&nbsp;&lt;div ca:ytestid="someValue"&gt;
 * 			&nbsp;&nbsp;&lt;!-- content --&gt;
 * 		&nbsp;&lt;/div&gt;
 * 	&lt;/widget&gt;
 * </code>
 *
 * @see org.zkoss.zk.ui.sys.PropertiesRenderer
 */
public class CockpitTestIdUIListener implements PropertiesRenderer
{
    private static final String TEST_ID_SWITCH_PROPERTY = "cockpit.ytestid.enabled";
    private static final String TEST_ID_ATTRIBUTE_NAME = "ytestid";
    private static final Logger LOG = LoggerFactory.getLogger(CockpitTestIdUIListener.class);
    private Boolean ytestidEnabled;


    private void assureInit()
    {
        if(ytestidEnabled == null)
        {
            final CockpitProperties props = (CockpitProperties)SpringUtil.getBean("cockpitProperties", CockpitProperties.class);
            if(props == null)
            {
                ytestidEnabled = Boolean.FALSE;
            }
            else
            {
                ytestidEnabled = BooleanUtils.toBooleanObject(props.getProperty(TEST_ID_SWITCH_PROPERTY));
                if(!Boolean.FALSE.equals(ytestidEnabled))
                {
                    LOG.info("UI Test ID's enabled. To disable set {} = false", TEST_ID_SWITCH_PROPERTY);
                    ytestidEnabled = Boolean.TRUE;
                }
            }
        }
    }


    @Override
    public void renderProperties(final Component component, final ContentRenderer renderer) throws IOException
    {
        assureInit();
        if(ytestidEnabled)
        {
            String ytestId = Objects.toString(component.getAttribute(TEST_ID_ATTRIBUTE_NAME), StringUtils.EMPTY);
            if(StringUtils.isBlank(ytestId) && StringUtils.isNotBlank(component.getId()))
            {
                ytestId = component.getId();
            }
            if(StringUtils.isNotBlank(ytestId))
            {
                renderYTestId(component, renderer, ytestId);
            }
        }
    }


    private static void renderYTestId(final Component component, final ContentRenderer renderer, final String ytestId)
    {
        final Map<String, String> clientAttributes = component.getWidgetAttributeNames().stream()
                        .collect(Collectors.toMap(key -> key, component::getClientAttribute));
        if(!clientAttributes.containsKey(TEST_ID_ATTRIBUTE_NAME))
        {
            clientAttributes.put(TEST_ID_ATTRIBUTE_NAME, ytestId);
        }
        if(component instanceof AbstractTag)
        {
            renderer.renderWidgetListeners(Collections.singletonMap("onBind",
                            "this.$n().setAttribute('" + TEST_ID_ATTRIBUTE_NAME + "', '" + ytestId + "');"));
        }
        else
        {
            renderer.renderClientAttributes(clientAttributes);
        }
    }
}
