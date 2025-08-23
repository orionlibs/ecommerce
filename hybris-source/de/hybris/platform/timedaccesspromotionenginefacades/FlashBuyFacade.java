package de.hybris.platform.timedaccesspromotionenginefacades;

import de.hybris.platform.commercefacades.product.data.ProductData;

public interface FlashBuyFacade
{
    String prepareFlashBuyInfo(ProductData paramProductData);


    void updateFlashBuyStatusForCart();
}
