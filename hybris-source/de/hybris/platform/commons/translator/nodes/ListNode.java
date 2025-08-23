package de.hybris.platform.commons.translator.nodes;

public class ListNode extends AbstractNode
{
    private AbstractNode[] nodesList;
    private int rows;


    public ListNode(String nodeName, String nodeText)
    {
        super(nodeName, nodeText);
    }


    public void initializeNodesList(int numberOfRows)
    {
        if(this.nodesList == null)
        {
            this.nodesList = new AbstractNode[numberOfRows];
            this.rows = numberOfRows;
        }
    }


    public void setElement(AbstractNode row, int x_Position)
    {
        this.nodesList[x_Position] = row;
    }


    public AbstractNode getElement(int atPosition)
    {
        return this.nodesList[atPosition];
    }


    public int getRows()
    {
        return this.rows;
    }
}
