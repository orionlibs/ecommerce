package de.hybris.bootstrap.ddl.sql;

import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.model.YColumn;
import de.hybris.bootstrap.util.LocaleHelper;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import org.apache.ddlutils.platform.oracle.Oracle10Builder;

public class HybrisOracleBuilder extends Oracle10Builder implements TableStructureChangesProcessor
{
    private static final String ORACLE_DATA_TABLESPACE = "oracle.dataTS";
    private static final String ORACLE_INDEX_TABLESPACE = "oracle.indexTS";
    private final HashSet<String> indexNames = new HashSet<>();
    private final DatabaseSettings databaseSettings;
    private final Map<String, String> hexToRawFunctions;
    private final SqlTransformersRegistry transformersRegistry;
    private final TableStructureChangesAdjuster tableStructureChangesAdjuster;


    public HybrisOracleBuilder(Platform platform, DatabaseSettings databaseSettings)
    {
        super(platform);
        this.tableStructureChangesAdjuster = new TableStructureChangesAdjuster(this);
        this.databaseSettings = databaseSettings;
        this.hexToRawFunctions = new LinkedHashMap<>();
        this.transformersRegistry = new SqlTransformersRegistry(registerValueTransformers());
        addEscapedCharSequence("%", "%");
        addEscapedCharSequence("_", "_");
    }


    private Set<SqlValueTransformer> registerValueTransformers()
    {
        Set<SqlValueTransformer> result = new HashSet<>(3);
        result.add(new BlobSqlValueTransformer(this, this));
        result.add(new NumericSqlValueTransformer(this));
        result.add(new TimeDateSqlValueTransformer(this));
        return result;
    }


    public void addHexToRawFunction(String funcName, String funcValue)
    {
        this.hexToRawFunctions.put(funcName, funcValue);
    }


    public void dropTable(Table table) throws IOException
    {
        Column[] columns = table.getAutoIncrementColumns();
        for(Column column : columns)
        {
            dropAutoIncrementTrigger(table, column);
            dropAutoIncrementSequence(table, column);
        }
        println("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + getTableName(table) + " CASCADE CONSTRAINTS PURGE'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -942 THEN RAISE; END IF; END;");
        println("/");
        println(this.databaseSettings.getStatementDelimiter());
    }


    protected void dropAutoIncrementTrigger(Table table, Column column) throws IOException
    {
        println("BEGIN EXECUTE IMMEDIATE 'DROP TRIGGER " + getConstraintName("trg", table, column.getName(), null) + " '; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -4080 THEN RAISE; END IF; END;");
        println("/");
        println(this.databaseSettings.getStatementDelimiter());
    }


    protected void dropAutoIncrementSequence(Table table, Column column) throws IOException
    {
        println("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE " + getConstraintName("seq", table, column.getName(), null) + "'; EXCEPTION WHEN OTHERS THEN IF SQLCODE != -2289 THEN RAISE; END IF; END;");
        println("/");
        println(this.databaseSettings.getStatementDelimiter());
    }


    public String getTableName(Table table)
    {
        return DDLGeneratorUtils.getTableName(table, (SqlBuilder)this, this.databaseSettings, false);
    }


    public void setIndexNames(Set<String> indexes)
    {
        this.indexNames.addAll(indexes);
    }


    public String getIndexName(Index index)
    {
        return DDLGeneratorUtils.getIndexName(index, (SqlBuilder)this, this.databaseSettings, false);
    }


    public String createIndexName(Index index)
    {
        String indexName = getIndexName(index);
        String indexNameUpperCase = indexName.toUpperCase(LocaleHelper.getPersistenceLocale());
        if(this.indexNames.contains(indexNameUpperCase))
        {
            boolean isNewNameGiven = false;
            int counter = 1;
            while(!isNewNameGiven)
            {
                indexName = indexName.substring(0, indexName.length() - String.valueOf(counter).length());
                indexName = indexName.concat(String.valueOf(counter));
                indexNameUpperCase = indexName.toUpperCase(LocaleHelper.getPersistenceLocale());
                if(!this.indexNames.contains(indexNameUpperCase))
                {
                    isNewNameGiven = true;
                    continue;
                }
                counter++;
            }
        }
        this.indexNames.add(indexNameUpperCase);
        return indexName;
    }


    public String getColumnName(Column column)
    {
        return DDLGeneratorUtils.getColumnName(column, (SqlBuilder)this);
    }


    public void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException
    {
        super.processTableStructureChanges(currentModel, desiredModel, sourceTable, targetTable, parameters, changes);
    }


    protected void processTableStructureChanges(Database currentModel, Database desiredModel, String tableName, Map parameters, List changes) throws IOException
    {
        this.tableStructureChangesAdjuster.processTableStructureChanges(currentModel, desiredModel, tableName, parameters, changes);
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


    protected void printEndOfStatement() throws IOException
    {
        super.printEndOfStatement();
        println();
        print(this.databaseSettings.getStatementDelimiter());
        println();
    }


    public void createTable(Database database, Table table, Map parameters) throws IOException
    {
        Column[] columns = table.getAutoIncrementColumns();
        int idx;
        for(idx = 0; idx < columns.length; idx++)
        {
            createAutoIncrementSequence(table, columns[idx]);
        }
        writeTableCreationStmt(database, table, parameters);
        writeTableCreationStmtEnding(table, parameters);
        if(!getPlatformInfo().isPrimaryKeyEmbedded())
        {
            writeExternalPrimaryKeysCreateStmt(table, table.getPrimaryKeyColumns());
        }
        if(!getPlatformInfo().isIndicesEmbedded())
        {
            writeExternalIndicesCreateStmt(table);
        }
        for(idx = 0; idx < columns.length; idx++)
        {
            createAutoIncrementTrigger(table, columns[idx]);
        }
    }


    protected void createAutoIncrementTrigger(Table table, Column column) throws IOException
    {
        String columnName = getColumnName(column);
        String triggerName = getConstraintName("trg", table, column.getName(), null);
        print("CREATE OR REPLACE TRIGGER ");
        printlnIdentifier(triggerName);
        print("BEFORE INSERT ON ");
        printlnIdentifier(getTableName(table));
        print("FOR EACH ROW WHEN (new.");
        printIdentifier(columnName);
        println(" IS NULL)");
        println("BEGIN");
        print("  SELECT ");
        printIdentifier(getConstraintName("seq", table, column.getName(), null));
        print(".nextval INTO :new.");
        printIdentifier(columnName);
        print(" FROM dual");
        println(getPlatformInfo().getSqlCommandDelimiter());
        print("END");
        println(getPlatformInfo().getSqlCommandDelimiter());
        println("/");
        println(this.databaseSettings.getStatementDelimiter());
    }


    protected void writeTableCreationStmt(Database database, Table table, Map parameters) throws IOException
    {
        print("CREATE TABLE ");
        printlnIdentifier(getTableName(table));
        println("(");
        writeColumns(table);
        if(getPlatformInfo().isPrimaryKeyEmbedded())
        {
            writeEmbeddedPrimaryKeysStmt(table);
        }
        if(getPlatformInfo().isForeignKeysEmbedded())
        {
            writeEmbeddedForeignKeysStmt(database, table);
        }
        if(getPlatformInfo().isIndicesEmbedded())
        {
            writeEmbeddedIndicesStmt(table);
        }
        println();
        print(")");
        String dataTs = this.databaseSettings.getProperty("oracle.dataTS");
        if(StringUtils.isNotBlank(dataTs))
        {
            print(" TABLESPACE " + dataTs);
        }
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
                printIdentifier(createIndexName(index));
                print(" ON ");
                printIdentifier(getTableName(table));
                print(" (");
                for(int idx = 0; idx < index.getColumnCount(); idx++)
                {
                    IndexColumn idxColumn = index.getColumn(idx);
                    Column col = table.findColumn(idxColumn.getName());
                    if(col == null)
                    {
                        throw new ModelException("Invalid column '" + idxColumn
                                        .getName() + "' on index " + index.getName() + " for table " + table
                                        .getName());
                    }
                    if(idx > 0)
                    {
                        print(", ");
                    }
                    if(isFunctionalIndexColumn(idxColumn))
                    {
                        print(getColumnFunction(idxColumn, getColumnName(col)));
                    }
                    else
                    {
                        printIdentifier(getColumnName(col));
                    }
                }
                print(")");
                String indexTableSpaceName = this.databaseSettings.getProperty("oracle.indexTS");
                if(StringUtils.isNotBlank(indexTableSpaceName))
                {
                    print(" TABLESPACE " + indexTableSpaceName);
                }
                if(isOnlineIndex(index))
                {
                    print(" ONLINE");
                }
                printEndOfStatement();
            }
        }
    }


    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException
    {
        boolean isRemoved = this.indexNames.remove(getIndexName(index).toUpperCase(LocaleHelper.getPersistenceLocale()));
        if(!isRemoved)
        {
            this._log.debug("Cannot remove indexName from list: " + index.getName());
        }
        super.writeExternalIndexDropStmt(table, index);
    }


    private boolean isOnlineIndex(Index index)
    {
        if(index instanceof DbAwareIndex)
        {
            return ((DbAwareIndex)index).isOnline();
        }
        return false;
    }


    private boolean isFunctionalIndexColumn(IndexColumn column)
    {
        return column instanceof DefaultFunctionalIndexColumn;
    }


    private String getColumnFunction(IndexColumn idxColumn, String columnName) throws IOException
    {
        StringBuilder sb = new StringBuilder(((DefaultFunctionalIndexColumn)idxColumn).getFunctionName());
        sb.append('(').append(columnName).append(')');
        return sb.toString();
    }


    public String getUpdateSql(Table table, Map columnValues, boolean genPlaceholders)
    {
        String updateSql = super.getUpdateSql(table, columnValues, genPlaceholders);
        return getOrWrapStatementWithPlsql(updateSql);
    }


    public String getInsertSql(Table table, Map columnValues, boolean genPlaceholders)
    {
        String insertSql = super.getInsertSql(table, columnValues, genPlaceholders);
        return getOrWrapStatementWithPlsql(insertSql);
    }


    private String getOrWrapStatementWithPlsql(String originalStatement)
    {
        StringBuilder sb = new StringBuilder();
        if(isQueryNeedsPsql())
        {
            try
            {
                sb.append(getPsqlDeclarations());
                sb.append(getPsqlFuncAssignments());
                sb.append(originalStatement).append(";");
                sb.append(getPsqlClosing());
                return sb.toString();
            }
            finally
            {
                this.hexToRawFunctions.clear();
            }
        }
        sb.append(originalStatement).append("\n");
        sb.append(this.databaseSettings.getStatementDelimiter());
        return sb.toString();
    }


    private boolean isQueryNeedsPsql()
    {
        return !this.hexToRawFunctions.isEmpty();
    }


    private String getPsqlDeclarations()
    {
        StringBuilder sb = new StringBuilder("DECLARE ");
        for(String funcName : this.hexToRawFunctions.keySet())
        {
            sb.append(funcName).append(" RAW(32767);").append("\n");
        }
        return sb.toString();
    }


    private String getPsqlFuncAssignments()
    {
        StringBuilder sb = new StringBuilder(" BEGIN ");
        sb.append("\n");
        for(Map.Entry<String, String> entry : this.hexToRawFunctions.entrySet())
        {
            sb.append(entry.getKey()).append(" := ").append(entry.getValue());
            sb.append(";").append("\n");
        }
        return sb.toString();
    }


    private String getPsqlClosing()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("END;").append("\n");
        sb.append("/").append(this.databaseSettings.getStatementDelimiter());
        return sb.toString();
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
}
