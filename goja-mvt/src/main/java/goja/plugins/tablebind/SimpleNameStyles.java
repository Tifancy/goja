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

public class SimpleNameStyles {
    public static final INameStyle DEFAULT = new INameStyle() {
        @Override
        public String name(String className) {
            return className;
        }
    };

    public static final INameStyle LOWER_UNDERLINE = new INameStyle() {
        @Override
        public String name(String className) {
            String tableName = "";
            for (int i = 0; i < className.length(); i++) {
                char ch = className.charAt(i);
                if (i == 0) {
                    tableName += Character.toLowerCase(ch);
                } else if (Character.isUpperCase(ch)) {
                    tableName += "_" + Character.toLowerCase(ch);
                } else {
                    tableName += ch;
                }
            }
            return tableName;
        }
    };
}
