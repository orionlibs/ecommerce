package de.hybris.platform.ruleengineservices.validation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsRegistry;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class SourceRuleConditionOverlapValidator implements Predicate<SourceRuleModel>
{
    private static final int COMBINATION_SIZE = 2;
    private RuleConditionsService ruleConditionsService;
    private RuleConditionsRegistry ruleConditionsRegistry;
    private Integer containerCountThreshold;
    private Integer overlapCountThreshold;
    private Function<RuleConditionData, List<CategoryModel>> conditionToCategoriesMapper;
    private Function<RuleConditionData, List<ProductModel>> conditionToProductsMapper;
    private Function<ProductModel, Set<String>> productToCodesMapper;
    private Function<CategoryModel, Set<String>> categoryToCodesMapper;
    private String containerDefinitionId;


    public boolean test(SourceRuleModel sourceRule)
    {
        Preconditions.checkArgument((sourceRule != null), "Source rule is required to perform this operation, null given");
        Map<String, RuleConditionDefinitionData> conditionDefinitions = getRuleConditionsRegistry().getConditionDefinitionsForRuleTypeAsMap(sourceRule.getClass());
        List<RuleConditionData> ruleConditions = getRuleConditionsService().convertConditionsFromString(sourceRule.getConditions(), conditionDefinitions);
        List<RuleConditionData> containers = getContainers(ruleConditions);
        if(containers.size() < getContainerCountThreshold().intValue())
        {
            return true;
        }
        Map<RuleConditionData, Pair<List<CategoryModel>, List<ProductModel>>> mappedItems = new HashMap<>();
        containers.forEach(c -> mappedItems.put(c, ImmutablePair.of(getConditionToCategoriesMapper().apply(c), getConditionToProductsMapper().apply(c))));
        Map<RuleConditionData, Set<String>> mappedItemCodes = (Map<RuleConditionData, Set<String>>)mappedItems.entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey,
                                        e -> SetUtils.union((Set)((List)((Pair)e.getValue()).getLeft()).stream().map(()).flatMap(Collection::stream).collect(Collectors.toSet()), (Set)((List)((Pair)e.getValue()).getRight()).stream().map(()).flatMap(Collection::stream).collect(Collectors.toSet()))
                                                        .toSet()));
        return validateOverlap(mappedItemCodes);
    }


    protected boolean validateOverlap(Map<RuleConditionData, Set<String>> mappedItemCodes)
    {
        int overlapCounter = 0;
        for(Set<RuleConditionData> combination : (Iterable<Set<RuleConditionData>>)Sets.combinations(mappedItemCodes.keySet(), 2))
        {
            Objects.requireNonNull(mappedItemCodes);
            List<Set<String>> pairSet = (List<Set<String>>)combination.stream().map(mappedItemCodes::get).collect(Collectors.toList());
            Set<String> commonElements = pairSet.stream().reduce(Sets::intersection).orElse(Collections.emptySet());
            if(!CollectionUtils.isEmpty(commonElements) && ++overlapCounter == getOverlapCountThreshold().intValue())
            {
                return false;
            }
        }
        return true;
    }


    protected List<RuleConditionData> getContainers(List<RuleConditionData> conditions)
    {
        return (List<RuleConditionData>)conditions.stream().filter(c -> getContainerDefinitionId().equals(c.getDefinitionId())).collect(Collectors.toList());
    }


    protected RuleConditionsService getRuleConditionsService()
    {
        return this.ruleConditionsService;
    }


    public void setRuleConditionsService(RuleConditionsService ruleConditionsService)
    {
        this.ruleConditionsService = ruleConditionsService;
    }


    protected RuleConditionsRegistry getRuleConditionsRegistry()
    {
        return this.ruleConditionsRegistry;
    }


    public void setRuleConditionsRegistry(RuleConditionsRegistry ruleConditionsRegistry)
    {
        this.ruleConditionsRegistry = ruleConditionsRegistry;
    }


    protected Integer getContainerCountThreshold()
    {
        return this.containerCountThreshold;
    }


    public void setContainerCountThreshold(Integer containerCountThreshold)
    {
        this.containerCountThreshold = containerCountThreshold;
    }


    protected Integer getOverlapCountThreshold()
    {
        return this.overlapCountThreshold;
    }


    public void setOverlapCountThreshold(Integer overlapCountThreshold)
    {
        this.overlapCountThreshold = overlapCountThreshold;
    }


    protected Function<RuleConditionData, List<CategoryModel>> getConditionToCategoriesMapper()
    {
        return this.conditionToCategoriesMapper;
    }


    public void setConditionToCategoriesMapper(Function<RuleConditionData, List<CategoryModel>> conditionToCategoriesMapper)
    {
        this.conditionToCategoriesMapper = conditionToCategoriesMapper;
    }


    protected Function<RuleConditionData, List<ProductModel>> getConditionToProductsMapper()
    {
        return this.conditionToProductsMapper;
    }


    public void setConditionToProductsMapper(Function<RuleConditionData, List<ProductModel>> conditionToProductsMapper)
    {
        this.conditionToProductsMapper = conditionToProductsMapper;
    }


    public Function<CategoryModel, Set<String>> getCategoryToCodesMapper()
    {
        return this.categoryToCodesMapper;
    }


    public void setCategoryToCodesMapper(Function<CategoryModel, Set<String>> categoryToCodesMapper)
    {
        this.categoryToCodesMapper = categoryToCodesMapper;
    }


    protected Function<ProductModel, Set<String>> getProductToCodesMapper()
    {
        return this.productToCodesMapper;
    }


    public void setProductToCodesMapper(Function<ProductModel, Set<String>> productToCodesMapper)
    {
        this.productToCodesMapper = productToCodesMapper;
    }


    protected String getContainerDefinitionId()
    {
        return this.containerDefinitionId;
    }


    public void setContainerDefinitionId(String containerDefinitionId)
    {
        this.containerDefinitionId = containerDefinitionId;
    }
}
