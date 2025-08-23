package de.hybris.bootstrap.codegenerator.jalo;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class ItemTypeWriter extends JaloClassWriter
{
    private static final String ITEM_FQN = "de.hybris.platform.jalo.Item";
    private static final String SESSION_CONTEXT_FQN = "de.hybris.platform.jalo.SessionContext";
    private static final String COMPOSED_TYPE_FQN = "de.hybris.platform.jalo.type.ComposedType";
    private final Set<String> relationOverrideConstantsAddedFor = new HashSet<>();
    private final Set<String> relationDisableMarkModified = new HashSet<>();
    private final YComposedType type;
    private final boolean managerMode;
    private final CodeGenerator generator;
    private final List<String> extensionBlackListForDefaultProps;


    public ItemTypeWriter(CodeGenerator gen, YExtension ext, YComposedType cType, boolean managerMode, boolean jaloLogicFree)
    {
        super(gen, ext, trimClassName(gen.getInfo(ext).getAbstractClassPrefix(), gen.getJaloClassName(cType), jaloLogicFree), jaloLogicFree);
        this.type = cType;
        this.managerMode = managerMode;
        this.generator = gen;
        setModifiers(getAllModifiers());
        setVisibility(Visibility.PUBLIC);
        if(cType.getSuperType() != null)
        {
            setClassToExtend(addRequiredImport(getGenerator().getJaloClassName(cType.getSuperType())));
        }
        String fullClassName = gen.getJaloClassName(cType);
        setPackageName(fullClassName.substring(0, fullClassName.lastIndexOf('.')));
        String copyright = getCopyright();
        setCopyright(GENERATED_NOTICE + GENERATED_NOTICE);
        addAllAnnotations(cType);
        String extBlackList = gen.getPlatformProperties().getProperty("generator.defaultprops.extensions.blacklist");
        if(extBlackList == null || extBlackList.trim().isEmpty())
        {
            this.extensionBlackListForDefaultProps = new ArrayList<>();
        }
        else
        {
            this.extensionBlackListForDefaultProps = Arrays.asList(extBlackList.replace(" ", "").toLowerCase().split(","));
        }
    }


    private static String trimClassName(String generatedPrefix, String jaloClassName, boolean jaloLogicFree)
    {
        int pos = jaloClassName.lastIndexOf('.');
        return (jaloLogicFree ? "" : generatedPrefix) + (jaloLogicFree ? "" : generatedPrefix);
    }


    private static String trimClassName(String jaloClassName)
    {
        int pos = jaloClassName.lastIndexOf('.');
        return firstLetterUpperCase((pos >= 0) ? jaloClassName.substring(pos + 1) : jaloClassName);
    }


    private int getAllModifiers()
    {
        if(isJaloLogicFree() && !this.type.isAbstract())
        {
            return getModifiers();
        }
        return getModifiers() | 0x8;
    }


    private void addAllAnnotations(YComposedType type)
    {
        if(isJaloLogicFree())
        {
            addAnnotation("SLDSafe");
            addRequiredImport("de.hybris.platform.directpersistence.annotation.SLDSafe");
            addAnnotation("SuppressWarnings({\"unused\",\"cast\"})");
        }
        else
        {
            addAnnotation("SuppressWarnings({\"deprecation\",\"unused\",\"cast\"})");
        }
        addAnnotationIfDeprecated(type);
    }


    public ClassWriter createNonAbstractClassWriter()
    {
        ClassWriter writer = new ClassWriter(getGenerator(), getExtension(), trimClassName(getGenerator().getJaloClassName(
                        getType())));
        writer.setModifiers(getType().isAbstract() ? getModifiers() : (getModifiers() & 0xFFFFFFF7));
        writer.setVisibility(Visibility.PUBLIC);
        writer.setClassToExtend(getClassName());
        writer.setPackageName(getPackageName());
        writer.addDeclaration("@SuppressWarnings(\"unused\")\nprivate static final Logger LOG = Logger.getLogger( " + writer
                        .getClassName() + ".class.getName() );", "org.apache.log4j.Logger");
        MethodWriter createItem = new MethodWriter(Visibility.PROTECTED, "de.hybris.platform.jalo.Item", "createItem");
        createItem.addAnnotation("Override");
        createItem.addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
        createItem.addParameter("de.hybris.platform.jalo.type.ComposedType", "type");
        createItem.addParameter("ItemAttributeMap", "allAttributes");
        createItem.addThrownException("de.hybris.platform.jalo.JaloBusinessException");
        createItem.setContentPlain(
                        "// business code placed here will be executed before the item is created \n\n// then create the item\nfinal Item item = super.createItem( ctx, type, allAttributes );\n\n// business code placed here will be executed after the item was created \n\n// and return the item \nreturn item;");
        writer.addMethod(createItem);
        MethodWriter removeItem = new MethodWriter(Visibility.PUBLIC, null, "remove");
        removeItem.addThrownException("de.hybris.platform.jalo.ConsistencyCheckException");
        removeItem.addAnnotation("Override");
        removeItem.addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
        removeItem.setContentPlain("// ## business code placed here will be executed before the item is removed\n\n// then create the item\nsuper.remove( ctx );\n\n// ## business code placed here will be executed after the item was removed");
        return writer;
    }


    protected TypedGetter createTypedGetter(YAttributeDescriptor desc)
    {
        return new TypedGetter(this, desc, isManagerMode());
    }


    protected PrimitiveGetter createPrimitiveGetter(TypedGetter original)
    {
        return new PrimitiveGetter(original);
    }


    protected LocalizedAllGetter createLocalizedAllGetter(YAttributeDescriptor desc)
    {
        return new LocalizedAllGetter(this, desc, isManagerMode());
    }


    protected TypedSetter createTypedSetter(YAttributeDescriptor desc)
    {
        return new TypedSetter(this, desc, isManagerMode(), this.generator.getCustomSettersSignatureInfoMap());
    }


    protected PrimitiveSetter createPrimitiveSetter(TypedSetter original)
    {
        return new PrimitiveSetter(original);
    }


    protected LocalizedAllSetter createLocalizedAllSetter(YAttributeDescriptor desc)
    {
        return new LocalizedAllSetter(this, desc, isManagerMode());
    }


    protected AddToRelation createAddToRelation(YAttributeDescriptor desc)
    {
        return new AddToRelation(this, desc, isManagerMode());
    }


    protected RemoveFromRelation createRemoveFromRelation(YAttributeDescriptor desc)
    {
        return new RemoveFromRelation(this, desc, isManagerMode());
    }


    protected Set<YAttributeDescriptor> getAttributes()
    {
        return getExtension().getOwnAttributes(getType().getCode());
    }


    protected String getRelationHandlerMemberName(YAttributeDescriptor relationAttributeDescriptor)
    {
        return (relationAttributeDescriptor.getQualifier() + "Handler").toUpperCase();
    }


    protected boolean mustGenerateSetter(YAttributeDescriptor ad)
    {
        return (ad.isGenerate() && ad.getPersistenceType() != YAttributeDescriptor.PersistenceType.DYNAMIC && (ad.isWritable() || ad.isInitial()) && (ad
                        .isDeclared() || isRedeclardAsWritable(ad) || ad.isRedeclaredOneToManyRelationEnd()));
    }


    protected boolean isRedeclardAsWritable(YAttributeDescriptor ad)
    {
        YAttributeDescriptor superAd = ad.getSuperAttribute();
        return (superAd != null && (ad.isInitial() || ad.isWritable()) && !superAd.isInitial() && !superAd.isWritable());
    }


    protected boolean mustGenerateGetter(YAttributeDescriptor ad)
    {
        return (ad.isGenerate() && ad.getPersistenceType() != YAttributeDescriptor.PersistenceType.DYNAMIC && ad.isReadable() && (ad
                        .isDeclared() || isRedeclardAsReadable(ad) || ad.isRedeclaredOneToManyRelationEnd()));
    }


    protected boolean isRedeclardAsReadable(YAttributeDescriptor ad)
    {
        YAttributeDescriptor superAd = ad.getSuperAttribute();
        return (superAd != null && ad.isReadable() && !superAd.isReadable());
    }


    private String getDefaultInitialPropertyMapRefFromSuperType()
    {
        String ref = "";
        YComposedType superType = getType().getSuperType();
        if(superType != null && isGenericItem(superType) && !"GenericItem".equalsIgnoreCase(superType.getCode()))
        {
            YExtension ext = (YExtension)superType.getNamespace();
            return addRequiredImport(CodeGenerator.getJaloClassName(superType, getGenerator().getExtensionPackage(ext))) + ".DEFAULT_INITIAL_ATTRIBUTES";
        }
        return "";
    }


    private void addDefaultInitialPropertyAttributes()
    {
        StringBuilder sb = new StringBuilder();
        String attrModeType = addRequiredImport("de.hybris.platform.jalo.Item.AttributeMode");
        sb.append("protected static final Map<String, ").append(attrModeType).append("> DEFAULT_INITIAL_ATTRIBUTES;\n");
        sb.append("static\n");
        sb.append("{\n");
        sb.append("final ").append(addRequiredImport("java.util.Map")).append("<String, ").append(attrModeType)
                        .append("> tmp = new ").append(addRequiredImport("java.util.HashMap")).append("<String, ").append(attrModeType)
                        .append(">(").append(getDefaultInitialPropertyMapRefFromSuperType()).append(");\n");
        for(YAttributeDescriptor ad : getAttributes())
        {
            if(ad.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY && ad.isDeclared())
            {
                sb.append("tmp.put(").append(ad.getQualifier().toUpperCase()).append(", ").append(attrModeType)
                                .append(".INITIAL);\n");
            }
        }
        sb.append("DEFAULT_INITIAL_ATTRIBUTES = ").append(addRequiredImport("java.util.Collections"))
                        .append(".unmodifiableMap(tmp);\n");
        sb.append("}\n");
        sb.append("@Override\n");
        sb.append("protected ").append(addRequiredImport("java.util.Map")).append("<String, ").append(attrModeType)
                        .append("> getDefaultAttributeModes()\n");
        sb.append("{\n");
        sb.append("return DEFAULT_INITIAL_ATTRIBUTES;\n");
        sb.append("}\n");
        addDeclaration(sb.toString());
    }


    protected boolean mustAddRelationOverrideConstant(String relationName)
    {
        return this.relationOverrideConstantsAddedFor.add(relationName);
    }


    protected boolean mustAddRelationMarkModifiedOverrideConstant(String relationName)
    {
        return this.relationDisableMarkModified.add(relationName);
    }


    protected void fill()
    {
        String doc;
        Set<YAttributeDescriptor> attributes = getAttributes();
        if(isJaloLogicFree())
        {
            doc = "Generated class for type " + getType().getCode() + ".";
        }
        else
        {
            doc = "Generated class for type {@link " + getType().getJavaClassName() + " " + getType().getCode() + "}.";
        }
        setJavadoc(doc);
        List<YAttributeDescriptor> attributesWithMarkModified = new ArrayList<>();
        for(YAttributeDescriptor ad : attributes)
        {
            boolean mustGenerateGetter = mustGenerateGetter(ad);
            boolean mustGenerateSetter = mustGenerateSetter(ad);
            if(mustGenerateGetter || mustGenerateSetter)
            {
                boolean generatePrimitive = AbstractPrimitiveAttributeMethod.isPrimitive(ad);
                if(ad.isDeclared())
                {
                    writeDeclaredAttribute(ad, attributesWithMarkModified);
                }
                String descr = (ad.getDescription() != null && ad.getDescription().length() > 0) ? (" - " + ad.getDescription()) : "";
                if(mustGenerateGetter)
                {
                    fillTypedGetter(ad, descr, generatePrimitive);
                }
                if(mustGenerateSetter)
                {
                    fillTypedSetter(ad, descr, generatePrimitive);
                }
            }
        }
        if(attributesWithMarkModified.size() > 0)
        {
            addMethod(createIsMarkModifiedDisabled(attributesWithMarkModified));
        }
        List<String> relationHandlerMembers = writeRelationHandlers(attributes);
        if(isDefaultPropsEnabled() && isGenericItem() && !isManagerMode())
        {
            if(!relationHandlerMembers.isEmpty())
            {
                addCreateItemForRelation(relationHandlerMembers);
            }
            addDefaultInitialPropertyAttributes();
        }
    }


    private void writeDeclaredAttribute(YAttributeDescriptor ad, List<YAttributeDescriptor> attributesWithMarkModified)
    {
        if(!isManagerMode())
        {
            addDeclaration("/** Qualifier of the <code>" + getType().getCode() + "." + ad.getQualifier() + "</code> attribute **/\npublic static final String " + ad
                            .getQualifier()
                            .toUpperCase() + " = \"" + ad
                            .getQualifier() + "\";\n");
        }
        if(ad.isRelationEndAttribute() && !ad.getRelationEnd().getRelation().isAbstract())
        {
            String relationName = ad.getRelationEnd().getRelationCode();
            if(mustAddRelationOverrideConstant(relationName))
            {
                addDeclaration("/** Relation ordering override parameter constants for " + relationName + " from " + ad
                                .getRelationEnd().getRelation().getNamespace() + "*/\nprotected static String " + relationName
                                .toUpperCase() + "_SRC_ORDERED = \"relation." + relationName + ".source.ordered\";\nprotected static String " + relationName
                                .toUpperCase() + "_TGT_ORDERED = \"relation." + relationName + ".target.ordered\";");
            }
            if(mustAddRelationMarkModifiedOverrideConstant(relationName))
            {
                addDeclaration("/** Relation disable markmodifed parameter constants for " + relationName + " from " + ad
                                .getRelationEnd().getRelation().getNamespace() + "*/\nprotected static String " + relationName
                                .toUpperCase() + "_MARKMODIFIED = \"relation." + relationName + ".markmodified\";");
                if(shouldGenerateWithMarkModified())
                {
                    attributesWithMarkModified.add(ad);
                }
            }
        }
    }


    private List<String> writeRelationHandlers(Set<YAttributeDescriptor> attributes)
    {
        List<String> relationHandlerMembers = new ArrayList<>();
        for(YAttributeDescriptor ad : attributes)
        {
            if((ad.isDeclared() || (ad.isRelationEndAttribute() && ad.isRedeclared())) && ad.isGenerate())
            {
                if(ad.isRelationEndAttribute())
                {
                    String memberName = getRelationHandlerMemberName(ad);
                    if(ad.getRelationEnd().getOppositeEnd().getCardinality() == YRelationEnd.Cardinality.ONE)
                    {
                        writeRelationHandlerOnTheManyEnd(ad, memberName);
                        continue;
                    }
                    if(ad.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.ONE && ad
                                    .getRelationEnd().getOppositeEnd().getCardinality() == YRelationEnd.Cardinality.MANY && !isManagerMode())
                    {
                        writeRelationHandlerOnTheOneEnd(ad, memberName);
                        relationHandlerMembers.add(memberName);
                    }
                }
            }
        }
        return relationHandlerMembers;
    }


    private void addCreateItemForRelation(List<String> relationHandlerMembers)
    {
        MethodWriter createItem = new MethodWriter(Visibility.PROTECTED, "de.hybris.platform.jalo.Item", "createItem");
        createItem.addAnnotation("Override");
        createItem.addParameter("de.hybris.platform.jalo.SessionContext", "ctx");
        createItem.addParameter("de.hybris.platform.jalo.type.ComposedType", "type");
        createItem.addParameter("ItemAttributeMap", "allAttributes");
        createItem.addThrownException("de.hybris.platform.jalo.JaloBusinessException");
        StringBuilder sb = new StringBuilder();
        for(String member : relationHandlerMembers)
        {
            sb.append(member).append(".newInstance(ctx, allAttributes);");
            sb.append("\n");
        }
        createItem.setContentPlain(sb.toString() + "return super.createItem( ctx, type, allAttributes );\n");
        addMethod(createItem);
    }


    private boolean isGenericItem()
    {
        return isGenericItem(getType());
    }


    private boolean isGenericItem(YComposedType type)
    {
        return getExtension().getTypeSystem().getType("GenericItem").isAssignableFrom((YType)type);
    }


    private boolean isDefaultPropsEnabled()
    {
        return !this.extensionBlackListForDefaultProps.contains(getExtension().getExtensionName());
    }


    private void writeRelationHandlerOnTheOneEnd(YAttributeDescriptor ad, String memberName)
    {
        StringBuilder javaDoc = new StringBuilder(100);
        String handlerClassName = addRequiredImport("de.hybris.platform.util.BidirectionalOneToManyHandler");
        javaDoc.append("\n").append("/**").append("\n");
        javaDoc.append("* {@link " + handlerClassName + "} for handling 1:n " + ad
                                        .getQualifier().toUpperCase() + "'s relation attributes from 'one' side.")
                        .append("\n");
        javaDoc.append("**/").append("\n");
        addDeclaration(javaDoc.toString());
        YRelationEnd relationSource = ad.getRelationEnd().getOppositeEnd();
        YRelationEnd relationTarget = ad.getRelationEnd();
        YComposedType handlerType = ad.isRedeclared() ? (YComposedType)ad.getType() : relationSource.getType();
        writeRelationHandler(ad, getClassName(), handlerClassName, memberName, relationSource, relationTarget, handlerType);
    }


    private void writeRelationHandlerOnTheManyEnd(YAttributeDescriptor ad, String memberName)
    {
        StringBuilder javaDoc = new StringBuilder(100);
        String handlerClassName = addRequiredImport("de.hybris.platform.util.OneToManyHandler");
        javaDoc.append("\n").append("/**").append("\n");
        javaDoc.append("* {@link " + handlerClassName + "} for handling 1:n " + ad
                                        .getQualifier().toUpperCase() + "'s relation attributes from 'many' side.")
                        .append("\n");
        String conditionQuery = ad.getConditionQuery();
        if(StringUtils.isNotBlank(conditionQuery))
        {
            javaDoc.append("Condition query: ").append(conditionQuery).append("\n");
        }
        javaDoc.append("**/").append("\n");
        addDeclaration(javaDoc.toString());
        YRelationEnd relationSource = ad.getRelationEnd();
        YRelationEnd relationTarget = ad.getRelationEnd().getOppositeEnd();
        YComposedType handlerType = ad.isRedeclared() ? (YComposedType)((YCollectionType)ad.getType()).getElementType() : relationSource.getType();
        String targetTypeClassName = addRequiredImport(getGenerator().getJaloClassName(relationSource.getType()));
        writeRelationHandler(ad, targetTypeClassName, handlerClassName, memberName, relationSource, relationTarget, handlerType);
    }


    private void writeRelationHandler(YAttributeDescriptor ad, String targetTypeClassName, String handlerClassName, String memberName, YRelationEnd relationSource, YRelationEnd relationTarget, YComposedType handlerType)
    {
        String typeofCollectionConstant, targetTypeConstantsClassName = ConstantsWriter.assembleConstantsClassName(getGenerator(), (YExtension)handlerType
                        .getNamespace());
        String foreignKeyConstant = "\"" + relationTarget.getQualifier() + "\"";
        String orderingAttributeConstant = null;
        String customOrderingAttribute = findCustomOrderingAttribute(relationTarget, relationSource);
        if(StringUtils.isNotBlank(customOrderingAttribute))
        {
            orderingAttributeConstant = customOrderingAttribute;
        }
        else if(relationSource.isOrdered() || relationTarget.isOrdered())
        {
            orderingAttributeConstant = "\"" + relationTarget.getQualifier() + "POS\"";
        }
        switch(relationSource.getCollectionType().getTypeCode())
        {
            case 1:
                typeofCollectionConstant = "SET";
                break;
            case 2:
                typeofCollectionConstant = "LIST";
                break;
            case 3:
                typeofCollectionConstant = "SORTED_SET";
                break;
            default:
                typeofCollectionConstant = "COLLECTION";
                break;
        }
        addDeclaration("protected static final " + handlerClassName + "<" + targetTypeClassName + "> " + memberName + " = new " + handlerClassName + "<" + targetTypeClassName + ">(\n" + (
                        (targetTypeConstantsClassName != null) ? (
                                        addRequiredImport(targetTypeConstantsClassName) + ".TC." + addRequiredImport(targetTypeConstantsClassName)) : ("\"" +
                                        handlerType.getCode() + "\"")) + ",\n" + Boolean.toString(ad.isPartOf()) + ",\n" + foreignKeyConstant + ",\n" + orderingAttributeConstant + ",\n" + relationSource
                        .isOrdered() + ",\ntrue, \n" +
                        addRequiredImport("de.hybris.platform.jalo.type.CollectionType") + "." + typeofCollectionConstant +
                        newLineOrConditionQueryIfExists(ad) + ");\n");
    }


    private String newLineOrConditionQueryIfExists(YAttributeDescriptor ad)
    {
        String conditionQuery = ad.getConditionQuery();
        if(StringUtils.isNotBlank(conditionQuery))
        {
            return ",\n" + conditionQuery + "\n";
        }
        return "\n";
    }


    protected String findCustomOrderingAttribute(YRelationEnd relationTarget, YRelationEnd relationSource)
    {
        String custom = (String)relationTarget.getCustomProps().get("ordering.attribute");
        return StringUtils.isBlank(custom) ? (String)relationSource.getCustomProps().get("ordering.attribute") :
                        custom;
    }


    protected void fillTypedGetter(YAttributeDescriptor ad, String descr, boolean generatePrimitive)
    {
        TypedGetter getter = createTypedGetter(ad);
        getter.setJavadoc("<i>Generated method</i> - Getter of the <code>" + getType().getCode() + "." + ad.getQualifier() + "</code> attribute.\n\n@return the " + ad
                        .getQualifier() + descr);
        addMethod((MethodWriter)getter);
        MethodWriter getterCtx = getter.createNonCtxDelegateMethod();
        addMethod(getterCtx);
        if(generatePrimitive)
        {
            PrimitiveGetter pGetter = createPrimitiveGetter(getter);
            pGetter.setJavadoc("<i>Generated method</i> - Getter of the <code>" + getType().getCode() + "." + ad.getQualifier() + "</code> attribute. \n\n@return the " + ad
                            .getQualifier() + descr);
            addMethod((MethodWriter)pGetter);
            addMethod(pGetter.createNonCtxDelegateMethod());
        }
        if(ad.isLocalized())
        {
            LocalizedAllGetter locAllGetter = createLocalizedAllGetter(ad);
            locAllGetter.setJavadoc("<i>Generated method</i> - Getter of the <code>" +
                            getType().getCode() + "." + ad.getQualifier() + "</code> attribute. \n\n@return the localized " + ad
                            .getQualifier() + descr);
            addMethod((MethodWriter)locAllGetter);
            addMethod(locAllGetter.createNonCtxDelegateMethod());
        }
        if(ad.isRelationEndAttribute() && !ad.getRelationEnd().getRelation().isAbstract())
        {
            RelationCount cnt = new RelationCount(this, ad, isManagerMode());
            addMethod((MethodWriter)cnt);
            for(MethodWriter del : cnt.createDelegateMethods())
            {
                addMethod(del);
            }
        }
    }


    protected void fillTypedSetter(YAttributeDescriptor ad, String descr, boolean generatePrimitive)
    {
        TypedSetter setter = createTypedSetter(ad);
        setter.setJavadoc("<i>Generated method</i> - Setter of the <code>" + getType().getCode() + "." + ad.getQualifier() + "</code> attribute. \n\n@param value the " + ad
                        .getQualifier() + descr);
        addMethod((MethodWriter)setter);
        MethodWriter setterCtx = setter.createNonCtxDelegateMethod();
        addMethod(setterCtx);
        if(generatePrimitive)
        {
            PrimitiveSetter pSetter = createPrimitiveSetter(setter);
            pSetter.setJavadoc("<i>Generated method</i> - Setter of the <code>" + getType().getCode() + "." + ad.getQualifier() + "</code> attribute. \n\n@param value the " + ad
                            .getQualifier() + descr);
            addMethod((MethodWriter)pSetter);
            addMethod(pSetter.createNonCtxDelegateMethod());
        }
        if(ad.isLocalized())
        {
            LocalizedAllSetter locAllSetter = createLocalizedAllSetter(ad);
            locAllSetter.setJavadoc("<i>Generated method</i> - Setter of the <code>" +
                            getType().getCode() + "." + ad.getQualifier() + "</code> attribute. \n\n@param value the " + ad
                            .getQualifier() + descr);
            addMethod((MethodWriter)locAllSetter);
            addMethod(locAllSetter.createNonCtxDelegateMethod());
        }
        if(ad.isRelationEndAttribute() && ad.getRelationEnd().getCardinality() == YRelationEnd.Cardinality.MANY)
        {
            AddToRelation addTo = createAddToRelation(ad);
            addTo.setJavadoc("<i>Generated method</i> - Adds <code>value</code> to " + ad.getQualifier() + ". \n\n@param value the item to add to " + ad
                            .getQualifier() + descr);
            addMethod((MethodWriter)addTo);
            addMethod(addTo.createNonCtxDelegateMethod());
            RemoveFromRelation removeFrom = createRemoveFromRelation(ad);
            removeFrom.setJavadoc("<i>Generated method</i> - Removes <code>value</code> from " + ad.getQualifier() + ". \n\n@param value the item to remove from " + ad
                            .getQualifier() + descr);
            addMethod((MethodWriter)removeFrom);
            addMethod(removeFrom.createNonCtxDelegateMethod());
        }
    }


    private MethodWriter createIsMarkModifiedDisabled(List<YAttributeDescriptor> attributes)
    {
        addRequiredImport("de.hybris.platform.jalo.type.ComposedType");
        addRequiredImport("de.hybris.platform.jalo.type.TypeManager");
        addRequiredImport("de.hybris.platform.util.Utilities");
        MethodWriter createItem = new MethodWriter(Visibility.PUBLIC, "boolean", "isMarkModifiedDisabled");
        createItem.addAnnotation("Override");
        createItem.addDeprecatedAnnotation("2105");
        createItem.addParameter("de.hybris.platform.jalo.Item", "referencedItem");
        createItem.setJavadoc("@deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)");
        StringBuilder body = new StringBuilder();
        for(YAttributeDescriptor attr : attributes)
        {
            String relationCode = attr.getRelationEnd().getRelationCode();
            String typeCode = attr.getRelationEnd().getTypeCode();
            String filedName = "relationSecondEnd" + attributes.indexOf(attr);
            body.append("ComposedType " + filedName + " = TypeManager.getInstance().getComposedType(\"" + typeCode + "\");\n");
            body.append("if(" + filedName + ".isAssignableFrom(referencedItem.getComposedType()))\n");
            body.append("{\n");
            body.append("return Utilities.getMarkModifiedOverride(" + relationCode.toUpperCase() + "_MARKMODIFIED);\n");
            body.append("}\n");
        }
        body.append("return true;");
        createItem.setContentPlain(body.toString());
        return createItem;
    }


    public YComposedType getType()
    {
        return this.type;
    }


    public boolean isManagerMode()
    {
        return this.managerMode;
    }


    public boolean shouldGenerateWithMarkModified()
    {
        return true;
    }
}
