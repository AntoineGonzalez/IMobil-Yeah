package com.example.myapplication.adapter;

import com.example.myapplication.model.Propriete;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapteur qui permet à Moshi de parser une liste de propriété au format JSON.
 */
public class ApiProprieteListAdapter {
    @FromJson
    List<Propriete> fromJson(JsonReader reader, JsonAdapter<Propriete> delegate) throws IOException {
        List<Propriete> result = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("success")) {
                boolean success = reader.nextBoolean();
                if (!success) {
                    throw new IOException("API a répondu FALSE");
                }
            } else if (name.equals("response")) {
                reader.beginArray();
                while (reader.hasNext()) {
                    Propriete p = delegate.fromJson(reader);
                    result.add(p);
                }
                reader.endArray();
            } else {
                throw new IOException("Response contient des données non conformes");
            }
        }
        reader.endObject();
        return result;
    }
}
