package de.hybris.platform.personalizationservices.trigger.expression.impl;

import de.hybris.platform.personalizationservices.trigger.expression.CxExpression;
import de.hybris.platform.personalizationservices.trigger.expression.CxExpressionContext;
import java.util.ArrayList;
import java.util.List;

public class CxGroupExpression implements CxExpression
{
    private CxGroupOperator operator;
    private List<CxExpression> elements;


    public CxGroupExpression()
    {
        this.operator = CxGroupOperator.AND;
        this.elements = new ArrayList<>();
    }


    public CxGroupExpression(CxGroupOperator operator)
    {
        this.elements = new ArrayList<>();
        this.operator = operator;
    }


    public void setOperator(CxGroupOperator operator)
    {
        this.operator = operator;
    }


    public CxGroupOperator getOperator()
    {
        return this.operator;
    }


    public void setElements(List<CxExpression> elements)
    {
        this.elements = elements;
    }


    public List<CxExpression> getElements()
    {
        return this.elements;
    }


    public CxGroupExpression add(CxExpression element)
    {
        this.elements.add(element);
        return this;
    }


    public boolean evaluate(CxExpressionContext context)
    {
        boolean result;
        if(getElements().isEmpty())
        {
            return true;
        }
        if(CxGroupOperator.AND == getOperator())
        {
            result = true;
            for(int i = 0; i < getElements().size(); i++)
            {
                if(!((CxExpression)getElements().get(i)).evaluate(context))
                {
                    result = false;
                    break;
                }
            }
        }
        else
        {
            result = false;
            for(int i = 0; i < getElements().size(); i++)
            {
                if(((CxExpression)getElements().get(i)).evaluate(context))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
