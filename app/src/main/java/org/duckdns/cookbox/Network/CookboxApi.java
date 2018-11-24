package org.duckdns.cookbox.Network;

import org.duckdns.cookbox.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CookboxApi {
    @GET("recipes")
    Call<List<Recipe>> getAllRecipes();

    @GET("recipes/{id}")
    Call<Recipe> getRecipe(@Path("id") long id);
}
