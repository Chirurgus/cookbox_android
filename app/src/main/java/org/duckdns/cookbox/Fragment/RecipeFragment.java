package org.duckdns.cookbox.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.duckdns.cookbox.Model.Recipe;
import org.duckdns.cookbox.Network.CookboxApi;
import org.duckdns.cookbox.R;
import org.duckdns.cookbox.View.LinearListLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeFragment extends Fragment {
    CookboxApi cookboxApi = null;
    Recipe recipe = null;

    public RecipeFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (cookboxApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            cookboxApi = retrofit.create(CookboxApi.class);
        }

        Bundle args = getArguments();
        if (args == null) {
            // Abort creation
            return;
        }
        Long id = args.getLong("id");

        Call<Recipe> call = cookboxApi.getRecipe(id);
        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                recipe = response.body();
                // If onCreateView has already been called yet
                if (getView() != null) {
                    clearFields(getView());
                    populateFields(getView(), recipe, 1);
                }
            }
            @Override
            public void onFailure(Call<Recipe> call, Throwable throwable) {
                Toast.makeText(getContext(), "Could not retreive recipe", Toast.LENGTH_SHORT).show();
                Log.e("RecipeFragment:", throwable.toString());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        getView().setKeepScreenOn(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.recipe, container, false);

        // If the recipe is already fetched form the api
        if (recipe != null) {
            populateFields(view, recipe, 1);
        }

        /* set on target quantity edit listener */
        EditText target_qty_et = (EditText) view.findViewById(R.id.recipe_text_qty_tgt);
        target_qty_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    clearFields(view);
                    final Float newQty = Float.parseFloat(v.getText().toString());
                    populateFields(view, recipe, newQty/recipe.total_yield);
                    return true;
                }
                return false;
            }
        });

        view.setKeepScreenOn(true);
        return view;
    }

    private void clearFields(View rootView) {
        TextView name = (TextView) rootView.findViewById(R.id.recipe_text_name);
        name.setText("");

        TextView long_desc = (TextView) rootView.findViewById(R.id.recipe_text_desc);
        long_desc.setText("");

        TextView target_qty = (TextView) rootView.findViewById(R.id.recipe_text_qty_tgt);
        target_qty.setText("");

        TextView target_desc = (TextView) rootView.findViewById(R.id.recipe_text_desc_tgt);
        target_desc.setText("");

        LinearLayout ingredient_ll = (LinearLayout) rootView.findViewById(R.id.recipe_ingredient_list);
        ingredient_ll.removeAllViews();

        LinearLayout instruction_ll = (LinearLayout) rootView.findViewById(R.id.recipe_instruction_list);
        instruction_ll.removeAllViews();

        LinearLayout comment_ll = (LinearLayout) rootView.findViewById(R.id.recipe_note_list);
        comment_ll.removeAllViews();

        LinearLayout tag_ll = (LinearLayout) rootView.findViewById(R.id.recipe_tag_list);
        tag_ll.removeAllViews();
    }

    private void populateFields(View rootView, Recipe recipe, float ingredientQtyMultyplier) {

        final ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        final TextView name = (TextView) rootView.findViewById(R.id.recipe_text_name);
        name.setText(recipe.name);

        final TextView long_desc = (TextView) rootView.findViewById(R.id.recipe_text_desc);
        long_desc.setText(recipe.description);

        final TextView target_qty = (TextView) rootView.findViewById(R.id.recipe_text_qty_tgt);
        target_qty.setText(Float.toString(ingredientQtyMultyplier * recipe.total_yield));

        final TextView target_desc = (TextView) rootView.findViewById(R.id.recipe_text_desc_tgt);
        target_desc.setText(recipe.unit_yield);

        final LinearListLayout ingGroupLl = (LinearListLayout) rootView.findViewById(R.id.recipe_ingredient_list);

        for (Recipe.IngredientGroup ingredientGroup : recipe.ingredient_groups){
            // Set name of the ingredient group
            final TextView ingGroupNameTv = new TextView(getContext());
            ingGroupNameTv.setText(ingredientGroup.name);
            ingGroupLl.addView(ingGroupNameTv);

            for (Recipe.Ingredient ingredient : ingredientGroup.ingredients) {

                final RelativeLayout ingLayout = (RelativeLayout) ingGroupLl.addListItem(R.layout.recipe_ingredient, layoutParams);
                TextView ing_qty = (TextView) ingLayout.getChildAt(0);
                ing_qty.setText(Float.toString(ingredientQtyMultyplier * ingredient.quantity));

                TextView ing_desc = (TextView) ingLayout.getChildAt(1);
                ing_desc.setText(ingredient.unit + " " + ingredient.description);
            }
        }

        final LinearListLayout instructionsLayout = rootView.findViewById(R.id.recipe_instruction_list);
        for (Recipe.Instruction instruction : recipe.instructions) {
            final TextView instructionTv = new TextView(getContext());
            instructionTv.setText(instruction.instruction);
            instructionsLayout.addView(instructionTv, layoutParams);
        }

        final LinearListLayout noteLayout = rootView.findViewById(R.id.recipe_note_list);
        for (Recipe.Note note : recipe.notes) {
            final TextView noteTv = new TextView(getContext());
            noteTv.setText(note.text);
            noteLayout.addView(noteTv, layoutParams);
        }

        final LinearListLayout tagLayout = rootView.findViewById(R.id.recipe_tag_list);
        for (Recipe.Tag tag : recipe.tags) {
            final TextView tagTv = new TextView(getContext());
            tagTv.setText(tag.tag);
            tagLayout.addView(tagTv, layoutParams);
        }
    }
}
