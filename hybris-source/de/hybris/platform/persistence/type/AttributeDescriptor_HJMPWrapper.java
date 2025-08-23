package de.hybris.platform.persistence.type;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.persistence.EJBInvalidParameterException;
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
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class AttributeDescriptor_HJMPWrapper extends AttributeDescriptorEJB
{
    private static boolean LOG = false;
    private static boolean LOG_ATTRIBUTEACCESS = false;
    private static final boolean LOG_MODIFICATIONS = false;
    private static final boolean CHECK_VALUE_DIFFERENCES = Config.getBoolean("hjmp.write.changes.only", true);
    public static final String TYPECODE_STR = "87".intern();

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
        access[5] = new Object[2];
        (new Object[2])[0] = JDBCValueMappings.getInstance().getValueReader(int.class);
        (new Object[2])[1] = JDBCValueMappings.getInstance().getValueWriter(int.class);
        access[4] = new Object[2];
        log = Logger.getLogger(AttributeDescriptor_HJMPWrapper.class.getName());
    }

    private int ejbCreateNestedCounter = 0;
    private boolean beforeCreate = false;
    public static final int READER = 0;
    public static final int WRITER = 1;
    private static final Object[][] access;
    AttributeDescriptorEntityState entityState = null;
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


    private final AttributeDescriptorEntityState getEntityState()
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


    private final void setEntityState(AttributeDescriptorEntityState newEntityState)
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
        AttributeDescriptorEntityState state = AttributeDescriptorEntityStateCacheUnit.getInstance(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pk).getEntityState();
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
        if(getEntityContext().getPersistencePool().isSystemCriticalType(87))
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


    public AttributeDescriptorRemote create(PK param0, AttributeDescriptorRemote param1, List param2, TypeRemote param3, int param4) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        throw new HJMPException("is never called!!");
    }


    public PK ejbCreate(PK param0, AttributeDescriptorRemote param1, List param2, TypeRemote param3, int param4) throws EJBDuplicateQualifierException, EJBInvalidParameterException
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


    public AttributeDescriptorRemote create(PK param0, ComposedTypeRemote param1, String param2, String param3, TypeRemote param4, AtomicTypeRemote param5, int param6, ComposedTypeRemote param7) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        throw new HJMPException("is never called!!");
    }


    public PK ejbCreate(PK param0, ComposedTypeRemote param1, String param2, String param3, TypeRemote param4, AtomicTypeRemote param5, int param6, ComposedTypeRemote param7) throws EJBDuplicateQualifierException, EJBInvalidParameterException
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


    public void ejbPostCreate(PK param0, AttributeDescriptorRemote param1, List param2, TypeRemote param3, int param4) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        super.ejbPostCreate(param0, param1, param2, param3, param4);
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


    public void ejbPostCreate(PK param0, ComposedTypeRemote param1, String param2, String param3, TypeRemote param4, AtomicTypeRemote param5, int param6, ComposedTypeRemote param7) throws EJBDuplicateQualifierException, EJBInvalidParameterException
    {
        super.ejbPostCreate(param0, param1, param2, param3, param4, param5, param6, param7);
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
            (new AttributeDescriptorEntityStateCacheUnit(pool, depl, pk)).hintEntityState(new AttributeDescriptorEntityState(pool, depl, rs));
            return pk;
        }
        catch(SQLException e)
        {
            throw new HJMPException(e);
        }
    }


    public PK getPkString()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PkString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPkString(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting PkString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getCreationTimestampInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        Date result = getEntityState().getCreationTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting CreationTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setCreationTimestampInternal(Date newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting CreationTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getModifiedTimestampInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        Date result = getEntityState().getModifiedTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setModifiedTimestampInternal(Date newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getACLTimestampInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        long result = getEntityState().getACLTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ACLTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setACLTimestampInternal(long newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting ACLTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getTypePkString()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getTypePkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting TypePkString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setTypePkString(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting TypePkString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getOwnerPkString()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getOwnerPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting OwnerPkString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setOwnerPkString(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting OwnerPkString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getPropertyTimestampInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        long result = getEntityState().getPropertyTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PropertyTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPropertyTimestampInternal(long newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
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
            log.debug("setting PropertyTimestampInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getQualifierInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        String result = getEntityState().getQualifierInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting QualifierInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setQualifierInternal(String newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getQualifierInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setQualifierInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setQualifierInternal(newvalue);
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
            log.debug("setting QualifierInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getQualifierLowerCaseInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        String result = getEntityState().getQualifierLowerCaseInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting QualifierLowerCaseInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setQualifierLowerCaseInternal(String newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getQualifierLowerCaseInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setQualifierLowerCaseInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setQualifierLowerCaseInternal(newvalue);
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
            log.debug("setting QualifierLowerCaseInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getAttributeTypePK()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getAttributeTypePK();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting AttributeTypePK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setAttributeTypePK(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getAttributeTypePK();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setAttributeTypePK(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setAttributeTypePK(newvalue);
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
            log.debug("setting AttributeTypePK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getColumnNameInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        String result = getEntityState().getColumnNameInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ColumnNameInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setColumnNameInternal(String newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getColumnNameInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setColumnNameInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setColumnNameInternal(newvalue);
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
            log.debug("setting ColumnNameInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getEnclosingTypePK()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getEnclosingTypePK();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting EnclosingTypePK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setEnclosingTypePK(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getEnclosingTypePK();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setEnclosingTypePK(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setEnclosingTypePK(newvalue);
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
            log.debug("setting EnclosingTypePK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getPersistenceQualifierInternal()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        String result = getEntityState().getPersistenceQualifierInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PersistenceQualifierInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPersistenceQualifierInternal(String newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getPersistenceQualifierInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setPersistenceQualifierInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setPersistenceQualifierInternal(newvalue);
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
            log.debug("setting PersistenceQualifierInternal for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getPersistenceTypePK()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getPersistenceTypePK();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PersistenceTypePK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPersistenceTypePK(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getPersistenceTypePK();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setPersistenceTypePK(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setPersistenceTypePK(newvalue);
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
            log.debug("setting PersistenceTypePK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getSelectionDescriptorPK()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getSelectionDescriptorPK();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting SelectionDescriptorPK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setSelectionDescriptorPK(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getSelectionDescriptorPK();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setSelectionDescriptorPK(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setSelectionDescriptorPK(newvalue);
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
            log.debug("setting SelectionDescriptorPK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public int getAttributeModifiers()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        int result = getEntityState().getAttributeModifiers();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting AttributeModifiers for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setAttributeModifiers(int newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            int oldvalue = oldEntityState.getAttributeModifiers();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setAttributeModifiers(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setAttributeModifiers(newvalue);
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
            log.debug("setting AttributeModifiers for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public boolean getHiddenFlag()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        boolean result = getEntityState().getHiddenFlag();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting HiddenFlag for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setHiddenFlag(boolean newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            boolean oldvalue = oldEntityState.getHiddenFlag();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setHiddenFlag(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setHiddenFlag(newvalue);
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
            log.debug("setting HiddenFlag for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public boolean getPropertyFlag()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        boolean result = getEntityState().getPropertyFlag();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PropertyFlag for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPropertyFlag(boolean newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            boolean oldvalue = oldEntityState.getPropertyFlag();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setPropertyFlag(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setPropertyFlag(newvalue);
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
            log.debug("setting PropertyFlag for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getSuperAttributeDescriptorPK()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        PK result = getEntityState().getSuperAttributeDescriptorPK();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting SuperAttributeDescriptorPK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setSuperAttributeDescriptorPK(PK newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getSuperAttributeDescriptorPK();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setSuperAttributeDescriptorPK(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setSuperAttributeDescriptorPK(newvalue);
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
            log.debug("setting SuperAttributeDescriptorPK for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getInheritancePathString()
    {
        AttributeDescriptorEntityState entityState = getEntityState();
        String result = getEntityState().getInheritancePathString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting InheritancePathString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setInheritancePathString(String newvalue)
    {
        AttributeDescriptorEntityState oldEntityState = getEntityState();
        AttributeDescriptorEntityState newEntityState = null;
        if(CHECK_VALUE_DIFFERENCES)
        {
            String oldvalue = oldEntityState.getInheritancePathString();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setInheritancePathString(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setInheritancePathString(newvalue);
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
            log.debug("setting InheritancePathString for " + this.entityContext.getPK() + " in AttributeDescriptor eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public AttributeDescriptorRemote findByPrimaryKey(PK pkValue) throws YObjectNotFoundException, YFinderException
    {
        throw new HJMPException("never called.");
    }


    public PK ejbFindByPrimaryKey(PK pkValue) throws YObjectNotFoundException
    {
        if(LOG)
        {
            log.debug("AttributeDescriptor ejbFindByPrimaryKey " + pkValue);
        }
        try
        {
            return AttributeDescriptorEntityStateCacheUnit.getInstance(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pkValue).getEntityState().getPK();
        }
        catch(YNoSuchEntityException e)
        {
            throw new YObjectNotFoundException(e.getMessage());
        }
    }


    public Collection findBySuperAttributeDescriptor(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindBySuperAttributeDescriptor(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindBySuperAttributeDescriptor(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindBySuperAttributeDescriptor");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindBySuperAttributeDescriptor1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
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


    public Collection findByEnclosingType(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByEnclosingType(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindByEnclosingType(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindByEnclosingType");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindByEnclosingType1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
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


    public Collection findPublicByEnclosingType(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindPublicByEnclosingType(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindPublicByEnclosingType(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindPublicByEnclosingType");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindPublicByEnclosingType1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
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


    public AttributeDescriptorRemote findDeclaredByEnclosingTypeAndQualifier(PK param0, String param1) throws YFinderException, YObjectNotFoundException
    {
        return (AttributeDescriptorRemote)EJBTools.convertEntityFinderResult(ejbFindDeclaredByEnclosingTypeAndQualifier(param0, param1), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public PK ejbFindDeclaredByEnclosingTypeAndQualifier(PK param0, String param1) throws YFinderException, YObjectNotFoundException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindDeclaredByEnclosingTypeAndQualifier");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            if(LOG)
            {
                log.debug("  1->" + param1);
            }
            Object resultObject = (new FindDeclaredByEnclosingTypeAndQualifier2FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0, param1)).getFinderResult();
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


    public Collection findInheritedByQualifierAndInheritancePath(String param0, String param1) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindInheritedByQualifierAndInheritancePath(param0, param1), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindInheritedByQualifierAndInheritancePath(String param0, String param1) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindInheritedByQualifierAndInheritancePath");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            if(LOG)
            {
                log.debug("  1->" + param1);
            }
            Object resultObject = (new FindInheritedByQualifierAndInheritancePath2FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0, param1)).getFinderResult();
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


    public AttributeDescriptorRemote findByEnclosingTypeAndQualifier(PK param0, String param1) throws YFinderException, YObjectNotFoundException
    {
        return (AttributeDescriptorRemote)EJBTools.convertEntityFinderResult(ejbFindByEnclosingTypeAndQualifier(param0, param1), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public PK ejbFindByEnclosingTypeAndQualifier(PK param0, String param1) throws YFinderException, YObjectNotFoundException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindByEnclosingTypeAndQualifier");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            if(LOG)
            {
                log.debug("  1->" + param1);
            }
            Object resultObject = (new FindByEnclosingTypeAndQualifier2FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0, param1)).getFinderResult();
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


    public Collection findByEnclosingTypeAndSelectionDescriptor(PK param0, PK param1) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByEnclosingTypeAndSelectionDescriptor(param0, param1), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindByEnclosingTypeAndSelectionDescriptor(PK param0, PK param1) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindByEnclosingTypeAndSelectionDescriptor");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            if(LOG)
            {
                log.debug("  1->" + param1);
            }
            Object resultObject = (new FindByEnclosingTypeAndSelectionDescriptor2FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0, param1)).getFinderResult();
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
                log.debug("AttributeDescriptor ejbFindAll");
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


    public Collection findPropertyByEnclosingType(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindPropertyByEnclosingType(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindPropertyByEnclosingType(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindPropertyByEnclosingType");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindPropertyByEnclosingType1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
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
                log.debug("AttributeDescriptor ejbFindChangedAfter");
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


    public Collection findInhertitedByEnclosingType(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindInhertitedByEnclosingType(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindInhertitedByEnclosingType(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindInhertitedByEnclosingType");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindInhertitedByEnclosingType1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
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


    public Collection findDeclaredByEnclosingType(PK param0) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindDeclaredByEnclosingType(param0), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public Collection ejbFindDeclaredByEnclosingType(PK param0) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindDeclaredByEnclosingType");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            Object resultObject = (new FindDeclaredByEnclosingType1FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0)).getFinderResult();
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
                log.debug("AttributeDescriptor ejbFindByType");
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


    public AttributeDescriptorRemote findPublicByEnclosingTypeAndQualifier(PK param0, String param1) throws YFinderException, YObjectNotFoundException
    {
        return (AttributeDescriptorRemote)EJBTools.convertEntityFinderResult(ejbFindPublicByEnclosingTypeAndQualifier(param0, param1), getEntityContext().getPersistencePool().getTenant().getSystemEJB());
    }


    public PK ejbFindPublicByEnclosingTypeAndQualifier(PK param0, String param1) throws YFinderException, YObjectNotFoundException
    {
        try
        {
            if(LOG)
            {
                log.debug("AttributeDescriptor ejbFindPublicByEnclosingTypeAndQualifier");
            }
            if(LOG)
            {
                log.debug("  0->" + param0);
            }
            if(LOG)
            {
                log.debug("  1->" + param1);
            }
            Object resultObject = (new FindPublicByEnclosingTypeAndQualifier2FinderResult(getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), param0, param1)).getFinderResult();
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
                log.debug("AttributeDescriptor ejbFindByPKList");
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
        return "de.hybris.platform.persistence.type.AttributeDescriptor";
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
