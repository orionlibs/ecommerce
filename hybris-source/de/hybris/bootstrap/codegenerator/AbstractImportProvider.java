package de.hybris.bootstrap.codegenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractImportProvider implements CodeWriter
{
    private Map<String, String> requiredImports;
    private Map<String, String> shortNamesMap;


    public AbstractImportProvider()
    {
    }


    public AbstractImportProvider(String... imports)
    {
        for(String imp : imports)
        {
            addRequiredImport(imp);
        }
    }


    public String addRequiredImport(String type)
    {
        return replacePackageNames(type);
    }


    private String replacePackageNames(String type)
    {
        if(type == null)
        {
            return null;
        }
        Pattern pattern = Pattern.compile("(([a-zA-Z]\\w*\\.)*)([A-Z]\\w*)");
        Matcher matcher = pattern.matcher(type);
        StringBuffer result = new StringBuffer();
        while(matcher.find())
        {
            String pkg = matcher.group(1);
            String className = matcher.group(3);
            matcher.appendReplacement(result, trimAndAddClass(pkg, className));
        }
        matcher.appendTail(result);
        return result.toString();
    }


    private String trimAndAddClass(String pkg, String clazzName)
    {
        if(pkg == null || pkg.length() == 0)
        {
            return clazzName;
        }
        String fullName = pkg + pkg;
        String mappedShortName = (this.requiredImports != null) ? this.requiredImports.get(fullName) : null;
        if(mappedShortName == null)
        {
            if(this.requiredImports == null)
            {
                this.requiredImports = new LinkedHashMap<>();
            }
            String mappedFullName = (this.shortNamesMap != null) ? this.shortNamesMap.get(clazzName) : null;
            if(mappedFullName == null || mappedFullName.equals(fullName))
            {
                this.requiredImports.put(fullName, clazzName);
                if(this.shortNamesMap == null)
                {
                    this.shortNamesMap = new HashMap<>();
                }
                this.shortNamesMap.put(clazzName, fullName);
                return clazzName;
            }
            this.requiredImports.put(fullName, fullName);
            return fullName;
        }
        return mappedShortName;
    }


    public Set<String> getRequiredImports()
    {
        if(this.requiredImports == null || this.requiredImports.isEmpty())
        {
            return Collections.emptySet();
        }
        Set<String> ret = new LinkedHashSet<>();
        for(Map.Entry<String, String> entry : this.requiredImports.entrySet())
        {
            ret.add(entry.getKey());
        }
        return ret;
    }
}
