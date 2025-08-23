package de.hybris.platform.hac.data.dto;

public class DryRunData
{
    public static final int BUTTON_DROP_DDL_SCRIPT = 1;
    public static final int BUTTON_DDL_SCRIPT = 2;
    public static final int BUTTON_DML_SCRIPT = 3;
    private int button;
    private boolean initialize;
    private boolean update;


    public boolean isInitialize()
    {
        return this.initialize;
    }


    public void setInitialize(boolean initialize)
    {
        this.initialize = initialize;
    }


    public boolean isUpdate()
    {
        return this.update;
    }


    public void setUpdate(boolean update)
    {
        this.update = update;
    }


    public int getButton()
    {
        return this.button;
    }


    public void setButton(int button)
    {
        this.button = button;
    }
}
