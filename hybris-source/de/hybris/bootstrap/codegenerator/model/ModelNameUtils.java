package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YType;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public final class ModelNameUtils
{
    private static final String MODEL_SUFFIX = "Model";


    private static String adjustClass(YType type, CodeGenerator gen, boolean primitive)
    {
        if(type.getCode().equals("EnumerationValue"))
        {
            return "de.hybris.platform.core.HybrisEnumValue";
        }
        if(type instanceof YComposedType)
        {
            return getModel((YComposedType)type, gen.getExtensionPackage((YExtension)type.getNamespace()));
        }
        if(type instanceof YAtomicType)
        {
            if(primitive)
            {
                return ((YAtomicType)type).getPrimitiveJavaClass().getName();
            }
            return ((YAtomicType)type).getJavaClassName();
        }
        if(type instanceof YCollectionType)
        {
            YCollectionType collType = (YCollectionType)type;
            StringBuilder result = new StringBuilder();
            switch(null.$SwitchMap$de$hybris$bootstrap$typesystem$YCollectionType$TypeOfCollection[collType.getTypeOfCollection().ordinal()])
            {
                case 1:
                    result.append(Set.class.getName());
                    result.append("<").append(adjustClass(collType.getElementType(), gen, primitive)).append(">");
                    return result.toString();
                case 2:
                    result.append(List.class.getName());
                    result.append("<").append(adjustClass(collType.getElementType(), gen, primitive)).append(">");
                    return result.toString();
                case 3:
                    result.append(Collection.class.getName());
                    result.append("<").append(adjustClass(collType.getElementType(), gen, primitive)).append(">");
                    return result.toString();
            }
            result.append(Set.class.getName());
            result.append("<").append(adjustClass(collType.getElementType(), gen, primitive)).append(">");
            return result.toString();
        }
        if(type instanceof YMapType)
        {
            return "java.util.Map<" + adjustClass(((YMapType)type).getArgumentType(), gen, primitive) + "," +
                            adjustClass(((YMapType)type).getReturnType(), gen, primitive) + ">";
        }
        throw new IllegalArgumentException("unknown type " + type);
    }


    private static YComposedType adjustType(YComposedType type)
    {
        if(type.getSuperType() != null && type.getSuperType().getCode().equals("Link"))
        {
            return type.getSuperType();
        }
        if(type.getCode().equalsIgnoreCase("ExtensibleItem"))
        {
            return type.getSuperType();
        }
        if(type.getCode().equalsIgnoreCase("LocalizableItem"))
        {
            return type.getSuperType().getSuperType();
        }
        if(type.getCode().equalsIgnoreCase("GenericItem"))
        {
            return type.getSuperType().getSuperType().getSuperType();
        }
        return type;
    }


    private static String adjustPackage(String jaloPackage, String packageRoot)
    {
        if(jaloPackage.startsWith("de.hybris.platform.jalo"))
        {
            return jaloPackage.replaceFirst(".jalo", ".core.model");
        }
        if(jaloPackage.contains(".jalo"))
        {
            return jaloPackage.replaceFirst(".jalo", ".model");
        }
        if(jaloPackage.startsWith("de.hybris.platform.util"))
        {
            return jaloPackage.replace("de.hybris.platform.util", "de.hybris.platform.core.model.util");
        }
        if(jaloPackage.startsWith("de.hybris.platform"))
        {
            String temp = jaloPackage.replace("de.hybris.platform.", "");
            return "de.hybris.platform." + temp.replaceFirst("\\.", ".model.");
        }
        if(jaloPackage.startsWith(packageRoot))
        {
            return jaloPackage.replaceFirst(packageRoot, packageRoot + ".model");
        }
        int pos = jaloPackage.lastIndexOf('.');
        return (pos >= 0) ? (jaloPackage.substring(0, pos) + ".model") : jaloPackage;
    }


    private static String adjustName(YComposedType type)
    {
        String result;
        if(type instanceof YEnumType)
        {
            result = getEnumModelName(type.getCode());
        }
        else
        {
            result = type.getCode() + "Model";
        }
        return ClassWriter.firstLetterUpperCase(result);
    }


    public static String getModelName(YComposedType type)
    {
        YComposedType curType = adjustType(type);
        return adjustName(curType);
    }


    private static String getEnumModelName(String code)
    {
        return ClassWriter.firstLetterUpperCase(code);
    }


    public static String getModelPackage(YComposedType type, String packageRoot)
    {
        YComposedType curType = adjustType(type);
        if(curType instanceof YEnumType)
        {
            return getEnumModelPackage((YEnumType)type, packageRoot);
        }
        String jaloClass = CodeGenerator.getJaloClassName(curType, packageRoot);
        int pos = jaloClass.lastIndexOf('.');
        String origPackage = jaloClass.substring(0, pos);
        return adjustPackage(origPackage, packageRoot);
    }


    private static String getEnumModelPackage(YEnumType enumType, String packageRoot)
    {
        String enumModelPackage = enumType.getModelPackage();
        if(enumModelPackage != null)
        {
            return enumModelPackage;
        }
        return getEnumModelPackage(packageRoot);
    }


    private static String getEnumModelPackage(String packageRoot)
    {
        if("de.hybris.platform".equals(packageRoot))
        {
            return packageRoot + ".core.enums";
        }
        return packageRoot + ".enums";
    }


    public static String getModel(YComposedType type, String packageRoot)
    {
        return getModelPackage(type, packageRoot) + "." + getModelPackage(type, packageRoot);
    }


    public static String getEnumModel(YEnumType enumType, String packageRoot)
    {
        return getEnumModelPackage(enumType, packageRoot) + "." + getEnumModelPackage(enumType, packageRoot);
    }


    public static String getModelOrClass(YAttributeDescriptor attribute, CodeGenerator gen, boolean primitive)
    {
        if(attribute.isLocalized())
        {
            YType curType = ((YMapType)attribute.getType()).getReturnType();
            return adjustClass(curType, gen, false);
        }
        return adjustClass(attribute.getType(), gen, (primitive && attribute.isPrimitive()));
    }
}
