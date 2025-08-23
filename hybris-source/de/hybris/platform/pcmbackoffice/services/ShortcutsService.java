package de.hybris.platform.pcmbackoffice.services;

import com.hybris.backoffice.enums.BackofficeSpecialCollectionType;
import com.hybris.backoffice.model.BackofficeObjectCollectionItemReferenceModel;
import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class ShortcutsService
{
    private UserService userService;
    private ModelService modelService;
    private GenericSearchService genericSearchService;
    private TypeService typeService;
    private static final String ENUMERATION_CODE = "BackofficeSpecialCollectionType";


    public List<PK> getAllCollectionList(BackofficeObjectSpecialCollectionModel collectionModel)
    {
        checkCollectionItemIfNull(collectionModel);
        GenericQuery genericQuery = new GenericQuery("BackofficeObjectCollectionItemReference");
        genericQuery.addSelectField(new GenericSelectField("product", PK.class));
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("collectionPk"), Operator.EQUAL, collectionModel));
        GenericConditionList allConditions = GenericCondition.createConditionList(conditions, Operator.AND);
        genericQuery.addCondition((GenericCondition)allConditions);
        GenericSearchQuery query = new GenericSearchQuery(genericQuery);
        query.setNeedTotal(true);
        query.setStart(0);
        SearchResult<PK> searchResult = getGenericSearchService().search(query);
        if(searchResult == null)
        {
            return Collections.emptyList();
        }
        return searchResult.getResult();
    }


    private void checkCollectionItemIfNull(BackofficeObjectSpecialCollectionModel collectionModel)
    {
        GenericQuery genericQuery = new GenericQuery("BackofficeObjectCollectionItemReference");
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("collectionPk"), Operator.EQUAL, collectionModel));
        GenericConditionList allConditions = GenericCondition.createConditionList(conditions, Operator.AND);
        genericQuery.addCondition((GenericCondition)allConditions);
        GenericSearchQuery query = new GenericSearchQuery(genericQuery);
        query.setNeedTotal(true);
        query.setStart(0);
        SearchResult<BackofficeObjectCollectionItemReferenceModel> searchResult = getGenericSearchService().search(query);
        if(searchResult != null && CollectionUtils.isNotEmpty(searchResult.getResult()))
        {
            List<BackofficeObjectCollectionItemReferenceModel> result = searchResult.getResult();
            for(BackofficeObjectCollectionItemReferenceModel item : result)
            {
                if(item.getProduct() == null)
                {
                    getModelService().remove(item);
                }
            }
        }
    }


    public void insertProductToCollectionlist(ProductModel product, BackofficeObjectSpecialCollectionModel collectionModel)
    {
        BackofficeObjectCollectionItemReferenceModel collectionItemReferenceModel = (BackofficeObjectCollectionItemReferenceModel)getModelService().create(BackofficeObjectCollectionItemReferenceModel.class);
        collectionItemReferenceModel.setCollectionPk(collectionModel);
        collectionItemReferenceModel.setProduct(product);
        getModelService().save(collectionItemReferenceModel);
    }


    public void deleteProductFromCollectionlist(ProductModel product, BackofficeObjectSpecialCollectionModel collectionModel)
    {
        GenericQuery genericQuery = new GenericQuery("BackofficeObjectCollectionItemReference");
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("collectionPk"), Operator.EQUAL, collectionModel));
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("product"), Operator.EQUAL, product));
        GenericConditionList allConditions = GenericCondition.createConditionList(conditions, Operator.AND);
        genericQuery.addCondition((GenericCondition)allConditions);
        SearchResult<BackofficeObjectCollectionItemReferenceModel> result = getGenericSearchService().search(new GenericSearchQuery(genericQuery));
        List<BackofficeObjectCollectionItemReferenceModel> containingElements = result.getResult();
        for(BackofficeObjectCollectionItemReferenceModel oce : containingElements)
        {
            getModelService().remove(oce);
        }
    }


    private BackofficeObjectSpecialCollectionModel getCollection(UserModel currentUser, HybrisEnumValue collectionType)
    {
        GenericQuery genericQuery = new GenericQuery("BackofficeObjectSpecialCollection");
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("user"), Operator.EQUAL, currentUser));
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("collectionType"), Operator.EQUAL, collectionType));
        GenericConditionList allConditions = GenericCondition.createConditionList(conditions, Operator.AND);
        genericQuery.addCondition((GenericCondition)allConditions);
        SearchResult<Object> result = getGenericSearchService().search(new GenericSearchQuery(genericQuery));
        if(CollectionUtils.isEmpty(result.getResult()))
        {
            return null;
        }
        return result.getResult().get(0);
    }


    public BackofficeObjectSpecialCollectionModel initCollection(String collectionCode)
    {
        EnumerationValueModel evm = getTypeService().getEnumerationValue("BackofficeSpecialCollectionType", collectionCode);
        HybrisEnumValue collectionType = (HybrisEnumValue)getModelService().get(evm.getPk());
        UserModel currentUser = getUserService().getCurrentUser();
        BackofficeObjectSpecialCollectionModel collectionModel = getCollection(currentUser, collectionType);
        if(collectionModel == null)
        {
            BackofficeObjectSpecialCollectionModel collection = (BackofficeObjectSpecialCollectionModel)getModelService().create(BackofficeObjectSpecialCollectionModel.class);
            collection.setUser(currentUser);
            collection.setCollectionType((BackofficeSpecialCollectionType)collectionType);
            getModelService().save(collection);
            return collection;
        }
        return collectionModel;
    }


    public boolean collectionContainsItem(ProductModel product, BackofficeObjectSpecialCollectionModel collectionModel)
    {
        GenericQuery genericQuery = new GenericQuery("BackofficeObjectCollectionItemReference");
        List<GenericCondition> conditions = new ArrayList<>();
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("collectionPk"), Operator.EQUAL, collectionModel));
        conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("product"), Operator.EQUAL, product));
        GenericConditionList allConditions = GenericCondition.createConditionList(conditions, Operator.AND);
        genericQuery.addCondition((GenericCondition)allConditions);
        SearchResult<Object> result = getGenericSearchService().search(new GenericSearchQuery(genericQuery));
        return (result.getCount() > 0);
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public GenericSearchService getGenericSearchService()
    {
        return this.genericSearchService;
    }


    @Required
    public void setGenericSearchService(GenericSearchService genericSearchService)
    {
        this.genericSearchService = genericSearchService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
