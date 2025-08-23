/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.yprofile.rest.clients;

import com.hybris.charon.RawResponse;
import com.hybris.charon.annotations.Control;
import com.hybris.charon.annotations.Header;
import com.hybris.charon.annotations.Http;
import com.hybris.charon.annotations.OAuth;
import com.hybris.yprofile.dto.AbstractProfileEvent;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.http.HttpHeaders;
import rx.Observable;

@OAuth
@Http
public interface ProfileClient
{
    /**
     * Send tracking events, orders and users to CDS
     * @deprecated since 1808 please use ProfileTag instead
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/events")
    @Header(name = "hybris-tenant", val = "${tenant}")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    @Deprecated(since = "1808", forRemoval = true)
    Observable<ProfileResponse> sendEvent(
                    @HeaderParam("event-type") String eventType,
                    @HeaderParam("consent-reference") String consentReferenceId,
                    @HeaderParam("site") String baseSiteId,
                    @HeaderParam(HttpHeaders.USER_AGENT) String userAgent,
                    @HeaderParam(HttpHeaders.ACCEPT) String accept,
                    @HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) String acceptLanguage,
                    @HeaderParam(HttpHeaders.REFERER) String referer,
                    AbstractProfileEvent event);


    /**
     * Send transactions (order events + login & registration events) to CDS
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/events")
    @Header(name = "hybris-tenant", val = "${tenant}")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    Observable<RawResponse> sendTransaction(
                    @HeaderParam("event-type") String eventType,
                    @HeaderParam("consent-reference") String consentReferenceId,
                    @HeaderParam("site") String baseSiteId,
                    @HeaderParam("X-B3-Sampled") String tracingEnabled,
                    AbstractProfileEvent event);


    /**
     * Send slim events to CDS
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/events")
    @Header(name = "hybris-tenant", val = "${tenant}")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    Observable<RawResponse> sendSlimEvent(
                    @HeaderParam("hybris-schema") String schema,
                    @HeaderParam("consent-reference") String consentReferenceId,
                    @HeaderParam("site") String baseSiteId,
                    @HeaderParam("X-B3-Sampled") String tracingEnabled,
                    AbstractProfileEvent event);
}
