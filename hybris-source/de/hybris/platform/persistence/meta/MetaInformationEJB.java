package de.hybris.platform.persistence.meta;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import java.util.Date;

public abstract class MetaInformationEJB extends ExtensibleItemEJB implements MetaInformationRemote, MetaInformationHome
{
    public static final PK DEFAULT_PRIMARY_KEY = PK.createFixedUUIDPK(55, 1L);


    public abstract void setSystemPKInternal(String paramString);


    public abstract String getSystemPKInternal();


    public abstract void setSystemNameInternal(String paramString);


    public abstract String getSystemNameInternal();


    public abstract void setInitializedFlagInternal(boolean paramBoolean);


    public abstract boolean getInitializedFlagInternal();


    public abstract void setLicenceIDInternal(String paramString);


    public abstract String getLicenceIDInternal();


    public abstract void setLicenceNameInternal(String paramString);


    public abstract String getLicenceNameInternal();


    public abstract void setLicenceEditionInternal(String paramString);


    public abstract String getLicenceEditionInternal();


    public abstract void setLicenceAdminFactorInternal(int paramInt);


    public abstract int getLicenceAdminFactorInternal();


    public abstract void setLicenceExpirationDateInternal(Date paramDate);


    public abstract Date getLicenceExpirationDateInternal();


    public abstract void setLicenceSignatureInternal(String paramString);


    public abstract String getLicenceSignatureInternal();


    public String getLockPkString()
    {
        throw new JaloSystemException(null, "MetaInformation does not support locking", 0);
    }


    public void setLockPkString(String pkString)
    {
        throw new JaloSystemException(null, "MetaInformation does not support locking", 0);
    }


    public int typeCode()
    {
        return 55;
    }


    public void setSystemName(String systemName)
    {
        setSystemNameInternal(systemName);
    }


    public String getSystemName()
    {
        return getSystemNameInternal();
    }


    public String getSystemPk()
    {
        return getSystemPKInternal();
    }


    public void setSystemPk(String pk)
    {
        setSystemPKInternal(pk);
    }


    public void clearEntityCaches()
    {
        super.clearEntityCaches();
    }


    public void setInitializedFlag(boolean isOk)
    {
        setInitializedFlagInternal(isOk);
    }


    public boolean getInitializedFlag()
    {
        return getInitializedFlagInternal();
    }


    public Licence getLicence()
    {
        return Licence.getDefaultLicence();
    }


    public PK ejbCreate(PK pk, String systemName)
    {
        setSystemNameInternal(systemName);
        setSystemPk(PK.createUUIDPK(55).getLongValueAsString());
        doCreateInternal(pk, null, null, null);
        setTypePkString(PK.createFixedUUIDPK(55, 0L));
        return null;
    }


    public void ejbPostCreate(PK pk, String systemName)
    {
        doPostCreateInternal(null, null, null);
    }


    public ComposedTypeRemote getComposedType()
    {
        return null;
    }


    public void setComposedType(ComposedTypeRemote type) throws EJBInvalidParameterException
    {
    }


    public TypeInfoMap getTypeInfoMap()
    {
        return TypeInfoMap.EMPTY_INFOMAP;
    }
}
