package de.hybris.platform.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Date;

public interface JaloOnlyItem
{
    void removeJaloOnly() throws ConsistencyCheckException;


    PK providePK();


    Date provideCreationTime();


    Date provideModificationTime();


    ComposedType provideComposedType();


    Object doGetAttribute(SessionContext paramSessionContext, String paramString) throws JaloInvalidParameterException, JaloSecurityException;


    void doSetAttribute(SessionContext paramSessionContext, String paramString, Object paramObject) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException;
}
