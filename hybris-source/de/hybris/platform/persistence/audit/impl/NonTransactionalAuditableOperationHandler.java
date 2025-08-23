package de.hybris.platform.persistence.audit.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.cache.SLDDataContainerProvider;
import de.hybris.platform.persistence.audit.AuditableChange;
import de.hybris.platform.persistence.audit.AuditableOperation;
import de.hybris.platform.persistence.audit.AuditableOperationHandler;
import de.hybris.platform.persistence.audit.AuditableSaver;
import de.hybris.platform.persistence.audit.InternalAuditableOperation;
import java.util.Collections;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonTransactionalAuditableOperationHandler implements AuditableOperationHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(NonTransactionalAuditableOperationHandler.class);
    private final SLDDataContainerProvider sldDataContainerProvider;
    private final AuditableSaver saver;
    private InternalAuditableOperation operationInProgress;
    private SLDDataContainer stateBefore;
    private boolean cleared = false;


    public NonTransactionalAuditableOperationHandler(SLDDataContainerProvider sldDataContainerProvider, AuditableSaver saver)
    {
        Objects.requireNonNull(sldDataContainerProvider, "sldDataContainerProvider mustn't be null.");
        this.sldDataContainerProvider = sldDataContainerProvider;
        this.saver = saver;
    }


    public void aboutToExecute(InternalAuditableOperation operation)
    {
        requireNonNullOperation((AuditableOperation)operation);
        requireThatThereIsNoOperationInProgress((AuditableOperation)operation);
        this.operationInProgress = operation;
        this.stateBefore = getStateBefore();
    }


    public void clearAudit(PK pk)
    {
        Objects.requireNonNull(pk, "pk mustn't be null");
        if(this.stateBefore != null && this.stateBefore.getPk().equals(pk))
        {
            this.cleared = true;
        }
    }


    private SLDDataContainer getStateBefore()
    {
        if(this.operationInProgress.isCreation())
        {
            return null;
        }
        return this.sldDataContainerProvider.get(this.operationInProgress.getItemPk());
    }


    public void successfullyExecuted(InternalAuditableOperation operation)
    {
        requireNonNullOperation((AuditableOperation)operation);
        requireToBeTheSameOperation((AuditableOperation)operation);
        if(this.cleared)
        {
            return;
        }
        SLDDataContainer stateAfter = getStateAfter();
        this.saver.storeAudit(Collections.singletonList(new AuditableChange(this.stateBefore, stateAfter)));
    }


    private SLDDataContainer getStateAfter()
    {
        if(this.operationInProgress.isDeletion())
        {
            return null;
        }
        return this.sldDataContainerProvider.get(this.operationInProgress.getItemPk());
    }


    public void failedToExecute(InternalAuditableOperation operation)
    {
        requireNonNullOperation((AuditableOperation)operation);
        requireToBeTheSameOperation((AuditableOperation)operation);
    }


    private static void requireNonNullOperation(AuditableOperation operation)
    {
        Objects.requireNonNull(operation, "operation mustn't be null.");
    }


    private void requireThatThereIsNoOperationInProgress(AuditableOperation operation)
    {
        if(this.operationInProgress != null)
        {
            LOG.error("Trying to execute '{}' but the handler was used for '{}'.", operation, this.operationInProgress);
            throw new IllegalStateException("Handler cannot be reused for more than one operation.");
        }
    }


    private void requireToBeTheSameOperation(AuditableOperation operation)
    {
        if(operation != this.operationInProgress)
        {
            LOG.error("Operation '{}' doesn't match to the '{}' which is in progress.", operation, this.operationInProgress);
            throw new IllegalStateException("Operation doesn't match");
        }
    }


    public void clearAudit(String typeCode)
    {
        if(this.stateBefore != null && this.stateBefore.getTypeCode().equals(typeCode))
        {
            this.cleared = true;
        }
    }
}
