package de.hybris.platform.cockpit.widgets.models.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.models.ItemEditorWidgetModel;
import org.apache.commons.lang.StringUtils;

public class DefaultItemEditorWidgetModel<T extends TypedObject> extends DefaultItemWidgetModel<T> implements ItemEditorWidgetModel<T>
{
    private String confCode;


    public boolean setConfigurationCode(String confCode)
    {
        boolean changed = false;
        if(!StringUtils.equals(this.confCode, confCode))
        {
            this.confCode = confCode;
            changed = true;
        }
        return changed;
    }


    public String getConfigurationCode()
    {
        return this.confCode;
    }
}
