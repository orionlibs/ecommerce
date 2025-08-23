package de.hybris.platform.cockpit.model.referenceeditor.simple;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.impl.CreateContext;

public interface SimpleReferenceSelectorListener
{
    void triggerAutoCompleteSearch(String paramString);


    void selectorNormaMode();


    void selectorAdvancedMode();


    void abortAndCloseAdvancedMode();


    void cancel();


    void saveActualItem(Object paramObject);


    void showAddItemPopupEditor(CreateContext paramCreateContext);


    void showAddItemPopupEditor(ObjectType paramObjectType, TypedObject paramTypedObject, CreateContext paramCreateContext);


    void doOpenReferencedItem(TypedObject paramTypedObject);
}
