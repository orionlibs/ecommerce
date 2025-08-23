package de.hybris.platform.cockpit.wizards;

public class Message
{
    public static final int INFO = 0;
    public static final int OK_WARNING = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int NOT_DEFINED = -1;
    private final String messageText;
    private final String componentId;
    private final int level;
    private final String constraintPK;


    public Message(int level, String messageText, String componentId)
    {
        this.level = level;
        this.messageText = messageText;
        this.componentId = componentId;
        this.constraintPK = null;
    }


    public Message(int level, String messageText, String componentId, String constraintPK)
    {
        this.level = level;
        this.messageText = messageText;
        this.componentId = componentId;
        this.constraintPK = constraintPK;
    }


    public String getMessageText()
    {
        return this.messageText;
    }


    public String getComponentId()
    {
        return this.componentId;
    }


    public int getLevel()
    {
        return this.level;
    }


    public String getConstraintPK()
    {
        return this.constraintPK;
    }
}
