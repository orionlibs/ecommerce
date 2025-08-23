package de.hybris.platform.servicelayer.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class XSSFilter implements Filter
{
    protected static Logger LOG = Logger.getLogger(XSSFilter.class.getName());
    public static final String CONFIG_PARAM_PREFIX = "xss.filter.";
    public static final String CONFIG_RULE_PREFIX_REGEXP = "xss\\.filter\\.rule\\..*(?i)";
    public static final String CONFIG_HEADER_PREFIX_REGEXP = "xss\\.filter\\.header\\..*(?i)";
    protected static final String CONFIG_HEADER_PREFIX = "xss.filter.header.";
    public static final String CONFIG_ENABLED = "xss.filter.enabled";
    public static final String CONFIG_SORT = "xss.filter.sort.rules";
    public static final String CONFIG_ACTION = "xss.filter.action";
    public static final String HOST_HEADER_WHITE_LIST_PREFIX = "header\\.host\\.allow\\.(.*)";
    protected static final String REJECTED_REQUEST_RESP_CONTENT = "Invalid request.";
    private final Pattern NULL_CHAR = Pattern.compile("\000");
    private volatile boolean enabled;
    private List<Pattern> patterns;
    private Map<String, String> headers;
    private XSSValueTranslator lazyValueTranslator;
    private XSSMatchAction actionOnPatternMatch;
    private String webroot;
    private XSSFilterConfig config;
    private final List<Pattern> hostHeaderWhiteList = new ArrayList<>();


    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.webroot = filterConfig.getServletContext().getContextPath();
        initFromConfig((XSSFilterConfig)new DefaultXSSFilterConfig(filterConfig));
    }


    protected void initFromConfig(XSSFilterConfig config)
    {
        this.config = config;
        this.actionOnPatternMatch = config.getActionOnMatch();
        initPatternsAndHeaders(this.config.isEnabled(), this.config.getPatternDefinitions(), this.config.getHeadersToInject());
        config.registerForConfigChanges(this);
        initHostHeaderWhiteList(config.getHostHeaderWhiteList());
    }


    private void initHostHeaderWhiteList(Map<String, String> hostHeadersWhiteList)
    {
        if(hostHeadersWhiteList != null && CollectionUtils.isNotEmpty(hostHeadersWhiteList.values()))
        {
            this.hostHeaderWhiteList.addAll((Collection<? extends Pattern>)hostHeadersWhiteList.values().stream().map(Pattern::compile).collect(Collectors.toList()));
        }
    }


    protected void initPatternsAndHeaders(boolean enabled, Map<String, String> patternDefinitions, Map<String, String> headers)
    {
        if(enabled)
        {
            this.patterns = compilePatterns(patternDefinitions);
            if(MapUtils.isNotEmpty(headers))
            {
                this.headers = new LinkedHashMap<>();
                for(Map.Entry<String, String> e : headers.entrySet())
                {
                    this.headers.put(((String)e.getKey()).replaceAll("xss.filter.header.", ""), e.getValue());
                }
            }
            else
            {
                this.headers = Collections.EMPTY_MAP;
            }
            this.lazyValueTranslator = (XSSValueTranslator)new DefaultXSSValueTranslator(this.patterns);
            this.enabled = true;
        }
        else
        {
            this.patterns = Collections.EMPTY_LIST;
            this.headers = Collections.EMPTY_MAP;
            this.enabled = false;
        }
    }


    public void reloadOnConfigChange()
    {
        String infoBefore = getSetupInfo();
        this.actionOnPatternMatch = this.config.getActionOnMatch();
        initPatternsAndHeaders(this.config.isEnabled(), this.config.getPatternDefinitions(), this.config.getHeadersToInject());
        String infoAfter = getSetupInfo();
        LOG.warn("XSS filter setup changed for " + this.webroot + " from " + infoBefore + " to " + infoAfter + " witch action " + this.actionOnPatternMatch);
    }


    protected List<Pattern> compilePatterns(Map<String, String> rules)
    {
        List<Pattern> ret = new ArrayList<>(rules.size() + 1);
        ret.add(this.NULL_CHAR);
        List<String> keys = new ArrayList<>(rules.keySet());
        if(this.config.sortRules())
        {
            Collections.sort(keys);
        }
        for(String key : keys)
        {
            String expr = rules.get(key);
            if(StringUtils.isNotBlank(expr))
            {
                try
                {
                    ret.add(Pattern.compile(expr));
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("loaded xss filter rule " + key + "=\"" + StringEscapeUtils.escapeJava(expr) + "\"");
                    }
                }
                catch(IllegalArgumentException e)
                {
                    LOG.error("error parsing xss filter rule " + key + "=\"" + StringEscapeUtils.escapeJava(expr) + "\"", e);
                }
            }
        }
        return ret;
    }


    protected String getSetupInfo()
    {
        return "enabled:" + this.enabled + "#patterns:" + (
                        (this.patterns != null) ? ("" + this.patterns.size()) : "<n/a>");
    }


    private boolean matchesWhiteList(String hostHeader)
    {
        if(this.hostHeaderWhiteList.isEmpty())
        {
            return true;
        }
        boolean matchedHost = false;
        for(Pattern p : this.hostHeaderWhiteList)
        {
            if(p.matcher(hostHeader).matches())
            {
                matchedHost = true;
            }
        }
        return matchedHost;
    }


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        if(matchesWhiteList(servletRequest.getServerName()))
        {
            if(this.enabled)
            {
                injectHeaders(servletResponse);
                processPatternsAndDoFilter(servletRequest, servletResponse, filterChain);
            }
            else
            {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
        else
        {
            resp.setStatus(400);
            resp.getWriter().write("Invalid request.");
        }
    }


    private void injectHeaders(ServletResponse servletResponse)
    {
        if(MapUtils.isNotEmpty(this.headers) && servletResponse instanceof HttpServletResponse)
        {
            HttpServletResponse resp = (HttpServletResponse)servletResponse;
            for(Map.Entry<String, String> me : this.headers.entrySet())
            {
                resp.setHeader(me.getKey(), me.getValue());
            }
        }
    }


    protected void processPatternsAndDoFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        if(CollectionUtils.isNotEmpty(this.patterns))
        {
            Map<String, String[]> originalParameterMap, parameters;
            HttpServletRequest req = (HttpServletRequest)servletRequest;
            switch(null.$SwitchMap$de$hybris$platform$servicelayer$web$XSSMatchAction[this.actionOnPatternMatch.ordinal()])
            {
                case 1:
                    originalParameterMap = req.getParameterMap();
                    parameters = this.lazyValueTranslator.translateParameters(originalParameterMap);
                    if(originalParameterMap == parameters)
                    {
                        Map<String, String[]> originalHeaderMap = getValueMapFromHeaders(req);
                        Map<String, String[]> headers = this.lazyValueTranslator.translateHeaders(originalHeaderMap);
                        if(originalHeaderMap == headers)
                        {
                            filterChain.doFilter((ServletRequest)req, servletResponse);
                            return;
                        }
                    }
                    setRejectResponseCodes((HttpServletResponse)servletResponse);
                    return;
                case 2:
                    filterChain.doFilter((ServletRequest)new XSSRequestWrapper(req, this.lazyValueTranslator), servletResponse);
                    return;
            }
            throw new UnsupportedOperationException("Match action type " + this.actionOnPatternMatch + " is not yet implemented.");
        }
    }


    protected void setRejectResponseCodes(HttpServletResponse httpResponse) throws IOException
    {
        httpResponse.setStatus(400);
        httpResponse.sendError(400);
    }


    public void destroy()
    {
        if(this.config != null)
        {
            this.config.unregisterConfigChangeListener();
            this.config = null;
        }
        this.patterns = null;
        this.enabled = false;
    }


    private static final String[] EMPTY = new String[0];


    static Map<String, String[]> getValueMapFromHeaders(HttpServletRequest request)
    {
        Map<String, String[]> headerValueMap = null;
        for(Enumeration<String> headerEnum = request.getHeaderNames(); headerEnum.hasMoreElements(); )
        {
            String header = headerEnum.nextElement();
            Enumeration<String> valuesEnum = request.getHeaders(header);
            if(valuesEnum != null)
            {
                if(headerValueMap == null)
                {
                    headerValueMap = (Map)new LinkedHashMap<>();
                }
                List<String> valueList = Collections.list(valuesEnum);
                headerValueMap.put(header, valueList.isEmpty() ? EMPTY : valueList.<String>toArray(new String[valueList.size()]));
            }
        }
        return (headerValueMap == null) ? Collections.EMPTY_MAP : headerValueMap;
    }
}
