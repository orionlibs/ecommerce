package de.hybris.platform.cockpit.interceptor;

import de.hybris.platform.cockpit.model.CockpitObjectCollectionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

public class CockpitObjectCollectionModelPrepareInterceptor implements PrepareInterceptor
{
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CockpitObjectCollectionModel)
        {
            UserModel user = ((CockpitObjectCollectionModel)model).getUser();
            if(user != null)
            {
                String qualifier = ((CockpitObjectCollectionModel)model).getQualifier();
                if(StringUtils.isEmpty(qualifier))
                {
                    qualifier = "CockpitObjectCollection_" + user.getUid() + "_" + UUID.randomUUID();
                    ((CockpitObjectCollectionModel)model).setQualifier(qualifier);
                }
            }
        }
    }
}
