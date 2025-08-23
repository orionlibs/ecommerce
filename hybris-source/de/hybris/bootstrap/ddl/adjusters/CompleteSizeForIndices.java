package de.hybris.bootstrap.ddl.adjusters;

import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.Table;

public abstract class CompleteSizeForIndices
{
    protected void adjustIndexColumnSizeIfNeeded(Table table)
    {
        for(Index index : table.getIndices())
        {
            for(IndexColumn col : index.getColumns())
            {
                if(col.getSize() == null && col.getColumn() != null)
                {
                    col.setSize(col.getColumn().getSize());
                }
            }
        }
    }
}
