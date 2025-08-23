/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.persistence.impl.jaxb.AutoCreateMode;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.Range;
import com.hybris.cockpitng.widgets.viewmodel.Couple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

/**
 *
 */
public class EditorExampleMVCController extends DefaultWidgetController
{
    /**
     *
     */
    protected static final String DATE_EDITOR = "dateEditor";
    protected static final String BOOLEAN_EDITOR = "booleanEditor";
    protected static final String CHECK_BOOLEAN_EDITOR = "checkBooleanEditor";
    protected static final String DROPDOWN_BOOLEAN_EDITOR = "dropdownBooleanEditor";
    protected static final String INTEGER_EDITOR = "integerEditor";
    protected static final String LONG_EDITOR = "longEditor";
    protected static final String DECIMAL_EDITOR = "decimalEditor";
    protected static final String TEXT_EDITOR = "textEditor";
    protected static final String RANGE_EDITOR = "rangeEditor";
    protected static final String LIST_EDITOR = "listEditor";
    protected static final String LIST_EDITOR_MOLD = "listEditorOsMold";
    protected static final String MAP_EDITOR = "mapEditor";
    protected static final String SOCKET_IN_MODIFIED_PROPERTY_INPUT = "modifiedPropertyInput";
    private static final long serialVersionUID = -7285075603931325042L;
    private static final Logger LOG = LoggerFactory.getLogger(EditorExampleMVCController.class);
    private static final transient List<Editor> EDITORS = new ArrayList<>();
    private static final String LIST_EDITOR_PROPERTY = "listEditorProperty";
    private static final String MAP_EDITOR_PROPERTY = "mapEditorProperty";
    protected transient Editor pkEditor;
    protected transient Editor pkEditor2;
    protected transient Editor dateEditor;
    protected transient Editor booleanEditor;
    protected transient Editor chkBoolEditor;
    protected transient Editor selBooleanEditor;
    protected transient Editor integerEditor;
    protected transient Editor longEditor;
    protected transient Editor decimalEditor;
    protected transient Editor textEditor;
    protected transient Editor rangeEditor;
    protected transient Editor listEditor;
    protected transient Editor mapEditor;
    protected transient Editor regularLocalized;
    protected transient Editor wysiwygLocalized;
    protected transient Editor listEditorOsMold;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        EDITORS.add(pkEditor);
        EDITORS.add(pkEditor2);
        EDITORS.add(dateEditor);
        EDITORS.add(booleanEditor);
        EDITORS.add(chkBoolEditor);
        EDITORS.add(selBooleanEditor);
        EDITORS.add(integerEditor);
        EDITORS.add(longEditor);
        EDITORS.add(decimalEditor);
        EDITORS.add(textEditor);
        EDITORS.add(rangeEditor);
        EDITORS.add(listEditor);
        EDITORS.add(mapEditor);
        EDITORS.add(listEditorOsMold);
        EDITORS.add(regularLocalized);
        EDITORS.add(wysiwygLocalized);
        // initialize one of editors
        if(getModel().getValue(MAP_EDITOR_PROPERTY, Map.class) == null)
        {
            final Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("A", "toB");
            map.put("B", "toC");
            map.put("C", "toTransitivity");
            getModel().setValue(MAP_EDITOR_PROPERTY, map);
        }
        if(getModel().getValue(LIST_EDITOR_PROPERTY, List.class) == null)
        {
            getModel().setValue(LIST_EDITOR_PROPERTY, Arrays.asList("A", "B", "C"));
        }
        if(getModel().getValue("listEditorPropertyDate", List.class) == null)
        {
            getModel().setValue("listEditorPropertyDate", Arrays.asList(new Date()));
        }
        if(getModel().getValue("xxx", Object.class) == null)
        {
            final Map<Locale, String> map = new LinkedHashMap<Locale, String>();
            map.put(Locale.ENGLISH, "Good morning!");
            map.put(Locale.GERMANY, "Guten Tag!");
            map.put(Locale.ITALY, null);
            getModel().setValue("xxx", map);
        }
        if(getModel().getValue("enumEditorProperty", Object.class) == null)
        {
            getModel().setValue("enumEditorProperty", AutoCreateMode.CREATE_IF_RESOLVING_FAILED);
        }
    }
    // -----------------------------------------------------------------
    // VIEW EVENTS LISTENERS
    // -----------------------------------------------------------------


    @ViewEvent(componentID = DATE_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onDateEditorValueModified()
    {
        LOG.info("onDateEditorValueModified -- dateEditor.getValue()=" + dateEditor.getValue());
        final Date date = (Date)dateEditor.getValue();
        sendModificationOutput("dateEditorProperty", date);
    }


    @ViewEvent(componentID = BOOLEAN_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onBooleanEditorValueModified()
    {
        LOG.info("onBooleanEditorValueModified -- booleanEditor.getValue()=" + booleanEditor.getValue());
        final Boolean bool = (Boolean)booleanEditor.getValue();
        sendModificationOutput("booleanEditorProperty", bool);
    }


    @ViewEvent(componentID = CHECK_BOOLEAN_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onChekBooleanEditorValueModified()
    {
        LOG.info("onCheckBooleanEditorValueModified -- booleanEditor.getValue()=" + chkBoolEditor.getValue());
        final Boolean bool = (Boolean)chkBoolEditor.getValue();
        sendModificationOutput("checkBooleanEditorProperty", bool);
    }


    @ViewEvent(componentID = DROPDOWN_BOOLEAN_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onDropdownBooleanEditorValueModified()
    {
        LOG.info("onDropdownBooleanEditorValueModified -- booleanEditor.getValue()=" + selBooleanEditor.getValue());
        final Boolean bool = (Boolean)selBooleanEditor.getValue();
        sendModificationOutput("dropdownBooleanEditorProperty", bool);
    }


    @ViewEvent(componentID = INTEGER_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onIntegerEditorValueModified()
    {
        LOG.info("onIntegerEditorValueModified -- integerEditor.getValue()=" + integerEditor.getValue());
        final Integer integer = (Integer)integerEditor.getValue();
        sendModificationOutput("integerEditorProperty", integer);
    }


    @ViewEvent(componentID = LONG_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onLongEditorValueModified()
    {
        LOG.info("onLongEditorValueModified -- longEditor.getValue()=" + longEditor.getValue());
        final Long longValue = (Long)longEditor.getValue();
        sendModificationOutput("longEditorProperty", longValue);
    }


    @ViewEvent(componentID = DECIMAL_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onDecimalEditorValueModified()
    {
        LOG.info("onDecimalEditorValueModified -- decimalEditor.getValue()=" + decimalEditor.getValue());
        final Double decimalValue = (Double)decimalEditor.getValue();
        sendModificationOutput("decimalEditorProperty", decimalValue);
    }


    @ViewEvent(componentID = TEXT_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onTextEditorValueModified()
    {
        LOG.info("onTextEditorValueModified -- textEditor.getValue()=" + textEditor.getValue());
        final String textValue = (String)textEditor.getValue();
        sendModificationOutput("textEditorProperty", textValue);
    }


    @ViewEvent(componentID = RANGE_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onRangeEditorValueModified()
    {
        LOG.info("onRangeEditorValueModified -- rangeEditor.getValue()=" + rangeEditor.getValue());
        final Range<?> rangeValue = (Range<?>)rangeEditor.getValue();
        sendModificationOutput("rangeEditorProperty", rangeValue);
    }


    @ViewEvent(componentID = LIST_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onListEditorValueModified()
    {
        LOG.info("onListEditorValueModified -- listEditor.getValue()=" + listEditor.getValue());
        final Object listValue = listEditor.getValue();
        sendModificationOutput(LIST_EDITOR_PROPERTY, listValue);
    }


    @ViewEvent(componentID = LIST_EDITOR_MOLD, eventName = Editor.ON_VALUE_CHANGED)
    public void onListEditorOsMoldValueModified()
    {
        LOG.info("onListEditorOsMoldValueModified -- listEditorOsMold.getValue()=" + listEditorOsMold.getValue());
        final Object listValue = listEditorOsMold.getValue();
        sendModificationOutput("listEditorPropertyOsMold", listValue);
    }


    @ViewEvent(componentID = MAP_EDITOR, eventName = Editor.ON_VALUE_CHANGED)
    public void onMapEditorValueModified()
    {
        LOG.info("onMapEditorValueModified -- mapEditor.getValue()=" + mapEditor.getValue());
        final Object mapValue = mapEditor.getValue();
        sendModificationOutput(MAP_EDITOR_PROPERTY, mapValue);
    }
    // -----------------------------------------------------------------
    // SOCKETS
    // -----------------------------------------------------------------


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
        LOG.info("receiveModificationInput -- this={}, key={}, value={}, model.getValue({})={}", this, key, value, key,
                        getModel().getValue(key, Object.class));
        for(final Editor editor : EDITORS)
        {
            if(editor != null && editor.getProperty() != null && editor.getProperty().equals(key))
            {
                LOG.info("Update value in editor={}", editor);
                // this sets the value in the ModelValueHandler
                setValue(key, value);
            }
            if(editor == null)
            {
                LOG.error("an editor is missing");
            }
        }
        LOG.info("After update : model.getValue({})={}", key, getModel().getValue(key, Object.class));
    }
}
