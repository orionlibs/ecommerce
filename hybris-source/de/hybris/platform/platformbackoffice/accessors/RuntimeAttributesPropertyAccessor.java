package de.hybris.platform.platformbackoffice.accessors;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import de.hybris.platform.core.model.ItemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class RuntimeAttributesPropertyAccessor implements PropertyAccessor, Ordered
{
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeAttributesPropertyAccessor.class);
    private static final Class[] targetClasses = new Class[] {ItemModel.class};
    private static final int DEFAULT_ORDER = 1300;
    private TypeFacade typeFacade;
    private int order = 1300;


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public Class[] getSpecificTargetClasses()
    {
        return targetClasses;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return canHandle((ItemModel)target, qualifier);
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return canHandle((ItemModel)target, qualifier);
    }


    private boolean canHandle(ItemModel model, String qualifier) throws AccessException
    {
        try
        {
            DataAttribute attributeType = getAttributeType(model, qualifier);
            return (attributeType != null && attributeType.isRuntimeAttribute());
        }
        catch(TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("", (Throwable)e);
            }
            throw new AccessException("", e);
        }
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return new TypedValue(((ItemModel)target).getProperty(qualifier));
    }


    public void write(EvaluationContext evaluationContext, Object target, String qualifier, Object newValue) throws AccessException
    {
        ((ItemModel)target).setProperty(qualifier, newValue);
    }


    private DataAttribute getAttributeType(ItemModel target, String qualifier) throws TypeNotFoundException
    {
        return this.typeFacade.load(target.getItemtype()).getAttribute(qualifier);
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
