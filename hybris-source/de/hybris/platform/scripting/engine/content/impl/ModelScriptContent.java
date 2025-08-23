package de.hybris.platform.scripting.engine.content.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.content.AutoDisablingScriptContent;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.util.Collections;
import java.util.Map;

public class ModelScriptContent implements AutoDisablingScriptContent
{
    private final ScriptModel model;
    private final AutoDisablingScriptStrategy strategy;


    public ModelScriptContent(ScriptModel model)
    {
        this(model, null);
    }


    public ModelScriptContent(ScriptModel model, AutoDisablingScriptStrategy strategy)
    {
        Preconditions.checkNotNull(model, "model is required");
        Preconditions.checkState(!ModelContextUtils.getItemModelContext((AbstractItemModel)model).isNew(), "script model must be persisted");
        this.model = model;
        this.strategy = strategy;
    }


    public String getEngineName()
    {
        return this.model.getScriptType().getCode().toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public String getContent()
    {
        return Strings.nullToEmpty(this.model.getContent());
    }


    public Map<String, Object> getCustomContext()
    {
        return Collections.emptyMap();
    }


    public String toString()
    {
        return "ModelScriptContent{model=" + this.model + "}";
    }


    public AutoDisablingScriptStrategy getAutoDisablingScriptStrategy()
    {
        return this.strategy;
    }
}
