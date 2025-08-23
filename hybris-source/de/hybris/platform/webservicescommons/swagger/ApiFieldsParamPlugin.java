package de.hybris.platform.webservicescommons.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

@Component
@Order(-2147482658)
public class ApiFieldsParamPlugin implements ParameterBuilderPlugin
{
    public static final String FIELDS_DESCRIPTION = "Response configuration. This is the list of fields that should be returned in the response body. Examples: ";
    private final TypeResolver resolver = new TypeResolver();


    public void apply(ParameterContext parameterContext)
    {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<ApiFieldsParam> fieldsParam = methodParameter.findAnnotation(ApiFieldsParam.class);
        if(fieldsParam.isPresent())
        {
            String commaSeparatedExamples = Arrays.<CharSequence>stream((CharSequence[])((ApiFieldsParam)fieldsParam.get()).examples()).collect(Collectors.joining(", "));
            parameterContext.parameterBuilder()
                            .defaultValue(((ApiFieldsParam)fieldsParam.get()).defaultValue())
                            .description("Response configuration. This is the list of fields that should be returned in the response body. Examples: " + commaSeparatedExamples)
                            .parameterType("query")
                            .type(this.resolver.resolve(String.class, new java.lang.reflect.Type[0]));
        }
    }


    public boolean supports(DocumentationType documentationType)
    {
        return true;
    }
}
