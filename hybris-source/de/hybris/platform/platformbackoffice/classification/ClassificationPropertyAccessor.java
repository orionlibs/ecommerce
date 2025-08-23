package de.hybris.platform.platformbackoffice.classification;

import com.hybris.cockpitng.core.model.WidgetModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.platformbackoffice.classification.util.BackofficeClassificationUtils;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class ClassificationPropertyAccessor implements PropertyAccessor, Ordered
{
    private static final String MODIFIED_FEATURES_MODEL_PARAM_PREFIX = "modifiedProductFeatures.pk";
    private static final int DEFAULT_ORDER = 400;
    private static final Class[] targetClasses = new Class[] {ProductModel.class};
    private ClassificationService classificationService;
    private int order = 400;


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public Class[] getSpecificTargetClasses()
    {
        return targetClasses;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return (target != null && isClassificationAttributeCorrect(qualifier));
    }


    protected boolean isClassificationAttributeCorrect(String qualifier)
    {
        return BackofficeClassificationUtils.isFeatureQualifier(qualifier);
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        if(target instanceof ProductModel)
        {
            ProductModel productModel = (ProductModel)target;
            Feature feature = getFeatureByCode(evaluationContext, productModel, qualifier);
            if(feature == null)
            {
                return TypedValue.NULL;
            }
            return extractTypedValue(feature);
        }
        return null;
    }


    protected Feature getFeatureByCode(EvaluationContext evaluationContext, ProductModel productModel, String qualifier)
    {
        Feature modifiedFeature = readModifiedFeature(evaluationContext, productModel, qualifier);
        if(modifiedFeature == null)
        {
            for(Feature feature : this.classificationService.getFeatures(productModel).getFeatures())
            {
                if(StringUtils.equals(qualifier,
                                BackofficeClassificationUtils.getFeatureQualifierEncoded(feature.getClassAttributeAssignment())))
                {
                    return feature;
                }
            }
            return null;
        }
        return modifiedFeature;
    }


    protected Feature readModifiedFeature(EvaluationContext evaluationContext, ProductModel productModel, String qualifier)
    {
        Feature ret = null;
        WidgetModel widgetModel = lookupWidgetModel(evaluationContext);
        Map<String, Feature> modifiedProductFeatures = lookupFeaturesMapForProduct(widgetModel, productModel);
        if(modifiedProductFeatures != null)
        {
            ret = modifiedProductFeatures.get(qualifier);
        }
        return ret;
    }


    private TypedValue extractTypedValue(Feature feature)
    {
        ClassificationInfo info = BackofficeClassificationUtils.convertFeatureToClassificationInfo(feature);
        return wrapCurrentValue(info);
    }


    protected TypedValue wrapCurrentValue(Object currentValue)
    {
        return new TypedValue(currentValue);
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object target, String qualifier) throws AccessException
    {
        return (target != null && isClassificationAttributeCorrect(qualifier));
    }


    public void write(EvaluationContext evaluationContext, Object target, String qualifier, Object newValue) throws AccessException
    {
        if(!(target instanceof ProductModel))
        {
            return;
        }
        ClassificationInfo info = (ClassificationInfo)newValue;
        Feature feature = getFeatureByCode(evaluationContext, (ProductModel)target, qualifier);
        if(feature != null)
        {
            BackofficeClassificationUtils.updateFeatureWithClassificationInfo(feature, info);
            WidgetModel widgetModel = lookupWidgetModel(evaluationContext);
            if(widgetModel != null)
            {
                Map<String, Feature> modifiedProductFeatures = lookupFeaturesMapForProduct(widgetModel, (ProductModel)target);
                modifiedProductFeatures.put(qualifier, feature);
                markDirty((ItemModel)target);
            }
        }
    }


    protected void markDirty(ItemModel target)
    {
        target.setModifiedtime(new Date());
    }


    private Map<String, Feature> lookupFeaturesMapForProduct(WidgetModel widgetModel, ProductModel productModel)
    {
        return (Map<String, Feature>)widgetModel.getValue(createFeatureMapForProductKey(productModel), Map.class);
    }


    private String createFeatureMapForProductKey(ProductModel productModel)
    {
        return "modifiedProductFeatures.pk" + productModel.getPk();
    }


    private WidgetModel lookupWidgetModel(EvaluationContext evaluationContext)
    {
        WidgetModel ret = null;
        TypedValue typedRootObject = evaluationContext.getRootObject();
        if(typedRootObject != null && typedRootObject.getValue() instanceof WidgetModel)
        {
            ret = (WidgetModel)typedRootObject.getValue();
        }
        return ret;
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }
}
