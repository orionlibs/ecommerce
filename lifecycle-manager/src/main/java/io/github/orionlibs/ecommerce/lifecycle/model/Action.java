package io.github.orionlibs.ecommerce.lifecycle.model;

import java.util.Map;

public record Action(String type, Map<String, String> properties)
{
}
