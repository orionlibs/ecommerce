package de.hybris.platform.cmscockpit.session.script.config.impl;

import de.hybris.platform.cmscockpit.session.script.ScriptGroup;
import de.hybris.platform.cmscockpit.session.script.config.ScriptFragment;
import org.springframework.beans.factory.annotation.Required;

public class SingleJavaScriptFile implements ScriptFragment
{
    protected ScriptGroup group;
    protected String script;


    public String getScript()
    {
        return this.script;
    }


    public ScriptGroup getGroup()
    {
        return this.group;
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
}
