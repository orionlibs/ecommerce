/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.SimpleListModel;

public class OutputTestWidgetController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private static final String RAW_VALUE_KEY = "rawValue";
    private static final String CURRENT_DISPLAYED_OBJECT = "currentObject";
    private static final String OUTPUT_TEST_WIDGET_OBJECT = "outputTestWidgetObject";
    protected static final String ATTRIBUTE_VALUE_LIST = "attributeValueList";
    protected static final String COLLINPUT = "collectionInput";
    protected static final String SHOW_COLL_INPUT = "showCollInput";
    protected static final String GENERIC_INPUT = "genericInput";
    protected static final String CONSTANTS = "constants";
    protected static final String STATIC_VARIABLES = "staticVariables";
    protected static final String QUALIFIED_NAMES = "qualifiedNames";
    private Listbox attributeValueList;
    private Menuitem qualifiedNames;
    private Menuitem staticVariables;
    private Menuitem constantsMenuItem;
    private Listbox showCollInput;


    @SocketEvent(socketId = COLLINPUT)
    public void chooseObjectFromCol(final Object object)
    {
        setValue(RAW_VALUE_KEY, object);
        setValue(CURRENT_DISPLAYED_OBJECT, null);
        if(object instanceof Collection<?>)
        {
            final List<Object> myList = new ArrayList<Object>();
            Collections.addAll(myList, ((Collection<?>)object).toArray());
            showCollInput.setModel(new SimpleListModel<Object>(myList));
            attributeValueList.setModel((ListModel<?>)null);
        }
    }


    @ViewEvent(componentID = SHOW_COLL_INPUT, eventName = Events.ON_SELECT)
    public void selectObjectFromCollection() throws IllegalAccessException
    {
        final Listitem selectedItem = showCollInput.getSelectedItem();
        updateList(selectedItem.getValue());
    }


    @SocketEvent(socketId = GENERIC_INPUT)
    public void updateTextboxInput(final Object object) throws IllegalAccessException
    {
        setValue(RAW_VALUE_KEY, object);
        showCollInput.setModel(new SimpleListModel<Object>(Collections.singletonList(object)));
        updateList(object);
    }


    private void updateList(final Object object) throws IllegalAccessException
    {
        if(object == null)
        {
            attributeValueList.setModel((ListModel<?>)null);
        }
        else
        {
            setValue(CURRENT_DISPLAYED_OBJECT, object);
            final Map<String, Object> temp = getDisplayedAttributeValues(object);
            attributeValueList.setModel(new SimpleListModel<Object>(new ArrayList<>(temp.entrySet())));
        }
    }


    @ViewEvent(componentID = ATTRIBUTE_VALUE_LIST, eventName = Events.ON_DROP)
    public void handleDrop(final Event event) throws IllegalAccessException
    {
        if(event instanceof DropEvent)
        {
            final Component dragged = ((DropEvent)event).getDragged();
            updateList(((Listitem)dragged).getValue());
        }
    }


    @Override
    public void initialize(final Component comp)
    {
        showCollInput.setItemRenderer((listItem, arg1, index) -> {
            listItem.setLabel(String.valueOf(arg1));
            listItem.setValue(arg1);
            listItem.setDraggable(OUTPUT_TEST_WIDGET_OBJECT);
        });
        attributeValueList.setItemRenderer(getEntryListitemRenderer());
        final Object value = getValue(RAW_VALUE_KEY, Object.class);
        if(value instanceof Collection)
        {
            try
            {
                chooseObjectFromCol(value);
            }
            catch(final Exception e)
            {
                LoggerFactory.getLogger(this.getClass()).error("Could not set value: ", e);
            }
        }
        else
        {
            resetListbox();
        }
    }


    ListitemRenderer<Entry<String, ?>> getEntryListitemRenderer()
    {
        return (arg0, data, index) -> {
            final FieldInfo value = (FieldInfo)data.getValue();
            final Listcell cell0 = new Listcell();
            final int mod = value.getModifier();
            final String access = Modifier.isPublic(mod) ? "public"
                            : (Modifier.isProtected(mod) ? "protected" : (Modifier.isPrivate(mod) ? "private" : "default"));
            cell0.setSclass("yw-modifier yw-modifier-" + access);
            cell0.setParent(arg0);
            arg0.setSclass("yw-outputTest-listitem");
            final Listcell cell1 = new Listcell(data.getKey());
            cell1.setParent(arg0);
            final Listcell cell2 = new Listcell(getString(value.getValue()));
            cell2.setParent(arg0);
        };
    }


    private void resetListbox()
    {
        try
        {
            updateList(getValue(CURRENT_DISPLAYED_OBJECT, Object.class));
        }
        catch(final Exception e)
        {
            LoggerFactory.getLogger(this.getClass()).warn("Error occured: ", e);
        }
    }


    @ViewEvent(componentID = QUALIFIED_NAMES, eventName = Events.ON_CLICK)
    public void filterQualifiedNames()
    {
        getModel().setValue(QUALIFIED_NAMES, qualifiedNames.isChecked());
        resetListbox();
    }


    @ViewEvent(componentID = STATIC_VARIABLES, eventName = Events.ON_CLICK)
    public void filterStaticVariables()
    {
        getModel().setValue(STATIC_VARIABLES, staticVariables.isChecked());
        resetListbox();
    }


    @ViewEvent(componentID = CONSTANTS, eventName = Events.ON_CLICK)
    public void filterConstants()
    {
        getModel().setValue(CONSTANTS, constantsMenuItem.isChecked());
        resetListbox();
    }


    private String getString(final Object obj)
    {
        String ret = null;
        if(isAtomicType(obj))
        {
            ret = String.valueOf(obj);
        }
        else
        {
            if(Boolean.TRUE.equals(getModel().getValue(QUALIFIED_NAMES, Boolean.class)))
            {
                ret = obj.getClass().getName();
            }
            else
            {
                ret = obj.getClass().getSimpleName();
            }
        }
        return ret;
    }


    private static boolean isAtomicType(final Object obj)
    {
        if(obj == null)
        {
            return true;
        }
        return obj instanceof Number || obj instanceof String || obj instanceof Enum || obj.getClass().isPrimitive();
    }


    private Map<String, Object> getDisplayedAttributeValues(final Object obj) throws IllegalAccessException
    {
        final Class<?> cls = obj.getClass();
        final Field[] fields = cls.getDeclaredFields();
        final Map<String, Object> mapOfFields = new TreeMap<String, Object>();
        for(final Field field : fields)
        {
            field.setAccessible(true);
            final Object value = field.get(obj);
            final boolean isConstant = Boolean.TRUE.equals(getModel().getValue(CONSTANTS, Boolean.class)) && isConstant(field);
            final boolean isStatic = Boolean.TRUE.equals(getModel().getValue(STATIC_VARIABLES, Boolean.class))
                            && isNonFinalStatic(field);
            if(isConstant || isStatic || (!isConstant(field) && !isNonFinalStatic(field)))
            {
                mapOfFields.put(field.getName(), new FieldInfo(field.getModifiers(), value));
            }
        }
        return mapOfFields;
    }


    private static boolean isNonFinalStatic(final Field field)
    {
        final int modifiers = field.getModifiers();
        return (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers));
    }


    private static boolean isConstant(final Field field)
    {
        final int modifiers = field.getModifiers();
        return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
    }


    static class FieldInfo
    {
        private final int modifier;
        private final Object value;


        public FieldInfo(final int modifier, final Object value)
        {
            super();
            this.modifier = modifier;
            this.value = value;
        }


        public int getModifier()
        {
            return modifier;
        }


        public Object getValue()
        {
            return value;
        }
    }


    public Listbox getShowCollInput()
    {
        return showCollInput;
    }


    public Menuitem getConstants()
    {
        return constantsMenuItem;
    }


    public Menuitem getStaticVariables()
    {
        return staticVariables;
    }


    public Menuitem getQualifiedNames()
    {
        return qualifiedNames;
    }


    public Listbox getAttributeValueList()
    {
        return attributeValueList;
    }


    public Menuitem getConstantsMenuItem()
    {
        return constantsMenuItem;
    }
}
