package de.hybris.platform.servicelayer.tx;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

public abstract class ModelAwareTransactionCallback<M extends ItemModel> implements TransactionCallback
{
    protected boolean isEnableDelayedStore()
    {
        return false;
    }


    public final M doInTransaction(TransactionStatus status)
    {
        return doInTransactionInternal(status);
    }


    private M doInTransactionInternal(TransactionStatus status)
    {
        M result = null;
        if(getModelService() == null)
        {
            throw new IllegalStateException("Provided model service is null.");
        }
        Transaction currentTx = prepareTransaction();
        currentTx.enableDelayedStore(isEnableDelayedStore());
        result = doInModelAwareTransaction(status);
        getModelService().saveAll();
        return result;
    }


    protected Transaction prepareTransaction()
    {
        Transaction currentTx = Transaction.current();
        if(currentTx == null)
        {
            throw new IllegalStateException("Current transaction retrived as Transaction.current() is null.");
        }
        return currentTx;
    }


    protected abstract ModelService getModelService();


    protected abstract M doInModelAwareTransaction(TransactionStatus paramTransactionStatus);
}
