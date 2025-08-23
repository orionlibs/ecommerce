package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.CodeWriter;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConstantsWriter extends JaloClassWriter
{
    public ConstantsWriter(YExtension extension, CodeGenerator generator)
    {
        super(generator, extension, assembleAbstractClassName(generator, extension, "Constants"));
        setPackageName(getPackageName() + ".constants");
        setVisibility(Visibility.PUBLIC);
        String copyright = getCopyright();
        setCopyright(GENERATED_NOTICE + GENERATED_NOTICE);
        setJavadoc("@deprecated since ages - use constants in Model classes instead");
        addAnnotation("Deprecated(since = \"ages\", forRemoval = false)");
        addAnnotation("SuppressWarnings({\"unused\",\"cast\"})");
    }


    public static String assembleConstantsClassName(CodeGenerator gen, YExtension ext)
    {
        return gen.getExtensionPackage(ext) + ".constants." + gen.getExtensionPackage(ext);
    }


    public ClassWriter createNonAbstractClassWriter()
    {
        JaloClassWriter jaloClassWriter = new JaloClassWriter(getGenerator(), getExtension(), assembleClassName(getGenerator(),
                        getExtension(), "Constants"));
        jaloClassWriter.setPackageName(getPackageName());
        jaloClassWriter.setVisibility(getVisibility());
        jaloClassWriter.setModifiers(getModifiers() & 0xFFFFFFFE);
        jaloClassWriter.setClassToExtend(getClassName());
        MethodWriter constr = new MethodWriter(jaloClassWriter.getClassName());
        constr.setVisibility(Visibility.PRIVATE);
        constr.setContentPlain("//empty");
        jaloClassWriter.addConstructor(constr);
        jaloClassWriter.addDeclaration("public static final String EXTENSIONNAME = \"" + getExtension().getExtensionName() + "\";");
        return (ClassWriter)jaloClassWriter;
    }


    protected void fill()
    {
        MethodWriter constr = new MethodWriter(getClassName());
        constr.setVisibility(Visibility.PROTECTED);
        constr.setContentPlain("// private constructor");
        addConstructor(constr);
        addDeclaration("public static final String EXTENSIONNAME = \"" + getExtension().getExtensionName() + "\";");
        addDeclaration((CodeWriter)new Object(this));
        addDeclaration((CodeWriter)new Object(this));
        addDeclaration((CodeWriter)new Object(this));
        addDeclaration((CodeWriter)new Object(this));
    }


    public static boolean requiresAttributeConstants(YComposedType cType, YExtension ext)
    {
        return (!ext.equals(cType.getNamespace()) || cType instanceof YEnumType || cType instanceof de.hybris.bootstrap.typesystem.YRelation || cType
                        .isViewType() || !cType.isGenerate());
    }


    public static Map<YComposedType, List<YAttributeDescriptor>> sortAttributes(Collection<YAttributeDescriptor> attributes)
    {
        Map<YComposedType, List<YAttributeDescriptor>> tmp = new HashMap<>();
        for(YAttributeDescriptor ad : attributes)
        {
            if(!ad.isDeclared())
            {
                continue;
            }
            YComposedType cType = ad.getEnclosingType();
            List<YAttributeDescriptor> lst = tmp.computeIfAbsent(cType, k -> new ArrayList());
            lst.add(ad);
        }
        List<YComposedType> types = new ArrayList<>(tmp.keySet());
        types.sort((type1, type2) -> type1.getCode().compareToIgnoreCase(type2.getCode()));
        Map<YComposedType, List<YAttributeDescriptor>> ret = new LinkedHashMap<>(types.size());
        for(YComposedType ct : types)
        {
            List<YAttributeDescriptor> lst = tmp.get(ct);
            lst.sort((desc1, desc2) -> desc1.getQualifier().compareToIgnoreCase(desc2.getQualifier()));
            ret.put(ct, lst);
        }
        return ret;
    }


    public static List<YType> sortTypes(Collection<YType> types)
    {
        List<YType> ret = new ArrayList<>(types);
        ret.sort((type1, type2) -> type1.getCode().compareToIgnoreCase(type2.getCode()));
        return ret;
    }


    protected Set<YEnumType> getExtendedEnumTypes()
    {
        Set<YEnumType> ret = new LinkedHashSet<>();
        Set<YEnumValue> values = getExtension().getOwnEnumValues();
        for(YEnumValue value : values)
        {
            YEnumType type = value.getEnumType();
            if(!getExtension().equals(type.getNamespace()))
            {
                ret.add(type);
            }
        }
        return ret;
    }


    protected Set<YEnumType> getDeclaredEnumTypes()
    {
        Set<YEnumType> ret = new LinkedHashSet<>();
        Set<YEnumType> types = getExtension().getOwnTypes(YEnumType.class);
        for(YEnumType t : types)
        {
            if(getExtension().equals(t.getNamespace()))
            {
                ret.add(t);
            }
        }
        return ret;
    }
}
