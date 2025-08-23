package de.hybris.platform.persistence.link;

import de.hybris.platform.core.ItemWrapperCreator;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.persistence.ExtensibleItemEJBImpl;
import de.hybris.platform.persistence.ExtensibleItemRemote;
import de.hybris.platform.persistence.ItemRemote;

public class LinkEJBImpl extends ExtensibleItemEJBImpl implements Link.LinkImpl
{
    static
    {
        WrapperFactory.registerItemWrapperCreator(7, (ItemWrapperCreator)new Object());
    }

    public LinkRemote getRemote()
    {
        return (LinkRemote)super.getRemote();
    }


    protected LinkEJBImpl(Tenant tenant, LinkRemote remoteObject)
    {
        super(tenant, (ExtensibleItemRemote)remoteObject);
    }


    public Item getSource()
    {
        return (Item)wrap(getRemote().getSource());
    }


    public void setSource(Item source)
    {
        getRemote().setSourcePK((source == null) ? null : source.getPK());
    }


    public Item getTarget()
    {
        return (Item)wrap(getRemote().getTarget());
    }


    public void setTarget(Item target)
    {
        getRemote().setTargetPK((target == null) ? null : target.getPK());
    }


    public String getQualifier()
    {
        return getRemote().getQualifier();
    }


    public void setQualifier(String quali)
    {
        getRemote().setQualifier(quali);
    }


    protected void removeInternal(SessionContext ctx) throws ConsistencyCheckException
    {
        LinkManagerEJB relr = (LinkManagerEJB)LinkManager.getInstance().getRemote();
        relr.removeItem((ItemRemote)getRemote());
    }


    public int getSequenceNumber()
    {
        return getRemote().getSequenceNumber();
    }


    public void setSequenceNumber(int number)
    {
        getRemote().setSequenceNumber(number);
    }


    public void setReverseSequenceNumber(int number)
    {
        getRemote().setReverseSequenceNumber(number);
    }


    public int getReverseSequenceNumber()
    {
        return getRemote().getReverseSequenceNumber();
    }
}
