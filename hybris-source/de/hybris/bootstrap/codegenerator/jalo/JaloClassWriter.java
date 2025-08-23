package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JaloClassWriter extends ClassWriter
{
    private final String constantsClassName;
    private final boolean jaloLogicFree;


    public JaloClassWriter(CodeGenerator gen, YExtension ext, String className)
    {
        super(gen, ext, className);
        this.constantsClassName = ConstantsWriter.assembleConstantsClassName(gen, ext);
        this.jaloLogicFree = false;
    }


    public JaloClassWriter(CodeGenerator gen, YExtension ext, String className, boolean jaloLogicFree)
    {
        super(gen, ext, className);
        this.constantsClassName = ConstantsWriter.assembleConstantsClassName(gen, ext);
        this.jaloLogicFree = jaloLogicFree;
    }


    public String getConstantsClassName()
    {
        return this.constantsClassName;
    }


    public boolean isJaloLogicFree()
    {
        return this.jaloLogicFree;
    }


    protected List<MethodWriter> sortMethods(List<MethodWriter> methods)
    {
        if(methods.isEmpty() || methods.size() == 1)
        {
            return methods;
        }
        List<MethodWriter> ret = new ArrayList<>(methods);
        Collections.sort(ret, (object1, object2) -> {
            String name1;
            String name2;
            if(object1 instanceof AbstractAttributeMethodWriter)
            {
                name1 = ((AbstractAttributeMethodWriter)object1).getAttribute().getQualifier();
            }
            else if(object1 instanceof AttributeMethodDelegate)
            {
                name1 = ((AttributeMethodDelegate)object1).getTarget().getAttribute().getQualifier();
            }
            else
            {
                name1 = object1.getName();
            }
            if(object2 instanceof AbstractAttributeMethodWriter)
            {
                name2 = ((AbstractAttributeMethodWriter)object2).getAttribute().getQualifier();
            }
            else if(object2 instanceof AttributeMethodDelegate)
            {
                name2 = ((AttributeMethodDelegate)object2).getTarget().getAttribute().getQualifier();
            }
            else
            {
                name2 = object2.getName();
            }
            return name1.compareToIgnoreCase(name2);
        });
        return ret;
    }


    protected void addAnnotations()
    {
        if(containsAnnotation("SuppressWarnings"))
        {
            return;
        }
        if(("Generated" + getClassName()).equals(getClassToExtend()))
        {
            addAnnotation("SuppressWarnings({\"deprecation\",\"squid:CallToDeprecatedMethod\"})");
        }
    }
}
