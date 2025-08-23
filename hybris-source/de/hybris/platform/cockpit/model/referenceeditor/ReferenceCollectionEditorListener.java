package de.hybris.platform.cockpit.model.referenceeditor;

import java.util.Collection;
import java.util.List;

@Deprecated
public interface ReferenceCollectionEditorListener
{
    void clearCollectionItems();


    void moveCollectionItem(int paramInt1, int paramInt2);


    void removeCollectionItems(Collection paramCollection);


    void removeCollectionItem(int paramInt);


    void removeCollectionItem(Object paramObject);


    void addCollectionItems(List<Object> paramList);


    void addCollectionItem(Object paramObject);
}
