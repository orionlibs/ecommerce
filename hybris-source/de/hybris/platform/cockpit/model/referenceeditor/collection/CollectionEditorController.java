package de.hybris.platform.cockpit.model.referenceeditor.collection;

import java.util.List;

public interface CollectionEditorController
{
    void clearCollectionItems();


    void moveCollectionItem(int paramInt1, int paramInt2);


    void removeCollectionItem(int paramInt);


    void removeCollectionItem(Object paramObject);


    void addCollectionItems(List<Object> paramList);


    void addCollectionItem(Object paramObject);
}
