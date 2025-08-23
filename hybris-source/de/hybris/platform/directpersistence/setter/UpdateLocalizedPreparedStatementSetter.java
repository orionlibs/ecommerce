package de.hybris.platform.directpersistence.setter;

import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class UpdateLocalizedPreparedStatementSetter extends UpdatePreparedStatementSetter
{
    private final PK langPk;


    public UpdateLocalizedPreparedStatementSetter(PK itemPk, PK langPk, Set<ColumnPayload> changes)
    {
        super(itemPk, changes);
        this.langPk = langPk;
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        super.setValues(ps);
        int fieldIndex = computeFieldIndex();
        this.jdbcValueMapper.PK_WRITER.setValue(ps, fieldIndex++, this.langPk);
    }


    private int computeFieldIndex()
    {
        int result = this.changes.size() + 2;
        if(this.optimisticLockCounter != null)
        {
            result++;
        }
        return result;
    }
}
