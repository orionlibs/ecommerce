package de.hybris.platform.persistence;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditableOperation;
import de.hybris.platform.persistence.audit.AuditableOperations;
import de.hybris.platform.persistence.audit.Operation;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.hjmp.HJMPException;
import de.hybris.platform.persistence.hjmp.HJMPFinderException;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.persistence.hjmp.HybrisOptimisticLockingFailureException;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.property.EJBPropertyRowCache;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.jeeapi.YFinderException;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import de.hybris.platform.util.jeeapi.YObjectNotFoundException;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;

public class GenericBMPBean extends GenericItemEJB
{
    private static final Logger LOGGER = Logger.getLogger(GenericBMPBean.class.getName());
    private static final boolean LOG = "true".equals(System.getProperty("hjmp.log"));
    private static final boolean LOG_ATTRIBUTEACCESS = "true".equals(System.getProperty("hjmp.log.attributeaccess"));
    private static final boolean LOG_MODIFICATIONS = false;
    private static final boolean CHECK_VALUE_DIFFERENCES = Config.getBoolean("hjmp.write.changes.only", true);
    private static final int READER = 0;
    private static final int WRITER = 1;
    private static final Object[] DATE_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(Date.class),
                    JDBCValueMappings.getInstance().getValueWriter(Date.class)};
    private static final Object[] STRING_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(String.class),
                    JDBCValueMappings.getInstance().getValueWriter(String.class)};
    private static final Object[] LONG_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(long.class), JDBCValueMappings.getInstance().getValueWriter(long.class)};
    private static final Object[] PK_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(PK.class), JDBCValueMappings.getInstance().getValueWriter(PK.class)};
    private int ejbCreateNestedCounter = 0;
    private boolean beforeCreate = false;
    private GenericItemEntityState entityState = null;
    private boolean localInvalidationAdded = false;


    private ItemDeployment getDeployment()
    {
        return getEntityContext().getItemDeployment();
    }


    private PersistencePool getPersistencePool()
    {
        return getEntityContext().getPersistencePool();
    }


    private String getTable()
    {
        return getDeployment().getDatabaseTableName();
    }


    private int getTypeCode()
    {
        return getDeployment().getTypeCode();
    }


    private String getDumptable()
    {
        return getDeployment().getDumpPropertyTableName();
    }


    private void clearFields()
    {
        this.localInvalidationAdded = false;
        setEntityState(null);
        if(this.ejbCreateNestedCounter != 0)
        {
            if(LOG)
            {
                LOGGER.debug("ejbCreateNestedCounter is " + this.ejbCreateNestedCounter);
            }
            this.ejbCreateNestedCounter = 0;
        }
    }


    public long getHJMPTS()
    {
        return (getEntityState()).hjmpTS;
    }


    public void ejbLoad()
    {
        clearFields();
        loadData();
        super.ejbLoad();
    }


    private String getID()
    {
        return Integer.toHexString(System.identityHashCode(this));
    }


    public final GenericItemEntityState getEntityState()
    {
        if(this.entityState == null)
        {
            throw new HJMPException("no entity state available for " + this.entityContext.getPK());
        }
        return this.entityState;
    }


    private void invalidate(boolean success, int type)
    {
        Transaction tx = Transaction.current();
        PK thePK = getEntityState().getPK();
        int invType = type;
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


    private void setEntityState(GenericItemEntityState newEntityState)
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


    private void addLocalRollbackInvalidation(boolean remove)
    {
        if(!this.localInvalidationAdded && this.entityState != null && this.entityState.isTransactionBound && Transaction.current().isRunning())
        {
            this.localInvalidationAdded = true;
            Object[] key = {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, PlatformStringUtils.valueOf(getDeployment().getTypeCode()), this.entityContext.getPK()};
            Transaction.current().addToDelayedRollbackLocalInvalidations(key,
                            remove ? 2 : 1, 3);
        }
    }


    private void loadData()
    {
        PK pk = this.entityContext.getPK();
        Preconditions.checkArgument((pk != null));
        GenericItemEntityState state = GenericItemEntityState.getInstance(getPersistencePool().getCache(), pk, getDeployment()).getEntityState();
        if(LOG)
        {
            LOGGER.debug("EJB loading " + pk + " to " + getID() + ": " + state.toDetailedString());
        }
        if(state == null)
        {
            int typeCode = 0;
            String name = null;
            String dbTable = null;
            if(getDeployment() != null)
            {
                try
                {
                    typeCode = getDeployment().getTypeCode();
                    name = getDeployment().getName();
                    dbTable = getDeployment().getDatabaseTableName();
                }
                catch(Exception e)
                {
                    if(LOGGER.isDebugEnabled())
                    {
                        LOGGER.debug(e.getMessage());
                    }
                }
            }
            String errorMsg = "";
            errorMsg = "Entity not found ( pk = " + pk + " " + ((name != null && name.length() > 0) ? ("name = '" + name + "' ") : "") + ((typeCode != 0) ? ("type code = '" + typeCode + "' ") : "") + ((dbTable != null && dbTable.length() > 0) ? ("db table = '" + dbTable + "'") : "") + ")";
            throw new YNoSuchEntityException(errorMsg, pk);
        }
        setEntityState(state);
        setNeedsStoring(false);
    }


    public void ejbStore()
    {
        PK pk = getPkString();
        if(LOG)
        {
            LOGGER.debug("EJB storing " + pk + " from " + getID() + " (tx-bound=" + needsStoring() + ")");
        }
        if(needsStoring())
        {
            GenericItemEntityState entityState = getEntityState();
            boolean fieldsChanged = entityState.hasChangedFields();
            boolean cachesChanged = hasModifiedCaches();
            checkOptimisticLockVersionIsValidForServiceLayer(entityState);
            if(fieldsChanged || cachesChanged)
            {
                boolean success = false;
                AuditableOperation audit = AuditableOperations.aboutToExecute(new Operation[] {Operation.update(getPkString(), getTypePkString())});
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
                    setEntityState(entityState.storeChanges(prc, infoMap));
                    setNeedsStoring(false);
                    success = true;
                }
                finally
                {
                    invalidate(success, 1);
                    audit.finish(success);
                }
            }
            else
            {
                setNeedsStoring(false);
            }
        }
    }


    private void checkOptimisticLockVersionIsValidForServiceLayer(GenericItemEntityState entityState)
    {
        if(HJMPUtils.isOptimisticLockingEnabledForType(entityState.getTypePkString()))
        {
            PK pkString = getPK();
            Long versionFromServiceLayer = HJMPUtils.getVersionForPk(pkString);
            if(versionFromServiceLayer != null)
            {
                long hjmpTSFromStartOfTransaction = entityState.hjmpTSFromStartOfTransaction;
                long hjmpTS = entityState.hjmpTS;
                if(versionFromServiceLayer.longValue() < hjmpTSFromStartOfTransaction)
                {
                    throw new HJMPException(
                                    new HybrisOptimisticLockingFailureException("item pk " + pkString + " was modified concurrently - expected version " + versionFromServiceLayer + " (from model) but got " + hjmpTSFromStartOfTransaction + ".." + hjmpTS + ", entity state = " + entityState, null));
                }
            }
        }
    }


    protected int typeCode()
    {
        return getTypeCode();
    }


    public boolean isBeforeCreate()
    {
        return this.beforeCreate;
    }


    public PK ejbCreate(PK pkBase, ComposedTypeRemote type, EJBPropertyContainer props)
    {
        try
        {
            this.ejbCreateNestedCounter++;
            if(this.ejbCreateNestedCounter == 1)
            {
                setEntityState(new GenericItemEntityState(getPersistencePool(), getDeployment()));
            }
            else if(this.entityState == null)
            {
                throw new HJMPException("no entity state in nested ejbCreate");
            }
            this.beforeCreate = true;
            super.ejbCreate(pkBase, type, props);
            if(this.ejbCreateNestedCounter == 1)
            {
                boolean success = false;
                AuditableOperation audit = AuditableOperations.aboutToExecute(new Operation[] {Operation.create(getPkString(), getTypePkString())});
                try
                {
                    if(LOG)
                    {
                        LOGGER.debug("EJB creating " + getPkString() + " in " + getID());
                    }
                    EJBPropertyRowCache prc = getModifiedUnlocalizedPropertyCache();
                    TypeInfoMap infoMap = (prc != null) ? getTypeInfoMap() : null;
                    getEntityState().createEntity(prc, infoMap);
                    this.beforeCreate = false;
                    writePropertyCaches();
                    writeACLEntries();
                    success = true;
                }
                finally
                {
                    addLocalRollbackInvalidation(true);
                    invalidate(success, 4);
                    this.beforeCreate = false;
                    audit.finish(success);
                }
            }
            else if(LOG)
            {
                LOGGER.debug("nested ejbCreate in " + getID());
            }
            return getPkString();
        }
        finally
        {
            this.ejbCreateNestedCounter--;
        }
    }


    public GenericItemRemote create(PK pkBase, ComposedTypeRemote type, EJBPropertyContainer props)
    {
        throw new RuntimeException("never called!");
    }


    public void ejbPostCreate(PK param0, ComposedTypeRemote param1, EJBPropertyContainer param2)
    {
        super.ejbPostCreate(param0, param1, param2);
        if(needsStoring())
        {
            EJBPropertyRowCache prc = getModifiedUnlocalizedPropertyCache();
            TypeInfoMap infoMap = (prc != null) ? getTypeInfoMap() : null;
            GenericItemEntityState entityState = getEntityState();
            checkOptimisticLockVersionIsValidForServiceLayer(entityState);
            setEntityState(entityState.storeChanges(prc, infoMap));
        }
    }


    public void ejbRemove()
    {
        loadData();
        boolean success = false;
        AuditableOperation audit = AuditableOperations.aboutToExecute(new Operation[] {Operation.delete(getPkString(), getTypePkString())});
        try
        {
            super.ejbRemove();
            if(LOG)
            {
                LOGGER.debug("EJB removing " + getPkString() + " in " + getID());
            }
            if(!Config.getBoolean("props.removal.optimisation", true))
            {
                removePropertyData();
            }
            removeACLEntries();
            setEntityState(getEntityState().removeEntity());
            success = true;
        }
        finally
        {
            invalidate(success, 2);
            audit.finish(success);
        }
        clearFields();
    }


    public void ejbHomeLoadItemData(ResultSet resultSet)
    {
        PK pk = handleResultRow(getPersistencePool(), resultSet, getDeployment());
        if(LOG && LOGGER.isDebugEnabled())
        {
            LOGGER.debug("loaded item " + pk);
        }
    }


    private static Collection handleResult(PersistencePool pool, ResultSet resultSet, ItemDeployment depl)
    {
        try
        {
            if(resultSet.next())
            {
                Collection<PK> result = new ArrayList();
                while(true)
                {
                    result.add(handleResultRow(pool, resultSet, depl));
                    if(!resultSet.next())
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


    private static PK handleResultRow(PersistencePool pool, ResultSet resultSet, ItemDeployment depl)
    {
        try
        {
            PK pk = (PK)((JDBCValueMappings.ValueReader)PK_RW[0]).getValue(resultSet, "PK");
            (new GenericItemEntityStateCacheUnit(pool.getCache(), pk, depl))
                            .hintEntityState((AbstractEntityState)new GenericItemEntityState(pool, resultSet, pk, depl));
            return pk;
        }
        catch(SQLException e)
        {
            throw new HJMPException(e);
        }
    }


    public PK getPkString()
    {
        GenericItemEntityState entityState = getEntityState();
        PK result = getEntityState().getPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting PkString for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setPkString(PK newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            LOGGER.debug("setting PkString for " + this.entityContext
                            .getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getCreationTimestampInternal()
    {
        GenericItemEntityState entityState = getEntityState();
        Date result = getEntityState().getCreationTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting CreationTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setCreationTimestampInternal(Date newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
        newEntityState = oldEntityState.setCreationTimestampInternal(newvalue);
        setEntityState(newEntityState);
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + newEntityState.getID();
            }
            LOGGER.debug("setting CreationTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getModifiedTimestampInternal()
    {
        GenericItemEntityState entityState = getEntityState();
        Date result = getEntityState().getModifiedTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setModifiedTimestampInternal(Date newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
        newEntityState = oldEntityState.setModifiedTimestampInternal(newvalue);
        setEntityState(newEntityState);
        if(LOG_ATTRIBUTEACCESS)
        {
            String sidString;
            if(oldEntityState == newEntityState)
            {
                sidString = "sid=" + oldEntityState.getID();
            }
            else
            {
                sidString = "oldsid=" + oldEntityState.getID() + " newsid=" + newEntityState.getID();
            }
            LOGGER.debug("setting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getACLTimestampInternal()
    {
        GenericItemEntityState entityState = getEntityState();
        long result = getEntityState().getACLTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting ACLTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setACLTimestampInternal(long newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            LOGGER.debug("setting ACLTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getTypePkString()
    {
        GenericItemEntityState entityState = getEntityState();
        PK result = getEntityState().getTypePkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting TypePkString for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setTypePkString(PK newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            LOGGER.debug("setting TypePkString for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getOwnerPkString()
    {
        GenericItemEntityState entityState = getEntityState();
        PK result = getEntityState().getOwnerPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting OwnerPkString for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setOwnerPkString(PK newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            LOGGER.debug("setting OwnerPkString for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getPropertyTimestampInternal()
    {
        GenericItemEntityState entityState = getEntityState();
        long result = getEntityState().getPropertyTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            LOGGER.debug("getting PropertyTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setPropertyTimestampInternal(long newvalue)
    {
        GenericItemEntityState oldEntityState = getEntityState();
        GenericItemEntityState newEntityState = null;
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            LOGGER.debug("setting PropertyTimestampInternal for " + this.entityContext.getPK() + " in GenericItem eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK ejbFindByPrimaryKey(PK pkValue) throws YObjectNotFoundException
    {
        if(LOG)
        {
            LOGGER.debug("GenericItem ejbFindByPrimaryKey " + pkValue);
        }
        GenericItemEntityState genericItemEntityState = GenericItemEntityState.getInstance(getPersistencePool().getCache(), pkValue, getDeployment()).getEntityState();
        if(genericItemEntityState == null)
        {
            if(LOG)
            {
                LOGGER.debug("'" + pkValue + "'  not found");
            }
            throw new YObjectNotFoundException("'" + pkValue + "'  not found");
        }
        if(LOG)
        {
            LOGGER.debug("  found entry");
        }
        return genericItemEntityState.getPK();
    }


    public GenericItemRemote findByPrimaryKey(PK pk) throws YObjectNotFoundException, YFinderException
    {
        throw new HJMPException("illegal call to findByPrimaryKey!");
    }


    public Collection findChangedAfter(Date param0) throws YFinderException
    {
        throw new HJMPException("findChangedAfter() not supported in generic deployments");
    }


    public Collection ejbFindAll() throws YFinderException
    {
        throw new HJMPException("findAll() no supported in generic deployments");
    }


    public Collection findAll() throws YFinderException
    {
        throw new HJMPException("findAll() no supported in generic deployments");
    }


    public static boolean checkTypeCodes(Collection<PK> pks, int typeCode)
    {
        if(pks != null)
        {
            for(PK pk : pks)
            {
                if(pk.getTypeCode() != typeCode)
                {
                    return false;
                }
            }
        }
        return true;
    }


    public Collection ejbFindByPKList(Collection<PK> pkList) throws YFinderException
    {
        try
        {
            return (Collection)(new FindByPKListFinderResult(getPersistencePool(), getDeployment(), pkList)).getFinderResult();
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    public Collection ejbFindByType(PK typePK) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                LOGGER.debug("GenericBMPBean ejbFindByType");
            }
            if(LOG)
            {
                LOGGER.debug("  0->" + typePK);
            }
            Object resultObject = (new FindByType1FinderResult(getPersistencePool(), getDeployment(), typePK)).getFinderResult();
            if(LOG)
            {
                LOGGER.debug("  result=" + resultObject);
            }
            return (Collection)resultObject;
        }
        catch(SQLException e)
        {
            throw new HJMPFinderException(e);
        }
    }


    public Collection findByType(PK typePK) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByType(typePK),
                        getEntityContext().getPersistencePool()
                                        .getTenant()
                                        .getSystemEJB());
    }


    public Collection findByPKList(Collection<PK> pks) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByPKList(pks),
                        getEntityContext().getPersistencePool()
                                        .getTenant()
                                        .getSystemEJB());
    }


    protected String getItemTableNameImpl()
    {
        return getTable();
    }


    public String getOwnJNDIName()
    {
        return getDeployment().getName();
    }


    public String getPropertyTableNameImpl()
    {
        return getDumptable();
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
