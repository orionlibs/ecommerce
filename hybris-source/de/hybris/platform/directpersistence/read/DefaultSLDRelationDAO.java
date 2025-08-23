package de.hybris.platform.directpersistence.read;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.core.LazyLoadItemList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.servicelayer.search.impl.LazyLoadModelList;
import de.hybris.platform.servicelayer.search.internal.resolver.ItemObjectResolver;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.StandardSearchResult;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSLDRelationDAO implements SLDRelationDAO
{
    private static final int DEFAULT_PREFETCH_SIZE = -1;
    private ItemObjectResolver modelResolver;
    private final Object typeServiceLock = new Object();
    private volatile TypeService typeService;


    public <T extends ItemModel> Collection<T> getRelatedModels(RelationInformation relationInfo)
    {
        return getRelatedObjects(relationInfo, (Class)ItemModel.class);
    }


    public <T> Collection<T> getRelatedObjects(RelationInformation relationInfo, Class<T> clazz)
    {
        Preconditions.checkNotNull(relationInfo, "relationInfo mustn't be null.");
        Objects.requireNonNull(clazz, "clazz is required");
        if(relationInfo.isOneToMany())
        {
            return getRelatedFromOneToManyRelation(relationInfo, clazz);
        }
        return getRalatedFromManyToManyRelation(relationInfo, clazz);
    }


    private <T> Collection<T> getRelatedFromOneToManyRelation(RelationInformation relationInfo, Class<T> clazz)
    {
        OneToManyHandler<?> handler = relationInfo.getOneToManyHandler();
        SearchResult searchResult = handler.searchForLinkedItems(relationInfo.gtPK());
        return prepareResultCollection(relationInfo, searchResult, clazz);
    }


    private String getOrderingAttributeQualifier(RelationInformation relationInfo)
    {
        if(relationInfo == null)
        {
            return null;
        }
        RelationMetaTypeModel relation = (RelationMetaTypeModel)getTypeService().getComposedTypeForCode(relationInfo.getRelationQualifier());
        AttributeDescriptorModel orderingAttribute = relation.getOrderingAttribute();
        return (orderingAttribute != null) ? orderingAttribute.getQualifier() : null;
    }


    private <T> Collection<T> getRalatedFromManyToManyRelation(RelationInformation relationInfo, Class<T> clazz)
    {
        PK pk = relationInfo.gtPK();
        boolean isSource = relationInfo.isSource();
        PK langPK = relationInfo.isLocalized() ? relationInfo.getLanguagePK() : null;
        boolean isSorted = relationInfo.isSorted();
        String qualifier = relationInfo.getRelationQualifier();
        SearchResult result = LinkManager.getInstance().getLinkedItems(pk, isSource, qualifier, langPK, true, 0, -1, isSorted, isSorted);
        return prepareResultCollection(relationInfo, result, clazz);
    }


    private <T> Collection<T> prepareResultCollection(RelationInformation relationInfo, SearchResult searchResult, Class<T> clazz)
    {
        List searchResultList = ((StandardSearchResult)searchResult).getOriginalResultList();
        if(!(searchResultList instanceof LazyLoadItemList))
        {
            return null;
        }
        LazyLoadItemList jaloList = (LazyLoadItemList)searchResultList;
        LazyLoadModelList enumsList = new LazyLoadModelList(jaloList, -1, Lists.newArrayList((Object[])new Class[] {clazz}, ), this.modelResolver);
        if(!relationInfo.isSetExpexted())
        {
            return (Collection<T>)enumsList;
        }
        if(relationInfo.isSorted())
        {
            return new LinkedHashSet<>((Collection<? extends T>)enumsList);
        }
        return new HashSet<>((Collection<? extends T>)enumsList);
    }


    private <T extends ItemModel> Collection<T> prepareResultCollection(RelationInformation relationInfo, SearchResult searchResult)
    {
        List searchResultList = ((StandardSearchResult)searchResult).getOriginalResultList();
        if(!(searchResultList instanceof LazyLoadItemList))
        {
            return null;
        }
        LazyLoadItemList jaloList = (LazyLoadItemList)searchResultList;
        LazyLoadModelList modelsList = new LazyLoadModelList(jaloList, -1, Lists.newArrayList((Object[])new Class[] {Item.class}, ), this.modelResolver);
        if(!relationInfo.isSetExpexted())
        {
            return (Collection<T>)modelsList;
        }
        if(relationInfo.isSorted())
        {
            return new LinkedHashSet<>((Collection<? extends T>)modelsList);
        }
        return new HashSet<>((Collection<? extends T>)modelsList);
    }


    @Required
    public void setModelResolver(ItemObjectResolver modelResolver)
    {
        this.modelResolver = modelResolver;
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            synchronized(this.typeServiceLock)
            {
                if(this.typeService == null)
                {
                    this.typeService = lookupTypeService();
                }
            }
        }
        return this.typeService;
    }


    public TypeService lookupTypeService()
    {
        throw new UnsupportedOperationException("please override DefaultSLDRelationDAO.lookupTypeService() or use <lookup-method>");
    }
}
