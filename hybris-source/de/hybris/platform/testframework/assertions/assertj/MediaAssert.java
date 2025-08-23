package de.hybris.platform.testframework.assertions.assertj;

import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.AbstractMedia;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.impl.JaloMediaSource;
import de.hybris.platform.servicelayer.media.impl.ModelMediaSource;
import java.text.MessageFormat;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.Assertions;

public class MediaAssert extends AbstractAssert<MediaAssert, MediaSource>
{
    private MediaAssert(Media actual)
    {
        super(new JaloMediaSource((AbstractMedia)actual), MediaAssert.class);
    }


    private MediaAssert(MediaModel actual)
    {
        super(new ModelMediaSource((AbstractMediaModel)actual), MediaAssert.class);
    }


    public static MediaAssert assertThat(Media actual)
    {
        return new MediaAssert(actual);
    }


    public static MediaAssert assertThat(MediaModel actual)
    {
        return new MediaAssert(actual);
    }


    public MediaAssert hasSameMetaDataAs(Media media)
    {
        checkMetadata((MediaSource)new JaloMediaSource((AbstractMedia)media));
        return this;
    }


    public MediaAssert hasSameDataPkAs(Media media)
    {
        checkDataPk((MediaSource)new JaloMediaSource((AbstractMedia)media));
        return this;
    }


    public MediaAssert hasSameMimeAs(Media media)
    {
        checkMime((MediaSource)new JaloMediaSource((AbstractMedia)media));
        return this;
    }


    public MediaAssert hasSameRealFileNameAs(Media media)
    {
        checkRealFileName((MediaSource)new JaloMediaSource((AbstractMedia)media));
        return this;
    }


    public MediaAssert hasSameSizeAs(Media media)
    {
        checkSize((MediaSource)new JaloMediaSource((AbstractMedia)media));
        return this;
    }


    public MediaAssert hasSameInternalUrlAs(Media media)
    {
        checkInternalUrl((MediaSource)new JaloMediaSource((AbstractMedia)media));
        return this;
    }


    public MediaAssert hasSameMetaDataAs(MediaModel media)
    {
        checkMetadata((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        return this;
    }


    public MediaAssert hasSameDataPkAs(MediaModel media)
    {
        checkDataPk((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        return this;
    }


    public MediaAssert hasSameMimeAs(MediaModel media)
    {
        checkMime((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        return this;
    }


    public MediaAssert hasSameRealFileNameAs(MediaModel media)
    {
        checkRealFileName((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        return this;
    }


    public MediaAssert hasSameSizeAs(MediaModel media)
    {
        checkSize((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        return this;
    }


    public MediaAssert hasSameInternalUrlAs(MediaModel media)
    {
        checkInternalUrl((MediaSource)new ModelMediaSource((AbstractMediaModel)media));
        return this;
    }


    private void checkMetadata(MediaSource sMedia)
    {
        checkMime(sMedia);
        checkLocation(sMedia);
        checkLocationHash(sMedia);
        checkRealFileName(sMedia);
        checkSize(sMedia);
    }


    private void checkSize(MediaSource sMedia)
    {
        ((AbstractLongAssert)Assertions.assertThat(((MediaSource)this.actual).getSize())
                        .overridingErrorMessage(errorMessage("mime", ((MediaSource)this.actual).getSize(), sMedia.getSize()), new Object[0]))
                        .isEqualTo(sMedia.getSize());
    }


    private void checkRealFileName(MediaSource sMedia)
    {
        ((AbstractCharSequenceAssert)Assertions.assertThat(((MediaSource)this.actual).getRealFileName())
                        .overridingErrorMessage(errorMessage("mime", ((MediaSource)this.actual).getRealFileName(), sMedia.getRealFileName()), new Object[0]))
                        .isEqualTo(sMedia.getRealFileName());
    }


    private void checkLocationHash(MediaSource sMedia)
    {
        ((AbstractCharSequenceAssert)Assertions.assertThat(((MediaSource)this.actual).getLocationHash())
                        .overridingErrorMessage(errorMessage("mime", ((MediaSource)this.actual).getLocationHash(), sMedia.getLocationHash()), new Object[0]))
                        .isEqualTo(sMedia.getLocationHash());
    }


    private void checkLocation(MediaSource sMedia)
    {
        ((AbstractCharSequenceAssert)Assertions.assertThat(((MediaSource)this.actual).getLocation())
                        .overridingErrorMessage(errorMessage("mime", ((MediaSource)this.actual).getLocation(), sMedia.getLocation()), new Object[0]))
                        .isEqualTo(sMedia.getLocation());
    }


    private void checkMime(MediaSource sMedia)
    {
        ((AbstractCharSequenceAssert)Assertions.assertThat(((MediaSource)this.actual).getMime())
                        .overridingErrorMessage(errorMessage("mime", ((MediaSource)this.actual).getMime(), sMedia.getMime()), new Object[0]))
                        .isEqualTo(sMedia.getMime());
    }


    private void checkDataPk(MediaSource sMedia)
    {
        ((AbstractLongAssert)Assertions.assertThat(((MediaSource)this.actual).getDataPk())
                        .overridingErrorMessage(errorMessage("dataPk", ((MediaSource)this.actual).getDataPk(), sMedia.getDataPk()), new Object[0]))
                        .isEqualTo(sMedia.getDataPk());
    }


    private void checkInternalUrl(MediaSource sMedia)
    {
        ((AbstractCharSequenceAssert)Assertions.assertThat(((MediaSource)this.actual).getInternalUrl())
                        .overridingErrorMessage(errorMessage("internalUrl", ((MediaSource)this.actual).getInternalUrl(), sMedia.getInternalUrl()), new Object[0]))
                        .isEqualTo(sMedia.getInternalUrl());
    }


    private String errorMessage(String property, Object actual, Object expected)
    {
        return MessageFormat.format("Found difference on property {0} [expected: {1}, actual: {2}", new Object[] {property, actual, expected});
    }
}
