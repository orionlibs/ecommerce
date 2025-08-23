package de.hybris.platform.ruleengineservices.validation;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Required;

public class ConditionToValuesMapper<T> implements Function<RuleConditionData, List<T>>
{
    private String type;
    private String definitionId;
    private Set<String> parameterNames;
    private RuleParameterValueMapper<T> parameterValueMapper;


    public List<T> apply(RuleConditionData ruleConditionData)
    {
        Preconditions.checkArgument((ruleConditionData != null), "RuleConditionData is required to perform this operation, null given");
        Stream<RuleConditionData> matchingConditions = flatten(ruleConditionData).filter(c -> getDefinitionId().equals(c.getDefinitionId()));
        Stream<RuleParameterData> matchingParameters = matchingConditions.map(c -> c.getParameters().entrySet()).flatMap(Collection::stream).filter(e -> (getParameterNames().contains(e.getKey()) && ((RuleParameterData)e.getValue()).getType().contains(getType()))).map(Map.Entry::getValue);
        Objects.requireNonNull(String.class);
        Objects.requireNonNull(String.class);
        Stream<String> values = matchingParameters.map(this::normalizeValue).flatMap(Collection::stream).filter(String.class::isInstance).map(String.class::cast);
        return (List<T>)values.map(this::mapValue).flatMap(Optional::stream).collect(Collectors.toList());
    }


    protected Stream<RuleConditionData> flatten(RuleConditionData ruleConditionData)
    {
        return Stream.concat(Stream.of(ruleConditionData), (ruleConditionData.getChildren() == null) ? Stream.<RuleConditionData>empty() :
                        ruleConditionData.getChildren().stream().flatMap(this::flatten));
    }


    protected <V> Collection<V> normalizeValue(RuleParameterData parameterData)
    {
        Object value = parameterData.getValue();
        return (value instanceof Collection) ? (Collection<V>)value : List.<V>of((V)value);
    }


    protected Optional<T> mapValue(String value)
    {
        try
        {
            return Optional.ofNullable((T)getParameterValueMapper().fromString(value));
        }
        catch(RuleParameterValueMapperException e)
        {
            return Optional.empty();
        }
    }


    protected String getType()
    {
        return this.type;
    }


    @Required
    public void setType(String type)
    {
        this.type = type;
    }


    protected String getDefinitionId()
    {
        return this.definitionId;
    }


    @Required
    public void setDefinitionId(String definitionId)
    {
        this.definitionId = definitionId;
    }


    protected Set<String> getParameterNames()
    {
        return this.parameterNames;
    }


    @Required
    public void setParameterNames(Set<String> parameterNames)
    {
        this.parameterNames = parameterNames;
    }


    protected RuleParameterValueMapper<T> getParameterValueMapper()
    {
        return this.parameterValueMapper;
    }


    @Required
    public void setParameterValueMapper(RuleParameterValueMapper<T> parameterValueMapper)
    {
        this.parameterValueMapper = parameterValueMapper;
    }
}
