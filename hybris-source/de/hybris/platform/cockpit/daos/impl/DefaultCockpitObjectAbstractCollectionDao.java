package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.daos.CockpitObjectAbstractCollectionDao;
import de.hybris.platform.cockpit.enums.CockpitSpecialCollectionType;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.cockpit.model.ObjectCollectionItemReferenceModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultCockpitObjectAbstractCollectionDao extends DefaultGenericDao<CockpitObjectAbstractCollectionModel> implements CockpitObjectAbstractCollectionDao
{
    public DefaultCockpitObjectAbstractCollectionDao()
    {
        this("CockpitObjectAbstractCollection");
    }


    public DefaultCockpitObjectAbstractCollectionDao(String typeCode)
    {
        super(typeCode);
    }


    public List<ObjectCollectionItemReferenceModel> findElements(CockpitObjectAbstractCollectionModel collection, int start, int count)
    {
        SearchResult<ObjectCollectionItemReferenceModel> result = findElementsInternal(collection, start, count, false);
        return result.getResult();
    }


    public int getElementCount(CockpitObjectAbstractCollectionModel collection)
    {
        SearchResult<ObjectCollectionItemReferenceModel> result = findElementsInternal(collection, 0, 1, true);
        return result.getTotalCount();
    }


    @Deprecated
    public List<ObjectCollectionItemReferenceModel> getElementsContainingItem(CockpitObjectAbstractCollectionModel collection, ItemModel item)
    {
        return findElementsContainingItem(collection, item);
    }


    public List<ObjectCollectionItemReferenceModel> findElementsContainingItem(CockpitObjectAbstractCollectionModel collection, ItemModel item)
    {
        if(item == null)
        {
            return Collections.EMPTY_LIST;
        }
        SearchResult<ObjectCollectionItemReferenceModel> result = getElementsContainingItemInternal(collection, item, -1);
        return result.getResult();
    }


    public boolean collectionContains(CockpitObjectAbstractCollectionModel collection, ItemModel item)
    {
        if(item == null)
        {
            return false;
        }
        SearchResult<ObjectCollectionItemReferenceModel> result = getElementsContainingItemInternal(collection, item, 1);
        return (result.getCount() > 0);
    }


    protected SearchResult<ObjectCollectionItemReferenceModel> findElementsInternal(CockpitObjectAbstractCollectionModel collection, int start, int count, boolean needTotal)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {ObjectCollectionItemReference} WHERE {collection} = ?objectColl ORDER BY {modifiedtime}", Collections.singletonMap("objectColl", collection));
        query.setStart(start);
        query.setCount(count);
        query.setNeedTotal(needTotal);
        return getFlexibleSearchService().search(query);
    }


    public SearchResult<ObjectCollectionItemReferenceModel> getElementsContainingItemInternal(CockpitObjectAbstractCollectionModel collection, ItemModel item, int count)
    {
        Map<String, Object> values = new HashMap<>(2);
        values.put("objectColl", collection);
        values.put("storedItem", item);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {ObjectCollectionItemReference} WHERE {collection} = ?objectColl AND {item} = ?storedItem ", values);
        query.setStart(0);
        query.setCount(count);
        query.setNeedTotal(false);
        return getFlexibleSearchService().search(query);
    }


    @Deprecated
    public List<CockpitObjectCollectionModel> getCollectionsByUser(UserModel user)
    {
        return findCollectionsByUser(user);
    }


    public List<CockpitObjectCollectionModel> findCollectionsByUser(UserModel user)
    {
        Map<String, Object> queryParams = new HashMap<>(1);
        queryParams.put("user", user);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectCollection").append("} ");
        builder.append("WHERE {").append("user").append("} = ?user ");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectCollectionModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    @Deprecated
    public List<CockpitObjectCollectionModel> getReadableCollectionsForUser(UserModel userModel)
    {
        return findReadableCollectionsByUser(userModel);
    }


    public List<CockpitObjectCollectionModel> findReadableCollectionsByUser(UserModel userModel)
    {
        Set<UserModel> userGroupsAndUser = new HashSet(userModel.getAllGroups());
        userGroupsAndUser.add(userModel);
        Map<Object, Object> queryParams = new HashMap<>();
        queryParams.put("users", userGroupsAndUser);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectCollection").append(" AS coll ");
        builder.append("JOIN ").append(GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION)
                        .append(" AS readRelation ");
        builder.append("ON {coll.").append("pk")
                        .append("} = {readRelation.target}} WHERE {readRelation.source} IN (?users)");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectCollectionModel> userReadableCollectionsResult = getFlexibleSearchService().search(query);
        builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectCollection").append(" AS coll ");
        builder.append("JOIN ").append(GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION)
                        .append(" AS writeRelation ");
        builder.append("ON {coll.").append("pk")
                        .append("} = {writeRelation.target}} WHERE {writeRelation.source} IN (?users)");
        query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectCollectionModel> userWritableCollectionsResult = getFlexibleSearchService().search(query);
        Set<CockpitObjectCollectionModel> resultList = new HashSet<>();
        resultList.addAll(getCollectionsByUser(userModel));
        resultList.addAll(userReadableCollectionsResult.getResult());
        resultList.addAll(userWritableCollectionsResult.getResult());
        return new ArrayList<>(resultList);
    }


    @Deprecated
    public List<CockpitObjectCollectionModel> getWritableCollectionsForUser(UserModel userModel)
    {
        return findWritableCollectionsByUser(userModel);
    }


    public List<CockpitObjectCollectionModel> findWritableCollectionsByUser(UserModel userModel)
    {
        List<CockpitObjectCollectionModel> userOwnedCollections = getCollectionsByUser(userModel);
        Set<UserModel> userGroupsAndUser = userModel.getAllGroups();
        userGroupsAndUser.add(userModel);
        Map<Object, Object> queryParams = new HashMap<>();
        queryParams.put("users", userGroupsAndUser);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectCollection").append("} AS coll");
        builder.append("JOIN ").append(GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION)
                        .append("AS writeRelation ");
        builder.append("ON {coll.").append("pk")
                        .append("} = {writeRelation.target}} WHERE {writeRelation.source} IN (?users)");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectCollectionModel> userWritableCollectionsResult = getFlexibleSearchService().search(query);
        List<CockpitObjectCollectionModel> userWritableCollections = userWritableCollectionsResult.getResult();
        Set<CockpitObjectCollectionModel> resultList = new HashSet<>();
        resultList.addAll(userOwnedCollections);
        resultList.addAll(userWritableCollections);
        return new ArrayList<>(resultList);
    }


    @Deprecated
    public List<CockpitObjectSpecialCollectionModel> getSpecialCollectionsByUser(UserModel user)
    {
        return findSpecialCollectionsByUser(user);
    }


    public List<CockpitObjectSpecialCollectionModel> findSpecialCollectionsByUser(UserModel user)
    {
        Map<String, Object> queryParams = new HashMap<>(1);
        queryParams.put("user", user);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectSpecialCollection").append("} ");
        builder.append("WHERE {").append("user").append("} = ?user ");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectSpecialCollectionModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    @Deprecated
    public List<CockpitObjectSpecialCollectionModel> getSpecialCollections(UserModel user, CockpitSpecialCollectionType collectionType)
    {
        return findSpecialCollections(user, collectionType);
    }


    public List<CockpitObjectSpecialCollectionModel> findSpecialCollections(UserModel user, CockpitSpecialCollectionType collectionType)
    {
        Map<String, Object> queryParams = new HashMap<>(2);
        queryParams.put("user", user);
        queryParams.put("collectionType", collectionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectSpecialCollection").append("} ");
        builder.append("WHERE {").append("user").append("} = ?user ");
        builder.append("AND {").append("collectionType").append("} = ?collectionType ");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), queryParams);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectSpecialCollectionModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }


    @Deprecated
    public List<CockpitObjectAbstractCollectionModel> getGlobalCollections()
    {
        return findGlobalCollections();
    }


    public List<CockpitObjectAbstractCollectionModel> findGlobalCollections()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT {").append("pk").append("} ");
        builder.append("FROM {").append("CockpitObjectCollection").append("} ");
        builder.append("WHERE {").append("user").append("} IS NULL");
        FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), Collections.EMPTY_MAP);
        query.setNeedTotal(false);
        SearchResult<CockpitObjectAbstractCollectionModel> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }
}
