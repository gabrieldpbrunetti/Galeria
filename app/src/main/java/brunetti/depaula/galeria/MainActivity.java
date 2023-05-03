package brunetti.depaula.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;
    String currentPhotoPath;
    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }

        mainAdapter = new MainAdapter(MainActivity.this, photos);
        RecyclerView rvGalery = findViewById(R.id.rvGalery);
        rvGalery.setAdapter(mainAdapter);

        float w = getResources().getDimension(R.dimen.itemWidth);
        int numberofColumns = Util.calculateNoOfColumns(MainActivity.this, w);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberofColumns);
        rvGalery.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opCamera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startPhotoActivity(String photoPath){
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("photo_path", photoPath);
        startActivity(i);
    }

    private void dispatchTakePictureIntent(){
        File f = null;
        try{
            f = createImageFile();
        }
        catch(IOException e){
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }

        currentPhotoPath = f.getAbsolutePath();

        if(f != null){
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "brunetti.depaula.galeria.fileprovider", f);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
        startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File f = File.createTempFile(imageFileName, ".jpg", storageDir);
    return f;
    }

}