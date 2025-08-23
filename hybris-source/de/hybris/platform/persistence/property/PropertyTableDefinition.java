package de.hybris.platform.persistence.property;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class PropertyTableDefinition implements Serializable
{
    private static final int MAX_COLUMN_LENGTH = 30;
    private final String dumpTableName;
    private final String unlocTableName;
    private final String locTableName;
    private final Map<String, ColumnDefintionEntry> unlocColumns;
    private final Map<String, ColumnDefintionEntry> locColumns;
    private final int itemTypeCode;


    PropertyTableDefinition(String dumpTableName, String unloctableName, String locTableName, int itemTypeCode)
    {
        this.dumpTableName = dumpTableName;
        this.unlocTableName = unloctableName;
        this.locTableName = locTableName;
        this.locColumns = (Map<String, ColumnDefintionEntry>)new CaseInsensitiveMap();
        this.unlocColumns = (Map<String, ColumnDefintionEntry>)new CaseInsensitiveMap();
        this.itemTypeCode = itemTypeCode;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(this.locTableName != null)
        {
            sb.append(this.locTableName).append("(").append(this.locColumns.size()).append(")");
        }
        if(this.unlocTableName != null)
        {
            if(this.locTableName != null)
            {
                sb.append(",");
            }
            sb.append(this.unlocTableName).append("(").append(this.unlocColumns.size()).append(")");
        }
        return sb.toString();
    }


    public int getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public String getDumpTableName()
    {
        return this.dumpTableName;
    }


    public String getTableName(boolean loc)
    {
        return loc ? this.locTableName : this.unlocTableName;
    }


    public Set<String> getColumnNames(boolean loc)
    {
        return loc ? this.locColumns.keySet() : this.unlocColumns.keySet();
    }


    private ColumnDefintionEntry getColumnEntry(String columnName, boolean loc)
    {
        return loc ? this.locColumns.get(columnName) : this.unlocColumns.get(columnName);
    }


    public Class getColumnDefinition(String columnName, boolean loc)
    {
        ColumnDefintionEntry definitionEntry = getColumnEntry(columnName, loc);
        if(definitionEntry != null)
        {
            return definitionEntry.getDefinition();
        }
        return null;
    }


    public String getColumnPropertyName(String columnName, boolean loc)
    {
        ColumnDefintionEntry definitionEntry = getColumnEntry(columnName, loc);
        if(definitionEntry != null)
        {
            return definitionEntry.getQualifier();
        }
        return null;
    }


    public String getSqlColumnDescription(String columnName, boolean loc)
    {
        ColumnDefintionEntry definitionEntry = getColumnEntry(columnName, loc);
        if(definitionEntry != null)
        {
            return definitionEntry.getSqlColumnDescription();
        }
        return null;
    }


    boolean canAddColumn(String qualifier, String originalColumnName, Class definition, String sqlColumnDescription, boolean loc)
    {
        ColumnDefintionEntry definitionEntry = getColumnEntry(originalColumnName, loc);
        if(definitionEntry == null)
        {
            return true;
        }
        String currentQualifier = definitionEntry.getQualifier();
        if(!currentQualifier.equalsIgnoreCase(qualifier))
        {
            return false;
        }
        Class currentDefinition = definitionEntry.getDefinition();
        String currentSqlDef = definitionEntry.getSqlColumnDescription();
        if(sqlColumnDescription == null)
        {
            return definition.equals(currentDefinition);
        }
        if(currentSqlDef != null)
        {
            return currentSqlDef.equalsIgnoreCase(sqlColumnDescription);
        }
        return definition.equals(currentDefinition);
    }


    private boolean isColumnQualifierAppropriate(String originalColumnName)
    {
        return (originalColumnName.length() <= 30);
    }


    String addColumn(String qualifier, String columnName, Class definition, String sqlColumnDescription, boolean loc, boolean trimColumnName)
    {
        String realColumn;
        ColumnDefintionEntry current;
        if(trimColumnName)
        {
            realColumn = getAdjustedColumnName(qualifier, columnName, definition, sqlColumnDescription, loc);
            current = getColumnEntry(realColumn, loc);
        }
        else
        {
            realColumn = columnName;
            current = null;
        }
        if(current == null)
        {
            ColumnDefintionEntry newOne = new ColumnDefintionEntry(this, qualifier, definition, sqlColumnDescription);
            if(loc)
            {
                this.locColumns.put(realColumn, newOne);
            }
            else
            {
                this.unlocColumns.put(realColumn, newOne);
            }
        }
        else if(current.getSqlColumnDescription() == null)
        {
            ColumnDefintionEntry newOne = new ColumnDefintionEntry(this, current.getQualifier(), current.getDefinition(), sqlColumnDescription);
            if(loc)
            {
                this.locColumns.put(realColumn, newOne);
            }
            else
            {
                this.unlocColumns.put(realColumn, newOne);
            }
        }
        return realColumn;
    }


    private String getAdjustedColumnName(String qualifier, String originalColumnName, Class definition, String sqlColumnDescription, boolean localized)
    {
        if(!isColumnQualifierAppropriate(originalColumnName) ||
                        !canAddColumn(qualifier, originalColumnName, definition, sqlColumnDescription, localized))
        {
            int toLength = Math.min(30, originalColumnName.length());
            String cutVersion = originalColumnName.substring(0, toLength);
            int i = 0;
            while(!canAddColumn(qualifier, cutVersion, definition, sqlColumnDescription, localized))
            {
                String postFix = Integer.toString(i++);
                cutVersion = originalColumnName.substring(0, toLength - postFix.length()) + originalColumnName.substring(0, toLength - postFix.length());
            }
            return cutVersion;
        }
        return originalColumnName;
    }
}
