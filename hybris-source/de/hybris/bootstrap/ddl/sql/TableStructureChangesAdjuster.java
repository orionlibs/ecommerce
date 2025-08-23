package de.hybris.bootstrap.ddl.sql;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.TableChange;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;

final class TableStructureChangesAdjuster
{
    private final TableStructureChangesProcessor tableStructureChangesProcessor;


    TableStructureChangesAdjuster(TableStructureChangesProcessor inter)
    {
        this.tableStructureChangesProcessor = inter;
    }


    void processTableStructureChanges(Database currentModel, Database desiredModel, String tableName, Map parameters, List changes) throws IOException
    {
        Table sourceTable = currentModel.findTable(tableName, this.tableStructureChangesProcessor
                        .getPlatform()
                        .isDelimitedIdentifierModeOn());
        Table targetTable = desiredModel.findTable(tableName, this.tableStructureChangesProcessor
                        .getPlatform()
                        .isDelimitedIdentifierModeOn());
        boolean requiresFullRebuild = false;
        for(Iterator<TableChange> changeIt = changes.iterator(); !requiresFullRebuild && changeIt.hasNext(); )
        {
            TableChange change = changeIt.next();
            if(change instanceof AddColumnChange)
            {
                AddColumnChange addColumnChange = (AddColumnChange)change;
                if(addColumnChange.getNewColumn().isRequired() && addColumnChange.getNewColumn().getDefaultValue() == null &&
                                !addColumnChange.getNewColumn().isAutoIncrement())
                {
                    requiresFullRebuild = true;
                }
            }
        }
        if(!requiresFullRebuild)
        {
            this.tableStructureChangesProcessor.processTableStructureChanges(currentModel, desiredModel, sourceTable, targetTable, parameters, changes);
        }
    }
}
