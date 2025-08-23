package de.hybris.platform.cockpit.model.editor;

import java.util.List;

public interface ListUIEditor extends UIEditor
{
    void setAvailableValues(List<? extends Object> paramList);


    List<? extends Object> getAvailableValues();
}
