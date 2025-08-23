package de.hybris.platform.catalog.jalo.synccompare;

public abstract class AbstractCompareResult
{
    protected AbstractCompareResult superCompareResult = null;
    protected CompareSyncUtils.Status result = null;
    private String srcValue;
    private String trgValue;
    private String problemText = "";
    private boolean empty = true;


    public AbstractCompareResult(AbstractCompareResult superResult)
    {
        this.superCompareResult = superResult;
    }


    public String getProblemDescription()
    {
        return (this.problemText == null) ? "&nbsp;" : this.problemText;
    }


    public void setProblemDescription(String txt)
    {
        this.empty = false;
        this.problemText = this.problemText + " " + this.problemText;
    }


    public void setResult(CompareSyncUtils.Status status, String txt)
    {
        setProblemDescription(txt);
        setResult(status);
    }


    public void setResult(CompareSyncUtils.Status status)
    {
        if(status.equals(CompareSyncUtils.Status.NOT_EQUAL))
        {
            this.result = CompareSyncUtils.Status.NOT_EQUAL;
            if(this.superCompareResult != null)
            {
                this.superCompareResult.setResult(CompareSyncUtils.Status.NOT_EQUAL);
            }
        }
        else if(status.equals(CompareSyncUtils.Status.EQUAL))
        {
            this.result = CompareSyncUtils.Status.EQUAL;
            if(this.superCompareResult != null)
            {
                if(this.superCompareResult.getResult() == null ||
                                !this.superCompareResult.getResult().equals(CompareSyncUtils.Status.NOT_EQUAL))
                {
                    this.superCompareResult.setResult(CompareSyncUtils.Status.EQUAL);
                }
            }
        }
        else if(status.equals(CompareSyncUtils.Status.IGNORE))
        {
            this.result = CompareSyncUtils.Status.IGNORE;
            if(this.superCompareResult != null && this.superCompareResult.getResult() == null)
            {
                this.superCompareResult.setResult(CompareSyncUtils.Status.IGNORE);
            }
        }
    }


    public CompareSyncUtils.Status getResult()
    {
        return this.result;
    }


    public void setDifference(Object srcValue, Object trgValue)
    {
        this.empty = false;
        this.srcValue = (srcValue == null) ? null : srcValue.toString();
        this.trgValue = (trgValue == null) ? null : trgValue.toString();
    }


    public String getDifference()
    {
        if(this.srcValue == null && this.trgValue == null)
        {
            return "&nbsp;";
        }
        StringBuilder strbuil = new StringBuilder();
        strbuil.append((this.srcValue == null) ? "null" : this.srcValue);
        strbuil.append(" <-> ");
        strbuil.append((this.trgValue == null) ? "null" : this.trgValue);
        return strbuil.toString();
    }


    public boolean hasAdditionalInformations()
    {
        return !this.empty;
    }
}
