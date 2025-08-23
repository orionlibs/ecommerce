package de.hybris.platform.cockpit.impl;

import de.hybris.platform.cockpit.CockpitCollectionService;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.daos.CockpitObjectAbstractCollectionDao;
import de.hybris.platform.cockpit.enums.CockpitSpecialCollectionType;
import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.cockpit.model.CockpitObjectSpecialCollectionModel;
import de.hybris.platform.cockpit.model.ObjectCollectionElementModel;
import de.hybris.platform.cockpit.model.ObjectCollectionItemReferenceModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCockpitCollectionService implements CockpitCollectionService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitCollectionService.class);
    private CockpitObjectAbstractCollectionDao objectCollectionDao;
    private EnumerationService enumerationService;
    private ModelService modelService;


    public int addToCollection(CockpitObjectAbstractCollectionModel collModel, List<ItemModel> elements)
    {
        ServicesUtil.validateParameterNotNull(collModel, "Given collection model must not be null");
        int counter = 0;
        for(ItemModel element : elements)
        {
            boolean alreadyInCollection = getObjectCollectionDao().collectionContains(collModel, element);
            if(!alreadyInCollection)
            {
                counter++;
                ObjectCollectionItemReferenceModel collectionItemReferenceModel = (ObjectCollectionItemReferenceModel)getModelService().create(ObjectCollectionItemReferenceModel.class);
                collectionItemReferenceModel.setCollection(collModel);
                collectionItemReferenceModel.setItem(element);
                collectionItemReferenceModel.setObjectTypeCode(element.getItemtype());
                getModelService().save(collectionItemReferenceModel);
            }
        }
        if(counter > 0)
        {
            getModelService().refresh(collModel);
        }
        return counter;
    }


    public int removeFromCollection(CockpitObjectAbstractCollectionModel collectionModel, List<ItemModel> elements)
    {
        ServicesUtil.validateParameterNotNull(collectionModel, "Parameter 'collectionModel' cannot be null");
        ServicesUtil.validateParameterNotNull(elements, "Parameter 'elements' cannot be null");
        int counter = 0;
        for(ItemModel element : elements)
        {
            List<ObjectCollectionItemReferenceModel> containingElements = getObjectCollectionDao().findElementsContainingItem(collectionModel, element);
            for(ObjectCollectionItemReferenceModel oce : containingElements)
            {
                getModelService().remove(oce);
                counter++;
            }
        }
        if(counter > 0)
        {
            getModelService().refresh(collectionModel);
        }
        return counter;
    }


    public List<ItemModel> getElements(CockpitObjectAbstractCollectionModel collModel, int start, int count)
    {
        ServicesUtil.validateParameterNotNull(collModel, "Parameter 'collModel' cannot be null");
        if(!getModelService().isNew(collModel) && !getModelService().isRemoved(collModel))
        {
            List<ObjectCollectionItemReferenceModel> elements = getObjectCollectionDao().findElements(collModel, start, count);
            if(elements.isEmpty())
            {
                return Collections.EMPTY_LIST;
            }
            List<ItemModel> result = new ArrayList<>(elements.size());
            for(ObjectCollectionElementModel element : elements)
            {
                ItemModel item = ((ObjectCollectionItemReferenceModel)element).getItem();
                if(item != null)
                {
                    result.add(item);
                }
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }


    public int removeNullItemReferences(CockpitObjectAbstractCollectionModel collModel)
    {
        ServicesUtil.validateParameterNotNull(collModel, "Parameter 'collModel' cannot be null");
        int removedElements = 0;
        if(!getModelService().isNew(collModel) && !getModelService().isRemoved(collModel))
        {
            List<ObjectCollectionItemReferenceModel> elements = getObjectCollectionDao().findElements(collModel, 0,
                            Config.getInt("cockpit.collections.max_element_size", -1));
            if(!elements.isEmpty())
            {
                for(ObjectCollectionElementModel element : elements)
                {
                    ItemModel item = ((ObjectCollectionItemReferenceModel)element).getItem();
                    if(item == null)
                    {
                        getModelService().remove(element);
                        removedElements++;
                    }
                }
            }
        }
        return removedElements;
    }


    public int getElementCount(CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collModel' cannot be null");
        return getObjectCollectionDao().getElementCount(collection);
    }


    public CockpitObjectAbstractCollectionModel cloneCollection(CockpitObjectAbstractCollectionModel collection, UserModel user)
    {
        CockpitObjectAbstractCollectionModel collectionClone = null;
        if("CockpitObjectSpecialCollection".equals(collection.getItemtype()))
        {
            collectionClone = (CockpitObjectAbstractCollectionModel)getModelService().create(CockpitObjectSpecialCollectionModel.class);
        }
        else
        {
            collectionClone = (CockpitObjectAbstractCollectionModel)getModelService().create(CockpitObjectCollectionModel.class);
        }
        collectionClone.setQualifier(collection.getQualifier());
        collectionClone.setUser(user);
        collectionClone.setLabel(collection.getLabel());
        getModelService().save(collectionClone);
        addToCollection(collectionClone, getElements(collection, 0, collection.getElements().size()));
        getModelService().refresh(collectionClone);
        return collectionClone;
    }


    public List<CockpitObjectAbstractCollectionModel> getCollectionsForUser(UserModel user)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        getModelService().refresh(user);
        List<CockpitObjectAbstractCollectionModel> ret = new ArrayList<>();
        Set<CockpitObjectAbstractCollectionModel> collectionModels = new HashSet<>();
        collectionModels.addAll(getObjectCollectionDao().findCollectionsByUser(user));
        collectionModels.addAll(getObjectCollectionDao().findReadableCollectionsByUser(user));
        collectionModels.addAll(getObjectCollectionDao().findGlobalCollections());
        ret.addAll(collectionModels);
        return ret;
    }


    public List<CockpitObjectSpecialCollectionModel> getSpecialCollectionsForUser(UserModel user)
    {
        return getObjectCollectionDao().findSpecialCollectionsByUser(user);
    }


    public List<CockpitObjectSpecialCollectionModel> getSpecialCollections(UserModel user, String collectionType)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(collectionType, "Parameter 'collectionType' cannot be null");
        return getObjectCollectionDao().findSpecialCollections(user, (CockpitSpecialCollectionType)
                        getEnumerationService().getEnumerationValue(GeneratedCockpitConstants.TC.COCKPITSPECIALCOLLECTIONTYPE, collectionType));
    }


    public boolean hasReadCollectionRight(PrincipalModel principal, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        Set<PrincipalGroupModel> allGroups = principal.getAllGroups();
        Collection<PrincipalModel> readUsers = collection.getReadPrincipals();
        if(principal.equals(collection.getUser()) || readUsers.contains(principal) ||
                        !CollectionUtils.intersection(allGroups, readUsers).isEmpty())
        {
            return true;
        }
        if(hasWriteCollectionRight(principal, collection))
        {
            return true;
        }
        return false;
    }


    public boolean hasWriteCollectionRight(PrincipalModel principal, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        if(principal.equals(collection.getUser()))
        {
            return true;
        }
        Set<PrincipalGroupModel> allGroups = principal.getAllGroups();
        Collection<PrincipalModel> writeUsers = collection.getWritePrincipals();
        if(writeUsers.contains(principal) || !CollectionUtils.intersection(allGroups, writeUsers).isEmpty())
        {
            return true;
        }
        return false;
    }


    public boolean isInCollection(ItemModel object, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(object, "Parameter 'object' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        return getObjectCollectionDao().collectionContains(collection, object);
    }


    public void addReadUser(PrincipalModel user, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Given collection must not be null");
        if(!user.getReadCollections().contains(collection))
        {
            Collection<CockpitObjectAbstractCollectionModel> userReadCollections = new ArrayList<>(user.getReadCollections());
            userReadCollections.add(collection);
            user.setReadCollections(userReadCollections);
            getModelService().save(user);
        }
    }


    public void addWriteUser(PrincipalModel user, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        if(!user.getWriteCollections().contains(collection))
        {
            Collection<CockpitObjectAbstractCollectionModel> userWriteCollections = new ArrayList<>(user.getWriteCollections());
            userWriteCollections.add(collection);
            user.setWriteCollections(userWriteCollections);
            getModelService().save(user);
        }
    }


    public void removeReadUser(PrincipalModel user, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        if(user.getReadCollections().contains(collection))
        {
            Collection<CockpitObjectAbstractCollectionModel> userReadCollections = new ArrayList<>(user.getReadCollections());
            userReadCollections.remove(collection);
            user.setReadCollections(userReadCollections);
            getModelService().save(user);
        }
    }


    public void removeWriteUser(PrincipalModel user, CockpitObjectAbstractCollectionModel collection)
    {
        ServicesUtil.validateParameterNotNull(user, "Parameter 'user' cannot be null");
        ServicesUtil.validateParameterNotNull(collection, "Parameter 'collection' cannot be null");
        if(user.getWriteCollections().contains(collection))
        {
            Collection<CockpitObjectAbstractCollectionModel> userWriteCollections = new ArrayList<>(user.getWriteCollections());
            userWriteCollections.remove(collection);
            user.setWriteCollections(userWriteCollections);
            getModelService().save(user);
        }
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


    protected CockpitObjectAbstractCollectionDao getObjectCollectionDao()
    {
        return this.objectCollectionDao;
    }


    @Required
    public void setObjectCollectionDao(CockpitObjectAbstractCollectionDao objectCollectionDao)
    {
        this.objectCollectionDao = objectCollectionDao;
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
