package com.webtomob.myreceipe.xml_parsing;

import com.webtomob.myreceipe.model.Category;
import com.webtomob.myreceipe.model.Receipe;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ReceipeXMLHandler extends DefaultHandler {
    boolean currentElement = false;
    String currentValue = "";

    Receipe receipeData;
    ArrayList<Receipe> receipeList;
    Category category;
    ArrayList<Category> categoryList;

    public ArrayList<Receipe> getReceipeList() {
        return receipeList;
    }

    public ArrayList<Category> getCategoryList(){
        return categoryList;
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;

        if (qName.equals("ReceipeTypes")){
            receipeList = new ArrayList<Receipe>();
            categoryList = new ArrayList<Category>();
        }
        else if (qName.equals("Category")) {
            category = new Category();
        }
        else if (qName.equals("ReceipeData")) {
            receipeData = new Receipe();
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
            receipeData.setCatId(currentValue.trim());
        else if (qName.equalsIgnoreCase("Name"))
            receipeData.setName(currentValue.trim());
        else if (qName.equalsIgnoreCase("Ingredients"))
            receipeData.setIngredient(currentValue.trim());
        else if (qName.equalsIgnoreCase("Duration"))
            receipeData.setCookingTime(currentValue.trim());
        else if (qName.equalsIgnoreCase("Steps"))
            receipeData.setSteps(currentValue.trim());
        else if (qName.equalsIgnoreCase("ReceipeData"))
            receipeList.add(receipeData);

        currentValue = "";
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }

    }

}