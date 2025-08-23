/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller.concept;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.util.Arrays;
import java.util.Date;
import org.zkoss.zk.ui.Component;

/**
 * Container to demonstrate the Editor concept.
 */
public class EditorWidgetController extends DefaultWidgetController
{
    private static final long serialVersionUID = 1L;
    private Editor booleanEditor;
    private Editor checkBooleanEditor;
    private Editor dropdownBooleanEditor;
    private Editor decimalEditor;
    private Editor integerEditor;
    private Editor longEditor;
    private Editor dateEditor;
    private Editor textEditor;


    @Override
    public void initialize(final Component comp)
    {
        setDefaultValues();
    }


    /**
     * Fill all editors with some values.
     */
    private void setDefaultValues()
    {
        booleanEditor.setValue(Boolean.TRUE);
        checkBooleanEditor.setValue(Boolean.TRUE);
        dropdownBooleanEditor.setValue(Boolean.TRUE);
        dropdownBooleanEditor.setAttribute("falseLabel", "false");
        dropdownBooleanEditor.reload();
        decimalEditor.setValue(Double.MAX_VALUE);
        integerEditor.setValue(Integer.MAX_VALUE);
        longEditor.setValue(Long.MAX_VALUE);
        dateEditor.setValue(new Date());
        textEditor.setValue("Hello, World!");
        getModel().setValue("listEditorProperty", Arrays.asList("First", "Second", "Third"));
    }


    public Editor getTextEditor()
    {
        return textEditor;
    }


    public Editor getDateEditor()
    {
        return dateEditor;
    }


    public Editor getLongEditor()
    {
        return longEditor;
    }


    public Editor getIntegerEditor()
    {
        return integerEditor;
    }


    public Editor getDecimalEditor()
    {
        return decimalEditor;
    }


    public Editor getDropdownBooleanEditor()
    {
        return dropdownBooleanEditor;
    }


    public Editor getCheckBooleanEditor()
    {
        return checkBooleanEditor;
    }


    public Editor getBooleanEditor()
    {
        return booleanEditor;
    }
}
