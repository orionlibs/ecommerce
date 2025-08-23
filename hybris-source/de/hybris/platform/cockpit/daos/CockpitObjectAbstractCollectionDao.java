package de.hybris.platform.cockpit.daos;

import de.hybris.platform.cockpit.enums.CockpitSpecialCollectionType;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.cockpit.model.ObjectCollectionItemReferenceModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface CockpitObjectAbstractCollectionDao extends Dao
{
    List<ObjectCollectionItemReferenceModel> findElements(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, int paramInt1, int paramInt2);


    int getElementCount(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    @Deprecated
    List<ObjectCollectionItemReferenceModel> getElementsContainingItem(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, ItemModel paramItemModel);


    List<ObjectCollectionItemReferenceModel> findElementsContainingItem(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, ItemModel paramItemModel);


    boolean collectionContains(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, ItemModel paramItemModel);


    @Deprecated
    List<CockpitObjectCollectionModel> getCollectionsByUser(UserModel paramUserModel);


    List<CockpitObjectCollectionModel> findCollectionsByUser(UserModel paramUserModel);


    @Deprecated
    List<CockpitObjectSpecialCollectionModel> getSpecialCollectionsByUser(UserModel paramUserModel);


    List<CockpitObjectSpecialCollectionModel> findSpecialCollectionsByUser(UserModel paramUserModel);


    @Deprecated
    List<CockpitObjectSpecialCollectionModel> getSpecialCollections(UserModel paramUserModel, CockpitSpecialCollectionType paramCockpitSpecialCollectionType);


    List<CockpitObjectSpecialCollectionModel> findSpecialCollections(UserModel paramUserModel, CockpitSpecialCollectionType paramCockpitSpecialCollectionType);


    @Deprecated
    List<CockpitObjectAbstractCollectionModel> getGlobalCollections();


    List<CockpitObjectAbstractCollectionModel> findGlobalCollections();


    @Deprecated
    List<CockpitObjectCollectionModel> getReadableCollectionsForUser(UserModel paramUserModel);


    List<CockpitObjectCollectionModel> findReadableCollectionsByUser(UserModel paramUserModel);


    @Deprecated
    List<CockpitObjectCollectionModel> getWritableCollectionsForUser(UserModel paramUserModel);


    List<CockpitObjectCollectionModel> findWritableCollectionsByUser(UserModel paramUserModel);
}
