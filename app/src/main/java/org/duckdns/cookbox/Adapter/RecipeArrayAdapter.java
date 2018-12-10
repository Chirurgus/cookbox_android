package org.duckdns.cookbox.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;

import java.util.List;

import org.duckdns.cookbox.R;

import org.duckdns.cookbox.Model.Recipe;


public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {
    public RecipeArrayAdapter(Context context, int layout_resource, List<Recipe> recipes){
        super(context,layout_resource,recipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.recipe_list_text_main);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.recipe_list_text_second);
        // Populate the data into the template view using the data object
        tvName.setText(recipe.name);
        tvDesc.setText(recipe.description);
        // Return the completed view to render on screen
        return convertView;
    }
}
