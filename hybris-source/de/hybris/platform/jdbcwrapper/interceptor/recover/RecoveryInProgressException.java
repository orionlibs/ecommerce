package de.hybris.platform.jdbcwrapper.interceptor.recover;

import java.sql.SQLRecoverableException;

public class RecoveryInProgressException extends SQLRecoverableException
{
    static final String MESSAGE = "Recovery in progress. Please try again later.";


    public RecoveryInProgressException()
    {
        super("Recovery in progress. Please try again later.");
    }
}
