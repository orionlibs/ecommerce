package de.hybris.bootstrap.ddl.adjusters;

import de.hybris.bootstrap.ddl.SchemaAdjuster;
import de.hybris.bootstrap.ddl.model.YDbModel;
import java.util.Objects;
import org.apache.ddlutils.model.Table;

public class CompleteSizeForIndicesFromDBModel extends CompleteSizeForIndices implements SchemaAdjuster
{
    private final YDbModel dbModel;


    public CompleteSizeForIndicesFromDBModel(YDbModel dbModel)
    {
        Objects.requireNonNull(dbModel);
        this.dbModel = dbModel;
    }


    public void adjust()
    {
        for(Table table : this.dbModel.getTables().values())
        {
            adjustIndexColumnSizeIfNeeded(table);
        }
    }
}
