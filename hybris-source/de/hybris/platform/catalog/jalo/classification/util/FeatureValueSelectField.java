package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.core.GenericSelectField;
import de.hybris.platform.jalo.c2l.Language;
import java.math.BigDecimal;
import java.util.Map;

public class FeatureValueSelectField extends GenericSelectField
{
    private final FeatureField field;


    public static FeatureValueSelectField select(ClassAttributeAssignment assignment)
    {
        return new FeatureValueSelectField(assignment, null, null);
    }


    public static FeatureValueSelectField select(ClassAttributeAssignment assignment, Language lang)
    {
        return new FeatureValueSelectField(assignment, null, lang);
    }


    public static FeatureValueSelectField select(ClassAttributeAssignment assignment, String productTypeAlias, Language lang)
    {
        return new FeatureValueSelectField(assignment, productTypeAlias, lang);
    }


    protected FeatureValueSelectField(ClassAttributeAssignment assignment, String productTypeAlias, Language lang)
    {
        super(null, "dummy", null);
        this.field = new FeatureField(assignment, productTypeAlias, lang, false, true);
        switch(null.$SwitchMap$de$hybris$platform$catalog$jalo$classification$util$TypedFeature$FeatureType[this.field.getFeatureType().ordinal()])
        {
            case 1:
                setReturnClass(String.class);
                break;
            case 2:
                setReturnClass(ClassificationAttributeValue.class);
                break;
            case 3:
                setReturnClass(BigDecimal.class);
                break;
            case 4:
                setReturnClass(Boolean.class);
                break;
        }
    }


    public Language getLanguage()
    {
        return getFeatureField().getLanguage();
    }


    public void setLanguage(Language lang)
    {
        getFeatureField().setLanguage(lang);
    }


    public String getProductTypeAlias()
    {
        return getFeatureField().getProductTypeAlias();
    }


    public void steProeductTypeAlias(String alias)
    {
        getFeatureField().setProductTypeAlias(alias);
    }


    public ClassificationAttributeUnit getUnit()
    {
        return getFeatureField().getUnit();
    }


    public void setUnit(ClassificationAttributeUnit unit)
    {
        getFeatureField().setUnit(unit);
    }


    public FeatureField getFeatureField()
    {
        return this.field;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        this.field.toFlexibleSearch(queryBuffer, typeIndexMap, valueMap);
    }
}
