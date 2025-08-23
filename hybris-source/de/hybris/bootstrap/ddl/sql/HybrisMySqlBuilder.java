package de.hybris.bootstrap.ddl.sql;

import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.model.YColumn;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.ModelException;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.mysql.MySqlBuilder;

public class HybrisMySqlBuilder extends MySqlBuilder implements TableStructureChangesProcessor
{
    private final DatabaseSettings databaseSettings;
    private final SqlTransformersRegistry transformersRegistry;
    private final TableStructureChangesAdjuster tableStructureChangesAdjuster;
    private String tableOptions;


    public HybrisMySqlBuilder(Platform platform, DatabaseSettings databaseSettings)
    {
        super(platform);
        this.tableStructureChangesAdjuster = new TableStructureChangesAdjuster(this);
        this.databaseSettings = databaseSettings;
        this.transformersRegistry = new SqlTransformersRegistry(registerValueTransformers());
        addEscapedCharSequence("%", "%");
        addEscapedCharSequence("_", "_");
    }


    private Set<SqlValueTransformer> registerValueTransformers()
    {
        Set<SqlValueTransformer> result = new HashSet<>(2);
        result.add(new BlobSqlValueTransformer(this));
        result.add(new TimeStampValueTransformer(this));
        return result;
    }


    protected void writeTableCreationStmtEnding(Table table, Map parameters) throws IOException
    {
        if(this.tableOptions == null)
        {
            String engine = this.databaseSettings.getProperty("mysql.tabletype", "InnoDB");
            String additionalParams = this.databaseSettings.getProperty("mysql.optional.tabledefs", "CHARSET=utf8 COLLATE=utf8_bin");
            this.tableOptions = "ENGINE " + engine + " " + additionalParams;
        }
        print(this.tableOptions);
        super.writeTableCreationStmtEnding(table, null);
    }


    public String getTableName(Table table)
    {
        return DDLGeneratorUtils.getTableName(table, (SqlBuilder)this, this.databaseSettings, false);
    }


    public String getIndexName(Index index)
    {
        return DDLGeneratorUtils.getIndexName(index, (SqlBuilder)this, this.databaseSettings, false);
    }


    public String getColumnName(Column column)
    {
        return column.getName();
    }


    public void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException
    {
        super.processTableStructureChanges(currentModel, desiredModel, sourceTable, targetTable, parameters, changes);
    }


    protected void processTableStructureChanges(Database currentModel, Database desiredModel, String tableName, Map parameters, List changes) throws IOException
    {
        this.tableStructureChangesAdjuster.processTableStructureChanges(currentModel, desiredModel, tableName, parameters, changes);
    }


    public String getInsertSql(Table table, Map columnValues, boolean genPlaceholders)
    {
        StringBuilder buffer = new StringBuilder("INSERT INTO ");
        boolean addComma = false;
        buffer.append(getDelimitedIdentifier(getTableName(table)));
        buffer.append(" (");
        int idx;
        for(idx = 0; idx < table.getColumnCount(); idx++)
        {
            Column column = table.getColumn(idx);
            if(columnValues.containsKey(column.getName()))
            {
                if(addComma)
                {
                    buffer.append(", ");
                }
                buffer.append(getDelimitedIdentifier(getColumnName(column)));
                addComma = true;
            }
        }
        buffer.append(") VALUES (");
        if(genPlaceholders)
        {
            addComma = false;
            for(idx = 0; idx < columnValues.size(); idx++)
            {
                if(addComma)
                {
                    buffer.append(", ");
                }
                buffer.append("?");
                addComma = true;
            }
        }
        else
        {
            addComma = false;
            for(idx = 0; idx < table.getColumnCount(); idx++)
            {
                Column column = table.getColumn(idx);
                if(columnValues.containsKey(column.getName()))
                {
                    if(addComma)
                    {
                        buffer.append(", ");
                    }
                    buffer.append(getValueAsString(column, columnValues.get(column.getName())));
                    addComma = true;
                }
            }
        }
        buffer.append(")");
        return buffer.toString();
    }


    protected String getValueAsString(Column column, Object value)
    {
        SqlValueTransformer sqlValueTransformer = this.transformersRegistry.getValueTransformerFor(column, value);
        if(sqlValueTransformer == null)
        {
            return super.getValueAsString(column, value);
        }
        return sqlValueTransformer.transform(column, value);
    }


    protected void writeExternalIndexCreateStmt(Table table, Index index) throws IOException
    {
        if(getPlatformInfo().isIndicesSupported())
        {
            if(index.getName() == null)
            {
                this._log.warn("Cannot write unnamed index " + index);
            }
            else
            {
                print("CREATE");
                if(index.isUnique())
                {
                    print(" UNIQUE");
                }
                print(" INDEX ");
                printIdentifier(getIndexName(index));
                print(" ON ");
                printIdentifier(getTableName(table));
                print(" (");
                for(int idx = 0; idx < index.getColumnCount(); idx++)
                {
                    IndexColumn idxColumn = index.getColumn(idx);
                    Column col = table.findColumn(idxColumn.getName());
                    if(col == null)
                    {
                        throw new ModelException("Invalid column '" + idxColumn.getName() + "' on index " + index.getName() + " for table " + table
                                        .getName());
                    }
                    if(idx > 0)
                    {
                        print(", ");
                    }
                    if(needsSizedIndex(col))
                    {
                        String columnWithSize = getColumnName(col) + "(255)";
                        printIdentifier(columnWithSize);
                    }
                    else
                    {
                        printIdentifier(getColumnName(col));
                    }
                }
                print(")");
                printEndOfStatement();
            }
        }
    }


    protected String getSqlType(Column column)
    {
        String nativeType;
        if(column instanceof YColumn)
        {
            YColumn yColumn = (YColumn)column;
            nativeType = StringUtils.isNotEmpty(yColumn.getCustomColumnDefinition()) ? yColumn.getCustomColumnDefinition() : getNativeType(column);
        }
        else
        {
            nativeType = getNativeType(column);
        }
        int sizePos = nativeType.indexOf("{0}");
        StringBuilder sqlType = new StringBuilder();
        sqlType.append((sizePos >= 0) ? nativeType.substring(0, sizePos) : nativeType);
        Object sizeSpec = column.getSize();
        if(sizeSpec == null)
        {
            sizeSpec = getPlatformInfo().getDefaultSize(column.getTypeCode());
        }
        if(sizeSpec != null)
        {
            if(getPlatformInfo().hasSize(column.getTypeCode()))
            {
                sqlType.append("(");
                sqlType.append(sizeSpec.toString());
                sqlType.append(")");
            }
            else if(getPlatformInfo().hasPrecisionAndScale(column.getTypeCode()))
            {
                sqlType.append("(");
                sqlType.append(column.getSizeAsInt());
                sqlType.append(",");
                sqlType.append(column.getScale());
                sqlType.append(")");
            }
        }
        sqlType.append((sizePos >= 0) ? nativeType.substring(sizePos + "{0}".length()) : "");
        return sqlType.toString();
    }


    private boolean needsSizedIndex(Column col)
    {
        int typeCode = col.getTypeCode();
        if(typeCode == -1 || typeCode == -4 || typeCode == 2005 || typeCode == 2004 || typeCode == 12000 || typeCode == 12001)
        {
            return true;
        }
        return false;
    }
}
