package de.hybris.platform.cockpit.model.query.impl;

import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.collection.PageableObjectCollection;
import de.hybris.platform.cockpit.model.collection.impl.ObjectCollectionImpl;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collections;
import java.util.List;

public class UICollectionQuery extends UIQuery
{
    private final ObjectCollection objectCollection;
    private boolean initial = false;


    public UICollectionQuery(ObjectCollection objectCollection)
    {
        super(objectCollection.getLabel());
        this.objectCollection = objectCollection;
    }


    public List<TypedObject> getItems()
    {
        return Collections.unmodifiableList(this.objectCollection.getElements());
    }


    public List<TypedObject> getItems(int start, int count)
    {
        return
                        Collections.unmodifiableList((this.objectCollection instanceof PageableObjectCollection) ? (
                                        (PageableObjectCollection)this.objectCollection).getElements(start, count) :
                                        this.objectCollection.getElements());
    }


    public int getSize()
    {
        return this.objectCollection.getTotalCount();
    }


    public ObjectCollection getObjectCollection()
    {
        return this.objectCollection;
    }


    public String getLabel()
    {
        return this.objectCollection.getLabel();
    }


    public void setLabel(String label)
    {
        ((ObjectCollectionImpl)this.objectCollection).setLabel(label);
    }


    public boolean isInitial()
    {
        return this.initial;
    }


    public void setInitial(boolean initial)
    {
        this.initial = initial;
    }
}
