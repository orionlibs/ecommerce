package de.hybris.platform.core.initialization;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class SystemSetupCollectorResult
{
    private static final HashFunction MD5 = Hashing.md5();
    private static final Logger log = Logger.getLogger(SystemSetupCollectorResult.class);
    private final Object object;
    private final Method method;
    private final SystemSetup classAnnotation;
    private final SystemSetup methodAnnotation;
    private final String extensionName;
    private final String beanId;
    private final String name;
    private final String description;
    private final boolean required;
    private final boolean patch;
    private final String hash;


    public SystemSetupCollectorResult(SystemSetup classAnnotation, SystemSetup methodAnnotation, Object importObject, String extensionName, String beanId, Method method, String name, String description, boolean required, boolean patch)
    {
        this.object = importObject;
        this.method = method;
        this.classAnnotation = classAnnotation;
        this.methodAnnotation = methodAnnotation;
        this.extensionName = extensionName;
        this.beanId = beanId;
        this.name = StringUtils.isEmpty(name) ? (beanId + "#" + beanId) : name;
        this.description = description;
        this.required = required;
        this.patch = patch;
        this.hash = computePatchHash();
    }


    private String computePatchHash()
    {
        String toHash = this.object.getClass().getCanonicalName() + this.object.getClass().getCanonicalName() + this.method.getName() + this.extensionName;
        return MD5.hashBytes(toHash.getBytes()).toString();
    }


    public Object getObject()
    {
        return this.object;
    }


    public Method getMethod()
    {
        return this.method;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getName()
    {
        return this.name;
    }


    public String getDescription()
    {
        return this.description;
    }


    public boolean isRequired()
    {
        return this.required;
    }


    public boolean isPatch()
    {
        return this.patch;
    }


    public String getHash()
    {
        return this.hash;
    }


    public SystemSetup.Type getType()
    {
        if(!this.methodAnnotation.type().equals(SystemSetup.Type.NOTDEFINED))
        {
            return this.methodAnnotation.type();
        }
        if(!this.classAnnotation.type().equals(SystemSetup.Type.NOTDEFINED))
        {
            return this.classAnnotation.type();
        }
        return SystemSetup.Type.ALL;
    }


    public SystemSetup.Process getProcess()
    {
        if(!this.methodAnnotation.process().equals(SystemSetup.Process.NOTDEFINED))
        {
            return this.methodAnnotation.process();
        }
        if(!this.classAnnotation.process().equals(SystemSetup.Process.NOTDEFINED))
        {
            return this.classAnnotation.process();
        }
        return SystemSetup.Process.ALL;
    }
}
