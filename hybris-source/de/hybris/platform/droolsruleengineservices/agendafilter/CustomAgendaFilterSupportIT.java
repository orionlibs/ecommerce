package de.hybris.platform.droolsruleengineservices.agendafilter;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.droolsruleengineservices.impl.AbstractRuleEngineServicesTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class CustomAgendaFilterSupportIT extends AbstractRuleEngineServicesTest
{
    @Resource
    List<AgendaFilterCreationStrategy> defaultAgendaFilterStrategies;
    @Resource
    AgendaFilterCreationStrategy testCustomAgendaFilterCreationStrategy;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/droolsruleengineservices/test/ruleenginesetup.impex", "utf-8");
        this.defaultAgendaFilterStrategies.add(this.testCustomAgendaFilterCreationStrategy);
    }


    @After
    public void tearDown()
    {
        boolean removed = this.defaultAgendaFilterStrategies.remove(this.testCustomAgendaFilterCreationStrategy);
        if(!removed)
        {
            Assert.fail("tearDown: removing testCustomAgendaFilterCreationStrategy failed! This might impact other tests as well!");
        }
    }


    @Test
    public void testCustomAgendaFilter() throws IOException
    {
        DroolsRuleModel rule1 = getRuleForFile("customAgendaFilterTest.drl", "/droolsruleengineservices/test/rules/evaluation/", "de.hybris.platform.promotionengineservices.test", null);
        initializeRuleEngine(new DroolsRuleModel[] {rule1});
        Map<String, String> mapFact = new HashMap<>();
        HashSet<Object> facts = new HashSet();
        facts.add(mapFact);
        RuleEvaluationContext context = prepareContext(facts);
        getCommerceRuleEngineService().evaluate(context);
        Assert.assertTrue("rule should have added map entry with key 'addedByRule'", mapFact.containsKey("addedByRule"));
        Assert.assertTrue("custom agendaFilter should have added map entry with key 'agendaFilter'", mapFact
                        .containsKey("addedByAgendaFilter"));
    }
}
