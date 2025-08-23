package de.hybris.platform.cockpit.model.referenceeditor.simple;

public interface SimpleSelectorModel
{
    Object getValue();


    Mode getMode();


    void addSelectorModelListener(SimpleSelectorModelListener paramSimpleSelectorModelListener);


    void removeSelectorModelListener(SimpleSelectorModelListener paramSimpleSelectorModelListener);
}
