package de.hybris.platform.servicelayer.tx;

import de.hybris.platform.core.model.ItemModel;
import org.springframework.transaction.TransactionStatus;

public abstract class ModelAwareTransactionCallbackWithoutResult<M extends ItemModel> extends ModelAwareTransactionCallback
{
    protected abstract void doInModelAwareTransactionWithoutResult(TransactionStatus paramTransactionStatus);


    protected final M doInModelAwareTransaction(TransactionStatus status)
    {
        doInModelAwareTransactionWithoutResult(status);
        return null;
    }
}
