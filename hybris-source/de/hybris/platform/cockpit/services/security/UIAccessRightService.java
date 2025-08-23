package de.hybris.platform.cockpit.services.security;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.user.UserModel;

public interface UIAccessRightService
{
    boolean canRead(UserModel paramUserModel, String paramString);


    boolean canWrite(UserModel paramUserModel, String paramString);


    boolean canWrite(UserModel paramUserModel, CatalogVersionModel paramCatalogVersionModel);


    boolean isReadable(ObjectType paramObjectType);


    boolean isReadable(ObjectType paramObjectType, PropertyDescriptor paramPropertyDescriptor, boolean paramBoolean);


    boolean isWritable(ObjectType paramObjectType);


    boolean isWritable(ObjectType paramObjectType, TypedObject paramTypedObject);


    boolean isWritable(ObjectType paramObjectType, PropertyDescriptor paramPropertyDescriptor, boolean paramBoolean);


    boolean isWritable(ObjectType paramObjectType, TypedObject paramTypedObject, PropertyDescriptor paramPropertyDescriptor, boolean paramBoolean);
}
