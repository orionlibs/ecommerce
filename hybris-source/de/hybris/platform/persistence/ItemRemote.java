package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.framework.EntityProxy;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.jeeapi.YEJBException;
import de.hybris.platform.util.jeeapi.YRemoveException;
import java.util.Date;

public interface ItemRemote extends EntityProxy
{
    PK getPkString();


    Date getCreationTime();


    void setCreationTime(Date paramDate);


    Date getModifiedTime();


    void setModifiedTime(Date paramDate);


    boolean wasModifiedSince(Date paramDate);


    boolean hasJNDIName(String paramString);


    ComposedTypeRemote getComposedType();


    void setComposedType(ComposedTypeRemote paramComposedTypeRemote) throws EJBInvalidParameterException;


    ItemRemote getOwner();


    void setOwner(ItemRemote paramItemRemote);


    void setOwnerRef(ItemPropertyValue paramItemPropertyValue);


    long getHJMPTS();


    PK getTypeKey();


    void remove() throws YEJBException, YRemoveException;
}
