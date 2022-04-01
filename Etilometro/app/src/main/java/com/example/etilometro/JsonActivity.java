package com.example.etilometro;

import android.util.JsonReader;

import com.example.etilometro.oggetti.Drink;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonActivity {

    private static final String TAG = "ANDREA";

    public List<Drink> readJsonStreamDrink(InputStreamReader in) throws IOException {
        JsonReader reader = new JsonReader(in);
        try {
            return readDrinkArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Drink> readDrinkArray(JsonReader reader) throws IOException {
        List<Drink> drink = new ArrayList<Drink>();
        reader.beginArray();
        while (reader.hasNext())
            drink.add(readDrink(reader));
        reader.endArray();
        return drink;
    }

    public Drink readDrink(JsonReader reader) throws IOException {
        String nome = null, img = null;
        int gradi = 0, litri = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                nome = reader.nextString();
            } else if (name.equals("gradi")) {
                gradi = reader.nextInt();
            } else if (name.equals("litri")) {
                litri = reader.nextInt();
            } else if (name.equals("img")) {
                img = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Drink(nome, gradi, litri, img);
    }













}
