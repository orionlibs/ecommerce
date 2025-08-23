/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.widgets.viewmodel.Couple;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class MapEditorsDemoController extends DefaultWidgetController
{
    protected static final String SOCKET_IN_MODIFIED_PROPERTY_INPUT = "modifiedPropertyInput";
    protected static final String MAP_EDITOR_TRANSLATIONS = "mapEditorTranslations";
    private static final Integer DOZEN = 12;
    private static final Integer GREAT_GROSS = 1728;
    private static final Integer KILO = 1000;
    private static final Integer CENTNER = 50;
    private static final Double SHOES = 142.00D;
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(MapEditorsDemoController.class);
    private final List<Editor> editors = new ArrayList<Editor>();
    private Editor mapEditorTranslations;
    private Editor mapEditorNumbers;
    private Editor mapEditorPrices;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        editors.add(mapEditorTranslations);
        editors.add(mapEditorNumbers);
        editors.add(mapEditorPrices);
        if(getModel().getValue("mapEditorTranslationsProperty", Map.class) == null)
        {
            final Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("Zero", "Null");
            map.put("One", "Eins");
            map.put("Two", "Zwei");
            map.put("Three", "Drei");
            getModel().setValue("mapEditorTranslationsProperty", map);
        }
        if(getModel().getValue("mapEditorNumbersProperty", Map.class) == null)
        {
            final Map<String, Integer> map = new LinkedHashMap<String, Integer>();
            map.put("Dozen", DOZEN);
            map.put("Great Gross", GREAT_GROSS);
            map.put("Kilo", KILO);
            map.put("Centner (DE)", CENTNER);
            getModel().setValue("mapEditorNumbersProperty", map);
        }
        if(getModel().getValue("mapEditorPricesProperty", Map.class) == null)
        {
            final Map<String, Double> map = new LinkedHashMap<String, Double>();
            map.put("[y] Shoes", SHOES);
            getModel().setValue("mapEditorPricesProperty", map);
        }
    }


    @ViewEvent(componentID = MAP_EDITOR_TRANSLATIONS, eventName = Editor.ON_VALUE_CHANGED)
    public void onMapEditorSimpleValueModified()
    {
        LOG.info("mapEditorTranslations.Value" + mapEditorTranslations.getValue());
        final Object mapValue = mapEditorTranslations.getValue();
        sendModificationOutput("mapEditorPropertyTranslations", mapValue);
    }


    public void sendModificationOutput(final String property, final Object value)
    {
        LOG.info("sendModificationOutput -- this={}, property={}, value={}", this, property, value);
        sendOutput("modifiedPropertyOutput", new Couple<String, Object>(property, value));
    }


    @SocketEvent(socketId = SOCKET_IN_MODIFIED_PROPERTY_INPUT)
    public void receiveModificationInput(final Couple<String, Object> couple)
    {
        final String key = couple.getFirst();
        final Object value = couple.getSecond();
        LOG.info("receiveModificationInput -- this={}, key={}, value={}, model.getValue({})={}", this, key, value, key, getModel().getValue(key, Object.class));
        for(final Editor editor : editors)
        {
            if(editor != null && editor.getProperty() != null && editor.getProperty().equals(key))
            {
                LOG.info("Update value in editor={}", editor);
                // sets the value in the ModelValueHandler
                setValue(key, value);
            }
            if(editor == null)
            {
                LOG.error("an editor is missing");
            }
        }
        LOG.info("After update : model.getValue({})=", getModel().getValue(key, Object.class));
    }


    public Editor getMapEditorPrices()
    {
        return mapEditorPrices;
    }


    public Editor getMapEditorNumbers()
    {
        return mapEditorNumbers;
    }


    public Editor getMapEditorTranslations()
    {
        return mapEditorTranslations;
    }
}
