package de.hybris.platform.commons.translator.renderers;

import com.google.common.util.concurrent.AtomicDouble;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import de.hybris.platform.commons.translator.nodes.TableNode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class IndesignUtility
{
    private static final Logger LOG = Logger.getLogger(IndesignUtility.class.getName());
    public static final String STYLE_NAME = "styleName";
    public static final String PARASTYLE_OPEN = "parastyleOpen";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String OPEN_TAG = "openTagKey";
    public static final String CLOSE_TAG = "closeTagKey";
    public static final String BORDER = "border";
    public static final String CELLPADDING = "cellpadding";
    public static final String CELLSPACING = "cellspacing";
    public static final String WIDTH = "width";
    public static final String SURPRESSED_BORDER_WIDTH = "SurpressedBorderWidth";
    public static final float DEFAULT_SURPRESSED_BORDER_WIDTH = 1.0F;
    private static final AtomicDouble PIXEL_MULTIPLIER = new AtomicDouble(2.83464567D);
    private static float BORDER_WIDTH_FACTOR = 0.25F;
    private static NumberFormat formatter = new DecimalFormat("#.##############");
    private Properties properties;


    public IndesignUtility(Properties properties)
    {
        this.properties = properties;
        if(properties != null && properties.containsKey("PIXEL_MULTIPLIER"))
        {
            PIXEL_MULTIPLIER.set(Double.parseDouble(properties.getProperty("PIXEL_MULTIPLIER")));
        }
    }


    public Properties getProperties()
    {
        if(this.properties == null)
        {
            this.properties = new Properties();
        }
        return this.properties;
    }


    public static void initializeParaStyle(AbstractNode node, String styleName)
    {
        Map<String, String> attributes = node.getAttributes();
        attributes.put("styleName", styleName);
        attributes.put("parastyleOpen", "yes");
        AbstractNode parent = node.getParentNode();
        while(parent != null)
        {
            if(parent.getAttributes().get("styleName") != null)
            {
                parent.getAttributes().put("parastyleOpen", "no");
                break;
            }
            parent = parent.getParentNode();
        }
    }


    public static String initializeCharStyle(AbstractNode node)
    {
        AbstractNode parent = node.getParentNode();
        while(parent != null)
        {
            if(parent.getAttributes().get("styleName") != null)
            {
                if(((String)parent.getAttributes().get("parastyleOpen")).equals("no"))
                {
                    parent.getAttributes().put("parastyleOpen", "yes");
                    return (String)parent.getAttributes().get("styleName");
                }
                return "";
            }
            parent = parent.getParentNode();
        }
        return "";
    }


    public static String closeParentsTags(AbstractNode node)
    {
        return parentTags(node, true, false);
    }


    public static String closeForcedParentsTags(AbstractNode node)
    {
        return parentTags(node, false, false);
    }


    public static String openParentsTags(AbstractNode node)
    {
        return parentTags(node, true, true);
    }


    public static String openForcedParentsTags(AbstractNode node)
    {
        return parentTags(node, false, true);
    }


    protected static String parentTags(AbstractNode node, boolean checkOpenTags, boolean openClose)
    {
        AbstractNode parent = node.getParentNode();
        if(parent.getParentNode() != null)
        {
            if(openClose)
            {
                String openTag = parent.getAttributeByName("openTagKey");
                String str1 = ((parent.isOpenTag() || !checkOpenTags) && openTag != null) ? openTag : "";
                return parentTags(parent, checkOpenTags, openClose) + parentTags(parent, checkOpenTags, openClose);
            }
            String closeTag = parent.getAttributeByName("closeTagKey");
            String tag = ((parent.isOpenTag() || !checkOpenTags) && closeTag != null) ? closeTag : "";
            return tag + tag;
        }
        return "";
    }


    public static boolean parentIsMain(AbstractNode node)
    {
        if(node != null && node.getParentNode() != null && "mainNode".equals(node.getParentNode().getNodeName()))
        {
            return true;
        }
        return false;
    }


    private float getSurpressedBorderWidth()
    {
        String surpressedBorderWidth = (String)getProperties().get("SurpressedBorderWidth");
        if(surpressedBorderWidth != null)
        {
            return Float.parseFloat(surpressedBorderWidth);
        }
        return 1.0F;
    }


    public String getBorder(AbstractNode node)
    {
        if(node instanceof TableNode && node.getAttributes() != null && node.getAttributes().get("border") != null)
        {
            try
            {
                float border = Float.parseFloat((String)node.getAttributes().get("border")) * BORDER_WIDTH_FACTOR;
                if(border == getSurpressedBorderWidth())
                {
                    return "";
                }
                return "<tCellAttrLeftStrokeWeight:" + border + "><tCellAttrRightStrokeWeight:" + border + "><tCellAttrTopStrokeWeight:" + border + "><tCellAttrBottomStrokeWeight:" + border + ">";
            }
            catch(Exception ex)
            {
                LOG.error((String)node.getAttributes().get("border") + " is not a float either int!", ex);
            }
        }
        return "";
    }


    public static String getCellpadding(AbstractNode node)
    {
        if(node instanceof TableNode && node.getAttributes() != null && node.getAttributes().get("cellpadding") != null)
        {
            try
            {
                int padding = Integer.parseInt((String)node.getAttributes().get("cellpadding"));
                String paddingOneSide = formatter.format(padding / 2.0D * PIXEL_MULTIPLIER.get());
                return "<tCellAttrLeftInset:" + paddingOneSide + "><tCellAttrTopInset:" + paddingOneSide + "><tCellAttrRightInset:" + paddingOneSide + "><tCellAttrBottomInset:" + paddingOneSide + ">";
            }
            catch(Exception ex)
            {
                LOG.error((String)node.getAttributes().get("cellpadding") + " is not a number!", ex);
            }
        }
        return "";
    }


    public static String getCellspacing(AbstractNode node)
    {
        if(node instanceof TableNode && node.getAttributes() != null && node.getAttributes().get("cellspacing") != null)
        {
            try
            {
                int spacing = Integer.parseInt((String)node.getAttributes().get("cellspacing"));
                String spacingString = formatter.format(spacing * PIXEL_MULTIPLIER.get());
                return "<tBeforeSpace:" + spacingString + "><tAfterSpace:" + spacingString + ">";
            }
            catch(Exception ex)
            {
                LOG.error((String)node.getAttributes().get("cellspacing") + " is not a number!", ex);
            }
        }
        return "";
    }


    public static String getColumnWidth(AbstractNode node, int column)
    {
        if(node instanceof TableNode)
        {
            TableNode table = (TableNode)node;
            int tableWidth = getWidthFromNode((AbstractNode)table);
            int columnWidth = getWidthFromNode(table.getCell(column, 1));
            if(columnWidth < 0 && tableWidth > 0)
            {
                int notSpecifiedColumns = 0;
                int specifiedColumnsWidth = 0;
                for(int i = 0; i < table.getCols(); i++)
                {
                    int cellWidth = getWidthFromNode(table.getCell(i, 1));
                    if(cellWidth > 0)
                    {
                        specifiedColumnsWidth += cellWidth;
                    }
                    else
                    {
                        notSpecifiedColumns++;
                    }
                }
                if(specifiedColumnsWidth < tableWidth)
                {
                    columnWidth = (tableWidth - specifiedColumnsWidth) / notSpecifiedColumns;
                }
                else
                {
                    columnWidth = tableWidth / table.getCols();
                }
            }
            if(columnWidth > 0)
            {
                String indesignWidth = formatter.format(columnWidth * PIXEL_MULTIPLIER.get());
                return "<tColAttrWidth:" + indesignWidth + ">";
            }
        }
        return "";
    }


    private static int getWidthFromNode(AbstractNode node)
    {
        if(node != null)
        {
            try
            {
                String width = (String)node.getAttributes().get("width");
                if(width != null)
                {
                    return Integer.parseInt(width.replace("px", "").trim());
                }
            }
            catch(NumberFormatException ex)
            {
                LOG.error((String)node.getAttributes().get("width") + " is not a number!");
            }
        }
        return -1;
    }


    public static boolean cellIsRenderable(AbstractNode node)
    {
        return (node != null);
    }
}
