package de.hybris.platform.droolsruleengineservices.agendafilter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.drools.core.common.InternalFactHandle;
import org.junit.Assert;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.Match;

public class CustomAgendaFilter implements AgendaFilter
{
    public boolean accept(Match match)
    {
        Rule rule = match.getRule();
        if(!"customAgendaFilterTest".equals(rule.getName()))
        {
            Assert.fail("expected rule to fire should be customAgendaFilterTest!");
        }
        Objects.requireNonNull(Map.class);
        Optional<Map> mapFactOptional = match.getFactHandles().stream().filter(fact -> fact instanceof InternalFactHandle).map(fact -> ((InternalFactHandle)fact).getObject()).filter(fact -> fact instanceof Map).map(Map.class::cast).findFirst();
        if(mapFactOptional.isPresent())
        {
            Map<String, String> mapFact = mapFactOptional.get();
            mapFact.put("addedByAgendaFilter", "some other value");
        }
        else
        {
            Assert.fail("expected a Map<String,String>");
        }
        return true;
    }
}
