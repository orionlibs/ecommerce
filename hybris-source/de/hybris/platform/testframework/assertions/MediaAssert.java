package de.hybris.platform.testframework.assertions;

import de.hybris.platform.core.model.media.AbstractMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.AbstractMedia;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.media.MediaSource;
import de.hybris.platform.media.impl.JaloMediaSource;
import de.hybris.platform.servicelayer.media.impl.ModelMediaSource;
import java.text.MessageFormat;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;
import org.fest.assertions.LongAssert;
import org.fest.assertions.StringAssert;

@Deprecated(since = "2011", forRemoval = true)
public class MediaAssert extends GenericAssert<MediaAssert, MediaSource>
{
    public MediaAssert(Media actual)
    {
        super(MediaAssert.class, new JaloMediaSource((AbstractMedia)actual));
    }


    public MediaAssert(MediaModel actual)
    {
        super(MediaAssert.class, new ModelMediaSource((AbstractMediaModel)actual));
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
        ((LongAssert)Assertions.assertThat(((MediaSource)this.actual).getSize())
                        .overridingErrorMessage(
                                        errorMessage("mime", ((MediaSource)this.actual).getSize(), sMedia.getSize())))
                        .isEqualTo(sMedia.getSize());
    }


    private void checkRealFileName(MediaSource sMedia)
    {
        ((StringAssert)Assertions.assertThat(((MediaSource)this.actual).getRealFileName())
                        .overridingErrorMessage(
                                        errorMessage("mime", ((MediaSource)this.actual).getRealFileName(), sMedia.getRealFileName())))
                        .isEqualTo(sMedia
                                        .getRealFileName());
    }


    private void checkLocationHash(MediaSource sMedia)
    {
        ((StringAssert)Assertions.assertThat(((MediaSource)this.actual).getLocationHash())
                        .overridingErrorMessage(
                                        errorMessage("mime", ((MediaSource)this.actual).getLocationHash(), sMedia.getLocationHash())))
                        .isEqualTo(sMedia
                                        .getLocationHash());
    }


    private void checkLocation(MediaSource sMedia)
    {
        ((StringAssert)Assertions.assertThat(((MediaSource)this.actual).getLocation())
                        .overridingErrorMessage(
                                        errorMessage("mime", ((MediaSource)this.actual).getLocation(), sMedia.getLocation())))
                        .isEqualTo(sMedia.getLocation());
    }


    private void checkMime(MediaSource sMedia)
    {
        ((StringAssert)Assertions.assertThat(((MediaSource)this.actual).getMime())
                        .overridingErrorMessage(
                                        errorMessage("mime", ((MediaSource)this.actual).getMime(), sMedia.getMime())))
                        .isEqualTo(sMedia.getMime());
    }


    private void checkDataPk(MediaSource sMedia)
    {
        ((LongAssert)Assertions.assertThat(((MediaSource)this.actual).getDataPk())
                        .overridingErrorMessage(
                                        errorMessage("dataPk", ((MediaSource)this.actual).getDataPk(), sMedia.getDataPk())))
                        .isEqualTo(sMedia.getDataPk());
    }


    private void checkInternalUrl(MediaSource sMedia)
    {
        ((StringAssert)Assertions.assertThat(((MediaSource)this.actual).getInternalUrl())
                        .overridingErrorMessage(
                                        errorMessage("internalUrl", ((MediaSource)this.actual).getInternalUrl(), sMedia.getInternalUrl())))
                        .isEqualTo(sMedia
                                        .getInternalUrl());
    }


    private String errorMessage(String property, Object actual, Object expected)
    {
        return MessageFormat.format("Found difference on property {0} [expected: {1}, actual: {2}", new Object[] {property, actual, expected});
    }
}
