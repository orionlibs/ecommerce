package de.hybris.platform.assertions;

import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.assertj.core.api.Assertions;

public class BaseCommerceAssertions extends Assertions
{
    public static CronJobResultAssert assertThat(PerformResult performResult)
    {
        return new CronJobResultAssert(performResult);
    }
}
