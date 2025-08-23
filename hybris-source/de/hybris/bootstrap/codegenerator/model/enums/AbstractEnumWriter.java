package de.hybris.bootstrap.codegenerator.model.enums;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.codegenerator.model.ModelNameUtils;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.LinkedList;
import java.util.List;

public class AbstractEnumWriter extends ClassWriter
{
    private final List<EnumWriterValue> enumWriterValues = new LinkedList<>();
    private final YEnumType enumType;


    public AbstractEnumWriter(CodeGenerator gen, YExtension ext, YEnumType myEnum)
    {
        super(gen, ext, ModelNameUtils.getModelName((YComposedType)myEnum));
        setVisibility(Visibility.PUBLIC);
        if(myEnum.getModelPackage() != null)
        {
            setPackageName(myEnum.getModelPackage());
        }
        this.enumType = myEnum;
        addAnnotationIfDeprecated((YComposedType)myEnum);
    }


    public void addEnumValue(EnumWriterValue enumWriterValue)
    {
        this.enumWriterValues.add(enumWriterValue);
    }


    public List<EnumWriterValue> getEnumValues()
    {
        return this.enumWriterValues;
    }


    protected MethodWriter generateConstructor()
    {
        MethodWriter constructor = new MethodWriter(getClassName());
        constructor.setVisibility(Visibility.PRIVATE);
        constructor.addParameter("String", "code");
        constructor.setContentPlain("this.code = code.intern();");
        constructor.setJavadoc("Creates a new enum value for this enum type.\n \n@param code the enum value code");
        return constructor;
    }


    protected void fill()
    {
        addDeclaration("/** The code of this enum.*/\nprivate final String code;");
        addInterface("de.hybris.platform.core.HybrisEnumValue");
        addConstructor(generateConstructor());
        addConstantDeclaration("/**<i>Generated model type code constant.</i>*/\npublic final static String _TYPECODE = \"" + this.enumType
                        .getCode() + "\";\n ");
        addConstantDeclaration("/**<i>Generated simple class name constant.</i>*/\npublic final static String SIMPLE_CLASSNAME = \"" +
                        getClassName() + "\";");
        MethodWriter getTypeMethod = new MethodWriter("String", "getType");
        getTypeMethod.setVisibility(Visibility.PUBLIC);
        getTypeMethod.addAnnotation("Override");
        getTypeMethod.setContentPlain("return SIMPLE_CLASSNAME;");
        getTypeMethod.setJavadoc("Gets the type this enum value belongs to.\n \n@return code of type");
        addMethod(getTypeMethod);
        MethodWriter getCodeMethod = new MethodWriter("String", "getCode");
        getCodeMethod.setVisibility(Visibility.PUBLIC);
        getCodeMethod.addAnnotation("Override");
        getCodeMethod.setContentPlain("return this.code;");
        getCodeMethod.setJavadoc("Gets the code of this enum value.\n \n@return code of value");
        addMethod(getCodeMethod);
    }
}
