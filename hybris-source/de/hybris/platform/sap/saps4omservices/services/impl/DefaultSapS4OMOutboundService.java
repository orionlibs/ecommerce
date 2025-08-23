/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory;
import de.hybris.platform.outboundservices.monitoring.OutboundRequestExecutionException;
import de.hybris.platform.sap.saps4omservices.decorator.SAPS4OMOutboundRequestDecorator;
import de.hybris.platform.sap.saps4omservices.exceptions.OutboundServiceException;
import de.hybris.platform.sap.saps4omservices.filter.dto.FilterData;
import de.hybris.platform.sap.saps4omservices.services.SapS4OMOutboundService;
import de.hybris.platform.saps4omservices.dto.SAPS4OMData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMOrders;
import de.hybris.platform.saps4omservices.dto.SAPS4OMRequestData;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * This handles outbound requests for order simulation, order creation and order details.
 */
public class DefaultSapS4OMOutboundService implements SapS4OMOutboundService
{
    private static final String A_SALES_ORDER_SIMULATION = "A_SalesOrderSimulation";
    private static final String A_SALES_ORDER = "A_SalesOrder";
    private static final String S4_ORDER_DESTINATION = "s4omOrderDestination";
    private static final String S4_ORDER_SIMULATE_DESTINATION = "s4omOrderSimulateDestination";
    private static final String A_SALES_ORDER_PATH_VAR = "('{0}')";
    private static final String A_SALES_ORDER_ID = "{0}";
    private static final String QUERY_PARAM_SEPARATOR = "&";
    private static final String QUERY_START = "?";
    private static final String METADATA = "$metadata";
    private static final String QUERY_SAP_CLIENT = "?sap-client=";
    private static final String SAP_CLIENT = "sap-client";
    private static final String ENTITY = "entity";
    private static final String X_CSRF_TOKEN = "X-CSRF-Token";
    private static final String X_CSRF_TOKEN_FETCH = "Fetch";
    private static final String COOKIE_SEPARATOR = "; ";
    private static final String X_CSRF_SET_COOKIES = "Set-Cookie";
    private static final String X_CSRF_COOKIE = "Cookie";
    private static final String CSRF_URL = "csrfURL";
    private static final String CREDENTIAL_ERROR_MESSAGE = "UNAUTHORIZED ACCESS";
    private static final String ERROR_MESSAGE = "Unable to process the request, Please try again";
    private static final String ERROR_MESSAGE_LOCALE = "message.error.backendcallfailure";
    private static final String REST_CLIENT_ERROR = "Failed to connect to the destination";
    private static final String REST_CLIENT_ERROR_LOCALE = "message.error.destinationconnectionfailure";
    private static final String ACCEPT_LANGUAGE = "Accept-Language";
    private static final String EMPTY_STRING = "";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSapS4OMOutboundService.class);
    private SessionService sessionService;
    private DestinationService<ConsumedDestinationModel> destinationService;
    private RestTemplate s4omRestTemplate;
    private List<SAPS4OMOutboundRequestDecorator> outboundRequestDecorator = Collections.emptyList();
    private CommonI18NService commonI18NService;
    private IntegrationRestTemplateFactory integrationRestTemplateFactory;


    /*
     *  creation of salesOrderSimulation
     *  Fetch CSRF token for salesOrderSimulation
     *  @return SalesOrderSimulationData
     */
    @Override
    public SAPS4OMData simulateOrder(String destinationId, String destinationTargetId, SAPS4OMRequestData requestData) throws OutboundServiceException
    {
        LOG.debug("Entering: DefaultSapS4OMOutboundService.simulateOrder() method");
        final ConsumedDestinationModel destinationModel = getDestinationService()
                        .getDestinationByIdAndByDestinationTargetId(destinationId, destinationTargetId);
        String backendUrl = getURL(destinationModel, Collections.emptyMap());
        ResponseEntity<SAPS4OMData> responseData = sendRequest(destinationModel, backendUrl,
                        HttpMethod.POST, requestData, SAPS4OMData.class);
        return responseData.getBody();
    }


    @Override
    public SAPS4OMData createOrder(String destinationId, String destinationTargetId, SAPS4OMRequestData requestData) throws OutboundServiceException
    {
        LOG.debug("Entering: DefaultSapS4OMOutboundService.createOrder() method");
        final ConsumedDestinationModel destinationModel = getDestinationService()
                        .getDestinationByIdAndByDestinationTargetId(destinationId, destinationTargetId);
        String backendUrl = getURL(destinationModel, Collections.emptyMap());
        ResponseEntity<SAPS4OMData> responseData = sendRequest(destinationModel, backendUrl,
                        HttpMethod.POST, requestData, SAPS4OMData.class);
        return responseData.getBody();
    }


    @Override
    public SAPS4OMOrders fetchOrders(String destinationId, String destinationTargetId, Map<String, List<FilterData>> filters) throws OutboundServiceException
    {
        LOG.debug("Entering: DefaultSapS4OMOutboundService.fetchOrders() method");
        final ConsumedDestinationModel destinationModel = getDestinationService()
                        .getDestinationByIdAndByDestinationTargetId(destinationId, destinationTargetId);
        String serachOrderHisotyURL = getURL(destinationModel, filters);
        ResponseEntity<SAPS4OMOrders> responseData = sendRequest(destinationModel, serachOrderHisotyURL,
                        HttpMethod.GET, new SAPS4OMRequestData(), SAPS4OMOrders.class);
        return responseData.getBody();
    }


    @Override
    public SAPS4OMData fetchOrderDetails(String destinationId, String destinationTargetId, String orderID, Map<String, List<FilterData>> filterData) throws OutboundServiceException
    {
        LOG.debug("Entering: DefaultSapS4OMOutboundService.fetchOrderDetails() method");
        final ConsumedDestinationModel destinationModel = getDestinationService()
                        .getDestinationByIdAndByDestinationTargetId(destinationId, destinationTargetId);
        String backendUrl = getURL(destinationModel, filterData);
        String saleOrderIdQueryParam = A_SALES_ORDER_PATH_VAR.replace(A_SALES_ORDER_ID, orderID);
        backendUrl = backendUrl.replace(A_SALES_ORDER, A_SALES_ORDER + saleOrderIdQueryParam);
        ResponseEntity<SAPS4OMData> responseData = sendRequest(destinationModel, backendUrl,
                        HttpMethod.GET, new SAPS4OMRequestData(), SAPS4OMData.class);
        return responseData.getBody();
    }


    protected <T> ResponseEntity<T> sendRequest(ConsumedDestinationModel destinationModel, String url, HttpMethod method, SAPS4OMRequestData requestData,
                    Class<T> responseType) throws OutboundServiceException
    {
        boolean validCsrfToken = true;
        HttpHeaders header = getHttpHeaderFromSession(getCurrentLanguage() + destinationModel.getId());
        if(header == null || header.isEmpty())
        {
            LOG.debug("No header found from session");
            header = getAuthenticationDetails(destinationModel);
        }
        postHeaderCreation(header, setHeaderAction(url, method));
        HttpEntity<SAPS4OMRequestData> requestEntity = new HttpEntity<>(requestData, header);
        ResponseEntity<T> responseData = null;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("The request payload send to {}  - Request payload:  {}", url, logObjectAsString(requestData));
        }
        try
        {
            responseData = getS4omRestTemplate().exchange(url, method, requestEntity, responseType);
        }
        catch(final HttpClientErrorException e)
        {
            if(e.getStatusCode() == HttpStatus.FORBIDDEN)
            {
                if(CollectionUtils.isEmpty(Optional.ofNullable(e.getResponseHeaders()).orElse(new HttpHeaders()).get(X_CSRF_TOKEN.toLowerCase())))
                {
                    LOG.error("The response status {} with error message: {}", e.getStatusCode(), e.getMessage());
                    throw new OutboundServiceException(Localization.getLocalizedString(ERROR_MESSAGE_LOCALE), e);
                }
                else
                {
                    validCsrfToken = false;
                    LOG.error("CSRF token validation failed");
                }
            }
            else
            {
                String error = formatErrorResponse(e.getResponseBodyAsString());
                throw new OutboundServiceException(error, e);
            }
        }
        catch(final ResourceAccessException e)
        {
            LOG.error(ERROR_MESSAGE);
            throw new OutboundServiceException(Localization.getLocalizedString(ERROR_MESSAGE_LOCALE), e);
        }
        catch(final RestClientException e)
        {
            LOG.error(REST_CLIENT_ERROR);
            throw new OutboundServiceException(Localization.getLocalizedString(REST_CLIENT_ERROR_LOCALE), e);
        }
        if(!validCsrfToken)
        {
            getAuthenticationDetails(destinationModel);
            sendRequest(destinationModel, url, method, requestData, responseType);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("The response payload from backend system : {} ", logObjectAsString(responseData));
        }
        return responseData;
    }


    private String logObjectAsString(Object objectType)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(objectType);
        }
        catch(JsonProcessingException e)
        {
            LOG.error("Error while processing JSON payloads");
        }
        return EMPTY_STRING;
    }


    protected String setHeaderAction(String url, HttpMethod method)
    {
        String action;
        if(url.contains(A_SALES_ORDER_SIMULATION))
        {
            action = String.valueOf(method) + " " + A_SALES_ORDER_SIMULATION;
        }
        else
        {
            action = String.valueOf(method) + " " + A_SALES_ORDER;
        }
        return action;
    }


    protected String formatErrorResponse(String errorResponse)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            JsonNode rootNode = mapper.readTree(errorResponse);
            return rootNode != null ? rootNode.path("error").path("message").path("value").textValue() : REST_CLIENT_ERROR;
        }
        catch(JsonProcessingException e)
        {
            LOG.error("Error while processing JSON error response");
        }
        return REST_CLIENT_ERROR;
    }


    private void postHeaderCreation(HttpHeaders header, String action)
    {
        for(SAPS4OMOutboundRequestDecorator decor : outboundRequestDecorator)
        {
            decor.decorate(header, action);
        }
    }


    protected HttpHeaders createHTTPHeader(HttpHeaders authenticationResult)
    {
        final HttpHeaders header = new HttpHeaders();
        header.add(X_CSRF_TOKEN, getCsrfToken(authenticationResult));
        header.add(X_CSRF_COOKIE, getCookies(authenticationResult));
        String languageISOCode = getCurrentLanguage();
        if(languageISOCode != null)
        {
            header.add(ACCEPT_LANGUAGE, languageISOCode);
        }
        final List<MediaType> acceptList = new ArrayList<>();
        acceptList.add(MediaType.APPLICATION_JSON);
        header.setAccept(acceptList);
        header.setContentType(MediaType.APPLICATION_JSON);
        return header;
    }


    private String getCurrentLanguage()
    {
        return getCommonI18NService().getCurrentLanguage().getIsocode();
    }


    protected String getCsrfToken(final HttpHeaders headers)
    {
        final List<String> tokens = headers.get(X_CSRF_TOKEN);
        return CollectionUtils.isNotEmpty(tokens) ? tokens.get(0) : "";
    }


    protected String getCookies(final HttpHeaders headers)
    {
        final List<String> cookies = headers.get(X_CSRF_SET_COOKIES);
        return StringUtils.join(cookies, COOKIE_SEPARATOR);
    }


    protected HttpHeaders getAuthenticationDetails(ConsumedDestinationModel destinationModel) throws OutboundServiceException
    {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.add(X_CSRF_TOKEN, X_CSRF_TOKEN_FETCH);
        HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(httpHeader);
        HttpHeaders httpRequestHeader = null;
        String csrfTokenUrl = getCsrfURL(destinationModel);
        LOG.debug("CSRF URL for backend call : {} ", csrfTokenUrl);
        ResponseEntity<String> csrfTokenExchange = null;
        final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);
        try
        {
            LOG.debug("Fetching the CSRF token");
            csrfTokenExchange = restOperations.exchange(csrfTokenUrl, HttpMethod.GET,
                            requestEntity, String.class);
            LOG.debug("Forming header with csrf token and the cookie");
            httpRequestHeader = createHTTPHeader(csrfTokenExchange.getHeaders());
            LOG.debug("Passing header details to set it in session");
            saveHttpHeaderInSession(getCurrentLanguage() + destinationModel.getId(), httpRequestHeader);
        }
        catch(final HttpClientErrorException e)
        {
            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED)
            {
                LOG.error(CREDENTIAL_ERROR_MESSAGE);
                throw new OutboundServiceException(Localization.getLocalizedString("message.error.invalidcredentials"), e);
            }
            else
            {
                LOG.error(ERROR_MESSAGE);
                throw new OutboundServiceException(Localization.getLocalizedString(ERROR_MESSAGE_LOCALE), e);
            }
        }
        catch(final ResourceAccessException | OutboundRequestExecutionException e)
        {
            LOG.error(ERROR_MESSAGE);
            throw new OutboundServiceException(Localization.getLocalizedString(ERROR_MESSAGE_LOCALE), e);
        }
        catch(final RestClientException e)
        {
            LOG.error(REST_CLIENT_ERROR);
            throw new OutboundServiceException(Localization.getLocalizedString(REST_CLIENT_ERROR_LOCALE), e);
        }
        return httpRequestHeader;
    }


    protected String getURL(ConsumedDestinationModel destinationModel, Map<String, List<FilterData>> filters)
    {
        String url = destinationModel.getUrl();
        if(destinationModel.getAdditionalProperties().get(ENTITY) != null)
        {
            url = url + destinationModel.getAdditionalProperties().get(ENTITY);
        }
        else if(S4_ORDER_DESTINATION.equals(destinationModel.getId()))
        {
            url = url + A_SALES_ORDER;
        }
        else if(S4_ORDER_SIMULATE_DESTINATION.equals(destinationModel.getId()))
        {
            url = url + A_SALES_ORDER_SIMULATION;
        }
        if(destinationModel.getAdditionalProperties().get(SAP_CLIENT) != null)
        {
            url = url + QUERY_SAP_CLIENT + destinationModel.getAdditionalProperties().get(SAP_CLIENT);
        }
        if(!filters.isEmpty())
        {
            if(!url.contains(QUERY_START))
            {
                url = url + QUERY_START;
            }
            StringBuilder filterURL = new StringBuilder();
            for(Entry<String, List<FilterData>> entry : filters.entrySet())
            {
                filterURL.append(QUERY_PARAM_SEPARATOR).append(entry.getKey()).append(getFilterString(entry.getValue()));
            }
            url = url + filterURL;
        }
        return url;
    }


    private String getFilterString(List<FilterData> filters)
    {
        StringBuilder filter = new StringBuilder();
        for(FilterData filterData : filters)
        {
            if(filterData.getKey() != null)
            {
                filter.append(filterData.getKey());
                if(filterData.getOperator() != null)
                {
                    filter.append(filterData.getOperator());
                }
                if(filterData.getValue() != null)
                {
                    filter.append(filterData.getValue());
                }
                if(filterData.getSeparator() != null)
                {
                    filter.append(filterData.getSeparator());
                }
            }
        }
        return filter.toString();
    }


    protected String getCsrfURL(ConsumedDestinationModel destinationModel)
    {
        String csrfUrl;
        if(destinationModel.getAdditionalProperties().get(CSRF_URL) != null)
        {
            csrfUrl = destinationModel.getAdditionalProperties().get(CSRF_URL);
        }
        else
        {
            csrfUrl = destinationModel.getUrl() + METADATA;
        }
        if(destinationModel.getAdditionalProperties().get(SAP_CLIENT) != null)
        {
            csrfUrl = csrfUrl + QUERY_SAP_CLIENT + destinationModel.getAdditionalProperties().get(SAP_CLIENT);
        }
        return csrfUrl;
    }


    protected void saveHttpHeaderInSession(String tokenKey, HttpHeaders header)
    {
        LOG.debug("Setting header into the session.");
        getSessionService().getCurrentSession().setAttribute(tokenKey, header);
    }


    protected HttpHeaders getHttpHeaderFromSession(String tokenKey)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        if(getSessionService().getCurrentSession().getAttribute(tokenKey) != null)
        {
            LOG.debug("Found header for token key : {}", tokenKey);
            Map<String, List<String>> headersMap = new HashMap<>(getSessionService().getCurrentSession().getAttribute(tokenKey));
            headersMap.entrySet().forEach(header -> {
                List<String> values = header.getValue();
                String value = !CollectionUtils.isEmpty(values) ? values.get(0) : "";
                httpHeaders.add(header.getKey(), value);
            });
        }
        return httpHeaders;
    }


    protected String getConfig(final String property)
    {
        return Config.getParameter(property);
    }


    protected DestinationService<ConsumedDestinationModel> getDestinationService()
    {
        return destinationService;
    }


    public void setDestinationService(DestinationService<ConsumedDestinationModel> destinationService)
    {
        this.destinationService = destinationService;
    }


    protected SessionService getSessionService()
    {
        return sessionService;
    }


    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public RestTemplate getS4omRestTemplate()
    {
        return s4omRestTemplate;
    }


    public void setS4omRestTemplate(RestTemplate s4omRestTemplate)
    {
        this.s4omRestTemplate = s4omRestTemplate;
    }


    public List<SAPS4OMOutboundRequestDecorator> getOutboundRequestDecorator()
    {
        return outboundRequestDecorator;
    }


    public void setOutboundRequestDecorator(List<SAPS4OMOutboundRequestDecorator> outboundRequestDecorator)
    {
        this.outboundRequestDecorator = outboundRequestDecorator;
    }


    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public IntegrationRestTemplateFactory getIntegrationRestTemplateFactory()
    {
        return integrationRestTemplateFactory;
    }


    public void setIntegrationRestTemplateFactory(IntegrationRestTemplateFactory integrationRestTemplateFactory)
    {
        this.integrationRestTemplateFactory = integrationRestTemplateFactory;
    }
}
