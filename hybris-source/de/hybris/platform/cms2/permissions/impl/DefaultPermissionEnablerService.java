package de.hybris.platform.cms2.permissions.impl;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import de.hybris.platform.cms2.data.AttributePermissionForType;
import de.hybris.platform.cms2.data.TypePermissionConfigs;
import de.hybris.platform.cms2.permissions.PermissionEnablerService;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPermissionEnablerService implements PermissionEnablerService, InitializingBean
{
    protected static final Long DEFAULT_EXPIRATION_TIME = Long.valueOf(360L);
    private List<AttributePermissionForType> permissionConfigs;
    private TypeService typeService;
    private boolean checkingAllTypes;
    private boolean checkingAllAttributes;
    private Supplier<Map<String, List<String>>> compiledTypeConfigurations = initializeCompiledTypeConfigurations(DEFAULT_EXPIRATION_TIME);
    private Supplier<Map<String, List<String>>> compiledAttributeConfigurations = initializeCompiledAttributeConfigurations(DEFAULT_EXPIRATION_TIME);


    public void afterPropertiesSet() throws Exception
    {
        this.compiledTypeConfigurations = initializeCompiledTypeConfigurations(DEFAULT_EXPIRATION_TIME);
        this.compiledAttributeConfigurations = initializeCompiledAttributeConfigurations(DEFAULT_EXPIRATION_TIME);
    }


    protected Supplier<Map<String, List<String>>> initializeCompiledTypeConfigurations(Long expirationTime)
    {
        return Suppliers.memoizeWithExpiration(
                        buildTypeConfigurations(), expirationTime
                                        .longValue(), TimeUnit.MINUTES);
    }


    protected Supplier<Map<String, List<String>>> initializeCompiledAttributeConfigurations(Long expirationTime)
    {
        return Suppliers.memoizeWithExpiration(
                        buildAttributeConfigurationsForAllTypes(), expirationTime
                                        .longValue(), TimeUnit.MINUTES);
    }


    protected Supplier<Map<String, List<String>>> buildTypeConfigurations()
    {
        return () ->
                        (!getCheckingAllTypes() || !getCheckingAllAttributes()) ? getAllTypePermissionConfigs(getPermissionConfigs()).stream().collect(Collectors.toMap((), ())) : new HashedMap();
    }


    protected Supplier<Map<String, List<String>>> buildAttributeConfigurationsForAllTypes()
    {
        return () -> {
            Map<String, List<String>> map;
            HashedMap hashedMap = new HashedMap();
            if(!getCheckingAllTypes() || !getCheckingAllAttributes())
            {
                map = (Map)getAllTypePermissionConfigs(getPermissionConfigs()).stream().collect(Collectors.toMap((), ()));
                map.putAll(getAllSubTypesPermissionConfigs(map));
            }
            return map;
        };
    }


    protected Map<String, List<String>> getAllSubTypesPermissionConfigs(Map<String, List<String>> configurations)
    {
        HashedMap hashedMap = new HashedMap();
        configurations.forEach((typeCode, config) -> {
            ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(typeCode);
            List<String> subTypeCodes = (List<String>)composedType.getAllSubTypes().stream().map(()).collect(Collectors.toList());
            List<String> filteredSubTypeCodes = (List<String>)subTypeCodes.stream().filter(()).collect(Collectors.toList());
            filteredSubTypeCodes.stream().forEach(());
        });
        return (Map<String, List<String>>)hashedMap;
    }


    protected List<TypePermissionConfigs> getAllTypePermissionConfigs(List<AttributePermissionForType> permissionConfigs)
    {
        List<TypePermissionConfigs> configurations = (List<TypePermissionConfigs>)permissionConfigs.stream().map(permissionConfig -> getPermissionConfigsForType(permissionConfig)).collect(Collectors.toList());
        return buildAllIncludedAttributes(configurations);
    }


    protected TypePermissionConfigs getPermissionConfigsForType(AttributePermissionForType permissionConfig)
    {
        ComposedTypeModel composedType = getTypeService().getComposedTypeForCode(permissionConfig.getTypecode());
        List<String> superCodes = (List<String>)composedType.getAllSuperTypes().stream().map(superType -> superType.getCode()).collect(Collectors.toList());
        List<AttributePermissionForType> superPermissionConfigs = getPermissionConfigsByTypeCodes(superCodes);
        List<AttributePermissionForType> attrPermissionConfigs = new ArrayList<>();
        attrPermissionConfigs.add(permissionConfig);
        attrPermissionConfigs.addAll(superPermissionConfigs);
        TypePermissionConfigs cfg = new TypePermissionConfigs();
        cfg.setTypecode(permissionConfig.getTypecode());
        cfg.setConfigs(attrPermissionConfigs);
        cfg.setInclude(new ArrayList());
        List<TypePermissionConfigs> allTypePermissionConfigs = new ArrayList<>();
        allTypePermissionConfigs.add(cfg);
        return cfg;
    }


    protected List<TypePermissionConfigs> buildAllIncludedAttributes(List<TypePermissionConfigs> configurations)
    {
        configurations.forEach(cfg -> buildIncludedAttributesForType(cfg));
        return configurations;
    }


    protected void buildIncludedAttributesForType(TypePermissionConfigs typeConfig)
    {
        ((LinkedList)typeConfig.getConfigs().stream()
                        .collect(Collectors.toCollection(LinkedList::new)))
                        .descendingIterator()
                        .forEachRemaining(config -> {
                            List<String> lastInclude = parseParam(config.getInclude());
                            List<String> lastExclude = parseParam(config.getExclude());
                            typeConfig.getInclude().addAll(lastInclude);
                            typeConfig.getInclude().removeAll(lastExclude);
                        });
    }


    protected List<String> parseParam(String params)
    {
        List<String> paramsArray = Objects.nonNull(params) ? Arrays.<String>asList(params.split(",")) : new ArrayList<>();
        return (List<String>)paramsArray.stream().map(String::trim).collect(Collectors.toList());
    }


    protected List<AttributePermissionForType> getPermissionConfigsByTypeCodes(List<String> typeCodes)
    {
        return (List<AttributePermissionForType>)typeCodes.stream()
                        .map(this::getPermissionConfigByTypeCode)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }


    protected Optional<AttributePermissionForType> getPermissionConfigByTypeCode(String typeCode)
    {
        return getPermissionConfigs().stream().filter(permissionConfig -> permissionConfig.getTypecode().equals(typeCode))
                        .findFirst();
    }


    public boolean isTypeVerifiable(String typeCode)
    {
        if(getCheckingAllTypes())
        {
            return true;
        }
        return ((Map)this.compiledTypeConfigurations.get()).containsKey(typeCode);
    }


    public boolean isAttributeVerifiable(String typeCode, String attribute)
    {
        if(getCheckingAllAttributes())
        {
            return true;
        }
        if(((Map)this.compiledAttributeConfigurations.get()).containsKey(typeCode))
        {
            List<String> attributes = (List<String>)((Map)this.compiledAttributeConfigurations.get()).get(typeCode);
            return attributes.contains(attribute);
        }
        return false;
    }


    protected List<AttributePermissionForType> getPermissionConfigs()
    {
        return this.permissionConfigs;
    }


    @Required
    public void setPermissionConfigs(List<AttributePermissionForType> permissionConfigs)
    {
        this.permissionConfigs = permissionConfigs;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected boolean getCheckingAllTypes()
    {
        return this.checkingAllTypes;
    }


    @Required
    public void setCheckingAllTypes(boolean checkingAllTypes)
    {
        this.checkingAllTypes = checkingAllTypes;
    }


    protected boolean getCheckingAllAttributes()
    {
        return this.checkingAllAttributes;
    }


    @Required
    public void setCheckingAllAttributes(boolean checkingAllAttributes)
    {
        this.checkingAllAttributes = checkingAllAttributes;
    }
}
