package de.hybris.platform.platformbackoffice.accessors;

import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.util.ViewResultItem;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class ViewResultItemPropertyAccessor implements PropertyAccessor, Ordered
{
    private static final Logger LOG = LoggerFactory.getLogger(ViewResultItemPropertyAccessor.class);
    private static final int DEFAULT_ORDER = 800;
    private int order = 800;


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public Class<?>[] getSpecificTargetClasses()
    {
        return new Class[] {ViewResultItem.class};
    }


    public boolean canRead(EvaluationContext evaluationContext, Object target, String attribute) throws AccessException
    {
        Map allAttributes;
        try
        {
            allAttributes = ((ViewResultItem)target).getAllAttributes();
        }
        catch(JaloSecurityException jaloExc)
        {
            LOG.info(jaloExc.getMessage(), (Throwable)jaloExc);
            return false;
        }
        return (allAttributes != null && allAttributes.containsKey(attribute));
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String attribute) throws AccessException
    {
        try
        {
            Map allAttributes = ((ViewResultItem)target).getAllAttributes();
            return new TypedValue(allAttributes.get(attribute));
        }
        catch(JaloSecurityException jaloExc)
        {
            throw new AccessException(jaloExc.getMessage(), jaloExc);
        }
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object target, String attribute) throws AccessException
    {
        return false;
    }


    public void write(EvaluationContext evaluationContext, Object target, String attribute, Object value) throws AccessException
    {
        throw new UnsupportedOperationException();
    }
}
