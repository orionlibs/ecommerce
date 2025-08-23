package de.hybris.platform.persistence.audit.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.AuditableChange;
import de.hybris.platform.persistence.audit.AuditableOperation;
import de.hybris.platform.persistence.audit.AuditableOperationHandler;
import de.hybris.platform.persistence.audit.AuditableSaver;
import de.hybris.platform.persistence.audit.InternalAuditableOperation;
import de.hybris.platform.tx.BeforeCommitNotification;
import de.hybris.platform.tx.BeforeRollbackNotification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionalAuditableOperationHandler implements AuditableOperationHandler, BeforeCommitNotification, BeforeRollbackNotification
{
    private static final Logger LOG = LoggerFactory.getLogger(TransactionalAuditableOperationHandler.class);
    private final SLDDataContainerProvider sldDataContainerProvider;
    private final AuditableSaver auditableSaver;
    private final Map<PK, SLDDataContainer> stateBefore = new HashMap<>();
    private final Set<PK> deletedItems = new HashSet<>();


    public TransactionalAuditableOperationHandler(SLDDataContainerProvider sldDataContainerProvider, AuditableSaver saver)
    {
        Objects.requireNonNull(sldDataContainerProvider, "sldDataContainerProvider mustn't be null.§");
        this.sldDataContainerProvider = sldDataContainerProvider;
        this.auditableSaver = saver;
    }


    public void beforeRollback()
    {
        LOG.debug("Transaction has been rolled back. No audit will be stored.");
    }


    public void beforeCommit()
    {
        LOG.debug("Transaction has been commited. Storing audit.");
        List<AuditableChange> changeList = new ArrayList<>(this.stateBefore.size());
        this.stateBefore.forEach((pk, before) -> {
            SLDDataContainer after = this.deletedItems.contains(pk) ? null : this.sldDataContainerProvider.get(pk);
            changeList.add(new AuditableChange(before, after));
        });
        this.auditableSaver.storeAudit(changeList);
    }


    public void aboutToExecute(InternalAuditableOperation operation)
    {
        requireNonNullOperation((AuditableOperation)operation);
        PK pk = operation.getItemPk();
        if(this.stateBefore.containsKey(pk))
        {
            return;
        }
        SLDDataContainer before = operation.isCreation() ? null : this.sldDataContainerProvider.get(pk);
        this.stateBefore.put(pk, before);
        if(operation.isDeletion())
        {
            this.deletedItems.add(pk);
        }
    }


    public void clearAudit(PK pk)
    {
        Objects.requireNonNull(pk, "pk mustn't be null.");
        this.stateBefore.remove(pk);
        this.deletedItems.remove(pk);
    }


    public void clearAudit(String typeCode)
    {
        Objects.requireNonNull(typeCode, "typeCode mustn't be null.");
        List<PK> pkListToRemove = (List<PK>)this.stateBefore.values().stream().filter(val -> typeCode.equals(val.getTypeCode())).map(it -> it.getPk()).collect(Collectors.toList());
        pkListToRemove.forEach(pk -> this.stateBefore.remove(pk));
        pkListToRemove.forEach(pk -> this.deletedItems.remove(pk));
    }


    public void successfullyExecuted(InternalAuditableOperation operation)
    {
        requireNonNullOperation((AuditableOperation)operation);
        requireThatOperationHasBeenStarted(operation);
    }


    public void failedToExecute(InternalAuditableOperation operation)
    {
        requireNonNullOperation((AuditableOperation)operation);
        requireThatOperationHasBeenStarted(operation);
    }


    private static void requireNonNullOperation(AuditableOperation operation)
    {
        Objects.requireNonNull(operation, "operation mustn't be null.");
    }


    private void requireThatOperationHasBeenStarted(InternalAuditableOperation operation)
    {
        if(!this.stateBefore.containsKey(operation.getItemPk()))
        {
            LOG.error("Operation '{}' hasn't been started in this transaction.", operation);
            throw new IllegalStateException("Operation hasn't been started in this transaction.");
        }
    }
}
