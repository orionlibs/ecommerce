package de.hybris.platform.persistence.polyglot.model;

import de.hybris.platform.persistence.polyglot.view.ItemStateView;

public interface ItemState extends ItemStateView
{
    ChangeSet beginModification();
}
