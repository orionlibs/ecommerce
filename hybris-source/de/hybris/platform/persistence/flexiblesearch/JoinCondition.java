package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class JoinCondition extends TableCondition
{
    JoinCondition(String conditionText, TypeJoin typeJoin)
    {
        super(typeJoin.getJoin().getFrom(), conditionText);
    }


    protected ParsedText translateNested(int resultInsertPos, String selectedText) throws FlexibleSearchException
    {
        return super.translateNested(resultInsertPos, selectedText);
    }


    boolean isLinkingCondition(ParsedType targetType)
    {
        int i;
        boolean targetTableFound = false;
        boolean sourceTableFound = false;
        for(Iterator<TableField> it = getFields().iterator(); (!targetTableFound || !sourceTableFound) && it.hasNext(); )
        {
            TableField field = it.next();
            boolean isTargetTable = targetType.equals(field.getType());
            targetTableFound |= isTargetTable;
            i = sourceTableFound | (!isTargetTable ? 1 : 0);
        }
        return (targetTableFound && i != 0);
    }


    Set<Table> getTargetTables(ParsedType targetType)
    {
        Set<Table> ret = null;
        List<TableField> fields = getFields();
        for(TableField field : fields)
        {
            if(targetType.equals(field.getType()))
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>(fields.size());
                }
                ret.add(field.getTable());
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }
}
