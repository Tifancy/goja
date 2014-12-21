package goja.rapid.ue;

import com.google.common.collect.Lists;
import com.jfinal.upload.UploadFile;
import goja.IntPool;
import goja.tuples.Tuple;

/**
 * <p> </p>
 *
 * @author sogYF
 * @version 1.0
 * @since JDK 1.6
 */
public enum UeditorApiAction {

    CONFIG {
        @Override
        public <T> T invoke() {
            return (T) UEditorConfig.default_conf();
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    },

    /**
     * 上传图片
     * <p/>
     * 返回示例：
     * <p/>
     * {"original":"demo.jpg","name":"demo.jpg","url":"\/server\/ueditor\/upload\/image\/demo.jpg","size":"99697",
     * "type":".jpg","state":"SUCCESS"}
     */
    UPLOADIMAGE {
        @Override
        public <T> T invoke() {
            return null;
        }

        @Override
        public <T> T invoke(Tuple param) {
            UploadFile file = (UploadFile) param.getValue(IntPool.ZERO);

            return null;
        }
    },

    UPLOADFILE {
        @Override
        public <T> T invoke() {
            return null;
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    },

    UPLOADVIDEO {
        @Override
        public <T> T invoke() {
            return null;
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    },

    CATCHIMAGE {
        @Override
        public <T> T invoke() {
            return null;
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    },

    UPLOADSCRAWL {
        @Override
        public <T> T invoke() {
            return null;
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    },

    /**
     * 列出指定目录下的图片
     * <p/>
     * imageManagerActionName {String} [默认值："listimage"] //执行图片管理的action名称
     * imageManagerListPath {String} [默认值："/ueditor/php/upload/image/"] //指定要列出图片的目录
     * imageManagerListSize {String} [默认值：20] //每次列出文件数量
     * imageManagerUrlPrefix {String} [默认值：""] //图片访问路径前缀
     * imageManagerInsertAlign {String} [默认值："none"] //插入的图片浮动方式
     * imageManagerAllowFiles {Array}, //列出的文件类型
     * <p/>
     * example:
     * <p/>
     * {
     * "state": "SUCCESS",
     * "list": [
     * {
     * "url": "/server/ueditor/upload/image/3 2.jpg",
     * "mtime": 1400203383
     * },
     * {
     * "url": "/server/ueditor/upload/image/1.jpg",
     * "mtime": 1400203383
     * }
     * ],
     * "start": "0",
     * "total": 29
     * }
     */
    LISTIMAGE {
        @Override
        public <T> T invoke() {
            return (T) Lists.newArrayList();
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    },

    /**
     * 列出指定目录下的文件
     * <p/>
     * fileManagerActionName {String} [默认值："listfile"] //执行文件管理的action名称
     * fileManagerListPath {String} [默认值："/ueditor/php/upload/file/"] //指定要列出文件的目录
     * fileManagerUrlPrefix {String} [默认值：""] //文件访问路径前缀
     * fileManagerListSize {String} [默认值：20] //每次列出文件数量
     * fileManagerAllowFiles {Array} //列出的文件类型
     * <p/>
     * 返回示例 ：
     * <p/>
     * {
     * "state": "SUCCESS",
     * "list": [
     * {
     * "url": "/server/ueditor/upload/file/7.pptx",
     * "mtime": 1400203383
     * }
     * ],
     * "start": "0",
     * "total": 7
     * }
     */
    LISTFILE {
        @Override
        public <T> T invoke() {
            return null;
        }

        @Override
        public <T> T invoke(Tuple param) {
            return null;
        }
    };


    public abstract <T> T invoke();

    public abstract <T> T invoke(Tuple param);
}
