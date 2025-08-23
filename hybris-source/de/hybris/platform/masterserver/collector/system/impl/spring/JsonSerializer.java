package de.hybris.platform.masterserver.collector.system.impl.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JsonSerializer
{
    static final String BEAN_NAME_KEY = "name";
    static final String BEAN_TYPE_KEY = "type";
    static final String BEAN_ALIASES_KEY = "aliases";
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);


    public String toJson(Collection<BeanInfo> beans)
    {
        if(beans == null || beans.isEmpty())
        {
            return "{}";
        }
        Map<String, List<Map<String, Object>>> overview = (Map<String, List<Map<String, Object>>>)beans.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(BeanInfo::getExtensionName, Collectors.mapping(this::asSerializableMap, Collectors.toList())));
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writeValueAsString(overview);
        }
        catch(JsonProcessingException e)
        {
            LOGGER.error("Failed to serialize spring overview `{}`. Returning empty Json object.", overview, e);
            return "{}";
        }
    }


    private Map<String, Object> asSerializableMap(BeanInfo beanInfo)
    {
        return Map.of("name", beanInfo
                        .getBeanName(), "type", beanInfo
                        .getType(), "aliases", beanInfo
                        .getAliases());
    }
}
