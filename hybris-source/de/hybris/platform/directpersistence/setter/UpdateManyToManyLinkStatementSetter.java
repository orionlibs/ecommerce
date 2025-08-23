package de.hybris.platform.directpersistence.setter;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UpdateManyToManyLinkStatementSetter implements PreparedStatementSetter
{
    private final JDBCValueMappings jdbcValueMapper;
    private final Set<ColumnPayload> changes;
    private final PK sourcePk;
    private final PK targetPk;


    public UpdateManyToManyLinkStatementSetter(PK sourcePk, PK targetPk, Set<ColumnPayload> changes)
    {
        this.sourcePk = sourcePk;
        this.targetPk = targetPk;
        this.changes = changes;
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        int fieldIndex = 1;
        for(ColumnPayload columnPayload : this.changes)
        {
            this.jdbcValueMapper.getValueWriter(columnPayload.getDeclaredTypeClass())
                            .setValue(ps, fieldIndex++, columnPayload.getValue());
        }
        this.jdbcValueMapper.PK_WRITER.setValue(ps, fieldIndex++, this.sourcePk);
        this.jdbcValueMapper.PK_WRITER.setValue(ps, fieldIndex++, this.targetPk);
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
