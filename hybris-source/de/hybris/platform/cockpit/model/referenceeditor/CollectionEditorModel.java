package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.impl.DefaultReferenceSelectorModel;
import java.util.List;

public interface CollectionEditorModel
{
    List<Object> getCollectionItems();


    String getItemLabel(Object paramObject);


    DefaultReferenceSelectorModel getReferenceSelectorModel();


    void addCollectionEditorModelListener(ReferenceCollectionEditorModelListener paramReferenceCollectionEditorModelListener);


    void removeCollectionEditorModelListener(ReferenceCollectionEditorModelListener paramReferenceCollectionEditorModelListener);


    ObjectType getRootType();


    ObjectType getRootSearchType();
}
