package org.duckdns.cookbox.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

import org.duckdns.cookbox.Adapter.RecipeArrayAdapter;
import org.duckdns.cookbox.Model.Recipe;
import org.duckdns.cookbox.Network.CookboxApi;
import org.duckdns.cookbox.R;

import java.util.List;

public class RecipeListFragment extends ListFragment {
    CookboxApi cookboxApi = null;
    List<Recipe> recipeList = null;

    public RecipeListFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();

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

        Call<List<Recipe>> call = cookboxApi.getAllRecipes("id,name,description");
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList = response.body();
                setListAdapter(
                        new RecipeArrayAdapter(
                                getContext(),
                                R.layout.recipe_list_item,
                                recipeList)
                );
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable throwable) {
                Toast.makeText(getContext(), "Could not retreive recipes", Toast.LENGTH_SHORT).show();
                Log.e("RecipeListFragment:", throwable.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerForContextMenu(getListView());

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            }
        });
    }
}
