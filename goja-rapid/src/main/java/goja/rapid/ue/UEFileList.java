package goja.rapid.ue;

import java.util.List;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public class UEFileList {

    public final String state;

    public final List<UEFile> list;

    public final int start;

    public final int total;

    public UEFileList(String state, List<UEFile> list, int start, int total) {
        this.state = state;
        this.list = list;
        this.start = start;
        this.total = total;
    }

    public String getState() {
        return state;
    }

    public List<UEFile> getList() {
        return list;
    }

    public int getStart() {
        return start;
    }

    public int getTotal() {
        return total;
    }
}
