/*
 * Copyright 2018 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.refdata.store.offheapstore.serdes;

import stroom.refdata.store.RefDataValue;
import stroom.refdata.store.StringValue;
import stroom.util.logging.LambdaLogger;

import java.nio.ByteBuffer;

public class StringValueSerde implements RefDataValueSerde {

    private final StringSerde stringSerde;

    StringValueSerde() {
        this.stringSerde = new StringSerde();
    }

    @Override
    public RefDataValue deserialize(final ByteBuffer byteBuffer) {
        String str = stringSerde.deserialize(byteBuffer);
        return new StringValue(str);
    }

    @Override
    public void serialize(final ByteBuffer byteBuffer, final RefDataValue refDataValue) {
        try {
            StringValue stringValue = (StringValue) refDataValue;
            stringSerde.serialize(byteBuffer, stringValue.getValue());
        } catch (ClassCastException e) {
            throw new RuntimeException(LambdaLogger.buildMessage("Unable to cast {} to {}",
                    refDataValue.getClass().getCanonicalName(), StringValue.class.getCanonicalName()), e);
        }
    }


    /**
     * Extracts the string value from the buffer.
     */
    public String extractValue(final ByteBuffer byteBuffer) {
        return stringSerde.deserialize(byteBuffer);
    }
}
