package cr.ac.unadeca.galeriafrank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import cr.ac.unadeca.galeriafrank.R;
import cr.ac.unadeca.galeriafrank.database.Models.Imagen;
import cr.ac.unadeca.galeriafrank.util.Functions;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;



    public class AgregarImagenActivity extends AppCompatActivity {
        private ImageView imagen;
        private EditText nombre;
        private EditText autor;
        private File foto;
        private Imagen imagenAGuardar;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.agregarimagen);
            imagen = findViewById(R.id.imagen);
            nombre = findViewById(R.id.nombre);
            autor = findViewById(R.id.autor);
            Functions.loadImage(imagen, this);
            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    abrirOpcionesDeImagenes();
                }
            });
            final Button guardar = findViewById(R.id.guardar);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        validar();
                        guardarImagen();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            EasyImage.configuration(this)
                    .setImagesFolderName("galeriaclase")
                    .saveInAppExternalFilesDir()
                    .saveInRootPicturesDirectory();
        }

    private void abrirOpcionesDeImagenes(){
        EasyImage.openChooserWithGallery(this, "De donde desea obtener la imagen?", 0);
    }

    private void validar() throws Exception{
        if (foto == null ){
            throw new IOException("Debe seleccionar una imagen");
        }

        if (nombre.getText().toString().isEmpty() ){
            throw new IOException("Debe escribir el nombre de la imagen");
        }

        if (autor.getText().toString().isEmpty() ){
            throw new IOException("Debe escribir el nombre del autor");
        }
    }
    private void guardarImagen(){
        imagenAGuardar = new Imagen();
        imagenAGuardar.autor = autor.getText().toString();
        imagenAGuardar.nombre = nombre.getText().toString();
        imagenAGuardar.uri = foto.getAbsolutePath();
        imagenAGuardar.save();
        agregarImagen();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                // Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AgregarImagenActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                imagenRetornada(imageFile);
            }
        });
    }

    private void imagenRetornada(File imagenr) {
        foto = imagenr;
        if(foto != null) {
            Functions.loadImage(foto, imagen, this);
        }else{
            Functions.loadImage(imagen, this);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        agregarImagen();
    }

    public  void agregarImagen(){
        if(imagenAGuardar != null){
            setResult(RESULT_OK);
            finish();
        }else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
