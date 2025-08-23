package de.hybris.platform.directpersistence.setter;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.OldPropertyJDBC;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UpdatePropsPreparedStatementSetter implements PreparedStatementSetter
{
    protected final JDBCValueMappings jdbcValueMapper;
    private final PK langPk;
    private final PK itemPk;
    private final String propName;
    private final Object value;


    public UpdatePropsPreparedStatementSetter(PK itemPk, PK langPK, String propName, Object value)
    {
        this.itemPk = itemPk;
        this.langPk = langPK;
        this.propName = propName;
        this.value = value;
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
    }


    public UpdatePropsPreparedStatementSetter(PK itemPk, String propName, Object value)
    {
        this.itemPk = itemPk;
        this.langPk = null;
        this.propName = propName;
        this.value = value;
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        int valueCode = OldPropertyJDBC.getValueTypeCode(this.value);
        OldPropertyJDBC.fillValueStatement(ps, valueCode, this.value, 1, 2);
        this.jdbcValueMapper.PK_WRITER.setValue(ps, 3, this.itemPk);
        this.jdbcValueMapper.STRING_WRITER.setValue(ps, 4, this.propName);
        if(this.langPk != null)
        {
            this.jdbcValueMapper.PK_WRITER.setValue(ps, 5, this.langPk);
        }
    }


    public String toString()
    {
        return "UpdatePropsPreparedStatementSetter{itemPk=" + this.itemPk + ", jdbcValueMapper=" + this.jdbcValueMapper + ", langPk=" + this.langPk + ", propName='" + this.propName + "', value=" + this.value + "}";
    }
}
