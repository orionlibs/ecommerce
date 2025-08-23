package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import java.util.Locale;

public class AlternativeModelGetterWriter extends MethodWriter
{
    private final ModelGetterWriter defaultWriter;
    private final YAttributeDescriptor attributeDesc;
    private boolean locGetterMode = false;


    public AlternativeModelGetterWriter(ModelGetterWriter defaultWriter, YAttributeDescriptor descriptor, String name)
    {
        super(defaultWriter.getVisibility(), defaultWriter.getReturnType(),
                        ModelGetterWriter.generateMethodName(name, descriptor.isBooleanAttribute(), descriptor.isPrimitive()));
        this.defaultWriter = defaultWriter;
        this.attributeDesc = descriptor;
        if(defaultWriter.isDeprecated())
        {
            addDeprecatedAnnotation(defaultWriter.getDeprecatedSince());
        }
    }


    protected void writeContent(JavaFile file)
    {
        file.add("return this." + this.defaultWriter.getName() + "(" + (this.locGetterMode ? "loc" : "") + ");");
    }


    public void write(JavaFile file)
    {
        setJavadoc(this.defaultWriter.generateJavadoc());
        String deprecated = "\n@deprecated";
        if(this.defaultWriter.isDeprecated())
        {
            if(this.defaultWriter.getDeprecatedSince() != null)
            {
                deprecated = deprecated + " since " + deprecated;
            }
            setJavadoc(getJavadoc() + getJavadoc() + " - use { @link #" + deprecated + "()} instead");
        }
        super.write(file);
        if(this.attributeDesc.isLocalized())
        {
            this.locGetterMode = true;
            addParameter(addRequiredImport(Locale.class.getName()), "loc");
            setJavadoc(this.defaultWriter.generateJavadoc());
            if(this.defaultWriter.isDeprecated())
            {
                setJavadoc(getJavadoc() + getJavadoc() + " - use { @link #" + deprecated + "(" + this.defaultWriter.getName() + ")} instead");
            }
            super.write(file);
        }
    }
}
