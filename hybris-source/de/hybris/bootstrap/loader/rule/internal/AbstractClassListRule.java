package de.hybris.bootstrap.loader.rule.internal;

import de.hybris.bootstrap.loader.rule.IgnoreClassLoadingRule;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractClassListRule implements IgnoreClassLoadingRule
{
    Set<String> classesTypes = new HashSet<>();
    Set<String> packagesTypes = new HashSet<>();


    public void initialize(String param)
    {
        for(String wType : parseWhitelistTypes(param))
        {
            if(wType.endsWith("*"))
            {
                this.packagesTypes.add(wType.substring(0, wType.lastIndexOf('.')));
                continue;
            }
            this.classesTypes.add(wType);
        }
    }


    protected IgnoreClassLoadingRule.IgnoredStatus isIgnored(String name, IgnoreClassLoadingRule.IgnoredStatus returnStatus)
    {
        if(this.classesTypes.contains(name))
        {
            return returnStatus;
        }
        if(!this.packagesTypes.isEmpty())
        {
            int indexOfDot = name.lastIndexOf('.');
            if(indexOfDot > 0 && this.packagesTypes.contains(name.substring(0, indexOfDot)))
            {
                return returnStatus;
            }
        }
        return IgnoreClassLoadingRule.IgnoredStatus.UNDEFINED;
    }


    protected Set<String> parseWhitelistTypes(String whitelistTypes)
    {
        if(whitelistTypes.length() == 0)
        {
            return Collections.emptySet();
        }
        Set<String> ret = new HashSet<>();
        for(String token : whitelistTypes.split(","))
        {
            String id = token.trim();
            if(id.length() > 0)
            {
                ret.add(id);
            }
        }
        return Collections.unmodifiableSet(ret);
    }
}
