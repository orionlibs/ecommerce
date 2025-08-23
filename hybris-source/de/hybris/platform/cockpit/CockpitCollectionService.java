package de.hybris.platform.cockpit;

import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;

public interface CockpitCollectionService
{
    int addToCollection(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, List<ItemModel> paramList);


    int removeFromCollection(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, List<ItemModel> paramList);


    List<ItemModel> getElements(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, int paramInt1, int paramInt2);


    int removeNullItemReferences(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    int getElementCount(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    CockpitObjectAbstractCollectionModel cloneCollection(CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel, UserModel paramUserModel);


    List<CockpitObjectAbstractCollectionModel> getCollectionsForUser(UserModel paramUserModel);


    List<CockpitObjectSpecialCollectionModel> getSpecialCollectionsForUser(UserModel paramUserModel);


    List<CockpitObjectSpecialCollectionModel> getSpecialCollections(UserModel paramUserModel, String paramString);


    boolean hasReadCollectionRight(PrincipalModel paramPrincipalModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    boolean hasWriteCollectionRight(PrincipalModel paramPrincipalModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    boolean isInCollection(ItemModel paramItemModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    void addReadUser(PrincipalModel paramPrincipalModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    void addWriteUser(PrincipalModel paramPrincipalModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    void removeReadUser(PrincipalModel paramPrincipalModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);


    void removeWriteUser(PrincipalModel paramPrincipalModel, CockpitObjectAbstractCollectionModel paramCockpitObjectAbstractCollectionModel);
}
