package de.hybris.platform.servicelayer.internal.converter;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import java.util.Locale;
import java.util.Set;

public class ReadParams
{
    private final Set<String> qualifiers;
    private final Locale locale;
    private final I18NService i18nService;
    private final ModelConverter modelConverter;


    private ReadParams(Builder builder)
    {
        this.qualifiers = builder.qualifiers;
        this.locale = builder.locale;
        this.i18nService = builder.i18nService;
        this.modelConverter = builder.modelConverter;
    }


    public boolean isLocalized()
    {
        return (this.locale != null);
    }


    public PK getLangPK()
    {
        if(!isLocalized())
        {
            return null;
        }
        return this.i18nService.getLangPKFromLocale(this.locale);
    }


    public PK getCurrentLangPK()
    {
        if(this.i18nService == null)
        {
            return null;
        }
        Locale currentLocale = this.i18nService.getCurrentLocale();
        if(currentLocale == null)
        {
            return null;
        }
        return this.i18nService.getLangPKFromLocale(currentLocale);
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public I18NService getI18nService()
    {
        return this.i18nService;
    }


    public boolean hasNoQualifiers()
    {
        return this.qualifiers.isEmpty();
    }


    public Set<String> getAllQualifiers()
    {
        return this.qualifiers;
    }


    public String getSingleQualifier()
    {
        return (String)Iterables.getOnlyElement(this.qualifiers);
    }


    public boolean isAttemptToReadUnsupportedAttributes()
    {
        if(this.modelConverter instanceof ItemModelConverter)
        {
            ItemModelConverter itemConverter = (ItemModelConverter)this.modelConverter;
            return this.qualifiers.stream().anyMatch(q -> (!isRelation(q) && itemConverter.jaloOnlyAttribute(q)));
        }
        return false;
    }


    public boolean isRelation(String qualifier)
    {
        Preconditions.checkNotNull(qualifier, "qualifier mustn't be null");
        if(!(this.modelConverter instanceof ItemModelConverter))
        {
            return false;
        }
        ItemModelConverter itemConverter = (ItemModelConverter)this.modelConverter;
        ItemModelConverter.ModelAttributeInfo attrInfo = itemConverter.getInfo(qualifier);
        if(!attrInfo.getAttributeInfo().isRelation())
        {
            return false;
        }
        RelationInfo relationInfo = new RelationInfo(attrInfo);
        if(relationInfo.isOneToManyRelation() && !relationInfo.isOneSide())
        {
            return false;
        }
        return true;
    }


    public Class<?> getExpectedType(String qualifier)
    {
        Preconditions.checkNotNull(qualifier, "qualifier mustn't be null");
        if(!(this.modelConverter instanceof ItemModelConverter))
        {
            return Object.class;
        }
        ItemModelConverter itemConverter = (ItemModelConverter)this.modelConverter;
        return itemConverter.getInfo(qualifier).getFieldType();
    }


    public RelationInfo getRelationInfo(String qualifier)
    {
        Preconditions.checkArgument(isRelation(qualifier), "qualifier is not a relation");
        ItemModelConverter itemConverter = (ItemModelConverter)this.modelConverter;
        ItemModelConverter.ModelAttributeInfo attributeInfo = itemConverter.getInfo(qualifier);
        return new RelationInfo(attributeInfo);
    }


    public static Builder builderForSingleQualifier(String qualifier, I18NService i18nService)
    {
        Preconditions.checkNotNull(qualifier, "qualifier mustn't be null");
        Preconditions.checkNotNull(i18nService, "i18nService mustn't be null");
        return new Builder((Set)ImmutableSet.of(qualifier), i18nService);
    }


    public static Builder builderForMultipleQualifiers(Set<String> qualifiers, I18NService i18nService)
    {
        Preconditions.checkNotNull(qualifiers, "qualifier mustn't be null");
        Preconditions.checkNotNull(i18nService, "i18nService mustn't be null");
        return new Builder((Set)ImmutableSet.copyOf(qualifiers), i18nService);
    }


    public static Builder builderForSingleNotLocalizedQualifier(String qualifier)
    {
        Preconditions.checkNotNull(qualifier, "qualifier mustn't be null");
        return new Builder((Set)ImmutableSet.of(qualifier), null);
    }
}
