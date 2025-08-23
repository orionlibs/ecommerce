package de.hybris.platform.cockpit.model.search;

import de.hybris.platform.core.model.c2l.LanguageModel;

public class SearchParameterValue
{
    private final SearchParameterDescriptor propertyDescriptor;
    private final Object value;
    private final Operator operator;
    private final LanguageModel language;
    private boolean caseInsensitive;


    public SearchParameterValue(SearchParameterDescriptor descriptor, Object value, Operator operator)
    {
        this(descriptor, value, operator, null);
    }


    public SearchParameterValue(SearchParameterDescriptor descriptor, Object value, Operator operator, LanguageModel language)
    {
        if(descriptor == null)
        {
            throw new IllegalArgumentException("Descriptor can not be null.");
        }
        this.propertyDescriptor = descriptor;
        this.value = value;
        this.operator = operator;
        this.language = language;
    }


    public Object getValue()
    {
        return this.value;
    }


    public boolean isCaseInsensitive()
    {
        return this.caseInsensitive;
    }


    public void setCaseInsensitive(boolean caseInsensitive)
    {
        this.caseInsensitive = caseInsensitive;
    }


    public Operator getOperator()
    {
        return this.operator;
    }


    public LanguageModel getLanguage()
    {
        return this.language;
    }


    public SearchParameterDescriptor getParameterDescriptor()
    {
        return this.propertyDescriptor;
    }
}
