package de.hybris.platform.commerceservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class QuoteProcessModel extends BusinessProcessModel
{
    public static final String _TYPECODE = "QuoteProcess";
    public static final String QUOTECODE = "quoteCode";


    public QuoteProcessModel()
    {
    }


    public QuoteProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public QuoteProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public QuoteProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "quoteCode", type = Accessor.Type.GETTER)
    public String getQuoteCode()
    {
        return (String)getPersistenceContext().getPropertyValue("quoteCode");
    }


    @Accessor(qualifier = "quoteCode", type = Accessor.Type.SETTER)
    public void setQuoteCode(String value)
    {
        getPersistenceContext().setPropertyValue("quoteCode", value);
    }
}
