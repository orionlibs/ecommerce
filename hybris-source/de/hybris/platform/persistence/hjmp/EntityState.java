package de.hybris.platform.persistence.hjmp;

import de.hybris.platform.core.PK;

public interface EntityState
{
    PK getPK();


    String getFullBeanName();
}
