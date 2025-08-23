package de.hybris.bootstrap.codegenerator.model.enums;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.HybrisDynamicEnumValueSerializedForm;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClassEnumWriter extends AbstractEnumWriter
{
    public ClassEnumWriter(CodeGenerator gen, YExtension ext, YEnumType myEnum)
    {
        super(gen, ext, myEnum);
    }


    protected MethodWriter generateConstructor()
    {
        MethodWriter constructor = new MethodWriter(getClassName());
        constructor.setVisibility(Visibility.PRIVATE);
        constructor.addParameter("String", "code");
        constructor.setContentPlain("this.code = code.intern();\nthis.codeLowerCase = this.code.toLowerCase().intern();");
        constructor.setJavadoc("Creates a new enum value for this enum type.\n \n@param code the enum value code");
        return constructor;
    }


    protected void fill()
    {
        super.fill();
        addDeclaration("private final String codeLowerCase;");
        addDeclaration("private static final long serialVersionUID = 0L;");
        addConstantDeclaration("private static final " + addRequiredImport(ConcurrentMap.class.getName()) + "<String, " +
                        getClassName() + "> cache = new " + addRequiredImport(ConcurrentHashMap.class
                        .getName()) + "<String, " +
                        getClassName() + ">();");
        for(EnumWriterValue value : getEnumValues())
        {
            StringBuilder javadoc = new StringBuilder();
            if(value.getJavadoc() != null)
            {
                javadoc.append("/**\n");
                for(StringTokenizer st = new StringTokenizer(value.getJavadoc(), "\n"); st.hasMoreTokens(); )
                {
                    javadoc.append(" * " + st.nextToken() + "\n");
                }
                javadoc.append(" */\n");
            }
            addConstantDeclaration("" + javadoc + "public static final " + javadoc + " " + getClassName() + " = valueOf(\"" + value
                            .getCode().toUpperCase() + "\");\n ");
        }
        MethodWriter valueOfMethod = new MethodWriter(getClassName(), "valueOf");
        valueOfMethod.addParameter("String", "code");
        valueOfMethod.setVisibility(Visibility.PUBLIC);
        valueOfMethod.setModifiers(valueOfMethod.getModifiers() | 0x2);
        valueOfMethod.setContentPlain("final String key = code.toLowerCase();\n" +
                        getClassName() + " result = cache.get(key);\nif (result == null)\n{\n" +
                        getClassName() + " newValue = new " + getClassName() + "(code);\n" +
                        getClassName() + " previous = cache.putIfAbsent(key, newValue);\nresult = previous != null ? previous : newValue;\n}\nreturn result;");
        valueOfMethod.setJavadoc("Returns a <tt>" +
                        getClassName() + "</tt> instance representing the specified enum value.\n \n@param code an enum value\n@return a <tt>" +
                        getClassName() + "</tt> instance representing <tt>value</tt>. ");
        addMethod(valueOfMethod);
        MethodWriter toStringMethod = new MethodWriter("String", "toString");
        toStringMethod.setVisibility(Visibility.PUBLIC);
        toStringMethod.addAnnotation("Override");
        toStringMethod.setContentPlain("return this.code.toString();");
        toStringMethod.setJavadoc("Returns the code representing this enum value.\n \n@return a string representation of the value of this object.");
        addMethod(toStringMethod);
        MethodWriter equalsMethod = new MethodWriter("boolean", "equals");
        equalsMethod.setVisibility(Visibility.PUBLIC);
        equalsMethod.addParameter("Object", "obj");
        equalsMethod.addAnnotation("Override");
        equalsMethod.setJavadoc("Compares this object to the specified object. The result is <code>true</code>\nif and only if the argument is not <code>null</code> and is an <code>" +
                        getClassName() + "\n</code> object that contains the enum value <code>code</code> as this object.\n \n@param obj the object to compare with.\n@return <code>true</code> if the objects are the same;\n        <code>false</code> otherwise.");
        equalsMethod.setContentPlain(
                        "try\n{\n\tfinal HybrisEnumValue enum2 = (HybrisEnumValue) obj;\n\treturn this == enum2\n\t\t\t|| (enum2 != null && !this.getClass().isEnum() && !enum2.getClass().isEnum()\n\t\t\t\t\t&& this.getType().equalsIgnoreCase(enum2.getType()) && this.getCode().equalsIgnoreCase(enum2.getCode()));\n}\ncatch (final ClassCastException e)\n{\n\treturn false;\n}\n");
        addMethod(equalsMethod);
        MethodWriter hashCodeMethod = new MethodWriter("int", "hashCode");
        hashCodeMethod.setVisibility(Visibility.PUBLIC);
        hashCodeMethod.addAnnotation("Override");
        hashCodeMethod.setContentPlain("return this.codeLowerCase.hashCode();");
        hashCodeMethod.setJavadoc("Returns a hash code for this <code>" +
                        getClassName() + "</code>.\n \n@return a hash code value for this object, equal to the enum value <code>code</code>\n        represented by this <code>" +
                        getClassName() + "</code> object.");
        addMethod(hashCodeMethod);
        MethodWriter writeReplaceMethod = new MethodWriter(getClassName(), "writeReplace");
        writeReplaceMethod.setVisibility(Visibility.PRIVATE);
        writeReplaceMethod.setReturnType("Object");
        writeReplaceMethod.setContentPlain("return new " + HybrisDynamicEnumValueSerializedForm.class
                        .getName() + "(this.getClass(), getCode());");
        addMethod(writeReplaceMethod);
    }
}
