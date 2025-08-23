package de.hybris.platform.mediaconversion.conversion;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import de.hybris.platform.util.Config;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageMagickSecurityService
{
    private static final Logger LOG = LoggerFactory.getLogger(ImageMagickSecurityService.class);
    private static final String BLACKLIST_PROPERTY = "imagemagick.executable.convert.commands.blacklist";
    private static final String WHITELIST_PROPERTY = "imagemagick.executable.convert.commands.whitelist";
    private static final String VALIDATION_TYPE_PROPERTY = "imagemagick.executable.convert.commands.validation.type";
    private static final Splitter CONFIG_SPLITTER = Splitter.on(',')
                    .omitEmptyStrings()
                    .trimResults();
    private static final Splitter COMMAND_OPTS_SPLITTER = Splitter.on(' ')
                    .omitEmptyStrings()
                    .trimResults();
    private static final Set<String> COMMAND_OPT_PREFIXES = Sets.newHashSet((Object[])new String[] {"+", "-"});


    public ConvertCommandValidationResult isCommandSecure(String command)
    {
        Objects.requireNonNull(command, "command is required");
        Iterable<String> splitted = COMMAND_OPTS_SPLITTER.split(command);
        return validate(splitted);
    }


    public ConvertCommandValidationResult isCommandSecure(List<String> commandOpts)
    {
        Objects.requireNonNull(commandOpts, "commandOpts is required");
        List<String> trimmedOpts = (List<String>)commandOpts.stream().map(String::trim).collect(Collectors.toList());
        return validate(trimmedOpts);
    }


    private ConvertCommandValidationResult validate(Iterable<String> splitted)
    {
        Set<String> commandOptsWithoutPrefix = extractCommandOpts(splitted);
        ValidationType validationType = loadValidationType();
        Set<String> configuredCommands = getConvertCommandList(validationType.getConfigParameter());
        return validationType.validate(commandOptsWithoutPrefix, configuredCommands);
    }


    private Set<String> extractCommandOpts(Iterable<String> splitted)
    {
        return (Set<String>)COMMAND_OPT_PREFIXES.stream()
                        .flatMap(prefix -> StreamSupport.stream(splitted.spliterator(), false).map(()))
                        .filter(pair -> ((String)pair.getValue()).startsWith((String)pair.getKey()))
                        .map(entry -> ((String)entry.getValue()).replace((CharSequence)entry.getKey(), ""))
                        .collect(Collectors.toSet());
    }


    private Set<String> getConvertCommandList(String propertyName)
    {
        String commaSeparatedCommands = getCommandsFromConfig(propertyName);
        if(StringUtils.isBlank(commaSeparatedCommands))
        {
            return Collections.emptySet();
        }
        return (Set<String>)StreamSupport.stream(CONFIG_SPLITTER.split(commaSeparatedCommands)
                                        .spliterator(), false)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
    }


    String getCommandsFromConfig(String property)
    {
        return Config.getParameter(property);
    }


    private ValidationType loadValidationType()
    {
        String validationType = getValidationType();
        try
        {
            return ValidationType.valueOf(validationType.toUpperCase());
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Unable to load image magick validation type '{}'. Default whitelist used.", validationType);
            return ValidationType.WHITELIST;
        }
    }


    String getValidationType()
    {
        return Config.getString("imagemagick.executable.convert.commands.validation.type", ValidationType.WHITELIST.toString());
    }
}
