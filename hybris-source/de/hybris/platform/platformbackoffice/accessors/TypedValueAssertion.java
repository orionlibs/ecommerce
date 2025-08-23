package de.hybris.platform.platformbackoffice.accessors;

import org.springframework.expression.TypedValue;

interface TypedValueAssertion
{
    void doAssertions(TypedValue paramTypedValue);
}
