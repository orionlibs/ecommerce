package de.hybris.platform.persistence.link;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.ManagerEJB;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.jeeapi.YCreateException;
import org.apache.log4j.Logger;

public class LinkManagerEJB extends ManagerEJB
{
    private static final Logger log = Logger.getLogger(LinkManagerEJB.class.getName());


    @Deprecated(since = "ages", forRemoval = true)
    public LinkRemote createLink(String qual, PK sourcePK, PK targetPK, int sequenceNumber) throws EJBInvalidParameterException
    {
        return createLink(qual, sourcePK, targetPK, sequenceNumber, 0);
    }


    public LinkRemote createLink(String qual, PK sourcePK, PK targetPK, int sequenceNumber, int reverseSequenceNumber) throws EJBInvalidParameterException
    {
        try
        {
            return getLinkHome(qual).create(qual, sourcePK, targetPK, sequenceNumber, reverseSequenceNumber);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void createLinkNoWrap(String qual, PK sourcePK, PK targetPK, int sequenceNumber, int reverseSequenceNumber) throws EJBInvalidParameterException
    {
        try
        {
            getLinkHome(qual).create(qual, sourcePK, targetPK, sequenceNumber, reverseSequenceNumber);
        }
        catch(YCreateException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public LinkRemote createLink(String qual, ItemRemote source, ItemRemote target, int sequenceNumber, int reverseSequenceNumber) throws EJBInvalidParameterException
    {
        return createLink(qual, EJBTools.getPK(source), EJBTools.getPK(target), sequenceNumber, reverseSequenceNumber);
    }


    private LinkHome getLinkHome(String qualifier)
    {
        return (LinkHome)getPersistencePool().getHomeProxy(getLinkDeployment(qualifier));
    }


    private String getLinkDeployment(String qualifier)
    {
        PersistenceManager pm = Registry.getCurrentTenant().getPersistencePool().getPersistenceManager();
        int tc = pm.isRootRelationType(qualifier) ? pm.getPersistenceInfo(qualifier).getItemTypeCode() : 7;
        return pm.getItemDeployment(tc).getName();
    }


    public void notifyItemRemove(ItemRemote item)
    {
    }


    public void removeItem(int stagingMethod, ItemRemote item) throws ConsistencyCheckException
    {
        if(!(item instanceof LinkRemote))
        {
            throw new JaloSystemException(null, "This manager is not responsible for removing this type of item: " + item, 0);
        }
        removeItem(item);
    }
}
