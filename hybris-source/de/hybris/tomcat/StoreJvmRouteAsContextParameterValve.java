package de.hybris.tomcat;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.catalina.Container;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

public class StoreJvmRouteAsContextParameterValve extends ValveBase
{
    private static final String JVM_ROUTE_KEY = "de.hybris.tomcat.jvmRoute";


    public StoreJvmRouteAsContextParameterValve()
    {
        super(true);
    }


    public void invoke(Request request, Response response) throws IOException, ServletException
    {
        if(request.getContext() != null)
        {
            ServletContext ctx = request.getServletContext();
            if(ctx.getAttribute("de.hybris.tomcat.jvmRoute") == null)
            {
                String jvmRoute = getJvmRoute(request);
                if(jvmRoute != null)
                {
                    ctx.setAttribute("de.hybris.tomcat.jvmRoute", jvmRoute);
                }
            }
        }
        getNext().invoke(request, response);
    }


    private String getJvmRoute(Request request)
    {
        Engine engine = getEngine(request);
        Objects.requireNonNull(engine, "Engine cannot be null");
        String jvmRoute = engine.getJvmRoute();
        if(jvmRoute == null || jvmRoute.trim().isEmpty())
        {
            return null;
        }
        return jvmRoute;
    }


    private Engine getEngine(Request request)
    {
        for(Host host = request.getHost(); host != null; container = host.getParent())
        {
            Container container;
            if(host instanceof Engine)
            {
                return (Engine)host;
            }
        }
        return null;
    }
}
