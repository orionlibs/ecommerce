package de.hybris.platform.cmscockpit.url.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public abstract class BaseFrontendRegexUrlDecoder<T> implements FrontendUrlDecoder<T>, InitializingBean
{
    private String regex;
    private Pattern pattern;


    public T decode(String url)
    {
        Matcher matcher = this.pattern.matcher(url);
        if(matcher.find())
        {
            return translateId(matcher.group(0));
        }
        return null;
    }


    protected abstract T translateId(String paramString);


    public void setRegex(String regex)
    {
        Assert.hasLength(regex);
        this.regex = regex;
    }


    public void afterPropertiesSet()
    {
        Assert.hasLength(this.regex);
        this.pattern = Pattern.compile(this.regex);
    }
}
