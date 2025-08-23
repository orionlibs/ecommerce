package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class PrincipalModelLabelProvider extends AbstractModelLabelProvider<PrincipalModel>
{
    protected String getIconPath(PrincipalModel item)
    {
        return item.getProfilePicture().getDownloadURL();
    }


    protected String getIconPath(PrincipalModel item, String languageIso)
    {
        return item.getProfilePicture().getDownloadURL();
    }


    protected String getItemDescription(PrincipalModel item)
    {
        String title = item.getDescription();
        return StringUtils.isBlank(title) ? "" : title;
    }


    protected String getItemDescription(PrincipalModel item, String languageIso)
    {
        String title = item.getDescription();
        if(StringUtils.isBlank(title))
        {
            return getItemDescription(item);
        }
        return title;
    }


    protected String getItemLabel(PrincipalModel item)
    {
        String label;
        if(item instanceof PrincipalGroupModel)
        {
            label = ((PrincipalGroupModel)item).getLocName();
        }
        else
        {
            label = item.getName();
        }
        return StringUtils.isBlank(label) ? item.getUid() : label;
    }


    protected String getItemLabel(PrincipalModel item, String languageIso)
    {
        String title;
        if(item instanceof PrincipalGroupModel)
        {
            title = ((PrincipalGroupModel)item).getLocName(new Locale(languageIso));
        }
        else
        {
            title = item.getName();
        }
        if(StringUtils.isBlank(title))
        {
            return getItemLabel(item);
        }
        return title;
    }
}
