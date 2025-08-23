package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;

public class JaloTenTimesSetNonExistingPropertyInTx extends AbstractJaloItemTest
{
    public void executeBlock() throws Exception
    {
        Transaction.current().execute((TransactionBody)new Object(this));
    }


    public String getTestName()
    {
        return "10 times set non existing property in Transaction";
    }
}
