package com.webtomob.myreceipe.view.home;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.webtomob.myreceipe.R;
import com.webtomob.myreceipe.model.Category;
import com.webtomob.myreceipe.model.Receipe;
import com.webtomob.myreceipe.view.add_receipe.AddReceipeActivity;
import com.webtomob.myreceipe.xmlparsing.ReceipeXMLHandler;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        setUpUI();

        parseXML();
    }

    private void setUpUI() {
        mAddFloatingActionButton = findViewById(R.id.addFloatingActionButton);
        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), AddReceipeActivity.class);
                startActivity(in);

            }
        });
    }

    private void parseXML() {
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            InputStream is = assetManager.open("receipetypes.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            ReceipeXMLHandler myXMLHandler = new ReceipeXMLHandler();
            xr.setContentHandler(myXMLHandler);
            InputSource inStream = new InputSource(is);
            xr.parse(inStream);

            ArrayList<Receipe> receipeList = myXMLHandler.getReceipeList();
            ArrayList<Category> categoryList = myXMLHandler.getCategoryList();

            for(int i=0; i<receipeList.size(); i++) {
                Log.v("List is CATID ", receipeList.get(i).getCatId());
                Log.v("List is NAME", receipeList.get(i).getName());
                Log.v("List is INGREDIENTS", receipeList.get(i).getIngredient());
                Log.v("List is STEPS", receipeList.get(i).getSteps());
                Log.v("List is DURATION", receipeList.get(i).getCookingTime());
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
