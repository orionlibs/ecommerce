package de.hybris.platform.webservicescommons.api.restrictions;

import java.lang.reflect.Method;

public class EndpointRestrictionsMockData
{
    public static final String VALID_METHOD_WITH_VALID_ANNOTATIONS = "validMockMethod1";
    public static final String VALID_METHOD_WITH_GET_REQUEST_MAPPING = "validMockMethod2";
    public static final String VALID_METHOD_WITH_GET_MAPPING = "validMockMethod3";
    public static final String VALID_METHOD_WITH_POST_REQUEST_MAPPING = "validMockMethod4";
    public static final String INVALID_METHOD_WITH_ONLY_API_OPERATION = "invalidMockMethod5";
    public static final String INVALID_METHOD_WITHOUT_ANY_VALID_ANNOTATION = "invalidMockMethod6";
    public static final String TEST_CONFIG_PREFIX = "configPrefix";
    public static final String TEST_NICKNAME = "testNickname";
    public static final String TEST_ANOTHER_NICKNAME = "testAnotherNickname";
    public static final String TEST_ALLOWED_NICKNAME = "allowedOperation";
    public static final String REQUEST_MAPPING_GET_OPERATION = "validMockMethod2UsingGET";
    public static final String GET_MAPPING_OPERATION = "validMockMethod3UsingGET";
    public static final String REQUEST_MAPPING_POST_OPERATION = "validMockMethod4UsingPOST";
    public static final String TEST_VALUE = "Test value";


    public static Method getMockMethod(String methodName) throws NoSuchMethodException
    {
        return MockApiController.class.getMethod(methodName, new Class[0]);
    }
}
