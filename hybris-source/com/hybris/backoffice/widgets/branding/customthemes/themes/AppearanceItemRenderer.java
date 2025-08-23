/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import de.hybris.platform.core.model.media.MediaModel;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class AppearanceItemRenderer
{
    private List<AppearanceItem> appearanceItems;
    private Map<String, String> styleVariableMap;
    private ThemesController controller;
    private boolean isColorChanged = false;


    public AppearanceItemRenderer(final Component comp, final ThemesController controller)
    {
        this.controller = controller;
        appearanceItems = controller.getCustomThemeUtil().getThemeVariablesMapping().stream()
                        .map(variablesMapping -> new AppearanceItem(variablesMapping, comp)).collect(Collectors.toList());
    }


    protected boolean resetAppearanceItemColor(final MediaModel style)
    {
        styleVariableMap = controller.getCustomThemeUtil().convertStyleMediaToVariableMap(style);
        if(styleVariableMap.isEmpty())
        {
            return false;
        }
        appearanceItems.forEach(appearanceItem -> appearanceItem
                        .setColor(styleVariableMap.get(appearanceItem.variablesMapping.getVariables().get(0))));
        controller.onColorChange(styleVariableMap);
        isColorChanged = false;
        return true;
    }


    public boolean isColorChanged()
    {
        return isColorChanged;
    }


    public Map<String, String> getStyleVariableMap()
    {
        return styleVariableMap;
    }


    public List<AppearanceItem> getAppearanceItems()
    {
        return appearanceItems;
    }


    protected void onColorChange(final Map<String, String> changedStyleVariableMap)
    {
        if(styleVariableMap == null || styleVariableMap.isEmpty())
        {
            return;
        }
        controller.onColorChange(changedStyleVariableMap);
        styleVariableMap.putAll(changedStyleVariableMap);
        isColorChanged = true;
        controller.enableSave(true);
    }


    protected class AppearanceItem
    {
        protected Colorbox colorBox;
        protected Textbox textBox;
        protected ThemeVariablesMapping variablesMapping;


        protected AppearanceItem(final ThemeVariablesMapping variablesMapping, final Component comp)
        {
            this.variablesMapping = variablesMapping;
            renderCustomizeItem(comp);
        }


        private void renderCustomizeItem(final Component comp)
        {
            final var container = new Div();
            container.setSclass("yw-customTheme-attribute");
            container.setParent(comp);
            container.appendChild(new Label(controller.getLabel(variablesMapping.getLabelKey())));
            colorBox = new Colorbox();
            colorBox.setSclass("yw-customTheme-attribute-colorBox");
            textBox = new Textbox();
            textBox.setSclass("yw-customTheme-attribute-colorTextBox");
            container.appendChild(colorBox);
            container.appendChild(textBox);
            colorBox.addEventListener(Events.ON_CHANGE, this::onColorboxChange);
            textBox.addEventListener(Events.ON_CHANGE, this::onTextboxChange);
        }


        protected void onColorboxChange(final Event event)
        {
            textBox.setValue(colorBox.getColor());
            onColorChange(getChangedStyleVariableMap());
        }


        protected void onTextboxChange(final Event event)
        {
            if(controller.getCustomThemeUtil().isValidHexColor(textBox.getValue()))
            {
                setColor(controller.getCustomThemeUtil().toLongHexColor(textBox.getValue()));
                onColorChange(getChangedStyleVariableMap());
            }
            else
            {
                textBox.setValue(colorBox.getColor());
            }
        }


        private Map<String, String> getChangedStyleVariableMap()
        {
            return variablesMapping.getVariables().stream()
                            .collect(Collectors.toMap(Function.identity(), str -> colorBox.getColor()));
        }


        protected void setColor(final String color)
        {
            colorBox.setColor(color);
            textBox.setValue(color);
        }
    }
}
