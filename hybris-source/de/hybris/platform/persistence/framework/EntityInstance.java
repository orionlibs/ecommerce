package de.hybris.platform.persistence.framework;

import de.hybris.platform.core.PK;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;

public interface EntityInstance
{
    void setEntityContext(EntityInstanceContext paramEntityInstanceContext);


    EntityInstanceContext getEntityContext();


    void ejbLoad();


    void ejbRemove();


    void ejbStore();


    void setNeedsStoring(boolean paramBoolean);


    boolean needsStoring();


    PK ejbFindByPrimaryKey(PK paramPK) throws YObjectNotFoundException;
}
