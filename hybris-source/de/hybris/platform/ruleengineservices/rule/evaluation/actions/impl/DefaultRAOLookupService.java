package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOLookupService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.spi.KnowledgeHelper;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRAOLookupService implements RAOLookupService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRAOLookupService.class);


    public <T> Optional<T> lookupRAOByType(Class<T> raoType, RuleActionContext context, Predicate<T>... raoFilters)
    {
        List<T> raoFacts = lookupRAOObjectsByType(raoType, context, raoFilters);
        if(raoFacts.size() == 1)
        {
            return Optional.ofNullable(raoFacts.iterator().next());
        }
        if(CollectionUtils.isEmpty(raoFacts))
        {
            LOG.error("No RAO facts of type {} are found in the Knowledgebase working memory", raoType.getName());
        }
        else
        {
            LOG.error("Multiple instances of RAO facts of type {} are found in the Knowledgebase working memory", raoType.getName());
        }
        return Optional.empty();
    }


    public <T> List<T> lookupRAOObjectsByType(Class<T> raoType, RuleActionContext context, Predicate<T>... raoFilters)
    {
        KnowledgeHelper helper = checkAndGetRuleContext(context);
        Predicate<T> composedRaoFilter = ArrayUtils.isNotEmpty((Object[])raoFilters) ? Stream.<Predicate<T>>of(raoFilters).reduce(o -> true, Predicate::and) : (o -> true);
        Collection<FactHandle> factHandles = helper.getWorkingMemory().getFactHandles(o ->
                        (raoType.isInstance(o) && composedRaoFilter.test(o)));
        if(CollectionUtils.isNotEmpty(factHandles))
        {
            return (List<T>)factHandles.stream().map(h -> ((InternalFactHandle)h).getObject()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected KnowledgeHelper checkAndGetRuleContext(RuleActionContext context)
    {
        Object delegate = context.getDelegate();
        Preconditions.checkState(delegate instanceof KnowledgeHelper, "context must be of type org.kie.api.runtime.rule.RuleContext.");
        return (KnowledgeHelper)delegate;
    }
}
