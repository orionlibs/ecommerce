package de.hybris.platform.webservicescommons.testsupport.client;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.core.Response;
import org.junit.Assert;

public final class WebservicesAssert
{
    public static final Map<String, String> SECURED_HEADERS;

    static
    {
        Map<String, String> localSecuredHeaders = new HashMap<>();
        localSecuredHeaders.put("X-FRAME-Options", "SAMEORIGIN");
        localSecuredHeaders.put("X-XSS-Protection", "1; mode=block");
        localSecuredHeaders.put("X-Content-Type-Options", "nosniff");
        localSecuredHeaders.put("Strict-Transport-Security", "max-age=16070400 ; includeSubDomains");
        SECURED_HEADERS = Collections.unmodifiableMap(localSecuredHeaders);
    }

    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertOk(Response response, boolean expectEmptyBody)
    {
        assertResponseStatus(Response.Status.OK, response, expectEmptyBody);
    }


    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertCreated(Response response, boolean expectEmptyBody)
    {
        assertResponseStatus(Response.Status.CREATED, response, expectEmptyBody);
    }


    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertForbidden(Response response, boolean expectEmptyBody)
    {
        assertResponseStatus(Response.Status.FORBIDDEN, response, expectEmptyBody);
    }


    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertBadRequest(Response response, boolean expectEmptyBody)
    {
        assertResponseStatus(Response.Status.BAD_REQUEST, response, expectEmptyBody);
    }


    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertUnauthorized(Response response, boolean expectEmptyBody)
    {
        assertResponseStatus(Response.Status.UNAUTHORIZED, response, expectEmptyBody);
    }


    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertResponseStatus(Response.Status expectedStatus, Response response, boolean expectEmptyBody)
    {
        Assert.assertEquals("Wrong HTTP status at response: " + response, expectedStatus.getStatusCode(), response.getStatus());
        if(expectEmptyBody)
        {
            Assert.assertTrue("Body should be empty at response: " + response, !response.hasEntity());
        }
    }


    @Deprecated(since = "6.1", forRemoval = true)
    public static void assertResponseStatus(Response.Status expectedStatus, Response response)
    {
        assertResponseStatus(expectedStatus, response, false);
    }


    public static void assertResponse(Response.Status expectedStatus, Optional<Map<String, String>> expectedHeaders, Response response)
    {
        Assert.assertEquals("Wrong HTTP status at response: " + response, expectedStatus.getStatusCode(), response.getStatus());
        if(expectedHeaders.isPresent())
        {
            for(Map.Entry<String, String> header : (Iterable<Map.Entry<String, String>>)((Map)expectedHeaders.get()).entrySet())
            {
                Assert.assertEquals(header.getValue(), response.getHeaderString(header.getKey()));
            }
        }
    }


    public static void assertResponse(Response.Status expectedStatus, Response response)
    {
        assertResponse(expectedStatus, Optional.of(SECURED_HEADERS), response);
    }


    public static void assertForbiddenError(Response response)
    {
        assertResponse(Response.Status.FORBIDDEN, response);
        ErrorListWsDTO errors = (ErrorListWsDTO)response.readEntity(ErrorListWsDTO.class);
        Assert.assertNotNull(errors);
        Assert.assertNotNull(errors.getErrors());
        Assert.assertEquals(1L, errors.getErrors().size());
        ErrorWsDTO error1 = errors.getErrors().get(0);
        Assert.assertEquals("ForbiddenError", error1.getType());
    }


    public static void assertJSONEquals(Object expected, Object actual)
    {
        assertJSONEquals(expected, actual, "***", true);
    }


    public static void assertJSONEquals(Object expected, Object actual, boolean acceptAdditionalFields)
    {
        assertJSONEquals(expected, actual, "***", acceptAdditionalFields);
    }


    public static void assertJSONEquals(Object expected, Object actual, String wildCard, boolean acceptAdditionalFields)
    {
        if(actual instanceof Map && expected instanceof Map)
        {
            Map<?, ?> actualMap = (Map<?, ?>)actual;
            Map<?, ?> expectedMap = (Map<?, ?>)expected;
            expectedMap.forEach((key, expectedValue) -> {
                Assert.assertTrue(actualMap.containsKey(key));
                Object actualValue = actualMap.get(key);
                assertJSONEquals(expectedValue, actualValue, wildCard, acceptAdditionalFields);
            });
            if(!acceptAdditionalFields)
            {
                actualMap.keySet().forEach(k -> Assert.assertTrue("Actual json contains unexpected field: [" + k + "] ", expectedMap.keySet().contains(k)));
            }
        }
        else if(actual instanceof List && expected instanceof List)
        {
            List<?> actualList = (List)actual;
            List<?> expectedList = (List)expected;
            Assert.assertEquals(expectedList.size(), actualList.size());
            for(int i = 0; i < expectedList.size(); i++)
            {
                assertJSONEquals(expectedList.get(i), actualList.get(i), wildCard, acceptAdditionalFields);
            }
        }
        else if(!wildCard.equals(expected))
        {
            Assert.assertEquals(expected, actual);
        }
    }
}
