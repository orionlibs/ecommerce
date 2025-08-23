/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.instant;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

/**
 * An renderer for label representation in InstantEditor that may be customized depending on value type. Renderer
 * follows rendering request to different subsequent renderers depending on type of value to be rendered.
 */
public class CustomizedInstantEditorLabelsRenderer extends DefaultInstantEditorLabelRenderer
{
    /**
     * Mappings types of value to be rendered and renderers to be used for them. Key is interpreted as a regular expression
     * that a type needs to match.
     *
     * @see com.hybris.cockpitng.editors.EditorUtils#getEditorType(DataType)
     */
    private Map<String, CockpitEditorRenderer<Object>> renderers;


    @Override
    public void render(final Component parent, final EditorContext<Object> context, final EditorListener<Object> listener)
    {
        final Optional<CockpitEditorRenderer<Object>> typeRenderer = resolveRenderer(context.getValueType());
        if(typeRenderer.isPresent())
        {
            typeRenderer.get().render(parent, context, listener);
        }
        else
        {
            super.render(parent, context, listener);
        }
    }


    protected Optional<CockpitEditorRenderer<Object>> resolveRenderer(final String type)
    {
        return renderers.entrySet().stream().filter(entry -> type.matches(entry.getKey())).map(Map.Entry::getValue).findFirst();
    }


    protected Map<String, CockpitEditorRenderer<Object>> getRenderers()
    {
        return Collections.unmodifiableMap(renderers);
    }


    @Required
    public void setRenderers(final Map<String, CockpitEditorRenderer<Object>> renderers)
    {
        this.renderers = new LinkedHashMap<>(renderers);
    }
}
