/*
 * Copyright (c) 2013 Android Alliance LTD
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
package uk.co.androidalliance.fsm.client.bundles;

public class Changed {

    public static final String ACTION_TYPE = Entering.class.getSimpleName();

    private final Object payload;

    public Changed () {
        this.payload = null;
    }

    public Changed (String message) {
        this.payload = message;
    }

    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return ACTION_TYPE + ", payload : " + payload;
    }

}
