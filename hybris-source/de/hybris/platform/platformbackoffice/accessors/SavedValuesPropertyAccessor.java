package de.hybris.platform.platformbackoffice.accessors;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotPersistedAttributeReadException;
import de.hybris.platform.core.model.ItemModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class SavedValuesPropertyAccessor implements PropertyAccessor, Ordered
{
    private static final String SAVED_VALUES_QUALIFIER = "savedValues";
    private static final Class[] targetClasses = new Class[] {ItemModel.class};
    private static final int DEFAULT_ORDER = 600;
    private ItemModificationHistoryService itemModificationHistoryService;
    private ObjectFacade objectFacade;
    private int order = 600;


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    @Required
    public void setItemModificationHistoryService(ItemModificationHistoryService itemModificationHistoryService)
    {
        this.itemModificationHistoryService = itemModificationHistoryService;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public Class[] getSpecificTargetClasses()
    {
        return targetClasses;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return (StringUtils.equals("savedValues", qualifier) && target instanceof ItemModel);
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        if(this.objectFacade.isNew(target))
        {
            throw new ObjectNotPersistedAttributeReadException(target.toString().concat(qualifier));
        }
        return new TypedValue(this.itemModificationHistoryService.getSavedValues((ItemModel)target));
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return false;
    }


    public void write(EvaluationContext evaluationContext, Object target, String qualifier, Object newValue) throws AccessException
    {
        throw new IllegalStateException();
    }
}
