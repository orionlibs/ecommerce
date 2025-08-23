package de.hybris.platform.customersupportbackoffice.accessors.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class CustomerCartsPropertyAccessor implements PropertyAccessor
{
    private static final String CARTS_ATTR = "carts";
    private static final String SAVED_CARTS_ATTR = "savedCarts";


    public Class<?>[] getSpecificTargetClasses()
    {
        Class<?>[] classes = new Class[] {CustomerModel.class};
        return classes;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object currentObject, String attribute) throws AccessException
    {
        return (isTypeSupported(currentObject) && (attribute
                        .equalsIgnoreCase("carts") || attribute.equalsIgnoreCase("savedCarts")));
    }


    protected boolean isTypeSupported(Object object)
    {
        return object instanceof CustomerModel;
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String attribute) throws AccessException
    {
        CustomerModel currentCustomer = (CustomerModel)target;
        Collection<CartModel> carts = currentCustomer.getCarts();
        if(null != carts)
        {
            if(attribute.equalsIgnoreCase("carts"))
            {
                return new TypedValue(carts
                                .stream().filter(i -> (null == i.getSaveTime() && !i.getEntries().isEmpty())).collect(Collectors.toList()));
            }
            if(attribute.equalsIgnoreCase("savedCarts"))
            {
                return new TypedValue(carts.stream().filter(i -> (null != i.getSaveTime())).collect(Collectors.toList()));
            }
        }
        return new TypedValue(carts);
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object currentObject, String attribute) throws AccessException
    {
        return false;
    }


    public void write(EvaluationContext evaluationContext, Object target, String attributeName, Object attributeValue) throws AccessException
    {
    }
}
