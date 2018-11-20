package com.example.android.bakingapp.models;

public class Recipe {

    private String recipeName, recipeImage, recipeServings;
    private int id;

    public Recipe(){}

    public int getId(){ return id;}

    public void setId(int id){ this.id = id;}

    public String getRecipeName(){
        return recipeName;
    }

    public void setRecipeName(String recipeName){ this.recipeName = recipeName;}

    public String getRecipeImage(){
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage){
        this.recipeImage = recipeImage;
    }

    public String getRecipeServings(){ return recipeServings;}

    public void setRecipeServings(String recipeServings){ this.recipeServings = recipeServings;}
}
