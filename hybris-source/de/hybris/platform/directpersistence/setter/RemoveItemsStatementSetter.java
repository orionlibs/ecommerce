package de.hybris.platform.directpersistence.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class RemoveItemsStatementSetter implements PreparedStatementSetter
{
    private static final int IS_BLOCKED = 0;
    private final List<Long> pks;
    private final boolean withOptimisticLocking;
    private boolean checkIsBlocked = false;
    protected Long optimisticLockCounter = null;


    public RemoveItemsStatementSetter(List<Long> pks)
    {
        this.pks = pks;
        this.optimisticLockCounter = null;
        this.withOptimisticLocking = false;
    }


    public RemoveItemsStatementSetter(List<Long> pks, Long optimisticLockCounter, boolean withOptimisticLocking)
    {
        this.pks = pks;
        this.optimisticLockCounter = optimisticLockCounter;
        this.withOptimisticLocking = withOptimisticLocking;
        this.checkIsBlocked = true;
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        int fieldIndex = 1;
        for(Long pk : this.pks)
        {
            ps.setLong(fieldIndex++, pk.longValue());
        }
        if(this.checkIsBlocked)
        {
            ps.setInt(fieldIndex++, 0);
        }
        if(this.withOptimisticLocking)
        {
            ps.setLong(fieldIndex++, this.optimisticLockCounter.longValue());
        }
    }
}
