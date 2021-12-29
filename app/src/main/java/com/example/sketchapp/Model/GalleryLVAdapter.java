package com.example.sketchapp.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.sketchapp.R;
import java.util.ArrayList;

/**
 * This class handles displaying the list of
 * drawings that the user has created in the
 * Gallery activity.
 */
public class GalleryLVAdapter extends ArrayAdapter<Drawing> {
    private final Context context;
    private final Integer resource;

    public GalleryLVAdapter(Context context, Integer resource, ArrayList<Drawing> drawingList) {
         super(context, resource, drawingList);
         this.context = context;
         this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Bitmap bitmap = getItem(position).getDrawingBitmap();
        String creationTime = getItem(position).getCreationTime();

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder") View drawingView = inflater.inflate(resource, parent, false);

        ImageView drawingImage = drawingView.findViewById(R.id.drawing_image);
        drawingImage.setImageBitmap(bitmap);

        TextView creationText = drawingView.findViewById(R.id.creationTV);
        creationText.setText(creationTime);

        //Can add onclick listener here for delete/edit.

        return drawingView;
    }
}
