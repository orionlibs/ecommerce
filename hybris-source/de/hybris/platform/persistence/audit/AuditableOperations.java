package de.hybris.platform.persistence.audit;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.persistence.audit.internal.AuditEnablementService;
import de.hybris.platform.tx.Transaction;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;

public final class AuditableOperations
{
    private static final AuditableOperation NO_OP_OPERATION = (AuditableOperation)new NoOpAuditableOperation();
    private static Supplier<AuditableOperationHandler> transactionalHandlerFactory = AuditableOperations::getTransactionalHandlerFromSpringContext;
    private static Supplier<AuditableOperationHandler> handlerFactory = AuditableOperations::getHandlerFromSpringContext;
    private static Predicate<Operation> shouldBeAudited = AuditableOperations::isAuditEnabled;


    public static AuditableOperation aboutToExecute(Operation... operations)
    {
        Objects.requireNonNull(operations, "operations mustn't be null.");
        if(operations.length == 0)
        {
            throw new IllegalArgumentException("No operations were provided.");
        }
        if(operations.length == 1)
        {
            return aboutToExecuteSingleOperation(operations[0]);
        }
        return aboutToExecuteMultipleOperations(operations);
    }


    public static void clearCurrentAuditOperationsFor(PK itemPK)
    {
        ((AuditableOperationHandler)handlerFactory.get()).clearAudit(itemPK);
    }


    public static void clearCurrentAuditOperationsFor(String typeCode)
    {
        ((AuditableOperationHandler)handlerFactory.get()).clearAudit(typeCode);
    }


    private static AuditableOperation aboutToExecuteSingleOperation(Operation operation)
    {
        if(!shouldBeAudited.test(operation))
        {
            return NO_OP_OPERATION;
        }
        AuditableOperationHandler handler = handlerFactory.get();
        SimpleAuditableOperation simpleAuditableOperation = new SimpleAuditableOperation(operation, handler);
        handler.aboutToExecute((InternalAuditableOperation)simpleAuditableOperation);
        return (AuditableOperation)simpleAuditableOperation;
    }


    private static AuditableOperation aboutToExecuteMultipleOperations(Operation[] operations)
    {
        if(!isRunningWithinTransaction())
        {
            throw new IllegalArgumentException("Executing more than one operation at once is not allowed outside the transaction.");
        }
        AuditableOperationHandler handler = handlerFactory.get();
        for(Operation operation : operations)
        {
            if(shouldBeAudited.test(operation))
            {
                SimpleAuditableOperation simpleAuditableOperation = new SimpleAuditableOperation(operation, handler);
                handler.aboutToExecute((InternalAuditableOperation)simpleAuditableOperation);
            }
        }
        return NO_OP_OPERATION;
    }


    public static AuditableOperationHandler getHandlerFromSpringContext()
    {
        if(isRunningWithinTransaction())
        {
            return getTransactionalHandler();
        }
        return getNonTransactionalHandlerFromSpringContext();
    }


    private static boolean isRunningWithinTransaction()
    {
        Transaction currentTx = Transaction.current();
        return (currentTx != null && currentTx.isRunning());
    }


    private static boolean isAuditEnabled(Operation operation)
    {
        ApplicationContext coreCtx = Registry.getCoreApplicationContext();
        AuditEnablementService service = (AuditEnablementService)coreCtx.getBean("auditingEnablementService", AuditEnablementService.class);
        if(!service.isAuditEnabledGlobally())
        {
            return false;
        }
        if(operation.isTypeIdentifiedByPk())
        {
            return service.isAuditEnabledForType(operation.getItemTypePk());
        }
        return service.isAuditEnabledForType(operation.getItemTypeCode());
    }


    private static AuditableOperationHandler getTransactionalHandler()
    {
        Transaction currentTx = Transaction.current();
        AuditableOperationHandler attachedHandler = (AuditableOperationHandler)currentTx.getAttached(AuditableOperationHandler.class);
        if(attachedHandler != null)
        {
            return attachedHandler;
        }
        AuditableOperationHandler transactionalHandler = transactionalHandlerFactory.get();
        currentTx.attach(transactionalHandler);
        return transactionalHandler;
    }


    static AuditableOperationHandler getTransactionalHandlerFromSpringContext()
    {
        ApplicationContext coreCtx = Registry.getCoreApplicationContext();
        AuditableOperationHandler handler = (AuditableOperationHandler)coreCtx.getBean("transactionalAuditableOperationHandler", AuditableOperationHandler.class);
        return handler;
    }


    static AuditableOperationHandler getNonTransactionalHandlerFromSpringContext()
    {
        ApplicationContext coreCtx = Registry.getCoreApplicationContext();
        AuditableOperationHandler handler = (AuditableOperationHandler)coreCtx.getBean("nonTransactionalAuditableOperationHandler", AuditableOperationHandler.class);
        return handler;
    }


    static void setTransactionalHandlerFactory(Supplier<AuditableOperationHandler> newTransactionalHandler)
    {
        transactionalHandlerFactory = newTransactionalHandler;
    }


    static Supplier<AuditableOperationHandler> getTransactionalHandlerFactory()
    {
        return transactionalHandlerFactory;
    }


    static Supplier<AuditableOperationHandler> getHandlerFactory()
    {
        return handlerFactory;
    }


    static void setHandlerFactory(Supplier<AuditableOperationHandler> handlerFactory)
    {
        AuditableOperations.handlerFactory = handlerFactory;
    }
}
