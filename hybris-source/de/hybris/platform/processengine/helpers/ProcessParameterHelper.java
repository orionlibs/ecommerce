package de.hybris.platform.processengine.helpers;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import java.util.Collection;
import java.util.Set;

public interface ProcessParameterHelper
{
    void setProcessParameter(BusinessProcessModel paramBusinessProcessModel, String paramString, Object paramObject);


    boolean containsParameter(BusinessProcessModel paramBusinessProcessModel, String paramString);


    Set<String> getAllParameterNames(BusinessProcessModel paramBusinessProcessModel);


    BusinessProcessParameterModel getProcessParameterByName(String paramString, Collection<BusinessProcessParameterModel> paramCollection);


    BusinessProcessParameterModel getProcessParameterByName(BusinessProcessModel paramBusinessProcessModel, String paramString);
}
