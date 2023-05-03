package brunetti.depaula.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

public class PhotoActivity extends AppCompatActivity {

    String photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent i = getIntent();
        photoPath = i.getStringExtra("photo_path");

        Bitmap bitmap = Util.getBitmap(photoPath);
        ImageView imPhoto = findViewById(R.id.imPhoto);
        imPhoto.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.opShare:
                sharePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}