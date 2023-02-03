package com.example.fundacionalbornozjimenez.Actividades;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fundacionalbornozjimenez.R;
import com.example.fundacionalbornozjimenez.model.AdministradorPreferencias;
import com.example.fundacionalbornozjimenez.model.myVolleySingleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormularioPub2 extends AppCompatActivity {
    private Button enviarDatos,subirImagen;
    private RadioGroup radioGroupE, radioGroupD, radioGroupV, radioGroupAK,radioGroupAM;
    private EditText enfermedad, extra;
    private String esterilizado,vacunado,desparacitado,enfermedadS,amigableK,amigableM,extraS;

    private String id,fecha,nombre, tipoA,ciudad,estado,edad,raza,size,sexo,personalidad;
    private ImageView imgASubir;

    private Bitmap bitmap;
    private String convertImage;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_pub2);


        Bundle bundle = getIntent().getExtras();
        ArrayList<String> datos = bundle.getStringArrayList("datos");


        Calendar date= Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        fecha = formato.format(date.getTime());

        id=datos.get(0);
        tipoA=datos.get(1);
        nombre=datos.get(2);
        ciudad=datos.get(3);
        estado=datos.get(4);
        edad=datos.get(5);
        raza=datos.get(6);
        size=datos.get(7);
        sexo=datos.get(8);
        personalidad=datos.get(9);


        enviarDatos=findViewById(R.id.buttonEnviarDatos);
        imgASubir=findViewById(R.id.imagenSubida);
        subirImagen=findViewById(R.id.buttonImagen);
        byteArrayOutputStream=new ByteArrayOutputStream();

        radioGroupE=findViewById(R.id.radioGroupEM);
        radioGroupD=findViewById(R.id.radioGroupDM);
        radioGroupV=findViewById(R.id.radioGroupVM);
        radioGroupAK=findViewById(R.id.radioGroupAK);
        radioGroupAM=findViewById(R.id.radioGroupAM);

        enfermedad=findViewById(R.id.cEnfermedadesM);
        extra=findViewById(R.id.cExtraM);


        enviarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id1=radioGroupE.getCheckedRadioButtonId();
                int id2=radioGroupD.getCheckedRadioButtonId();
                int id3=radioGroupV.getCheckedRadioButtonId();
                int id4=radioGroupAK.getCheckedRadioButtonId();
                int id5=radioGroupAM.getCheckedRadioButtonId();

                try {
                    RadioButton seleccion=radioGroupE.findViewById(id1);
                    esterilizado=seleccion.getText().toString();
                }catch (Exception exception){

                    Toast.makeText(getApplicationContext(),"Indica si la mascota se encuentra " +
                            "esterilizada", Toast.LENGTH_SHORT).show();
                }

                try {
                    RadioButton seleccion=radioGroupD.findViewById(id2);
                    desparacitado=seleccion.getText().toString();
                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"Indica si la mascota se encuentra " +
                            "desparacitada", Toast.LENGTH_SHORT).show();
                }

                try {
                    RadioButton seleccion=radioGroupV.findViewById(id3);
                    vacunado=seleccion.getText().toString();
                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"Indica si la mascota se encuentra " +
                            "vacunada", Toast.LENGTH_SHORT).show();
                }

                try {
                    RadioButton seleccion=radioGroupAK.findViewById(id4);
                    amigableK=seleccion.getText().toString();
                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"Indica si la mascota es amigable " +
                            "con niños", Toast.LENGTH_SHORT).show();
                }

                try {
                    RadioButton seleccion=radioGroupAM.findViewById(id5);
                    amigableM=seleccion.getText().toString();
                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"Indica si la mascota es amigable" +
                            "con mascotas", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(esterilizado) || TextUtils.isEmpty(desparacitado) || TextUtils.isEmpty(vacunado)
                        || TextUtils.isEmpty(amigableK) || TextUtils.isEmpty(amigableM) || imgASubir.getDrawable()==null){
                    Toast.makeText(getApplicationContext(),"Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
                    enviarDatos.setError("Completa los datos obligatorios");
                    if (imgASubir.getDrawable()==null){
                        subirImagen.setError("Sube una foto de la mascota");
                    }
                }else{
                    if (esterilizado.equals("Sí")) esterilizado = "Si";
                    if (vacunado.equals("Sí")) vacunado="Si";
                    if (desparacitado.equals("Sí")) desparacitado="Si";
                    if (amigableK.equals("Sí")) amigableK="Si";
                    if (amigableM.equals("Sí")) amigableM="Si";

                    enfermedadS=enfermedad.getText().toString();
                    extraS=extra.getText().toString();

                    if(TextUtils.isEmpty(estado)) estado="Desconocido";
                    if(TextUtils.isEmpty(raza)) raza="Desconocido";
                    if(TextUtils.isEmpty(enfermedadS)) enfermedadS="Desconocido";
                    if(TextUtils.isEmpty(extraS))  extraS="";


                    String u= new AdministradorPreferencias(getApplicationContext()).correo();
                    enviarDatos(u);

                }

            }
        });

        subirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialog();
            }
        });


    }

    private void mostrarDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        String[] pictureDialogItem={"Galería de fotos","Cámara"};

        alertDialog.setItems(pictureDialogItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        fotoGaleria();
                        break;
                    case 1:
                        fotoCamara();
                        break;
                }
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void fotoGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launcher.launch(intent);
    }


    private void fotoCamara() {
        launcher2.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

    }


    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode()== Activity.RESULT_CANCELED){
                    return;
                }
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                        Matrix m = new Matrix();
                        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, imgASubir.getHeight(), imgASubir.getWidth()), Matrix.ScaleToFit.CENTER);
                        Bitmap bitmapScaled= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                        imgASubir.setImageBitmap(bitmapScaled);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
    );


    private final ActivityResultLauncher<Intent> launcher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==Activity.RESULT_CANCELED){
                        return;
                    }if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras=result.getData().getExtras();
                        try {
                            bitmap=(Bitmap) extras.get("data");
                            imgASubir.setImageBitmap(bitmap);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }

    );


    private void enviarDatos(String u){
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byteArray=byteArrayOutputStream.toByteArray();
        convertImage= Base64.getEncoder().encodeToString(byteArray);
        if(TextUtils.isEmpty(convertImage)){
            Toast.makeText(getApplicationContext(),"Imagen vacia",Toast.LENGTH_SHORT).show();
        }

        String url="https://fundacionalbornoz.com/RegistrarMascota.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(response.contains("Registro exitoso3")){
                    Toast.makeText(getApplicationContext(),"Mascota registrada exitosamente",Toast.LENGTH_SHORT).show();
                    int value=3;
                    Intent intent = new Intent(FormularioPub2.this, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    startActivity(intent);
                    FormularioPublicacion.fp.finish();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Error al intentar registrar mascota",Toast.LENGTH_SHORT).show();
                    int value=3;
                    Intent intent = new Intent(FormularioPub2.this, PaginaPrincipal.class);
                    intent.putExtra("key",value);
                    startActivity(intent);
                    FormularioPublicacion.fp.finish();
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"No se ha podido registrar a la mascota",Toast.LENGTH_SHORT).show();
                int value=3;
                Intent intent = new Intent(FormularioPub2.this, PaginaPrincipal.class);
                intent.putExtra("key",value);
                startActivity(intent);
                FormularioPublicacion.fp.finish();
                finish();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> parametros=new HashMap<>();

                parametros.put("correo",u);
                parametros.put("fecha",fecha);
                parametros.put("id_ref_res",id);
                parametros.put("nombre",nombre);
                parametros.put("tipoA",tipoA);
                parametros.put("ciudad",ciudad);
                parametros.put("estado",estado);
                parametros.put("edad",edad);
                parametros.put("raza",raza);
                parametros.put("size",size);
                parametros.put("sexo",sexo);
                parametros.put("personalidad",personalidad);
                parametros.put("esterilizado",esterilizado);
                parametros.put("vacunado",vacunado);
                parametros.put("desparacitado",desparacitado);
                parametros.put("enfermedades",enfermedadS);
                parametros.put("amigablek",amigableK);
                parametros.put("amigableM",amigableM);
                parametros.put("extra",extraS);
                parametros.put("fotoMascota",convertImage);

                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myVolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}