package goja.rapid.ue;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class UEUpload {

    public final String original;
    public final String name;
    public final String url;
    public final int size;
    public final String type;
    public final String state;

    public UEUpload(String original, String name, String url, int size, String type, String state) {
        this.original = original;
        this.name = name;
        this.url = url;
        this.size = size;
        this.type = type;
        this.state = state;
    }

    public String getOriginal() {
        return original;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getState() {
        return state;
    }
}
