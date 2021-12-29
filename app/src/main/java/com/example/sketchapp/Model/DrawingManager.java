package com.example.sketchapp.Model;

import android.content.Context;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * This class stores the user's drawings as Bitmaps.
 * Supports Singleton, to be accessed by the 'Gallery' and 'SketchScreen'
 * activities. Can add, delete, or edit drawings.
 * Supports saving in between executions with Gson.
 */
public class DrawingManager {

    private static DrawingManager instance;
    private static final String FILE_NAME = "drawingList.json";
    private ArrayList<Drawing> drawingList = new ArrayList<>();

    private DrawingManager(Context context) {
        setDrawingList(context);
    }

    public static DrawingManager getInstance(Context context) {
        if (instance == null) instance = new DrawingManager(context);
        return instance;
    }

    public void addDrawing(Context context, Drawing newDrawing) {
        drawingList.add(newDrawing);
        saveDrawingList(context, drawingList);
    }

    public Drawing getDrawing(Integer index) {
        return drawingList.get(index);
    }

    public ArrayList<Drawing> getDrawingList() {
        return drawingList;
    }

    private void setDrawingList(Context context) {
        drawingList = loadDrawingList(context);
    }

    public void removeDrawing(Context context, Integer index) {
        if (index <= drawingList.size()) drawingList.remove(getDrawing(index));
        saveDrawingList(context, drawingList);
    }

    public static ArrayList<Drawing> loadDrawingList(Context context) {
        Gson gson = createGson();
        ArrayList<Drawing> drawingList = null;

        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            drawingList = gson.fromJson(bufferedReader, new TypeToken<ArrayList<Drawing>>() {
            }.getType());
            bufferedReader.close();
            fileReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Reading error");
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (drawingList == null) return new ArrayList<>();
        return drawingList;
    }

    public static void saveDrawingList(Context context, ArrayList<Drawing> drawingList) {
        Gson gson = createGson();
        String json = gson.toJson(drawingList);

        File file = new File(context.getFilesDir(), FILE_NAME);

        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Gson createGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
            new TypeAdapter<LocalDateTime>() {
                @Override
                public void write(JsonWriter jsonWriter,
                                  LocalDateTime localDateTime) throws IOException {
                    jsonWriter.value(localDateTime.toString());
                }
                @Override
                public LocalDateTime read(JsonReader jsonReader) throws IOException {
                    return LocalDateTime.parse(jsonReader.nextString());
                }
            }).create();
    }
}
