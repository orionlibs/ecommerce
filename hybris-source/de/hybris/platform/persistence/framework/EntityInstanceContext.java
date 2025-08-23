package de.hybris.platform.persistence.framework;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;

public interface EntityInstanceContext
{
    PK getPK();


    void setPK(PK paramPK);


    PersistencePool getPersistencePool();


    ItemDeployment getItemDeployment();
}
