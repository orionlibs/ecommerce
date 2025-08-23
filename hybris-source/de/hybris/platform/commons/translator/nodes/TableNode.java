package de.hybris.platform.commons.translator.nodes;

public class TableNode extends AbstractNode
{
    private AbstractNode[][] nodesTable;
    private int cols;
    private int rows;


    public TableNode(String nodeName, String nodeText)
    {
        super(nodeName, nodeText);
    }


    public void initializeNodesTable(int colCount, int rowCount)
    {
        if(this.nodesTable == null)
        {
            this.nodesTable = new AbstractNode[colCount][rowCount];
            this.cols = colCount;
            this.rows = rowCount;
        }
    }


    public void setCell(AbstractNode cell, int colNumber, int rowNumber)
    {
        this.nodesTable[colNumber][rowNumber - 1] = cell;
    }


    public AbstractNode getCell(int colNumber, int rowNumber)
    {
        return this.nodesTable[colNumber][rowNumber - 1];
    }


    public int getCols()
    {
        return this.cols - 1;
    }


    public int getRows()
    {
        return this.rows;
    }
}
