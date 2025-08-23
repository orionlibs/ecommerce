package de.hybris.platform.cockpit.components.sectionpanel;

import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import org.apache.commons.lang.StringUtils;

public class Message
{
    public static final int INFO = 0;
    public static final int OK_WARNING = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int NOT_DEFINED = -1;
    public static final int MAX_MESSAGE_LENGHT = 50;
    private String text;
    private int level;
    private boolean visible;
    private String tooltip;
    private CockpitValidationDescriptor validationDescriptor;
    private String constraintPk;
    private SectionRow sectionRow;


    public Message(CockpitValidationDescriptor validationDescriptor)
    {
        this(validationDescriptor.getValidationMessage(), validationDescriptor.getCockpitMessageLevel(), true);
        this.validationDescriptor = validationDescriptor;
        this.constraintPk = validationDescriptor.getConstraint().getPk().toString();
    }


    public Message(String text)
    {
        this(text, 0);
    }


    public Message(String text, int level)
    {
        this(text, level, true);
    }


    public Message(String text, int level, boolean visible)
    {
        this.text = StringUtils.abbreviate(text, 50);
        this.tooltip = text;
        this.level = level;
        this.visible = visible;
    }


    public String getConstraintPk()
    {
        return this.constraintPk;
    }


    public int getLevel()
    {
        return this.level;
    }


    public SectionRow getSectionRow()
    {
        return this.sectionRow;
    }


    public String getText()
    {
        return this.text;
    }


    public String getTooltip()
    {
        return this.tooltip;
    }


    public CockpitValidationDescriptor getValidationDescriptor()
    {
        return this.validationDescriptor;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setConstraintPk(String constraintPk)
    {
        this.constraintPk = constraintPk;
    }


    public void setLevel(int level)
    {
        this.level = level;
    }


    public void setSectionRow(SectionRow sectionRow)
    {
        this.sectionRow = sectionRow;
    }


    public void setText(String text)
    {
        this.text = text;
    }


    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }


    public void setValidationDescriptor(CockpitValidationDescriptor validationDescriptor)
    {
        this.validationDescriptor = validationDescriptor;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
