package de.hybris.platform.servicelayer.user.interceptors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

public class PrincipalCatalogVersionsPrepareInterceptor implements PrepareInterceptor<PrincipalModel>
{
    public void onPrepare(PrincipalModel principalModel, InterceptorContext ctx) throws InterceptorException
    {
        List<CatalogVersionModel> readableAndWritableCatalogVersions = principalModel.getReadableCatalogVersions();
        boolean hasWritableCatalogVersions = CollectionUtils.isNotEmpty(principalModel.getWritableCatalogVersions());
        if(readableAndWritableCatalogVersions == null && hasWritableCatalogVersions)
        {
            readableAndWritableCatalogVersions = Lists.newArrayList();
        }
        if(hasWritableCatalogVersions)
        {
            readableAndWritableCatalogVersions = Lists.newArrayList(readableAndWritableCatalogVersions);
            readableAndWritableCatalogVersions.addAll(principalModel.getWritableCatalogVersions());
        }
        principalModel.setReadableCatalogVersions(removeNonUniqueElementsAndPreserveOrder(readableAndWritableCatalogVersions));
    }


    private <T> List<T> removeNonUniqueElementsAndPreserveOrder(List<T> value)
    {
        if(value == null)
        {
            return null;
        }
        return Lists.newArrayList(Sets.newLinkedHashSet(value));
    }
}
