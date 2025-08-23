package de.hybris.platform.cmscockpit.session.script.components;

import de.hybris.platform.cmscockpit.session.script.ScriptGroup;
import de.hybris.platform.cmscockpit.session.script.config.ScriptFragment;
import org.zkoss.lang.Objects;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.Script;

public class GroupScript extends Script
{
    protected String _group;


    public String getGroup()
    {
        return this._group;
    }


    public void setGroup(String group)
    {
        if(group == null || group.length() == 0)
        {
            throw new IllegalArgumentException("group cannot be null or 0 length");
        }
        if(!Objects.equals(this._group, group))
        {
            this._group = group;
            ScriptGroup type = ScriptGroup.valueOf(group);
            if(type == null)
            {
                throw new IllegalArgumentException(group + " is not a valid ExtendedScriptGroup group");
            }
            setContent(getScriptWithGroup(type));
        }
    }


    protected String getScriptWithGroup(ScriptGroup group)
    {
        StringBuilder script = new StringBuilder();
        for(ScriptFragment fragment : SpringUtil.getApplicationContext().getBeansOfType(ScriptFragment.class)
                        .values())
        {
            if(fragment.getGroup().equals(group))
            {
                script.append(fragment.getScript());
                script.append("\n");
            }
        }
        return script.toString();
    }
}
