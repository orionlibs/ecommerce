package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.ArrayList;
import java.util.Collection;

@Deprecated(since = "1818", forRemoval = true)
public abstract class AbstractDtoWriter extends ClassWriter
{
    protected static final String XML_ROOTELEMENT = "javax.xml.bind.annotation.XmlRootElement";
    protected static final String XML_ATTRIBUTE = "javax.xml.bind.annotation.XmlAttribute";
    protected static final String XML_ELEMENT = "javax.xml.bind.annotation.XmlElement";
    protected static final String XML_ELEMENTWRAPPER = "javax.xml.bind.annotation.XmlElementWrapper";
    private final DtoConfig dtoConfig;
    protected WebservicesConfig wsConfig;


    protected AbstractDtoWriter(WebservicesConfig wsConfig, DtoConfig cfg, CodeGenerator gen, YExtension ext)
    {
        super(gen, ext, null);
        this.dtoConfig = cfg;
        this.wsConfig = wsConfig;
        setPackageName(this.dtoConfig.getDtoPackage());
        addRequiredImport("javax.xml.bind.annotation.XmlRootElement");
        setModifiers(0);
        setVisibility(Visibility.PUBLIC);
        String copyright = getCopyright();
        setCopyright(GENERATED_NOTICE + GENERATED_NOTICE);
        addAnnotation("SuppressWarnings(\"all\")");
    }


    public DtoConfig getDtoConfig()
    {
        return this.dtoConfig;
    }


    public String getClassName()
    {
        return getDtoConfig().getDtoClassSimpleName();
    }


    protected void fill()
    {
        Collection<MethodWriter> methods = null;
        methods = getConstructorMethodWriters();
        addMethods(methods);
        methods = getBeanMethodWriters();
        addMethods(methods);
    }


    protected Collection<MethodWriter> getConstructorMethodWriters()
    {
        Collection<MethodWriter> result = new ArrayList<>();
        result.add(createDefaultConstructorMethodWriter());
        return result;
    }


    protected Collection<MethodWriter> getBeanMethodWriters()
    {
        return new ArrayList<>();
    }


    protected MethodWriter createDefaultConstructorMethodWriter()
    {
        MethodWriter result = new MethodWriter(Visibility.PUBLIC, null, getClassName());
        result.setContentPlain("super();");
        result.setConstructor(true);
        result.setJavadoc("<i>Generated constructor</i> - for generic creation.");
        return result;
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
}
