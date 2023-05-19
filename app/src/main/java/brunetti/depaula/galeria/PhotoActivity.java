package brunetti.depaula.galeria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    String photoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inicializa a tela de layout de activity_photo
        setContentView(R.layout.activity_photo);

        //inicializa uma toolbar para a tela
        Toolbar toolbar = findViewById(R.id.tbPhoto);
        setSupportActionBar(toolbar);

        //inicializa uma ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //captura a intent que iniciou a activity
        Intent i = getIntent();
        //captura o uri da foto clicada
        photoPath = i.getStringExtra("photo_path");

        //define um bitmap dado um uri de um foto
        Bitmap bitmap = Util.getBitmap(photoPath);
        //define o bitmap do image view
        ImageView imPhoto = findViewById(R.id.imPhoto);
        imPhoto.setImageBitmap(bitmap);
    }

    @Override
    //se o icone de compartilhar for clicado inicia a funcao sharePhoto
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.opShare:
                sharePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    //inicializa o layout da toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_activity_tb, menu);
        return true;
    }

    void sharePhoto(){
        //cria um uri para um arquivo dado
        Uri photoUri = FileProvider.getUriForFile(PhotoActivity.this, "brunetti.depaula.galeria.fileprovider", new File(photoPath));
        //inicializa uma intent para enviar enviar dados
        Intent i = new Intent(Intent.ACTION_SEND);
        //adiciona o uri que sera enviado
        i.putExtra(Intent.EXTRA_STREAM, photoUri);
        //define o tipo de dado que a intent ira receber
        i.setType("image/jpeg");
        //iniciliza a intent
        startActivity(i);
    }
}