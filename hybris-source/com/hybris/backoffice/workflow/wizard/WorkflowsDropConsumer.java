/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.wizard;

import com.hybris.backoffice.cockpitng.dnd.DropConsumer;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.function.Consumer;

/**
 * Allows to open collaboration wizard with dropped items.
 */
public class WorkflowsDropConsumer implements DropConsumer<ItemModel>
{
    private final Consumer<List<ItemModel>> wizardOpener;


    public WorkflowsDropConsumer(final Consumer<List<ItemModel>> wizardOpener)
    {
        this.wizardOpener = wizardOpener;
    }


    @Override
    public void itemsDropped(final List<ItemModel> droppedItems)
    {
        if(wizardOpener != null)
        {
            wizardOpener.accept(droppedItems);
        }
    }
}
