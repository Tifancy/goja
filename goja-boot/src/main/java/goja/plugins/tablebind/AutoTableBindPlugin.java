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
import goja.StringPool;
import goja.annotation.TableBind;
import goja.initialize.ctxbox.ClassBox;
import goja.initialize.ctxbox.ClassType;

import java.util.List;

public class AutoTableBindPlugin extends ActiveRecordPlugin {


    public AutoTableBindPlugin(String configName, IDataSourceProvider dataSourceProvider) {
        super(configName, dataSourceProvider);
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
                    tableName = name(modelClass.getSimpleName());
                    this.addMapping(tableName, modelClass);
                } else {
                    if (tb.ignore()) {

                        continue;
                    }
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


    public String name(String className) {
        String tableName = StringPool.EMPTY;
        for (int i = 0; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (i == 0) {
                tableName += Character.toLowerCase(ch);
            } else if (Character.isUpperCase(ch)) {
                tableName += StringPool.UNDERSCORE + Character.toLowerCase(ch);
            } else {
                tableName += ch;
            }
        }
        return tableName;
    }
}
