package de.hybris.platform.persistence.property.loader.internal.mapper;

import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.persistence.property.loader.internal.dto.AttributeDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AttributeRowMapper implements RowMapper<AttributeDTO>
{
    private final JDBCValueMappings.ValueReader longReader = JDBCValueMappings.getInstance().getValueReader(Long.class);
    private final JDBCValueMappings.ValueReader stringReader = JDBCValueMappings.getInstance().getValueReader(String.class);
    private final JDBCValueMappings.ValueReader intReader = JDBCValueMappings.getInstance().getValueReader(Integer.class);


    public AttributeDTO mapRow(ResultSet rs, int i) throws SQLException
    {
        long pk = this.longReader.getLong(rs, "PK");
        long ownerPK = this.longReader.getLong(rs, "ownerPkString");
        String qualifierInternal = (String)this.stringReader.getValue(rs, "qualifierInternal");
        String columnName = (String)this.stringReader.getValue(rs, "columnName");
        int modifiers = this.intReader.getInt(rs, "modifiers");
        Long persistenceTypePk = Long.valueOf(this.longReader.getLong(rs, "PersistenceTypePK"));
        AttributeDTO attribute = new AttributeDTO();
        attribute.setPk(Long.valueOf(pk));
        attribute.setOwnerPk(Long.valueOf(ownerPK));
        attribute.setQualifier(qualifierInternal);
        attribute.setColumnName(columnName);
        attribute.setModifiers(modifiers);
        attribute.setPersistenceTypePk(persistenceTypePk);
        attribute.setProperty(isProperty(modifiers));
        attribute.setLocalized(isLocalized(modifiers));
        attribute.setEncrypted(isEncrypted(modifiers));
        attribute.setNotForOptimization(isNotForOptimization(modifiers));
        return attribute;
    }


    private boolean isProperty(int modifiers)
    {
        return getSingleModifier(modifiers, 256);
    }


    private boolean isLocalized(int modifiers)
    {
        return getSingleModifier(modifiers, 512);
    }


    private boolean isEncrypted(int modifiers)
    {
        return getSingleModifier(modifiers, 16384);
    }


    private boolean isNotForOptimization(int modifiers)
    {
        return getSingleModifier(modifiers, 8192);
    }


    private boolean getSingleModifier(int modifiers, int flag)
    {
        return ((modifiers & flag) == flag);
    }
}
