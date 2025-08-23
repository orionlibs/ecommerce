package de.hybris.bootstrap.ddl.adjusters;

import de.hybris.bootstrap.ddl.SchemaAdjuster;
import de.hybris.bootstrap.ddl.model.YDbModel;
import java.util.Objects;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;

public class RemoveIndicesFromDbModel implements SchemaAdjuster
{
    private final YDbModel dbModel;


    public RemoveIndicesFromDbModel(YDbModel dbModel)
    {
        Objects.requireNonNull(dbModel);
        this.dbModel = dbModel;
    }


    public void adjust()
    {
        for(Table table : this.dbModel.getTables().values())
        {
            for(Index index : table.getIndices())
            {
                table.removeIndex(index);
            }
        }
    }
}
