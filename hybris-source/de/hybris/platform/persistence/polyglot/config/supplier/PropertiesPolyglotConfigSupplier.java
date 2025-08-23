package de.hybris.platform.persistence.polyglot.config.supplier;

import com.google.common.base.Splitter;
import com.google.common.base.Suppliers;
import de.hybris.platform.util.Config;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class PropertiesPolyglotConfigSupplier implements PolyglotConfigSupplier
{
    private static final String PARAM_PREFIX = "polyglot.repository.config.";
    private static final String BEAN_NAME_SUFFIX = ".beanName";
    private static final String TYPE_CODES_SUFFIX = ".typeCodes";
    private static final String REPO_NAME_PART = "repoName";
    private static final Pattern REPO_NAME_PATTERN = Pattern.compile(
                    String.format("^%s(?<%s>[a-z][\\w]*)(\\%s|\\%s)$", new Object[] {"polyglot.repository.config.", "repoName", ".beanName", ".typeCodes"}), 2);
    private static final String TYPE_CODE_PART = "typeCode";
    private static final String QUALIFIER_PART = "qualifier";
    private static final String QUALIFIER_TYPE_PART = "qualifierType";
    private static final String CONDITION_PART = "condition";
    private static final Pattern TYPE_CODE_PATTERN = Pattern.compile(
                    String.format("^(?<%s>[a-z_$][\\w$]*)(?<%s>\\[(?<%s>[a-z_$][^=\\[\\]]*)=?(?<%s>[a-z_$][\\w$]*)?\\])?$", new Object[] {"typeCode", "condition", "qualifier", "qualifierType"}), 2);
    private static final Pattern MULTIPLE_CONDITION_PATTERN = Pattern.compile(
                    String.format("\\[(?<%s>([a-z_$]\\w+;){1,}\\w+)\\]", new Object[] {"qualifier"}), 2);
    private final Supplier<Map<String, String>> properties = (Supplier<Map<String, String>>)Suppliers.memoize(this::readProperties);
    private final Supplier<Set<String>> repoNames = (Supplier<Set<String>>)Suppliers.memoize(() -> (Set)((Map)this.properties.get()).keySet().stream().map(this::getRepoName).collect(Collectors.toSet()));
    private final Map<String, Set<PropertyTypeCodeDefinition>> typeCodeConfigs = new HashMap<>();


    protected Map<String, String> readProperties()
    {
        return Config.getParametersByPattern("polyglot.repository.config.");
    }


    public Set<String> getRepositoryNames()
    {
        return this.repoNames.get();
    }


    private String getRepoName(String param)
    {
        Matcher m = REPO_NAME_PATTERN.matcher(param);
        if(m.matches())
        {
            return m.group("repoName");
        }
        throw new IllegalArgumentException("Incorrect parameter name pattern for parameter '" + param + "'");
    }


    private Set<PropertyTypeCodeDefinition> getTypeCodes(String values)
    {
        return (values != null) ? (Set<PropertyTypeCodeDefinition>)Splitter.on(
                        ',').trimResults().omitEmptyStrings().splitToList(values).stream().flatMap(value -> string2PropertyTypeCodeDefinition(value).stream()).collect(Collectors.toSet()) : Collections.<PropertyTypeCodeDefinition>emptySet();
    }


    private Set<PropertyTypeCodeDefinition> string2PropertyTypeCodeDefinition(String typeCode)
    {
        Matcher m = TYPE_CODE_PATTERN.matcher(typeCode);
        Set<PropertyTypeCodeDefinition> returnSet = new HashSet<>();
        if(m.matches())
        {
            if(StringUtils.isEmpty(m.group("condition")))
            {
                returnSet.add(new PropertyTypeCodeDefinition(m.group("typeCode"), "", ""));
                return returnSet;
            }
            Matcher m2 = MULTIPLE_CONDITION_PATTERN.matcher(m.group("condition"));
            if(m2.matches() && StringUtils.isEmpty(m.group("qualifierType")))
            {
                Arrays.<String>stream(m2.group("qualifier").split(";"))
                                .forEach(qualifier -> returnSet.add(new PropertyTypeCodeDefinition(m.group("typeCode"), qualifier, "")));
                return returnSet;
            }
            if(StringUtils.isEmpty(m.group("qualifierType")))
            {
                returnSet.add(new PropertyTypeCodeDefinition(m.group("typeCode"), m.group("qualifier"), ""));
                return returnSet;
            }
            returnSet.add(new PropertyTypeCodeDefinition(m.group("typeCode"), m.group("qualifier"), m
                            .group("qualifierType")));
            return returnSet;
        }
        throw new IllegalArgumentException("Incorrect typeCode definition pattern for typeCode '" + typeCode + "'");
    }


    public Set<PropertyTypeCodeDefinition> getTypeCodeDefinitions(String repoName)
    {
        return this.typeCodeConfigs
                        .computeIfAbsent(repoName, repo -> getTypeCodes((String)((Map)this.properties.get()).get("polyglot.repository.config." + repo + ".typeCodes")));
    }


    public String getBeanName(String repoName)
    {
        return (String)((Map)this.properties.get()).get("polyglot.repository.config." + repoName + ".beanName");
    }
}
