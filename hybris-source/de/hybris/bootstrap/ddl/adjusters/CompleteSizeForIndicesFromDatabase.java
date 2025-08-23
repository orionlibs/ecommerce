package de.hybris.bootstrap.ddl.adjusters;

import de.hybris.bootstrap.ddl.SchemaAdjuster;
import java.util.Objects;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

public class CompleteSizeForIndicesFromDatabase extends CompleteSizeForIndices implements SchemaAdjuster
{
    private final Database database;


    public CompleteSizeForIndicesFromDatabase(Database database)
    {
        Objects.requireNonNull(database);
        this.database = database;
    }


    public void adjust()
    {
        for(Table table : this.database.getTables())
        {
            adjustIndexColumnSizeIfNeeded(table);
        }
    }
}
