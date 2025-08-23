package de.hybris.platform.directpersistence.cache;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class SLDDataContainer
{
    private final PK pk;
    private final PK typePk;
    private final String typeCode;
    private final long version;
    private final List<AttributeValue> attributeValues;
    private final List<AttributeValue> localizedAttributeValues;
    private final List<AttributeValue> propertyValues;


    private SLDDataContainer(Builder builder)
    {
        Preconditions.checkArgument(builder.isVersionSpecified(), "version must be specified");
        this.pk = Objects.<PK>requireNonNull(builder.pk, "pk must not be null");
        this.typePk = Objects.<PK>requireNonNull(builder.typePk, "typePk must not be null");
        this.typeCode = Objects.<String>requireNonNull(builder.typeCode, "typeCode must not be null");
        this.attributeValues = Objects.<List<AttributeValue>>requireNonNull(builder.attributeValues, "attributeValues must not be null");
        this
                        .localizedAttributeValues = (builder.localizedAttributeValues == null) ? Collections.<AttributeValue>emptyList() : builder.localizedAttributeValues;
        this.propertyValues = (builder.propertyValues == null) ? Collections.<AttributeValue>emptyList() : builder.propertyValues;
        this.version = builder.version;
    }


    public static Builder builder()
    {
        return new Builder();
    }


    public PK getPk()
    {
        return this.pk;
    }


    public PK getTypePk()
    {
        return this.typePk;
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public long getVersion()
    {
        return this.version;
    }


    public List<AttributeValue> getAllAttributes()
    {
        List<AttributeValue> allAttributeValues = new ArrayList<>();
        allAttributeValues.addAll(this.attributeValues);
        allAttributeValues.addAll(this.localizedAttributeValues);
        allAttributeValues.addAll(this.propertyValues);
        return allAttributeValues;
    }


    public List<AttributeValue> getAllLocalizedAttributes()
    {
        return this.localizedAttributeValues;
    }


    public List<AttributeValue> getAllPropertyValues()
    {
        return this.propertyValues;
    }


    public AttributeValue getAttributeValue(String attributeName, PK langPk)
    {
        Optional<? extends AttributeValue> found;
        String lowerCaseAttributeName = attributeName.toLowerCase(LocaleHelper.getPersistenceLocale());
        if(langPk == null)
        {
            found = this.attributeValues.stream().filter(byName(lowerCaseAttributeName)).findFirst();
            if(!found.isPresent())
            {
                found = this.propertyValues.stream().filter(byName(lowerCaseAttributeName)).findFirst();
            }
        }
        else
        {
            found = this.localizedAttributeValues.stream().filter(byNameAndLang(lowerCaseAttributeName, langPk)).findFirst();
            if(!found.isPresent())
            {
                found = this.propertyValues.stream().filter(byNameAndLang(lowerCaseAttributeName, langPk)).findFirst();
            }
        }
        return found.isPresent() ? found.get() : null;
    }


    public AttributeValue getPropertyValue(String propertyName, PK langPk)
    {
        Optional<? extends AttributeValue> found;
        String lowerCasePropertyName = propertyName.toLowerCase(LocaleHelper.getPersistenceLocale());
        if(langPk == null)
        {
            found = this.propertyValues.stream().filter(byName(lowerCasePropertyName)).findFirst();
        }
        else
        {
            found = this.propertyValues.stream().filter(byNameAndLang(lowerCasePropertyName, langPk)).findFirst();
        }
        return found.isPresent() ? found.get() : null;
    }


    private Predicate<AttributeValue> byNameAndLang(String name, PK langPk)
    {
        return v -> (name.equals(v.getName()) && langPk.equals(v.getLangPk()));
    }


    private Predicate<AttributeValue> byName(String name)
    {
        return v -> (name.equals(v.getName()) && v.getLangPk() == null);
    }
}
