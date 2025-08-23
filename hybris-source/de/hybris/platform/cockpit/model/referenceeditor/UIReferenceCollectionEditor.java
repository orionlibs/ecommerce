package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.general.UIViewComponent;

@Deprecated
public interface UIReferenceCollectionEditor extends UIViewComponent
{
    void setModel(CollectionEditorModel paramCollectionEditorModel);


    CollectionEditorModel getModel();


    void updateCollectionItems();


    void updateRootTypeChanged();


    void updateRootSearchTypeChanged();


    void addReferenceCollectionEditorListener(ReferenceCollectionEditorListener paramReferenceCollectionEditorListener);


    void removeReferenceCollectionEditorListener(ReferenceCollectionEditorListener paramReferenceCollectionEditorListener);
}
