package de.hybris.platform.commons.translator.parsers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import de.hybris.platform.commons.translator.nodes.SimpleNode;
import de.hybris.platform.commons.translator.nodes.TableNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class HtmlTableParser extends HtmlSimpleParser
{
    private static final String ROW_NODE_NAME = "rowNode";
    private static final String CELL_NODE_NAME = "cellNode";
    private static final Logger LOG = Logger.getLogger(HtmlTableParser.class);
    private int cols;
    private int rows;
    public static final String COLSPAN = "colspan";
    public static final String ROWSPAN = "rowspan";
    public static final String HEADERS = "headers";
    public static final String BODY = "body";
    public static final String FOOTERS = "footers";
    private static final String ROW_START_EXPRESSION = "(<\\s*tr(\\s([^>])*)?/?>)";
    private static final String CELL_START_EXPRESSION = "(<\\s*t(d|h)(\\s([^>])*)?/?>)";
    private static final String ROW_END_EXPRESSION = "</\\s*tr\\s*>";
    private static final String CELL_END_EXPRESSION = "</\\s*t(d|h)\\s*>";
    private static final String TABLE_HEADER_START_EXPRESSION = "(<\\s*thead(\\s([^>])*)?>)";
    private static final String TABLE_HEADER_END_EXPRESSION = "</\\s*thead\\s*>";
    private static final String TABLE_BODY_START_EXPRESSION = "(<\\s*tbody(\\s([^>])*)?>)";
    private static final String TABLE_BODY_END_EXPRESSION = "</\\s*tbody\\s*>";
    private static final String TABLE_FOOTER_START_EXPRESSION = "(<\\s*tfoot(\\s([^>])*)?>)";
    private static final String TABLE_FOOTER_END_EXPRESSION = "</\\s*tfoot\\s*>";


    public HtmlTableParser()
    {
    }


    public HtmlTableParser(String name, String start, String end)
    {
        super(name, start, end);
    }


    public AbstractNode createNode(String start, String end, String text, Translator translator)
    {
        TableNode tableNode = new TableNode(this.name, text);
        tableNode.setAttributes(generateAttributes(start));
        int position = 1;
        int headers = 0;
        int body = 0;
        int footers = 0;
        Pattern firstPattern = Pattern.compile("(<\\s*tr(\\s([^>])*)?/?>)|(<\\s*thead(\\s([^>])*)?>)|(<\\s*tbody(\\s([^>])*)?>)|(<\\s*tfoot(\\s([^>])*)?>)", 42);
        Matcher matcher1 = firstPattern.matcher(text);
        while(matcher1.find())
        {
            Pattern rowPattern = Pattern.compile("(<\\s*tr(\\s([^>])*)?/?>)", 42);
            Matcher rowMatcher = rowPattern.matcher(text.substring(matcher1.start(), matcher1.end()));
            if(rowMatcher.matches())
            {
                switch(position)
                {
                    case 0:
                        headers++;
                        break;
                    case 1:
                        body++;
                        break;
                    case 2:
                        footers++;
                        break;
                }
            }
            Pattern headersPattern = Pattern.compile("(<\\s*thead(\\s([^>])*)?>)", 42);
            Matcher headersMatcher = headersPattern.matcher(text.substring(matcher1.start(), matcher1.end()));
            if(headersMatcher.matches())
            {
                position = 0;
            }
            Pattern bodyPattern = Pattern.compile("(<\\s*tbody(\\s([^>])*)?>)", 42);
            Matcher bodyMatcher = bodyPattern.matcher(text.substring(matcher1.start(), matcher1.end()));
            if(bodyMatcher.matches())
            {
                position = 1;
            }
            Pattern footerPattern = Pattern.compile("(<\\s*tfoot(\\s([^>])*)?>)", 42);
            Matcher footerMatcher = footerPattern.matcher(text.substring(matcher1.start(), matcher1.end()));
            if(footerMatcher.matches())
            {
                position = 2;
            }
        }
        tableNode.getAttributes().put("headers", String.valueOf(headers));
        tableNode.getAttributes().put("body", String.valueOf(body));
        tableNode.getAttributes().put("footers", String.valueOf(footers));
        Pattern removePattern = Pattern.compile("</\\s*tr\\s*>|</\\s*t(d|h)\\s*>(<\\s*thead(\\s([^>])*)?>)|</\\s*thead\\s*>|(<\\s*tbody(\\s([^>])*)?>)|</\\s*tbody\\s*>|(<\\s*tfoot(\\s([^>])*)?>)|</\\s*tfoot\\s*>", 42);
        Matcher removeMatcher = removePattern.matcher(text);
        while(removeMatcher.find())
        {
            text = text.substring(0, removeMatcher.start()) + text.substring(0, removeMatcher.start());
            removePattern = Pattern.compile("</\\s*tr\\s*>|</\\s*t(d|h)\\s*>|(<\\s*thead(\\s([^>])*)?>)|</\\s*thead\\s*>|(<\\s*tbody(\\s([^>])*)?>)|</\\s*tbody\\s*>|(<\\s*tfoot(\\s([^>])*)?>)|</\\s*tfoot\\s*>", 42);
            removeMatcher = removePattern.matcher(text);
        }
        Pattern pattern = Pattern.compile("(<\\s*tr(\\s([^>])*)?/?>)|(<\\s*t(d|h)(\\s([^>])*)?/?>)", 42);
        Matcher matcher = pattern.matcher(text);
        this.rows = 0;
        this.cols = 0;
        int colsInCurrentLine = 0;
        while(matcher.find())
        {
            Pattern rowPattern = Pattern.compile("(<\\s*tr(\\s([^>])*)?/?>)", 42);
            Matcher rowMatcher = rowPattern.matcher(text.substring(matcher.start(), matcher.end()));
            if(rowMatcher.matches())
            {
                this.rows++;
                colsInCurrentLine = 0;
                SimpleNode rowNode = new SimpleNode("rowNode", null);
                String rowStartTag = text.substring(matcher.start(), matcher.end());
                rowNode.setAttributes(generateAttributes(rowStartTag));
                tableNode.addChildNode((AbstractNode)rowNode);
            }
            else
            {
                Matcher endMatcher = pattern.matcher(text.substring(matcher.end()));
                String cellContent = "";
                if(endMatcher.find())
                {
                    cellContent = text.substring(matcher.end(), matcher.end() + endMatcher.start());
                }
                else
                {
                    cellContent = text.substring(matcher.end());
                }
                SimpleNode tableCellNode = new SimpleNode("cellNode", cellContent);
                tableNode.addChildNode((AbstractNode)tableCellNode);
                String cellStartTag = text.substring(matcher.start(), matcher.end());
                tableCellNode.setAttributes(generateAttributes(cellStartTag));
                tableCellNode.addChildNodes(translator.parseText(cellContent));
                int colspan = 1;
                try
                {
                    if(tableCellNode.getAttributes().get("colspan") == null)
                    {
                        tableCellNode.getAttributes().put("colspan", "1");
                    }
                    colspan = Integer.parseInt((String)tableCellNode.getAttributes().get("colspan"));
                    tableCellNode.getAttributes().put("colspan", String.valueOf(colspan));
                }
                catch(NumberFormatException ex)
                {
                    tableCellNode.getAttributes().put("colspan", "1");
                }
                int rowspan = 1;
                try
                {
                    if(tableCellNode.getAttributes().get("rowspan") == null)
                    {
                        tableCellNode.getAttributes().put("rowspan", "1");
                    }
                    rowspan = Integer.parseInt((String)tableCellNode.getAttributes().get("rowspan"));
                    tableCellNode.getAttributes().put("rowspan", String.valueOf(rowspan));
                }
                catch(NumberFormatException ex)
                {
                    tableCellNode.getAttributes().put("rowspan", "1");
                }
                colsInCurrentLine += colspan;
            }
            if(colsInCurrentLine > this.cols)
            {
                this.cols = colsInCurrentLine;
            }
        }
        int maxColumns = getMaxColumnsForRow(tableNode);
        if(this.cols < maxColumns)
        {
            this.cols = maxColumns;
        }
        tableNode.initializeNodesTable(this.cols + 1, this.rows);
        int colNumber = 1;
        int rowNumber = 0;
        for(AbstractNode child : tableNode.getChildNodes())
        {
            if("cellNode".equals(child.getNodeName()))
            {
                while(tableNode.getCell(colNumber, rowNumber) != null)
                {
                    colNumber++;
                }
                boolean first = true;
                int cellcolspan = 1;
                int cellrowspan = 1;
                try
                {
                    cellcolspan = Integer.parseInt((String)child.getAttributes().get("colspan"));
                }
                catch(Exception ex)
                {
                    LOG.debug(ex.getMessage(), ex);
                }
                try
                {
                    cellrowspan = Integer.parseInt((String)child.getAttributes().get("rowspan"));
                }
                catch(Exception ex)
                {
                    LOG.debug(ex.getMessage(), ex);
                }
                for(int i = 0; i < cellcolspan; i++)
                {
                    for(int j = 0; j < cellrowspan; j++)
                    {
                        if(first)
                        {
                            tableNode.setCell(child, colNumber + i, rowNumber + j);
                            first = false;
                        }
                        else
                        {
                            Map<String, String> attributes = new HashMap<>();
                            attributes.put("rowspan", "1");
                            attributes.put("colspan", "1");
                            SimpleNode emptyCell = new SimpleNode("cellNode", "", attributes);
                            tableNode.setCell((AbstractNode)emptyCell, colNumber + i, rowNumber + j);
                        }
                    }
                }
                colNumber++;
                continue;
            }
            rowNumber++;
            tableNode.setCell(child, 0, rowNumber);
            colNumber = 1;
        }
        return (AbstractNode)tableNode;
    }


    private int getMaxColumnsForRow(TableNode tableNode)
    {
        Stack<Integer> colSpanAffectingNextRows = new Stack<>();
        int maxColumnsInRow = 0, localMaxColumns = 0;
        for(AbstractNode node : tableNode.getChildNodes())
        {
            if("rowNode".compareToIgnoreCase(node.getNodeName()) == 0)
            {
                maxColumnsInRow = Math.max(maxColumnsInRow, localMaxColumns);
                localMaxColumns = 0;
                if(!colSpanAffectingNextRows.isEmpty())
                {
                    localMaxColumns += ((Integer)colSpanAffectingNextRows.pop()).intValue();
                }
            }
            if("cellNode".compareToIgnoreCase(node.getNodeName()) == 0)
            {
                int colSpan = (node.getAttributes().get("colspan") != null) ? Integer.parseInt((String)node.getAttributes().get("colspan")) : 1;
                if(colSpan > 1 && node.getAttributes().get("rowspan") != null)
                {
                    int rowSpan = Integer.parseInt((String)node.getAttributes().get("rowspan"));
                    for(int i = 0; i < rowSpan - 1; i++)
                    {
                        int affectingColSpan = 0;
                        if(i < colSpanAffectingNextRows.size())
                        {
                            affectingColSpan = ((Integer)colSpanAffectingNextRows.get(i)).intValue();
                            affectingColSpan += colSpan;
                            colSpanAffectingNextRows.set(i, Integer.valueOf(affectingColSpan));
                        }
                        else if(colSpanAffectingNextRows.isEmpty())
                        {
                            colSpanAffectingNextRows.push(Integer.valueOf(colSpan));
                        }
                        else
                        {
                            colSpanAffectingNextRows.add(0,
                                            Integer.valueOf(colSpan));
                        }
                    }
                }
                localMaxColumns += colSpan;
            }
        }
        maxColumnsInRow = Math.max(maxColumnsInRow, localMaxColumns);
        return maxColumnsInRow;
    }
}
