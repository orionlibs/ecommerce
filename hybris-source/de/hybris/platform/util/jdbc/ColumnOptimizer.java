package de.hybris.platform.util.jdbc;

import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

class ColumnOptimizer
{
    private static final int MAX_TABLE_NAME_LENGTH = 30;
    private static final Logger log = Logger.getLogger(ColumnOptimizer.class.getName());
    private final int columnMaxLength;
    private final DBTable tbl;
    private final Map<String, AttributeColumnMapping> columns;


    ColumnOptimizer(DBTable tbl)
    {
        this(tbl, -1);
    }


    ColumnOptimizer(DBTable tbl, int maxlengthColumnName)
    {
        this.tbl = tbl;
        this.columns = new LinkedHashMap<>();
        this.columnMaxLength = (maxlengthColumnName > 0) ? maxlengthColumnName : getDefaultMaxLength();
    }


    protected int getDefaultMaxLength()
    {
        return 30;
    }


    private Map<String, AttributeColumnMapping> getColumnMappings()
    {
        if(MapUtils.isEmpty(this.columns))
        {
            return Collections.EMPTY_MAP;
        }
        return Collections.unmodifiableMap(this.columns);
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder(this.tbl.getName());
        sb.append("(").append(getColumnMappings().size()).append(")");
        return sb.toString();
    }


    private AttributeColumnMapping getMapping(String columnName)
    {
        return getColumnMappings().get(columnName.toLowerCase());
    }


    private boolean canAddColumn(String qualifier, String columnName, String sqlColumnDef, boolean isPK)
    {
        AttributeColumnMapping current = getMapping(columnName);
        if(current == null)
        {
            return true;
        }
        if(current.getQualifier().equalsIgnoreCase(qualifier))
        {
            if(!current.getColumn().getSQLTypeDefinition().equalsIgnoreCase(sqlColumnDef))
            {
                if(log.isDebugEnabled())
                {
                    log.debug("attribute '" + qualifier + "' cannot share column '" + columnName + "' due to mismatching sql definition : '" + current
                                    .getColumn()
                                    .getSQLTypeDefinition() + "'<>'" + sqlColumnDef + "'");
                }
                return false;
            }
            if(current.getColumn().isPrimaryKey() != isPK)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("attribute '" + qualifier + "' cannot share column '" + columnName + "' due to mismatching PK status : " + current
                                    .getColumn().isPrimaryKey() + "<>" + isPK);
                }
                return false;
            }
            return true;
        }
        if(log.isDebugEnabled())
        {
            log.debug("current column '" + current
                            .getQualifier() + "' does not match qualifier '" + qualifier + "' of column '" + columnName + "'");
        }
        return false;
    }


    public DBColumn addColumn(YAttributeDeployment aDepl, String qualifier, String columnName, String sqlColumnDescription, boolean isPrimaryKey, boolean adjustName)
    {
        DBColumn col = null;
        String adjusted = adjustColumnName(qualifier, columnName.toLowerCase(), sqlColumnDescription, isPrimaryKey);
        if(!adjustName && !columnName.equalsIgnoreCase(adjusted))
        {
            throw new IllegalArgumentException("non-adjustable column name '" + columnName + "' of " + aDepl + " cannot be used - adjusted name is '" + adjusted + "' ");
        }
        String realColumn = adjustName ? adjusted : columnName;
        AttributeColumnMapping current = getMapping(realColumn);
        if(current == null)
        {
            col = this.tbl.getColumn(realColumn);
            if(col == null)
            {
                col = this.tbl.createColumn(realColumn, sqlColumnDescription, isPrimaryKey);
            }
            else
            {
                col.setSQLTypeDefinition(sqlColumnDescription);
                col.setIsPrimaryKey(isPrimaryKey);
            }
            this.columns.put(realColumn.toLowerCase(), new AttributeColumnMapping(qualifier, col));
        }
        else
        {
            (col = current.col).setSQLTypeDefinition(sqlColumnDescription);
        }
        return col;
    }


    private String adjustColumnName(String attributeQualifier, String proposedColumnName, String sqlColumnDescription, boolean isPK)
    {
        if(proposedColumnName.length() > this.columnMaxLength ||
                        !canAddColumn(attributeQualifier, proposedColumnName, sqlColumnDescription, isPK))
        {
            int toLength = Math.min(this.columnMaxLength, proposedColumnName.length());
            String cutVersion = proposedColumnName.substring(0, toLength);
            int i = 0;
            while(!canAddColumn(attributeQualifier, cutVersion, sqlColumnDescription, isPK))
            {
                String postFix = Integer.toString(i++);
                cutVersion = proposedColumnName.substring(0, toLength - postFix.length()) + proposedColumnName.substring(0, toLength - postFix.length());
            }
            return cutVersion;
        }
        return proposedColumnName;
    }
}
