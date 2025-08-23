package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.services.label.AbstractModelLabelProvider;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Locale;

public class LanguageModelLabelProvider extends AbstractModelLabelProvider<LanguageModel>
{
    protected String getItemLabel(LanguageModel item)
    {
        String ret = item.getName();
        if(ret == null)
        {
            ret = item.getIsocode();
        }
        return ret;
    }


    protected String getItemLabel(LanguageModel item, String languageIso)
    {
        String ret = item.getName(new Locale(languageIso));
        if(ret == null)
        {
            ret = item.getIsocode();
        }
        return ret;
    }


    protected String getIconPath(LanguageModel item)
    {
        return null;
    }


    protected String getIconPath(LanguageModel item, String languageIso)
    {
        return null;
    }


    protected String getItemDescription(LanguageModel item)
    {
        return "";
    }


    protected String getItemDescription(LanguageModel item, String languageIso)
    {
        return "";
    }
}
