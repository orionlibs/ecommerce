package de.hybris.platform.cockpit.components;

public class CockpitBasicFCKEditor extends CockpitFCKEditor
{
    public CockpitBasicFCKEditor(String langIso, CockpitFCKEditor.Skin skin)
    {
        super(langIso, null, skin);
        setToolbarIcons("[\n['Bold','Italic','Underline','StrikeThrough','-','OrderedList','UnorderedList'],\n ]", false);
        applySettings();
    }
}
