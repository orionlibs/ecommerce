package de.hybris.platform.cockpit.model.referenceeditor.collection.model;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import java.util.Collection;
import java.util.List;

public interface CollectionEditorModel
{
    List<Object> getCollectionItems();


    String getItemLabel(Object paramObject);


    DefaultSimpleReferenceSelectorModel getSimpleReferenceSelectorModel();


    void addCollectionEditorModelListener(CollectionEditorModelListener paramCollectionEditorModelListener);


    void removeCollectionEditorModelListener(CollectionEditorModelListener paramCollectionEditorModelListener);


    ObjectType getRootType();


    ObjectType getRootSearchType();


    void addCollectionItem(Object paramObject);


    void addCollectionItems(Collection<? extends Object> paramCollection);
}
