package de.hybris.platform.cockpit.services.dragdrop.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Collection;
import java.util.HashSet;

public class DefaultDraggedItem implements DraggedItem
{
    private final TypedObject typedObject;
    private final TypedObject superCategory;
    private final BrowserModel browser;


    public DefaultDraggedItem(TypedObject typedObject, BrowserModel browser, TypedObject superCategory)
    {
        this.typedObject = typedObject;
        this.superCategory = superCategory;
        this.browser = browser;
    }


    public DefaultDraggedItem(TypedObject typedObject)
    {
        this.typedObject = typedObject;
        this.superCategory = null;
        this.browser = null;
    }


    public Collection<TypedObject> getAllTypedObjects()
    {
        Collection<TypedObject> ret = new HashSet<>();
        if(getBrowser() == null || !getBrowser().getSelectedItems().contains(this.typedObject))
        {
            ret.add(getSingleTypedObject());
        }
        else
        {
            ret.addAll(getBrowser().getSelectedItems());
        }
        return ret;
    }


    public BrowserModel getBrowser()
    {
        return this.browser;
    }


    public TypedObject getSingleTypedObject()
    {
        return this.typedObject;
    }


    public TypedObject getSuperCategory()
    {
        return this.superCategory;
    }
}
