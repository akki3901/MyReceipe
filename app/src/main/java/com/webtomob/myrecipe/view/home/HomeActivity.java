package com.webtomob.myrecipe.view.home;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.webtomob.myrecipe.R;
import com.webtomob.myrecipe.database.AppDatabase;
import com.webtomob.myrecipe.database.AppExecutor;
import com.webtomob.myrecipe.model.Category;
import com.webtomob.myrecipe.model.Recipe;
import com.webtomob.myrecipe.utils.AppPreference;
import com.webtomob.myrecipe.utils.PrefConstant;
import com.webtomob.myrecipe.view.addrecipe.AddRecipeActivity;
import com.webtomob.myrecipe.xmlparsing.RecipeXMLHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class HomeActivity extends AppCompatActivity {
    private FloatingActionButton mAddFloatingActionButton;
    private AppPreference mPreference;
    private AppDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mDatabase = AppDatabase.getInstance(this);
        mPreference = AppPreference.getInstance(this);
        setUpUI();

        Log.e("SYNC STATUS ", mPreference.getBoolean(PrefConstant.CAT_SYNC) + " .. ");
        if(!mPreference.getBoolean(PrefConstant.CAT_SYNC)){
            parseXML();
        }

        retrieveCategory();
    }

    private void setUpUI() {
        mAddFloatingActionButton = findViewById(R.id.addFloatingActionButton);
        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HomeActivity.this, AddRecipeActivity.class);
                startActivity(in);

            }
        });
    }

    private void parseXML() {
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            InputStream is = assetManager.open("recipetypes.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            RecipeXMLHandler myXMLHandler = new RecipeXMLHandler();
            xr.setContentHandler(myXMLHandler);
            InputSource inStream = new InputSource(is);
            xr.parse(inStream);

            ArrayList<Recipe> recipeList = myXMLHandler.getRecipeList();
            ArrayList<Category> categoryList = myXMLHandler.getCategoryList();

            for(int i = 0; i< recipeList.size(); i++) {
                Log.v("List is CATID ", recipeList.get(i).getCatName());
                Log.v("List is NAME", recipeList.get(i).getName());
                Log.v("List is INGREDIENTS", recipeList.get(i).getIngredient());
                Log.v("List is STEPS", recipeList.get(i).getSteps());
                Log.v("List is DURATION", recipeList.get(i).getCookingTime());

                saveRecipeIntoDB(recipeList.get(i).getCatName(), recipeList.get(i).getName(), recipeList.get(i).getIngredient(),
                        recipeList.get(i).getSteps(), recipeList.get(i).getCookingTime());
            }

            for(int j=0; j<categoryList.size(); j++){
                saveCategoryIntoDB(categoryList.get(j).getCatId(), categoryList.get(j).getCateName());
            }

            mPreference.putBoolean(PrefConstant.CAT_SYNC, true);

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveCategoryIntoDB(final String catId, final String catName){
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Category category = new Category(0,catId, catName);
                mDatabase.categoryDao().insertCategoryItem(category);
            }
        });
    }

    private void saveRecipeIntoDB(final String catId, final String name, final String ingredients, final String steps, final String duration){
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Recipe recipe = new Recipe(0,catId, name, "", steps, ingredients, duration);
                mDatabase.receipeDao().insertRecipeItem(recipe);
            }
        });
    }

    private void retrieveCategory(){
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.e(" Cate Value ", mDatabase.categoryDao().getAllCategory().get(0).getId() + " .... ");
                Log.e(" Cate Value ", mDatabase.categoryDao().getAllCategory().get(1).getId() + " .... ");
                Log.e(" Cate Value ", mDatabase.categoryDao().getAllCategory().get(2).getId() + " .... ");
                Log.e(" Cate Value ", mDatabase.categoryDao().getAllCategory().get(2).getCateName() + " .... ");
                /*Gson gson = new GsonBuilder().setLenient().create();
                TypeAdapter<GetTicketNewResponse> ticketResponseTypeAdapter = gson.getAdapter(GetTicketNewResponse.class);
                try {
                    GetTicketNewResponse ticketResponse = ticketResponseTypeAdapter.fromJson(mDatabase.ticketDao().getAllTickets().get(0).getJsonValue());
                    ticketList = ticketResponse.getData();

                    if(mDatabase.ticketDao().getAllTickets().get(0).getUserName().equals(SessionManager.getInstance(fragment.mContext).getUserEmail())) {
                        (fragment.getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Stuff that updates the UI
                                preparingTicketRecyclerView();
                            }
                        });
                    }
                } catch (Exception json) {
                    Log.e("Ticket DB exception ", json.getMessage());
                }*/
            }
        });
    }
}
