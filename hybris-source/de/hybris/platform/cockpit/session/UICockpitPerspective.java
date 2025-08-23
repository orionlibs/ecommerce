package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.notifier.NotifierZKComponent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.events.CockpitEventProducer;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapperService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import java.util.Collection;
import java.util.Map;

public interface UICockpitPerspective extends UIComponent, CockpitEventProducer, CockpitEventAcceptor
{
    void setUid(String paramString);


    String getUid();


    UINavigationArea getNavigationArea();


    UIBrowserArea getBrowserArea();


    UIEditorArea getEditorArea();


    UIEditorArea getPopupEditorArea();


    void setNotifier(NotifierZKComponent paramNotifierZKComponent);


    NotifierZKComponent getNotifier();


    void resetOpenBrowserContainer();


    FocusablePerspectiveArea getFocusedArea();


    void setFocusedArea(FocusablePerspectiveArea paramFocusablePerspectiveArea);


    TypeService getTypeService();


    DragAndDropWrapperService getDragAndDropWrapperService();


    boolean isActivationEffectEnabled();


    String getEffectBorderColor();


    double getEffectDuration();


    int getMoveTargetX();


    int getMoveTargetY();


    TypedObject getActiveItem();


    void activateItemInEditor(TypedObject paramTypedObject);


    void createItemInPopupEditor(ObjectType paramObjectType, Map<String, ? extends Object> paramMap, BrowserModel paramBrowserModel);


    void createItemInPopupEditor(ObjectType paramObjectType, Map<String, ? extends Object> paramMap, BrowserModel paramBrowserModel, boolean paramBoolean);


    void createItemInPopupEditor(ObjectType paramObjectType, Map<String, ? extends Object> paramMap, BrowserModel paramBrowserModel, boolean paramBoolean1, boolean paramBoolean2);


    void createItemInPopupEditor(ObjectType paramObjectType, Map<String, ? extends Object> paramMap, CreateContext paramCreateContext);


    void createItemInPopupEditor(ObjectType paramObjectType, Map<String, ? extends Object> paramMap, CreateContext paramCreateContext, boolean paramBoolean);


    void activateItemInPopupEditor(TypedObject paramTypedObject);


    void openReferenceCollectionInBrowserContext(Collection<TypedObject> paramCollection, ObjectTemplate paramObjectTemplate, TypedObject paramTypedObject, Map<String, ? extends Object> paramMap);


    void handleItemRemoved(TypedObject paramTypedObject);


    void setSelectable(boolean paramBoolean);


    boolean isSelectable();


    int getInfoBoxTimeout();


    void setInfoBoxTimeout(int paramInt);
}
