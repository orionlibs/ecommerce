package de.hybris.platform.customersupportbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import de.hybris.platform.core.model.user.AddressModel;
import org.apache.commons.lang.StringUtils;

public class AddressLabelProvider implements LabelProvider<AddressModel>
{
    public static final String DASH = " - ";


    public String getLabel(AddressModel address)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(address.getLine1());
        if(StringUtils.isNotEmpty(address.getLine2()))
        {
            stringBuilder.append(" - ").append(address.getLine2());
        }
        stringBuilder.append(" - ").append(address.getTown());
        if(address.getRegion() != null && StringUtils.isNotEmpty(address.getRegion().getName()))
        {
            stringBuilder.append(" - ").append(address.getRegion().getName());
        }
        stringBuilder.append(" - ").append(address.getPostalcode());
        if(address.getCountry() != null)
        {
            stringBuilder.append(" - ").append(address.getCountry().getName());
        }
        return stringBuilder.toString();
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
