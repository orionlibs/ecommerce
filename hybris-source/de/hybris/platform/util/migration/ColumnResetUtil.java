package de.hybris.platform.util.migration;

import de.hybris.platform.core.Registry;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ColumnResetUtil
{
    public static void resetDefaultValue(String typeCode)
    {
        JdbcTemplate template = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
        template.update("UPDATE attributedescriptors ad SET ad.p_defaultvalue = ? WHERE ad.enclosingTypePk IN (SELECT ct.pk FROM composedtypes ct WHERE ct.InternalCode = ?)", new Object[] {null, typeCode});
    }
}
