/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package goja.plugins.tablebind;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import goja.annotation.TableBind;
import goja.initialize.ctxbox.ClassBox;
import goja.initialize.ctxbox.ClassType;

import java.util.List;

public class AutoTableBindPlugin extends ActiveRecordPlugin {

    private final INameStyle nameStyle;

    public AutoTableBindPlugin(String configName, IDataSourceProvider dataSourceProvider, INameStyle nameStyle) {
        super(configName, dataSourceProvider);
        this.nameStyle = nameStyle;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean start() {
        List<Class> modelClasses = ClassBox.getInstance().getClasses(ClassType.MODEL);
        if (modelClasses != null && !modelClasses.isEmpty()) {
            TableBind tb;
            for (Class modelClass : modelClasses) {
                tb = (TableBind) modelClass.getAnnotation(TableBind.class);
                String tableName;
                if (tb == null) {
                    tableName = nameStyle.name(modelClass.getSimpleName());
                    this.addMapping(tableName, modelClass);
                } else {
                    tableName = tb.tableName();
                    if (StrKit.notBlank(tb.pkName())) {
                        this.addMapping(tableName, tb.pkName(), modelClass);
                    } else {
                        this.addMapping(tableName, modelClass);
                    }
                }
            }
        }
        return super.start();
    }

    @Override
    public boolean stop() {
        return super.stop();
    }

}
