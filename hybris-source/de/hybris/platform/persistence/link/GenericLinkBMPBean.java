package de.hybris.platform.persistence.link;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.AdditionalInvalidationData;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationKey;
import de.hybris.platform.cache.RelationsCache;
import de.hybris.platform.cache.relation.RelationAttributes;
import de.hybris.platform.cache.relation.TypeId;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.GenericBMPBean;
import de.hybris.platform.persistence.ItemCacheKey;
import de.hybris.platform.persistence.audit.AuditableOperation;
import de.hybris.platform.persistence.audit.AuditableOperations;
import de.hybris.platform.persistence.audit.Operation;
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
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import org.apache.log4j.Logger;

public class GenericLinkBMPBean extends LinkEJB
{
    private static final Logger log = Logger.getLogger(GenericBMPBean.class.getName());
    private static final boolean LOG = "true".equals(System.getProperty("hjmp.log"));
    private static final boolean LOG_ATTRIBUTEACCESS = "true".equals(System.getProperty("hjmp.log.attributeaccess"));
    private static final boolean LOG_MODIFICATIONS = false;
    private static final int READER = 0;
    private static final int WRITER = 1;
    private final boolean CHECK_VALUE_DIFFERENCES = Config.getBoolean("hjmp.write.changes.only", true);
    private static final Object[] DATE_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(Date.class),
                    JDBCValueMappings.getInstance().getValueWriter(Date.class)};
    private static final Object[] INT_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(int.class), JDBCValueMappings.getInstance().getValueWriter(int.class)};
    private static final Object[] STRING_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(String.class),
                    JDBCValueMappings.getInstance().getValueWriter(String.class)};
    private static final Object[] LONG_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(long.class), JDBCValueMappings.getInstance().getValueWriter(long.class)};
    private static final Object[] PK_RW = new Object[] {JDBCValueMappings.getInstance().getValueReader(PK.class), JDBCValueMappings.getInstance().getValueWriter(PK.class)};
    private int ejbCreateNestedCounter = 0;
    private boolean beforeCreate = false;
    private GenericLinkBMPEntityState entityState = null;
    private boolean localInvalidationAdded = false;


    private ItemDeployment getDeployment()
    {
        return getEntityContext().getItemDeployment();
    }


    private PersistencePool getPersistencePool()
    {
        return getEntityContext().getPersistencePool();
    }


    private final String getDumptable()
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
                log.debug("ejbCreateNestedCounter is " + this.ejbCreateNestedCounter);
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


    private final String getID()
    {
        return Integer.toHexString(System.identityHashCode(this));
    }


    private final GenericLinkBMPEntityState getEntityState()
    {
        if(this.entityState == null)
        {
            throw new HJMPException("no entity state available for " + this.entityContext.getPK());
        }
        return this.entityState;
    }


    private GenericLinkBMPBean getPreviousState()
    {
        GenericLinkBMPBean result = (GenericLinkBMPBean)getPersistencePool().createEntityInstance(getOwnJNDIName(), getPK());
        result.ejbLoad();
        return result;
    }


    private static AdditionalInvalidationData getAdditionalInvalidationData(GenericLinkBMPBean linkBean)
    {
        PK typePk = linkBean.getTypePkString();
        RelationAttributes attributesToExtract = RelationsCache.getDefaultInstance().getSingleCacheableAttributes(TypeId.fromTypePk(typePk));
        if(!attributesToExtract.containsAnyAttribute())
        {
            return null;
        }
        return fillBuilder(linkBean, AdditionalInvalidationData.builder()).build();
    }


    private static AdditionalInvalidationData getAdditionalInvalidationData(GenericLinkBMPBean linkBean1, GenericLinkBMPBean linkBean2)
    {
        RelationAttributes attributesToExtract1 = RelationsCache.getDefaultInstance().getSingleCacheableAttributes(TypeId.fromTypePk(linkBean1.getTypePkString()));
        RelationAttributes attributesToExtract2 = RelationsCache.getDefaultInstance().getSingleCacheableAttributes(TypeId.fromTypePk(linkBean2.getTypePkString()));
        if(!attributesToExtract1.containsAnyAttribute() && !attributesToExtract2.containsAnyAttribute())
        {
            return null;
        }
        AdditionalInvalidationData.Builder builder = AdditionalInvalidationData.builder();
        if(attributesToExtract1.containsAnyAttribute())
        {
            fillBuilder(linkBean1, builder);
        }
        if(attributesToExtract2.containsAnyAttribute())
        {
            fillBuilder(linkBean2, builder);
        }
        return builder.build();
    }


    private static AdditionalInvalidationData.Builder fillBuilder(GenericLinkBMPBean linkBean, AdditionalInvalidationData.Builder builder)
    {
        builder.addForeignKey(Item.TYPE, linkBean.getTypePkString());
        PK srcPk = linkBean.getSourcePKInternal();
        if(srcPk != null)
        {
            builder.addForeignKey("source", srcPk);
        }
        PK tgtPk = linkBean.getTargetPKInternal();
        if(tgtPk != null)
        {
            builder.addForeignKey("target", tgtPk);
        }
        return builder;
    }


    private final void invalidate(boolean success, int type, AdditionalInvalidationData data)
    {
        Transaction tx = Transaction.current();
        PK thePK = getEntityState().getPK();
        int invType = type;
        Object[] key = (data == null) ? InvalidationKey.entityArrayKey(thePK) : InvalidationKey.entityArrayKey(thePK, data);
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


    private final void setEntityState(GenericLinkBMPEntityState newEntityState)
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
            Object[] key = {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, PlatformStringUtils.valueOf(getDeployment().getTypeCode()), this.entityContext.getPK()};
            Transaction.current().addToDelayedRollbackLocalInvalidations(key,
                            remove ? 2 : 1, 3);
        }
    }


    private final void loadData()
    {
        PK pk = this.entityContext.getPK();
        GenericLinkBMPEntityState state = GenericLinkBMPEntityStateCacheUnit.getInstance(getPersistencePool().getCache(), getDeployment(), pk).getEntityState();
        if(LOG)
        {
            log.debug("EJB loading " + pk + " to " + getID() + ": " + state.toDetailedString());
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
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
            String errorMsg = "";
            errorMsg = "Entity not found (" + ((name != null && name.length() > 0) ? ("name = '" + name + "' ") : "") + ((typeCode != 0) ? ("type code = '" + typeCode + "' ") : "") + ((dbTable != null && dbTable.length() > 0) ? ("db table = '" + dbTable + "'") : "") + ")";
            throw new YNoSuchEntityException(errorMsg, pk);
        }
        setEntityState(state);
        setNeedsStoring(false);
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
                AdditionalInvalidationData data = getAdditionalInvalidationData(getPreviousState(), this);
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
                    setEntityState(getEntityState().storeChanges(prc, infoMap));
                    setNeedsStoring(false);
                    success = true;
                }
                finally
                {
                    invalidate(success, 1, data);
                    audit.finish(success);
                }
            }
            else
            {
                setNeedsStoring(false);
            }
        }
    }


    public boolean isBeforeCreate()
    {
        return this.beforeCreate;
    }


    public PK ejbCreate(String quali, PK scrPK, PK tgtPK, int seqNr, int revSeqNr) throws EJBInvalidParameterException
    {
        try
        {
            this.ejbCreateNestedCounter++;
            if(this.ejbCreateNestedCounter == 1)
            {
                setEntityState(new GenericLinkBMPEntityState(getPersistencePool(), getDeployment()));
            }
            else if(this.entityState == null)
            {
                throw new HJMPException("no entity state in nested ejbCreate");
            }
            this.beforeCreate = true;
            super.ejbCreate(quali, scrPK, tgtPK, seqNr, revSeqNr);
            if(this.ejbCreateNestedCounter == 1)
            {
                boolean success = false;
                AdditionalInvalidationData additionalInvalidationData = getAdditionalInvalidationData(this);
                AuditableOperation audit = AuditableOperations.aboutToExecute(new Operation[] {Operation.create(getPkString(), getTypePkString())});
                try
                {
                    if(LOG)
                    {
                        log.debug("EJB creating " + getPkString() + " in " + getID());
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
                    invalidate(success, 4, additionalInvalidationData);
                    this.beforeCreate = false;
                    audit.finish(success);
                }
            }
            else if(LOG)
            {
                log.debug("nested ejbCreate in " + getID());
            }
            return getPkString();
        }
        finally
        {
            this.ejbCreateNestedCounter--;
        }
    }


    public LinkRemote create(String quali, PK scrPK, PK tgtPK, int seqNr, int revSeqNr) throws EJBInvalidParameterException
    {
        throw new RuntimeException("never called!");
    }


    public void ejbPostCreate(String param0, PK param2, PK param3, int param4, int param5)
    {
        super.ejbPostCreate(param0, param2, param3, param4, param5);
        if(needsStoring())
        {
            EJBPropertyRowCache prc = getModifiedUnlocalizedPropertyCache();
            TypeInfoMap infoMap = (prc != null) ? getTypeInfoMap() : null;
            setEntityState(getEntityState().storeChanges(prc, infoMap));
        }
    }


    public void ejbRemove()
    {
        loadData();
        boolean success = false;
        AdditionalInvalidationData additionalInvalidationData = getAdditionalInvalidationData(this);
        AuditableOperation audit = AuditableOperations.aboutToExecute(new Operation[] {Operation.delete(getPkString(), getTypePkString())});
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
        }
        finally
        {
            invalidate(success, 2, additionalInvalidationData);
            audit.finish(success);
        }
        clearFields();
    }


    public void ejbHomeLoadItemData(ResultSet rs)
    {
        throw new HJMPException("not supported");
    }


    private static final Collection handleResult(PersistencePool pool, GenericLinkBMPFinderResult finderResult, ResultSet rs)
    {
        try
        {
            if(rs.next())
            {
                Collection<PK> result = new ArrayList();
                while(true)
                {
                    result.add(handleResultRow(pool, finderResult, rs));
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


    private static final PK handleResultRow(PersistencePool pool, GenericLinkBMPFinderResult finderResult, ResultSet rs)
    {
        try
        {
            PK pk = (PK)((JDBCValueMappings.ValueReader)PK_RW[0]).getValue(rs, "PK");
            Preconditions.checkArgument(
                            GenericBMPBean.checkTypeCodes(Collections.singleton(pk), finderResult.getDeployment().getTypeCode()));
            (new GenericLinkBMPEntityStateCacheUnit(pool.getCache(), finderResult.getDeployment(), pk))
                            .hintEntityState(new GenericLinkBMPEntityState(pool, finderResult.getDeployment(), rs));
            return pk;
        }
        catch(SQLException e)
        {
            throw new HJMPException(e);
        }
    }


    public PK getPkString()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        PK result = getEntityState().getPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PkString for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setPkString(PK newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
            log.debug("setting PkString for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getCreationTimestampInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        Date result = getEntityState().getCreationTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting CreationTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setCreationTimestampInternal(Date newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            log.debug("setting CreationTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public Date getModifiedTimestampInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        Date result = getEntityState().getModifiedTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setModifiedTimestampInternal(Date newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            log.debug("setting ModifiedTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getACLTimestampInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        long result = getEntityState().getACLTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ACLTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setACLTimestampInternal(long newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
            log.debug("setting ACLTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getTypePkString()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        PK result = getEntityState().getTypePkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting TypePkString for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setTypePkString(PK newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
            log.debug("setting TypePkString for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getOwnerPkString()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        PK result = getEntityState().getOwnerPkString();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting OwnerPkString for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setOwnerPkString(PK newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
            log.debug("setting OwnerPkString for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public long getPropertyTimestampInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        long result = getEntityState().getPropertyTimestampInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting PropertyTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setPropertyTimestampInternal(long newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
            log.debug("setting PropertyTimestampInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getSourcePKInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        PK result = getEntityState().getSourcePKInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting SourcePKInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setSourcePKInternal(PK newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getSourcePKInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setSourcePKInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setSourcePKInternal(newvalue);
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
            log.debug("setting SourcePKInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK getTargetPKInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        PK result = getEntityState().getTargetPKInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting TargetPKInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " sid=" + entityState
                            .getID() + ": " + result);
        }
        return result;
    }


    public void setTargetPKInternal(PK newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
        {
            PK oldvalue = oldEntityState.getTargetPKInternal();
            if(oldvalue != newvalue && (oldvalue == null || !oldvalue.equals(newvalue)))
            {
                newEntityState = oldEntityState.setTargetPKInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setTargetPKInternal(newvalue);
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
            log.debug("setting TargetPKInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" + getID() + " " + sidString + ": " + newvalue);
        }
    }


    public String getQualifierInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        String result = getEntityState().getQualifierInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting QualifierInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setQualifierInternal(String newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
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
                sidString = "oldsid=" + oldEntityState.getID();
                if(newEntityState != null)
                {
                    sidString = sidString + " newsid=" + sidString;
                }
            }
            log.debug("setting QualifierInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public int getReverseSequenceNumberInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        int result = getEntityState().getReverseSequenceNumberInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting ReverseSequenceNumberInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setReverseSequenceNumberInternal(int newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
        {
            int oldvalue = oldEntityState.getReverseSequenceNumberInternal();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setReverseSequenceNumberInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setReverseSequenceNumberInternal(newvalue);
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
            log.debug("setting ReverseSequenceNumberInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public int getSequenceNumberInternal()
    {
        GenericLinkBMPEntityState entityState = getEntityState();
        int result = getEntityState().getSequenceNumberInternal();
        if(LOG_ATTRIBUTEACCESS)
        {
            log.debug("getting SequenceNumberInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " sid=" + entityState.getID() + ": " + result);
        }
        return result;
    }


    public void setSequenceNumberInternal(int newvalue)
    {
        GenericLinkBMPEntityState oldEntityState = getEntityState();
        GenericLinkBMPEntityState newEntityState = null;
        if(this.CHECK_VALUE_DIFFERENCES)
        {
            int oldvalue = oldEntityState.getSequenceNumberInternal();
            if(oldvalue != newvalue)
            {
                newEntityState = oldEntityState.setSequenceNumberInternal(newvalue);
                setEntityState(newEntityState);
            }
        }
        else
        {
            newEntityState = oldEntityState.setSequenceNumberInternal(newvalue);
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
            log.debug("setting SequenceNumberInternal for " + this.entityContext.getPK() + " in GenericLinkBMPBean eid=" +
                            getID() + " " + sidString + ": " + newvalue);
        }
    }


    public PK ejbFindByPrimaryKey(PK pkValue) throws YObjectNotFoundException
    {
        if(LOG)
        {
            log.debug("GenericLinkBMPBean ejbFindByPrimaryKey " + pkValue);
        }
        GenericLinkBMPEntityState cacheEntry = GenericLinkBMPEntityStateCacheUnit.getInstance(getPersistencePool().getCache(), getDeployment(), pkValue).getEntityState();
        if(cacheEntry == null)
        {
            if(LOG)
            {
                log.debug("'" + pkValue + "'  not found");
            }
            throw new YObjectNotFoundException("'" + pkValue + "'  not found");
        }
        if(LOG)
        {
            log.debug("  found entry");
        }
        return cacheEntry.getPK();
    }


    public LinkRemote findByPrimaryKey(PK pk) throws YObjectNotFoundException, YFinderException
    {
        throw new HJMPException("illegal call to findByPrimaryKey!");
    }


    public Collection ejbFindByType(PK typePK) throws YFinderException
    {
        try
        {
            if(LOG)
            {
                log.debug("GenericLinkBMPBean ejbFindByType");
            }
            if(LOG)
            {
                log.debug("  0->" + typePK);
            }
            Object resultObject = (new FindByType1FinderResult(getPersistencePool(), getDeployment(), typePK)).getFinderResult();
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


    public Collection findByType(PK typePK) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByType(typePK),
                        getEntityContext().getPersistencePool()
                                        .getTenant()
                                        .getSystemEJB());
    }


    public Collection ejbFindChangedAfter(Date param0) throws YFinderException
    {
        throw new HJMPException("findChangedAfter() not supported in generic deployments");
    }


    public Collection findChangedAfter(Date param0) throws YFinderException
    {
        throw new HJMPException("findChangedAfter() not supported in generic deployments");
    }


    public Collection ejbFindAll() throws YFinderException
    {
        throw new HJMPException("findAll() not supported in generic deployments");
    }


    public Collection findAll() throws YFinderException
    {
        throw new HJMPException("findAll() not supported in generic deployments");
    }


    protected String getItemTableNameImpl()
    {
        throw new HJMPException("no supported within generic link bean");
    }


    public String getOwnJNDIName()
    {
        return getDeployment().getName();
    }


    protected final int typeCode()
    {
        return getDeployment().getTypeCode();
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


    public Collection findByPKList(Collection<PK> pks) throws YFinderException
    {
        return (Collection)EJBTools.convertEntityFinderResult(ejbFindByPKList(pks),
                        getEntityContext().getPersistencePool()
                                        .getTenant()
                                        .getSystemEJB());
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
}
