package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

public enum VertexColor
{
    GREEN("#3e9743"),
    GREY("#afafaf"),
    ORANGE("#f39422"),
    RED("#f54028");
    private final String hexColor;


    VertexColor(String hex)
    {
        this.hexColor = hex;
    }


    public String getHexColor()
    {
        return this.hexColor;
    }
}
