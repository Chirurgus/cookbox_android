package org.duckdns.cookbox.Model;

import java.util.List;
import java.util.Date;

public class Recipe {
    public long id;
    public String name;
    public String description;
    public String unit_time;

    public float total_time;
    public float preparation_time;
    public float cook_time;

    public String unit_yield;
    public float total_yield;
    public float serving_size;

    public String source;

    public Date last_modified;

    public List<IngredientGroup> ingredient_groups;
    public List<Instruction> instructions;
    public List<Tag> tags;
    public List<Note> notes;

    public class IngredientGroup {
        public String name;
        public List<Ingredient> ingredients;
    }

    public class Ingredient {
        public String unit;
        public float quantity;
        public String description;
        public String usda_code;
        public List<Note> notes;
    }

    public class Instruction {
        public String instruction;
        public List<Note> notes;
    }

    public class Tag {
        public String tag;
    }

    public class Note {
        public String text;
        public String image;
    }
}
