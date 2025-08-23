package de.hybris.platform.subscriptioncockpits.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.impl.DefaultSelectUIEditor;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

public class NullableSelectUIEditor extends DefaultSelectUIEditor
{
    public void setAvailableValues(List<? extends Object> availableValues)
    {
        List<Object> listWithNull = new ArrayList(availableValues.size() + 1);
        listWithNull.add(null);
        listWithNull.addAll(availableValues);
        super.setAvailableValues(listWithNull);
    }


    protected void addObjectToCombo(Object value, Combobox box)
    {
        if(value == null)
        {
            Comboitem comboitem = new Comboitem();
            comboitem.setLabel(Labels.getLabel("subscriptioncockpits.general.null"));
            comboitem.setValue(null);
            box.appendChild((Component)comboitem);
        }
        else
        {
            super.addObjectToCombo(value, box);
        }
    }
}
