package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import de.hybris.platform.core.order.EntryGroup;
import org.apache.commons.lang.StringUtils;

public class EntryGroupLabelProvider implements LabelProvider<EntryGroup>
{
    protected static final String ENTRY_GROUP = "<EntryGroup>";
    protected static final String ENTRY_GROUP_NUMBER_PREFIX = " #";


    public String getLabel(EntryGroup group)
    {
        if(group == null)
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isNotBlank(group.getLabel()))
        {
            builder.append(group.getLabel());
        }
        else if(StringUtils.isNotBlank(group.getExternalReferenceId()))
        {
            builder.append(group.getExternalReferenceId());
        }
        else
        {
            builder.append("<EntryGroup>");
        }
        if(group.getGroupNumber() != null)
        {
            builder.append(" #");
            builder.append(group.getGroupNumber());
        }
        return builder.toString();
    }


    public String getDescription(EntryGroup object)
    {
        return "";
    }


    public String getIconPath(EntryGroup object)
    {
        return null;
    }
}
