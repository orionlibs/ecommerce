package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.warehousing.util.builder.ComposedTypeModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class ComposedTypes extends AbstractItems<ComposedTypeModel>
{
    public static final String CS_CUSTOMER_EVENT = "CsCustomerEvent";
    private TypeService typeService;


    public ComposedTypeModel customerEvent()
    {
        return (ComposedTypeModel)getOrSaveAndReturn(() -> getTypeService().getComposedTypeForCode("CsCustomerEvent"), () -> ComposedTypeModelBuilder.aModel().withCode("CsCustomerEvent").withName("CsCustomerEvent").build());
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
}
