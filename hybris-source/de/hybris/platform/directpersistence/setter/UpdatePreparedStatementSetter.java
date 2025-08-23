package de.hybris.platform.directpersistence.setter;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UpdatePreparedStatementSetter implements PreparedStatementSetter
{
    private static final int IS_BLOCKED = 0;
    protected final Set<ColumnPayload> changes;
    protected Long optimisticLockCounter = null;
    protected boolean isLockedExpectedValue = false;
    protected final PK itemPk;
    protected final JDBCValueMappings jdbcValueMapper;
    protected final boolean checkOptimisticLockCounter;


    public UpdatePreparedStatementSetter(PK itemPk, Set<ColumnPayload> changes)
    {
        this.itemPk = itemPk;
        this.changes = changes;
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
        this.isLockedExpectedValue = false;
        this.checkOptimisticLockCounter = false;
    }


    public UpdatePreparedStatementSetter(PK itemPk, Set<ColumnPayload> changes, Long optimisticLockCounter, boolean isLockedExpectedValue, boolean checkOptimisticLockCounter)
    {
        this.itemPk = itemPk;
        this.changes = changes;
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
        this.optimisticLockCounter = optimisticLockCounter;
        this.isLockedExpectedValue = isLockedExpectedValue;
        this.checkOptimisticLockCounter = checkOptimisticLockCounter;
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        int fieldIndex = 1;
        for(ColumnPayload columnPayload : this.changes)
        {
            this.jdbcValueMapper.getValueWriter(columnPayload.getDeclaredTypeClass()).setValue(ps, fieldIndex++, columnPayload
                            .getValue());
        }
        this.jdbcValueMapper.PK_WRITER.setValue(ps, fieldIndex++, this.itemPk);
        if(this.checkOptimisticLockCounter && this.optimisticLockCounter != null)
        {
            ps.setLong(fieldIndex++, this.optimisticLockCounter.longValue());
        }
        if(this.isLockedExpectedValue)
        {
            ps.setInt(fieldIndex++, 0);
        }
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("[");
        for(ColumnPayload colPayload : this.changes)
        {
            sb.append(colPayload).append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
