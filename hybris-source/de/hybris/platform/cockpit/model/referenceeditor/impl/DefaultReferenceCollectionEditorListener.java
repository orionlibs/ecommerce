package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.referenceeditor.ReferenceCollectionEditorListener;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import java.util.List;

public class DefaultReferenceCollectionEditorListener implements ReferenceCollectionEditorListener
{
    private final DefaultReferenceCollectionEditorModel model;
    private final ObjectType rootType;
    private TypeService typeService = null;


    public DefaultReferenceCollectionEditorListener(DefaultReferenceCollectionEditorModel model, ObjectType rooType)
    {
        this.model = model;
        this.rootType = rooType;
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


    public void removeCollectionItems(Collection indexes)
    {
        this.model.removeCollectionItems(indexes);
    }


    public void removeCollectionItem(int index)
    {
        this.model.removeCollectionItem(index);
    }


    public void removeCollectionItem(Object item)
    {
        this.model.removeCollectionItem(item);
        this.model.getReferenceSelectorModel().removeTemporaryItem(item);
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
