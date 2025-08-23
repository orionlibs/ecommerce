package de.hybris.platform.cmscockpit.session.script.config.impl;

import de.hybris.platform.cmscockpit.session.script.ScriptGroup;
import de.hybris.platform.cmscockpit.session.script.config.ScriptFragment;
import org.springframework.beans.factory.annotation.Required;

public class SimpleScriptFragment implements ScriptFragment
{
    protected ScriptGroup group;
    protected String script;


    public String getScript()
    {
        return this.script;
    }


    @Required
    public void setScript(String script)
    {
        this.script = script;
    }


    @Required
    public void setGroup(ScriptGroup type)
    {
        this.group = type;
    }


    public ScriptGroup getGroup()
    {
        return this.group;
    }
}
