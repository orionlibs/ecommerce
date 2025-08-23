package de.hybris.platform.commons.jalo.translator;

import de.hybris.platform.commons.translator.parsers.AbstractParser;
import de.hybris.platform.commons.translator.parsers.HtmlSimpleParser;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class JaloTranslatorConfiguration extends GeneratedJaloTranslatorConfiguration
{
    private static final Logger LOG = Logger.getLogger(JaloTranslatorConfiguration.class.getName());


    public Properties getRenderersPropertiesAsMap()
    {
        Collection<RenderersProperty> propertiesList = getRenderersProperties();
        Properties properties = new Properties();
        if(propertiesList != null)
        {
            for(RenderersProperty renderersProperty : propertiesList)
            {
                String key = renderersProperty.getKey();
                if(key != null)
                {
                    String value = renderersProperty.getValue();
                    if(value == null)
                    {
                        value = "";
                    }
                    properties.setProperty(key, value);
                }
            }
        }
        return properties;
    }


    public Map<String, AbstractParser> getParserPropertiesAsMap()
    {
        Collection<ParserProperty> propertiesList = getParserProperties();
        Map<String, AbstractParser> map = new HashMap<>();
        if(propertiesList != null)
        {
            for(ParserProperty parserProperty : propertiesList)
            {
                AbstractParser abstractParser;
                String name = parserProperty.getName();
                String startExp = parserProperty.getStartExp();
                String endExp = parserProperty.getEndExp();
                String parserClass = parserProperty.getParserClass();
                if(endExp == null && startExp != null)
                {
                    endExp = startExp;
                }
                try
                {
                    abstractParser = createParser(parserClass, name, startExp, endExp);
                }
                catch(Exception e)
                {
                    LOG.error("Could not create a parser for Class [" + parserClass + "] and name [" + name + "], startExp [" + startExp + "], endExp [" + endExp + "]");
                    continue;
                }
                map.put(abstractParser.getStartExpression(), abstractParser);
            }
        }
        return map;
    }


    private AbstractParser createParser(String parserClass, String name, String startExp, String endExp) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        if(StringUtils.isNotBlank(parserClass))
        {
            AbstractParser parser = (AbstractParser)Class.forName(parserClass).newInstance();
            parser.setName(name);
            parser.setStart(startExp);
            parser.setEnd(endExp);
            return parser;
        }
        return (AbstractParser)new HtmlSimpleParser(name, startExp, endExp);
    }
}
