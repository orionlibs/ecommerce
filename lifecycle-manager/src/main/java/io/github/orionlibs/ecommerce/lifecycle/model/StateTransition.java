package io.github.orionlibs.ecommerce.lifecycle.model;

import java.util.List;

public record StateTransition(String from,
                              String to,
                              List<Guard> guards,
                              List<Action> actions)
{
}
