package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.impex.jalo.translators.DefaultAttributeTranslator;
import de.hybris.platform.impex.jalo.translators.HeaderCellTranslator;
import de.hybris.platform.util.CSVCellDecorator;

public abstract class AbstractColumnDescriptor extends AbstractDescriptor
{
    private int position;
    private final HeaderDescriptor header;
    private final CSVCellDecorator decorator;


    public AbstractColumnDescriptor(int position, HeaderDescriptor header, String expr) throws HeaderValidationException
    {
        super(expr);
        this.position = position;
        this.header = header;
        this.decorator = createCSVDecorator();
        if(this.decorator != null && this.decorator instanceof AbstractImpExCSVCellDecorator)
        {
            ((AbstractImpExCSVCellDecorator)this.decorator).init(this);
        }
    }


    public AbstractColumnDescriptor(int position, HeaderDescriptor header, String expr, AbstractDescriptor.DescriptorParams params) throws HeaderValidationException
    {
        super(expr, params);
        this.position = position;
        this.header = header;
        this.decorator = createCSVDecorator();
        if(this.decorator != null && this.decorator instanceof AbstractImpExCSVCellDecorator)
        {
            ((AbstractImpExCSVCellDecorator)this.decorator).init(this);
        }
    }


    protected void changePosition(int newPos)
    {
        this.position = newPos;
    }


    protected void shiftPosition(int offset)
    {
        this.position += offset;
    }


    protected CSVCellDecorator createCSVDecorator() throws HeaderValidationException
    {
        String className = getDescriptorData().getModifier("cellDecorator");
        if(className != null && className.length() > 0)
        {
            try
            {
                Class<?> c = Class.forName(className);
                return (CSVCellDecorator)c.newInstance();
            }
            catch(ClassNotFoundException e)
            {
                throw new HeaderValidationException("invalid csv decorator class '" + className + "' : " + e.getMessage(), 0);
            }
            catch(InstantiationException e)
            {
                throw new HeaderValidationException("error while instantiating csv decorator class '" + className + "' : " + e
                                .getMessage(), 0);
            }
            catch(IllegalAccessException e)
            {
                throw new HeaderValidationException("can not access csv decorator class '" + className + "' : " + e.getMessage(), 0);
            }
        }
        return null;
    }


    public CSVCellDecorator getCSVCellDecorator()
    {
        return this.decorator;
    }


    protected HeaderCellTranslator createTranslator(String expr)
    {
        return (HeaderCellTranslator)new DefaultAttributeTranslator();
    }


    public String getQualifier()
    {
        return ((AbstractDescriptor.ColumnParams)getDescriptorData()).getQualifier();
    }


    public int getValuePosition()
    {
        return this.position;
    }


    public HeaderDescriptor getHeader()
    {
        return this.header;
    }


    protected void validate() throws HeaderValidationException
    {
    }


    public String getQualifierForComment()
    {
        return getQualifier();
    }
}
