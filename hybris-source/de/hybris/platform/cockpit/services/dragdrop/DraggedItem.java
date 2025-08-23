package de.hybris.platform.cockpit.services.dragdrop;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Collection;

public interface DraggedItem
{
    public static final String TYPED_ITEM = "typedItem";
    public static final String SUPER_CATEGORY = "superCategory";
    public static final String CONTAINING_BROWSER = "browser";


    TypedObject getSingleTypedObject();


    Collection<TypedObject> getAllTypedObjects();


    BrowserModel getBrowser();


    TypedObject getSuperCategory();
}
