/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.renderers.common;

import com.hybris.cockpitng.core.SocketConnectionService;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Doublespinner;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;

public class DefaultTypedSettingsRenderer implements TypedSettingsRenderer
{
    @Override
    public Component renderSetting(final TypedSettingsMap widgetSettings, final String settingKey, final boolean hasConnections)
    {
        Component ret = null;
        final Object settingValue = widgetSettings.get(settingKey);
        if(settingValue instanceof String && !widgetSettings.isEnum(settingKey))
        {
            ret = renderStringInput(widgetSettings, settingValue, settingKey, hasConnections);
        }
        else if(widgetSettings.isEnum(settingKey))
        {
            ret = renderEnumInput(widgetSettings, settingValue, settingKey);
        }
        else if(settingValue instanceof Integer)
        {
            ret = renderIntegerInput(widgetSettings, settingValue, settingKey);
        }
        else if(settingValue instanceof Boolean)
        {
            ret = renderBooleanInput(widgetSettings, settingValue, settingKey);
        }
        else if(settingValue instanceof Double)
        {
            ret = renderDoubleInput(widgetSettings, settingValue, settingKey);
        }
        return ret;
    }


    protected Component renderEnumInput(final TypedSettingsMap widgetSettings, final Object settingValue, final String settingKey)
    {
        final Combobox combobox = new Combobox();
        combobox.setId(settingKey);
        combobox.setReadonly(true);
        final List<String> availableValues = widgetSettings.getAvailableValues(settingKey);
        for(final String string : availableValues)
        {
            final Comboitem item = new Comboitem(string);
            item.setValue(string);
            combobox.appendChild(item);
        }
        if(settingValue != null)
        {
            final int indexOf = availableValues.indexOf(settingValue);
            if(indexOf >= 0)
            {
                combobox.setSelectedIndex(indexOf);
            }
        }
        combobox.addEventListener(Events.ON_SELECT, (EventListener<SelectEvent<Comboitem, String>>)event -> {
            if(event.getSelectedItems().size() == 1)
            {
                final Comboitem next = event.getSelectedItems().iterator().next();
                final String value = next.getValue();
                widgetSettings.put(settingKey, value);
            }
        });
        return combobox;
    }


    protected Component renderStringInput(final TypedSettingsMap widgetSettings, final Object settingValue,
                    final String settingKey, final boolean hasConnections)
    {
        final Textbox tbox = new Textbox();
        tbox.setId(settingKey);
        tbox.setMultiline(true);
        tbox.setValue((String)settingValue);
        tbox.setSclass("stringSetting");
        if(settingKey.startsWith(SocketConnectionService.SOCKET_DATA_TYPE_PREFIX) && hasConnections
                        && StringUtils.isNotBlank((String)settingValue))
        {
            tbox.setDisabled(true);
            tbox.setTooltiptext("Type variable can not be changed until there are connections.");
        }
        else
        {
            tbox.addEventListener(Events.ON_CHANGE, (EventListener<InputEvent>)event -> widgetSettings.put(settingKey, event.getValue()));
        }
        return tbox;
    }


    protected Component renderIntegerInput(final TypedSettingsMap widgetSettings, final Object settingValue,
                    final String settingKey)
    {
        final Spinner intbox = new Spinner(((Integer)settingValue).intValue());
        intbox.setSclass("integerSetting");
        intbox.setId(settingKey);
        intbox.addEventListener(Events.ON_CHANGE, event -> widgetSettings.put(settingKey, intbox.getValue()));
        return intbox;
    }


    protected Component renderBooleanInput(final TypedSettingsMap widgetSettings, final Object settingValue,
                    final String settingKey)
    {
        final Combobox cbox = new Combobox();
        cbox.setId(settingKey);
        cbox.setSclass("booleanCombo");
        final Comboitem trueItem = new Comboitem(Boolean.TRUE.toString());
        cbox.appendChild(trueItem);
        final Comboitem falseItem = new Comboitem(Boolean.FALSE.toString());
        cbox.appendChild(falseItem);
        cbox.setSelectedItem(Boolean.TRUE.equals(settingValue) ? trueItem : falseItem);
        cbox.addEventListener(Events.ON_CHANGE, event -> widgetSettings.put(settingKey, Boolean.valueOf(trueItem.equals(cbox.getSelectedItem()))));
        return cbox;
    }


    protected Component renderDoubleInput(final TypedSettingsMap widgetSettings, final Object settingValue, final String settingKey)
    {
        final Doublespinner doublebox = new Doublespinner(((Double)settingValue).doubleValue());
        doublebox.setId(settingKey);
        doublebox.setSclass("doubleSetting");
        doublebox.addEventListener(Events.ON_CHANGE, event -> widgetSettings.put(settingKey, doublebox.getValue()));
        return doublebox;
    }
}
