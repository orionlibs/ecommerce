package de.hybris.bootstrap.ddl.sql;

import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.model.YColumn;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.hsqldb.HsqlDbBuilder;

public class HybrisHsqlDbSqlBuilder extends HsqlDbBuilder implements TableStructureChangesProcessor
{
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
    private final DatabaseSettings databaseSettings;
    private final SqlTransformersRegistry transformersRegistry;
    private final boolean useCachedTables;
    private final TableStructureChangesAdjuster tableStructureChangesAdjuster;


    public HybrisHsqlDbSqlBuilder(Platform platform, DatabaseSettings databaseSettings)
    {
        super(platform);
        this.tableStructureChangesAdjuster = new TableStructureChangesAdjuster(this);
        this.databaseSettings = databaseSettings;
        this.transformersRegistry = new SqlTransformersRegistry(registerValueTransformers());
        this.useCachedTables = Boolean.parseBoolean(databaseSettings.getProperty("hsqldb.usecachedtables", "false"));
        addEscapedCharSequence("%", "%");
        addEscapedCharSequence("_", "_");
    }


    private Set<SqlValueTransformer> registerValueTransformers()
    {
        Set<SqlValueTransformer> result = new HashSet<>(3);
        result.add(new BlobSqlValueTransformer(this));
        result.add(new TimeStampSqlValueTransformer(this));
        return result;
    }


    public String getTableName(Table table)
    {
        return DDLGeneratorUtils.getTableName(table, (SqlBuilder)this, this.databaseSettings, false);
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


    protected void writeTableCreationStmt(Database database, Table table, Map parameters) throws IOException
    {
        print("CREATE " + (this.useCachedTables ? "CACHED" : "") + " TABLE ");
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


    public String getIndexName(Index index)
    {
        return DDLGeneratorUtils.getIndexName(index, (SqlBuilder)this, this.databaseSettings, false);
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


    public void writeExternalIndexDropStmt(Table table, Index index) throws IOException
    {
        print("DROP INDEX \"");
        printIdentifier(getIndexName(index));
        print("\"");
        printEndOfStatement();
    }
}
