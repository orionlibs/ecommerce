package de.hybris.platform.persistence.enumeration;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.persistence.c2l.LocalizableItemEJBImpl;
import de.hybris.platform.persistence.c2l.LocalizableItemRemote;

public class EnumerationValueEJBImpl extends LocalizableItemEJBImpl implements EnumerationValue.EnumerationValueImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(91, (ItemWrapperCreator)new Object());
    }

    protected EnumerationValueEJBImpl(Tenant tenant, EnumerationValueRemote remoteObject)
    {
        super(tenant, (LocalizableItemRemote)remoteObject);
    }


    public int getSequenceNumber()
    {
        return ((EnumerationValueRemote)getRemote()).getSequenceNumber();
    }


    public void setSequenceNumber(int i)
    {
        ((EnumerationValueRemote)getRemote()).setSequenceNumber(i);
    }


    public String getCode()
    {
        return ((EnumerationValueRemote)getRemote()).getCode();
    }


    public void setCode(String newCode) throws ConsistencyCheckException
    {
        ((EnumerationValueRemote)getRemote()).setCode(newCode);
    }


    protected void removeInternal(SessionContext ctx) throws ConsistencyCheckException
    {
        EnumerationManagerEJB em = (EnumerationManagerEJB)EnumerationManager.getInstance().getRemote();
        em.removeItem(getRemote());
    }


    protected boolean isHandlingOwnerAsReferenceSupported()
    {
        return true;
    }
}
