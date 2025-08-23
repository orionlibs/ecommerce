package io.github.orionlibs.ecommerce.core.lifecycle;

import java.util.List;

public record LifecycleDefinition(String key,
                                  String name,
                                  int version,
                                  List<String> states,
                                  List<StateTransition> transitions)
{
    public String getFirstState()
    {
        return states.getFirst();
    }
}
