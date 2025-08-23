package io.github.orionlibs.ecommerce.lifecycle;

import io.github.orionlibs.ecommerce.lifecycle.model.LifecycleDefinition;
import io.github.orionlibs.ecommerce.lifecycle.model.StateTransition;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StateTransitionFinder
{
    public Optional<StateTransition> findTransition(LifecycleDefinition definition, String fromState)
    {
        return definition.transitions()
                        .stream()
                        .filter(t -> t.from().equals(fromState))
                        .findFirst();
    }


    public Optional<StateTransition> findTransition(LifecycleDefinition definition, String fromState, String toState)
    {
        return definition.transitions()
                        .stream()
                        .filter(t -> t.from().equals(fromState) && t.to().equals(toState))
                        .findFirst();
    }
}
