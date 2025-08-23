package de.hybris.platform.cms2.model;

import de.hybris.platform.cms2.model.restrictions.CMSInverseRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

public class InverseRestrictionDescription implements DynamicAttributeHandler<String, CMSInverseRestrictionModel>
{
    public String get(CMSInverseRestrictionModel model)
    {
        return Localization.getLocalizedString("type.cmsinverserestriction.description.text") + " " + Localization.getLocalizedString("type.cmsinverserestriction.description.text");
    }


    public void set(CMSInverseRestrictionModel model, String value)
    {
        throw new UnsupportedOperationException();
    }
}
