package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorConfiguration;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.Set;

public interface UIEditorArea extends UICockpitArea, UISessionListener, FocusablePerspectiveArea
{
    void addEditorAreaListener(EditorAreaListener paramEditorAreaListener);


    void removeEditorAreaListener(EditorAreaListener paramEditorAreaListener);


    void setCurrentObject(TypedObject paramTypedObject);


    TypedObject getCurrentObject();


    void setCurrentObjectType(ObjectType paramObjectType);


    ObjectType getCurrentObjectType();


    ObjectValueContainer getCurrentObjectValues();


    EditorConfiguration getTypeConfiguration();


    void doSave();


    void doPreviousItem();


    void doNextItem();


    void doBrowseItem();


    void doOnChange(PropertyDescriptor paramPropertyDescriptor);


    void setManagingPerspective(UICockpitPerspective paramUICockpitPerspective);


    UICockpitPerspective getManagingPerspective();


    void reset();


    void updateAllValues();


    void updateValues(Set<PropertyDescriptor> paramSet);


    EditorAreaController getEditorAreaController();


    void setWidth(String paramString);


    String getWidth();
}
