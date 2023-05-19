package brunetti.depaula.galeria;

import androidx.annotation.NonNull;
import android.Manifest;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
    static int RESULT_REQUEST_PERMISSION = 2;
    String currentPhotoPath;
    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //cria o layout de main activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializa o array de permissoes
        List<String> permissions = new ArrayList<>();
        //adiciona a permissao de acessar a camera
        permissions.add(Manifest.permission.CAMERA);

        //checa pelas permissoes
        checkForPermissions(permissions);

        //seleciona a toolbar da tela e define ela como action bar
        Toolbar toolbar = findViewById(R.id.tbMain);
        setSupportActionBar(toolbar);

        //captura a action bar definida anteriormente
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //captura o diretorio de fotos, percorrer as fotos e adiciona o array
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }

        //inicializa o main adapter e define o o adapter da recycle view como este adapter criado
        mainAdapter = new MainAdapter(MainActivity.this, photos);
        RecyclerView rvGalery = findViewById(R.id.rvGalery);
        rvGalery.setAdapter(mainAdapter);

        //captura a largura de tela, calcula o numero de colunas, e cria um grid layout para ser usado no rvGalery
        float w = getResources().getDimension(R.dimen.itemWidth);
        int numberofColumns = Util.calculateNoOfColumns(MainActivity.this, w);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberofColumns);
        rvGalery.setLayoutManager(gridLayoutManager);
    }

    @Override
    //inicializa o menu da toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }

    @Override
    //define que quando o icone de camera da toolbar for clicada a funcao dispatchTakePictureIntent e excutada
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opCamera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //inicia a photoActivity e passa o caminho de arquivo da foto
    public void startPhotoActivity(String photoPath){
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("photo_path", photoPath);
        startActivity(i);
    }


    private void dispatchTakePictureIntent(){
        File f = null;
        //tenta criar um arquivo de imagem
        try{
            f = createImageFile();
        }
        //caso falhe na criacao do arquivo gera uma mensagem de erro
        catch(IOException e){
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo", Toast.LENGTH_LONG).show();
            return;
        }

        //define o caminho de foto da variavel como o caminho da imagem criada
        currentPhotoPath = f.getAbsolutePath();

        //se a file foi criada executa os comandos abaixo
        if(f != null){
            //gera um uri para um arquivo dado
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "brunetti.depaula.galeria.fileprovider", f);
            //inicia uma intent para capturar uma imagem
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }

    private File createImageFile() throws IOException {
        //captura o momento que a foto foi tirada
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //adiciona jpeg_ a string gerada pela captura do momento que a foto foi tirada
        String imageFileName = "JPEG_" + timeStamp;
        //acesse o diretorio de imagens
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //cria um arquivo jpg no diretorio de imagens com o nome definido
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);
        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //se a foto for tirada adiciona a foto no array photos e notifica o main adapter de que uma nova foto foi inserida foi inserida
        if(requestCode == RESULT_TAKE_PICTURE){
            if(resultCode == Activity.RESULT_OK){
                photos.add(currentPhotoPath);
                mainAdapter.notifyItemInserted(photos.size()-1);
            }
            //caso a foto nao seja tirada deleta o arquivo f
            else{
                File f = new File(currentPhotoPath);
                f.delete();
            }
        }
    }

    private void checkForPermissions(List<String> permissions){
        List<String> permissionsNotGranted = new ArrayList<>();

        //percorre o array de strings, se houver alguma permissao nao concedida, adiciona a mesma no array permissionsNotGranted
        for(String permission : permissions){
            if(!hasPermission(permission)){
                permissionsNotGranted.add(permission);
            }
        }

        //checa se a versao de desenvolvedor e maior ou igual a versao marshmallow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //se houve permissoes nao concedidas, faz a requisicao da permissao
            if(permissionsNotGranted.size() > 0){
            requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
    }

    //checa se o aplicativo possui uma permissao
    private boolean hasPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        final List<String> permissionRejected = new ArrayList<>();
        //checa se o aplicativo possui uma permissao, se nao houver adiciona no array permissionRejected
        if(requestCode == RESULT_REQUEST_PERMISSION){
            for(String permission : permissions){
                if(!hasPermission(permission)){
                    permissionRejected.add(permission);
                }
            }
        }

        //se houver permissoes nao concediadas, mostra um alerta na tela que avisa o usuario que a permissao pedida e necessaria para o aplicativo funcionar
        //tambem mostra um botao que ao ser clicado faz a requisicao das permissoes de novo
        if(permissionRejected.size() > 0){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(shouldShowRequestPermissionRationale(permissionRejected.get(0))){
                    new AlertDialog.Builder(MainActivity.this).setMessage("Para usar essa app é necessário conceder essas permissões").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            requestPermissions(permissionRejected.toArray(new String[permissionRejected.size()]), RESULT_REQUEST_PERMISSION);
                        }
                    }).create().show();
                }
            }
        }
    }

}