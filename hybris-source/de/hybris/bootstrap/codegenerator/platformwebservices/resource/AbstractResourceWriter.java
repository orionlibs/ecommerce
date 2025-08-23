package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Deprecated(since = "1818", forRemoval = true)
public abstract class AbstractResourceWriter extends ClassWriter
{
    protected static final String WS_GET = "javax.ws.rs.GET";
    protected static final String WS_PUT = "javax.ws.rs.PUT";
    protected static final String WS_POST = "javax.ws.rs.POST";
    protected static final String WS_DELETE = "javax.ws.rs.DELETE";
    protected static final String WS_RESPONSE = "javax.ws.rs.core.Response";
    public static final String WS_PATH = "javax.ws.rs.Path";
    public static final String WS_PATHPARAM = "javax.ws.rs.PathParam";
    protected static final String ABSTRACT_YRESOURCE = "de.hybris.platform.webservices.AbstractYResource";
    protected static final String READRESOURCE_METHOD = "readResource";
    private static final String EXCEPTION = "java.lang.Exception";
    private final ResourceConfig resourceConfig;


    protected AbstractResourceWriter(ResourceConfig cfg, CodeGenerator gen, YExtension ext)
    {
        super(gen, ext, null);
        this.resourceConfig = cfg;
        addRequiredImport(this.resourceConfig.getDTOConfig().getModelClassName());
        addRequiredImport(this.resourceConfig.getDTOConfig().getDtoClassName());
        setPackageName(this.resourceConfig.getPackageName());
        setModifiers(0);
        setVisibility(Visibility.PUBLIC);
        String copyright = getCopyright();
        setCopyright(GENERATED_NOTICE + GENERATED_NOTICE);
        addAnnotation("SuppressWarnings(\"all\")");
    }


    public String getClassName()
    {
        return getResourceConfig().getSimpleClassName();
    }


    protected ResourceConfig getResourceConfig()
    {
        return this.resourceConfig;
    }


    protected void fill()
    {
        Collection<MethodWriter> methods = null;
        methods = getConstructorMethodWriters();
        addMethods(methods);
        methods = getCRUDMethodWriters();
        addMethods(methods);
        MethodWriter readMethod = getReadResourceMethodWriter();
        if(readMethod != null)
        {
            addMethod(readMethod);
        }
        methods = getSubResourceMethodWriters();
        addMethods(methods);
        methods = getResourceValueMethodWriters();
        addMethods(methods);
        addMethods(getCustomMethodWriters());
    }


    private void addMethods(Collection<MethodWriter> methods)
    {
        for(MethodWriter method : methods)
        {
            if(method.isConstructor())
            {
                addConstructor(method);
                continue;
            }
            addMethod(method);
        }
    }


    protected Collection<MethodWriter> getConstructorMethodWriters()
    {
        MethodWriter result = createDefaultConstructorMethodWriter();
        return Collections.singletonList(result);
    }


    protected Collection<MethodWriter> getCRUDMethodWriters()
    {
        Collection<MethodWriter> result = new ArrayList<>();
        ResourceConfig resourceCfg = getResourceConfig();
        if(resourceCfg.isGetSupport())
        {
            result.add(createGetMethodWriter());
        }
        if(resourceCfg.isDeleteSupport())
        {
            result.add(createDeleteMethodWriter());
        }
        if(resourceCfg.isPutSupport())
        {
            result.add(createPutMethodWriter());
        }
        if(resourceCfg.isPostSupport())
        {
            result.add(createPostMethodWriter());
        }
        return result;
    }


    protected abstract Collection<MethodWriter> getSubResourceMethodWriters();


    protected abstract MethodWriter getReadResourceMethodWriter();


    protected abstract Collection<MethodWriter> getResourceValueMethodWriters();


    protected Collection<MethodWriter> getCustomMethodWriters()
    {
        return Collections.emptyList();
    }


    protected MethodWriter createDefaultConstructorMethodWriter()
    {
        MethodWriter result = new MethodWriter(Visibility.PUBLIC, null, getClassName());
        result.setContentPlain("super(\"" + getResourceConfig().getDTOConfig().getType().getCode() + "\");");
        result.setConstructor(true);
        result.setJavadoc("<i>Generated constructor</i> - for generic creation.");
        return result;
    }


    protected abstract MethodWriter createGetMethodWriter();


    protected abstract MethodWriter createDeleteMethodWriter();


    protected abstract MethodWriter createPutMethodWriter();


    protected abstract MethodWriter createPostMethodWriter();


    protected Collection<MethodWriter> createResourceValueMethodWriters(String valueType, String genericType, String name)
    {
        String finalType = (genericType != null) ? (valueType + "<" + valueType + ">") : valueType;
        MethodWriter getter = new MethodWriter(Visibility.PUBLIC, finalType, "get" + name);
        getter.setContentPlain("return super.getResourceValue();");
        getter.setJavadoc("Convenience method which just delegates to {@link #getResourceValue()}");
        MethodWriter setter = new MethodWriter(Visibility.PUBLIC, null, "set" + name);
        setter.addParameter(finalType, "value");
        setter.setContentPlain("super.setResourceValue(value);");
        setter.setJavadoc("Convenience method which just delegates to {@link #setResourceValue(" + valueType + ")}");
        Collection<MethodWriter> result = new ArrayList<>();
        result.add(getter);
        result.add(setter);
        return result;
    }


    protected MethodWriter createCRUDMethodWriter(String ano, String methodName)
    {
        String anoShort = ano.substring(ano.lastIndexOf('.') + 1);
        MethodWriter result = new MethodWriter(Visibility.PUBLIC, "javax.ws.rs.core.Response", methodName);
        result.addAnnotation(anoShort);
        result.setJavadoc("Generated HTTP method for covering " + anoShort + " requests.\n@return {@link javax.ws.rs.core.Response}\n");
        addRequiredImport(ano);
        return result;
    }


    protected MethodWriter createReadResourceMethodWriter(String returnClassType)
    {
        MethodWriter method = new MethodWriter(Visibility.PROTECTED, null, "readResource");
        method.setReturnType(addRequiredImport(returnClassType));
        method.addParameter("String", "resourceId");
        method.addThrownException("java.lang.Exception");
        method.addAnnotation("Override");
        return method;
    }


    protected MethodWriter createSubResourceMethodWriter(ResourceConfig subResourceCfg, boolean isSingleResource)
    {
        String subResUri = subResourceCfg.getDTOConfig().getType().getCode().toLowerCase();
        String subResourceName = subResourceCfg.getSimpleClassName();
        Object object = new Object(this, Visibility.PUBLIC, "de.hybris.platform.webservices.AbstractYResource", "get" + subResourceName, subResUri);
        object.addRequiredImport("de.hybris.platform.webservices.AbstractYResource");
        object.addRequiredImport(subResourceCfg.getClassName());
        object.addRequiredImport("javax.ws.rs.Path");
        object.addRequiredImport("javax.ws.rs.PathParam");
        object.addParameter("String", "resourceKey");
        object.setJavadoc(" Generated getter for sub resource of type {@link " + subResourceName + "} for current root resource ");
        StringBuilder content = new StringBuilder();
        content.append("final " + subResourceName + "  resource  = resourceCtx.getResource(" + subResourceName + ".class);")
                        .append("\n");
        content.append("resource.setResourceId(resourceKey );").append("\n");
        content.append("resource.setParentResource(this);").append("\n");
        if(isSingleResource)
        {
            content.append("passUniqueMember(resource);").append("\n");
        }
        else
        {
            content.append("super.prepareJaloSession();").append("\n");
        }
        content.append("return resource;");
        object.setContentPlain(content.toString());
        return (MethodWriter)object;
    }
}
