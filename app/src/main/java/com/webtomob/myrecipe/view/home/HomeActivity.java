package com.webtomob.myrecipe.view.home;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.webtomob.myrecipe.R;
import com.webtomob.myrecipe.database.AppDatabase;
import com.webtomob.myrecipe.database.AppExecutor;
import com.webtomob.myrecipe.model.Category;
import com.webtomob.myrecipe.model.Recipe;
import com.webtomob.myrecipe.utils.AppPreference;
import com.webtomob.myrecipe.utils.PrefConstant;
import com.webtomob.myrecipe.utils.Utils;
import com.webtomob.myrecipe.view.addrecipe.AddRecipeActivity;
import com.webtomob.myrecipe.view.detail.DetailActivity;
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

import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity {
    private FloatingActionButton mAddFloatingActionButton;
    private AppPreference mPreference;
    private AppDatabase mDatabase;
    private Spinner mCatSpinner;
    private RecyclerView mRecipeRecyclerView;
    private ArrayList<Recipe> mRecipeList = new ArrayList<>();
    private boolean isRecyclerViewCreated = false;
    private String mSelectedCat = "";
    private boolean isSpinnerFirst = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.home_title));
        setSupportActionBar(toolbar);

        mDatabase = AppDatabase.getInstance(this);
        mPreference = AppPreference.getInstance(this);
        setUpUI();

        if(!mPreference.getBoolean(PrefConstant.CAT_SYNC)){
            parseXML();
        }
        loadSpinnerData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSelectedCat.isEmpty()) {
            getRecipes(getString(R.string.default_cat));
        }else{
            getRecipes(mSelectedCat);
        }
    }

    private void setUpUI() {
        mRecipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        mAddFloatingActionButton = findViewById(R.id.addFloatingActionButton);
        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HomeActivity.this, AddRecipeActivity.class);
                startActivity(in);

            }
        });

        mCatSpinner = findViewById(R.id.catSpinner);

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

    private void saveRecipeIntoDB(final String catName, final String name, final String ingredients, final String steps, final String duration){
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Recipe recipe = new Recipe(0, catName, name, "", steps, ingredients, duration);
                mDatabase.receipeDao().insertRecipeItem(recipe);
            }
        });
    }

    private void loadSpinnerData() {
        final ArrayList<String> categories = new ArrayList<>();
        categories.add(getString(R.string.default_cat));
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mDatabase.categoryDao().getAllCategory().size(); i++) {
                    categories.add(mDatabase.categoryDao().getAllCategory().get(i).getCateName());
                }
                // Stuff that updates the UI
                if (categories != null) {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplication(), R.layout.custom_spinner_layout, categories);

                    dataAdapter.setDropDownViewResource(R.layout.custom_spinner_layout);
                    mCatSpinner.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                }

                mCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        mSelectedCat = mCatSpinner.getSelectedItem().toString();
                        Log.e(" Cat name is ", mSelectedCat);
                        if(isSpinnerFirst) {
                            getRecipes(mSelectedCat);
                        }
                        isSpinnerFirst = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
    }

    private void settingRecyclerView(){
        HomeAdpater homeAdapter = new HomeAdpater(this, mRecipeList);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecipeRecyclerView.hasFixedSize();
        mRecipeRecyclerView.setAdapter(homeAdapter);

        //Recyclerview On Item click listerner using Rxjava
        homeAdapter.getViewClickedObservable()
                .subscribe(recipeSelectedItem -> {
                    Intent in = new Intent(this, DetailActivity.class);
                    in.putExtra("recipeItem", Utils.g.toJson(recipeSelectedItem, Recipe.class));
                    startActivity(in);
                });
    }

    private void getRecipes(final String selectedCat){
        mRecipeList.clear();
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if(selectedCat.equalsIgnoreCase(getString(R.string.default_cat))) {
                    for (int i = 0; i < mDatabase.receipeDao().getAllReceipe().size(); i++) {
                        Recipe recipe = new Recipe();
                        recipe.setCatName(mDatabase.receipeDao().getAllReceipe().get(i).getCatName());
                        recipe.setName(mDatabase.receipeDao().getAllReceipe().get(i).getName());
                        recipe.setCookingTime(mDatabase.receipeDao().getAllReceipe().get(i).getCookingTime());
                        if (mDatabase.receipeDao().getAllReceipe().get(i).getImageUrl() != null) {
                            recipe.setImageUrl(mDatabase.receipeDao().getAllReceipe().get(i).getImageUrl());
                        } else {
                            recipe.setImageUrl("");
                        }
                        recipe.setIngredient(mDatabase.receipeDao().getAllReceipe().get(i).getIngredient());
                        recipe.setSteps(mDatabase.receipeDao().getAllReceipe().get(i).getSteps());
                        recipe.setId(mDatabase.receipeDao().getAllReceipe().get(i).getId());

                        mRecipeList.add(recipe);
                    }
                }else{
                    for (int i = 0; i < mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).size(); i++) {
                        Recipe recipe = new Recipe();
                        recipe.setCatName(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getCatName());
                        recipe.setName(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getName());
                        recipe.setCookingTime(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getCookingTime());
                        if (mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getImageUrl() != null) {
                            recipe.setImageUrl(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getImageUrl());
                        } else {
                            recipe.setImageUrl("");
                        }
                        recipe.setIngredient(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getIngredient());
                        recipe.setSteps(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getSteps());
                        recipe.setId(mDatabase.receipeDao().loadReceipeItemByCatName(selectedCat).get(i).getId());

                        mRecipeList.add(recipe);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Stuff that updates the UI
                        if(!isRecyclerViewCreated) {
                            settingRecyclerView();
                            isRecyclerViewCreated = true;
                        }else{
                            mRecipeRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}
