/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.composer;

import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.renderers.common.DefaultTypedSettingsRenderer;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

public class DefaultLoginInfoRenderer extends DefaultTypedSettingsRenderer
{
    private static final String SPRING_LIKE_USER_NAME = "j_username";
    private static final String SPRING_LIKE_PASS = "j_password";
    private static final String SPRING_LIKE_REMEMBER_ME = "_spring_security_remember_me";
    private static final String TEXTBOX_TYPE_PASS = "password";


    @Override
    protected Component renderStringInput(final TypedSettingsMap widgetSettings, final Object settingValue,
                    final String settingKey, final boolean hasConnections)
    {
        final Textbox textBox = new Textbox();
        if(SPRING_LIKE_USER_NAME.equals(settingKey))
        {
            textBox.setClientAttribute("autocomplete", "off");
        }
        if(SPRING_LIKE_PASS.equals(settingKey))
        {
            textBox.setType(TEXTBOX_TYPE_PASS);
            textBox.setClientAttribute("autocomplete", "off");
        }
        textBox.setValue((String)settingValue);
        textBox.setId(settingKey);
        textBox.setInstant(true);
        textBox.setName(settingKey);
        textBox.setMultiline(false);
        final boolean errorOccurred = StringUtils.equals("1", Objects.toString(getLoginErrorParam(), ""));
        textBox.setSclass(errorOccurred ? "login_error" : "login");
        if(!TEXTBOX_TYPE_PASS.equalsIgnoreCase(textBox.getType()))
        {
            textBox.addEventListener(Events.ON_CHANGING, (InputEvent event) -> widgetSettings.put(settingKey, event.getValue()));
        }
        return textBox;
    }


    @Override
    protected Component renderBooleanInput(final TypedSettingsMap widgetSettings, final Object settingValue,
                    final String settingKey)
    {
        HtmlBasedComponent ret = null;
        if(SPRING_LIKE_REMEMBER_ME.equals(settingKey))
        {
            final Checkbox checkbox = new Checkbox();
            checkbox.setId(settingKey);
            checkbox.setName(SPRING_LIKE_REMEMBER_ME);
            checkbox.setChecked(Boolean.TRUE.equals(settingValue));
            checkbox.addEventListener(Events.ON_CHECK,
                            (EventListener<CheckEvent>)event -> widgetSettings.put(settingKey, event.isChecked()));
            ret = checkbox;
        }
        else
        {
            final Radiogroup booleanGroup = new Radiogroup();
            booleanGroup.setId(settingKey);
            final Hlayout hlayout = new Hlayout();
            final Radio trueRadio = new Radio(Boolean.TRUE.toString());
            final Radio falseRadio = new Radio(Boolean.FALSE.toString());
            hlayout.appendChild(falseRadio);
            hlayout.appendChild(trueRadio);
            booleanGroup.appendChild(hlayout);
            booleanGroup.setSelectedItem(Boolean.TRUE.equals(settingValue) ? trueRadio : falseRadio);
            booleanGroup.addEventListener(Events.ON_CHECK,
                            event -> widgetSettings.put(settingKey, trueRadio.equals(booleanGroup.getSelectedItem())));
            ret = booleanGroup;
        }
        return ret;
    }


    Object getLoginErrorParam()
    {
        return Executions.getCurrent().getParameter("login_error");
    }
}
