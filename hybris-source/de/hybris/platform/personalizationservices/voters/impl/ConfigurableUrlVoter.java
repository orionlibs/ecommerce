package de.hybris.platform.personalizationservices.voters.impl;

import de.hybris.platform.personalizationservices.RecalculateAction;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.model.config.CxUrlVoterConfigModel;
import de.hybris.platform.personalizationservices.voters.Vote;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ConfigurableUrlVoter extends AbstractVoter
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurableUrlVoter.class);
    public static final int CONFIGURABLE_URL_PRECEDENCE = 100;
    private CxConfigurationService cxConfigurationService;
    private final Map<String, Pattern> patternCache = new HashMap<>();


    public ConfigurableUrlVoter()
    {
        super(100);
    }


    public ConfigurableUrlVoter(int order)
    {
        super(order);
    }


    public Vote getVote(HttpServletRequest request, HttpServletResponse response)
    {
        Vote result = getDefaultVote();
        List<CxUrlVoterConfigModel> urlVoterConfigurations = this.cxConfigurationService.getUrlVoterConfigurations();
        try
        {
            String requestURI = request.getRequestURI();
            String decodedRequestURI = URLDecoder.decode(requestURI, "UTF-8");
            urlVoterConfigurations.stream()
                            .filter(c -> matches(c.getUrlRegexp(), decodedRequestURI))
                            .forEach(c -> addRecalculateActions(result, c.getActions()));
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.debug("Failed to process url", e);
        }
        return result;
    }


    protected boolean matches(String regexp, String key)
    {
        Pattern urlPattern = getUrlPattern(regexp);
        if(urlPattern != null)
        {
            Matcher matcher = urlPattern.matcher(key);
            return matcher.find();
        }
        return false;
    }


    protected void addRecalculateActions(Vote vote, Collection<String> actions)
    {
        Objects.requireNonNull(vote.getRecalculateActions());
        actions.stream().filter(this::actionExist).map(RecalculateAction::valueOf).forEach(vote.getRecalculateActions()::add);
    }


    protected Pattern getUrlPattern(String regex)
    {
        if(StringUtils.isBlank(regex))
        {
            return null;
        }
        if(this.patternCache.containsKey(regex))
        {
            return this.patternCache.get(regex);
        }
        return compilePattern(regex);
    }


    protected Pattern compilePattern(String regex)
    {
        Pattern pattern = null;
        try
        {
            pattern = Pattern.compile(regex);
        }
        catch(PatternSyntaxException e)
        {
            LOG.error("Incorrect regexp defined for url voter ", e);
        }
        this.patternCache.put(regex, pattern);
        return pattern;
    }


    protected boolean actionExist(String actionName)
    {
        try
        {
            RecalculateAction.valueOf(actionName);
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Recalculate action doesn't exist :" + actionName, e);
            return false;
        }
        return true;
    }


    protected CxConfigurationService getCxConfigurationService()
    {
        return this.cxConfigurationService;
    }


    @Required
    public void setCxConfigurationService(CxConfigurationService cxConfigurationService)
    {
        this.cxConfigurationService = cxConfigurationService;
    }
}
