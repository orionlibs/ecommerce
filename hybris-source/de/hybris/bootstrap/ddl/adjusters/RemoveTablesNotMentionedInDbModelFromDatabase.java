package de.hybris.bootstrap.ddl.adjusters;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.ddl.DDLGeneratorUtils;
import de.hybris.bootstrap.ddl.SchemaAdjuster;
import de.hybris.bootstrap.ddl.model.YDbModel;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Objects;
import java.util.Set;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class RemoveTablesNotMentionedInDbModelFromDatabase implements SchemaAdjuster
{
    private final YDbModel dbModel;
    private final Database database;
    private final String tablePrefix;


    public RemoveTablesNotMentionedInDbModelFromDatabase(YDbModel dbModel, Database database, String tablePrefix)
    {
        Objects.requireNonNull(dbModel);
        Objects.requireNonNull(database);
        this.dbModel = dbModel;
        this.database = database;
        this.tablePrefix = tablePrefix;
    }


    public void adjust()
    {
        Set<String> dbModelTableNames = getDbModelTableNames();
        for(Table table : this.database.getTables())
        {
            if(!dbModelTableNames.contains(table.getName().toLowerCase(LocaleHelper.getPersistenceLocale())))
            {
                this.database.removeTable(table);
            }
        }
    }


    private Set<String> getDbModelTableNames()
    {
        ImmutableSet.Builder resultBuilder = ImmutableSet.builder();
        for(Table table : this.dbModel.getTables().values())
        {
            resultBuilder.add(DDLGeneratorUtils.adjustForTablePrefix(table.getName(), this.tablePrefix).toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
        return (Set<String>)resultBuilder.build();
    }
}
