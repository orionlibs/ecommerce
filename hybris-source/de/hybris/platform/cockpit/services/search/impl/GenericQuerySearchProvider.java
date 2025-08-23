package de.hybris.platform.cockpit.services.search.impl;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemTemplate;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Facet;
import de.hybris.platform.cockpit.model.search.FacetsResult;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.Query;
import de.hybris.platform.cockpit.model.search.ResultObject;
import de.hybris.platform.cockpit.model.search.SearchParameterValue;
import de.hybris.platform.cockpit.model.search.SearchType;
import de.hybris.platform.cockpit.model.search.impl.DefaultExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.impl.ItemSearchType;
import de.hybris.platform.cockpit.model.search.impl.ResultObjectWrapper;
import de.hybris.platform.cockpit.model.template.CockpitItemTemplateModel;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.ConditionTranslator;
import de.hybris.platform.cockpit.services.search.ConditionTranslatorContext;
import de.hybris.platform.cockpit.services.search.GenericQueryConditionTranslator;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSearchOrderBy;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.genericsearch.GenericSearchQuery;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.spring.SpringUtil;

public class GenericQuerySearchProvider extends AbstractSearchProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(GenericQuerySearchProvider.class);
    private SearchType defaultRootType;
    private TypeService typeService;
    private ObjectCollectionService objectCollectionService;
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private GenericSearchService genericSearchService;
    private CommonI18NService commonI18NService;
    private TypeService slayerTypeService;


    public SearchType getDefaultRootType()
    {
        return this.defaultRootType;
    }


    public void setDefaultRootType(SearchType defaultRootType)
    {
        this.defaultRootType = defaultRootType;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getSlayerTypeService()
    {
        if(this.slayerTypeService == null)
        {
            this
                            .slayerTypeService = (TypeService)Registry.getApplicationContext().getBean("typeService");
        }
        return this.slayerTypeService;
    }


    @Deprecated
    protected BaseType matchObjectType(Item result)
    {
        BaseType ret = null;
        for(ComposedType t = result.getComposedType(); ret == null && t != null; t = t.getSuperType())
        {
            ret = getTypeService().getBaseType(t.getCode());
        }
        if(ret == null)
        {
            throw new IllegalStateException("cannot match object type for " + result + " ( type = " + result
                            .getComposedType().getCode() + " )");
        }
        return ret;
    }


    protected BaseType matchObjectType(ItemModel result)
    {
        BaseType ret = null;
        ComposedTypeModel typeModel = getSlayerTypeService().getComposedTypeForCode(result.getItemtype());
        for(; ret == null && typeModel != null; typeModel = typeModel.getSuperType())
        {
            ret = getTypeService().getBaseType(typeModel.getCode());
        }
        if(ret == null)
        {
            throw new IllegalStateException("cannot match object type for " + result);
        }
        return ret;
    }


    @Deprecated
    protected Set<ComposedType> getPermittedTypes(ComposedType rootType, Set<SearchType> types, boolean exludeSubTypes)
    {
        Set<ComposedType> requiredTypes = new HashSet<>();
        requiredTypes.add(rootType);
        requiredTypes.addAll(rootType.getAllSubTypes());
        Set<ComposedType> selectedTypes = new LinkedHashSet<>();
        for(ObjectType objectType : types)
        {
            ObjectType loadedType = objectType;
            ComposedType composedType = null;
            if(objectType instanceof ItemType)
            {
                composedType = (ComposedType)this.modelService.getSource(((ItemType)objectType).getComposedType());
            }
            else if(objectType instanceof ItemTemplate)
            {
                composedType = (ComposedType)this.modelService.getSource(((ItemTemplate)objectType).getBaseType().getComposedType());
            }
            else
            {
                loadedType = getTypeService().getObjectType(objectType.getCode());
                if(loadedType instanceof ItemType)
                {
                    composedType = (ComposedType)this.modelService.getSource(((ItemType)loadedType).getComposedType());
                }
                else if(loadedType instanceof ItemTemplate)
                {
                    composedType = (ComposedType)this.modelService.getSource(((ItemTemplate)loadedType).getBaseType().getComposedType());
                }
                else
                {
                    LOG.warn("Could not get resolve composed type from specified type '" + objectType + "'.");
                }
            }
            if(composedType != null)
            {
                selectedTypes.add(composedType);
                if(loadedType.getSubtypes().isEmpty())
                {
                    selectedTypes.addAll(composedType.getAllSubTypes());
                }
            }
        }
        if(exludeSubTypes)
        {
            requiredTypes.retainAll(selectedTypes);
        }
        return requiredTypes;
    }


    protected Set<ComposedTypeModel> getPermittedTypes(ComposedTypeModel rootType, Set<SearchType> types, boolean exludeSubTypes)
    {
        Set<ComposedTypeModel> requiredTypes = new HashSet<>();
        requiredTypes.add(rootType);
        if(!exludeSubTypes)
        {
            requiredTypes.addAll(rootType.getAllSubTypes());
        }
        Set<ComposedTypeModel> selectedTypes = new LinkedHashSet<>();
        for(ObjectType objectType : types)
        {
            ObjectType loadedType = objectType;
            ComposedTypeModel composedType = null;
            if(objectType instanceof ItemType)
            {
                composedType = ((ItemType)objectType).getComposedType();
            }
            else if(objectType instanceof ItemTemplate)
            {
                composedType = ((ItemTemplate)objectType).getBaseType().getComposedType();
            }
            else
            {
                loadedType = getTypeService().getObjectType(objectType.getCode());
                if(loadedType instanceof ItemType)
                {
                    composedType = ((ItemType)loadedType).getComposedType();
                }
                else if(loadedType instanceof ItemTemplate)
                {
                    composedType = ((ItemTemplate)loadedType).getBaseType().getComposedType();
                }
                else
                {
                    LOG.warn("Could not get resolve composed type from specified type '" + objectType + "'.");
                }
            }
            if(composedType != null)
            {
                selectedTypes.add(composedType);
                if(loadedType.getSubtypes().isEmpty())
                {
                    selectedTypes.addAll(composedType.getAllSubTypes());
                }
            }
        }
        return requiredTypes;
    }


    @Deprecated
    protected ComposedType extractRootType(Collection<GenericSearchParameterDescriptor> activeParameters)
    {
        SearchType rootSearchType = getDefaultRootType();
        ComposedType rootRequiredType = null;
        if(rootSearchType instanceof ItemSearchType)
        {
            rootRequiredType = (ComposedType)this.modelService.getSource(((ItemSearchType)rootSearchType).getComposedType());
        }
        else if(rootSearchType != null)
        {
            ObjectType objectType = getTypeService().getObjectType(rootSearchType.getCode());
            if(objectType instanceof ItemType)
            {
                rootRequiredType = (ComposedType)this.modelService.getSource(((ItemType)objectType).getComposedType());
            }
            else if(objectType instanceof ItemTemplate)
            {
                rootRequiredType = (ComposedType)this.modelService.getSource(((ItemTemplate)objectType).getBaseType().getComposedType());
            }
            else
            {
                LOG.warn("Can not extract root search type (Reason: Specified root type is of class '" + rootSearchType
                                .getClass().getCanonicalName() + "').");
            }
        }
        for(GenericSearchParameterDescriptor asd : activeParameters)
        {
            ComposedType req = (ComposedType)this.modelService.getSource(asd.getRequiredComposedType());
            if(rootRequiredType == null)
            {
                rootRequiredType = req;
                continue;
            }
            if(!rootRequiredType.equals(req))
            {
                if(rootRequiredType.isAssignableFrom((Type)req))
                {
                    rootRequiredType = req;
                    continue;
                }
                if(req != null && !req.isAssignableFrom((Type)rootRequiredType))
                {
                    rootRequiredType = null;
                    break;
                }
            }
        }
        return rootRequiredType;
    }


    protected ComposedTypeModel extractRootTypeModel(Collection<GenericSearchParameterDescriptor> activeParameters)
    {
        SearchType rootSearchType = getDefaultRootType();
        ComposedTypeModel rootRequiredType = null;
        if(rootSearchType instanceof ItemSearchType)
        {
            rootRequiredType = ((ItemSearchType)rootSearchType).getComposedType();
        }
        else if(rootSearchType != null)
        {
            ObjectType objectType = getTypeService().getObjectType(rootSearchType.getCode());
            if(objectType instanceof ItemType)
            {
                rootRequiredType = ((ItemType)objectType).getComposedType();
            }
            else if(objectType instanceof ItemTemplate)
            {
                rootRequiredType = ((ItemTemplate)objectType).getBaseType().getComposedType();
            }
            else
            {
                LOG.warn("Can not extract root search type (Reason: Specified root type is of class '" + rootSearchType
                                .getClass().getCanonicalName() + "').");
            }
        }
        for(GenericSearchParameterDescriptor asd : activeParameters)
        {
            ComposedTypeModel req = asd.getRequiredComposedType();
            if(rootRequiredType == null)
            {
                rootRequiredType = req;
                continue;
            }
            if(!rootRequiredType.equals(req))
            {
                if(getSlayerTypeService().isAssignableFrom((TypeModel)rootRequiredType, (TypeModel)req))
                {
                    rootRequiredType = req;
                    continue;
                }
                if(req != null && !getSlayerTypeService().isAssignableFrom((TypeModel)req, (TypeModel)rootRequiredType))
                {
                    rootRequiredType = null;
                    break;
                }
            }
        }
        return rootRequiredType;
    }


    protected ExtendedSearchResult performQuery(Query query, GenericQuery genQuery)
    {
        GenericSearchQuery genericSearchQuery = new GenericSearchQuery(genQuery);
        if(query.getCount() >= 0)
        {
            genericSearchQuery.setStart(query.getStart());
            genericSearchQuery.setCount(query.getCount());
        }
        genericSearchQuery.setNeedTotal(query.isNeedTotalCount());
        genericSearchQuery.setLocale(UISessionUtils.getCurrentSession().getGlobalDataLocale());
        SearchResult<Object> result = getGenericSearchService().search(genericSearchQuery);
        return (ExtendedSearchResult)new DefaultExtendedSearchResult(query, wrapModelResults(result.getResult()),
                        query.isNeedTotalCount() ? result.getTotalCount() : result.getCount());
    }


    @Deprecated
    protected List<ResultObject> wrapResults(List<Object> rawList)
    {
        List<ResultObject> ret = new ArrayList<>(rawList.size());
        for(Object raw : rawList)
        {
            ret.add(new Object(this, matchObjectType((Item)raw), this.modelService.get(raw)));
        }
        return ret;
    }


    protected List<ResultObject> wrapModelResults(List<Object> rawList)
    {
        List<ResultObject> ret = new ArrayList<>(rawList.size());
        for(Object raw : rawList)
        {
            ret.add(new ResultObjectWrapper(this.typeService.wrapItem(raw), raw));
        }
        return ret;
    }


    public ExtendedSearchResult search(Query query)
    {
        ExtendedSearchResult ret;
        SearchType oldRootType = getDefaultRootType();
        Set<SearchType> searchTypes = query.getSelectedTypes();
        if(searchTypes != null && searchTypes.size() == 1)
        {
            setDefaultRootType(searchTypes.iterator().next());
        }
        List<GenericCondition> conditions = new ArrayList<>();
        Set<GenericSearchParameterDescriptor> activeParameters = new LinkedHashSet<>();
        ComposedTypeModel rootTypeModel = extractRootTypeModel(activeParameters);
        Set<ComposedTypeModel> permittedTypeModels = (rootTypeModel == null) ? Collections.EMPTY_SET : getPermittedTypes(rootTypeModel, query.getSelectedTypes(), query.isExcludeSubTypes());
        if(rootTypeModel == null || permittedTypeModels.isEmpty())
        {
            DefaultExtendedSearchResult defaultExtendedSearchResult = new DefaultExtendedSearchResult(query, Collections.EMPTY_LIST, 0);
        }
        else
        {
            GenericQuery genQuery = new GenericQuery(rootTypeModel.getCode());
            genQuery.setInitialTypeAlias("item");
            conditions.addAll(createConditions(query, genQuery));
            GenericSearchOrderBy orderBy = createOrderBy(query);
            if(orderBy != null)
            {
                genQuery.addOrderBy(orderBy);
            }
            Collection<GenericSearchOrderBy> orderByList = genQuery.getOrderByList();
            if(orderByList.isEmpty())
            {
                genQuery.addOrderBy(new GenericSearchOrderBy(new GenericSearchField("item", "pk"), true));
            }
            conditions.add(GenericCondition.createConditionForValueComparison(new GenericSearchField("itemtype"), Operator.IN, permittedTypeModels));
            genQuery.addCondition((GenericCondition)GenericCondition.and(conditions));
            beforeSearch();
            try
            {
                ret = performQuery(query, genQuery);
            }
            finally
            {
                afterSearch();
            }
        }
        setDefaultRootType(oldRootType);
        return ret;
    }


    protected void beforeSearch()
    {
    }


    protected void afterSearch()
    {
    }


    protected String getCollectionsPKList(List<ObjectCollection> collections)
    {
        if(collections == null || collections.isEmpty())
        {
            return "";
        }
        StringWriter writer = new StringWriter();
        boolean first = true;
        for(ObjectCollection coll : collections)
        {
            if(!first)
            {
                writer.append(", ");
            }
            writer.append(coll.getPK().toString());
            if(first)
            {
                first = false;
            }
        }
        return writer.toString();
    }


    public List<GenericCondition> createConditions(Query query, GenericQuery genQuery)
    {
        List<GenericCondition> conditions = new ArrayList<>();
        addCondition(createSimpleSearchCondition(query, genQuery), conditions);
        addCondition(createExcludeItemsCondition(query.getExcludedItems()), conditions);
        addCondition(createObjectTemplateCondition(query), conditions);
        addCondition(createAdvancedSearchCondition(query, genQuery), conditions);
        addCondition(createBlacklistCondition(query, genQuery), conditions);
        return conditions;
    }


    protected void addCondition(GenericCondition condition, List<GenericCondition> conditions)
    {
        if(condition != null)
        {
            conditions.add(condition);
        }
    }


    protected GenericSearchOrderBy createOrderBy(Query query)
    {
        GenericSearchOrderBy orderBy = null;
        for(Map.Entry<PropertyDescriptor, Boolean> e : (Iterable<Map.Entry<PropertyDescriptor, Boolean>>)query.getSortCriteria().entrySet())
        {
            try
            {
                boolean asc = true;
                if(e.getValue() != null)
                {
                    asc = ((Boolean)e.getValue()).booleanValue();
                }
                orderBy = new GenericSearchOrderBy(new GenericSearchField(getTypeService().getAttributeCodeFromPropertyQualifier(((PropertyDescriptor)e.getKey()).getQualifier())), asc);
            }
            catch(Exception exc)
            {
                LOG.warn("Could not create sort condition", exc);
            }
        }
        return orderBy;
    }


    protected void createAndAddCondition(SearchParameterValue searchParameterValue, List<GenericCondition> searchConditions, GenericConditionTranslatorContext context)
    {
        ConditionTranslator translator = getConditionTranslatorRegistry().getTranslator(searchParameterValue.getParameterDescriptor().getEditorType(), searchParameterValue.getOperator());
        if(translator instanceof GenericQueryConditionTranslator)
        {
            GenericCondition condition = ((GenericQueryConditionTranslator)translator).translate(searchParameterValue, (ConditionTranslatorContext)context);
            if(condition != null)
            {
                searchConditions.add(condition);
            }
        }
        else
        {
            LOG.error("Could not find translator for operator '" + searchParameterValue.getOperator().getQualifier() + "' and type '" + searchParameterValue
                            .getParameterDescriptor().getEditorType() + "', ignoring condition");
        }
    }


    protected GenericCondition createAdvancedSearchCondition(Query query, GenericQuery genQuery)
    {
        List<GenericCondition> advancedSearchConditions = new ArrayList<>(2);
        GenericConditionTranslatorContext context = new GenericConditionTranslatorContext(this, genQuery);
        context.setWrapToItemNeeded(true);
        for(SearchParameterValue searchParameterValue : query.getParameterValues())
        {
            createAndAddCondition(searchParameterValue, advancedSearchConditions, context);
        }
        context.setWrapToItemNeeded(false);
        for(List<SearchParameterValue> orValues : (Iterable<List<SearchParameterValue>>)query.getParameterOrValues())
        {
            List<GenericCondition> orConditions = new ArrayList<>();
            for(SearchParameterValue searchParameterValue : orValues)
            {
                createAndAddCondition(searchParameterValue, orConditions, context);
            }
            if(!orConditions.isEmpty())
            {
                advancedSearchConditions.add(GenericCondition.or(orConditions));
            }
        }
        return advancedSearchConditions.isEmpty() ? null : (GenericCondition)GenericCondition.and(advancedSearchConditions);
    }


    protected GenericCondition createExcludeItemsCondition(Collection<TypedObject> excludeItems)
    {
        GenericCondition ret = null;
        if(excludeItems != null)
        {
            List<PK> pkList = new ArrayList<>();
            for(TypedObject item : excludeItems)
            {
                if(item.getObject() instanceof ItemModel)
                {
                    pkList.add(((ItemModel)item.getObject()).getPk());
                }
            }
            if(!pkList.isEmpty())
            {
                ret = GenericCondition.createConditionForValueComparison(new GenericSearchField("PK"), Operator.NOT_IN, pkList);
            }
        }
        return ret;
    }


    protected GenericCondition createSimpleSearchCondition(Query query, GenericQuery genericQuery)
    {
        GenericConditionList genericConditionList;
        GenericCondition ret = null;
        if(query.getSimpleText() != null && query.getSimpleText().trim().length() > 0)
        {
            List<GenericCondition> conditions = new ArrayList<>();
            Set<PropertyDescriptor> supportedPropertyDescriptors = getAllSupportedPropertyDescriptors(query
                            .getSelectedTypes());
            for(PropertyDescriptor pd : supportedPropertyDescriptors)
            {
                GenericSearchParameterDescriptor asd = (GenericSearchParameterDescriptor)pd;
                if(asd.isSimpleSearchProperty())
                {
                    GenericCondition cond = asd.createCondition(genericQuery, query.getSimpleText(), getSimpleOperator());
                    if(cond != null)
                    {
                        conditions.add(cond);
                    }
                }
            }
            if(!conditions.isEmpty())
            {
                genericConditionList = GenericCondition.or(conditions);
            }
        }
        return (GenericCondition)genericConditionList;
    }


    protected Operator getSimpleOperator()
    {
        String parameterName = "default.simpleSearchOperator";
        String parameterValue = UITools.getCockpitParameter("default.simpleSearchOperator", Executions.getCurrent());
        if(!StringUtils.isEmpty(parameterValue))
        {
            return new Operator(parameterValue);
        }
        return SIMPLE_OPERATOR;
    }


    protected GenericCondition createObjectTemplateCondition(Query query)
    {
        GenericCondition condition = null;
        Set<ObjectTemplate> selectedTemplates = getSelectedTemplates(query);
        if(selectedTemplates != null && !selectedTemplates.isEmpty())
        {
            Set<ItemTemplate> matchedTemplates = matchTemplates(selectedTemplates);
            Collection<PK> args = new ArrayList<>(selectedTemplates.size());
            for(ItemTemplate template : matchedTemplates)
            {
                CockpitItemTemplateModel cit = template.getCockpitItemTemplate();
                if(cit != null)
                {
                    args.add(cit.getPk());
                }
            }
            if(!args.isEmpty())
            {
                GenericQuery genericQuery = new GenericQuery(GeneratedCockpitConstants.Relations.ITEM2COCKPITITEMTEMPLATERELATION);
                genericQuery.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField("target"), Operator.IN, args));
                genericQuery.addSelectField(new GenericSelectField("source", PK.class));
                condition = GenericCondition.createSubQueryCondition(new GenericSearchField("item", "PK"), Operator.IN, genericQuery);
            }
        }
        return condition;
    }


    protected GenericCondition createBlacklistCondition(Query query, GenericQuery genQuery)
    {
        List<ObjectCollection> collections = getObjectCollectionService().getSpecialCollections(UISessionUtils.getCurrentSession().getUser(), "blacklist");
        if(collections != null && !collections.isEmpty())
        {
            GenericQuery genericQuery = new GenericQuery("ObjectCollectionItemReference");
            genericQuery.setInitialTypeAlias("CollElem");
            genericQuery.addCondition(GenericCondition.createConditionForFieldComparison(new GenericSearchField("CollElem", "item"), Operator.EQUAL, new GenericSearchField("item", Item.PK)));
            if(collections.size() == 1)
            {
                genericQuery.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField("CollElem", "collection"), Operator.EQUAL, ((ObjectCollection)collections
                                .get(0)).getPK()));
            }
            else
            {
                genericQuery.addCondition(GenericCondition.createConditionForValueComparison(new GenericSearchField("CollElem", "collection"), Operator.IN,
                                getPKSet(collections)));
            }
            return GenericCondition.createSubQueryCondition(null, Operator.NOT_EXISTS, genericQuery);
        }
        return null;
    }


    private Set<PK> getPKSet(List<ObjectCollection> collections)
    {
        if(collections == null || collections.isEmpty())
        {
            return Collections.emptySet();
        }
        return (Set<PK>)collections.stream().map(ObjectCollection::getPK).collect(Collectors.toSet());
    }


    protected Set<ObjectTemplate> getSelectedTemplates(Query query)
    {
        ObjectTemplate selectedTemplate = (ObjectTemplate)query.getContextParameter("objectTemplate");
        return (selectedTemplate == null) ? Collections.EMPTY_SET : Collections.<ObjectTemplate>singleton(selectedTemplate);
    }


    protected Set<ItemTemplate> matchTemplates(Set<ObjectTemplate> templates)
    {
        if(templates == null || templates.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<ItemTemplate> matched = new HashSet<>(templates.size());
        for(ObjectTemplate template : templates)
        {
            if(template instanceof ItemTemplate)
            {
                ItemTemplate itemTemplate = (ItemTemplate)template;
                if(!itemTemplate.isDefaultTemplate())
                {
                    matched.add(itemTemplate);
                }
            }
        }
        return matched;
    }


    protected boolean isEmpty_(GenericSearchParameterDescriptor descriptor, Object value)
    {
        return (value == null || "".equals(value) || (value instanceof Collection && ((Collection)value).isEmpty()));
    }


    public FacetsResult queryFacets(ExtendedSearchResult result, Set<Facet> facets)
    {
        LOG.error("No implementation for query facets.");
        return null;
    }


    public ObjectCollectionService getObjectCollectionService()
    {
        if(this.objectCollectionService == null)
        {
            this.objectCollectionService = (ObjectCollectionService)SpringUtil.getBean("objectCollectionService");
        }
        return this.objectCollectionService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        if(this.flexibleSearchService == null)
        {
            this.flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService");
        }
        return this.flexibleSearchService;
    }


    protected GenericSearchService getGenericSearchService()
    {
        if(this.genericSearchService == null)
        {
            this.genericSearchService = (GenericSearchService)Registry.getApplicationContext().getBean("genericSearchService");
        }
        return this.genericSearchService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        if(this.commonI18NService == null)
        {
            this.commonI18NService = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService");
        }
        return this.commonI18NService;
    }
}
