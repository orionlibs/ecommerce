package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public Process createProcess()
    {
        return new Process();
    }


    public ContextParameter createContextParameter()
    {
        return new ContextParameter();
    }


    public Action createAction()
    {
        return new Action();
    }


    public ScriptAction createScriptAction()
    {
        return new ScriptAction();
    }


    public Split createSplit()
    {
        return new Split();
    }


    public Wait createWait()
    {
        return new Wait();
    }


    public End createEnd()
    {
        return new End();
    }


    public Join createJoin()
    {
        return new Join();
    }


    public Notify createNotify()
    {
        return new Notify();
    }


    public Case createCase()
    {
        return new Case();
    }


    public Script createScript()
    {
        return new Script();
    }


    public Transition createTransition()
    {
        return new Transition();
    }


    public Parameter createParameter()
    {
        return new Parameter();
    }


    public Choice createChoice()
    {
        return new Choice();
    }


    public TargetNode createTargetNode()
    {
        return new TargetNode();
    }


    public UserGroupType createUserGroupType()
    {
        return new UserGroupType();
    }


    public Timeout createTimeout()
    {
        return new Timeout();
    }


    public Localizedmessage createLocalizedmessage()
    {
        return new Localizedmessage();
    }
}
