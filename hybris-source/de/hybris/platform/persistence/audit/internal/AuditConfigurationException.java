package de.hybris.platform.persistence.audit.internal;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Collection;
import java.util.Collections;

public class AuditConfigurationException extends SystemException
{
    private final Collection<String> violatingProperties;


    public AuditConfigurationException(String message, Collection<String> violatingProperties)
    {
        super(message);
        this.violatingProperties = Collections.unmodifiableCollection(violatingProperties);
    }


    public Collection<String> getViolatingProperties()
    {
        return this.violatingProperties;
    }
}
