package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.editor.UIEditor;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultColumnDescriptor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCustomColumnConfiguration extends AbstractColumnConfiguration
{
    private Map<LanguageModel, String> labels = new HashMap<>();


    public void setAllLabels(Map<LanguageModel, String> createLabels)
    {
        this.labels = createLabels;
    }


    public Map<LanguageModel, String> getAllLabels()
    {
        return this.labels;
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


    public DefaultColumnDescriptor getColumnDescriptor()
    {
        return null;
    }


    public ValueHandler getValueHandler()
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
