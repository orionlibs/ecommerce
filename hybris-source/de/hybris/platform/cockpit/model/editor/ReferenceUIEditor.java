package de.hybris.platform.cockpit.model.editor;

import de.hybris.platform.cockpit.model.meta.ObjectType;

public interface ReferenceUIEditor extends UIEditor
{
    public static final String ALLOW_CREATE_PARAM_KEY = "allowCreate";


    void setRootType(ObjectType paramObjectType);


    void setRootSearchType(ObjectType paramObjectType);


    ObjectType getRootType();


    ObjectType getRootSearchType();


    Boolean isAllowCreate();


    void setAllowCreate(Boolean paramBoolean);
}
