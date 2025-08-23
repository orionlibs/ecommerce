package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.exception.ModelPersistenceException;
import de.hybris.platform.directpersistence.statement.StatementHolder;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UpdateRowResultCheck implements BatchCollector.ResultCheck
{
    private static final Logger LOG = Logger.getLogger(UpdateRowResultCheck.class);
    private StatementHolder initialQuery;
    private final StatementHolder rescueQuery;
    private final JdbcTemplate jdbcTemplate;
    private final AtomicInteger updatedRows = new AtomicInteger();


    public UpdateRowResultCheck(StatementHolder rescueQuery, JdbcTemplate jdbcTemplate)
    {
        this.rescueQuery = rescueQuery;
        this.jdbcTemplate = jdbcTemplate;
    }


    public int getUpdatedRows()
    {
        return this.updatedRows.get();
    }


    public void checkResult(int result)
    {
        if(result != 1)
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Row was not updated, trying to insert it instead: [" + this.rescueQuery + "]");
                }
                this.updatedRows.addAndGet(executeUpdate(this.rescueQuery));
            }
            catch(DuplicateKeyException e)
            {
                if(this.initialQuery != null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Row was already in database, retrying update instead: [" + this.initialQuery + "]", (Throwable)e);
                    }
                    this.updatedRows.addAndGet(executeUpdate(this.initialQuery));
                }
                if(this.updatedRows.get() == 0)
                {
                    throw new ModelPersistenceException("Row was neither inserted or updated on retry: [initial query: " + this.initialQuery + ", rescue query: " + this.rescueQuery + "]");
                }
            }
            catch(Exception e)
            {
                throw new ModelPersistenceException(e.getMessage(), e);
            }
        }
    }


    public void setInitialQuery(StatementHolder initialQuery)
    {
        this.initialQuery = initialQuery;
    }


    private int executeUpdate(StatementHolder holder)
    {
        if(holder.isSetterBased())
        {
            return this.jdbcTemplate.update(holder.getStatement(), holder.getSetter());
        }
        return this.jdbcTemplate.update(holder.getStatement(), holder.getParams());
    }
}
