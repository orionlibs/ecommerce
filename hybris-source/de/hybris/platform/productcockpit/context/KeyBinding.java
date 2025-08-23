package de.hybris.platform.productcockpit.context;

import org.zkoss.util.resource.Labels;

public class KeyBinding
{
    public static final int SHIFT = 1;
    public static final int CTRL = 2;
    public static final int ALT = 4;
    private final String controlKey;
    private final String name;
    private final int keyCode;
    private final int modifiers;
    private final String keyTxt;


    public KeyBinding(String ctrl, String name, int keyCode, int modifiers, String keyTxt)
    {
        this.controlKey = ctrl;
        this.name = name;
        this.keyCode = keyCode;
        this.modifiers = modifiers & 0x7;
        this.keyTxt = keyTxt;
    }


    public int getKeyCode()
    {
        return this.keyCode;
    }


    public String getControlKey()
    {
        return this.controlKey;
    }


    public String getName()
    {
        return this.name;
    }


    public int getModifiers()
    {
        return this.modifiers;
    }


    public boolean isShift()
    {
        return ((getModifiers() & 0x1) == 1);
    }


    public boolean isCtrl()
    {
        return ((getModifiers() & 0x2) == 2);
    }


    public boolean isAlt()
    {
        return ((getModifiers() & 0x4) == 4);
    }


    public String getKeyText()
    {
        return this.keyTxt.startsWith("L_") ? Labels.getLabel(this.keyTxt) : this.keyTxt;
    }


    public String toString()
    {
        return getName() + "(" + getName() + "," + getControlKey() + "," + getKeyCode() + "," + getModifiers() + ")";
    }
}
