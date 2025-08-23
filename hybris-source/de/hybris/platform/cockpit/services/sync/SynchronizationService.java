package de.hybris.platform.cockpit.services.sync;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SynchronizationService
{
    public static final int SYNCHRONIZATION_OK = 0;
    public static final int SYNCHRONIZATION_NOT_AVAILABLE = -1;
    public static final int SYNCHRONIZATION_NOT_OK = 1;
    public static final int INITIAL_SYNC_IS_NEEDED = 2;


    Collection<TypedObject> performSynchronization(Collection<? extends Object> paramCollection, List<String> paramList, CatalogVersionModel paramCatalogVersionModel, String paramString);


    void performPullSynchronization(List<TypedObject> paramList);


    int isObjectSynchronized(TypedObject paramTypedObject);


    int getPullSyncStatus(TypedObject paramTypedObject);


    List<SyncItemJobModel>[] getTargetCatalogVersions(TypedObject paramTypedObject);


    CatalogVersionModel getCatalogVersionForItem(TypedObject paramTypedObject);


    Map<String, String>[] getAllSynchronizationRules(Collection paramCollection);


    boolean hasMultipleRules(Collection paramCollection);


    List<String> getSynchronizationStatuses(List<SyncItemJobModel> paramList, TypedObject paramTypedObject);


    void performCatalogVersionSynchronization(Collection<CatalogVersionModel> paramCollection, List<String> paramList, CatalogVersionModel paramCatalogVersionModel, String paramString);


    boolean isVersionSynchronizedAtLeastOnce(List paramList);


    List<SyncItemJobModel>[] getSyncJobs(ItemModel paramItemModel, ObjectType paramObjectType);


    Collection<TypedObject> getSyncSources(TypedObject paramTypedObject);


    Collection<TypedObject> getSyncTargets(TypedObject paramTypedObject);


    Collection<TypedObject> getSyncSourcesAndTargets(TypedObject paramTypedObject);


    SyncContext getSyncContext(TypedObject paramTypedObject);


    SyncContext getSyncContext(TypedObject paramTypedObject, boolean paramBoolean);
}
