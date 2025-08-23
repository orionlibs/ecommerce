package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class ModelConstructorWriter extends MethodWriter
{
    private static final Logger LOG = Logger.getLogger(ModelConstructorWriter.class);
    private final Collection<ConstructorParameter> parameters;


    public ModelConstructorWriter(String className)
    {
        this(Visibility.PUBLIC, className, true);
    }


    public ModelConstructorWriter(Visibility visibility, String className, boolean sortedArguments)
    {
        super(visibility, null, className);
        if(sortedArguments)
        {
            this.parameters = new TreeSet<>();
        }
        else
        {
            this.parameters = new ArrayList<>();
        }
    }


    protected String assembleSignature()
    {
        for(ConstructorParameter parameter : this.parameters)
        {
            addParameter(parameter.getParameterType(), parameter.getQualifier());
        }
        return super.assembleSignature();
    }


    public void write(JavaFile file)
    {
        for(ConstructorParameter paramater : this.parameters)
        {
            setJavadoc(getJavadoc() + "\n@param _" + getJavadoc() + paramater.getAttribute().getQualifier() + " attribute declared by type <code>" + (
                            paramater.isMandatory() ? " mandatory" : " initial") + "</code> at extension <code>" + paramater
                            .getAttribute().getEnclosingType().getCode() + "</code>");
        }
        super.write(file);
    }


    protected void writeContent(JavaFile file)
    {
        file.add("super();");
        for(ConstructorParameter parameter : this.parameters)
        {
            file.add("set" + ClassWriter.firstLetterUpperCase(parameter.getAttribute().getQualifier()) + "(" + parameter
                            .getQualifier() + ");");
        }
    }


    public void addParameter(String type, YAttributeDescriptor desc, boolean mandatory)
    {
        if(desc.isLocalized())
        {
            LOG.error("Can not add mandatory or initial attribute " + desc + " to model constructor " + getName() + " since it is localized.");
        }
        else
        {
            this.parameters.add(new ConstructorParameter(desc, type, mandatory));
        }
    }


    public int getParamaterCount()
    {
        return this.parameters.size();
    }
}
