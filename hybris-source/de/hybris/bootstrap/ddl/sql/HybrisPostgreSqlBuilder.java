package de.hybris.bootstrap.ddl.sql;

import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.model.YColumn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.SqlBuilder;
import org.apache.ddlutils.platform.postgresql.PostgreSqlBuilder;

public class HybrisPostgreSqlBuilder extends PostgreSqlBuilder implements TableStructureChangesProcessor
{
    private static final String ESCAPE_STRING_STARTER = "E";
    private static final String BLOB_STRING_STARTER = "\\\\x";
    private final SqlTransformersRegistry transformersRegistry;
    private final DatabaseSettings databaseSettings;
    private final TableStructureChangesAdjuster tableStructureChangesAdjuster;


    public HybrisPostgreSqlBuilder(Platform platform, DatabaseSettings databaseSettings)
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
        Set<SqlValueTransformer> result = new HashSet<>();
        result.add(new TextSqlValueTransformer(this));
        result.add(new BlobSqlValueTransformer(this));
        result.add(new TimeStampValueTransformer(this));
        return result;
    }


    public String getColumnName(Column column)
    {
        return column.getName();
    }


    public String getTableName(Table table)
    {
        return DDLGeneratorUtils.getTableName(table, (SqlBuilder)this, this.databaseSettings, false);
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


    public void processTableStructureChanges(Database currentModel, Database desiredModel, Table sourceTable, Table targetTable, Map parameters, List changes) throws IOException
    {
        ArrayList<TableChange> addColumnChanges = new ArrayList();
        for(Iterator<TableChange> iterator = changes.iterator(); iterator.hasNext(); )
        {
            TableChange change = iterator.next();
            if(change instanceof AddColumnChange)
            {
                addColumnChanges.add(change);
                iterator.remove();
            }
        }
        for(ListIterator<TableChange> changeIt = addColumnChanges.listIterator(addColumnChanges.size()); changeIt.hasPrevious(); )
        {
            AddColumnChange addColumnChange = (AddColumnChange)changeIt.previous();
            processChange(currentModel, desiredModel, addColumnChange);
            changeIt.remove();
        }
    }


    protected void processTableStructureChanges(Database currentModel, Database desiredModel, String tableName, Map parameters, List changes) throws IOException
    {
        this.tableStructureChangesAdjuster.processTableStructureChanges(currentModel, desiredModel, tableName, parameters, changes);
    }


    protected void processChange(Database currentModel, Database desiredModel, AddColumnChange change) throws IOException
    {
        print("ALTER TABLE ");
        printlnIdentifier(getTableName(change.getChangedTable()));
        printIndent();
        print("ADD COLUMN ");
        writeColumn(change.getChangedTable(), change.getNewColumn());
        printEndOfStatement();
        change.apply(currentModel, getPlatform().isDelimitedIdentifierModeOn());
    }


    public void dropTable(Table table) throws IOException
    {
        print("DROP TABLE IF EXISTS ");
        printIdentifier(getTableName(table));
        printEndOfStatement();
        Column[] columns = table.getAutoIncrementColumns();
        for(int idx = 0; idx < columns.length; idx++)
        {
            print("DROP SEQUENCE IF EXISTS ");
            printIdentifier(getConstraintName(null, table, columns[idx].getName(), "seq"));
            printEndOfStatement();
        }
    }
}
