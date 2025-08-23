package de.hybris.platform.catalog.jalo.synchronization;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.catalog.SynchronizationPersistenceAdapter;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.tx.RollbackOnlyException;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.PartOfItemAlreadyAssignedToTheParentException;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

public class ItemCopyCreator
{
    private static final String SYNCHRONIZATION_ITEMCOPYCREATOR_STACKTRACES = "synchronization.itemcopycreator.stacktraces";
    private static final Logger LOG = Logger.getLogger(ItemCopyCreator.class);
    private final TypeCopyDescriptor tcd;
    private final GenericCatalogCopyContext context;
    private final ItemCopyCreator parent;
    private final Map<String, Boolean> whitelist;
    private final Map<String, Boolean> blacklist;
    private final Map<String, Object> presetedValues;
    private final ComposedType _targetType;
    private Set<AttributeCopyCreator> initialAttributeCopyCreators;
    private Set<AttributeCopyCreator> partOfAttributeCopyCreators;
    private Set<AttributeCopyCreator> otherAttributeCopyCreators;
    private final ImmutableSet.Builder<AttributeCopyCreator> failedAttributeCopyCreators = new ImmutableSet.Builder();
    private final boolean update;
    private long timeTotal = 0L;
    private long timeRead = 0L;
    private long timeCreate = 0L;
    private long timeSetAttributes = 0L;
    private Map<String, Object> storedValues;
    private final Item _sourceItem;
    private Item _targetItem;
    private boolean encounteredOptimisticLockException;
    private Exception optimisticLockingException;
    private boolean transactionRolledBack;
    private boolean plainItemProcessedSuccessfully;
    private boolean rootItemCreatedSuccessfullyAfterMainTxRolledBack;
    private AfterItemCopiedCallback afterItemCopiedCallback = null;


    protected ItemCopyCreator(GenericCatalogCopyContext genericCatalogCopyContext, ItemCopyCreator parent, Item source, Item target, Collection<String> blackList, Collection<String> whiteList, Map<String, Object> presetedValues) throws IllegalArgumentException
    {
        if(genericCatalogCopyContext == null)
        {
            throw new NullPointerException("copy context cannot be null");
        }
        if(source == null)
        {
            throw new NullPointerException("source value cannot be null");
        }
        this.context = genericCatalogCopyContext;
        this.parent = parent;
        this._sourceItem = source;
        this._targetType = source.getComposedType();
        this._targetItem = target;
        this.update = (target != null);
        this.tcd = genericCatalogCopyContext.getTypeCopyDescriptor(getTargetType());
        if(presetedValues != null)
        {
            this.presetedValues = (Map<String, Object>)new CaseInsensitiveMap(presetedValues);
        }
        else
        {
            this.presetedValues = Collections.EMPTY_MAP;
        }
        if(genericCatalogCopyContext.isDebugEnabled())
        {
            if(getParent() == null || getParent().getCopyContext() == null)
            {
                genericCatalogCopyContext.debug("preseted values = " + ((getParent() == null) ? "<PARENT>" : "<COPY CONTEXT>") + " was <NULL>");
            }
            else
            {
                genericCatalogCopyContext
                                .debug("preseted values = " + getParent().getCopyContext().safeToString(this.presetedValues));
            }
        }
        this.blacklist = calculateBlackList(blackList, presetedValues);
        if(genericCatalogCopyContext.isDebugEnabled())
        {
            genericCatalogCopyContext.debug("blacklist = " + this.blacklist);
        }
        this.whitelist = calculateWhiteList(whiteList, presetedValues);
        if(genericCatalogCopyContext.isDebugEnabled())
        {
            genericCatalogCopyContext.debug("whitelist = " + this.whitelist);
        }
    }


    private Map<String, Boolean> calculateWhiteList(Collection<String> whiteList, Map<String, Object> givenPresetedValues)
    {
        CaseInsensitiveMap<String, Boolean> caseInsensitiveMap;
        Map<String, Boolean> tmp = null;
        if(whiteList != null)
        {
            for(String qualifier : whiteList)
            {
                if(tmp == null)
                {
                    caseInsensitiveMap = new CaseInsensitiveMap();
                }
                caseInsensitiveMap.put(qualifier, Boolean.TRUE);
            }
        }
        if(!givenPresetedValues.isEmpty())
        {
            for(Map.Entry<String, Object> e : givenPresetedValues.entrySet())
            {
                if(caseInsensitiveMap == null)
                {
                    caseInsensitiveMap = new CaseInsensitiveMap();
                }
                caseInsensitiveMap.put(e.getKey(), Boolean.TRUE);
            }
        }
        return (caseInsensitiveMap != null) ? (Map<String, Boolean>)caseInsensitiveMap : Collections.EMPTY_MAP;
    }


    private Map<String, Boolean> calculateBlackList(Collection<String> blackList, Map<String, Object> givenPresetedValues)
    {
        CaseInsensitiveMap<String, Boolean> caseInsensitiveMap;
        Map<String, Boolean> tmpbl = null;
        if(blackList != null)
        {
            for(String qualifier : blackList)
            {
                if(!givenPresetedValues.containsKey(qualifier))
                {
                    if(tmpbl == null)
                    {
                        caseInsensitiveMap = new CaseInsensitiveMap();
                    }
                    caseInsensitiveMap.put(qualifier, Boolean.TRUE);
                }
            }
        }
        return (caseInsensitiveMap != null) ? (Map<String, Boolean>)caseInsensitiveMap : Collections.EMPTY_MAP;
    }


    protected <T> T copy()
    {
        boolean newTransaction = false;
        try
        {
            if(!Transaction.current().isRunning())
            {
                newTransaction = true;
                getPersistenceAdapter().resetUnitOfWork();
                SessionContext localSessionContext = JaloSession.getCurrentSession().createLocalSessionContext();
                try
                {
                    disableNestedTransactions(localSessionContext);
                    doCopyInTx();
                }
                catch(Exception e)
                {
                    if(this.plainItemProcessedSuccessfully)
                    {
                        getPersistenceAdapter().resetUnitOfWork();
                        resetAfterRollback();
                        createRootItemInTx();
                    }
                    throw e;
                }
                finally
                {
                    clearNestedTransactionsSettings();
                }
            }
            else
            {
                newTransaction = false;
                doCopy();
                notifyCallback();
            }
        }
        catch(RuntimeException e)
        {
            markAsTransactionRolledBack(newTransaction);
            if(CatalogSyncUtils.shouldRetryAfter(e))
            {
                this.encounteredOptimisticLockException = true;
                this.optimisticLockingException = e;
                rethrowIfHasParent(e);
            }
            logException(e);
        }
        catch(Exception e)
        {
            markAsTransactionRolledBack(newTransaction);
            throw new RuntimeException(e);
        }
        return (T)this._targetItem;
    }


    protected void notifyCallback()
    {
        if(this.afterItemCopiedCallback != null)
        {
            this.afterItemCopiedCallback.afterItemCopied(this);
        }
    }


    public AfterItemCopiedCallback getAfterItemCopiedCallback()
    {
        return this.afterItemCopiedCallback;
    }


    public void setAfterItemCopiedCallback(AfterItemCopiedCallback callback)
    {
        this.afterItemCopiedCallback = callback;
    }


    private void resetAfterRollback()
    {
        for(AttributeCopyCreator acc : this.initialAttributeCopyCreators)
        {
            acc.reset();
        }
        for(AttributeCopyCreator acc : this.partOfAttributeCopyCreators)
        {
            acc.reset();
        }
        for(AttributeCopyCreator acc : this.otherAttributeCopyCreators)
        {
            acc.reset();
        }
        if(!this.update)
        {
            this._targetItem = null;
        }
        resetStoredValues();
    }


    private void clearNestedTransactionsSettings()
    {
        JaloSession.getCurrentSession().removeLocalSessionContext();
        getPersistenceAdapter().clearTransactionsSettings();
    }


    private void disableNestedTransactions(SessionContext localSessionContext)
    {
        localSessionContext.setAttribute("transaction_in_create_disabled", Boolean.FALSE);
        localSessionContext.setAttribute("all.attributes.use.ta", Boolean.FALSE);
        getPersistenceAdapter().disableTransactions();
    }


    private void rethrowIfHasParent(RuntimeException e)
    {
        if(this.parent != null)
        {
            throw e;
        }
    }


    private void logException(RuntimeException e)
    {
        if(getCopyContext().isInfoEnabled())
        {
            logInfo(e);
        }
        else if(getCopyContext().isDebugEnabled())
        {
            logDebug(e);
        }
    }


    private void logInfo(RuntimeException e)
    {
        if(Config.getBoolean("synchronization.itemcopycreator.stacktraces", false))
        {
            getCopyContext().info(e.getMessage(), e);
        }
        else
        {
            getCopyContext().info(e.getMessage());
        }
    }


    private void logDebug(RuntimeException e)
    {
        if(Config.getBoolean("synchronization.itemcopycreator.stacktraces", false))
        {
            getCopyContext().debug(e.getMessage(), e);
        }
        else
        {
            getCopyContext().debug(e.getMessage());
        }
    }


    private Transaction.TransactionAwareExecution createResetOnRollbackAction(boolean full)
    {
        return (Transaction.TransactionAwareExecution)new Object(this);
    }


    private void doCopyInTx() throws Exception
    {
        Transaction tx = Transaction.current();
        tx.execute((TransactionBody)new Object(this, tx));
    }


    private void createRootItemInTx() throws Exception
    {
        Transaction tx = Transaction.current();
        tx.execute((TransactionBody)new Object(this, tx));
    }


    private void doCopy()
    {
        if(getCopyContext().isDebugEnabled())
        {
            getCopyContext().debug((isUpdate() ? "updating" : "copying") + " item " + (isUpdate() ? "updating" : "copying"));
        }
        long startTime = System.currentTimeMillis();
        readInitialAttributes();
        readNonInitialAttributes();
        this.timeRead = System.currentTimeMillis() - startTime;
        createOrUpdate();
        this.plainItemProcessedSuccessfully = true;
        setPartOfReferences();
        setOtherReferences();
        this.timeTotal = System.currentTimeMillis() - startTime;
        if(getCopyContext().isDebugEnabled())
        {
            getCopyContext().debug((
                            isUpdate() ? "updated" : "copied") + " item " + (isUpdate() ? "updated" : "copied") + " to " + getSourceItem().getPK() + "( read=" + (
                            (getTargetItem() != null) ? (String)getTargetType().getPK() : "null") + "ms, create=" + this.timeRead + "ms, set attributes=" + this.timeCreate + "ms)" + this.timeSetAttributes);
        }
    }


    private void markAsTransactionRolledBack(boolean newTransaction)
    {
        if(!newTransaction)
        {
            return;
        }
        this.transactionRolledBack = true;
        if(!isUpdate() && !this.rootItemCreatedSuccessfullyAfterMainTxRolledBack)
        {
            this._targetItem = null;
        }
    }


    private void notifyAfterCreateOrUpdate(Map<String, Object> attributes)
    {
        if(Transaction.current().isRollbackOnly())
        {
            return;
        }
        addToStoredValues(attributes);
        getCopyContext().afterCreate(this, this._targetItem);
    }


    private void createOrUpdate()
    {
        Map<String, Object> initOrUpdateValues = null;
        long startCreateTime = System.currentTimeMillis();
        if(isUpdate())
        {
            setTypeIfNeeded(this._targetItem);
            initOrUpdateValues = getUpdateInitialAttributes(this.initialAttributeCopyCreators);
            updateEntity(this._targetItem, initOrUpdateValues);
        }
        else
        {
            initOrUpdateValues = getCreationInitialAttributes(this.initialAttributeCopyCreators);
            this._targetItem = createEntity(this._targetType, initOrUpdateValues);
        }
        this.timeCreate = System.currentTimeMillis() - startCreateTime;
        notifyAfterCreateOrUpdate(initOrUpdateValues);
    }


    @Deprecated(since = "5.0.1", forRemoval = false)
    protected Item copy(GenericCatalogCopyContext genericCatalogCopyContext)
    {
        return copy();
    }


    public Map<String, Object> getStoredValues()
    {
        return this.storedValues;
    }


    public Map<String, Object>[] getModifiedValues()
    {
        if(!isUpdate() || this.storedValues == null)
        {
            return null;
        }
        Map[] ret = {(Map)new CaseInsensitiveMap(), (Map)new CaseInsensitiveMap()};
        checkAndAddSavedValues(ret, getInitialAttributeCreators());
        checkAndAddSavedValues(ret, getPartOfAttributeCreators());
        checkAndAddSavedValues(ret, getOtherAttributeCreators());
        return (Map<String, Object>[])ret;
    }


    private void checkAndAddSavedValues(Map[] retmap, Collection<AttributeCopyCreator> attdescColl)
    {
        for(AttributeCopyCreator acc : attdescColl)
        {
            if(!acc.isPending())
            {
                Object sourceValue = acc.getSourceValue();
                Object targetValue = acc.getCopiedValue();
                String qualifier = acc.getDescriptor().getQualifier();
                if(sourceValue != targetValue && (sourceValue == null || !sourceValue.equals(targetValue)))
                {
                    retmap[0].put(qualifier, sourceValue);
                    retmap[1].put(qualifier, targetValue);
                }
            }
        }
    }


    public void resetStoredValues()
    {
        this.storedValues = null;
    }


    protected void addToStoredValues(Map<String, Object> attributes)
    {
        if(MapUtils.isEmpty(attributes) && getCopyContext().isSavePrevousValues())
        {
            if(this.storedValues == null)
            {
                this.storedValues = (Map<String, Object>)new CaseInsensitiveMap();
            }
            this.storedValues.putAll(attributes);
        }
    }


    protected GenericCatalogCopyContext getCopyContext()
    {
        return this.context;
    }


    private Map<String, Object> getCreationInitialAttributes(Collection<AttributeCopyCreator> initial) throws MissingInitialAttributes
    {
        return getInitialAttributes(initial, false);
    }


    private Map<String, Object> getUpdateInitialAttributes(Collection<AttributeCopyCreator> initial)
    {
        return getInitialAttributes(initial, true);
    }


    private Map<String, Object> getInitialAttributes(Collection<AttributeCopyCreator> initial, boolean forUpdate) throws MissingInitialAttributes
    {
        Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
        boolean cannotCreate = false;
        for(AttributeCopyCreator acc : initial)
        {
            acc.copy();
            if(!acc.isPending())
            {
                Object value = acc.getCopiedValue();
                if(forUpdate || !isEmpty(value, acc.getDescriptor().isLocalized()))
                {
                    itemAttributeMap.put(acc.getDescriptor().getQualifier(), value);
                }
                continue;
            }
            if(!forUpdate && (getCopyContext().isRequiredForCreation(acc.getDescriptor().getRealAttributeDescriptor()) || acc
                            .getDescriptor().isInitialOnly()))
            {
                cannotCreate = true;
            }
        }
        if(cannotCreate)
        {
            throw new MissingInitialAttributes(this, "cannot create item due to pending attributes (values " +
                            getCopyContext().valuesToString(itemAttributeMap) + ")", this._sourceItem);
        }
        return (Map<String, Object>)itemAttributeMap;
    }


    private Item createEntity(ComposedType expectedType, Map<String, Object> initialAttributeValues)
    {
        Item targetEntity = null;
        try
        {
            if(getCopyContext().isDebugEnabled())
            {
                getCopyContext().debug("creating new item (values " +
                                getCopyContext().valuesToString(initialAttributeValues) + ")");
            }
            targetEntity = (Item)getPersistenceAdapter().create(expectedType, initialAttributeValues);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1921);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "An exception occured during synchronization for item: " + this._sourceItem.getPK() + " of type: " + this._sourceItem
                            .getComposedType().getName(), 1923);
        }
        return targetEntity;
    }


    private void updateEntity(Item existing, Map<String, Object> initialValues)
    {
        Item ret = existing;
        try
        {
            if(getCopyContext().isDebugEnabled())
            {
                getCopyContext().debug("updating target item " + this._targetType
                                .getPK() + " (values " + getCopyContext().valuesToString(initialValues) + ")");
            }
            getPersistenceAdapter().update(ret, initialValues);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1921);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "An exception occured during synchronization for item: " + this._sourceItem.getPK() + " of type: " + this._sourceItem
                            .getComposedType().getName(), 1923);
        }
    }


    private SynchronizationPersistenceAdapter getPersistenceAdapter()
    {
        return getCopyContext().getPersistenceAdapter();
    }


    protected void setPartOfReferences()
    {
        long time1 = System.currentTimeMillis();
        setReferenceAttributesWaitingOnDelay(getPartOfAttributeCreators());
        long time3 = System.currentTimeMillis();
        this.timeSetAttributes += time3 - time1;
    }


    protected void setOtherReferences()
    {
        long time1 = System.currentTimeMillis();
        setReferenceAttributesWaitingOnDelay(getOtherAttributeCreators());
        long time3 = System.currentTimeMillis();
        this.timeSetAttributes += time3 - time1;
    }


    protected void setReferenceAttributesWaitingOnDelay(Set<AttributeCopyCreator> attributeCopyCreators)
    {
        ImmutableSet.Builder<AttributeCopyCreator> delayedBuilder = new ImmutableSet.Builder();
        for(AttributeCopyCreator acc : attributeCopyCreators)
        {
            try
            {
                acc.copy();
                abortIfTxIsRollbackOnly();
            }
            catch(RollbackOnlyException roe)
            {
                throw roe;
            }
            catch(Exception e)
            {
                if(CatalogSyncUtils.shouldRetryAfter(e))
                {
                    throw e;
                }
                this.failedAttributeCopyCreators.add(acc);
                getCopyContext().error(buildErrorMessage(acc.getDescriptor().getQualifier(), e));
                continue;
            }
            if(!acc.isPending() || acc.canBeTranslatedPartially())
            {
                if(setReferenceAttribute(acc) == ReferenceAttributeResult.BLOCKED)
                {
                    delayedBuilder.add(acc);
                }
            }
        }
        ImmutableSet immutableSet = delayedBuilder.build();
        while(CollectionUtils.isNotEmpty((Collection)immutableSet))
        {
            ImmutableSet.Builder<AttributeCopyCreator> newDelayedBuilder = new ImmutableSet.Builder();
            for(AttributeCopyCreator acc : immutableSet)
            {
                if(setReferenceAttribute(acc) == ReferenceAttributeResult.BLOCKED)
                {
                    newDelayedBuilder.add(acc);
                    Thread.yield();
                }
            }
            immutableSet = newDelayedBuilder.build();
        }
    }


    private void abortIfTxIsRollbackOnly()
    {
        if(Transaction.current().isRollbackOnly())
        {
            throw new RollbackOnlyException("Aborting processing of attributes, because current transaction has been marked as rollback-only");
        }
    }


    public boolean encounteredOptimisticLockException()
    {
        return this.encounteredOptimisticLockException;
    }


    public Exception getOptimisticLockException()
    {
        return this.optimisticLockingException;
    }


    public boolean isTransactionRolledBack()
    {
        return this.transactionRolledBack;
    }


    protected ReferenceAttributeResult setReferenceAttribute(AttributeCopyCreator acc)
    {
        Object value = acc.getCopiedValue();
        if(isUpdate() || !isEmpty(value, acc.getDescriptor().isLocalized()))
        {
            String qualifier = acc.getDescriptor().getQualifier();
            boolean isPartOf = acc.getDescriptor().isPartOf();
            if(getCopyContext().isDebugEnabled())
            {
                getCopyContext().debug("setting " + (isPartOf ? "partOf" : "reference") + " attribute " + qualifier + " of " +
                                getTargetItem().getPK() + " to " + getCopyContext().safeToString(value));
            }
            try
            {
                getPersistenceAdapter().update(getTargetItem(), Maps.immutableEntry(qualifier, value));
                addToStoredValues(Collections.singletonMap(qualifier, value));
            }
            catch(Exception e)
            {
                if(isPartOf && Utilities.getRootCauseOfType(e, PartOfItemAlreadyAssignedToTheParentException.class) != null)
                {
                    throw new ConcurrentModificationDuringSyncException(e);
                }
                if(CatalogSyncUtils.shouldRetryAfter(e))
                {
                    throw e;
                }
                this.failedAttributeCopyCreators.add(acc);
                getCopyContext().error(buildErrorMessage(qualifier, e));
                return ReferenceAttributeResult.ERROR;
            }
        }
        return ReferenceAttributeResult.OK;
    }


    private String buildErrorMessage(String qualifier, Exception e)
    {
        StringBuilder errorSb = new StringBuilder();
        errorSb.append("error setting partOf attribute ").append(qualifier).append(" : ").append(e.getMessage()).append("\n")
                        .append(Utilities.getStackTraceAsString(e)).append("\n");
        return errorSb.toString();
    }


    private void setTypeIfNeeded(Item updatedItem)
    {
        if(!this._targetType.getPK().equals(updatedItem.getComposedType().getPK()))
        {
            if(getCopyContext().isDebugEnabled())
            {
                getCopyContext().debug("updating target item type from " + updatedItem
                                .getComposedType()
                                .getCode() + " to " + this._targetType.getCode() + " )");
            }
            updatedItem.setComposedType(this._targetType);
        }
    }


    public final Item getSourceItem()
    {
        return this._sourceItem;
    }


    public final Item getTargetItem()
    {
        return this._targetItem;
    }


    public final boolean isUpdate()
    {
        return this.update;
    }


    public final boolean isPartOf()
    {
        return (getParent() != null);
    }


    public final ComposedType getTargetType()
    {
        return this._targetType;
    }


    protected ItemCopyCreator getParent()
    {
        return this.parent;
    }


    protected final ItemCopyCreator getEnclosingCreatorFor(Item item)
    {
        if(!isPartOf())
        {
            return null;
        }
        for(ItemCopyCreator p = getParent(); p != null; p = p.getParent())
        {
            if(p.getSourceItem().equals(item))
            {
                return p;
            }
        }
        return null;
    }


    protected void readNonInitialAttributes()
    {
        try
        {
            Set<AttributeCopyDescriptor> partOf = this.tcd.getPartOf(this.blacklist, this.whitelist);
            Set<AttributeCopyDescriptor> others = this.tcd.getOther(this.blacklist, this.whitelist);
            Set<String> unlocQualifiers = new LinkedHashSet<>(partOf.size() + others.size());
            Set<String> locQualifiers = new LinkedHashSet<>(partOf.size() + others.size());
            for(AttributeCopyDescriptor acd : partOf)
            {
                if(acd.isLocalized())
                {
                    locQualifiers.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
                    continue;
                }
                unlocQualifiers.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            for(AttributeCopyDescriptor acd : others)
            {
                if(acd.isLocalized())
                {
                    locQualifiers.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
                    continue;
                }
                unlocQualifiers.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            if(this.presetedValues != null && !this.presetedValues.isEmpty())
            {
                if(!unlocQualifiers.isEmpty())
                {
                    unlocQualifiers.removeAll(this.presetedValues.keySet());
                }
                if(!locQualifiers.isEmpty())
                {
                    locQualifiers.removeAll(this.presetedValues.keySet());
                }
            }
            Map unlocSourceValues = readValues(unlocQualifiers, false);
            Map locSourceValues = readValues(locQualifiers, true);
            this.partOfAttributeCopyCreators = adjustPresetedValues(partOf, unlocSourceValues, locSourceValues);
            this.otherAttributeCopyCreators = adjustPresetedValues(others, unlocSourceValues, locSourceValues);
        }
        catch(JaloInvalidParameterException e)
        {
            if(getCopyContext().isErrorEnabled())
            {
                getCopyContext().error("error reading non-initial values from " +
                                getSourceItem().getPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
        catch(JaloSecurityException e)
        {
            if(getCopyContext().isErrorEnabled())
            {
                getCopyContext().error("error reading non-initial values from " +
                                getSourceItem().getPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
    }


    private Set<AttributeCopyCreator> adjustPresetedValues(Set<AttributeCopyDescriptor> attributeCopyDescriptors, Map unlocSourceValues, Map locSourceValues)
    {
        ImmutableSet.Builder<AttributeCopyCreator> setBuilder = new ImmutableSet.Builder();
        for(AttributeCopyDescriptor acd : attributeCopyDescriptors)
        {
            String qualifier = acd.getQualifier();
            boolean preset = (this.presetedValues != null && this.presetedValues.containsKey(qualifier));
            setBuilder.add(
                            getCopyContext().createAttributeCopyCreator(this, acd, preset,
                                            preset ? this.presetedValues.get(qualifier) : (acd.isLocalized() ? locSourceValues.get(qualifier) :
                                                            unlocSourceValues.get(qualifier))));
        }
        return (Set<AttributeCopyCreator>)setBuilder.build();
    }


    protected void readInitialAttributes()
    {
        try
        {
            Set<AttributeCopyDescriptor> initial = this.tcd.getInitial(isUpdate(), this.blacklist, this.whitelist);
            Set<String> unlocQualifiers = new LinkedHashSet<>(initial.size());
            Set<String> locQualifiers = new LinkedHashSet<>(initial.size());
            for(AttributeCopyDescriptor acd : initial)
            {
                if(acd.isLocalized())
                {
                    locQualifiers.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
                    continue;
                }
                unlocQualifiers.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            if(this.presetedValues != null && !this.presetedValues.isEmpty())
            {
                if(!unlocQualifiers.isEmpty())
                {
                    unlocQualifiers.removeAll(this.presetedValues.keySet());
                }
                if(!locQualifiers.isEmpty())
                {
                    locQualifiers.removeAll(this.presetedValues.keySet());
                }
            }
            Map unlocSourceValues = readValues(unlocQualifiers, false);
            Map locSourceValues = readValues(locQualifiers, true);
            ImmutableSet.Builder<AttributeCopyCreator> initialAttributeCopyCreators = new ImmutableSet.Builder();
            for(AttributeCopyDescriptor acd : initial)
            {
                String qualifier = acd.getQualifier();
                boolean preset = (this.presetedValues != null && this.presetedValues.containsKey(qualifier));
                initialAttributeCopyCreators.add(
                                getCopyContext().createAttributeCopyCreator(this, acd, preset,
                                                preset ? this.presetedValues.get(qualifier) : (
                                                                acd.isLocalized() ? locSourceValues.get(qualifier) :
                                                                                unlocSourceValues.get(qualifier))));
            }
            this.initialAttributeCopyCreators = (Set<AttributeCopyCreator>)initialAttributeCopyCreators.build();
        }
        catch(JaloInvalidParameterException e)
        {
            if(getCopyContext().isErrorEnabled())
            {
                getCopyContext().error("error reading initial values from " +
                                getSourceItem().getPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
        catch(JaloSecurityException e)
        {
            if(getCopyContext().isErrorEnabled())
            {
                getCopyContext().error("error reading initial values from " +
                                getSourceItem().getPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
    }


    private Map readValues(Set<String> qualifiers, boolean localized) throws JaloInvalidParameterException, JaloSecurityException
    {
        if(localized)
        {
            if(!qualifiers.isEmpty())
            {
                Set<Language> languages = getCopyContext().getTargetLanguages();
                if(CollectionUtils.isNotEmpty(languages))
                {
                    return readLocValues(qualifiers, languages);
                }
                return Collections.EMPTY_MAP;
            }
            return null;
        }
        return qualifiers.isEmpty() ? null : getPersistenceAdapter().read(getSourceItem(), qualifiers);
    }


    private Map readLocValues(Set<String> qualifiers, Set<Language> languages) throws JaloSecurityException
    {
        return qualifiers.isEmpty() ? null : getPersistenceAdapter().readLocalized(getSourceItem(), qualifiers, languages);
    }


    protected final void readSourceValues(Item targetItem)
    {
        long time1 = System.currentTimeMillis();
        try
        {
            Set<AttributeCopyDescriptor> initial = this.tcd.getInitial(isUpdate(), this.blacklist, this.whitelist);
            Set<AttributeCopyDescriptor> partOf = this.tcd.getPartOf(this.blacklist, this.whitelist);
            Set<AttributeCopyDescriptor> others = this.tcd.getOther(this.blacklist, this.whitelist);
            Set<String> permitted = new LinkedHashSet<>(initial.size() + partOf.size() + others.size());
            for(AttributeCopyDescriptor acd : initial)
            {
                permitted.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            for(AttributeCopyDescriptor acd : partOf)
            {
                permitted.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            for(AttributeCopyDescriptor acd : others)
            {
                permitted.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            SessionContext ctx = getCopyContext().getCtx();
            Set<String> qualifiers = new LinkedHashSet<>();
            qualifiers.addAll(permitted);
            if(this.presetedValues != null && !this.presetedValues.isEmpty())
            {
                qualifiers.removeAll(this.presetedValues.keySet());
            }
            Map<String, Object> sourceValues = getSourceItem().getAllAttributes(ctx, qualifiers);
            this.initialAttributeCopyCreators = new LinkedHashSet<>();
            for(AttributeCopyDescriptor acd : initial)
            {
                boolean prest = (this.presetedValues != null && this.presetedValues.containsKey(acd.getQualifier()));
                this.initialAttributeCopyCreators.add(getCopyContext().createAttributeCopyCreator(this, acd, prest,
                                prest ? this.presetedValues.get(acd
                                                .getQualifier()) :
                                                sourceValues.get(acd.getQualifier())));
            }
            this.partOfAttributeCopyCreators = new LinkedHashSet<>();
            for(AttributeCopyDescriptor acd : partOf)
            {
                boolean prest = (this.presetedValues != null && this.presetedValues.containsKey(acd.getQualifier()));
                this.partOfAttributeCopyCreators.add(getCopyContext().createAttributeCopyCreator(this, acd, prest,
                                prest ? this.presetedValues.get(acd
                                                .getQualifier()) :
                                                sourceValues.get(acd.getQualifier())));
            }
            this.otherAttributeCopyCreators = new LinkedHashSet<>();
            for(AttributeCopyDescriptor acd : others)
            {
                boolean prest = (this.presetedValues != null && this.presetedValues.containsKey(acd.getQualifier()));
                this.otherAttributeCopyCreators.add(getCopyContext().createAttributeCopyCreator(this, acd, prest,
                                prest ? this.presetedValues.get(acd
                                                .getQualifier()) :
                                                sourceValues.get(acd.getQualifier())));
            }
        }
        catch(JaloInvalidParameterException e)
        {
            if(getCopyContext().isWarnEnabled())
            {
                getCopyContext().error("error reading values from " +
                                getSourceItem().getPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
        catch(JaloSecurityException e)
        {
            if(getCopyContext().isWarnEnabled())
            {
                getCopyContext().error("error reading values from " +
                                getSourceItem().getPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
        this.timeRead = System.currentTimeMillis() - time1;
    }


    protected boolean isEmpty(Object translatedValue, boolean localized)
    {
        if(translatedValue == null)
        {
            return true;
        }
        if(translatedValue instanceof Collection)
        {
            return ((Collection)translatedValue).isEmpty();
        }
        if(translatedValue instanceof Map)
        {
            if(((Map)translatedValue).isEmpty())
            {
                return true;
            }
            if(localized)
            {
                for(Iterator<Map.Entry> it = ((Map)translatedValue).entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry entry = it.next();
                    if(!isEmpty(entry.getValue(), false))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    protected final boolean hasPartOfItems()
    {
        if(this.partOfAttributeCopyCreators == null)
        {
            throw new IllegalStateException("partOf values not read - cannot call hasPartOfItems() yet");
        }
        for(Iterator<AttributeCopyCreator> it = getPartOfAttributeCreators().iterator(); it.hasNext(); )
        {
            if(((AttributeCopyCreator)it.next()).hasPartOfItems())
            {
                return true;
            }
        }
        return false;
    }


    public final boolean hasPendingAttributes()
    {
        for(AttributeCopyCreator acc : getPartOfAttributeCreators())
        {
            if(acc.isPending() || !acc.isTranslated())
            {
                return true;
            }
        }
        for(AttributeCopyCreator acc : getOtherAttributeCreators())
        {
            if(acc.isPending() || !acc.isTranslated())
            {
                return true;
            }
        }
        for(AttributeCopyCreator acc : getInitialAttributeCreators())
        {
            if(acc.isPending() || !acc.isTranslated())
            {
                return true;
            }
        }
        return false;
    }


    protected final Set<AttributeCopyCreator> getPartOfAttributeCreators()
    {
        return (this.partOfAttributeCopyCreators != null) ? this.partOfAttributeCopyCreators : Collections.EMPTY_SET;
    }


    protected final Set<AttributeCopyCreator> getInitialAttributeCreators()
    {
        return (this.initialAttributeCopyCreators != null) ? this.initialAttributeCopyCreators : Collections.EMPTY_SET;
    }


    protected final Set<AttributeCopyCreator> getOtherAttributeCreators()
    {
        return (this.otherAttributeCopyCreators != null) ? this.otherAttributeCopyCreators : Collections.EMPTY_SET;
    }


    public final Set<AttributeCopyCreator> getPendingAttributes()
    {
        Set<AttributeCopyCreator> ret = new LinkedHashSet<>();
        for(AttributeCopyCreator acc : getPartOfAttributeCreators())
        {
            if(acc.isPending() || !acc.isTranslated())
            {
                ret.add(acc);
            }
        }
        for(AttributeCopyCreator acc : getOtherAttributeCreators())
        {
            if(acc.isPending() || !acc.isTranslated())
            {
                ret.add(acc);
            }
        }
        for(AttributeCopyCreator acc : getInitialAttributeCreators())
        {
            if(acc.isPending() || !acc.isTranslated())
            {
                ret.add(acc);
            }
        }
        return ret;
    }


    public final Set<AttributeCopyCreator> getFailedAttributes()
    {
        return (Set<AttributeCopyCreator>)this.failedAttributeCopyCreators.build();
    }


    public final Set<String> getPendingAttributeQualifiers()
    {
        Set<String> ret = new LinkedHashSet();
        for(AttributeCopyCreator acc : getPendingAttributes())
        {
            ret.add(acc.getDescriptor().getQualifier());
        }
        return ret;
    }


    public String toString()
    {
        return "ICC(" + getSourceItem().getPK() + (isUpdate() ? ("->" + getTargetItem().getPK()) : "") + ")";
    }


    public String getReport()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CopyReport src:").append(getSourceItem().getPK()).append(", target:")
                        .append((getTargetItem() != null) ? getTargetItem().getPK() : "n/a").append("\n");
        stringBuilder.append("\tmode:").append(isUpdate() ? "update" : "create").append("\n");
        stringBuilder.append("\ttime: total:").append(this.timeTotal).append("ms, read:").append(this.timeRead).append("ms, ");
        stringBuilder.append(isUpdate() ? "initial:" : "create:").append(this.timeCreate).append("ms, refs:").append(this.timeSetAttributes)
                        .append("ms \n");
        Set<AttributeCopyCreator> pending = getPendingAttributes();
        Set<AttributeCopyCreator> failed = getFailedAttributes();
        Set<AttributeCopyCreator> initial = getInitialAttributeCreators();
        Set<AttributeCopyCreator> partOf = getPartOfAttributeCreators();
        Set<AttributeCopyCreator> other = getOtherAttributeCreators();
        boolean first = true;
        for(AttributeCopyCreator acc : initial)
        {
            if(failed.contains(acc) || pending.contains(acc))
            {
                continue;
            }
            if(first)
            {
                stringBuilder.append("\twrote initial attributes \n");
                first = false;
            }
            stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" value:")
                            .append(getCopyContext().safeToString(acc.getSourceValue()));
            if(acc.isPreset())
            {
                stringBuilder.append(", preset\n");
                continue;
            }
            if(!acc.isTranslated())
            {
                stringBuilder.append(", NOT YET TRANSLATED!\n");
                continue;
            }
            if(acc.valueHasChanged())
            {
                stringBuilder.append(" changed into ").append(getCopyContext().safeToString(acc.getCopiedValue())).append("\n");
                continue;
            }
            stringBuilder.append(" unchanged \n");
        }
        first = true;
        for(AttributeCopyCreator acc : partOf)
        {
            if(failed.contains(acc) || pending.contains(acc))
            {
                continue;
            }
            if(first)
            {
                stringBuilder.append("\twrote partOf attributes \n");
                first = false;
            }
            stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" value:")
                            .append(getCopyContext().safeToString(acc.getSourceValue()));
            if(acc.isPreset())
            {
                stringBuilder.append(", preset\n");
                continue;
            }
            if(!acc.isTranslated())
            {
                stringBuilder.append(", NOT YET TRANSLATED!\n");
                continue;
            }
            if(acc.valueHasChanged())
            {
                stringBuilder.append(" changed into ").append(getCopyContext().safeToString(acc.getCopiedValue())).append("\n");
                continue;
            }
            stringBuilder.append(" unchanged \n");
        }
        first = true;
        for(AttributeCopyCreator acc : other)
        {
            if(failed.contains(acc) || pending.contains(acc))
            {
                continue;
            }
            if(first)
            {
                stringBuilder.append("\twrote other attributes \n");
            }
            first = false;
            stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" value:")
                            .append(getCopyContext().safeToString(acc.getSourceValue()));
            if(acc.isPreset())
            {
                stringBuilder.append(", preset\n");
                continue;
            }
            if(!acc.isTranslated())
            {
                stringBuilder.append(", NOT YET TRANSLATED!\n");
                continue;
            }
            if(acc.valueHasChanged())
            {
                stringBuilder.append(" changed into ").append(getCopyContext().safeToString(acc.getCopiedValue())).append("\n");
                continue;
            }
            stringBuilder.append(" unchanged \n");
        }
        stringBuilder.append("\tblacklist: ").append(this.blacklist).append("\n");
        stringBuilder.append("\twhitelist: ").append(this.whitelist).append("\n");
        if(!pending.isEmpty())
        {
            stringBuilder.append("\t\tpending attributes \n");
            for(AttributeCopyCreator acc : pending)
            {
                stringBuilder.append("\t\t").append(acc.getDescriptor()).append("\n");
            }
        }
        if(!failed.isEmpty())
        {
            stringBuilder.append("\t\tfailed attributes \n");
            for(AttributeCopyCreator acc : failed)
            {
                stringBuilder.append("\t\t").append(acc.getDescriptor()).append("\n");
            }
        }
        Set<AttributeCopyDescriptor> excluded = this.tcd.getExcludedAttributeCopyDescriptors(isUpdate(), this.blacklist, this.whitelist);
        if(!excluded.isEmpty())
        {
            stringBuilder.append("\t\texcluded attributes \n");
            for(AttributeCopyDescriptor acd : excluded)
            {
                stringBuilder.append("\t\t").append(acd)
                                .append(this.blacklist.containsKey(acd.getQualifier()) ? " due to blacklist" : " due to whitelist")
                                .append("\n");
            }
        }
        return stringBuilder.toString();
    }


    public int hashCode()
    {
        return getSourceItem().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj instanceof ItemCopyCreator && getSourceItem().equals(((ItemCopyCreator)obj).getSourceItem()));
    }
}
