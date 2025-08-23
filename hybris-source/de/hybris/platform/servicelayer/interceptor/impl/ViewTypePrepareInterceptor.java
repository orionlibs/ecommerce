package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;
import org.springframework.beans.factory.annotation.Required;

public class ViewTypePrepareInterceptor implements PrepareInterceptor<ViewTypeModel>
{
    private TypeService typeService;


    public void onPrepare(ViewTypeModel viewType, InterceptorContext ctx) throws InterceptorException
    {
        if(ctx.isNew(viewType) || viewType.getSuperType() == null)
        {
            viewType.setSuperType(this.typeService.getComposedTypeForCode("Item"));
        }
        if(viewType.getParams() != null)
        {
            viewType.getParams().forEach(p -> {
                if(ctx.isNew(p))
                {
                    p.setParam(Boolean.TRUE);
                }
            });
        }
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
