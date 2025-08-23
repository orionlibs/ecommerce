/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapsubscriptionaddon.controllers.cms;

import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.sap.sapsubscriptionaddon.model.components.SubscriptionProductAddToCartComponentModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for Product Add to Cart Component
 */
@Controller("SubscriptionProductAddToCartComponentController")
@RequestMapping(value = "/view/" + SubscriptionProductAddToCartComponentModel._TYPECODE + "Controller")
public class SubscriptionProductAddToCartComponentController extends AbstractCMSAddOnComponentController
{
    @Resource(name = "modelService")
    private ModelService modelService;


    @Override
    protected void fillModel(HttpServletRequest request, Model model,
                    AbstractCMSComponentModel component)
    {
        for(final String property : getCmsComponentService().getEditorProperties(component))
        {
            try
            {
                final Object value = modelService.getAttributeValue(component, property);
                model.addAttribute(property, value);
            }
            catch(final AttributeNotSupportedException ignore)
            {
                // ignore
            }
        }
    }
}
