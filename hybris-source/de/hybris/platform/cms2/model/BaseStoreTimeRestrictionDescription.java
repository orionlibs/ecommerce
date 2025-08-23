package de.hybris.platform.cms2.model;

import de.hybris.platform.cms2.model.restrictions.CMSBaseStoreTimeRestrictionModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.localization.Localization;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class BaseStoreTimeRestrictionDescription implements DynamicAttributeHandler<String, CMSBaseStoreTimeRestrictionModel>
{
    private static final String ALL_STORES = "all stores";


    public String get(CMSBaseStoreTimeRestrictionModel model)
    {
        String stores;
        if(CollectionUtils.isEmpty(model.getBaseStores()))
        {
            stores = "all stores";
        }
        else
        {
            stores = model.getBaseStores().stream().map(BaseStoreModel::getName).collect(Collectors.joining(","));
        }
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String from = (model.getActiveFrom() == null) ? null : format.format(model.getActiveFrom());
        String until = (model.getActiveUntil() == null) ? null : format.format(model.getActiveUntil());
        Object[] args = {stores, from, until};
        String localizedString = Localization.getLocalizedString("type.CMSBaseStoreTimeRestriction.description.text", args);
        return (localizedString == null) ? ("Display for base stores: " + stores + " from " + from + " to " + until) : localizedString;
    }


    public void set(CMSBaseStoreTimeRestrictionModel model, String s)
    {
        throw new UnsupportedOperationException();
    }
}
