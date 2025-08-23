package de.hybris.bootstrap.codegenerator.model;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YType;
import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.iterators.ReverseListIterator;
import org.apache.log4j.Logger;

public class ModelWriter extends ClassWriter
{
    private static final String ROOT_MODEL = "de.hybris.platform.servicelayer.model.AbstractItemModel";
    private static final Logger LOG = Logger.getLogger(ModelWriter.class);
    private static final String CONSTRUCTOR_DEPRECATION_VERSION = "4.1.1";
    private static final String ITEM_TYPE = "itemtype";
    private final YComposedType type;


    public ModelWriter(CodeGenerator gen, YExtension ext, YComposedType type)
    {
        super(gen, ext, ModelNameUtils.getModelName(type));
        this.type = type;
        setModifiers(0);
        setVisibility(Visibility.PUBLIC);
        if(type.getSuperType() == null)
        {
            setClassToExtend(addRequiredImport("de.hybris.platform.servicelayer.model.AbstractItemModel"));
        }
        else
        {
            setClassToExtend(addRequiredImport(ModelNameUtils.getModel(type.getSuperType(),
                            getGenerator().getExtensionPackage((YExtension)type
                                            .getSuperType()
                                            .getNamespace()))));
        }
        setPackageName(
                        ModelNameUtils.getModelPackage(type, getGenerator().getExtensionPackage((YExtension)type.getNamespace())));
        String copyright = getCopyright();
        setCopyright(GENERATED_NOTICE + GENERATED_NOTICE);
        setJavadoc(generateClassDescription());
        addAnnotation("SuppressWarnings(\"all\")");
        addAnnotationIfDeprecated(type);
    }


    protected String generateClassDescription()
    {
        StringBuilder description = new StringBuilder("Generated model class for type " + this.type.getCode() + " first defined at extension " + ((YExtension)this.type.getNamespace()).getExtensionName() + ".");
        if(this.type.getTypeDescription() != null)
        {
            description.append("\n");
            description.append("<p>");
            description.append("\n");
            description
                            .append(
                                            this.type.getTypeDescription().endsWith(".") ? this.type.getTypeDescription() : (this.type.getTypeDescription() + "."));
        }
        return description.toString();
    }


    protected void fill()
    {
        Set<YAttributeDescriptor> allAttributes = gatherAttributes(this.type.getCode());
        Set<YAttributeDescriptor> attributesNonPrivateAndWithoutPKandItemType = filterAttributes(allAttributes, true, new String[] {"pk", "itemtype"});
        writeModelConstructors(attributesNonPrivateAndWithoutPKandItemType);
        writeConstants(allAttributes);
        writeGetters(attributesNonPrivateAndWithoutPKandItemType);
        writeSetters(attributesNonPrivateAndWithoutPKandItemType);
    }


    private void writeModelConstructors(Set<YAttributeDescriptor> attributes)
    {
        addConstructor((MethodWriter)generatePublicNoArgConstructor());
        addConstructor((MethodWriter)generatePublicItemModelContextConstructor());
        for(YComposedType.YModelConstructor constDef : this.type.getYModelConstructors())
        {
            addConstructor((MethodWriter)generatedConvenienceConstructor(constDef));
        }
        for(ModelConstructorWriter constr : generatedConfiguredAndDeprecatedConstructors(attributes))
        {
            addConstructor((MethodWriter)constr);
        }
    }


    private Collection<ModelConstructorWriter> generatedConfiguredAndDeprecatedConstructors(Set<YAttributeDescriptor> attributes)
    {
        Collection<ModelConstructorWriter> ret = new ArrayList<>();
        String createAdditionalConstr = getGenerator().getPlatformProperties().getProperty("servicelayer.generate.constructors", "deprecated").trim();
        if("on".equals(createAdditionalConstr) || "deprecated".equals(createAdditionalConstr))
        {
            List[] attributesForConstructors = gatherAttributesForConstructors(attributes);
            List<String> mandatoryConstAttributes = attributesForConstructors[0];
            List<String> initialConstAttributes = attributesForConstructors[1];
            if(!mandatoryConstAttributes.isEmpty() || !initialConstAttributes.isEmpty())
            {
                boolean isDeprecated = "deprecated".equals(createAdditionalConstr);
                String deprecatedInfo = isDeprecated ? "\n@deprecated since 4.1.1 Please use the default constructor without parameters\n" : "";
                if(!mandatoryConstAttributes.isEmpty())
                {
                    String javaDoc = "<i>Generated constructor</i> - Constructor with all mandatory attributes." + deprecatedInfo;
                    ret.add(generatedOldConstructor(mandatoryConstAttributes, javaDoc, isDeprecated, "4.1.1", mandatoryConstAttributes));
                }
                if(!initialConstAttributes.isEmpty() && mandatoryConstAttributes.size() < initialConstAttributes.size())
                {
                    String javaDoc = "<i>Generated constructor</i> - for all mandatory and initial attributes." + deprecatedInfo;
                    ret.add(generatedOldConstructor(initialConstAttributes, javaDoc, isDeprecated, "4.1.1", mandatoryConstAttributes));
                }
            }
        }
        return ret;
    }


    private List[] gatherAttributesForConstructors(Set<YAttributeDescriptor> attributes)
    {
        List<String> mandatoryConstAttributes = new ArrayList<>();
        List<String> initialConstAttributes = new ArrayList<>();
        Set<YAttributeDescriptor> allAttributes = gatherInheritedAttributes();
        allAttributes.addAll(attributes);
        for(YAttributeDescriptor yad : allAttributes)
        {
            if("itemtype".equalsIgnoreCase(yad.getQualifier()) || "creationtime".equalsIgnoreCase(yad.getQualifier()))
            {
                continue;
            }
            if(yad.isPrivate() || yad.isRedeclared() || !yad.isGenerateInModel() ||
                            !isAttributeTypeGenerationEnabled(yad.getType()))
            {
                continue;
            }
            if(!yad.isOptional() && (yad.isWritable() || yad.isInitial()) && yad.getDefaultValueDefinition() == null &&
                            !yad.isLocalized())
            {
                mandatoryConstAttributes.add(yad.getQualifier());
                initialConstAttributes.add(yad.getQualifier());
                continue;
            }
            if(yad.isOptional() && yad.isInitial() && !yad.isWritable())
            {
                initialConstAttributes.add(yad.getQualifier());
            }
        }
        return new List[] {mandatoryConstAttributes, initialConstAttributes};
    }


    private ModelConstructorWriter generatedConvenienceConstructor(YComposedType.YModelConstructor constDef)
    {
        ModelConstructorWriter definedConstructor = new ModelConstructorWriter(Visibility.PUBLIC, getClassName(), false);
        for(YAttributeDescriptor yad : constDef.getConstructorParameters())
        {
            String type = ModelNameUtils.getModelOrClass(yad, getGenerator(), true);
            definedConstructor.addParameter(type, yad, true);
            definedConstructor.setJavadoc("<i>Defined constructor from items.xml</i>");
            definedConstructor.addComment("Constructor was defined in file: " + constDef.getLocationOfDefinition());
        }
        return definedConstructor;
    }


    private ModelConstructorWriter generatePublicNoArgConstructor()
    {
        ModelConstructorWriter emptyConstructor = new ModelConstructorWriter(Visibility.PUBLIC, getClassName(), false);
        emptyConstructor.setJavadoc("<i>Generated constructor</i> - Default constructor for generic creation.");
        return emptyConstructor;
    }


    private ModelConstructorWriter generatePublicItemModelContextConstructor()
    {
        Object object = new Object(this, Visibility.PUBLIC, getClassName(), false);
        object.setJavadoc("<i>Generated constructor</i> - Default constructor for creation with existing context\n@param ctx the model context to be injected, must not be null");
        object.addParameter("de.hybris.platform.servicelayer.model.ItemModelContext", "ctx");
        return (ModelConstructorWriter)object;
    }


    private ModelConstructorWriter generatedOldConstructor(List<String> paramList, String javaDoc, boolean isDeprecated, String deprecatedSince, List<String> mandatoryAttributes)
    {
        String text = "Automatically created default constructor. See advanced.properties, key: servicelayer.generate.constructors for enabling/disabling/set to deprecated";
        YComposedType.YModelConstructor oldConstructor = new YComposedType.YModelConstructor(this.type,
                        new ModelTagListener.ModelDataConstructor("Automatically created default constructor. See advanced.properties, key: servicelayer.generate.constructors for enabling/disabling/set to deprecated", paramList, true), this.type.getNamespace());
        if(!constructorWithSameSignatureExists(oldConstructor))
        {
            ModelConstructorWriter initialAttributeConstructor = new ModelConstructorWriter(getClassName());
            initialAttributeConstructor.setJavadoc(javaDoc);
            if(isDeprecated)
            {
                initialAttributeConstructor.addDeprecatedAnnotation(deprecatedSince);
            }
            for(YAttributeDescriptor yad : oldConstructor.getConstructorParameters())
            {
                boolean isItemTypeAttribute = "itemtype".equalsIgnoreCase(yad.getQualifier());
                String declaredType = isItemTypeAttribute ? "java.lang.String" : ModelNameUtils.getModelOrClass(yad,
                                getGenerator(), true);
                initialAttributeConstructor.addParameter(declaredType, yad, mandatoryAttributes.contains(yad));
            }
            return initialAttributeConstructor;
        }
        return null;
    }


    private boolean constructorWithSameSignatureExists(YComposedType.YModelConstructor oldConstructor)
    {
        for(YComposedType.YModelConstructor existingConst : this.type.getYModelConstructors())
        {
            if(existingConst.hasSameSignature(oldConstructor))
            {
                LOG.error("Found error!", new IllegalArgumentException("Could not generate old model constructor  (for backward compability). You declared a model constructor " + existingConst
                                + " with same signature. Remove this constructor or disable in the file advanced.properties the key: servicelayer.generate.constructors (set to off). But be warned: Possibility of not compiling code in non-hybris model constructors may occure."));
                return true;
            }
        }
        return false;
    }


    private void writeConstants(Set<YAttributeDescriptor> attributes)
    {
        addConstantDeclaration("/**<i>Generated model type code constant.</i>*/\npublic static final String _TYPECODE = \"" + this.type
                        .getCode() + "\";\n ");
        for(YAttributeDescriptor ad : attributes)
        {
            if(ad.getRelationEnd() != null && ad.getRelationEnd().isSource())
            {
                addConstantDeclaration("/**<i>Generated relation code constant for relation <code>" + ad
                                .getRelationEnd()
                                .getRelationCode() + "</code> defining source attribute <code>" + ad.getQualifier() + "</code> in extension <code>" + ((YExtension)ad
                                .getRelationEnd().getNamespace())
                                .getExtensionName() + "</code>.</i>*/\npublic static final String _" + ad
                                .getRelationEnd()
                                .getRelationCode()
                                .toUpperCase() + " = \"" + ad
                                .getRelationEnd().getRelationCode() + "\";\n ");
            }
        }
        for(YAttributeDescriptor ad : attributes)
        {
            if(!ad.isRedeclared())
            {
                String constantName = "\"" + ad.getQualifier() + "\"";
                addConstantDeclaration("/** <i>Generated constant</i> - Attribute key of <code>" + this.type.getCode() + "." + ad
                                .getQualifier() + "</code> attribute defined at extension <code>" + ((YExtension)ad
                                .getNamespace()).getExtensionName() + "</code>. */\npublic static final String " + ad
                                .getQualifier().toUpperCase() + " = " + constantName + ";\n ");
            }
        }
    }


    private void writeGetters(Set<YAttributeDescriptor> attributes)
    {
        for(YAttributeDescriptor attDesc : attributes)
        {
            String declaredType = gatherDeclaredType(attDesc);
            if(needsPublicGetter(attDesc, declaredType))
            {
                writePublicGetter(attDesc);
                continue;
            }
            if(!attDesc.isReadable() && isRedeclaredReadableInSubtype(attDesc))
            {
                addMethod((MethodWriter)new ModelGetterWriter(Visibility.PROTECTED, getGenerator(), attDesc));
            }
        }
    }


    private void writePublicGetter(YAttributeDescriptor attDesc)
    {
        ModelGetterWriter modelGetterWriter = new ModelGetterWriter(Visibility.PUBLIC, getGenerator(), attDesc);
        List<ModelTagListener.ModelDataMethod> modelDeclaredGetters = attDesc.getGetters();
        for(ModelTagListener.ModelDataMethod getter : modelDeclaredGetters)
        {
            if(getter.defaultAttribute)
            {
                modelGetterWriter = new ModelGetterWriter(Visibility.PUBLIC, getGenerator(), attDesc, getter.name, getter.nullDecorator, getter.deprecated, getter.deprecatedSince);
                continue;
            }
            AlternativeModelGetterWriter alternativeModelWriter = new AlternativeModelGetterWriter(new ModelGetterWriter(Visibility.PUBLIC, getGenerator(), attDesc, attDesc.getQualifier(), getter.nullDecorator, getter.deprecated, getter.deprecatedSince), attDesc, getter.name);
            addMethod((MethodWriter)alternativeModelWriter);
        }
        addAccessorAnnotation(attDesc.getQualifier(), (MethodWriter)modelGetterWriter, true);
        addMethod((MethodWriter)modelGetterWriter);
    }


    private void writeSetters(Set<YAttributeDescriptor> attributes)
    {
        for(YAttributeDescriptor attDesc : attributes)
        {
            String declaredType = gatherDeclaredType(attDesc);
            if(needsPublicSetter(attDesc, declaredType))
            {
                writePublicSetter(attDesc);
                continue;
            }
            if(!attDesc.isWritable() && !attDesc.isInitial() && isRedeclaredWritableInSubtype(attDesc))
            {
                addMethod((MethodWriter)new ModelSetterWriter(Visibility.PROTECTED, getGenerator(), attDesc));
            }
        }
    }


    private void writePublicSetter(YAttributeDescriptor attDesc)
    {
        ModelSetterWriter modelSetterWriter = new ModelSetterWriter(Visibility.PUBLIC, getGenerator(), attDesc);
        List<ModelTagListener.ModelDataMethod> modelDefinedSetters = attDesc.getSetters();
        for(ModelTagListener.ModelDataMethod setter : modelDefinedSetters)
        {
            if(setter.defaultAttribute)
            {
                modelSetterWriter = new ModelSetterWriter(Visibility.PUBLIC, getGenerator(), attDesc, setter.name, setter.deprecated, setter.deprecatedSince);
                continue;
            }
            addMethod((MethodWriter)new AlternativeModelSetterWriter(new ModelSetterWriter(Visibility.PUBLIC,
                            getGenerator(), attDesc, setter.deprecated, setter.deprecatedSince), attDesc, setter.name));
        }
        addAccessorAnnotation(attDesc.getQualifier(), (MethodWriter)modelSetterWriter, false);
        addMethod((MethodWriter)modelSetterWriter);
    }


    private void addAccessorAnnotation(String qualifier, MethodWriter writer, boolean getter)
    {
        writer.addRequiredImport("de.hybris.bootstrap.annotations.Accessor");
        writer.addAnnotation("Accessor(qualifier = \"" + qualifier + "\", type = " + (
                        getter ? "Accessor.Type.GETTER" : "Accessor.Type.SETTER") + ")");
    }


    private Set<YAttributeDescriptor> gatherAttributes(String typeCode)
    {
        Set<YAttributeDescriptor> mySet = new LinkedHashSet<>(getGenerator().getTypeSystem().getAttributes(typeCode));
        for(Iterator<YAttributeDescriptor> iter = mySet.iterator(); iter.hasNext(); )
        {
            YAttributeDescriptor attribute = iter.next();
            if(isAttributeIgnored(attribute))
            {
                iter.remove();
            }
        }
        return mySet;
    }


    private Set<YAttributeDescriptor> filterAttributes(Set<YAttributeDescriptor> all, boolean excludePrivate, String... qualifiers)
    {
        Set<YAttributeDescriptor> mySet = new LinkedHashSet<>();
        label18:
        for(YAttributeDescriptor attribute : all)
        {
            String[] arrayOfString;
            int i;
            byte b;
            for(arrayOfString = qualifiers, i = arrayOfString.length, b = 0; b < i; )
            {
                String excluded = arrayOfString[b];
                if(!excluded.equalsIgnoreCase(attribute.getQualifier()))
                {
                    if(attribute.isPrivate() && excludePrivate)
                    {
                        continue label18;
                    }
                    b++;
                }
                continue label18;
            }
            mySet.add(attribute);
        }
        return mySet;
    }


    private boolean isAttributeIgnored(YAttributeDescriptor attribute)
    {
        return ((attribute
                        .isRedeclared() && attribute
                        .getType().equals(attribute.getDeclaringAttribute().getType()) && attribute
                        .getDeclaringAttribute().isWritable()) || (
                        !attribute.isPrivate() && (!attribute.isGenerateInModel() ||
                                        !isAttributeTypeGenerationEnabled(attribute.getType()))));
    }


    private Set<YAttributeDescriptor> gatherInheritedAttributes()
    {
        YComposedType superType = this.type.getSuperType();
        Set<YAttributeDescriptor> result = new LinkedHashSet<>();
        List<Set<YAttributeDescriptor>> subResult = new LinkedList<>();
        while(superType != null)
        {
            Set<YAttributeDescriptor> mySet = new LinkedHashSet<>(getGenerator().getTypeSystem().getAttributes(superType
                            .getCode()));
            for(Iterator<YAttributeDescriptor> iter = mySet.iterator(); iter.hasNext(); )
            {
                YAttributeDescriptor attribute = iter.next();
                if(attribute.isPrivate() || attribute.isRedeclared() || !attribute.isGenerateInModel() ||
                                !isAttributeTypeGenerationEnabled(attribute.getType()))
                {
                    iter.remove();
                }
            }
            subResult.add(mySet);
            superType = superType.getSuperType();
        }
        for(ReverseListIterator it = new ReverseListIterator(subResult); it.hasNext(); )
        {
            Set<YAttributeDescriptor> descs = (Set<YAttributeDescriptor>)it.next();
            result.addAll(descs);
        }
        return result;
    }


    private String gatherDeclaredType(YAttributeDescriptor attDesc)
    {
        if(isItemTypeAttribute(attDesc))
        {
            return "java.lang.String";
        }
        String type = ModelNameUtils.getModelOrClass(attDesc, getGenerator(), true);
        return attDesc.isPrimitive() ? ModelGetterWriter.convertPrimTypes(type) : type;
    }


    private String gatherRedeclaredType(YAttributeDescriptor attDesc, String declaredType)
    {
        YAttributeDescriptor declaringAttribute = attDesc.getDeclaringAttribute();
        if(!attDesc.equals(declaringAttribute) && !(attDesc.getType() instanceof de.hybris.bootstrap.typesystem.YEnumType) && !isItemTypeAttribute(attDesc))
        {
            return ModelNameUtils.getModelOrClass(declaringAttribute, getGenerator(), true);
        }
        return declaredType;
    }


    private boolean isRedeclaredWritableInSubtype(YAttributeDescriptor desc)
    {
        if(!desc.isDeclared())
        {
            return false;
        }
        for(YComposedType subType : desc.getEnclosingType().getAllSubtypes())
        {
            YAttributeDescriptor subAd = subType.getAttribute(desc.getQualifier());
            if(subAd != null && subAd.isRedeclared() && (subAd.isWritable() || subAd.isInitial()))
            {
                return true;
            }
        }
        return false;
    }


    private boolean isRedeclaredReadableInSubtype(YAttributeDescriptor desc)
    {
        if(!desc.isDeclared())
        {
            return false;
        }
        for(YComposedType subType : desc.getEnclosingType().getAllSubtypes())
        {
            YAttributeDescriptor subAd = subType.getAttribute(desc.getQualifier());
            if(subAd != null && subAd.isRedeclared() && subAd.isReadable())
            {
                return true;
            }
        }
        return false;
    }


    private boolean needsPublicGetter(YAttributeDescriptor attDesc, String declaredType)
    {
        return (attDesc.isReadable() && (
                        !attDesc.isRedeclared() || isGetterRequiredForRedeclaredAttribute(attDesc, declaredType)));
    }


    private boolean isGetterRequiredForRedeclaredAttribute(YAttributeDescriptor attDesc, String declaredType)
    {
        boolean isGetterRequired = false;
        if(attDesc.isRedeclared() && !(attDesc.getType() instanceof YCollectionType))
        {
            String redeclaredType = gatherRedeclaredType(attDesc, declaredType);
            if(!redeclaredType.equals(declaredType))
            {
                isGetterRequired = true;
            }
        }
        return isGetterRequired;
    }


    private boolean needsPublicSetter(YAttributeDescriptor attDesc, String declaredType)
    {
        return ((attDesc.isWritable() || attDesc.isInitial()) && ((
                        !attDesc.isRedeclared() && !isItemTypeAttribute(attDesc)) ||
                        isSetterRequiredForRedeclaredAttribute(attDesc, declaredType)));
    }


    private boolean isSetterRequiredForRedeclaredAttribute(YAttributeDescriptor attDesc, String declaredType)
    {
        boolean isSetterRequired = false;
        if(attDesc.isRedeclared() && !(attDesc.getType() instanceof YCollectionType))
        {
            String redeclaredType = gatherRedeclaredType(attDesc, declaredType);
            if(!redeclaredType.equals(declaredType) || (redeclaredType
                            .equals(declaredType) && !attDesc.getDeclaringAttribute().isWritable()))
            {
                isSetterRequired = true;
            }
        }
        return isSetterRequired;
    }


    private boolean isItemTypeAttribute(YAttributeDescriptor attDesc)
    {
        return "itemtype".equalsIgnoreCase(attDesc.getQualifier());
    }


    private boolean isAttributeTypeGenerationEnabled(YType type)
    {
        if(type instanceof de.hybris.bootstrap.typesystem.YEnumType)
        {
            return true;
        }
        if(type instanceof YComposedType)
        {
            YComposedType cType = (YComposedType)type;
            return cType.isGenerateModel();
        }
        if(type instanceof YCollectionType)
        {
            YCollectionType cType = (YCollectionType)type;
            return isAttributeTypeGenerationEnabled(cType.getElementType());
        }
        if(type instanceof YMapType)
        {
            YMapType mType = (YMapType)type;
            return (isAttributeTypeGenerationEnabled(mType.getArgumentType()) &&
                            isAttributeTypeGenerationEnabled(mType.getReturnType()));
        }
        return true;
    }
}
