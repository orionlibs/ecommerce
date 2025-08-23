package de.hybris.bootstrap.ddl.sql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.model.YColumn;
import de.hybris.bootstrap.util.LocaleHelper;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.ModelException;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.log4j.Logger;

public class HanaSqlBuilder extends SqlBuilder implements TableStructureChangesProcessor
{
    private static final Logger LOG = Logger.getLogger(HanaSqlBuilder.class);
    public static final String HANADB_STORAGE_COLUMNBASED = "hanadb.storage.columnbased";
    public static final String HANADB_STORAGE_OPTIMIZED_INDEXES = "hanadb.storage.optimized.indexes";
    private static final String TABLE_TYPE_PROPERTY = "deployment.hana.tabletype";
    private static final String COLUMN_BASED_TYPE = "\"COLUMN\"";
    private static final String ROW_BASED_TYPE = "\"ROW\"";
    private static final ColumnNativeTypeDecorator DO_NOT_DECORATE_NATIVE_TYPE = (ColumnNativeTypeDecorator)new Object();
    private final Set<String> indexNames = new HashSet<>();
    private final DatabaseSettings databaseSettings;
    private final boolean globalColumnBased;
    private final boolean createOnlyUniqueIndexes;
    private final SqlTransformersRegistry transformersRegistry;
    private final Collection<ColumnNativeTypeDecorator> columnNativeTypeDecorators;
    private final TableStructureChangesAdjuster tableStructureChangesAdjuster;
    private Map parametersForTable;


    public HanaSqlBuilder(Platform platform, DatabaseSettings databaseSettings, Iterable<ColumnNativeTypeDecorator> columnNativeTypeDecorators)
    {
        super(platform);
        this.tableStructureChangesAdjuster = new TableStructureChangesAdjuster(this);
        this.databaseSettings = databaseSettings;
        this.transformersRegistry = new SqlTransformersRegistry(registerValueTransformers());
        this.globalColumnBased = Boolean.parseBoolean(databaseSettings.getProperty("hanadb.storage.columnbased", "true"));
        this.createOnlyUniqueIndexes = Boolean.parseBoolean(databaseSettings.getProperty("hanadb.storage.optimized.indexes", "true"));
        addEscapedCharSequence("'", "''");
        if(this.globalColumnBased && !this.createOnlyUniqueIndexes)
        {
            LOG.info("It is highly recommended to avoid creation of the additional indices for the table column storage. In order to achieve it, set the 'hanadb.storage.optimized.indexes' property to true ");
        }
        this
                        .columnNativeTypeDecorators = (columnNativeTypeDecorators == null) ? (Collection<ColumnNativeTypeDecorator>)ImmutableList.of() : (Collection<ColumnNativeTypeDecorator>)ImmutableList.copyOf(columnNativeTypeDecorators);
    }


    private Set<SqlValueTransformer> registerValueTransformers()
    {
        Set<SqlValueTransformer> result = new HashSet<>(2);
        result.add(new BlobSqlValueTransformer(this));
        result.add(new TimeDateSqlValueTransformer(this));
        return result;
    }


    public void dropTable(Table table) throws IOException
    {
        println("DROP TABLE " + getTableName(table) + ";");
    }


    public String getTableName(Table table)
    {
        return DDLGeneratorUtils.getTableName(table, this, this.databaseSettings, false);
    }


    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException
    {
        boolean isRemoved = this.indexNames.remove(getIndexName(index).toUpperCase(LocaleHelper.getPersistenceLocale()));
        if(!isRemoved)
        {
            this._log.debug("Cannot remove indexName from list: " + index.getName());
        }
        print("DROP INDEX \"");
        printIdentifier(getIndexName(index));
        print("\"");
        printEndOfStatement();
    }


    public void setIndexNames(Set<String> indexes)
    {
        this.indexNames.addAll(indexes);
    }


    public String getIndexName(Index index)
    {
        return DDLGeneratorUtils.getIndexName(index, this, this.databaseSettings, false);
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


    protected void writeTableCreationStmt(Database database, Table table, Map parameters) throws IOException
    {
        boolean localColumnBased = isLocalColumnBased(parameters);
        boolean localRowBased = isLocalRowBased(parameters);
        boolean columnBased = (localColumnBased || (this.globalColumnBased && !localRowBased));
        if(columnBased)
        {
            print("CREATE COLUMN TABLE ");
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
        }
        else
        {
            super.writeTableCreationStmt(database, table, parameters);
        }
    }


    private boolean isLocalRowBased(Map parameters)
    {
        return (parameters != null && parameters.containsKey("deployment.hana.tabletype") && parameters
                        .get("deployment.hana.tabletype").equals("\"ROW\""));
    }


    private boolean isLocalColumnBased(Map parameters)
    {
        return (parameters != null && parameters.containsKey("deployment.hana.tabletype") && parameters
                        .get("deployment.hana.tabletype").equals("\"COLUMN\""));
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


    public String getColumnName(Column column)
    {
        return DDLGeneratorUtils.getColumnName(column, this);
    }


    public String getInsertSql(Table table, Map columnValues, boolean genPlaceholders)
    {
        StringBuilder buffer = new StringBuilder(super.getInsertSql(table, columnValues, genPlaceholders));
        buffer.append("\n");
        buffer.append(this.databaseSettings.getStatementDelimiter());
        return buffer.toString();
    }


    protected String getSqlType(Column column)
    {
        String nativeType = getNativeTypeFor(column);
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


    private String getNativeTypeFor(Column column)
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
        ColumnNativeTypeDecorator decorator = this.columnNativeTypeDecorators.stream().filter(d -> d.canBeUsed(column, nativeType)).findFirst().orElse(DO_NOT_DECORATE_NATIVE_TYPE);
        return decorator.decorate(column, nativeType);
    }


    public void processTableStructureChanges(Database currentModel, Database desiredModel, String tableName, Map parameters, List changes) throws IOException
    {
        this.tableStructureChangesAdjuster.processTableStructureChanges(currentModel, desiredModel, tableName, parameters, changes);
    }


    public void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException
    {
        for(Iterator<TableChange> changeIt = changes.iterator(); changeIt.hasNext(); )
        {
            TableChange change = changeIt.next();
            if(change instanceof AddColumnChange)
            {
                AddColumnChange addColumnChange = (AddColumnChange)change;
                if(addColumnChange.isAtEnd())
                {
                    processChange(currentModel, addColumnChange);
                    changeIt.remove();
                }
            }
        }
        super.processTableStructureChanges(currentModel, desiredModel, sourceTable, targetTable, parameters, changes);
    }


    private void processChange(Database currentModel, AddColumnChange addColumnChange) throws IOException
    {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(addColumnChange.getChangedTable()));
        printIndent();
        print("ADD (");
        writeColumn(addColumnChange.getChangedTable(), addColumnChange.getNewColumn());
        print(")");
        printEndOfStatement();
        addColumnChange.apply(currentModel, getPlatform().isDelimitedIdentifierModeOn());
    }


    public void createTable(Database database, Table table, Map parameters) throws IOException
    {
        this.parametersForTable = (parameters == null) ? (Map)ImmutableMap.of() : (Map)ImmutableMap.copyOf(parameters);
        super.createTable(database, table, parameters);
    }


    protected void writeExternalIndexCreateStmt(Table table, Index index) throws IOException
    {
        if(isConfigureToBeCreated(index) && getPlatformInfo().isIndicesSupported())
        {
            if(index.getName() == null)
            {
                this._log.warn("Cannot write unnamed index " + index);
            }
            else
            {
                writeCreateStmt(table, index);
            }
        }
    }


    private void writeCreateStmt(Table table, Index index) throws IOException
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
                                .getName() + "' on index " + index.getName() + " for table " + table.getName());
            }
            if(idx > 0)
            {
                print(", ");
            }
            printIdentifier(getColumnName(col));
        }
        print(")");
        printEndOfStatement();
    }


    private boolean isConfigureToBeCreated(Index index)
    {
        if(this.createOnlyUniqueIndexes && isEffectiveColumnStorage(this.parametersForTable))
        {
            if(index.isUnique() || isForcedToBeCreated(index))
            {
                return true;
            }
        }
        else
        {
            return true;
        }
        return false;
    }


    protected void writeColumnAutoIncrementStmt(Table table, Column column) throws IOException
    {
        print("GENERATED BY DEFAULT AS IDENTITY");
    }


    boolean isForcedToBeCreated(Index index)
    {
        if(index instanceof DbAwareIndex)
        {
            switch(null.$SwitchMap$de$hybris$bootstrap$ddl$sql$IndexCreationMode[((DbAwareIndex)index).getCreationMode().ordinal()])
            {
                case 1:
                    return true;
                case 2:
                    return true;
            }
            return false;
        }
        return false;
    }


    boolean isEffectiveColumnStorage(Map parametersForTable)
    {
        return (!isLocalRowBased(parametersForTable) && this.globalColumnBased);
    }
}
