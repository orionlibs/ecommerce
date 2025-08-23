package de.hybris.platform.cockpit.model.referenceeditor;

import java.util.List;

public interface SelectorModel
{
    @Deprecated
    List<Object> getItems();


    Object getValue();


    Mode getMode();


    boolean isMultiple();


    String getLabel();


    String getItemLabel(Object paramObject);


    void addSelectorModelListener(SelectorModelListener paramSelectorModelListener);


    void removeSelectorModelListener(SelectorModelListener paramSelectorModelListener);
}
