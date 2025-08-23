package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.locking.ItemLockedForProcessingException;
import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.exception.ConcurrentModificationException;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import java.text.MessageFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class OptimisticLockingAndItemLockingResultCheck implements BatchCollector.ResultCheck
{
    private final PK itemPk;
    private final JdbcTemplate jdbcTemplate;
    private final String tableName;
    private final boolean optimisticLockingEnabled;
    private final long optimisticLockingCounter;
    private static final String SELECT_STATEMENT = "SELECT " + ServiceCol.SEALED.colName() + ", " + ServiceCol.HJMP_TS.colName() + " FROM {0} WHERE PK = {1}";


    public OptimisticLockingAndItemLockingResultCheck(PK itemPk, JdbcTemplate jdbcTemplate, String tableName, boolean optimisticLockingEnabled, long optimisticLockingCounter)
    {
        this.itemPk = itemPk;
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.optimisticLockingEnabled = optimisticLockingEnabled;
        this.optimisticLockingCounter = optimisticLockingCounter;
    }


    public void checkResult(int modifiedLines)
    {
        if(modifiedLines == 1)
        {
            return;
        }
        SLDResultCheckRow row = readRow();
        if(row.getNumberOfRows() == 0)
        {
            if(this.optimisticLockingEnabled)
            {
                throw new ConcurrentModificationException("item " + this.itemPk + " doesnt exist in database");
            }
            return;
        }
        if(row.isLocked())
        {
            throw new ItemLockedForProcessingException("Item " + this.itemPk + " is locked for processing and cannot be saved or removed");
        }
        if(this.optimisticLockingEnabled)
        {
            throw new ConcurrentModificationException("item pk " + this.itemPk + " was modified concurrently - expected database version " + this.optimisticLockingCounter + " but got " + row
                            .getCurrentHJMPTS() + ", expected blocked = false but got blocked = " + row
                            .isLocked() + ", entity state = " + this);
        }
    }


    private SLDResultCheckRow readRow()
    {
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(MessageFormat.format(SELECT_STATEMENT, new Object[] {this.tableName, this.itemPk}));
        if(rowSet.next())
        {
            long dbTS = rowSet.getLong(2);
            boolean isLocked = rowSet.getBoolean(1);
            if(rowSet.next())
            {
                throw new ConcurrentModificationException("item pk " + this.itemPk + " exist multiple times in database");
            }
            return new SLDResultCheckRow(dbTS, isLocked, 1);
        }
        return new SLDResultCheckRow(0);
    }
}
