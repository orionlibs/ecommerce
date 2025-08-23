package de.hybris.platform.ruleengine.concurrency.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.servicelayer.ServicelayerTest;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@IntegrationTest
public class DefaultRuleEngineSpliteratorStrategyConfigurationIT extends ServicelayerTest
{
    @Resource
    private RuleEngineSpliteratorStrategy ruleEngineSpliteratorStrategy;


    @Test
    public void shouldHaveConfiguredDefaultNumberOfWorkerThreads() throws Exception
    {
        int defaultNumberOfThreads = this.ruleEngineSpliteratorStrategy.getNumberOfThreads();
        Assertions.assertThat(defaultNumberOfThreads).isEqualTo(numberOfProcessors() + 1);
    }


    protected int numberOfProcessors()
    {
        return Runtime.getRuntime().availableProcessors();
    }
}
