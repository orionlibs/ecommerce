package de.hybris.platform.servicelayer.internal.converter.impl;

import de.hybris.platform.servicelayer.internal.converter.ModelConverter;

public interface TypeSystemAwareModelConverter extends ModelConverter
{
    void typeSystemChanged();
}
