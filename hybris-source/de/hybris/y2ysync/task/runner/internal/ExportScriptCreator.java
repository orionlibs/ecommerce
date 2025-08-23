package de.hybris.y2ysync.task.runner.internal;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import java.util.Collection;
import java.util.Objects;

public class ExportScriptCreator
{
    private final String impExHeader;
    private final String typeCode;
    private final Collection<ItemChangeDTO> changes;


    public ExportScriptCreator(String impExHeader, String typeCode, Collection<ItemChangeDTO> changes)
    {
        Objects.requireNonNull(impExHeader, "impExHeader can't be null");
        Objects.requireNonNull(typeCode, "typeCode can't be null");
        Objects.requireNonNull(changes, "changes can't be null");
        this.impExHeader = impExHeader;
        this.typeCode = typeCode;
        this.changes = changes;
    }


    public String createInsertUpdateExportScript()
    {
        StringBuilder insertUpdatePart = (new StringBuilder("\"#% impex.setTargetFile( \"\"")).append(getFileNameForInsertedOrUpdatedItems()).append("\"\")\"\n").append(getInsertUpdateHeader()).append("\n\"#% impex.exportItems(new String[] {\n");
        boolean anyItemToInsertUpdate = false;
        for(ItemChangeDTO change : this.changes)
        {
            if(ChangeType.DELETED != change.getChangeType())
            {
                anyItemToInsertUpdate = true;
                insertUpdatePart.append("\"\"").append(change.getItemPK()).append("\"\",");
            }
        }
        insertUpdatePart.append("\n})\"\n");
        StringBuilder result = new StringBuilder();
        if(anyItemToInsertUpdate)
        {
            result.append(insertUpdatePart);
        }
        return result.toString();
    }


    public String createDeleteCsv()
    {
        StringBuilder result = new StringBuilder();
        this.changes.stream().filter(change -> (ChangeType.DELETED == change.getChangeType())).forEach(change -> result.append(change.getInfo()).append("\n"));
        return result.toString();
    }


    String getTypeCode()
    {
        return this.typeCode;
    }


    String getFileNameForRemovedItems()
    {
        return getTypeCode() + "_remove.csv";
    }


    String getFileNameForInsertedOrUpdatedItems()
    {
        return getTypeCode() + "_insert_update.csv";
    }


    String getRemoveHeader()
    {
        return "REMOVE " + getTypeCode() + ";" + this.impExHeader;
    }


    String getInsertUpdateHeader()
    {
        return "INSERT_UPDATE " + getTypeCode() + ";" + this.impExHeader;
    }
}
