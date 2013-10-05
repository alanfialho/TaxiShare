/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author alan
 */
public class TsTypeAdapter<T> extends TypeAdapter<T> {
    
    public T read(JsonReader reader) throws IOException {
        return null;
    }

    public void write(JsonWriter writer, T obj) throws IOException {
        if (obj == null) {
            writer.nullValue();
            return;
        }
        writer.value(obj.toString());
    }
}
