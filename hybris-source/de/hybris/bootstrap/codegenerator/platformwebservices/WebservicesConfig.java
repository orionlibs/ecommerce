package de.hybris.bootstrap.codegenerator.platformwebservices;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.model.ModelNameUtils;
import de.hybris.bootstrap.codegenerator.platformwebservices.dto.CollectionDtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.dto.SingleDtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.CollectionResourceConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.SingleResourceConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.SubResourceResolver;
import de.hybris.bootstrap.codegenerator.platformwebservices.resource.UniqueIdentifierResolver;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated(since = "1818", forRemoval = true)
public class WebservicesConfig
{
    private static final Boolean TRUE = Boolean.TRUE;
    private static final Boolean FALSE = Boolean.FALSE;
    private Map<String, SingleResourceConfig> singleResourcesMap = null;
    private Map<String, CollectionResourceConfig> collectionResourcesMap = null;
    private Map<String, DtoConfig> singleDtoCfg = null;
    private Map<String, DtoConfig> collectionDtoCfg = null;
    private SubResourceResolver subResolver = null;
    private UniqueIdentifierResolver uidResolver = null;
    private CodeGenerator codeGen = null;


    public WebservicesConfig(CodeGenerator codeGen, SubResourceResolver subResResolver, UniqueIdentifierResolver uidResolver)
    {
        this.singleResourcesMap = new HashMap<>();
        this.collectionResourcesMap = new HashMap<>();
        this.singleDtoCfg = new HashMap<>();
        this.collectionDtoCfg = new HashMap<>();
        this.codeGen = codeGen;
        this.subResolver = subResResolver;
        this.uidResolver = uidResolver;
    }


    public SubResourceResolver getSubresourceResolver()
    {
        return this.subResolver;
    }


    public UniqueIdentifierResolver getUidResover()
    {
        return this.uidResolver;
    }


    public ResourceConfig getResourceConfig(YType yType)
    {
        SingleResourceConfig singleResourceConfig;
        ResourceConfig result = null;
        if(yType instanceof YCollectionType)
        {
            YComposedType type = (YComposedType)((YCollectionType)yType).getElementType();
            CollectionResourceConfig collectionResourceConfig = getCollectionResourceConfig(type);
        }
        if(yType instanceof YComposedType)
        {
            YComposedType type = (YComposedType)yType;
            singleResourceConfig = getSingleResourceConfig(type);
        }
        return (ResourceConfig)singleResourceConfig;
    }


    public SingleResourceConfig getSingleResourceConfig(YComposedType type)
    {
        SingleResourceConfig result = null;
        YComposedType adjustedType = getAdjustedType(type);
        result = this.singleResourcesMap.get(adjustedType.getCode());
        if(result == null)
        {
            String extPackage = this.codeGen.getExtensionPackage((YExtension)adjustedType.getNamespace());
            String resourcePackage = createPackage(adjustedType, extPackage, "resource");
            SingleDtoConfig dtoConfig = getSingleDtoConfig(adjustedType);
            result = new SingleResourceConfig((DtoConfig)dtoConfig, resourcePackage, this);
            try
            {
                SingleResourceConfigEnum customConfig = SingleResourceConfigEnum.valueOf(adjustedType
                                .getCode().toUpperCase());
                if(customConfig.deleteNeeded != null)
                {
                    result.setDeleteSupport(customConfig.deleteNeeded.booleanValue());
                }
                if(customConfig.getNeeded != null)
                {
                    result.setGetSupport(customConfig.getNeeded.booleanValue());
                }
                if(customConfig.postNeeded != null)
                {
                    result.setPostSupport(customConfig.postNeeded.booleanValue());
                }
                if(customConfig.putNeeded != null)
                {
                    result.setPutSupport(customConfig.putNeeded.booleanValue());
                }
            }
            catch(Exception exception)
            {
            }
            this.singleResourcesMap.put(adjustedType.getCode(), result);
        }
        return result;
    }


    public CollectionResourceConfig getCollectionResourceConfig(YComposedType type)
    {
        CollectionResourceConfig result = null;
        YComposedType adjustedType = getAdjustedType(type);
        result = this.collectionResourcesMap.get(adjustedType.getCode());
        if(result == null)
        {
            String extPackage = this.codeGen.getExtensionPackage((YExtension)adjustedType.getNamespace());
            String resourcepackage = createPackage(adjustedType, extPackage, "resource");
            CollectionDtoConfig dtoConfig = getCollectionDtoConfig(adjustedType);
            result = new CollectionResourceConfig(dtoConfig, resourcepackage, this);
            try
            {
                CollectionResourceConfigEnum customConfig = CollectionResourceConfigEnum.valueOf(adjustedType.getCode()
                                .toUpperCase());
                if(customConfig.deleteNeeded != null)
                {
                    result.setDeleteSupport(customConfig.deleteNeeded.booleanValue());
                }
                if(customConfig.getNeeded != null)
                {
                    result.setGetSupport(customConfig.getNeeded.booleanValue());
                }
                if(customConfig.postNeeded != null)
                {
                    result.setPostSupport(customConfig.postNeeded.booleanValue());
                }
                if(customConfig.putNeeded != null)
                {
                    result.setPutSupport(customConfig.putNeeded.booleanValue());
                }
            }
            catch(Exception exception)
            {
            }
            this.collectionResourcesMap.put(adjustedType.getCode(), result);
        }
        return result;
    }


    public SingleDtoConfig getSingleDtoConfig(YComposedType type)
    {
        YComposedType adjustedType = getAdjustedType(type);
        SingleDtoConfig result = (SingleDtoConfig)this.singleDtoCfg.get(adjustedType.getCode());
        if(result == null)
        {
            String extPackage = this.codeGen.getExtensionPackage((YExtension)adjustedType.getNamespace());
            String plural = getPluralNoun(adjustedType.getCode());
            String dtoPackage = createPackage(adjustedType, extPackage, "dto");
            String dtoClassName = dtoPackage + "." + dtoPackage + "DTO";
            String modelClassName = ModelNameUtils.getModel(adjustedType, extPackage);
            result = new SingleDtoConfig(adjustedType, modelClassName, dtoClassName, plural, this);
            this.singleDtoCfg.put(adjustedType.getCode(), result);
        }
        return result;
    }


    public CollectionDtoConfig getCollectionDtoConfig(YComposedType type)
    {
        YComposedType adjustedType = getAdjustedType(type);
        CollectionDtoConfig result = (CollectionDtoConfig)this.collectionDtoCfg.get(adjustedType.getCode());
        if(result == null)
        {
            String extPackage = this.codeGen.getExtensionPackage((YExtension)adjustedType.getNamespace());
            String plural = getPluralNoun(type.getCode());
            String dtoPackage = createPackage(adjustedType, extPackage, "dto");
            String dtoClassName = dtoPackage + "." + dtoPackage + "DTO";
            String modelClassName = ModelNameUtils.getModel(adjustedType, extPackage);
            result = new CollectionDtoConfig(adjustedType, modelClassName, dtoClassName, plural, this);
            this.collectionDtoCfg.put(adjustedType.getCode(), result);
        }
        return result;
    }


    public Collection<CollectionResourceConfig> getAllRootResources()
    {
        List<CollectionResourceConfig> result = new ArrayList<>(this.collectionResourcesMap.values());
        Collections.sort(result, (Comparator<? super CollectionResourceConfig>)new ResourceConfigComparator());
        return result;
    }


    public Collection<SingleResourceConfig> getAllSingleResources()
    {
        List<SingleResourceConfig> result = new ArrayList<>(this.singleResourcesMap.values());
        Collections.sort(result, (Comparator<? super SingleResourceConfig>)new ResourceConfigComparator());
        return result;
    }


    public YComposedType getAdjustedType(YComposedType type)
    {
        if(type.getSuperType() != null && "Link".equals(type.getSuperType().getCode()))
        {
            return type.getSuperType();
        }
        if("ExtensibleItem".equalsIgnoreCase(type.getCode()))
        {
            return type.getSuperType();
        }
        if("LocalizableItem".equalsIgnoreCase(type.getCode()))
        {
            return type.getSuperType().getSuperType();
        }
        if("GenericItem".equalsIgnoreCase(type.getCode()))
        {
            return type.getSuperType().getSuperType().getSuperType();
        }
        return type;
    }


    public static String getPluralNoun(String singularNoun)
    {
        if(singularNoun.endsWith("y"))
        {
            return singularNoun.substring(0, singularNoun.length() - 1) + "ies";
        }
        if(singularNoun.endsWith("s"))
        {
            return singularNoun + "es";
        }
        return singularNoun + "s";
    }


    private String createPackage(YComposedType type, String rootPackage, String pathSegment)
    {
        String jaloClass = CodeGenerator.getJaloClassName(type, rootPackage);
        int pos = jaloClass.lastIndexOf('.');
        String jaloPackage = jaloClass.substring(0, pos);
        if(jaloPackage.startsWith("de.hybris.platform.jalo"))
        {
            return jaloPackage.replaceFirst(".jalo", ".core." + pathSegment);
        }
        if(jaloPackage.contains(".jalo"))
        {
            return jaloPackage.replaceFirst(".jalo", "." + pathSegment);
        }
        if(jaloPackage.startsWith("de.hybris.platform.util"))
        {
            return jaloPackage.replace("de.hybris.platform.util", "de.hybris.platform.core." + pathSegment + ".util");
        }
        if(jaloPackage.startsWith("de.hybris.platform"))
        {
            String temp = jaloPackage.replace("de.hybris.platform.", "");
            return "de.hybris.platform." + temp.replaceFirst("\\.", "." + pathSegment + ".");
        }
        if(jaloPackage.startsWith(rootPackage))
        {
            return jaloPackage.replaceFirst(rootPackage, rootPackage + "." + rootPackage);
        }
        pos = jaloPackage.lastIndexOf('.');
        return (pos >= 0) ? (jaloPackage.substring(0, pos) + "." + jaloPackage.substring(0, pos)) : jaloPackage;
    }
}
