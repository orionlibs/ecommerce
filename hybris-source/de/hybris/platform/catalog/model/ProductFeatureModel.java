package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ProductFeatureModel extends ItemModel
{
    public static final String _TYPECODE = "ProductFeature";
    public static final String _PRODUCT2FEATURERELATION = "Product2FeatureRelation";
    public static final String QUALIFIER = "qualifier";
    public static final String CLASSIFICATIONATTRIBUTEASSIGNMENT = "classificationAttributeAssignment";
    public static final String LANGUAGE = "language";
    public static final String VALUEPOSITION = "valuePosition";
    public static final String FEATUREPOSITION = "featurePosition";
    public static final String VALUETYPE = "valueType";
    public static final String STRINGVALUE = "stringValue";
    public static final String BOOLEANVALUE = "booleanValue";
    public static final String NUMBERVALUE = "numberValue";
    public static final String RAWVALUE = "rawValue";
    public static final String VALUE = "value";
    public static final String UNIT = "unit";
    public static final String VALUEDETAILS = "valueDetails";
    public static final String DESCRIPTION = "description";
    public static final String PRODUCTPOS = "productPOS";
    public static final String PRODUCT = "product";
    public static final String AUTHOR = "author";


    public ProductFeatureModel()
    {
    }


    public ProductFeatureModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductFeatureModel(ProductModel _product, String _qualifier, Object _value)
    {
        setProduct(_product);
        setQualifier(_qualifier);
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductFeatureModel(ClassAttributeAssignmentModel _classificationAttributeAssignment, LanguageModel _language, ItemModel _owner, ProductModel _product, String _qualifier, Object _value)
    {
        setClassificationAttributeAssignment(_classificationAttributeAssignment);
        setLanguage(_language);
        setOwner(_owner);
        setProduct(_product);
        setQualifier(_qualifier);
        setValue(_value);
    }


    @Accessor(qualifier = "author", type = Accessor.Type.GETTER)
    public String getAuthor()
    {
        return (String)getPersistenceContext().getPropertyValue("author");
    }


    @Accessor(qualifier = "classificationAttributeAssignment", type = Accessor.Type.GETTER)
    public ClassAttributeAssignmentModel getClassificationAttributeAssignment()
    {
        return (ClassAttributeAssignmentModel)getPersistenceContext().getPropertyValue("classificationAttributeAssignment");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "featurePosition", type = Accessor.Type.GETTER)
    public Integer getFeaturePosition()
    {
        return (Integer)getPersistenceContext().getPropertyValue("featurePosition");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "product", type = Accessor.Type.GETTER)
    public ProductModel getProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("product");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public ClassificationAttributeUnitModel getUnit()
    {
        return (ClassificationAttributeUnitModel)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public Object getValue()
    {
        return getPersistenceContext().getDynamicValue((AbstractItemModel)this, "value");
    }


    @Accessor(qualifier = "valueDetails", type = Accessor.Type.GETTER)
    public String getValueDetails()
    {
        return (String)getPersistenceContext().getPropertyValue("valueDetails");
    }


    @Accessor(qualifier = "valuePosition", type = Accessor.Type.GETTER)
    public Integer getValuePosition()
    {
        return (Integer)getPersistenceContext().getPropertyValue("valuePosition");
    }


    @Accessor(qualifier = "author", type = Accessor.Type.SETTER)
    public void setAuthor(String value)
    {
        getPersistenceContext().setPropertyValue("author", value);
    }


    @Accessor(qualifier = "classificationAttributeAssignment", type = Accessor.Type.SETTER)
    public void setClassificationAttributeAssignment(ClassAttributeAssignmentModel value)
    {
        getPersistenceContext().setPropertyValue("classificationAttributeAssignment", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "featurePosition", type = Accessor.Type.SETTER)
    public void setFeaturePosition(Integer value)
    {
        getPersistenceContext().setPropertyValue("featurePosition", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "product", type = Accessor.Type.SETTER)
    public void setProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("product", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(ClassificationAttributeUnitModel value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(Object value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "value", value);
    }


    @Accessor(qualifier = "valueDetails", type = Accessor.Type.SETTER)
    public void setValueDetails(String value)
    {
        getPersistenceContext().setPropertyValue("valueDetails", value);
    }


    @Accessor(qualifier = "valuePosition", type = Accessor.Type.SETTER)
    public void setValuePosition(Integer value)
    {
        getPersistenceContext().setPropertyValue("valuePosition", value);
    }
}
