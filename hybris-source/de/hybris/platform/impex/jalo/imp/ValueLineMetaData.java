package de.hybris.platform.impex.jalo.imp;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import de.hybris.platform.core.PK;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class ValueLineMetaData
{
    public static final String VALUE_ENTRY_ERROR_MSG_DELIM = ";";
    public static final String VALUE_LINE_ERROR_MSG_DELIM = "| ";
    private static final Splitter SPLITTER = Splitter.on(',').trimResults().limit(5);
    private static final ValueLineMetaData EMPTY_METADATA = new ValueLineMetaData();
    private final String typeCode;
    private final PK processedItemPK;
    private final Boolean unrecoverable;
    private final PK conflictingItemPk;
    private final String errorMessage;


    protected ValueLineMetaData()
    {
        this(null, null, null, null, null);
    }


    protected ValueLineMetaData(String typeCode, PK processedItemPK, Boolean unrecoverable, PK conflictingItemPk, String errorMessage)
    {
        this.typeCode = typeCode;
        this.processedItemPK = processedItemPK;
        this.unrecoverable = unrecoverable;
        this.conflictingItemPk = conflictingItemPk;
        this.errorMessage = errorMessage;
    }


    public static ValueLineMetaData toMetaData(String input)
    {
        if(StringUtils.isEmpty(input))
        {
            return EMPTY_METADATA;
        }
        List<String> result = SPLITTER.splitToList(input);
        if(result.size() == 1)
        {
            String str = result.get(0);
            return builder().withTypeCode(str).build();
        }
        Preconditions.checkState((result.size() == 5), "full meta data string must contain 5 fields");
        String typeCode = Strings.emptyToNull(result.get(0));
        PK processedItemPK = StringUtils.isEmpty(result.get(1)) ? null : PK.parse(result.get(1));
        Boolean unrecoverable = StringUtils.isEmpty(result.get(2)) ? null : Boolean.valueOf(result.get(2));
        PK conflictingItemPk = StringUtils.isEmpty(result.get(3)) ? null : PK.parse(result.get(3));
        String errorMessage = Strings.emptyToNull(result.get(4));
        return builder().withTypeCode(typeCode).withProcessedItemPK(processedItemPK).withUnrecoverable(unrecoverable)
                        .withConflictingItemPk(conflictingItemPk).withErrorMessage(errorMessage).build();
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public PK getProcessedItemPK()
    {
        return this.processedItemPK;
    }


    public boolean isUnrecoverable()
    {
        return (this.unrecoverable != null && this.unrecoverable.booleanValue());
    }


    public String getErrorMessage()
    {
        return this.errorMessage;
    }


    public PK getConflictingItemPk()
    {
        return this.conflictingItemPk;
    }


    public String dump()
    {
        StringBuilder sb = new StringBuilder();
        sb.append((getTypeCode() == null) ? "" : getTypeCode());
        sb.append(",");
        sb.append((this.processedItemPK == null) ? "" : this.processedItemPK);
        sb.append(",");
        sb.append(isUnrecoverable() ? this.unrecoverable : "");
        sb.append(",");
        sb.append((this.conflictingItemPk == null) ? "" : this.conflictingItemPk);
        sb.append(",");
        sb.append(StringUtils.isEmpty(this.errorMessage) ? "" : this.errorMessage);
        return sb.toString();
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
