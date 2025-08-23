package de.hybris.platform.cockpit.model.referenceeditor;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import java.util.Collection;
import java.util.List;

public interface ReferenceSelectorListener
{
    void addItem(Object paramObject);


    void addToNotConfirmedItems(Collection paramCollection);


    void addItems(List<Object> paramList);


    void removeItem(int paramInt);


    void removeItems(Collection paramCollection);


    void moveItem(int paramInt1, int paramInt2);


    void selectTemporaryItem(Object paramObject);


    void selectItem(Object paramObject);


    void deselectTemporaryItem(Object paramObject);


    void deselectTemporaryItems();


    void deselectItem(Object paramObject);


    void selectTemporaryItems(Collection paramCollection);


    void selectItems(Collection paramCollection);


    void addTemporaryItem(Object paramObject);


    void addTemporaryItems(Collection paramCollection);


    void removeTemporaryItem(int paramInt);


    void removeTemporaryItem(Object paramObject);


    void moveTemporaryItem(int paramInt1, int paramInt2);


    void clearTemporaryItems();


    void triggerAutoCompleteSearch(String paramString);


    void triggerSearch(String paramString);


    void changeMode(SelectorModel.Mode paramMode);


    void confirmAndCloseAdvancedMode();


    void selectorNormaMode();


    void selectorAdvancedMode();


    void abortAndCloseAdvancedMode();


    void saveActualItems();


    void cancel();


    void showAddItemPopupEditor(CreateContext paramCreateContext);


    void showAddItemPopupEditor(ObjectType paramObjectType, TypedObject paramTypedObject, CreateContext paramCreateContext);


    void doEnterPressed();


    void doOpenReferencedItem(TypedObject paramTypedObject);
}
