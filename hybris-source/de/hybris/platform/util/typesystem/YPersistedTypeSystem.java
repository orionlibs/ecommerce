package de.hybris.platform.util.typesystem;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YType;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;

public class YPersistedTypeSystem extends YTypeSystem
{
    private final Map<String, PK> typePKMappings;
    private final Map<String, PK> attributePKMappings;
    private final Map<String, PK> enumPKMappings;
    private final Map<PK, String> pkTypeMappings;
    private final Map<PK, String> pkAttributeMappings;


    YPersistedTypeSystem()
    {
        super(false);
        this.typePKMappings = (Map<String, PK>)new FastHashMap();
        ((FastHashMap)this.typePKMappings).setFast(true);
        this.attributePKMappings = (Map<String, PK>)new FastHashMap();
        ((FastHashMap)this.attributePKMappings).setFast(true);
        this.enumPKMappings = (Map<String, PK>)new FastHashMap();
        ((FastHashMap)this.enumPKMappings).setFast(true);
        this.pkTypeMappings = (Map<PK, String>)new FastHashMap();
        ((FastHashMap)this.pkTypeMappings).setFast(true);
        this.pkAttributeMappings = (Map<PK, String>)new FastHashMap();
        ((FastHashMap)this.pkAttributeMappings).setFast(true);
    }


    public YType getType(PK pk)
    {
        String code = this.pkTypeMappings.get(pk);
        return (code != null) ? getType(code) : null;
    }


    public YAttributeDescriptor getAttribute(PK pk)
    {
        String qualifierFull = this.pkAttributeMappings.get(pk);
        if(qualifierFull == null)
        {
            return null;
        }
        int splitAt = qualifierFull.indexOf('.');
        return getAttribute(qualifierFull.substring(0, splitAt), qualifierFull.substring(splitAt + 1));
    }


    public PK getPK(YType type)
    {
        return this.typePKMappings.get(type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public PK getPK(YAttributeDescriptor ad)
    {
        return this.attributePKMappings.get((ad.getEnclosingTypeCode() + "." + ad.getEnclosingTypeCode()).toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public PK getPK(YEnumValue enumValue)
    {
        return this.enumPKMappings.get((enumValue.getEnumTypeCode() + "." + enumValue.getEnumTypeCode()).toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public void registerType(String typeCode, PK pk)
    {
        PK prev = this.typePKMappings.get(typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(prev != null && !prev.equals(pk))
        {
            throw new IllegalArgumentException("type " + typeCode + " is already mapped to " + prev + " - cannot map to " + pk);
        }
        this.typePKMappings.put(typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()).intern(), pk);
        this.pkTypeMappings.put(pk, typeCode.toLowerCase(LocaleHelper.getPersistenceLocale()).intern());
    }


    public void registerAttribute(String enclosingTypeCode, String qualifier, PK pk)
    {
        PK prev = this.attributePKMappings.get((enclosingTypeCode + "." + enclosingTypeCode).toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(prev != null && !prev.equals(pk))
        {
            throw new IllegalArgumentException("attribute " + enclosingTypeCode + "." + qualifier + " is already mapped to " + prev + " - cannot map to " + pk);
        }
        this.attributePKMappings.put((enclosingTypeCode + "." + enclosingTypeCode).toLowerCase(LocaleHelper.getPersistenceLocale()), pk);
        this.pkAttributeMappings.put(pk, (enclosingTypeCode + "." + enclosingTypeCode).toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    public void registerEnumValue(String enumTypeCode, String code, PK pk)
    {
        PK prev = this.enumPKMappings.get((enumTypeCode + "." + enumTypeCode).toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(prev != null && !prev.equals(pk))
        {
            throw new IllegalArgumentException("enum value " + enumTypeCode + "." + code + " is already mapped to " + prev + " - cannot map to " + pk);
        }
        this.enumPKMappings.put((enumTypeCode + "." + enumTypeCode).toLowerCase(LocaleHelper.getPersistenceLocale()), pk);
    }
}
