/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.facades.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.cpq.productconfig.facades.ProductDetailFacade;
import de.hybris.platform.cpq.productconfig.facades.data.ProductDetailData;
import de.hybris.platform.cpq.productconfig.facades.data.ProductThumbnailData;
import de.hybris.platform.cpq.productconfig.services.ProductCheckService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link ProductDetailFacade}
 *
 * @deprecated Since 2108.
 */
@Deprecated(since = "2108", forRemoval = true)
public class DefaultProductDetailFacade implements ProductDetailFacade
{
    private final String imageFormatUsedForCPQ;
    private final ProductFacade productFacade;
    private final String thumbnailFormatUsedForCPQ;
    private final ProductCheckService productCheckService;


    /**
     * Constructor, taking the mandatory supporting beans
     *
     * @param productFacade
     *           For getting (generic) product details
     * @param productCheckService
     *           Used for checking if a product exists in commerce
     * @param imageFormatUsedForCPQ
     *           Image format that we use to determine the image to sent to the interactive CPQ configuration UI. The
     *           range of formats is no enum neither hard-coded in commerce extensions, but typically customized per
     *           spring property injection. Search for 'imageFormats'.
     * @param thumbnailFormatUsedForCPQ
     *           Thumbnail image format that we use to determine the thumbnail to sent to the interactive CPQ
     *           configuration UI. The range of formats is no enum neither hard-coded in commerce extensions, but
     *           typically customized per spring property injection. Search for 'imageFormats'.
     */
    public DefaultProductDetailFacade(final ProductFacade productFacade, final ProductCheckService productCheckService,
                    final String imageFormatUsedForCPQ, final String thumbnailFormatUsedForCPQ)
    {
        this.productFacade = productFacade;
        this.imageFormatUsedForCPQ = imageFormatUsedForCPQ;
        this.thumbnailFormatUsedForCPQ = thumbnailFormatUsedForCPQ;
        this.productCheckService = productCheckService;
    }


    protected String getImageFormatUsedForCPQ()
    {
        return imageFormatUsedForCPQ;
    }


    protected String getThumbnailFormatUsedForCPQ()
    {
        return thumbnailFormatUsedForCPQ;
    }


    protected ProductFacade getProductFacade()
    {
        return productFacade;
    }


    @Override
    public ProductDetailData getProductDetail(final String productCode)
    {
        final ProductDetailData result = new ProductDetailData();
        final ProductData productData = getProductFacade().getProductForCodeAndOptions(productCode,
                        Arrays.asList(ProductOption.IMAGES, ProductOption.DESCRIPTION));
        transferSimpleAttributes(result, productData);
        transferImage(result, productData);
        return result;
    }


    protected void transferSimpleAttributes(final ProductDetailData result, final ProductData productData)
    {
        checkTransferArguments(result, productData);
        result.setDescription(productData.getDescription());
        result.setCode(productData.getCode());
        result.setName(productData.getName());
    }


    protected void transferImage(final ProductDetailData productDetailData, final ProductData productData)
    {
        checkTransferArguments(productDetailData, productData);
        final Collection<ImageData> images = productData.getImages();
        if(images != null)
        {
            final Optional<ImageData> imageOptional = images.stream().filter(
                                            image -> ImageDataType.PRIMARY == image.getImageType() && getImageFormatUsedForCPQ().equals(image.getFormat()))
                            .findFirst();
            if(imageOptional.isPresent())
            {
                productDetailData.setPrimaryProductImageUrl(imageOptional.get().getUrl());
            }
        }
    }


    protected void checkTransferArguments(final ProductDetailData result, final ProductData productData)
    {
        Preconditions.checkArgument(result != null, "We expect product details");
        Preconditions.checkArgument(productData != null, "We expect product data to be present");
    }


    @Override
    public Set<ProductThumbnailData> getProductThumbnailImages(final Set<String> productSet)
    {
        Preconditions.checkArgument(productSet != null, "We expect a set of products");
        return getProductCheckService().checkProductIds(productSet).stream().map(productCode -> getProductData(productCode))
                        .map(productData -> getThumbnailData(productData)).collect(Collectors.toSet());
    }


    protected ProductThumbnailData getThumbnailData(final ProductData productData)
    {
        Preconditions.checkArgument(productData != null, "We expect a product data bean");
        final ProductThumbnailData thumbnailData = new ProductThumbnailData();
        thumbnailData.setCode(productData.getCode());
        final Collection<ImageData> images = productData.getImages();
        if(images != null)
        {
            final Optional<ImageData> imageOptional = images.stream().filter(
                                            image -> ImageDataType.PRIMARY == image.getImageType() && getThumbnailFormatUsedForCPQ().equals(image.getFormat()))
                            .findFirst();
            if(imageOptional.isPresent())
            {
                thumbnailData.setThumbnailProductImageUrl(imageOptional.get().getUrl());
            }
        }
        return thumbnailData;
    }


    protected ProductData getProductData(final String productCode)
    {
        Preconditions.checkArgument(productCode != null, "We expect a product code");
        return getProductFacade().getProductForCodeAndOptions(productCode, Arrays.asList(ProductOption.IMAGES));
    }


    protected ProductCheckService getProductCheckService()
    {
        return this.productCheckService;
    }
}
