package de.hybris.platform.util;

import org.apache.log4j.Logger;

class CorePlusUtilities
{
    private static final Logger LOG = Logger.getLogger(CorePlusUtilities.class);
    public static final String COREPLUS_PROJECT_DEPLOYMENT_MODE_SUFFIX = ".deployment.mode";
    public static final String DEFAULT_LOCALHOST = "http://localhost:";
    public static final String DEFAULT_INIT_APP_REST_REST = "/init-app-rest/rest/";
    public static final String EMBEDDED_MODE = "embedded";
    private final CoreUtilities coreUtilities;


    CorePlusUtilities(CoreUtilities coreUtilities)
    {
        this.coreUtilities = coreUtilities;
    }


    String calculateLocalCorePlusEndPoint(String extensionName, String webroot) throws IllegalArgumentException
    {
        assertLocalEndPoint(extensionName);
        return calculateLocalEndPointIfNeeded(webroot);
    }


    private void assertLocalEndPoint(String platformExtensionName) throws IllegalArgumentException
    {
        String mode = this.coreUtilities.getConfigProperty(platformExtensionName + ".deployment.mode");
        if(!"embedded".equalsIgnoreCase(mode))
        {
            throw new IllegalArgumentException("Expected embedded  but found <" + mode + ">  for extension <" + platformExtensionName + ">. Local end point can be generated only in embedded, otherwise provide explicit end point.");
        }
    }


    private String calculateLocalEndPointIfNeeded(String webRoot)
    {
        String port = this.coreUtilities.getConfigProperty("tomcat.http.port");
        StringBuilder endpointBuilder = new StringBuilder("http://localhost:");
        endpointBuilder.append(port);
        endpointBuilder.append(webRoot);
        endpointBuilder.append("/init-app-rest/rest/");
        return endpointBuilder.toString();
    }
}
