package de.hybris.y2ysync.model;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class Y2YStreamConfigurationValidateInterceptor implements ValidateInterceptor<Y2YStreamConfigurationModel>
{
    private TypeService typeService;


    public void onValidate(Y2YStreamConfigurationModel streamConfiguration, InterceptorContext ctx) throws InterceptorException
    {
        Preconditions.checkNotNull(streamConfiguration);
        Preconditions.checkNotNull(streamConfiguration.getItemTypeForStream());
        assureAllColumnsBelongToType(streamConfiguration);
    }


    private void assureAllColumnsBelongToType(Y2YStreamConfigurationModel streamConfiguration) throws InterceptorException
    {
        String streamCfgTypeCode = streamConfiguration.getItemTypeForStream().getCode();
        Set<Y2YColumnDefinitionModel> columns = streamConfiguration.getColumnDefinitions();
        if(CollectionUtils.isNotEmpty(columns))
        {
            for(Y2YColumnDefinitionModel column : columns)
            {
                AttributeDescriptorModel ad = column.getAttributeDescriptor();
                if(ad != null)
                {
                    ComposedTypeModel columnEnclosingType = ad.getEnclosingType();
                    if(!columnBelongsToTypeAttributes(streamCfgTypeCode, columnEnclosingType))
                    {
                        String msg = streamConfiguration.getStreamId() + " stream has assigned '" + streamConfiguration.getStreamId() + "' column definition for '" + column.getColumnName() + "' type attribute. " + columnEnclosingType.getCode() + " is not assignable from "
                                        + columnEnclosingType.getCode() + ".";
                        throw new InterceptorException(msg);
                    }
                }
            }
        }
    }


    private boolean columnBelongsToTypeAttributes(String streamTypeCode, ComposedTypeModel columnEnclosingType)
    {
        return this.typeService.isAssignableFrom(columnEnclosingType.getCode(), streamTypeCode);
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
