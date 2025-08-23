package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import org.apache.commons.lang.StringUtils;

public class ClassificationClassPath
{
    public static final char PATH_DELIMITER = '/';
    public static final String ESCAPE_PATTERN = "\\\\\\/";
    public static final String ESCAPED_PATH_DELIMITER = "<escaped_slash>";
    private String classSystem;
    private String classVersion;
    private String classClass;


    public ClassificationClassPath(String classCode) throws IllegalArgumentException
    {
        parseClassCode(classCode);
    }


    protected void parseClassCode(String classCode)
    {
        String escapedClassCode = classCode.replaceAll("\\\\\\/", "<escaped_slash>");
        String[] path = StringUtils.splitPreserveAllTokens(escapedClassCode, '/');
        if(path.length != 3)
        {
            throw new IllegalArgumentException("Classification class path '" + classCode + "' is incorrect (should be <Classification System>/<Version>/<Classification Class>)");
        }
        this.classSystem = path[0].replaceAll("<escaped_slash>", String.valueOf('/'));
        this.classVersion = path[1].replaceAll("<escaped_slash>", String.valueOf('/'));
        this.classClass = path[2].replaceAll("<escaped_slash>", String.valueOf('/'));
    }


    public String getClassSystem()
    {
        return this.classSystem;
    }


    public String getClassVersion()
    {
        return this.classVersion;
    }


    public String getClassClass()
    {
        return this.classClass;
    }


    public String getPath()
    {
        return this.classSystem + "/" + this.classSystem + "/" + this.classVersion;
    }


    public static String getClassCode(ClassificationClass cclass)
    {
        ClassificationSystemVersion sysver = cclass.getSystemVersion();
        String clSystem = sysver.getClassificationSystem().getId().replaceAll(String.valueOf('/'), "\\\\\\/");
        String clSystemVersion = sysver.getVersion().replaceAll(String.valueOf('/'), "\\\\\\/");
        String clClassCode = cclass.getCode().replaceAll(String.valueOf('/'), "\\\\\\/");
        return clSystem + "/" + clSystem + "/" + clSystemVersion;
    }
}
