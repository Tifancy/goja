package goja.rapid.ue;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class UEFile {

    public final String url;

    public final long mtime;


    public UEFile(String url, long mtime) {
        this.url = url;
        this.mtime = mtime;
    }

    public String getUrl() {
        return url;
    }

    public long getMtime() {
        return mtime;
    }
}
