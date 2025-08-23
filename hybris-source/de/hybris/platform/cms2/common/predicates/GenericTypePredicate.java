package de.hybris.platform.cms2.common.predicates;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class GenericTypePredicate<T extends ItemModel> implements Predicate<T>
{
    private TypeService typeService;
    private String typeCode;


    public boolean test(T itemModel)
    {
        return getTypeService().isAssignableFrom(getTypeCode(), itemModel.getItemtype());
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected String getTypeCode()
    {
        return this.typeCode;
    }


    @Required
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
}
