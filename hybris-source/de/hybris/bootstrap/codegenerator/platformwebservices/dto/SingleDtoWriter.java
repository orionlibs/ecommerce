package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.UniqueIdentifierResolver;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.iterators.ReverseListIterator;

@Deprecated(since = "1818", forRemoval = true)
public class SingleDtoWriter extends AbstractDtoWriter
{
    private static final String ABSTRACT_ITEM_DTO = "de.hybris.platform.webservices.dto.AbstractItemDTO";
    private static final String GRAPHNODE = "de.hybris.platform.webservices.util.objectgraphtransformer.GraphNode";
    private static final String GRAPHPROPERTY = "de.hybris.platform.webservices.util.objectgraphtransformer.GraphProperty";
    private static final String PROPERTY_INTERCEPTOR = "interceptor";
    private static final String GENERICNODEFACTORY = "de.hybris.platform.webservices.model.nodefactory.GenericNodeFactory";
    private static final String GENERICNODEFACTORYCLASS = "GenericNodeFactory.class";
    private static final String STRING2HYBRISENUMVALUE = "de.hybris.platform.webservices.objectgraphtransformer.StringToHybrisEnumValueConverter";
    private static final String HYBRISENUMVALUE2STRING = "de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumValueToStringConverter";
    private static final String STRINGCOLL2HYBRISENUMVALUES = "de.hybris.platform.webservices.objectgraphtransformer.StringCollectionToHybrisEnumValuesConverter";
    private static final String STRINGLIST2HYBRISENUMVALUES = "de.hybris.platform.webservices.objectgraphtransformer.StringListToHybrisEnumValuesConverter";
    private static final String STRINGSET2HYBRISENUMVALUES = "de.hybris.platform.webservices.objectgraphtransformer.StringSetToHybrisEnumValuesConverter";
    private static final String HYBRISENUMCOLL2STRINGVALUES = "de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumCollectionToStringValuesConverter";
    private static final String HYBRISENUMLIST2STRINGVALUES = "de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumListToStringValuesConverter";
    private static final String HYBRISENUMSET2STRINGVALUES = "de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumSetToStringValuesConverter";
    private static final String PK2LONG = "de.hybris.platform.webservices.objectgraphtransformer.PkToLongConverter";
    private static final String PK = "de.hybris.platform.core.PK";
    private final WebservicesConfig wsConfig;
    private final Map<YComposedType, Set<YComposedType>> uniqueInfoMap;


    public SingleDtoWriter(WebservicesConfig wsConfig, DtoConfig cfg, CodeGenerator gen, YExtension ext, Map<YComposedType, Set<YComposedType>> uniqueInfoMap)
    {
        super(wsConfig, cfg, gen, ext);
        this.wsConfig = wsConfig;
        this.uniqueInfoMap = uniqueInfoMap;
        UniqueIdentifierResolver uidResolver = ((AbstractDtoConfig)cfg).getConfigProvider().getUidResover();
        String uid = uidResolver.getUniqueIdentifier(cfg.getType());
        addRequiredImport("de.hybris.platform.webservices.util.objectgraphtransformer.GraphNode");
        addRequiredImport("de.hybris.platform.webservices.model.nodefactory.GenericNodeFactory");
        addRequiredImport(cfg.getModelClassName());
        DtoConfig parent = getDtoConfig().getParentConfig();
        String parentDto = (parent != null) ? parent.getDtoClassName() : "de.hybris.platform.webservices.dto.AbstractItemDTO";
        setClassToExtend(addRequiredImport(parentDto));
        addAnnotation("GraphNode(target = " + cfg.getModelClassSimpleName() + ".class, factory = GenericNodeFactory.class, uidProperties=\"" + uid + "\")");
        addAnnotation("XmlRootElement(name = \"" + cfg.getType().getCode().toLowerCase() + "\")");
        setJavadoc("Generated dto class for type " + cfg.getType().getCode() + " first defined at extension " + ((YExtension)cfg
                        .getType().getNamespace()).getExtensionName());
    }


    public SingleDtoConfig getDtoConfig()
    {
        return (SingleDtoConfig)super.getDtoConfig();
    }


    private void buildUniqueInfoMap(Set<YAttributeDescriptor> attributes)
    {
        Set<YAttributeDescriptor> allAttributes = getInheritedAttributes();
        allAttributes.addAll(attributes);
        for(YAttributeDescriptor ad : allAttributes)
        {
            if(ad.isUniqueModifier() && ad.getType() instanceof YComposedType && !(ad.getType() instanceof de.hybris.bootstrap.typesystem.YRelation))
            {
                buildUniqueInfoMapInternal((YComposedType)ad.getType());
                for(YComposedType keyComposedType : ((YComposedType)ad.getType()).getAllSubtypes())
                {
                    buildUniqueInfoMapInternal(keyComposedType);
                }
            }
        }
    }


    private void buildUniqueInfoMapInternal(YComposedType composedType)
    {
        Set<YComposedType> values = this.uniqueInfoMap.get(composedType);
        if(values == null)
        {
            values = new HashSet<>(Collections.singleton(getDtoConfig().getType()));
            addAllCurrentTypeSubtypesToCollection(values);
            this.uniqueInfoMap.put(composedType, values);
        }
        else
        {
            values.add(getDtoConfig().getType());
            addAllCurrentTypeSubtypesToCollection(values);
        }
    }


    private void addAllCurrentTypeSubtypesToCollection(Collection<YComposedType> values)
    {
        if(!"Link".equals(getDtoConfig().getType().getCode()))
        {
            values.addAll(getDtoConfig().getType().getAllSubtypes());
        }
    }


    protected Collection<MethodWriter> getBeanMethodWriters()
    {
        Collection<MethodWriter> result = new ArrayList<>();
        Set<YAttributeDescriptor> attributes = getSupportedDTOAttributes();
        buildUniqueInfoMap(attributes);
        for(YAttributeDescriptor ad : attributes)
        {
            Set<String> imports = new HashSet<>();
            String returnType = getReturnType(ad, imports);
            for(String type : imports)
            {
                addRequiredImport(type);
            }
            if(ad.isRedeclared())
            {
                if(!(ad.getType() instanceof YCollectionType))
                {
                    MethodWriter methodWriter = createGetMethodWriter(ad, returnType);
                    result.add(methodWriter);
                }
                continue;
            }
            String doc = "/** <i>Generated variable</i> - Variable of <code>" + getDtoConfig().getType().getCode() + "." + ad.getQualifier() + "</code> attribute defined at extension <code> */";
            String variable = "private " + returnType + " _" + ad.getQualifier() + ";";
            addDeclaration(doc + "\n" + doc + "\n");
            MethodWriter getter = createGetMethodWriter(ad, returnType);
            result.add(getter);
            MethodWriter setter = createBeanSetter(ad, returnType);
            result.add(setter);
        }
        return result;
    }


    private Set<YAttributeDescriptor> getSupportedDTOAttributes()
    {
        Set<YAttributeDescriptor> result = new HashSet<>();
        for(YAttributeDescriptor attribute : getDtoConfig().getType().getAttributes())
        {
            boolean generate = (isAttributeAllowed(attribute) && isTypeAllowed(attribute.getType(), attribute));
            if(generate)
            {
                result.add(attribute);
            }
        }
        return result;
    }


    private boolean isAttributeAllowed(YAttributeDescriptor attribute)
    {
        boolean isFiltered = (attribute.isPrivate() || !attribute.isGenerateInModel());
        if(!isFiltered && attribute.isRedeclared())
        {
            isFiltered = (isFiltered || attribute.getType().equals(attribute.getDeclaringAttribute().getType()) || attribute.getType() instanceof de.hybris.bootstrap.typesystem.YEnumType);
        }
        return !isFiltered;
    }


    protected boolean isTypeAllowed(YType type, YAttributeDescriptor attribute)
    {
        boolean isAllowed = true;
        if(type instanceof YComposedType)
        {
            if(type instanceof de.hybris.bootstrap.typesystem.YEnumType)
            {
                return true;
            }
            return ((YComposedType)type).isGenerateModel();
        }
        if(type instanceof YCollectionType)
        {
            return isTypeAllowed(((YCollectionType)type).getElementType(), attribute);
        }
        if(type instanceof YMapType)
        {
            YMapType mapType = (YMapType)type;
            if(!attribute.isLocalized())
            {
                return false;
            }
            if(mapType.getArgumentType() instanceof YMapType || mapType.getReturnType() instanceof YMapType)
            {
                return false;
            }
            boolean isKeyAlowed = isTypeAllowed(mapType.getArgumentType(), attribute);
            if(!isKeyAlowed)
            {
                return false;
            }
            boolean isValueAlowed = isTypeAllowed(mapType.getReturnType(), attribute);
            if(!isValueAlowed)
            {
                return false;
            }
        }
        if(type instanceof YAtomicType)
        {
            String clazz = ((YAtomicType)type).getCode();
            isAllowed = !clazz.startsWith("de.hybris.platform.util");
        }
        return isAllowed;
    }


    private MethodWriter createGetMethodWriter(YAttributeDescriptor attribute, String returnType)
    {
        String propName = String.valueOf(attribute.getQualifier().charAt(0)).toUpperCase() + String.valueOf(attribute.getQualifier().charAt(0)).toUpperCase();
        String prefix = "boolean".equals(returnType) ? "is" : "get";
        String methodName = prefix + prefix;
        MethodWriter result = new MethodWriter(Visibility.PUBLIC, returnType, methodName);
        if(attribute.isRedeclared())
        {
            result.addAnnotation("Override");
        }
        if(attribute.getType() instanceof de.hybris.bootstrap.typesystem.YEnumType)
        {
            addRequiredImport("de.hybris.platform.webservices.util.objectgraphtransformer.GraphProperty");
            addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.StringToHybrisEnumValueConverter");
            result.addAnnotation("GraphProperty(interceptor = StringToHybrisEnumValueConverter.class)");
        }
        if((attribute.isUniqueModifier() && !(attribute.getType() instanceof YComposedType)) || "pk"
                        .equals(attribute.getQualifier()))
        {
            addRequiredImport("javax.xml.bind.annotation.XmlAttribute");
            result.addAnnotation("XmlAttribute");
        }
        if(attribute.getType() instanceof YCollectionType)
        {
            String wrapperName = attribute.getQualifier();
            String elementName = "value";
            YCollectionType colType = (YCollectionType)attribute.getType();
            if(colType.getElementType() instanceof YComposedType)
            {
                CollectionDtoConfig collectionDtoConfig = this.wsConfig.getCollectionDtoConfig((YComposedType)colType.getElementType());
                elementName = String.valueOf(collectionDtoConfig.getSingular().charAt(0)).toLowerCase() + String.valueOf(collectionDtoConfig.getSingular().charAt(0)).toLowerCase();
            }
            if(colType.getElementType() instanceof de.hybris.bootstrap.typesystem.YEnumType)
            {
                String converterClass;
                addRequiredImport("de.hybris.platform.webservices.util.objectgraphtransformer.GraphProperty");
                if(colType.getTypeOfCollection() == YCollectionType.TypeOfCollection.LIST)
                {
                    addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.StringListToHybrisEnumValuesConverter");
                    converterClass = "StringListToHybrisEnumValuesConverter";
                }
                else if(colType.getTypeOfCollection() == YCollectionType.TypeOfCollection.SET)
                {
                    addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.StringSetToHybrisEnumValuesConverter");
                    converterClass = "StringSetToHybrisEnumValuesConverter";
                }
                else
                {
                    addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.StringCollectionToHybrisEnumValuesConverter");
                    converterClass = "StringCollectionToHybrisEnumValuesConverter";
                }
                result.addAnnotation("GraphProperty(interceptor = " + converterClass + ".class)");
            }
            addRequiredImport("javax.xml.bind.annotation.XmlElement");
            addRequiredImport("javax.xml.bind.annotation.XmlElementWrapper");
            result.addAnnotation("XmlElementWrapper(name = \"" + wrapperName + "\")");
            result.addAnnotation("XmlElement(name = \"" + elementName + "\")");
        }
        if(attribute.isRedeclared())
        {
            result.setContentPlain("return (" + returnType + ") super." + methodName + "();");
        }
        else
        {
            result.setContentPlain("return this._" + attribute.getQualifier() + ";");
        }
        return result;
    }


    private MethodWriter createBeanSetter(YAttributeDescriptor attribute, String paramType)
    {
        String methodName = "set" + String.valueOf(attribute.getQualifier().charAt(0)).toUpperCase() + attribute.getQualifier().substring(1);
        MethodWriter result = new MethodWriter(Visibility.PUBLIC, "void", methodName);
        if(attribute.isRedeclared())
        {
            result.addAnnotation("Override");
        }
        if(attribute.getType() instanceof de.hybris.bootstrap.typesystem.YEnumType)
        {
            addRequiredImport("de.hybris.platform.webservices.util.objectgraphtransformer.GraphProperty");
            addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumValueToStringConverter");
            result.addAnnotation("GraphProperty(interceptor = HybrisEnumValueToStringConverter.class)");
        }
        if(attribute.getType() instanceof YCollectionType)
        {
            YCollectionType colType = (YCollectionType)attribute.getType();
            if(colType.getElementType() instanceof de.hybris.bootstrap.typesystem.YEnumType)
            {
                String converterClass;
                addRequiredImport("de.hybris.platform.webservices.util.objectgraphtransformer.GraphProperty");
                if(colType.getTypeOfCollection() == YCollectionType.TypeOfCollection.LIST)
                {
                    addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumListToStringValuesConverter");
                    converterClass = "HybrisEnumListToStringValuesConverter";
                }
                else if(colType.getTypeOfCollection() == YCollectionType.TypeOfCollection.SET)
                {
                    addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumSetToStringValuesConverter");
                    converterClass = "HybrisEnumSetToStringValuesConverter";
                }
                else
                {
                    addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.HybrisEnumCollectionToStringValuesConverter");
                    converterClass = "HybrisEnumCollectionToStringValuesConverter";
                }
                result.addAnnotation("GraphProperty(interceptor = " + converterClass + ".class)");
            }
        }
        if("de.hybris.platform.core.PK".equals(attribute.getTypeCode()))
        {
            addRequiredImport("de.hybris.platform.webservices.util.objectgraphtransformer.GraphProperty");
            addRequiredImport("de.hybris.platform.webservices.objectgraphtransformer.PkToLongConverter");
            result.addAnnotation("GraphProperty(interceptor = PkToLongConverter.class)");
        }
        if(!attribute.isRedeclared())
        {
            String content = "this.modifiedPropsSet.add(\"" + attribute.getQualifier() + "\");\nthis._" + attribute.getQualifier() + " = value;";
            result.setContentPlain(content);
        }
        result.addParameter(paramType, "value");
        return result;
    }


    private String getReturnType(YAttributeDescriptor attribute, Set<String> involvedTypes)
    {
        StringBuilder result = new StringBuilder();
        if("de.hybris.platform.core.PK".equals(attribute.getTypeCode()))
        {
            result.append(Long.class.getName());
            involvedTypes.add(Long.class.getName());
            return result.toString();
        }
        YType _type = attribute.getType();
        if(attribute.isLocalized())
        {
            _type = ((YMapType)attribute.getType()).getReturnType();
        }
        collectReturnTypeInformation(_type, result, involvedTypes, attribute.isPrimitive());
        return result.toString();
    }


    private void collectReturnTypeInformation(YType type, StringBuilder returnType, Set<String> involvedTypes, boolean isPrimitive)
    {
        if(type instanceof de.hybris.bootstrap.typesystem.YEnumType)
        {
            returnType.append(String.class.getName());
            involvedTypes.add(String.class.getName());
            return;
        }
        if(type instanceof YAtomicType)
        {
            YAtomicType _type = (YAtomicType)type;
            if(isPrimitive)
            {
                returnType.append(_type.getPrimitiveJavaClass().getName());
            }
            else
            {
                returnType.append(_type.getJavaClassName());
                involvedTypes.add(_type.getJavaClassName());
            }
            return;
        }
        if(type instanceof YComposedType)
        {
            String name = this.wsConfig.getSingleDtoConfig((YComposedType)type).getDtoClassName();
            returnType.append(name);
            involvedTypes.add(name);
        }
        if(type instanceof YCollectionType)
        {
            String colType;
            YCollectionType _type = (YCollectionType)type;
            switch(null.$SwitchMap$de$hybris$bootstrap$typesystem$YCollectionType$TypeOfCollection[_type.getTypeOfCollection().ordinal()])
            {
                case 1:
                    colType = Set.class.getName();
                    break;
                case 2:
                    colType = List.class.getName();
                    break;
                case 3:
                    colType = Collection.class.getName();
                    break;
                default:
                    colType = Collection.class.getName();
                    break;
            }
            returnType.append(colType + "<");
            collectReturnTypeInformation(_type.getElementType(), returnType, involvedTypes, isPrimitive);
            returnType.append(">");
            involvedTypes.add(colType);
            return;
        }
        if(type instanceof YMapType)
        {
            YMapType _type = (YMapType)type;
            returnType.append("java.util.Map <");
            collectReturnTypeInformation(_type.getArgumentType(), returnType, involvedTypes, isPrimitive);
            returnType.append(",");
            collectReturnTypeInformation(_type.getReturnType(), returnType, involvedTypes, isPrimitive);
            returnType.append(">");
            involvedTypes.add("java.util.Map");
        }
    }


    private Set<YAttributeDescriptor> getInheritedAttributes()
    {
        YComposedType type = getDtoConfig().getType().getSuperType();
        Set<YAttributeDescriptor> result = new LinkedHashSet<>();
        List<Set<YAttributeDescriptor>> subResult = new LinkedList<>();
        while(type != null)
        {
            Set<YAttributeDescriptor> mySet = new LinkedHashSet<>(getGenerator().getTypeSystem().getAttributes(type
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
            type = type.getSuperType();
        }
        for(ReverseListIterator it = new ReverseListIterator(subResult); it.hasNext(); )
        {
            Set<YAttributeDescriptor> descs = (Set<YAttributeDescriptor>)it.next();
            result.addAll(descs);
        }
        return result;
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
