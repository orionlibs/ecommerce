package de.hybris.platform.testframework.assertions.assertj;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.io.InputStream;
import org.assertj.core.api.Assertions;

public class ExtendedAssertions extends Assertions
{
    public static InputStreamAssert assertThat(InputStream actual)
    {
        return InputStreamAssert.assertThat(actual);
    }


    public static MediaAssert assertThat(Media actual)
    {
        return MediaAssert.assertThat(actual);
    }


    public static MediaAssert assertThat(MediaModel actual)
    {
        return MediaAssert.assertThat(actual);
    }


    public static ModelStateAssert assertThat(AbstractItemModel actual)
    {
        return ModelStateAssert.assertThat(actual);
    }
}
