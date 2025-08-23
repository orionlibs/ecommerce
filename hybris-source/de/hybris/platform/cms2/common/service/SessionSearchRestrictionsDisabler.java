package de.hybris.platform.cms2.common.service;

import java.util.function.Supplier;

public interface SessionSearchRestrictionsDisabler
{
    <T> T execute(Supplier<T> paramSupplier);
}
