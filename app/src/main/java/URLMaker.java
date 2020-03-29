import androidx.appcompat.app.AppCompatActivity;

import com.chrisgreenup.recipeinfo.R;

public class URLMaker extends AppCompatActivity{
    public String makeSearchURL(String searchTerms){
        return "https://api.nal.usda.gov/fdc/v1/search?api_key=" +
                getResources().getString(R.string.api_key) + "&generalSearchInput=" + searchTerms;
    }

    public String makeFoodIdURL(String foodId){
        return "https://api.nal.usda.gov/fdc/v1/" + foodId + "?api_key=" +
                getResources().getString(R.string.api_key);
    }
}