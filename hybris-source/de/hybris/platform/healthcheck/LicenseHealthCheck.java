package de.hybris.platform.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.licence.internal.ValidationResult;

public class LicenseHealthCheck extends HybrisHealthCheck
{
    protected HealthCheck.Result check()
    {
        ValidationResult result = Licence.getDefaultLicence().validate();
        if(result.isValid())
        {
            return HealthCheck.Result.healthy();
        }
        return HealthCheck.Result.unhealthy(result.getMessage());
    }
}
