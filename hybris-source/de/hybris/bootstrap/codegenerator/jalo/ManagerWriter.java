package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YRelation;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class ManagerWriter extends JaloClassWriter
{
    private final Set<String> relationOverrideConstantsAddedFor = new HashSet<>();
    private final Set<String> relationMarkModifiedConstantsAddedFor = new HashSet<>();


    public ManagerWriter(YExtension ext, CodeGenerator gen, boolean jaloLogicFree)
    {
        super(gen, ext, (jaloLogicFree ? "" : gen.getInfo(ext).getAbstractClassPrefix()) + (jaloLogicFree ? "" : gen.getInfo(ext).getAbstractClassPrefix()), jaloLogicFree);
        setModifiers(getAllModifiers());
        setVisibility(Visibility.PUBLIC);
        setPackageName(determineManagerPackage(gen, ext));
        setClassToExtend(getInfo().getManagerSuperclass());
        String copyright = getCopyright();
        setCopyright(GENERATED_NOTICE + GENERATED_NOTICE);
        if(isJaloLogicFree())
        {
            addAnnotation("SuppressWarnings({\"unused\",\"cast\"})");
        }
        else
        {
            addAnnotation("SuppressWarnings({\"deprecation\",\"unused\",\"cast\"})");
        }
    }


    private static String determineManagerName(CodeGenerator gen, YExtension ext)
    {
        String declaredManager = gen.getInfo(ext).getCoreModule().getManager();
        if(declaredManager != null)
        {
            int sep = declaredManager.lastIndexOf('.');
            if(sep > 0 && sep < declaredManager.length())
            {
                return declaredManager.substring(sep + 1, declaredManager.length());
            }
        }
        return gen.getInfo(ext).getManagerName();
    }


    public static String assembleManagerClassName(CodeGenerator gen, YExtension ext)
    {
        return gen.getExtensionPackage(ext) + ".jalo." + gen.getExtensionPackage(ext);
    }


    private static String determineManagerPackage(CodeGenerator gen, YExtension ext)
    {
        String declaredManager = gen.getInfo(ext).getCoreModule().getManager();
        if(declaredManager != null)
        {
            int sep = declaredManager.lastIndexOf('.');
            if(sep > 0 && sep < declaredManager.length())
            {
                return declaredManager.substring(0, sep);
            }
        }
        return gen.getExtensionPackage(ext) + ".jalo";
    }


    public ClassWriter createNonAbstractClassWriter()
    {
        ClassWriter writer = new ClassWriter(getGenerator(), getExtension(), determineManagerName(getGenerator(), getExtension()));
        writer.setPackageName(determineManagerPackage(getGenerator(), getExtension()));
        writer.setVisibility(getVisibility());
        writer.setModifiers(getModifiers() & 0xFFFFFFF7);
        writer.setClassToExtend(getClassName());
        writer.addDeclaration("@SuppressWarnings(\"unused\")\nprivate static final Logger log = Logger.getLogger( " + writer
                        .getClassName() + ".class.getName() );", "org.apache.log4j.Logger");
        MethodWriter getInstance = new MethodWriter(Visibility.PUBLIC, writer.getClassName(), "getInstance");
        getInstance.setModifiers(getInstance.getModifiers() | 0x2 | 0x1);
        getInstance.addRequiredImport("de.hybris.platform.jalo.extension.ExtensionManager");
        getInstance.addRequiredImport("de.hybris.platform.jalo.JaloSession");
        getInstance.setContentPlain("ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();\nreturn (" + writer
                        .getClassName() + ") em.getExtension(" + getInstance
                        .addRequiredImport(getConstantsClassName()) + ".EXTENSIONNAME);");
        writer.addMethod(getInstance);
        return writer;
    }


    private int getAllModifiers()
    {
        if(isJaloLogicFree())
        {
            return getModifiers();
        }
        return getModifiers() | 0x8;
    }


    private boolean mustAddRelationOverrideConstant(String relationName)
    {
        return this.relationOverrideConstantsAddedFor.add(relationName);
    }


    private boolean mustAddRelationMarkModifiedOverrideConstant(String relationName)
    {
        return this.relationMarkModifiedConstantsAddedFor.add(relationName);
    }


    protected void fill()
    {
        if(isJaloLogicFree())
        {
            addRequiredImport("de.hybris.platform.directpersistence.annotation.SLDSafe");
            addAnnotation("SLDSafe");
        }
        setJavadoc("Generated class for type <code>" + getInfo().getManagerName() + "</code>.");
        MethodWriter writer = new MethodWriter(Visibility.PUBLIC, "String", "getName");
        writer.setContentPlain("return " + addRequiredImport(getConstantsClassName()) + ".EXTENSIONNAME;");
        writer.addAnnotation("Override");
        addMethod(writer);
        for(YComposedType t : getDeclaredTypes())
        {
            if(!t.isAbstract() && !t.isViewType() && !(t instanceof YRelation) && !(t instanceof YEnumType))
            {
                addCreatorMethods(t);
            }
        }
        for(YComposedType ct : getExtendedTypes())
        {
            (new ManagerItemTypeWriter(this, ct)).generateMethods();
        }
        addDefaultInitialPropertyAttributes();
        if(isJaloLogicFree())
        {
            MethodWriter getInstance = new MethodWriter(Visibility.PUBLIC, getClassName(), "getInstance");
            getInstance.setModifiers(getInstance.getModifiers() | 0x2 | 0x1);
            getInstance.addRequiredImport("de.hybris.platform.jalo.extension.ExtensionManager");
            getInstance.addRequiredImport("de.hybris.platform.jalo.JaloSession");
            getInstance.setContentPlain("ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();\nreturn (" +
                            getClassName() + ") em.getExtension(" + getInstance
                            .addRequiredImport(getConstantsClassName()) + ".EXTENSIONNAME);");
            addMethod(getInstance);
        }
    }


    private void addDefaultInitialPropertyAttributes()
    {
        StringBuilder sb = new StringBuilder();
        String attrModeType = addRequiredImport("de.hybris.platform.jalo.Item.AttributeMode");
        sb.append("protected static final Map<String, Map<String, ").append(attrModeType)
                        .append(">> DEFAULT_INITIAL_ATTRIBUTES;\n");
        sb.append("static\n");
        sb.append("{\n");
        sb.append("final ").append(addRequiredImport("java.util.Map")).append("<String, Map<String, ").append(attrModeType)
                        .append(">> ttmp = new ").append(addRequiredImport("java.util.HashMap")).append("(").append(");\n");
        String mapDeclarationStr = addRequiredImport("java.util.Map") + "<String, " + addRequiredImport("java.util.Map") + "> ";
        boolean mapDeclared = false;
        StringBuilder sbInternal = new StringBuilder();
        for(YComposedType type : getExtendedTypes())
        {
            Set<YAttributeDescriptor> attributes = type.getAttributes();
            if(CollectionUtils.isEmpty(attributes))
            {
                continue;
            }
            for(YAttributeDescriptor ad : attributes)
            {
                if(ad.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY && ad.isDeclared() && ad
                                .getNamespace().equals(getExtension()))
                {
                    sbInternal.append("tmp.put(\"").append(ad.getQualifier()).append("\", ").append(attrModeType)
                                    .append(".INITIAL);\n");
                }
            }
            if(sbInternal.length() > 0)
            {
                sb.append(mapDeclared ? "" : mapDeclarationStr)
                                .append("tmp = new ")
                                .append(addRequiredImport("java.util.HashMap"))
                                .append("<String, ")
                                .append(attrModeType)
                                .append(">(")
                                .append(");\n");
                sb.append(sbInternal.toString());
                sb.append("ttmp.put(\"").append(getGenerator().getJaloClassName(type)).append("\", ")
                                .append(addRequiredImport("java.util.Collections")).append(".unmodifiableMap(tmp));\n");
                mapDeclared = true;
                sbInternal.setLength(0);
            }
        }
        sb.append("DEFAULT_INITIAL_ATTRIBUTES = ttmp;\n");
        sb.append("}\n");
        sb.append("@Override\n");
        sb.append("public ").append(addRequiredImport("java.util.Map")).append("<String, ").append(attrModeType)
                        .append("> getDefaultAttributeModes(").append("final Class<? extends ")
                        .append(addRequiredImport("de.hybris.platform.jalo.Item")).append("> itemClass)").append("\n");
        sb.append("{\n");
        sb.append("Map<String, AttributeMode> ret = new HashMap<>();\n");
        sb.append("final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());\n");
        sb.append("if (attr != null)\n");
        sb.append("{\n");
        sb.append("ret.putAll(attr);\n");
        sb.append("}\n");
        sb.append("return ret;\n");
        sb.append("}\n");
        addDeclaration(sb.toString());
    }


    protected void addCreatorMethods(YComposedType type)
    {
        String jaloClassName = getGenerator().getJaloClassName(type);
        MethodWriter create = new MethodWriter(Visibility.PUBLIC, jaloClassName, "create" + firstLetterUpperCase(type.getCode()));
        create.addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
        create.addParameter("java.util.Map", "attributeValues");
        create.setContentPlain("try\n{\n   " + addRequiredImport("de.hybris.platform.jalo.type.ComposedType") +
                        getTypeCreatorClause(type) + "   return (" +
                        addRequiredImport(jaloClassName) + ")type.newInstance( ctx, attributeValues );\n}\ncatch( " +
                        addRequiredImport("de.hybris.platform.jalo.type.JaloGenericCreationException") + " e)\n{\n   final Throwable cause = e.getCause();\n   throw (cause instanceof RuntimeException ? \n      (RuntimeException)cause \n      : \n      new " +
                        addRequiredImport("de.hybris.platform.jalo.JaloSystemException") + "( cause, cause.getMessage(), e.getErrorCode() ) ); \n}\ncatch( " +
                        addRequiredImport("de.hybris.platform.jalo.JaloBusinessException") + " e )\n{\n   throw new " +
                        addRequiredImport("de.hybris.platform.jalo.JaloSystemException") + "( e ,\"error creating " + type
                        .getCode() + " : \"+e.getMessage(), 0 );\n}");
        addMethod(create);
        MethodWriter createDelegate = new MethodWriter(create.getVisibility(), create.getReturnType(), create.getName());
        createDelegate.addParameter("java.util.Map", "attributeValues");
        createDelegate.setContentPlain("return " + create.getName() + "( getSession().getSessionContext(), attributeValues );");
        addMethod(createDelegate);
    }


    private String getTypeCreatorClause(YComposedType type)
    {
        return isJaloLogicFree() ? (" type = getTenant().getJaloConnection().getTypeManager().getComposedType(\"" +
                        type.getCode() + "\");\n") : (" type = getTenant().getJaloConnection().getTypeManager().getComposedType( " + (
                        (getConstantsClassName() != null) ? (
                                        addRequiredImport(getConstantsClassName()) + ".TC." + addRequiredImport(getConstantsClassName())) : ("\"" +
                                        type.getCode() + "\".intern()")) + " );\n");
    }


    protected Set<YComposedType> getExtendedTypes()
    {
        Set<YComposedType> ret = new LinkedHashSet<>();
        Set<YAttributeDescriptor> attributes = getExtension().getOwnAttributes();
        for(YAttributeDescriptor ad : attributes)
        {
            YComposedType type = ad.getEnclosingType();
            if(!getExtension().equals(type.getNamespace()) || type instanceof YEnumType || type instanceof YRelation)
            {
                ret.add(type);
            }
        }
        return ret;
    }


    protected Set<YComposedType> getDeclaredTypes()
    {
        Set<YComposedType> ret = new LinkedHashSet<>();
        Set<YComposedType> types = getExtension().getOwnTypes(YComposedType.class, new Class[] {YRelation.class, YEnumType.class});
        for(YComposedType t : types)
        {
            if(getExtension().equals(t.getNamespace()))
            {
                ret.add(t);
            }
        }
        return ret;
    }
}
