package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import de.hybris.platform.core.model.user.AddressModel;
import org.apache.commons.lang.StringUtils;

public class AddressLabelProvider implements LabelProvider<AddressModel>
{
    public static final String SINGLE_SPACE = " ";


    public String getLabel(AddressModel address)
    {
        StringBuilder sb = new StringBuilder();
        appendPart(sb, address.getStreetname());
        appendPart(sb, " ");
        appendPart(sb, address.getStreetnumber());
        if(StringUtils.isNotBlank(sb.toString()))
        {
            sb.append(", ");
        }
        appendPart(sb, address.getPostalcode());
        appendPart(sb, " ");
        appendPart(sb, address.getTown());
        return sb.toString().trim();
    }


    private void appendPart(StringBuilder sb, String part)
    {
        String result = (part == null) ? "" : part;
        if(StringUtils.isNotEmpty(result))
        {
            sb.append(result);
        }
    }


    public String getDescription(AddressModel address)
    {
        return getLabel(address);
    }


    public String getIconPath(AddressModel address)
    {
        return null;
    }
}
