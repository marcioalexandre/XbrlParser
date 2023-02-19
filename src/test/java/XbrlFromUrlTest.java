import com.xbrlframework.file.XbrlFromURL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XbrlFromUrlTest {

    public final String url ="https://www.sec.gov/Archives/edgar/data/759944/000075994422000142/cfg-20220930_htm.xml";

    @Test
    public void validate() {
        XbrlFromURL xbrlFromURL = new XbrlFromURL(url);
        assertNotNull(xbrlFromURL.processing());
    }
}
