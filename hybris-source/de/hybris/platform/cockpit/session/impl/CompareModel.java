package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompareModel
{
    private final List<TypedObject> items = new ArrayList<>();
    private TypedObject referenceObject = null;


    public CompareModel(List<TypedObject> items)
    {
        setItems(items);
    }


    public void setItems(List<TypedObject> items)
    {
        this.items.clear();
        this.items.addAll(items);
    }


    public void setReferenceObject(TypedObject referenceObject)
    {
        this.referenceObject = referenceObject;
    }


    public List<TypedObject> getItems()
    {
        return (this.items == null) ? Collections.EMPTY_LIST : this.items;
    }


    public TypedObject getReferenceObject()
    {
        if(this.referenceObject == null && !getItems().isEmpty())
        {
            this.referenceObject = getItems().iterator().next();
        }
        return this.referenceObject;
    }
}
