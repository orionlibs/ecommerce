package de.hybris.platform.webservicescommons.jaxb.adapters;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSSStringAdapter extends XmlAdapter<String, String>
{
    private static final Logger LOG = LoggerFactory.getLogger(XSSStringAdapter.class);
    private static final Pattern NULL_CHAR = Pattern.compile("\000");
    private final ConfigIntf cfg = Registry.getMasterTenant().getConfig();
    private List<Pattern> xssPatternDefinitions;
    private boolean xssFilterEnabled;


    public XSSStringAdapter()
    {
        initXSSSettings();
    }


    protected final void initXSSSettings()
    {
        this.xssFilterEnabled = isXSSFilterEnabled();
        if(this.xssFilterEnabled)
        {
            this.xssPatternDefinitions = compilePatterns(getPatternDefinitions());
        }
    }


    public String marshal(String d)
    {
        return d;
    }


    public String unmarshal(String d)
    {
        if(this.xssFilterEnabled)
        {
            return stripXSS(d);
        }
        return d;
    }


    protected boolean isXSSFilterEnabled()
    {
        String enabledForExtension = this.cfg.getParameter("webservicescommons.xss.filter.enabled");
        if(StringUtils.isBlank(enabledForExtension))
        {
            return this.cfg.getBoolean("xss.filter.enabled", true);
        }
        return Boolean.parseBoolean(enabledForExtension);
    }


    protected Map<String, String> getPatternDefinitions()
    {
        Map<Object, Object> rules = new LinkedHashMap<>(this.cfg.getParametersMatching("xss\\.filter\\.rule\\..*(?i)"));
        rules.putAll(this.cfg.getParametersMatching("webservicescommons\\.(xss\\.filter\\.rule\\..*(?i))", true));
        return (Map)rules;
    }


    protected List<Pattern> compilePatterns(Map<String, String> rules)
    {
        ArrayList<Pattern> patterns = new ArrayList(rules.size() + 1);
        patterns.add(NULL_CHAR);
        Iterator<Map.Entry> rulesIterator = rules.entrySet().iterator();
        while(rulesIterator.hasNext())
        {
            Map.Entry rule = rulesIterator.next();
            addCompiledRule(patterns, rule);
        }
        return patterns;
    }


    protected void addCompiledRule(List<Pattern> patterns, Map.Entry rule)
    {
        String expr = (String)rule.getValue();
        if(StringUtils.isNotBlank(expr))
        {
            try
            {
                patterns.add(Pattern.compile(expr));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("loaded xss rule {} = \"{}\"", rule.getKey(), StringEscapeUtils.escapeJava((String)rule.getValue()));
                }
            }
            catch(IllegalArgumentException ex)
            {
                LOG.error("error parsing xss rule " + rule, ex);
            }
        }
    }


    protected String stripXSS(String value)
    {
        String processedValue = value;
        if(processedValue != null && !processedValue.isEmpty())
        {
            Iterator<Pattern> patternsIterator = this.xssPatternDefinitions.iterator();
            while(patternsIterator.hasNext())
            {
                Pattern scriptPattern = patternsIterator.next();
                processedValue = scriptPattern.matcher(processedValue).replaceAll("");
            }
        }
        return processedValue;
    }
}
