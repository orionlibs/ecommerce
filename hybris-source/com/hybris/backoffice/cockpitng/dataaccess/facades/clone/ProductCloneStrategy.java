/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.clone;

import com.hybris.cockpitng.dataaccess.facades.clone.CloneStrategy;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation {@link CloneStrategy} for {@link ProductModel}
 */
public class ProductCloneStrategy implements CloneStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(ProductCloneStrategy.class);
    private ModelService modelService;
    /**
     * @deprecated since 1905 - code will be removed, implementation was changed for using typeService
     */
    @Deprecated(since = "1905", forRemoval = true)
    private TypeFacade typeFacade;
    /**
     * @deprecated since 1905 - code will be removed, implementation was changed for using modelService
     */
    @Deprecated(since = "1905", forRemoval = true)
    private ObjectFacade objectFacade;
    private TypeService typeService;


    @Override
    public <T> boolean canHandle(final T objectToClone)
    {
        return isProductModel(objectToClone) && !isNew(objectToClone) && !isSingleton(objectToClone);
    }


    @Override
    public <T> T clone(final T objectToClone)
    {
        if(!canHandle(objectToClone))
        {
            throw new IllegalStateException("You can't clone with strategy for which canHandle() return false");
        }
        final ModelCloningContext context = createCloningContext();
        final T clonedProduct = getModelService().clone(objectToClone, context);
        getModelService().setAttributeValue(clonedProduct, ProductModel.CODE, null);
        return clonedProduct;
    }


    protected ModelCloningContext createCloningContext()
    {
        return new ProductModelCloningContext(typeService);
    }


    protected static class ProductModelCloningContext implements ModelCloningContext
    {
        final protected TypeService typeService;


        public ProductModelCloningContext(final TypeService typeService)
        {
            this.typeService = typeService;
        }


        @Override
        public boolean skipAttribute(final Object original, final String qualifier)
        {
            try
            {
                final ComposedTypeModel composedType = typeService.getComposedTypeForClass(original.getClass());
                final AttributeDescriptorModel attributeDescriptor = typeService.getAttributeDescriptor(composedType, qualifier);
                if(attributeDescriptor != null)
                {
                    return attributeDescriptor.getPartOf();
                }
            }
            catch(final UnknownIdentifierException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Unknown attribute: {}.{}, facade will be used.", original, qualifier);
                }
            }
            return true;
        }


        @Override
        public boolean treatAsPartOf(final Object original, final String qualifier)
        {
            return false;
        }


        @Override
        public boolean usePresetValue(final Object original, final String qualifier)
        {
            return false;
        }


        @Override
        public Object getPresetValue(final Object original, final String qualifier)
        {
            return null;
        }
    }


    private static boolean isProductModel(final Object objectToClone)
    {
        return objectToClone instanceof ProductModel;
    }


    private boolean isNew(final Object objectToClone)
    {
        return modelService.isNew(objectToClone);
    }


    private boolean isSingleton(final Object objectToClone)
    {
        return typeService.getComposedTypeForClass(objectToClone.getClass()).getSingleton();
    }


    /**
     * @return Integer.MAX_VALUE / 4
     */
    @Override
    public int getOrder()
    {
        return Integer.MAX_VALUE / 4;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    /**
     * @deprecated since 1905 - code will be removed, implementation was changed for using typeService
     */
    @Deprecated(since = "1905", forRemoval = true)
    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    /**
     * @deprecated since 1905 - code will be removed, implementation was changed for using typeService
     */
    @Deprecated(since = "1905", forRemoval = true)
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    /**
     * @deprecated since 1905 - code will be removed, implementation was changed for using modelService
     */
    @Deprecated(since = "1905", forRemoval = true)
    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    /**
     * @deprecated since 1905 - code will be removed, implementation was changed for using modelService
     */
    @Deprecated(since = "1905", forRemoval = true)
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public TypeService getTypeService()
    {
        return typeService;
    }


    @Required
    public void setTypeService(final TypeService typeService)
    {
        this.typeService = typeService;
    }
}
