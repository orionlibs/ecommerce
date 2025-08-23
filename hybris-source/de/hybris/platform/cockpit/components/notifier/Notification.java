package de.hybris.platform.cockpit.components.notifier;

import java.util.ArrayList;
import java.util.List;

public class Notification
{
    private String caption = null;
    private String message = null;
    private String author = null;
    private final List<String> messages = new ArrayList<>();


    public Notification(String message)
    {
        this(null, message);
    }


    public Notification(String caption, String message)
    {
        this(caption, message, null);
    }


    public Notification(List<String> messages)
    {
        this(null, null, null);
        getMessages().clear();
        getMessages().addAll(messages);
    }


    public Notification(String caption, String message, String author)
    {
        this.caption = caption;
        this.message = message;
        this.author = author;
    }


    public String getCaption()
    {
        return this.caption;
    }


    public void setCaption(String caption)
    {
        this.caption = caption;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setMessage(String message)
    {
        this.message = message;
    }


    public String getAuthor()
    {
        return this.author;
    }


    public void setAuthor(String author)
    {
        this.author = author;
    }


    public List<String> getMessages()
    {
        return this.messages;
    }
}
