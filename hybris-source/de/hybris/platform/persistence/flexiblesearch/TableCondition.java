package de.hybris.platform.persistence.flexiblesearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TableCondition extends FieldExpression
{
    private Table myTable;


    TableCondition(FromClause from, String condTxt)
    {
        super(from, condTxt);
    }


    void assignedTo(Table table)
    {
        this.myTable = table;
    }


    Table getTable()
    {
        return this.myTable;
    }


    List<TableField> getOwnFields()
    {
        List<TableField> ret = null;
        if(hasFields())
        {
            List<TableField> fields = getFields();
            for(TableField field : fields)
            {
                if(field.getTable().equals(getTable()))
                {
                    if(ret == null)
                    {
                        ret = new ArrayList<>(fields.size());
                    }
                    ret.add(field);
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }
}
