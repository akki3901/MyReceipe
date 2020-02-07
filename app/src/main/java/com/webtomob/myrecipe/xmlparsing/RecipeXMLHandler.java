package com.webtomob.myrecipe.xmlparsing;

import com.webtomob.myrecipe.model.Category;
import com.webtomob.myrecipe.model.Recipe;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class RecipeXMLHandler extends DefaultHandler {
    boolean currentElement = false;
    String currentValue = "";

    Recipe recipeData;
    ArrayList<Recipe> recipeList;
    Category category;
    ArrayList<Category> categoryList;

    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public ArrayList<Category> getCategoryList(){
        return categoryList;
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;

        if (qName.equals("RecipeTypes")){
            recipeList = new ArrayList<Recipe>();
            categoryList = new ArrayList<Category>();
        }
        else if (qName.equals("Category")) {
            category = new Category();
        }
        else if (qName.equals("RecipeData")) {
            recipeData = new Recipe();
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        if (qName.equalsIgnoreCase("Id"))
            category.setCatId(currentValue.trim());
        else if (qName.equalsIgnoreCase("CatName"))
            category.setCateName(currentValue.trim());
        else if (qName.equalsIgnoreCase("Category"))
            categoryList.add(category);

        else if (qName.equalsIgnoreCase("CatId"))
            recipeData.setCatName(currentValue.trim());
        else if (qName.equalsIgnoreCase("Name"))
            recipeData.setName(currentValue.trim());
        else if (qName.equalsIgnoreCase("Ingredients"))
            recipeData.setIngredient(currentValue.trim());
        else if (qName.equalsIgnoreCase("Duration"))
            recipeData.setCookingTime(currentValue.trim());
        else if (qName.equalsIgnoreCase("Steps"))
            recipeData.setSteps(currentValue.trim());
        else if (qName.equalsIgnoreCase("RecipeData"))
            recipeList.add(recipeData);

        currentValue = "";
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }

    }

}