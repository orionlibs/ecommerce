package de.hybris.platform.directpersistence.setter;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UpdateOneToManyTargetStatementSetter implements PreparedStatementSetter
{
    public static final Integer RESET_POSITION = Integer.valueOf(-1);
    private final JDBCValueMappings jdbcValueMapper;
    private final Iterable<PK> targetPks;
    private final PK sourcePk;
    private final Integer position;


    public UpdateOneToManyTargetStatementSetter(Iterable<PK> targetPks)
    {
        this(targetPks, null);
    }


    public UpdateOneToManyTargetStatementSetter(Iterable<PK> targetPks, PK sourcePk)
    {
        this(targetPks, sourcePk, null);
    }


    public UpdateOneToManyTargetStatementSetter(Iterable<PK> targetPks, PK sourcePk, Integer position)
    {
        this.jdbcValueMapper = JDBCValueMappings.getInstance();
        this.targetPks = targetPks;
        this.sourcePk = sourcePk;
        this.position = position;
    }


    public void setValues(PreparedStatement ps) throws SQLException
    {
        int fieldIndex = 1;
        if(this.position != null && this.position.intValue() >= 0)
        {
            this.jdbcValueMapper.getValueWriter(Integer.class).setValue(ps, fieldIndex++, this.position);
        }
        else if(this.position != null && this.position.intValue() == RESET_POSITION.intValue())
        {
            this.jdbcValueMapper.getValueWriter(Integer.class).setValue(ps, fieldIndex++, null);
        }
        this.jdbcValueMapper.PK_WRITER.setValue(ps, fieldIndex++, this.sourcePk);
        for(PK pk : this.targetPks)
        {
            this.jdbcValueMapper.PK_WRITER.setValue(ps, fieldIndex++, pk);
        }
    }


    public String toString()
    {
        return MessageFormat.format("SourcePK({0}), Targets({1})", new Object[] {this.sourcePk, this.targetPks});
    }
}
