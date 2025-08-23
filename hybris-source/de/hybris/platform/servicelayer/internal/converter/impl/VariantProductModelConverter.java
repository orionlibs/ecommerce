package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.directpersistence.selfhealing.SelfHealingService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.SourceTransformer;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;

public class VariantProductModelConverter extends ItemModelConverter
{
    public VariantProductModelConverter(ModelService modelService, I18NService i18nService, CommonI18NService commonI18NService, String type, Class<? extends AbstractItemModel> modelClass, SerializationStrategy serializationStrategy, SourceTransformer sourceTransformer,
                    SelfHealingService selfHealingService)
    {
        super(modelService, i18nService, commonI18NService, type, modelClass, serializationStrategy, sourceTransformer, selfHealingService);
    }


    public String getType(Object model)
    {
        String explicitType = getTypeFromModelContext(model);
        if(explicitType == null || "VariantProduct".equalsIgnoreCase(explicitType))
        {
            String typeFromBaseProduct = getTypeFromBaseProduct((VariantProductModel)model);
            return (typeFromBaseProduct == null) ? getDefaultType() : typeFromBaseProduct;
        }
        return explicitType;
    }


    protected String getTypeFromBaseProduct(VariantProductModel model)
    {
        String result = null;
        ProductModel baseProduct = model.getBaseProduct();
        if(baseProduct != null)
        {
            VariantTypeModel vtm = baseProduct.getVariantType();
            if(vtm != null)
            {
                result = vtm.getCode();
            }
        }
        return result;
    }
}
