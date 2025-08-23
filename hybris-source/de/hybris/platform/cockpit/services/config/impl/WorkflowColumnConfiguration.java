package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTextCellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.WorkflowValueHandler;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowColumnConfiguration extends DefaultCustomColumnConfiguration
{
    private Map<LanguageModel, String> labels = new HashMap<>();
    private DefaultColumnDescriptor columnDescriptor;
    private ValueHandler valueHandler;


    public void setAllLabels(Map<LanguageModel, String> createLabels)
    {
        this.labels = createLabels;
    }


    public Map<LanguageModel, String> getAllLabels()
    {
        return this.labels;
    }


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        if(this.columnDescriptor == null)
        {
            this.columnDescriptor = new DefaultColumnDescriptor(getName());
            this.columnDescriptor.setVisible(isVisible());
            this.columnDescriptor.setSelectable(isSelectable());
            this.columnDescriptor.setSortable(isSortable());
            this.columnDescriptor.setEditable(isEditable());
        }
        return this.columnDescriptor;
    }


    public ValueHandler getValueHandler()
    {
        if(this.valueHandler == null)
        {
            this.valueHandler = (ValueHandler)new WorkflowValueHandler(super.getName());
        }
        return this.valueHandler;
    }


    public CellRenderer getCellRenderer()
    {
        if(super.getCellRenderer() == null)
        {
            setCellRenderer((CellRenderer)new DefaultTextCellRenderer());
        }
        return super.getCellRenderer();
    }


    public String getLabel()
    {
        if(this.labels.size() == 1)
        {
            return (String)((Map.Entry)this.labels.entrySet().iterator().next()).getValue();
        }
        if(this.labels.isEmpty())
        {
            return null;
        }
        return getLabel(UISessionUtils.getCurrentSession().getLanguageIso());
    }


    public String getName()
    {
        if(!this.labels.isEmpty())
        {
            return getLabel();
        }
        return super.getName();
    }


    public String getLabel(String iso)
    {
        String ret = null;
        if(this.labels == null || this.labels.isEmpty())
        {
            ret = "<" + this.name + ">";
        }
        else
        {
            for(Map.Entry<LanguageModel, String> e : this.labels.entrySet())
            {
                if(((LanguageModel)e.getKey()).getIsocode().equals(iso))
                {
                    ret = e.getValue();
                    break;
                }
            }
        }
        return ret;
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
