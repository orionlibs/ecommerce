package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.Locale;

public class ModelSetterWriter extends MethodWriter
{
    private final YAttributeDescriptor attributeDesc;
    private final String redeclaredReturnType;
    private final String declaringReturnType;
    private final CodeGenerator gen;
    private boolean locSetterMode = false;
    private final boolean isDeprecated;
    private final String deprecatedSince;


    public ModelSetterWriter(Visibility visibility, CodeGenerator gen, YAttributeDescriptor descriptor)
    {
        this(visibility, gen, descriptor, descriptor.getQualifier(), false, null);
    }


    public ModelSetterWriter(Visibility visibility, CodeGenerator gen, YAttributeDescriptor descriptor, boolean isDeprecated, String deprecatedSince)
    {
        this(visibility, gen, descriptor, descriptor.getQualifier(), isDeprecated, deprecatedSince);
    }


    public ModelSetterWriter(Visibility visibility, CodeGenerator gen, YAttributeDescriptor descriptor, String attributeName, boolean isDeprecated, String deprecatedSince)
    {
        super(visibility, "void", generateMethodName(attributeName));
        this.attributeDesc = descriptor;
        this.gen = gen;
        this.redeclaredReturnType = ModelNameUtils.getModelOrClass(descriptor, gen, true);
        YAttributeDescriptor declaringDescriptor = descriptor.getDeclaringAttribute();
        if(descriptor.equals(declaringDescriptor))
        {
            this.declaringReturnType = this.redeclaredReturnType;
        }
        else
        {
            this.declaringReturnType = ModelNameUtils.getModelOrClass(declaringDescriptor, gen, true);
        }
        if(this.attributeDesc.isRedeclared())
        {
            addAnnotation("Override");
        }
        addParameter(addRequiredImport(this.declaringReturnType), "value");
        this.deprecatedSince = deprecatedSince;
        this.isDeprecated = isDeprecated;
        if(this.isDeprecated)
        {
            addDeprecatedAnnotation(deprecatedSince);
        }
    }


    protected static String generateMethodName(String qualifier)
    {
        return "set" + ClassWriter.firstLetterUpperCase(qualifier);
    }


    protected String generateJavadoc()
    {
        String descr = (this.attributeDesc.getDescription() != null && this.attributeDesc.getDescription().length() > 0) ? (" - " + this.attributeDesc.getDescription()) : "";
        String redeclareDoc = this.attributeDesc.isRedeclared() ? ("Will only accept values of type {@link " + getRedeclaredParameterType() + "}. ") : "";
        return this.attributeDesc.isWritable() ? ("<i>Generated method</i> - Setter of <code>" +
                        this.attributeDesc.getDeclaringType().getCode() + "." + this.attributeDesc
                        .getQualifier() + "</code> " + (
                        (this.attributeDesc.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC) ? "dynamic " : "") + "attribute defined at extension <code>" + ((YExtension)this.attributeDesc
                        .getDeclaringAttribute().getNamespace()).getExtensionName() + "</code>" + (
                        this.attributeDesc.isRedeclared() ? (" and redeclared at extension <code>" + ((YExtension)this.attributeDesc
                                        .getNamespace()).getExtensionName() + "</code>") : "") + ". " + redeclareDoc + "\n \n@param value the " + this.attributeDesc
                        .getQualifier() + descr + (
                        this.locSetterMode ?
                                        "\n@param loc the value localization key \n@throws IllegalArgumentException if localization key cannot be mapped to data language\n" :
                                        "")) : ("<i>Generated method</i> - Initial setter of <code>" +
                        this.attributeDesc.getDeclaringType().getCode() + "." + this.attributeDesc
                        .getQualifier() + "</code> " + (
                        (this.attributeDesc.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC) ? "dynamic " : "") + "attribute defined at extension <code>" + ((YExtension)this.attributeDesc
                        .getDeclaringAttribute().getNamespace()).getExtensionName() + "</code>" + (
                        (this.attributeDesc.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC) ? "Dynamic attribute." : "") + (
                        this.attributeDesc.isRedeclared() ? (" and redeclared at extension <code>" + ((YExtension)this.attributeDesc
                                        .getNamespace()).getExtensionName() + "</code>") : "") + ". Can only be used at creation of model - before first save. " + redeclareDoc + " \n \n@param value the " + this.attributeDesc
                        .getQualifier() + descr + (
                        this.locSetterMode ?
                                        "\n@param loc the value localization key \n@throws IllegalArgumentException if localization key cannot be mapped to data language\n" :
                                        ""));
    }


    public void write(JavaFile file)
    {
        setJavadoc(generateJavadoc());
        String deprecated = "\n@deprecated";
        if(this.isDeprecated)
        {
            if(this.deprecatedSince != null)
            {
                deprecated = deprecated + " since " + deprecated;
            }
            setJavadoc(getJavadoc() + getJavadoc());
        }
        super.write(file);
        if(this.attributeDesc.isLocalized())
        {
            this.locSetterMode = true;
            setJavadoc(generateJavadoc());
            addParameter(addRequiredImport(Locale.class.getName()), "loc");
            super.write(file);
            this.locSetterMode = false;
        }
    }


    protected void writeContent(JavaFile file)
    {
        if(this.attributeDesc.isLocalized() && !this.locSetterMode)
        {
            file.add(getName() + "(value,null);");
        }
        else if(this.attributeDesc.isRedeclared())
        {
            writeContentForRedeclaredAttribute(file);
        }
        else if(this.attributeDesc.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC)
        {
            writeDynamicAttributeSetterContent(file, this.attributeDesc, this.attributeDesc.getQualifier().toUpperCase());
        }
        else
        {
            writeNormalAttributeSetterContent(file, this.attributeDesc, this.attributeDesc.getQualifier().toUpperCase());
        }
    }


    private void writeNormalAttributeSetterContent(JavaFile file, YAttributeDescriptor desc, String constantName)
    {
        String objectValueExpr = desc.isPrimitive() ? "toObject(value)" : "value";
        if(this.attributeDesc.isLocalized())
        {
            file.add("getPersistenceContext().setLocalizedValue(" + constantName + ", loc, " + objectValueExpr + ");");
        }
        else
        {
            file.add("getPersistenceContext().setPropertyValue(" + constantName + ", " + objectValueExpr + ");");
        }
    }


    private void writeDynamicAttributeSetterContent(JavaFile file, YAttributeDescriptor desc, String constantName)
    {
        String objectValueExpr = desc.isPrimitive() ? "toObject(value)" : "value";
        if(this.attributeDesc.isLocalized())
        {
            file.add("getPersistenceContext().setLocalizedDynamicValue(this," + constantName + ", loc, " + objectValueExpr + ");");
        }
        else
        {
            file.add("getPersistenceContext().setDynamicValue(this," + constantName + ", " + objectValueExpr + ");");
        }
    }


    private void writeContentForRedeclaredAttribute(JavaFile file)
    {
        if((this.attributeDesc.getDeclaringAttribute().isWritable() || this.attributeDesc.getDeclaringAttribute().isInitial()) &&
                        !this.redeclaredReturnType.equals(this.declaringReturnType))
        {
            file.add("if( value == null || value instanceof " + addRequiredImport(this.redeclaredReturnType) + ")");
            file.startBlock();
            file.add("super." + getName() + "(value);");
            file.endBlock();
            file.add("else");
            file.startBlock();
            file.add("throw new IllegalArgumentException(\"Given value is not instance of " + this.redeclaredReturnType + "\");");
            file.endBlock();
        }
        else
        {
            file.add("super." + getName() + "(value);");
        }
    }


    public String getRedeclaredParameterType()
    {
        return this.redeclaredReturnType;
    }


    public String getDeclaredParameterType()
    {
        return this.declaringReturnType;
    }


    public boolean isDeprecated()
    {
        return this.isDeprecated;
    }


    public String getDeprecatedSince()
    {
        return this.deprecatedSince;
    }
}
