package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.util.LocaleHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class YTypeSystem
{
    private static final Logger LOG = Logger.getLogger(YTypeSystem.class.getName());
    private final Map<String, YExtension> extensionsMap = new LinkedHashMap<>();
    private final boolean buildMode;
    private boolean finalized;
    private YNamespace mergedNamespace;
    private final Map<String, Class> resolvedClassMap;


    public YTypeSystem(boolean buildMode)
    {
        this.buildMode = buildMode;
        this.resolvedClassMap = (Map<String, Class>)new YNamespace.LinkedCaseInsensitiveMap();
    }


    public YExtension getExtension(String extName)
    {
        return this.extensionsMap.get((extName != null) ? extName.toLowerCase(LocaleHelper.getPersistenceLocale()) : null);
    }


    public boolean isFinalized()
    {
        return this.finalized;
    }


    public void finalizeTypeSystem()
    {
        if(isFinalized())
        {
            throw new IllegalStateException("type system " + this + " has already been finalzed");
        }
        createRelationAttributes();
        if(!isBuildMode())
        {
            createInheritedAttributes();
            deployAttributes();
            deployIndexes();
        }
        mergeNamespaces();
        this.finalized = true;
    }


    protected void mergeNamespaces()
    {
        this.mergedNamespace = (YNamespace)new YExtension(this, "<merged>", null);
        for(YExtension ext : getExtensions())
        {
            this.mergedNamespace.mergeNamespace((YNamespace)ext);
        }
    }


    protected void createRelationAttributes()
    {
        for(YRelation rel : getRelationTypes())
        {
            YRelationEnd _src = rel.getSourceEnd();
            YRelationEnd _tgt = rel.getTargetEnd();
            if(_src.mappedAttributeDoesntExist())
            {
                createRelationEndAttribute(rel, _src, _tgt);
            }
            else
            {
                _src.getMappedAttribute().setMetaTypeCode(_src.getMetaType().getCode());
            }
            if(_tgt.mappedAttributeDoesntExist())
            {
                createRelationEndAttribute(rel, _tgt, _src);
                continue;
            }
            throw new UnsupportedOperationException("Attribute " + _tgt.getQualifier() + " from " + rel.getCode() + " relation is already declared in " + _tgt
                            .getTypeCode());
        }
    }


    protected YAttributeDescriptor createRelationEndAttribute(YRelation rel, YRelationEnd end, YRelationEnd _other)
    {
        String role = end.getRole();
        String elementType = end.getTypeCode();
        YCollectionType.TypeOfCollection typeOfCollection = end.getCollectionType();
        String enclosingType = _other.getTypeCode();
        int modifiers = end.getModifiers();
        boolean uniqueModifier = end.isUniqueModifier();
        String metaType = end.getMetaType().getCode();
        String attrType = null;
        if(end.getCardinality() == YRelationEnd.Cardinality.ONE)
        {
            attrType = elementType;
            if(end.isOrdered() || end.getOppositeEnd().isOrdered())
            {
                YAttributeDescriptor posAttr = new YAttributeDescriptor(rel.getNamespace(), enclosingType, role + "POS", "java.lang.Integer");
                posAttr.setPersistenceType(YAttributeDescriptor.PersistenceType.PROPERTY);
                posAttr.setLoaderInfo(end.getLoaderInfo());
                posAttr.setModifiers(155);
                rel.getNamespace().registerTypeSystemElement((YNameSpaceElement)posAttr);
            }
            if(rel.isLocalized())
            {
                YAttributeDescriptor locAttr = new YAttributeDescriptor(rel.getNamespace(), enclosingType, role + "LOC", "java.lang.Integer");
                locAttr.setPersistenceType(YAttributeDescriptor.PersistenceType.PROPERTY);
                locAttr.setLoaderInfo(end.getLoaderInfo());
                locAttr.setModifiers(155);
                rel.getNamespace().registerTypeSystemElement((YNameSpaceElement)locAttr);
            }
        }
        else if(end.isNavigable())
        {
            YCollectionType relColl = getOrCreateCollectionType(rel.getNamespace(), rel.getCode() + rel.getCode() + "Coll", elementType, typeOfCollection);
            if(rel.isLocalized())
            {
                YMapType locMapType = getOrCreateMapType(rel.getNamespace(), rel.getCode() + rel.getCode() + "LocMap", "Language", relColl
                                .getCode());
                locMapType.setAutocreate(false);
                locMapType.setGenerate(false);
                attrType = locMapType.getCode();
            }
            else
            {
                attrType = relColl.getCode();
            }
        }
        YAttributeDescriptor mapped = null;
        if(end.isNavigable())
        {
            mapped = new YAttributeDescriptor(rel.getNamespace(), enclosingType, role, attrType);
            mapped.connectToRelationEnd(end);
            mapped.setLoaderInfo(end.getLoaderInfo());
            mapped.setModifiers(modifiers);
            mapped.setUniqueModifier(uniqueModifier);
            mapped.setModelData(end.getModelData());
            mapped.setMetaTypeCode(metaType);
            mapped.setDescription(end.getDescription());
            rel.getNamespace().registerTypeSystemElement((YNameSpaceElement)mapped);
        }
        if(!rel.isAbstract())
        {
            YAttributeDescriptor linkAd = rel.getTypeSystem().getAttribute(rel.getCode(), end.isSource() ? "source" : "target");
            if(linkAd == null)
            {
                String qualifier = end.isSource() ? "source" : "target";
                YAttributeDescriptor superAttribute = getAttribute("Link", qualifier);
                if(superAttribute == null)
                {
                    throw new IllegalStateException("missing attribute Link." + qualifier);
                }
                linkAd = new YAttributeDescriptor(rel.getCode(), superAttribute);
                linkAd.redeclare(end.getTypeCode());
                rel.getNamespace().registerTypeSystemElement((YNameSpaceElement)linkAd);
            }
            else
            {
                linkAd.redeclare(end.getTypeCode(), linkAd.getModifiers());
            }
        }
        return mapped;
    }


    protected YCollectionType getOrCreateCollectionType(YNamespace namespace, String code, String elementType, YCollectionType.TypeOfCollection toc)
    {
        YType tzpe = getType(code);
        if(tzpe == null)
        {
            YCollectionType composedType = new YCollectionType(namespace, code, elementType, toc);
            composedType.setAutocreate(false);
            composedType.setGenerate(false);
            namespace.registerTypeSystemElement((YNameSpaceElement)composedType);
            return composedType;
        }
        if(!(tzpe instanceof YCollectionType) ||
                        !((YCollectionType)tzpe).getElementTypeCode().equalsIgnoreCase(elementType))
        {
            throw new IllegalStateException("cannot create collection type " + code + "(" + elementType + ") and existing type " + tzpe + " is not compatible");
        }
        return (YCollectionType)tzpe;
    }


    protected YMapType getOrCreateMapType(YNamespace namespace, String code, String argumentType, String elementType)
    {
        YType type = getType(code);
        if(type == null)
        {
            YMapType compesedType = new YMapType(namespace, code, argumentType, elementType);
            namespace.registerTypeSystemElement((YNameSpaceElement)compesedType);
            return compesedType;
        }
        if(!(type instanceof YMapType) || !((YMapType)type).getArgumentTypeCode().equalsIgnoreCase(argumentType) ||
                        !((YMapType)type).getReturnTypeCode().equalsIgnoreCase(elementType))
        {
            throw new IllegalStateException("cannot create map type " + code + "{" + argumentType + "->" + elementType + ") and existing type " + type + " is not compatible");
        }
        return (YMapType)type;
    }


    protected String adjustColumnName(String requested, Set<String> currentColumnNames, int maxLength)
    {
        return null;
    }


    protected void deployAttributes()
    {
        for(YAttributeDescriptor ad : getAttributes())
        {
            if(ad.isDeclared() && ad.isPersistable())
            {
                deployAttribute(ad);
            }
        }
    }


    protected void deployAttribute(YAttributeDescriptor attDesc)
    {
        YDeployment depl = attDesc.getEnclosingType().getDeployment();
        YAttributeDeployment aDepl = depl.getAttributeDeployment(attDesc.getPersistenceQualifier());
        if(aDepl == null)
        {
            YAttributeDeployment newOne = new YAttributeDeployment(attDesc);
            attDesc.getNamespace().registerTypeSystemElement((YNameSpaceElement)newOne);
            depl.resetCaches();
        }
    }


    protected void deployIndexes()
    {
        for(YIndex idx : getIndexes())
        {
            deployIndex(idx);
        }
    }


    protected void deployIndex(YIndex idx)
    {
        YDeployment depl = idx.getEnclosingType().getDeployment();
        YIndexDeployment iDepl = depl.getIndexDeployment(idx.getName().toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(iDepl == null)
        {
            YIndexDeployment newOne = new YIndexDeployment(idx);
            idx.getNamespace().registerTypeSystemElement((YNameSpaceElement)newOne);
            depl.resetCaches();
        }
    }


    protected void createInheritedAttributes()
    {
        for(YAttributeDescriptor ad : getAttributes())
        {
            if(ad.isInherited())
            {
                continue;
            }
            for(YComposedType subtype : getSubtypes(ad.getEnclosingTypeCode()))
            {
                createInheritedAttributes(subtype, ad);
            }
        }
    }


    protected void createInheritedAttributes(YComposedType composedType, YAttributeDescriptor inheritFrom)
    {
        if(getAttribute(composedType.getCode(), inheritFrom.getQualifier()) == null)
        {
            YAttributeDescriptor inherited = new YAttributeDescriptor(composedType.getCode(), inheritFrom);
            inheritFrom.getNamespace().registerTypeSystemElement((YNameSpaceElement)inherited);
            for(YComposedType subtype : getSubtypes(composedType.getCode()))
            {
                createInheritedAttributes(subtype, inherited);
            }
        }
    }


    public void validate()
    {
        if(!isBuildMode())
        {
            for(YDeployment t : getDeployments())
            {
                t.validate();
            }
        }
        if(!isBuildMode())
        {
            for(YDeploymentElement t : getFinders())
            {
                t.validate();
            }
        }
        if(!isBuildMode())
        {
            for(YAttributeDeployment t : getAttributeDeployments())
            {
                t.validate();
            }
        }
        for(YIndex t : getIndexes())
        {
            t.validate();
        }
        for(YType t : getTypes())
        {
            t.validate();
        }
        for(YAttributeDescriptor t : getAttributes())
        {
            t.validate();
        }
        for(YEnumValue t : getEnumValues())
        {
            t.validate();
        }
    }


    public Set<YExtension> getExtensions()
    {
        return new LinkedHashSet<>(this.extensionsMap.values());
    }


    public Set<? extends YType> getTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes();
        }
        Set<YType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnTypes());
        }
        return ret;
    }


    public Set<? extends YType> getTypes(Set<String> codes)
    {
        if(codes == null || codes.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<YType> ret = new LinkedHashSet<>();
        for(String code : codes)
        {
            ret.add(getType(code));
        }
        return ret;
    }


    public <T extends YType> T getType(String code)
    {
        if(isFinalized())
        {
            return (T)this.mergedNamespace.getOwnType(code);
        }
        YType type = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            type = ((YExtension)entry.getValue()).getOwnType(code);
            if(type != null)
            {
                break;
            }
        }
        return (T)type;
    }


    public YDBTypeMapping getDBTypeMappings(String databaseName)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnDBTypeMappings(databaseName);
        }
        YExtension coreExt = getExtension("core");
        YDBTypeMapping ret = (coreExt != null) ? coreExt.getOwnDBTypeMappings(databaseName) : null;
        if(ret == null)
        {
            for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
            {
                if(((YExtension)entry.getValue()).getExtensionName().equalsIgnoreCase("core"))
                {
                    continue;
                }
                ret = ((YExtension)entry.getValue()).getOwnDBTypeMappings(databaseName);
                if(ret != null)
                {
                    break;
                }
            }
        }
        return ret;
    }


    public Map<String, YDBTypeMapping> getDBTypeMappings()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getAllOwnDBTypeMappings();
        }
        YExtension coreExt = getExtension("core");
        Map<String, YDBTypeMapping> ret = (coreExt != null) ? coreExt.getAllOwnDBTypeMappings() : null;
        if(ret == null)
        {
            for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
            {
                if(((YExtension)entry.getValue()).getExtensionName().equalsIgnoreCase("core"))
                {
                    continue;
                }
                ret = ((YExtension)entry.getValue()).getAllOwnDBTypeMappings();
                if(ret != null)
                {
                    break;
                }
            }
        }
        return ret;
    }


    public Set<YAttributeDescriptor> getAttributes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnAttributes();
        }
        Set<YAttributeDescriptor> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnAttributes());
        }
        return ret;
    }


    public Set<YAttributeDescriptor> getAttributes(String enclosingTypeCode)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnAttributes(enclosingTypeCode);
        }
        Set<YAttributeDescriptor> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnAttributes(enclosingTypeCode));
        }
        return ret;
    }


    public YAttributeDescriptor getAttribute(String enclosingTypeCode, String qualifier)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnAttribute(enclosingTypeCode, qualifier);
        }
        YAttributeDescriptor attDesc = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            attDesc = ((YExtension)entry.getValue()).getOwnAttribute(enclosingTypeCode, qualifier);
            if(attDesc != null)
            {
                break;
            }
        }
        return attDesc;
    }


    public YRelationEnd getAttributeRelationEnd(String enclosingTypeCode, String qualifier)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnAttributeRelationEnd(enclosingTypeCode, qualifier);
        }
        YRelationEnd end = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            end = ((YExtension)entry.getValue()).getOwnAttributeRelationEnd(enclosingTypeCode, qualifier);
            if(end != null)
            {
                break;
            }
        }
        return end;
    }


    public Set<? extends YType> getSubtypes(String code)
    {
        if(isFinalized())
        {
            return getTypes(this.mergedNamespace.getOwnSubtypeCodes(code));
        }
        Set<YType> ret = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<String> extSt = ((YExtension)entry.getValue()).getOwnSubtypeCodes(code);
            if(!extSt.isEmpty())
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>();
                }
                for(String stCode : extSt)
                {
                    ret.add(getType(stCode));
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<YIndex> getIndexes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnIndexes();
        }
        Set<YIndex> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnIndexes());
        }
        return ret;
    }


    public Set<YIndex> getIndexes(String composedTypeCode)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnIndexes(composedTypeCode);
        }
        Set<YIndex> ret = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YIndex> extSt = ((YExtension)entry.getValue()).getOwnIndexes(composedTypeCode);
            if(!extSt.isEmpty())
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>();
                }
                ret.addAll(extSt);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public Set<YEnumValue> getEnumValues()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnEnumValues();
        }
        Set<YEnumValue> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnEnumValues());
        }
        return ret;
    }


    public List<YEnumValue> getEnumValues(String enumTypeCode)
    {
        if(isFinalized())
        {
            Set<YEnumValue> values = this.mergedNamespace.getOwnEnumValues(enumTypeCode);
            if(values.isEmpty())
            {
                return Collections.EMPTY_LIST;
            }
            List<YEnumValue> list = new ArrayList<>(values);
            Collections.sort(list);
            return list;
        }
        List<YEnumValue> ret = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YEnumValue> extSt = ((YExtension)entry.getValue()).getOwnEnumValues(enumTypeCode);
            if(!extSt.isEmpty())
            {
                if(ret == null)
                {
                    ret = new ArrayList<>();
                }
                ret.addAll(extSt);
            }
        }
        if(ret != null)
        {
            Collections.sort(ret);
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public Set<YDeployment> getDeployments()
    {
        return getDeployments(false);
    }


    public Set<YDeployment> getDeployments(boolean filterAbstract)
    {
        if(isFinalized())
        {
            if(filterAbstract)
            {
                Set<YDeployment> set = new LinkedHashSet<>();
                for(YDeployment depl : this.mergedNamespace.getOwnDeployments())
                {
                    if(!depl.isAbstract())
                    {
                        set.add(depl);
                    }
                }
                return set;
            }
            return this.mergedNamespace.getOwnDeployments();
        }
        Set<YDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            if(filterAbstract)
            {
                for(YDeployment depl : ((YExtension)entry.getValue()).getOwnDeployments())
                {
                    if(!depl.isAbstract())
                    {
                        ret.add(depl);
                    }
                }
                continue;
            }
            ret.addAll(((YExtension)entry.getValue()).getOwnDeployments());
        }
        return ret;
    }


    public Set<YFinder> getFinders()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnFinders();
        }
        Set<YFinder> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnFinders());
        }
        return ret;
    }


    public Set<YAttributeDeployment> getAttributeDeployments()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnAttributeDeployments();
        }
        Set<YAttributeDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnAttributeDeployments());
        }
        return ret;
    }


    public Set<YComposedType> getDeploymentTypes(YDeployment deployment)
    {
        String deploymentName = deployment.getFullName();
        Set<YComposedType> rootTypes = new LinkedHashSet<>();
        if(isFinalized())
        {
            rootTypes.addAll(this.mergedNamespace.getOwnTypesByDeployment(deploymentName));
        }
        else
        {
            for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
            {
                rootTypes.addAll(((YExtension)entry.getValue()).getOwnTypesByDeployment(deploymentName));
            }
        }
        if(!rootTypes.isEmpty())
        {
            Set<YComposedType> ret = new LinkedHashSet<>(rootTypes.size() * 2);
            label46:
            for(YComposedType rootType : rootTypes)
            {
                if(ret.contains(rootType))
                {
                    continue;
                }
                ret.add(rootType);
                Set<YComposedType> current = Collections.singleton(rootType);
                while(true)
                {
                    Set<YComposedType> nextLevel = null;
                    for(YComposedType t : current)
                    {
                        for(YComposedType subType : t.getSubtypes())
                        {
                            if(subType.getDeploymentName() == null || deploymentName.equalsIgnoreCase(subType
                                            .getDeploymentName()))
                            {
                                if(nextLevel == null)
                                {
                                    nextLevel = new LinkedHashSet<>();
                                }
                                ret.add(subType);
                                nextLevel.add(subType);
                            }
                        }
                    }
                    current = nextLevel;
                    if(current != null)
                    {
                        if(current.isEmpty())
                        {
                            continue label46;
                        }
                        continue;
                    }
                    continue label46;
                }
            }
            for(YComposedType rootType : rootTypes)
            {
                ret.addAll(rootType.getAllSuperTypes());
            }
            return ret;
        }
        return Collections.EMPTY_SET;
    }


    public YDeployment getDeployment(String name)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnDeployment(name);
        }
        YDeployment depl = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            depl = ((YExtension)entry.getValue()).getOwnDeployment(name);
            if(depl != null)
            {
                break;
            }
        }
        return depl;
    }


    public YDeployment getDeployment(int typeCode)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnDeployment(typeCode);
        }
        YDeployment depl = null;
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            depl = ((YExtension)entry.getValue()).getOwnDeployment(typeCode);
            if(depl != null)
            {
                break;
            }
        }
        return depl;
    }


    public Set<YAttributeDeployment> getAttributeDeployments(String deploymentName)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnAttributeDeployments(deploymentName);
        }
        Set<YAttributeDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnAttributeDeployments(deploymentName));
        }
        return ret;
    }


    public Set<YIndexDeployment> getIndexDeployments()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnIndexDeployments();
        }
        Set<YIndexDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnIndexDeployments());
        }
        return ret;
    }


    public Set<YIndexDeployment> getIndexDeployments(String deploymentName)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnIndexDeployments(deploymentName);
        }
        Set<YIndexDeployment> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnIndexDeployments(deploymentName));
        }
        return ret;
    }


    public Set<YFinder> getFinders(String deploymentName)
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnFinders(deploymentName);
        }
        Set<YFinder> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            ret.addAll(((YExtension)entry.getValue()).getOwnFinders(deploymentName));
        }
        return ret;
    }


    public void addExtension(YExtension currentExtension)
    {
        if(isFinalized())
        {
            throw new IllegalStateException("type system is finalized - cannot add extension");
        }
        this.extensionsMap.put(currentExtension.getExtensionName().toLowerCase(LocaleHelper.getPersistenceLocale()), currentExtension);
    }


    public Set<YAtomicType> getAtomicTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes(YAtomicType.class);
        }
        Set<YAtomicType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YAtomicType> extt = ((YExtension)entry.getValue()).getOwnTypes(YAtomicType.class);
            ret.addAll(extt);
        }
        return ret;
    }


    public Set<YComposedType> getComposedTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes(YComposedType.class, new Class[] {YRelation.class, YEnumType.class});
        }
        Set<YComposedType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YComposedType> extt = ((YExtension)entry.getValue()).getOwnTypes(YComposedType.class, new Class[] {YRelation.class, YEnumType.class});
            ret.addAll(extt);
        }
        return ret;
    }


    public Set<YMapType> getMapTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes(YMapType.class);
        }
        Set<YMapType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YMapType> extt = ((YExtension)entry.getValue()).getOwnTypes(YMapType.class);
            ret.addAll(extt);
        }
        return ret;
    }


    public Set<YCollectionType> getCollectionTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes(YCollectionType.class);
        }
        Set<YCollectionType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YCollectionType> extt = ((YExtension)entry.getValue()).getOwnTypes(YCollectionType.class);
            ret.addAll(extt);
        }
        return ret;
    }


    public Set<YEnumType> getEnumTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes(YEnumType.class);
        }
        Set<YEnumType> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YEnumType> extt = ((YExtension)entry.getValue()).getOwnTypes(YEnumType.class);
            ret.addAll(extt);
        }
        return ret;
    }


    public Set<YRelation> getRelationTypes()
    {
        if(isFinalized())
        {
            return this.mergedNamespace.getOwnTypes(YRelation.class);
        }
        Set<YRelation> ret = new LinkedHashSet<>();
        for(Map.Entry<String, YExtension> entry : this.extensionsMap.entrySet())
        {
            Set<YRelation> extt = ((YExtension)entry.getValue()).getOwnTypes(YRelation.class);
            ret.addAll(extt);
        }
        return ret;
    }


    public boolean isBuildMode()
    {
        return this.buildMode;
    }


    protected Class resolveClass(Object resolveFor, String className)
    {
        Class<?> ret = this.resolvedClassMap.get(className);
        if(ret == null && !this.resolvedClassMap.containsKey(className))
        {
            try
            {
                ret = Class.forName(className, false, getClass().getClassLoader());
            }
            catch(ClassNotFoundException e)
            {
                if(isBuildMode())
                {
                    LOG.debug("class '" + className + "' not available in build mode - ignored");
                }
                else
                {
                    throw new IllegalStateException("invalid typesystem element " + resolveFor + " due to missing class '" + className + "'");
                }
            }
            this.resolvedClassMap.put(className, ret);
        }
        return ret;
    }
}
