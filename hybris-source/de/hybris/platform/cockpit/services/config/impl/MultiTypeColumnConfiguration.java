package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.impl.MultiTypeValueHandler;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;

public class MultiTypeColumnConfiguration extends AbstractColumnConfiguration
{
    private final DefaultColumnDescriptor columnDescriptor;
    private final ValueHandler valueHandler;


    public MultiTypeColumnConfiguration(String qualifier, String label, CellRenderer cellRenderer, boolean selectable)
    {
        this.columnDescriptor = new DefaultColumnDescriptor(label);
        this.columnDescriptor.setVisible(true);
        this.columnDescriptor.setSelectable(selectable);
        this.valueHandler = (ValueHandler)new MultiTypeValueHandler(qualifier);
        setCellRenderer(cellRenderer);
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        return this.columnDescriptor;
    }


    public ValueHandler getValueHandler()
    {
        return this.valueHandler;
    }


    public UIEditor getCellEditor()
    {
        return null;
    }


    public void setCellEditor(UIEditor editor)
    {
    }


    public void setLanguages(List<LanguageModel> languages)
    {
    }
}
