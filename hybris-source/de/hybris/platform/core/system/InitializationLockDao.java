package de.hybris.platform.core.system;

import de.hybris.platform.core.Tenant;
import java.io.Serializable;

public interface InitializationLockDao extends Serializable
{
    InitializationLockInfo readLockInfo();


    boolean releaseRow(Tenant paramTenant);


    boolean lockRow(Tenant paramTenant, String paramString);


    boolean insertRow();


    void createTable();


    long getUniqueInstanceIdentifier();
}
