package de.hybris.platform.persistence.links.jdbc.dml.context;

import com.google.common.base.Preconditions;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.persistence.links.jdbc.JdbcLinkOperation;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModificationContextFactory;
import de.hybris.platform.persistence.links.jdbc.dml.RelationModifictionContext;
import java.util.Date;

public class TransactionAwareModificationContextFactory implements RelationModificationContextFactory
{
    private final WritePersistenceGateway writePersistenceGateway;


    public TransactionAwareModificationContextFactory(WritePersistenceGateway writePersistenceGateway)
    {
        Preconditions.checkNotNull(writePersistenceGateway, "writePersistenceGateway can't be null");
        this.writePersistenceGateway = writePersistenceGateway;
    }


    public RelationModifictionContext createContextForNewTransaction(JdbcLinkOperation operation)
    {
        return (RelationModifictionContext)new NewTransactionContext(operation.getRelationCode(), this.writePersistenceGateway, operation.isParentSource(), new Date());
    }


    public RelationModifictionContext createContextForRunningTransaction(JdbcLinkOperation operation)
    {
        return (RelationModifictionContext)new RunningTransactionContext(operation.getRelationCode(), this.writePersistenceGateway, operation.isParentSource(), new Date());
    }
}
