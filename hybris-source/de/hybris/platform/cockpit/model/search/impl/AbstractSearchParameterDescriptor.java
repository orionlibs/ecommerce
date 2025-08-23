package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.meta.impl.AbstractPropertyDescriptor;
import de.hybris.platform.cockpit.model.search.Operator;
import de.hybris.platform.cockpit.model.search.SearchParameterDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSearchParameterDescriptor extends AbstractPropertyDescriptor implements SearchParameterDescriptor
{
    private List<Operator> operators;


    public void setOperators(List<Operator> operators)
    {
        this.operators = (operators != null) ? new ArrayList<>(operators) : null;
    }


    public List<Operator> getOperators()
    {
        return (this.operators != null) ? Collections.<Operator>unmodifiableList(this.operators) : Collections.EMPTY_LIST;
    }
}
