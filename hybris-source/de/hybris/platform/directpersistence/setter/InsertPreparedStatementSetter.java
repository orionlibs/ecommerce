package de.hybris.platform.directpersistence.setter;

import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class InsertPreparedStatementSetter implements PreparedStatementSetter
{
    protected final JDBCValueMappings jdbcValueMapper;
    protected final Set<ColumnPayload> changes;


    public InsertPreparedStatementSetter(Set<ColumnPayload> changes)
    {
        this.changes = changes;
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        setValues(ps, this.changes);
    }


    protected void setValues(PreparedStatement ps, Set<ColumnPayload> changes) throws SQLException
    {
        int fieldIndex = 1;
        for(ColumnPayload columnPayload : changes)
        {
            this.jdbcValueMapper.getValueWriter(columnPayload.getDeclaredTypeClass())
                            .setValue(ps, fieldIndex++, columnPayload.getValue());
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
