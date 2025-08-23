package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import java.util.Locale;

public class AlternativeModelSetterWriter extends MethodWriter
{
    private final ModelSetterWriter defaultWriter;
    private final YAttributeDescriptor attributeDesc;
    private boolean locSetterMode = false;


    public AlternativeModelSetterWriter(ModelSetterWriter defaultWriter, YAttributeDescriptor descriptor, String name)
    {
        super(defaultWriter.getVisibility(), defaultWriter.getReturnType(), ModelSetterWriter.generateMethodName(name));
        this.defaultWriter = defaultWriter;
        this.attributeDesc = descriptor;
        if(defaultWriter.isDeprecated())
        {
            addDeprecatedAnnotation(defaultWriter.getDeprecatedSince());
        }
        addParameter(defaultWriter.getDeclaredParameterType(), "value");
    }


    protected void writeContent(JavaFile file)
    {
        file.add("this." + this.defaultWriter.getName() + "(value" + (this.locSetterMode ? ",loc" : "") + ");");
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
            setJavadoc(getJavadoc() + getJavadoc() + " - use { @link #" + deprecated + "(" + this.defaultWriter.getName() + ")} instead");
        }
        super.write(file);
        if(this.attributeDesc.isLocalized())
        {
            this.locSetterMode = true;
            addParameter(addRequiredImport(Locale.class.getName()), "loc");
            setJavadoc(this.defaultWriter.generateJavadoc());
            if(this.defaultWriter.isDeprecated())
            {
                setJavadoc(getJavadoc() + getJavadoc() + " - use { @link #" + deprecated + "(" + this.defaultWriter.getName() + "," + this.defaultWriter
                                .getDeclaredParameterType() + ")} instead");
            }
            super.write(file);
        }
    }
}
