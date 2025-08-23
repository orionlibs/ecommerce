package de.hybris.platform.webservicescommons.resolver.helpers;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.apache.commons.text.StringSubstitutor;

public class FallbackConfigurationHelper
{
    private final List<String> propertyFormats;
    private final ConfigurationService configurationService;


    public FallbackConfigurationHelper(List<String> propertyFormats, ConfigurationService configurationService)
    {
        this.propertyFormats = new ArrayList<>(propertyFormats);
        this.configurationService = configurationService;
    }


    public Optional<String> getFirstValue(String extension, String group, String attribute)
    {
        return getFirstValue(extension, group, attribute, Function.identity());
    }


    public <T> Optional<T> getFirstValue(String extension, String group, String attribute, Function<String, T> valueMapper)
    {
        ServicesUtil.validateParameterNotNull(extension, "extension cannot be null");
        ServicesUtil.validateParameterNotNull(group, "group cannot be null");
        ServicesUtil.validateParameterNotNull(attribute, "attribute cannot be null");
        ServicesUtil.validateParameterNotNull(valueMapper, "valueMapper cannot be null");
        Map<String, String> substitutions = Map.of("extension", extension, "group", group, "attribute", attribute);
        return getFirstValue(substitutions, valueMapper);
    }


    public <T> Optional<T> getFirstValue(Map<String, String> substitutions, Function<String, T> valueMapper)
    {
        ServicesUtil.validateParameterNotNull(substitutions, "substitutions cannot be null");
        ServicesUtil.validateParameterNotNull(valueMapper, "valueMapper cannot be null");
        UnaryOperator<String> stringSubstitutor = new StringSubstitutor(substitutions, "{{", "}}")::replace;
        return this.propertyFormats.stream()
                        .map(stringSubstitutor)
                        .map(p -> this.configurationService.getConfiguration().getString(p, null))
                        .filter(Objects::nonNull)
                        .<T>map(valueMapper)
                        .findFirst();
    }
}
