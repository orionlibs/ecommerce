/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.occ.controllers;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.cpq.productconfig.facades.CartIntegrationFacade;
import de.hybris.platform.cpq.productconfig.facades.data.ProductConfigData;
import de.hybris.platform.cpq.productconfig.occ.ProductConfigOrderEntryWsDTO;
import de.hybris.platform.cpq.productconfig.occ.ProductConfigWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Api(tags = "Product Configurator CPQ Cart Integration")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
public class ProductConfiguratorCPQCartIntegrationController
{
    private static final Logger LOG = Logger.getLogger(ProductConfiguratorCPQCartIntegrationController.class);
    @Resource(name = "cpqProductConfigCartIntegrationFacade")
    protected CartIntegrationFacade cartIntegrationFacade;
    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;


    @PostMapping(value = "/{cartId}/entries/" + CpqproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(nickname = "createCartEntryCPQConfiguration", value = "Adds a product configuration to the cart", notes = "Adds a product configuration to the cart. The root product of the configuration is added as a cart entry, in addition the configuration is attached to the new entry")
    @ApiBaseSiteIdUserIdAndCartIdParam
    public CartModificationWsDTO addCartEntry(//
                    @ApiParam(value = "Base site identifier") @PathVariable final String baseSiteId,
                    @ApiParam(value = "Request body parameter that contains attributes for creating the order entry, like quantity, product code and configuration identifier", required = true) //
                    @RequestBody final ProductConfigOrderEntryWsDTO entry) throws CommerceCartModificationException
    {
        final CartModificationData cartModificationData = addCartEntryInternal(entry);
        return getDataMapper().map(cartModificationData, CartModificationWsDTO.class);
    }


    @PutMapping(value = "/{cartId}/entries/{entryNumber}/"
                    + CpqproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(nickname = "replaceCartEntryCPQConfiguration", value = "Updates the configuration of a cart entry", notes = "Updates the configuration. The entire configuration attached to the cart entry is replaced by the configuration specified in the request body. Possible only if the configuration change has been initiated by the corresponding GET method before")
    @ApiBaseSiteIdUserIdAndCartIdParam
    public CartModificationWsDTO updateCartEntry(@ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
                    @ApiParam(required = true, value = "The entry number. Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero (0).") @PathVariable final int entryNumber,
                    @ApiParam(value = "Product configuration order entry with config id and order entry attributes") @RequestBody(required = true) final ProductConfigOrderEntryWsDTO entry)
    {
        entry.setEntryNumber(entryNumber);
        final CartModificationData cartModificationData = updateCartEntryInternal(entry);
        return getDataMapper().map(cartModificationData, CartModificationWsDTO.class);
    }


    @GetMapping(value = "/{cartId}/entries/{entryNumber}/"
                    + CpqproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE)
    @ResponseBody
    @ApiOperation(nickname = "getCartEntryCPQConfigurationId", value = "Gets the configuration id of a cart entry", notes = "Returns the configuration id of a cart entry ")
    @ApiBaseSiteIdUserIdAndCartIdParam
    public ProductConfigWsDTO getCartEntryConfigurationId(
                    @ApiParam(value = "Base site identifier.") @PathVariable final String baseSiteId,
                    @ApiParam(required = true, value = "The entry number. Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero (0).") @PathVariable final int entryNumber)
    {
        final ProductConfigData data = getCartEntryConfiguartionIdInternal(entryNumber);
        return getDataMapper().map(data, ProductConfigWsDTO.class);
    }


    protected ProductConfigData getCartEntryConfiguartionIdInternal(final int entryNumber)
    {
        final ProductConfigData data = cartIntegrationFacade.getConfigIdForSessionCartEntry(entryNumber);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("configuration '%s' is attached to cart entry number '%d'", data.getConfigId(), entryNumber));
        }
        return data;
    }


    protected CartModificationData addCartEntryInternal(final ProductConfigOrderEntryWsDTO entry)
                    throws CommerceCartModificationException
    {
        final String configId = entry.getConfigId();
        final Long quantity = entry.getQuantity();
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Adding Product with code '%s', quantity '%d' and configuration '%s' to cart",
                            entry.getProduct().getCode(), quantity, configId));
        }
        final CartModificationData modificationData = getCartIntegrationFacade().addConfigurationToCart(configId, quantity);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format(
                            "Product with code '%s'and configuration '%s' was added with quantity '%d' to cart returning status code '%s'",
                            modificationData.getEntry().getProduct().getCode(), configId, modificationData.getQuantityAdded(),
                            modificationData.getStatusCode()));
        }
        return modificationData;
    }


    protected CartModificationData updateCartEntryInternal(final ProductConfigOrderEntryWsDTO entry)
    {
        final String configId = entry.getConfigId();
        final int cartEntryNumber = entry.getEntryNumber();
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("updating cart entry number '%d' from configId '%s'", cartEntryNumber, configId));
        }
        final CartModificationData modificationData = getCartIntegrationFacade().updateCartEntryFromConfiguration(configId,
                        cartEntryNumber);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("updating cart entry number '%d' from configId '%s' returned statusCode '%s'", cartEntryNumber,
                            configId, modificationData.getStatusCode()));
        }
        return modificationData;
    }


    protected CartIntegrationFacade getCartIntegrationFacade()
    {
        return cartIntegrationFacade;
    }


    protected DataMapper getDataMapper()
    {
        return dataMapper;
    }
}
