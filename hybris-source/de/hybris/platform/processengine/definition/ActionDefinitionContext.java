package de.hybris.platform.processengine.definition;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ActionDefinitionContext implements Serializable
{
    private final Map<String, String> transitions;
    private final Map<String, String> parameters;


    private ActionDefinitionContext(Builder builder)
    {
        this.parameters = Collections.unmodifiableMap(getMapOrEmpty(builder.parameters));
        this.transitions = Collections.unmodifiableMap(getMapOrEmpty(builder.transitions));
    }


    public final Optional<String> getParameter(String parameterName)
    {
        return Optional.ofNullable(this.parameters.get(parameterName));
    }


    public Map<String, String> getTransitions()
    {
        return this.transitions;
    }


    public Map<String, String> getParameters()
    {
        return this.parameters;
    }


    private Map<String, String> getMapOrEmpty(Map<String, String> map)
    {
        return (map == null) ? Collections.<String, String>emptyMap() : map;
    }


    static Builder builder()
    {
        return new Builder();
    }
}
