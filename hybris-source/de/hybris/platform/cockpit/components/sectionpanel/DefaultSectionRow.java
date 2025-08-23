package de.hybris.platform.cockpit.components.sectionpanel;

import java.util.HashMap;
import java.util.Map;

public class DefaultSectionRow implements SectionRow
{
    private String label;
    private boolean visible;
    private boolean editable = true;
    private boolean modified;
    private String valueInfo;
    private SectionRow.FocusListener focusListener = null;
    Map<String, SectionRow.FocusListener> listenersForLanguage = new HashMap<>();


    public void setFocusListener(SectionRow.FocusListener listener, String langIso)
    {
        if(langIso == null)
        {
            this.focusListener = listener;
        }
        else
        {
            this.listenersForLanguage.put(langIso, listener);
        }
    }


    public SectionRow.FocusListener getFocusListener(String langIso)
    {
        if(langIso == null)
        {
            return this.focusListener;
        }
        return this.listenersForLanguage.get(langIso);
    }


    public DefaultSectionRow()
    {
    }


    public DefaultSectionRow(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public boolean isModified()
    {
        return this.modified;
    }


    public void setModified(boolean modified)
    {
        this.modified = modified;
    }


    public String getValueInfo()
    {
        return this.valueInfo;
    }


    public void setValueInfo(String valueInfo)
    {
        this.valueInfo = valueInfo;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }
}
