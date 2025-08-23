package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.cockpit.CockpitCollectionService;
import de.hybris.platform.cockpit.daos.CockpitObjectAbstractCollectionDao;
import de.hybris.platform.cockpit.enums.CockpitSpecialCollectionType;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.CockpitObjectAbstractCollection;
import de.hybris.platform.cockpit.jalo.CockpitObjectSpecialCollection;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.cockpit.model.ObjectCollectionElementModel;
import de.hybris.platform.cockpit.model.ObjectCollectionItemReferenceModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.collection.impl.ObjectCollectionImpl;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ObjectCollectionServiceImpl extends AbstractServiceImpl implements ObjectCollectionService
{
    private static final Logger LOG = LoggerFactory.getLogger(ObjectCollectionServiceImpl.class);
    protected CockpitObjectAbstractCollectionDao objectCollectionDao;
    private EnumerationService enumerationService;
    private CockpitCollectionService cockpitCollectionService;
    private List<String> objectTypeFilterList;


    public void addReadUser(PrincipalModel user, ObjectCollection collection)
    {
        getCockpitCollectionService().addReadUser(user, unmapToModel(collection));
    }


    public int addToCollection(ObjectCollection collection, Collection<TypedObject> elements) throws IllegalStateException
    {
        return getCockpitCollectionService().addToCollection(unmapToModel(collection), getTypeService().unwrapItems(elements));
    }


    public List<TypedObject> getElements(ObjectCollection collection, int start, int count)
    {
        List<TypedObject> result = new ArrayList<>();
        for(ItemModel model : getCockpitCollectionService().getElements(unmapToModel(collection), start, count))
        {
            result.add(getTypeService().wrapItem(model));
        }
        return result;
    }


    public int removeNullItemReferences(ObjectCollection collection)
    {
        return getCockpitCollectionService().removeNullItemReferences(unmapToModel(collection));
    }


    public int getElementCount(ObjectCollection collection)
    {
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        CockpitObjectAbstractCollectionModel collModel = unmapToModel(collection);
        if(collModel != null && !getModelService().isNew(collModel) && !getModelService().isRemoved(collModel))
        {
            return getObjectCollectionDao().getElementCount(collModel);
        }
        return 0;
    }


    public void addWriteUser(PrincipalModel user, ObjectCollection collection)
    {
        getCockpitCollectionService().addWriteUser(user, unmapToModel(collection));
    }


    public ObjectCollection cloneCollection(ObjectCollection collection, UserModel user)
    {
        return mapCollection(getCockpitCollectionService().cloneCollection(unmapToModel(collection), user));
    }


    public ObjectCollection createCollection(String qualifier, UserModel user)
    {
        CockpitObjectCollectionModel collection = (CockpitObjectCollectionModel)getModelService().create(CockpitObjectCollectionModel.class);
        collection.setQualifier(qualifier);
        collection.setUser(user);
        getModelService().save(collection);
        return mapCollection((CockpitObjectAbstractCollectionModel)collection);
    }


    public ObjectCollection createSpecialCollection(String qualifier, UserModel user)
    {
        CockpitObjectSpecialCollectionModel collection = (CockpitObjectSpecialCollectionModel)getModelService().create(CockpitObjectSpecialCollectionModel.class);
        collection.setQualifier(qualifier);
        collection.setUser(user);
        getModelService().save(collection);
        return mapCollection((CockpitObjectAbstractCollectionModel)collection);
    }


    public ObjectCollection createSpecialCollection(String qualifier, UserModel user, EnumerationValue collectionType)
    {
        Map<String, Object> attr = new HashMap<>();
        attr.put("qualifier", qualifier);
        attr.put("user", this.modelService.getSource(user));
        attr.put("collectionType", collectionType);
        CockpitObjectSpecialCollection coc = CockpitManager.getInstance().createCockpitObjectSpecialCollection(attr);
        coc.setCollectionType(collectionType);
        return mapCollection((CockpitObjectAbstractCollection)coc);
    }


    public ObjectCollection createSpecialCollection(String qualifier, UserModel user, HybrisEnumValue collectionType)
    {
        CockpitObjectSpecialCollectionModel collection = (CockpitObjectSpecialCollectionModel)getModelService().create(CockpitObjectSpecialCollectionModel.class);
        collection.setQualifier(qualifier);
        collection.setUser(user);
        collection.setCollectionType((CockpitSpecialCollectionType)collectionType);
        getModelService().save(collection);
        return mapCollection((CockpitObjectAbstractCollectionModel)collection);
    }


    public List<ObjectCollection> getCollections(UserModel user)
    {
        getModelService().refresh(user);
        List<ObjectCollection> ret = new ArrayList<>();
        Collection<CockpitObjectAbstractCollection> collections = CockpitManager.getInstance().getAllCollections((User)this.modelService.getSource(user));
        collections = filterCollections(collections);
        buildCollections(collections, ret);
        collections = CockpitManager.getInstance().getGlobalCollections();
        collections = filterCollections(collections);
        buildCollections(collections, ret);
        return ret;
    }


    public List<PrincipalModel> getReadUsers(ObjectCollection collection)
    {
        List<PrincipalModel> readUsers = new ArrayList<>();
        CockpitObjectAbstractCollection coll = unmapCollection(collection);
        getModelService().getAll(coll.getReadPrincipals(), readUsers);
        return readUsers;
    }


    public List<ObjectCollection> getSpecialCollections(UserModel user)
    {
        List<ObjectCollection> ret = new ArrayList<>();
        Collection<CockpitObjectAbstractCollection> collections = CockpitManager.getInstance().getSpecialCollections((User)this.modelService.getSource(user));
        return buildCollections(collections, ret);
    }


    public List<ObjectCollection> getSpecialCollections(UserModel user, String collectionType)
    {
        return mapCollections(getCockpitCollectionService().getSpecialCollections(user, collectionType));
    }


    public List<PrincipalModel> getWriteUsers(ObjectCollection collection)
    {
        List<PrincipalModel> writeUsers = new ArrayList<>();
        CockpitObjectAbstractCollection coll = unmapCollection(collection);
        getModelService().getAll(coll.getWritePrincipals(), writeUsers);
        return writeUsers;
    }


    public Boolean isCollectionOwner(PrincipalModel principal, ObjectCollection collection)
    {
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        if(collection.getUser() != null && collection.getUser().equals(principal))
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    public Boolean hasWriteCollectionRight(PrincipalModel principal, ObjectCollection collection)
    {
        return Boolean.valueOf(getCockpitCollectionService().hasWriteCollectionRight(principal, unmapToModel(collection)));
    }


    public Boolean hasReadCollectionRight(PrincipalModel principal, ObjectCollection collection)
    {
        return Boolean.valueOf(getCockpitCollectionService().hasReadCollectionRight(principal, unmapToModel(collection)));
    }


    @Deprecated
    public ObjectCollection mapCollection(CockpitObjectAbstractCollection source)
    {
        return (ObjectCollection)new ObjectCollectionImpl((CockpitObjectAbstractCollectionModel)getModelService().get(source.getPK()));
    }


    private ObjectCollection mapCollection(CockpitObjectAbstractCollectionModel collectionModel)
    {
        return (ObjectCollection)new ObjectCollectionImpl(collectionModel);
    }


    private List<ObjectCollection> mapCollections(Collection<? extends CockpitObjectAbstractCollectionModel> collections)
    {
        List<ObjectCollection> result = new ArrayList<>(collections.size());
        for(CockpitObjectAbstractCollectionModel collection : collections)
        {
            result.add(mapCollection(collection));
        }
        return result;
    }


    public void publishCollection(ObjectCollection collection)
    {
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        CockpitObjectAbstractCollectionModel collectionModel = unmapToModel(collection);
        collectionModel.setUser(null);
        getModelService().save(collectionModel);
        ((ObjectCollectionImpl)collection).setUser(null);
    }


    public ObjectCollection refreshCollection(ObjectCollection collection)
    {
        return collection;
    }


    public Boolean isInCollection(TypedObject object, ObjectCollection collection)
    {
        ServicesUtil.validateParameterNotNull(object, "Parameter 'object' cannot be null");
        return
                        Boolean.valueOf(getCockpitCollectionService().isInCollection((ItemModel)object.getObject(), unmapToModel(collection)));
    }


    public void removeCollection(ObjectCollection collection)
    {
        getModelService().remove(collection.getPK());
    }


    public int removeFromCollection(ObjectCollection collection, Collection<TypedObject> elements)
    {
        return getCockpitCollectionService().removeFromCollection(unmapToModel(collection), getTypeService().unwrapItems(elements));
    }


    public void removeReadUser(PrincipalModel user, ObjectCollection collection)
    {
        getCockpitCollectionService().removeReadUser(user, unmapToModel(collection));
    }


    public void removeWriteUser(PrincipalModel user, ObjectCollection collection)
    {
        getCockpitCollectionService().removeWriteUser(user, unmapToModel(collection));
    }


    public void renameCollection(ObjectCollection collection, String label)
    {
        CockpitObjectAbstractCollectionModel collectionModel = unmapToModel(collection);
        collectionModel.setLabel(label);
        getModelService().save(collectionModel);
        ((ObjectCollectionImpl)collection).setLabel(label);
    }


    public void setObjectTypeFilterList(List<String> objectTypeFilterList)
    {
        this.objectTypeFilterList = objectTypeFilterList;
    }


    protected Set<String> getObjectTypeFilterSet()
    {
        Set<String> objectTypeFilterSet = new HashSet<>();
        if(this.objectTypeFilterList != null)
        {
            for(String string : this.objectTypeFilterList)
            {
                String[] typeRule = string.split(":");
                if(typeRule.length > 1 && "s".equals(typeRule[1]))
                {
                    ObjectType filterType = getTypeService().getObjectType(typeRule[0]);
                    if(filterType == null)
                    {
                        LOG.error("Wrong filtertype configuration: type '" + typeRule[0] + "' not found.");
                        continue;
                    }
                    for(ObjectType subtype : getTypeService().getAllSubtypes(filterType))
                    {
                        objectTypeFilterSet.add(subtype.getCode());
                    }
                    continue;
                }
                if(typeRule.length == 1)
                {
                    objectTypeFilterSet.add(typeRule[0]);
                    continue;
                }
                LOG.error("filter type '" + string + "' invalid.");
            }
        }
        return objectTypeFilterSet;
    }


    @Deprecated
    protected CockpitObjectAbstractCollection unmapCollection(ObjectCollection collection)
    {
        CockpitObjectAbstractCollection ret = null;
        try
        {
            ret = (CockpitObjectAbstractCollection)JaloSession.getCurrentSession().getItem(collection.getPK());
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.error("Collection item PK: " + collection.getPK() + " not found!", (Throwable)e);
        }
        return ret;
    }


    protected CockpitObjectAbstractCollectionModel unmapToModel(ObjectCollection collection)
    {
        CockpitObjectAbstractCollectionModel ret = null;
        try
        {
            ret = (CockpitObjectAbstractCollectionModel)getModelService().get(collection.getPK());
        }
        catch(ModelLoadingException mle)
        {
            LOG.error("Collection item PK: " + collection.getPK() + " not found!", (Throwable)mle);
        }
        return ret;
    }


    @Deprecated
    private <T extends Collection> T buildCollections(Collection<? extends CockpitObjectAbstractCollection> collections, T result)
    {
        for(CockpitObjectAbstractCollection collection : collections)
        {
            result.add(mapCollection(collection));
        }
        return result;
    }


    @Deprecated
    private Collection<CockpitObjectAbstractCollection> filterCollections(Collection<CockpitObjectAbstractCollection> collectionIn)
    {
        LazyLoadItemList<CockpitObjectAbstractCollection> lazyLoadItemList = new LazyLoadItemList();
        for(CockpitObjectAbstractCollection c : collectionIn)
        {
            ObjectCollectionElementModel element = getFirstElement((CockpitObjectAbstractCollectionModel)
                            getModelService().get(c.getPK()));
            if(element != null)
            {
                String objectTypeCode = element.getObjectTypeCode();
                Set<String> objectTypeFilterSet = getObjectTypeFilterSet();
                if(CollectionUtils.isNotEmpty(objectTypeFilterSet) && objectTypeFilterSet.contains(objectTypeCode))
                {
                    lazyLoadItemList.add(c);
                }
                continue;
            }
            lazyLoadItemList.add(c);
        }
        return (Collection<CockpitObjectAbstractCollection>)lazyLoadItemList;
    }


    private Collection<CockpitObjectAbstractCollectionModel> filterCollectionModels(Collection<? extends CockpitObjectAbstractCollectionModel> collectionsToFilter)
    {
        Collection<CockpitObjectAbstractCollectionModel> filteredList = new ArrayList<>();
        for(CockpitObjectAbstractCollectionModel collectionModel : collectionsToFilter)
        {
            ObjectCollectionElementModel element = getFirstElement(collectionModel);
            if(element != null)
            {
                String objectTypeCode = element.getObjectTypeCode();
                Set<String> objectTypeFilterSet = getObjectTypeFilterSet();
                if(CollectionUtils.isNotEmpty(objectTypeFilterSet) && objectTypeFilterSet.contains(objectTypeCode))
                {
                    filteredList.add(collectionModel);
                }
                continue;
            }
            filteredList.add(collectionModel);
        }
        return filteredList;
    }


    private ObjectCollectionElementModel getFirstElement(CockpitObjectAbstractCollectionModel collection)
    {
        List<ObjectCollectionItemReferenceModel> result = getObjectCollectionDao().findElements(collection, 0, 1);
        return result.isEmpty() ? null : result.iterator().next();
    }


    @Required
    public void setObjectCollectionDao(CockpitObjectAbstractCollectionDao dao)
    {
        this.objectCollectionDao = dao;
    }


    protected CockpitObjectAbstractCollectionDao getObjectCollectionDao()
    {
        return this.objectCollectionDao;
    }


    protected EnumerationService getEnumerationService()
    {
        return this.enumerationService;
    }


    @Required
    public void setEnumerationService(EnumerationService enumerationService)
    {
        this.enumerationService = enumerationService;
    }


    protected CockpitCollectionService getCockpitCollectionService()
    {
        return this.cockpitCollectionService;
    }


    @Required
    public void setCockpitCollectionService(CockpitCollectionService cockpitCollectionService)
    {
        this.cockpitCollectionService = cockpitCollectionService;
    }


    public List<ObjectCollection> getCollectionsForUser(UserModel user)
    {
        return mapCollections(filterCollectionModels(getCockpitCollectionService().getCollectionsForUser(user)));
    }


    public List<ObjectCollection> getSpecialCollectionsForUser(UserModel user)
    {
        return mapCollections(getCockpitCollectionService().getSpecialCollectionsForUser(user));
    }


    public List<PrincipalModel> getReadUsersForCollection(ObjectCollection collection)
    {
        return new ArrayList<>(unmapToModel(collection).getReadPrincipals());
    }


    public List<PrincipalModel> getWriteUsersForCollection(ObjectCollection collection)
    {
        return new ArrayList<>(unmapToModel(collection).getWritePrincipals());
    }
}
