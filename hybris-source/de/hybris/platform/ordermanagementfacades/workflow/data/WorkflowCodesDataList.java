package de.hybris.platform.ordermanagementfacades.workflow.data;

import java.io.Serializable;
import java.util.List;

public class WorkflowCodesDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<String> codes;


    public void setCodes(List<String> codes)
    {
        this.codes = codes;
    }


    public List<String> getCodes()
    {
        return this.codes;
    }
}
