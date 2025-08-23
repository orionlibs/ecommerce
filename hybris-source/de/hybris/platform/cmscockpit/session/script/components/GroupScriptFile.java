package de.hybris.platform.cmscockpit.session.script.components;

import de.hybris.platform.cmscockpit.session.script.ScriptGroup;
import de.hybris.platform.cmscockpit.session.script.config.impl.SingleJavaScriptFile;
import java.util.Collection;
import org.springframework.context.ApplicationContext;
import org.zkoss.lang.Objects;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zul.Script;

public class GroupScriptFile extends Script
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
            ScriptGroup type = getScriptGroupType(group);
            setSrc(getSrcFileWithGroup(type));
        }
    }


    protected ScriptGroup getScriptGroupType(String group)
    {
        if(group == null || group.length() == 0)
        {
            throw new IllegalArgumentException("group cannot be null or 0 length");
        }
        ScriptGroup type = ScriptGroup.valueOf(group);
        if(type == null)
        {
            throw new IllegalArgumentException(group + " is not a valid ExtendedScriptGroup group");
        }
        return type;
    }


    protected String getSrcFileWithGroup(ScriptGroup group)
    {
        StringBuilder script = new StringBuilder();
        Collection<SingleJavaScriptFile> JSSrc = getApplicationContext().getBeansOfType(SingleJavaScriptFile.class).values();
        if(JSSrc.size() > 1)
        {
            throw new IllegalArgumentException("" + group + " contains more than one JavaScript src file. The zk script tag's src attribute only takes one URI. So the group can only contain a single " + group);
        }
        for(SingleJavaScriptFile src : JSSrc)
        {
            if(src.getGroup().equals(group))
            {
                script.append(src.getScript());
            }
        }
        return script.toString();
    }


    protected ApplicationContext getApplicationContext()
    {
        return SpringUtil.getApplicationContext();
    }
}
