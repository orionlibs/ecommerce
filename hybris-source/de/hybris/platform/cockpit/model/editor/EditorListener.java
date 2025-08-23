package de.hybris.platform.cockpit.model.editor;

public interface EditorListener
{
    public static final String ENTER_PRESSED = "enter_pressed";
    public static final String ESCAPE_PRESSED = "escape_pressed";
    public static final String OPEN_EXTERNAL_CLICKED = "open_external_clicked";
    public static final String CANCEL_CLICKED = "cancel_clicked";
    public static final String FORCE_SAVE_CLICKED = "force_save_clicked";


    void valueChanged(Object paramObject);


    void actionPerformed(String paramString);
}
