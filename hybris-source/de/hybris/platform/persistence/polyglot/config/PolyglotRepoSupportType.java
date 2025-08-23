package de.hybris.platform.persistence.polyglot.config;

public enum PolyglotRepoSupportType
{
    FULL(2),
    PARTIAL(1),
    NONE(0);
    private final int level;


    PolyglotRepoSupportType(int level)
    {
        this.level = level;
    }


    public PolyglotRepoSupportType getHigher(PolyglotRepoSupportType supportType)
    {
        if(supportType.level > this.level)
        {
            return supportType;
        }
        return this;
    }
}
