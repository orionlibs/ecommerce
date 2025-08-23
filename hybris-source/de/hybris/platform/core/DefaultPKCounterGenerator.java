package de.hybris.platform.core;

import de.hybris.platform.persistence.numberseries.SerialNumberGenerator;
import de.hybris.platform.util.typesystem.PlatformStringUtils;

public class DefaultPKCounterGenerator implements PK.PKCounterGenerator
{
    public long fetchNextCounter(int typecode)
    {
        long counter;
        SerialNumberGenerator generator = Registry.getCurrentTenant().getSerialNumberGenerator();
        try
        {
            counter = generator.getUniqueNumber("pk_" + typecode).getCurrentNumber();
        }
        catch(IllegalArgumentException e)
        {
            try
            {
                generator.createSeries("pk_" + typecode, 1, 1L);
            }
            catch(IllegalArgumentException illegalArgumentException)
            {
            }
            counter = generator.getUniqueNumber("pk_" + typecode).getCurrentNumber();
        }
        return counter;
    }


    public String getTypeCodeAsString(int typeCode)
    {
        return PlatformStringUtils.valueOf(typeCode);
    }


    public int getClusterID()
    {
        return MasterTenant.getInstance().getClusterID();
    }


    private Boolean legacyPKDetect = null;


    public boolean check31LegacyDetection()
    {
        if(this.legacyPKDetect == null)
        {
            MasterTenant masterTenant;
            Tenant currentTenant = Registry.getCurrentTenantNoFallback();
            if(currentTenant == null)
            {
                masterTenant = Registry.getMasterTenant();
            }
            this.legacyPKDetect = Boolean.valueOf(masterTenant.getConfig().getBoolean("legacy.pk31.detection", false));
        }
        return this.legacyPKDetect.booleanValue();
    }
}
