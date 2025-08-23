package de.hybris.platform.persistence.audit;

import de.hybris.platform.tx.BeforeCommitNotification;
import de.hybris.platform.tx.BeforeRollbackNotification;
import de.hybris.platform.tx.Transaction;
import java.util.function.Supplier;

public final class AuditableOperationsTestSupport
{
    private static final ThreadLocal<AuditableOperationHandler> handlerToUse = new ThreadLocal<>();
    private static final ThreadLocal<Supplier<AuditableOperationHandler>> transactionalHandler = new ThreadLocal<>();


    public static void executeWithHandlerFactory(AuditableOperationHandler handler, RunnableWithException operation) throws Exception
    {
        handlerToUse.set(handler);
        Supplier<AuditableOperationHandler> currentFactory = AuditableOperations.getHandlerFactory();
        ThreadAwareHandlerFactory newFactory = new ThreadAwareHandlerFactory(currentFactory);
        AuditableOperations.setHandlerFactory((Supplier)newFactory);
        try
        {
            operation.run();
        }
        finally
        {
            handlerToUse.remove();
            AuditableOperations.setHandlerFactory(currentFactory);
        }
    }


    public static void executeWithTransactionalHandler(Supplier<AuditableOperationHandler> handler, RunnableWithException operation) throws Exception
    {
        transactionalHandler.set(handler);
        Supplier<AuditableOperationHandler> currentFactory = AuditableOperations.getTransactionalHandlerFactory();
        ThreadAwareTransactionalHandlerFactory newHandler = new ThreadAwareTransactionalHandlerFactory(currentFactory);
        AuditableOperations.setTransactionalHandlerFactory((Supplier)newHandler);
        try
        {
            operation.run();
        }
        finally
        {
            transactionalHandler.remove();
            AuditableOperations.setTransactionalHandlerFactory(currentFactory);
        }
    }


    public static void verifyIfMethodBeforeCommitWasCalledOnCommitXTimes(BeforeCommitNotification beforeCommitToSpy, int numberOfTimes)
    {
        BeforeCommitNotification beforeCommitVeryficator = (BeforeCommitNotification)Transaction.current().getAttached(BeforeCommitNotificationVeryficator.class);
        if(beforeCommitVeryficator != null)
        {
            Transaction.current().dettach(beforeCommitVeryficator);
        }
        Transaction.current().attach(new BeforeCommitNotificationVeryficator(beforeCommitToSpy, numberOfTimes));
    }


    public static void verifyIfMethodBeforeRollbackWasCalledOnRollbacktXTimes(BeforeRollbackNotification beforeCommitToSpy, int numberOfTimes)
    {
        BeforeRollbackNotificationVeryficator beforeRollbackVeryficator = (BeforeRollbackNotificationVeryficator)Transaction.current().getAttached(BeforeRollbackNotificationVeryficator.class);
        if(beforeRollbackVeryficator != null)
        {
            Transaction.current().dettach(beforeRollbackVeryficator);
        }
        Transaction.current().attach(new BeforeRollbackNotificationVeryficator(beforeCommitToSpy, numberOfTimes));
    }


    public static Supplier<AuditableOperationHandler> getMockitoSpyTransactionalHandler()
    {
        return (Supplier<AuditableOperationHandler>)new Object();
    }


    public static Supplier<AuditableOperationHandler> getMockitoSpyNonTransactionalHandler()
    {
        return (Supplier<AuditableOperationHandler>)new Object();
    }
}
