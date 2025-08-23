package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

public class ModelGetterWriter extends MethodWriter
{
    private final YAttributeDescriptor descriptor;
    private final CodeGenerator gen;
    private boolean locGetterMode = false;
    private final String nullDecorator;
    private final boolean isDeprecated;
    private final String deprecatedSince;


    public ModelGetterWriter(Visibility visibility, CodeGenerator gen, YAttributeDescriptor descriptor)
    {
        this(visibility, gen, descriptor, descriptor.getQualifier());
    }


    public ModelGetterWriter(Visibility visibility, CodeGenerator gen, YAttributeDescriptor descriptor, String name)
    {
        this(visibility, gen, descriptor, name, null, false, null);
    }


    public ModelGetterWriter(Visibility visibility, CodeGenerator gen, YAttributeDescriptor descriptor, String name, String nullDecorator, boolean isDeprecated, String deprecatedSince)
    {
        super(visibility, null, generateMethodName(name, descriptor.isBooleanAttribute(), descriptor.isPrimitive()));
        this.descriptor = descriptor;
        this.gen = gen;
        String rType = calculateReturnType(descriptor);
        setReturnType(addRequiredImport(rType));
        if(descriptor.isRedeclared())
        {
            addAnnotation("Override");
        }
        this.isDeprecated = isDeprecated;
        this.deprecatedSince = deprecatedSince;
        this.nullDecorator = nullDecorator;
        if(this.isDeprecated)
        {
            addDeprecatedAnnotation(deprecatedSince);
        }
    }


    protected static String generateMethodName(String qualifier, boolean booleanAttribute, boolean primitive)
    {
        return ((booleanAttribute && primitive) ? "is" : "get") + ((booleanAttribute && primitive) ? "is" : "get");
    }


    private String calculateReturnType(YAttributeDescriptor desc)
    {
        String ret;
        if("itemtype".equalsIgnoreCase(desc.getQualifier()))
        {
            ret = "java.lang.String";
        }
        else
        {
            ret = ModelNameUtils.getModelOrClass(this.descriptor, this.gen, true);
        }
        return ret;
    }


    protected String generateJavadoc()
    {
        String descr = (this.descriptor.getDescription() != null && this.descriptor.getDescription().length() > 0) ? (" - " + this.descriptor.getDescription()) : "";
        return "<i>Generated method</i> - Getter of the <code>" + this.descriptor
                        .getDeclaringType().getCode() + "." + this.descriptor
                        .getQualifier() + "</code> " + (
                        (this.descriptor.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC) ? "dynamic " : "") + "attribute defined at extension <code>" + ((YExtension)this.descriptor
                        .getDeclaringAttribute().getNamespace()).getExtensionName() + "</code>" + (
                        this.descriptor.isRedeclared() ? (" and redeclared at extension <code>" + ((YExtension)this.descriptor
                                        .getNamespace()).getExtensionName() + "</code>") : "") + ". \n" + (
                        this.locGetterMode ? "@param loc the value localization key \n" : "") + "\n" + (
                        (this.descriptor.getType() instanceof de.hybris.bootstrap.typesystem.YCollectionType) ? "Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.\n" :
                                        "") + "@return the " + this.descriptor
                        .getQualifier() + descr + (
                        this.locGetterMode ? "\n@throws IllegalArgumentException if localization key cannot be mapped to data language\n" : "");
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
        if(this.descriptor.isLocalized())
        {
            this.locGetterMode = true;
            setJavadoc(generateJavadoc());
            addParameter(addRequiredImport(Locale.class.getName()), "loc");
            super.write(file);
            this.locGetterMode = false;
        }
    }


    protected void writeContent(JavaFile file)
    {
        if(this.descriptor.isLocalized() && !this.locGetterMode)
        {
            file.add("return " + getName() + "(null);");
        }
        else if(this.descriptor.isRedeclared())
        {
            file.add("return (" + getReturnType() + ") super." + getName() + "();");
        }
        else if(this.descriptor.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC)
        {
            writeDynamicAttributeGetterContent(file, this.descriptor, this.descriptor.getQualifier().toUpperCase());
        }
        else
        {
            writeNormalAttributeGetterContent(file, this.descriptor, this.descriptor.getQualifier().toUpperCase());
        }
    }


    private void writeNormalAttributeGetterContent(JavaFile file, YAttributeDescriptor descriptor, String constantName)
    {
        String valueFromCtxExpression;
        if(descriptor.isLocalized())
        {
            if(descriptor.isRelationEndAttribute() && descriptor.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.MANY)
            {
                valueFromCtxExpression = "getPersistenceContext().getLocalizedRelationValue(" + constantName + ", loc)";
            }
            else
            {
                valueFromCtxExpression = "getPersistenceContext().getLocalizedValue(" + constantName + ", loc)";
            }
        }
        else
        {
            valueFromCtxExpression = "getPersistenceContext().getPropertyValue(" + constantName + ")";
        }
        if(descriptor.isPrimitive())
        {
            String primitiveType = ((YAtomicType)descriptor.getType()).getJavaClass().getSimpleName();
            file.add("return toPrimitive((" + primitiveType + ")" + valueFromCtxExpression + ");");
        }
        else if(StringUtils.isNotBlank(this.nullDecorator))
        {
            file.add("final " + getReturnType() + " value = " + valueFromCtxExpression + ";");
            file.add("return value != null ? value : " + this.nullDecorator + ";");
        }
        else
        {
            file.add("return " + valueFromCtxExpression + ";");
        }
    }


    private void writeDynamicAttributeGetterContent(JavaFile file, YAttributeDescriptor descriptor, String constantName)
    {
        String valueFromCtxExpression;
        if(descriptor.isLocalized())
        {
            valueFromCtxExpression = "getPersistenceContext().getLocalizedDynamicValue(this," + constantName + ", loc)";
        }
        else
        {
            valueFromCtxExpression = "getPersistenceContext().getDynamicValue(this," + constantName + ")";
        }
        if(descriptor.isPrimitive())
        {
            file.add("return toPrimitive( (" +
                            addRequiredImport(
                                            convertPrimTypes(ModelNameUtils.getModelOrClass(descriptor, this.gen, true))) + ") " + valueFromCtxExpression + ");");
        }
        else
        {
            file.add("return " + valueFromCtxExpression + ";");
        }
    }


    protected static String convertPrimTypes(String type)
    {
        switch(type)
        {
            case "int":
                return Integer.class.getName();
            case "boolean":
                return Boolean.class.getName();
            case "double":
                return Double.class.getName();
            case "long":
                return Long.class.getName();
            case "float":
                return Float.class.getName();
            case "byte":
                return Byte.class.getName();
            case "short":
                return Short.class.getName();
            case "char":
                return Character.class.getName();
        }
        return type;
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
