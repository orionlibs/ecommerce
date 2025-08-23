package de.hybris.platform.persistence.meta;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.persistence.ItemCacheKey;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.hjmp.HJMPException;
import de.hybris.platform.persistence.hjmp.HJMPFinderException;
import de.hybris.platform.persistence.property.EJBPropertyRowCache;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;

public class MetaInformation_HJMPWrapper extends MetaInformationEJB
{
    private static boolean LOG = false;
    private static boolean LOG_ATTRIBUTEACCESS = false;
    private static final boolean LOG_MODIFICATIONS = false;
    private static final boolean CHECK_VALUE_DIFFERENCES = Config.getBoolean("hjmp.write.changes.only", true);
    public static final String TYPECODE_STR = "55".intern();

    static
    {
        LOG = "true".equals(System.getProperty("hjmp.log"));
        LOG_ATTRIBUTEACCESS = "true".equals(System.getProperty("hjmp.log.attributeaccess"));
        access = new Object[6][2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(Date.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(Date.class);
        access[2] = new Object[2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(PK.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(PK.class);
        access[0] = new Object[2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(String.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(String.class);
        access[1] = new Object[2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(long.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(long.class);
        access[3] = new Object[2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(boolean.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(boolean.class);
        access[4] = new Object[2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(int.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(int.class);
        access[5] = new Object[2];
        log = Logger.getLogger(MetaInformation_HJMPWrapper.class.getName());
    }

    private int ejbCreateNestedCounter = 0;
    private boolean beforeCreate = false;
    public static final int READER = 0;
    public static final int WRITER = 1;
    private static final Object[][] access;
    MetaInformationEntityState entityState = null;
    boolean localInvalidationAdded = false;
    static final Logger log;


    private void clearFields()
    {
        this.localInvalidationAdded = false;
        setEntityState(null);
        if(this.ejbCreateNestedCounter != 0)
        {
            if(LOG)
            {
                log.info("ejbCreateNestedCounter is " + this.ejbCreateNestedCounter + " in clearFields()");
            }
            this.ejbCreateNestedCounter = 0;
        }
    }


    public void ejbLoad()
    {
        clearFields();
        loadData();
        super.ejbLoad();
    }


    public long getHJMPTS()
    {
        return (getEntityState()).hjmpTS;
    }


    private final String getID()
    {
        return Integer.toHexString(System.identityHashCode(this));
    }


    private final MetaInformationEntityState getEntityState()
    {
        if(this.entityState == null)
        {
            throw new HJMPException("no entity state available for " + this.entityContext.getPK());
        }
        return this.entityState;
    }


    private final void invalidate(boolean success, boolean removed)
    {
        Transaction tx = Transaction.current();
        PK thePK = getEntityState().getPK();
        int invType = removed ? 2 : 1;
        Object[] key = {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, thePK.getTypeCodeAsString(), thePK};
        if(tx.isRunning())
        {
            tx.invalidate(key, 3, invType);
        }
        else if(success)
        {
            tx.invalidateAndNotifyCommit(key, 3, invType);
        }
        else
        {
            tx.invalidateAndNotifyRollback(key, 3, invType);
        }
    }


    private final void setEntityState(MetaInformationEntityState newEntityState)
    {
        if(newEntityState != null)
        {
            setNeedsStoring(true);
        }
        this.entityState = newEntityState;
        if(this.ejbCreateNestedCounter == 0)
        {
            addLocalRollbackInvalidation(false);
        }
    }


    private final void addLocalRollbackInvalidation(boolean remove)
    {
        if(!this.localInvalidationAdded && this.entityState != null && this.entityState.isTransactionBound && Transaction.current().isRunning())
        {
            this.localInvalidationAdded = true;
            Object[] key = {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, TYPECODE_STR, this.entityContext.getPK()};
            Transaction.current().addToDelayedRollbackLocalInvalidations(key,
                            remove ? 2 : 1, 3);
        }
    }


    private final void loadData()
    {
        PK pk = this.entityContext.getPK();
        MetaInformationEntityState state = MetaInformationEntityStateCacheUnit.getInstance(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pk).getEntityState();
        if(LOG)
        {
            log.debug("EJB loading " + pk + " to " + getID() + ": " + state.toDetailedString());
        }
        if(state == null)
        {
            throw new YNoSuchEntityException("entity " + pk + " not found", pk);
        }
        setEntityState(state);
        setNeedsStoring(false);
    }


    private OperationInfo getEJBOperationInfo()
    {
        if(getEntityContext().getPersistencePool().isSystemCriticalType(55))
        {
            return OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build();
        }
        return OperationInfo.empty();
    }


    public void ejbStore()
    {
        if(LOG)
        {
            log.debug("EJB storing " + getPkString() + " from " + getID() + " (tx-bound=" + needsStoring() + ")");
        }
        if(needsStoring())
        {
            boolean fieldsChanged = getEntityState().hasChangedFields();
            boolean cachesChanged = hasModifiedCaches();
            if(fieldsChanged || cachesChanged)
            {
                boolean success = false;
                try
                {
                    RevertibleUpdate theUpdate = OperationInfo.updateThread(getEJBOperationInfo());
                    try
                    {
                        super.ejbStore();
                        setModifiedTimestamp(new Date());
                        if(cachesChanged)
                        {
                            storeCaches();
                        }
                        super.ejbStore();
                        if(cachesChanged)
                        {
                            writePropertyCaches();
                        }
                        writeACLEntries();
                        EJBPropertyRowCache prc = cachesChanged ? getModifiedUnlocalizedPropertyCache() : null;
                        TypeInfoMap infoMap = (prc != null) ? getTypeInfoMap() : null;
                        setEntityState(getEntityState().storeChanges(prc, infoMap));
                        setNeedsStoring(false);
                        success = true;
                        if(theUpdate != null)
                        {
                            theUpdate.close();
                        }
                    }
                    catch(Throwable throwable)
                    {
                        if(theUpdate != null)
                        {
                            try
                            {
                                theUpdate.close();
                            }
                            catch(Throwable throwable1)
                            {
                                throwable.addSuppressed(throwable1);
                            }
                        }
                        throw throwable;
                    }
                }
                finally
                {
                    invalidate(success, false);
                }
            }
            else
            {
                setNeedsStoring(false);
            }
        }
    }


    public MetaInformationRemote create(PK param0, String param1)
    {
        throw new HJMPException("is never called!!");
    }


    public PK ejbCreate(PK param0, String param1)
    {
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(getEJBOperationInfo());
        }
        finally
        {
            this.ejbCreateNestedCounter--;
        }
    }


    public void ejbPostCreate(PK param0, String param1)
    {
        super.ejbPostCreate(param0, param1);
        if(needsStoring())
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(getEJBOperationInfo());
            try
            {
                EJBPropertyRowCache prc = getModifiedUnlocalizedPropertyCache();
                TypeInfoMap infoMap = (prc != null) ? getTypeInfoMap() : null;
                setEntityState(getEntityState().storeChanges(prc, infoMap));
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
    }


    public void ejbRemove()
    {
        loadData();
        boolean success = false;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(getEJBOperationInfo());
            try
            {
                super.ejbRemove();
                if(LOG)
                {
                    log.debug("EJB removing " + getPkString() + " in " + getID());
                }
                removePropertyData();
                removeACLEntries();
                setEntityState(getEntityState().removeEntity());
                success = true;
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        finally
        {
            invalidate(success, true);
        }
        clearFields();
    }


    public void ejbHomeLoadItemData(ResultSet rs)
    {
        PK pk = handleResultRow(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), rs);
        log.debug("loaded item " + pk);
    }


    private static final Collection handleResult(PersistencePool pool, ItemDeployment depl, ResultSet rs)
    {
        try
        {
            if(rs.next())
            {
                Collection<PK> result = new ArrayList();
                while(true)
                {
                    result.add(handleResultRow(pool, depl, rs));
                    if(!rs.next())
                    {
                        return result;
                    }
                }
            }
            return Collections.EMPTY_LIST;
        }
        catch(SQLException e)
        {
            throw new HJMPException(e);
        }
    }


    private static final PK handleResultRow(PersistencePool pool, ItemDeployment depl, ResultSet rs)
    {
        try
        {
            PK pk = (PK)(pool.getJDBCValueMappings()).PK_READER.getValue(rs, "PK");
            (new MetaInformationEntityStateCacheUnit(pool, depl, pk)).hintEntityState(new MetaInformationEntityState(pool, depl, rs));
            return pk;
        }
        catch(SQLException e)
        {
            throw new HJMPException(e);
        }
    }


    public PK getPkString()
    {
        MetaInformationEntityState entityState = getEntityState();
        PK result = getEntityState().getPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PkString for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPkString(PK newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getPkString();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setPkString(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setPkString(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting PkString for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getCreationTimestampInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        Date result = getEntityState().getCreationTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting CreationTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setCreationTimestampInternal(Date newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            Date oldvalue = oldEntityState.getCreationTimestampInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setCreationTimestampInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setCreationTimestampInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting CreationTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getModifiedTimestampInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        Date result = getEntityState().getModifiedTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setModifiedTimestampInternal(Date newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            Date oldvalue = oldEntityState.getModifiedTimestampInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setModifiedTimestampInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setModifiedTimestampInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getACLTimestampInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        long result = getEntityState().getACLTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ACLTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setACLTimestampInternal(long newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            long oldvalue = oldEntityState.getACLTimestampInternal();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setACLTimestampInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setACLTimestampInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting ACLTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getTypePkString()
    {
        MetaInformationEntityState entityState = getEntityState();
        PK result = getEntityState().getTypePkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting TypePkString for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setTypePkString(PK newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getTypePkString();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setTypePkString(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setTypePkString(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting TypePkString for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getOwnerPkString()
    {
        MetaInformationEntityState entityState = getEntityState();
        PK result = getEntityState().getOwnerPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting OwnerPkString for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setOwnerPkString(PK newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getOwnerPkString();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setOwnerPkString(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setOwnerPkString(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting OwnerPkString for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getPropertyTimestampInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        long result = getEntityState().getPropertyTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PropertyTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPropertyTimestampInternal(long newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            long oldvalue = oldEntityState.getPropertyTimestampInternal();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setPropertyTimestampInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setPropertyTimestampInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting PropertyTimestampInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getSystemPKInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        String result = getEntityState().getSystemPKInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting SystemPKInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setSystemPKInternal(String newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getSystemPKInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setSystemPKInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setSystemPKInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting SystemPKInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getSystemNameInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        String result = getEntityState().getSystemNameInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting SystemNameInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setSystemNameInternal(String newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getSystemNameInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setSystemNameInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setSystemNameInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting SystemNameInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public boolean getInitializedFlagInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        boolean result = getEntityState().getInitializedFlagInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting InitializedFlagInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setInitializedFlagInternal(boolean newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            boolean oldvalue = oldEntityState.getInitializedFlagInternal();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setInitializedFlagInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setInitializedFlagInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting InitializedFlagInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getLicenceIDInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        String result = getEntityState().getLicenceIDInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting LicenceIDInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setLicenceIDInternal(String newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getLicenceIDInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setLicenceIDInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setLicenceIDInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting LicenceIDInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getLicenceNameInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        String result = getEntityState().getLicenceNameInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting LicenceNameInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setLicenceNameInternal(String newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getLicenceNameInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setLicenceNameInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setLicenceNameInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting LicenceNameInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getLicenceEditionInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        String result = getEntityState().getLicenceEditionInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting LicenceEditionInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setLicenceEditionInternal(String newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getLicenceEditionInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setLicenceEditionInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setLicenceEditionInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting LicenceEditionInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public int getLicenceAdminFactorInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        int result = getEntityState().getLicenceAdminFactorInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting LicenceAdminFactorInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setLicenceAdminFactorInternal(int newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            int oldvalue = oldEntityState.getLicenceAdminFactorInternal();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setLicenceAdminFactorInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setLicenceAdminFactorInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting LicenceAdminFactorInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getLicenceExpirationDateInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        Date result = getEntityState().getLicenceExpirationDateInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting LicenceExpirationDateInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setLicenceExpirationDateInternal(Date newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            Date oldvalue = oldEntityState.getLicenceExpirationDateInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setLicenceExpirationDateInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setLicenceExpirationDateInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting LicenceExpirationDateInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getLicenceSignatureInternal()
    {
        MetaInformationEntityState entityState = getEntityState();
        String result = getEntityState().getLicenceSignatureInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting LicenceSignatureInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setLicenceSignatureInternal(String newvalue)
    {
        MetaInformationEntityState oldEntityState = getEntityState();
        MetaInformationEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getLicenceSignatureInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setLicenceSignatureInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setLicenceSignatureInternal(newvalue);
            setEntityState(newEntityState);
        }
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + ((newEntityState != null) ? newEntityState.getID() : "n/a");
            }
            log.debug("setting LicenceSignatureInternal for " + this.entityContext.getPK() + " in MetaInformation eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public MetaInformationRemote findByPrimaryKey(PK pkValue) throws YObjectNotFoundException, YFinderException
    {
        throw new HJMPException("never called.");
    }


    public PK ejbFindByPrimaryKey(PK pkValue) throws YObjectNotFoundException
    {
        if(LOG)
        {
            log.debug("MetaInformation ejbFindByPrimaryKey " + pkValue);
        }
        try
        {
            return MetaInformationEntityStateCacheUnit.getInstance(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pkValue).getEntityState().getPK();
        }
        catch(YNoSuchEntityException e)
        {
            throw new YObjectNotFoundException(e.getMessage());
        }
    }


    public Collection findByType(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByType(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindByType(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("MetaInformation ejbFindByType");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindByType1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
            if(LOG)
            {
                log.debug("  result=" + resultObject);
            }
            return (Collection)resultObject;
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    public MetaInformationRemote findBySystemID(String param0) throws YFinderException, YObjectNotFoundException
    {
        return (MetaInformationRemote)EJBTools.convertEntityFinderResult(ejbFindBySystemID(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public PK ejbFindBySystemID(String param0) throws YFinderException, YObjectNotFoundException
    {
        try
        {
            if(LOG)
            {
                log.debug("MetaInformation ejbFindBySystemID");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindBySystemID1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
            if(LOG)
            {
                log.debug("  result=" + resultObject);
            }
            if(resultObject == null)
            {
                throw new YObjectNotFoundException();
            }
            return (PK)resultObject;
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    public Collection findChangedAfter(Date param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindChangedAfter(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindChangedAfter(Date param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("MetaInformation ejbFindChangedAfter");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindChangedAfter1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
            if(LOG)
            {
                log.debug("  result=" + resultObject);
            }
            return (Collection)resultObject;
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    public Collection findAll() throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindAll(), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindAll() throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("MetaInformation ejbFindAll");
            }
            Object resultObject = (new FindAll0FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment())).getFinderResult();
            if(LOG)
            {
                log.debug("  result=" + resultObject);
            }
            return (Collection)resultObject;
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    public Collection findByPKList(Collection pks) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByPKList(pks), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindByPKList(Collection pks) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("MetaInformation ejbFindByPKList");
            }
            if(LOG)
            {
                log.debug("  0->" + pks);
            }
            Object resultObject = (new FindByPKListFinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pks)).getFinderResult();
            if(LOG)
            {
                log.debug("  result=" + resultObject);
            }
            return (Collection)resultObject;
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    protected String getItemTableNameImpl()
    {
        return getEntityContext().getItemDeployment().getDatabaseTableName();
    }


    public String getOwnJNDIName()
    {
        return "de.hybris.platform.persistence.meta.MetaInformation";
    }


    public String getPropertyTableNameImpl()
    {
        return getEntityContext().getItemDeployment().getDumpPropertyTableName();
    }


    public boolean isBeforeCreate()
    {
        return this.beforeCreate;
    }


    protected Object getCachedValueForModification(ItemCacheKey key)
    {
        setEntityState(getEntityState().getModifiedVersion());
        return super.getCachedValueForModification(key);
    }


    protected Map getCacheKeyMap()
    {
        return getEntityState().getCacheKeyMap();
    }
}
