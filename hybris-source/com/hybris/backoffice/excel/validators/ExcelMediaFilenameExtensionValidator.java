package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;

public class ExcelMediaFilenameExtensionValidator extends ExcelMediaImportValidator
{
    protected static final String EXCEL_IMPORT_VALIDATION_MEDIA_EXTENSIONS = "excel.import.validation.media.extensions";
    protected static final String CONFIG_EXCEL_AVAILABLE_MEDIA_EXTENSIONS = "excel.available.media.extensions";
    private ConfigurationService configurationService;


    public List<ValidationMessage> validateSingleValue(Map<String, Object> context, Map<String, String> parameters)
    {
        return Optional.<String>ofNullable(getValueToValidate(parameters))
                        .map(this::validateZipEntries)
                        .orElse(Collections.emptyList());
    }


    protected String getValueToValidate(Map<String, String> parameters)
    {
        return parameters.get("filePath");
    }


    protected List<ValidationMessage> validateZipEntries(String filePath)
    {
        Map<String, String> cache = new HashMap<>();
        String extensionsKey = "extensions";
        Supplier<String> availableExtensions = () -> {
            if(!cache.containsKey("extensions"))
            {
                cache.put("extensions", getAvailableExtensions());
            }
            return cache.get("extensions");
        };
        return Optional.<String>of(filePath)
                        .map(FilenameUtils::getExtension)
                        .filter(StringUtils::isNotBlank)
                        .filter(this::isNotAvailable)
                        .map(extension -> new ValidationMessage("excel.import.validation.media.extensions", new Serializable[] {extension, filePath, availableExtensions.get()})).map(xva$0 -> Lists.newArrayList((Object[])new ValidationMessage[] {xva$0})).orElse(new ArrayList<>());
    }


    protected boolean isNotAvailable(String extension)
    {
        return getConfigExtensions()
                        .stream()
                        .distinct()
                        .map(String::trim)
                        .noneMatch(configExtension -> StringUtils.equalsIgnoreCase(configExtension, extension));
    }


    protected Collection<String> getConfigExtensions()
    {
        return Lists.newArrayList((Object[])StringUtils.split(this.configurationService
                        .getConfiguration().getString("excel.available.media.extensions", ""), ","));
    }


    protected String getAvailableExtensions()
    {
        return getConfigExtensions().stream().collect(Collectors.joining(", ", "[", "]"));
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
