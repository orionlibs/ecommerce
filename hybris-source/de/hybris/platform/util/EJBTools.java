package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import de.hybris.platform.util.jeeapi.YRemoveException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class EJBTools
{
    private static final Logger log = Logger.getLogger(EJBTools.class);
    public static final String ITEMCOLLECTION_TYPE_MARKER = "#".intern();


    public static void removeBeanCollection(Collection<? extends ItemRemote> c)
    {
        try
        {
            for(ItemRemote rem : c)
            {
                rem.remove();
            }
        }
        catch(YRemoveException e)
        {
            throw new JaloSystemException(e, "[EJBTools.removeBeanCollection] error removing EJB.", 0);
        }
    }


    public static void removeAllRowsFromTable(String tablename)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            conn = Registry.getCurrentTenant().getDataSource().getConnection();
            pstmt = conn.prepareStatement("DELETE FROM " + tablename);
            pstmt.executeUpdate();
        }
        catch(SQLException e)
        {
            log.error("[EJBTools.removeAllRowsFromTable] error:" + e.getMessage());
        }
        finally
        {
            Utilities.tryToCloseJDBC(conn, pstmt, null, true);
        }
    }


    public static final ItemRemote instantiatePK(PK pk)
    {
        ItemRemote remote;
        if(pk == null || PK.NULL_PK.equals(pk))
        {
            return null;
        }
        try
        {
            remote = SystemEJB.getInstance().findRemoteObjectByPK(pk);
        }
        catch(EJBItemNotFoundException e)
        {
            if(log.isInfoEnabled())
            {
                log.info("pk " + pk + " does no longer exist.");
            }
            if(log.isDebugEnabled())
            {
                log.debug(Utilities.getStackTraceAsString(new Exception()));
            }
            return null;
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloSystemException(e);
        }
        return remote;
    }


    public static ItemRemote instantiateItemPropertyValue(ItemPropertyValue ipv)
    {
        if(ipv == null)
        {
            return null;
        }
        return instantiatePK(ipv.getPK());
    }


    public static Collection instantiateItemPropertyValueCollection(Collection ipvs)
    {
        List<ItemRemote> ret = new ArrayList();
        for(Iterator<ItemPropertyValue> it = ipvs.iterator(); it.hasNext(); )
        {
            ItemRemote obj = instantiateItemPropertyValue(it.next());
            if(obj != null)
            {
                ret.add(obj);
            }
        }
        return ret;
    }


    public static ItemPropertyValue createItemPropertyValue(ItemRemote item)
    {
        if(item == null)
        {
            return null;
        }
        return new ItemPropertyValue(item.getPK());
    }


    public static List instantiatePKCollection(Collection<PK> pks)
    {
        List<ItemRemote> ret = new ArrayList();
        for(Iterator<PK> it = pks.iterator(); it.hasNext(); )
        {
            ItemRemote obj = instantiatePK(it.next());
            if(obj != null)
            {
                ret.add(obj);
            }
        }
        return ret;
    }


    public static List instantiateCommaSeparatedPKString(String pks)
    {
        return instantiateCommaSeparatedPKString(pks, false);
    }


    public static List instantiateCommaSeparatedPKString(String pks, boolean ignoreNull)
    {
        if(pks == null)
        {
            return ignoreNull ? new ArrayList() : null;
        }
        if(pks.length() == 0)
        {
            return new ArrayList();
        }
        List<Object> ret = new ArrayList();
        if(pks.charAt(0) != ',' || !pks.endsWith(","))
        {
            throw new IllegalArgumentException("invalid PK collection string:" + pks);
        }
        int pos = 1;
        int i;
        for(i = pks.indexOf(',', pos); i > 0; i = pks.indexOf(',', pos))
        {
            Object obj = instantiatePK(PK.parse(pks.substring(pos, i)));
            if(obj != null)
            {
                ret.add(obj);
            }
            pos = i + 1;
        }
        return ret;
    }


    public static PK getPK(ItemRemote remote)
    {
        if(remote == null)
        {
            return null;
        }
        return remote.getPK();
    }


    public static String getPKInLIKEFragment(ItemRemote remote)
    {
        if(remote == null)
        {
            throw new IllegalStateException("remote can't be null");
        }
        return "%," + getPK(remote).getLongValueAsString() + ",%";
    }


    public static List<PK> toPKList(Collection items)
    {
        List<PK> lst = new ArrayList<>(items.size());
        for(Object o : items)
        {
            if(o == null)
            {
                lst.add(null);
                continue;
            }
            if(o instanceof ItemRemote)
            {
                lst.add(((ItemRemote)o).getPK());
                continue;
            }
            if(o instanceof ItemPropertyValue)
            {
                lst.add(((ItemPropertyValue)o).getPK());
                continue;
            }
            if(o instanceof Item)
            {
                lst.add(((Item)o).getPK());
                continue;
            }
            throw new RuntimeException("unknown class in getPKCollection: " + o.getClass().getName());
        }
        return lst;
    }


    public static String getPKCollectionString(Collection items)
    {
        if(items == null)
        {
            return null;
        }
        if(items.isEmpty())
        {
            return ",";
        }
        if(items instanceof ItemPropertyValueCollection)
        {
            return getPKCollectionStringFromItemPropertyValues(items);
        }
        StringBuilder sb = new StringBuilder(",");
        for(Iterator it = items.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            if(o != null)
            {
                if(o instanceof ItemPropertyValue)
                {
                    sb.append(((ItemPropertyValue)o).getPK().getLongValueAsString());
                    sb.append(",");
                    continue;
                }
                if(o instanceof ItemRemote)
                {
                    sb.append(getPK((ItemRemote)o).getLongValueAsString());
                    sb.append(",");
                    continue;
                }
                if(o instanceof Item)
                {
                    sb.append(((Item)o).getPK().getLongValueAsString());
                    sb.append(",");
                    continue;
                }
                if(o instanceof PK)
                {
                    sb.append(((PK)o).getLongValueAsString());
                    sb.append(",");
                    continue;
                }
                throw new RuntimeException("illegal object in getPKCollectionString(): " + o.getClass().getName());
            }
        }
        return sb.toString();
    }


    private static String getPKCollectionStringFromItemPropertyValues(Collection itemPropertyValues)
    {
        if(itemPropertyValues == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder(",");
        for(Iterator<ItemPropertyValue> it = itemPropertyValues.iterator(); it.hasNext(); )
        {
            ItemPropertyValue ipv = it.next();
            if(ipv != null)
            {
                sb.append(ipv.getPK().getLongValueAsString());
                sb.append(',');
            }
        }
        return sb.toString();
    }


    public static final String EMPTY_PK_COLLECTION_STRING = getPKCollectionString(Collections.EMPTY_LIST);


    public static String addPKToPKCollectionString(String pkCollectionString, PK pk)
    {
        if(pkCollectionString == null || pkCollectionString.length() == 0)
        {
            return "," + pk.getLongValueAsString() + ",";
        }
        return pkCollectionString + pkCollectionString + ",";
    }


    public static List<String> getPKCollection(String pks)
    {
        List<String> ret = new ArrayList<>();
        if(pks == null)
        {
            return ret;
        }
        int pos = 1;
        for(int i = pks.indexOf(',', pos); i > 0; i = pks.indexOf(',', pos))
        {
            String pk = pks.substring(pos, i).trim().intern();
            ret.add((pk.length() > 0) ? pk : null);
            pos = i + 1;
        }
        return ret;
    }


    public static ItemPropertyValueCollection getCollectionFromString(String value)
    {
        ItemPropertyValueCollection coll = new ItemPropertyValueCollection();
        List<String> stringList = getPKCollection(value);
        int offset = 0;
        String firstOne = !stringList.isEmpty() ? stringList.get(0) : null;
        if(firstOne != null && firstOne.startsWith(ITEMCOLLECTION_TYPE_MARKER))
        {
            offset = 1;
            try
            {
                coll.setWrapedCollectionType(
                                Integer.parseInt(firstOne.substring(ITEMCOLLECTION_TYPE_MARKER.length())));
            }
            catch(NumberFormatException e)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("invalid type header '" + firstOne + "' of item collection string '" + value + "'");
                }
            }
        }
        for(int i = offset; i < stringList.size(); i++)
        {
            String str = stringList.get(i);
            coll.add((str != null) ? new ItemPropertyValue(PK.parse(str)) : null);
        }
        return coll;
    }


    public static String convertToDatabase(String realString)
    {
        if(realString == null)
        {
            return null;
        }
        if("".equals(realString) || realString.charAt(0) == '_')
        {
            return "_" + realString;
        }
        return realString;
    }


    public static String convertFromDatabase(String databaseString)
    {
        if(databaseString == null || databaseString.equals(""))
        {
            return null;
        }
        if(databaseString.charAt(0) == '_')
        {
            return databaseString.substring(1);
        }
        return databaseString;
    }


    public static Object convertEntityFinderResult(Object finderResult, SystemEJB sys) throws YFinderException
    {
        if(finderResult == null)
        {
            return null;
        }
        if(finderResult instanceof PK)
        {
            return convertItemResult((PK)finderResult, sys);
        }
        return convertCollectionResult((Collection<PK>)finderResult, sys);
    }


    private static ItemRemote convertItemResult(PK pk, SystemEJB sys) throws YObjectNotFoundException
    {
        try
        {
            return sys.findRemoteObjectByPKInternal(pk);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new YObjectNotFoundException(e.getMessage());
        }
    }


    private static Collection<ItemRemote> convertCollectionResult(Collection<PK> coll, SystemEJB sys) throws YFinderException
    {
        Collection<ItemRemote> items = new ArrayList<>(coll.size());
        for(PK pk : coll)
        {
            try
            {
                items.add(sys.findRemoteObjectByPKInternal(pk));
            }
            catch(YObjectNotFoundException yObjectNotFoundException)
            {
            }
            catch(EJBInvalidParameterException eJBInvalidParameterException)
            {
            }
        }
        return items.isEmpty() ? Collections.EMPTY_LIST : items;
    }
}
