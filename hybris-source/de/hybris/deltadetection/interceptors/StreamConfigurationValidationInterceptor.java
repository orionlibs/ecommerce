package de.hybris.deltadetection.interceptors;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class StreamConfigurationValidationInterceptor implements ValidateInterceptor<StreamConfigurationModel>
{
    private TypeService typeService;


    public void onValidate(StreamConfigurationModel config, InterceptorContext ctx) throws InterceptorException
    {
        ValidationResult whereClauseValidationResult = hasExtraWhereClause(config);
        ValidationResult subTypesValidationResult = hasExcludedTypesNotBeingSubtypesOfItemType(config);
        if(!whereClauseValidationResult.isValid() || !subTypesValidationResult.isValid())
        {
            throw new InterceptorException("StreamConfiguration is invalid [reason: " + whereClauseValidationResult.getMessage() + subTypesValidationResult
                            .getMessage() + "]");
        }
    }


    private ValidationResult hasExtraWhereClause(StreamConfigurationModel config)
    {
        String whereClause = config.getWhereClause();
        if(whereClause == null)
        {
            return ValidationResult.VALID;
        }
        return new ValidationResult("Your whereClause for stream: " + config.getStreamId() + " contains unnecessary WHERE keyword at the beginning. ",
                        !whereClause.trim().toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("where"));
    }


    private ValidationResult hasExcludedTypesNotBeingSubtypesOfItemType(StreamConfigurationModel config)
    {
        Set<ComposedTypeModel> excludedTypes = config.getExcludedTypes();
        if(CollectionUtils.isEmpty(excludedTypes))
        {
            return ValidationResult.VALID;
        }
        ComposedTypeModel itemTypeForStream = config.getItemTypeForStream();
        for(ComposedTypeModel type : excludedTypes)
        {
            if(!this.typeService.isAssignableFrom((TypeModel)itemTypeForStream, (TypeModel)type))
            {
                return new ValidationResult("One of your excluded types [" + type + "] is not subtype of main item type for stream: " + itemTypeForStream + ". ", false);
            }
        }
        return ValidationResult.VALID;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
