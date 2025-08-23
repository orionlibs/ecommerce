package de.hybris.platform.directpersistence.setter;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import de.hybris.platform.directpersistence.record.ColumnPayload;
import de.hybris.platform.directpersistence.statement.backend.ServiceCol;
import de.hybris.platform.persistence.property.OldPropertyJDBC;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class InsertPropsPreparedStatementSetter extends InsertPreparedStatementSetter
{
    public InsertPropsPreparedStatementSetter(Set<ColumnPayload> changes)
    {
        super(changes);
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        Set<ColumnPayload> changes = filterColumnsForManualSettingFromChanges();
        setValues(ps, changes);
        ColumnPayload valueColumn = getColumnPayload(ServiceCol.VALUE1.colName());
        int valueCode = OldPropertyJDBC.getValueTypeCode(valueColumn.getValue());
        this.jdbcValueMapper.getValueWriter(Integer.class).setValue(ps, 7, Integer.valueOf(valueCode));
        OldPropertyJDBC.fillValueStatement(ps, valueCode, valueColumn.getValue(), 8, 9);
    }


    private Set<ColumnPayload> filterColumnsForManualSettingFromChanges()
    {
        return Sets.filter(this.changes, (Predicate)new Object(this));
    }


    private ColumnPayload getColumnPayload(String colName)
    {
        return Iterables.filter(this.changes, (Predicate)new Object(this, colName))
                        .iterator().next();
    }
}
