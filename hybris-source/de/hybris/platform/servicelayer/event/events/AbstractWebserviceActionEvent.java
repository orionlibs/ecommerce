package de.hybris.platform.servicelayer.event.events;

import java.net.URI;
import java.security.Principal;

public abstract class AbstractWebserviceActionEvent<RESOURCE> extends AbstractEvent
{
    private final RESOURCE wrapperResource;
    private final Object wrapperParentResource;
    private final Principal principal;
    private final String resourceId;
    private final URI uri;


    public AbstractWebserviceActionEvent(String resourceId, RESOURCE resource, Object parentResource, Principal pricipal, URI uri)
    {
        this.wrapperResource = resource;
        this.wrapperParentResource = parentResource;
        this.principal = pricipal;
        this.resourceId = resourceId;
        this.uri = uri;
    }


    public abstract CRUD_METHOD getCrudMethod();


    public abstract TRIGGER getTriggered();


    public RESOURCE getWrapperResource()
    {
        return this.wrapperResource;
    }


    public Object getWrapperParentResource()
    {
        return this.wrapperParentResource;
    }


    public Principal getPrincipal()
    {
        return this.principal;
    }


    public String getResourceId()
    {
        return this.resourceId;
    }


    public URI getUri()
    {
        return this.uri;
    }
}
