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
import org.apache.ddlutils.platform.mssql.MSSqlBuilder;

public class HybrisMSSqlBuilder extends MSSqlBuilder implements TableStructureChangesProcessor
{
    private final SqlTransformersRegistry transformersRegistry;
    private final DatabaseSettings databaseSettings;
    private final TableStructureChangesAdjuster tableStructureChangesAdjuster;


    public HybrisMSSqlBuilder(Platform platform, DatabaseSettings databaseSettings)
    {
        super(platform);
        this.tableStructureChangesAdjuster = new TableStructureChangesAdjuster(this);
        this.databaseSettings = databaseSettings;
        this.transformersRegistry = new SqlTransformersRegistry(registerValueTransformers());
        addEscapedCharSequence("%", "%");
        addEscapedCharSequence("_", "_");
    }


    public void dropTable(Table table) throws IOException
    {
        String tableName = getTableName(table);
        String tableNameVar = "tn" + createUniqueIdentifier();
        String constraintNameVar = "cn" + createUniqueIdentifier();
        writeQuotationOnStatement();
        print("IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = ");
        printAlwaysSingleQuotedIdentifier(tableName);
        println(")");
        println("BEGIN");
        println("  DECLARE @" + tableNameVar + " nvarchar(256), @" + constraintNameVar + " nvarchar(256)");
        println("  DECLARE refcursor CURSOR FOR");
        println("  SELECT object_name(fks.parent_object_id) tablename, fks.name constraintname");
        println("    FROM sys.foreign_keys fks");
        print("    WHERE object_id(");
        printAlwaysSingleQuotedIdentifier(tableName);
        println(") in (fks.parent_object_id, fks.referenced_object_id)");
        println("  OPEN refcursor");
        println("  FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
        println("  WHILE @@FETCH_STATUS = 0");
        println("    BEGIN");
        println("      EXEC ('ALTER TABLE '+@" + tableNameVar + "+' DROP CONSTRAINT '+@" + constraintNameVar + ")");
        println("      FETCH NEXT FROM refcursor INTO @" + tableNameVar + ", @" + constraintNameVar);
        println("    END");
        println("  CLOSE refcursor");
        println("  DEALLOCATE refcursor");
        print("  DROP TABLE ");
        printlnIdentifier(tableName);
        print("END");
        printEndOfStatement();
    }


    private String getQuotationOnStatement()
    {
        if(getPlatform().isDelimitedIdentifierModeOn())
        {
            return "SET quoted_identifier on" + getPlatformInfo().getSqlCommandDelimiter() + "\n";
        }
        return "";
    }


    private void writeQuotationOnStatement() throws IOException
    {
        print(getQuotationOnStatement());
    }


    private void printAlwaysSingleQuotedIdentifier(String identifier) throws IOException
    {
        print("'");
        print(identifier);
        print("'");
    }


    private Set<SqlValueTransformer> registerValueTransformers()
    {
        Set<SqlValueTransformer> result = new HashSet<>(3);
        result.add(new BlobSqlValueTransformer(this));
        result.add(new TimeDateSqlValueTransformer(this));
        return result;
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


    public String getIndexName(Index index)
    {
        return DDLGeneratorUtils.getIndexName(index, (SqlBuilder)this, this.databaseSettings, false);
    }


    public String getTableName(Table table)
    {
        return DDLGeneratorUtils.getTableName(table, (SqlBuilder)this, this.databaseSettings, false);
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


    private void writeCoreIndexCreateStmt(Table table, Index index) throws IOException
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
                Column col = getColumnForIndexColumn(idxColumn, table, index.getName());
                if(idx > 0)
                {
                    print(", ");
                }
                printIdentifier(getColumnName(col));
            }
            print(")");
        }
    }


    private Column getColumnForIndexColumn(IndexColumn idxColumn, Table table, String indexName)
    {
        Column col = table.findColumn(idxColumn.getName());
        if(col == null)
        {
            throw new ModelException("Invalid column '" + idxColumn
                            .getName() + "' on index " + indexName + " for table " + table.getName());
        }
        return col;
    }


    protected void writeExternalIndexCreateStmt(Table table, Index index) throws IOException
    {
        if(getPlatformInfo().isIndicesSupported())
        {
            writeCoreIndexCreateStmt(table, index);
            if(index instanceof ExtendedAwareIndex)
            {
                writeEnhancedIndexCreateStmt(table, index.getName(), (ExtendedAwareIndex)index);
            }
            printEndOfStatement();
        }
    }


    private void writeEnhancedIndexCreateStmt(Table table, String indexName, ExtendedAwareIndex index) throws IOException
    {
        ExtendedParamsForIndex columns = index.getExtendedParams();
        if(columns instanceof MSSqlExtendedParamsForIndex)
        {
            MSSqlExtendedParamsForIndex inclColumns = (MSSqlExtendedParamsForIndex)columns;
            if(inclColumns.getIncludeColumnCollection() != null && !inclColumns.getIncludeColumnCollection().isEmpty())
            {
                print(" INCLUDE (");
                boolean first = true;
                for(IndexColumn idxCol : inclColumns.getIncludeColumnCollection())
                {
                    Column col = getColumnForIndexColumn(idxCol, table, indexName);
                    if(!first)
                    {
                        print(", ");
                    }
                    printIdentifier(getColumnName(col));
                    first = false;
                }
                print(")");
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
}
