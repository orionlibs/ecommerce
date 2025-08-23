package de.hybris.platform.persistence.links.jdbc.dml;

import com.google.common.base.Preconditions;
import de.hybris.platform.persistence.links.jdbc.JdbcInsertLinkOperation;
import de.hybris.platform.persistence.links.jdbc.JdbcLinkOperation;
import de.hybris.platform.persistence.links.jdbc.JdbcLinkOperationExecutor;
import de.hybris.platform.persistence.links.jdbc.JdbcRemoveLinkOperation;
import de.hybris.platform.persistence.links.jdbc.JdbcSetLinkOperation;
import de.hybris.platform.tx.Transaction;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class DmlLinkOperationExecutor implements JdbcLinkOperationExecutor
{
    private final RelationModificationContextFactory contextFactory;
    private final RelationsSearcher relationSearcher;
    private final TransactionTemplate transactionTemplate;
    private final Remove remove;
    private final Insert insert;
    private final Set set;


    public DmlLinkOperationExecutor(RelationModificationContextFactory contextFactory, RelationsSearcher relationSearcher, TransactionTemplate transactionTemplate)
    {
        Preconditions.checkNotNull(contextFactory, "contextFactory can't be null");
        Preconditions.checkNotNull(relationSearcher, "relationSearcher can't be null");
        Preconditions.checkNotNull(transactionTemplate, "transactionTemplate can't be null");
        this.contextFactory = contextFactory;
        this.relationSearcher = relationSearcher;
        this.transactionTemplate = transactionTemplate;
        this.remove = new Remove(this);
        this.insert = new Insert(this);
        this.set = new Set(this);
    }


    public void execute(JdbcInsertLinkOperation operation)
    {
        doWithContextWithinTransaction(operation, (ExecutableWithContext<JdbcInsertLinkOperation>)this.insert);
    }


    public void execute(JdbcRemoveLinkOperation operation)
    {
        doWithContextWithinTransaction(operation, (ExecutableWithContext<JdbcRemoveLinkOperation>)this.remove);
    }


    public void execute(JdbcSetLinkOperation operation)
    {
        doWithContextWithinTransaction(operation, (ExecutableWithContext<JdbcSetLinkOperation>)this.set);
    }


    private <T extends JdbcLinkOperation> void doWithContextWithinTransaction(T operation, ExecutableWithContext<T> executable)
    {
        if(Transaction.current().isRunning())
        {
            RelationModifictionContext ctx = this.contextFactory.createContextForRunningTransaction((JdbcLinkOperation)operation);
            executable.execute((JdbcLinkOperation)operation, ctx);
            ctx.flush();
        }
        else
        {
            this.transactionTemplate.execute((TransactionCallback)new Object(this, (JdbcLinkOperation)operation, executable));
        }
    }
}
