package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public abstract class AbstractClassificationCellRenderer<DATA> extends AbstractWidgetComponentRenderer<Listcell, ListColumn, DATA>
{
    static final String KEY_INLINE_CURRENT_OBJECT = "inlinecurrentObject";


    protected abstract String getValue(DATA paramDATA, WidgetInstanceManager paramWidgetInstanceManager);


    public void render(Listcell parent, ListColumn configuration, DATA model, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        Label label = new Label(getValue(model, widgetInstanceManager));
        parent.appendChild((Component)label);
        fireComponentRendered(parent, configuration, model);
    }
}
