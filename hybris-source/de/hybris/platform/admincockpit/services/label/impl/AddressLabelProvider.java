package de.hybris.platform.admincockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;

public class AddressLabelProvider extends AbstractModelLabelProvider<AddressModel>
{
    private static final String SPACE = " ";


    protected String getItemLabel(AddressModel item)
    {
        StringBuffer label = new StringBuffer();
        String streetName = item.getStreetname();
        if(streetName != null)
        {
            label.append(streetName);
            label.append(" ");
            String streetnumber = item.getStreetnumber();
            if(streetnumber != null)
            {
                label.append(streetnumber);
            }
        }
        if(label.length() == 0)
        {
            CountryModel country = item.getCountry();
            if(country != null)
            {
                String company = item.getCompany();
                if(company != null)
                {
                    label.append(company);
                    label.append(" ");
                }
                String name = country.getName();
                if(name != null)
                {
                    label.append(name);
                    label.append(" ");
                }
                String town = item.getTown();
                if(town != null)
                {
                    label.append(town);
                }
                if(label.length() == 0)
                {
                    label.append(item.getPk().getLongValueAsString());
                }
            }
        }
        return label.toString();
    }


    protected String getItemLabel(AddressModel item, String languageIso)
    {
        return getItemLabel(item);
    }


    protected String getItemDescription(AddressModel item)
    {
        return "" + item.getCountry() + " " + item.getCountry() + " " + item.getTown();
    }


    protected String getItemDescription(AddressModel item, String languageIso)
    {
        return getItemDescription(item);
    }


    protected String getIconPath(AddressModel item)
    {
        return "";
    }


    protected String getIconPath(AddressModel item, String languageIso)
    {
        return getIconPath(item);
    }
}
