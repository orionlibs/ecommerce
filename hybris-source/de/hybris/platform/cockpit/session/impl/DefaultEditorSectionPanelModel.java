package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.DefaultSectionPanelModel;
import de.hybris.platform.cockpit.session.EditorSectionPanelModel;
import de.hybris.platform.cockpit.session.UIEditorArea;
import java.util.HashMap;
import java.util.Map;

public class DefaultEditorSectionPanelModel extends DefaultSectionPanelModel implements EditorSectionPanelModel
{
    private boolean createMode;
    private final UIEditorArea editorArea;


    public DefaultEditorSectionPanelModel(UIEditorArea editorArea)
    {
        if(editorArea == null)
        {
            throw new IllegalArgumentException("Editor area can not be null.");
        }
        this.editorArea = editorArea;
    }


    public UIEditorArea getEditorArea()
    {
        return this.editorArea;
    }


    public void setCreateMode(boolean create)
    {
        this.createMode = create;
    }


    public boolean isCreateMode()
    {
        return this.createMode;
    }


    public Map<String, Object> getContext()
    {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("editorArea", getEditorArea());
        ctx.put("currentObject", getEditorArea().getCurrentObject());
        ctx.put("valueContainer", getEditorArea().getCurrentObjectValues());
        return ctx;
    }
}
