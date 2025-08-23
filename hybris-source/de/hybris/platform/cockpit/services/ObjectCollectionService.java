package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collection;
import java.util.List;

public interface ObjectCollectionService
{
    @Deprecated
    ObjectCollection createCollection(String paramString, UserModel paramUserModel);


    @Deprecated
    ObjectCollection createSpecialCollection(String paramString, UserModel paramUserModel);


    @Deprecated
    ObjectCollection createSpecialCollection(String paramString, UserModel paramUserModel, EnumerationValue paramEnumerationValue);


    @Deprecated
    ObjectCollection createSpecialCollection(String paramString, UserModel paramUserModel, HybrisEnumValue paramHybrisEnumValue);


    void renameCollection(ObjectCollection paramObjectCollection, String paramString);


    @Deprecated
    void removeCollection(ObjectCollection paramObjectCollection);


    int addToCollection(ObjectCollection paramObjectCollection, Collection<TypedObject> paramCollection);


    int removeFromCollection(ObjectCollection paramObjectCollection, Collection<TypedObject> paramCollection);


    List<TypedObject> getElements(ObjectCollection paramObjectCollection, int paramInt1, int paramInt2);


    int removeNullItemReferences(ObjectCollection paramObjectCollection);


    int getElementCount(ObjectCollection paramObjectCollection);


    void publishCollection(ObjectCollection paramObjectCollection);


    ObjectCollection cloneCollection(ObjectCollection paramObjectCollection, UserModel paramUserModel);


    @Deprecated
    List<ObjectCollection> getCollections(UserModel paramUserModel);


    List<ObjectCollection> getCollectionsForUser(UserModel paramUserModel);


    @Deprecated
    List<ObjectCollection> getSpecialCollections(UserModel paramUserModel);


    List<ObjectCollection> getSpecialCollectionsForUser(UserModel paramUserModel);


    List<ObjectCollection> getSpecialCollections(UserModel paramUserModel, String paramString);


    @Deprecated
    ObjectCollection refreshCollection(ObjectCollection paramObjectCollection);


    @Deprecated
    List<PrincipalModel> getReadUsers(ObjectCollection paramObjectCollection);


    List<PrincipalModel> getReadUsersForCollection(ObjectCollection paramObjectCollection);


    @Deprecated
    List<PrincipalModel> getWriteUsers(ObjectCollection paramObjectCollection);


    List<PrincipalModel> getWriteUsersForCollection(ObjectCollection paramObjectCollection);


    Boolean hasReadCollectionRight(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);


    Boolean hasWriteCollectionRight(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);


    @Deprecated
    Boolean isCollectionOwner(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);


    Boolean isInCollection(TypedObject paramTypedObject, ObjectCollection paramObjectCollection);


    void addReadUser(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);


    void addWriteUser(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);


    void removeReadUser(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);


    void removeWriteUser(PrincipalModel paramPrincipalModel, ObjectCollection paramObjectCollection);
}
