package de.hybris.platform.impex.jalo.translators;

import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.migration.MigrationUtilities;

public class ItemPKTranslator extends SingleValueTranslator
{
    private final ComposedType targetType;


    public ItemPKTranslator(ComposedType targetType)
    {
        this.targetType = targetType;
    }


    protected Object convertToJalo(String valueExpr, Item forItem)
    {
        try
        {
            Item item = isSetPkFromExternalImportKey() ? findCorrespondingTargetSystemItem(valueExpr) : findStandardItem(valueExpr);
            if(isNotDefinedInHeaderItem(item))
            {
                setError();
                throw new JaloInvalidParameterException("cannot import item pk '" + valueExpr + "' since it is no instance of attribute type " + this.targetType
                                .getCode(), 0);
            }
            return item;
        }
        catch(JaloItemNotFoundException | de.hybris.platform.servicelayer.exceptions.ModelNotFoundException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException e)
        {
            setError();
            return null;
        }
        catch(IllegalArgumentException e)
        {
            setError();
            throw e;
        }
    }


    private Item findStandardItem(String valueExpr)
    {
        PK parsedPK;
        if(MigrationUtilities.isOldPK(valueExpr))
        {
            parsedPK = MigrationUtilities.convertOldPK(valueExpr);
        }
        else
        {
            parsedPK = PK.parse(valueExpr);
        }
        return JaloSession.getCurrentSession().getItem(parsedPK);
    }


    private Item findCorrespondingTargetSystemItem(String sourceKey)
    {
        FlexibleSearchService service = getFlexibleSearchService();
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {targetPK} FROM {ExternalImportKey} WHERE {sourceSystemID}=?sourceSystemId AND {sourceKey}=?sourceKey");
        fQuery.addQueryParameter("sourceSystemId", getSourceSystemId());
        fQuery.addQueryParameter("sourceKey", sourceKey);
        fQuery.setResultClassList(Lists.newArrayList((Object[])new Class[] {PK.class}));
        return JaloSession.getCurrentSession().getItem((PK)service.searchUnique(fQuery));
    }


    private boolean isSetPkFromExternalImportKey()
    {
        if(getColumnDescriptor() == null || getColumnDescriptor().getDescriptorData() == null)
        {
            return false;
        }
        return Boolean.parseBoolean(getColumnDescriptor().getDescriptorData().getModifier("pkFromExternalImportKey"));
    }


    private String getSourceSystemId()
    {
        return getColumnDescriptor().getDescriptorData().getModifier("sourceSystemId");
    }


    private boolean isNotDefinedInHeaderItem(Item item)
    {
        return (item != null && !item.isInstanceOf(this.targetType));
    }


    private FlexibleSearchService getFlexibleSearchService()
    {
        return (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService", FlexibleSearchService.class);
    }


    protected String convertToString(Object value)
    {
        return ((Item)value).getPK().toString();
    }
}
