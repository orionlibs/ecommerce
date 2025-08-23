package de.hybris.platform.cockpit.model.referenceeditor.collection;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.DefaultCollectionEditorModel;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.List;

public class DefaultCollectionEditorListener implements CollectionEditorController
{
    private final DefaultCollectionEditorModel model;
    private TypeService typeService = null;


    public DefaultCollectionEditorListener(DefaultCollectionEditorModel model, ObjectType rooType)
    {
        this.model = model;
    }


    public void addCollectionItem(Object collectionItem)
    {
        this.model.addCollectionItem(getTypeService().wrapItem(collectionItem));
    }


    public void addCollectionItems(List<Object> collectionItems)
    {
        this.model.addCollectionItems(getTypeService().wrapItems(collectionItems));
    }


    public void clearCollectionItems()
    {
        this.model.clearCollectionItems();
    }


    public void moveCollectionItem(int fromIndex, int toIndex)
    {
        this.model.moveCollectionItem(fromIndex, toIndex);
    }


    public void removeCollectionItem(int index)
    {
        this.model.removeCollectionItem(index);
    }


    public void removeCollectionItem(Object item)
    {
        this.model.removeCollectionItem(item);
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
