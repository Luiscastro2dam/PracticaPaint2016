package com.example.clase.practicapaint2016;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Vista vista;
    private Button btColor;
    private Button btNuevo;
    private Button btTamanoPincel;
    private Button btFiguras;
    private Button btGoma;
    EditText ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        vista = (Vista)findViewById(R.id.view);

        ed = (EditText)findViewById(R.id.etGrosorPincel);

        btColor = (Button)findViewById(R.id.btColor);
        btColor.setOnClickListener(this);
        btNuevo = (Button)findViewById(R.id.btNuevo);
        btNuevo.setOnClickListener(this);
        btTamanoPincel = (Button)findViewById(R.id.btTamanoPincel);
        btTamanoPincel.setOnClickListener(this);
        btFiguras = (Button)findViewById(R.id.btfiguras);
        btFiguras.setOnClickListener(this);
        btGoma = (Button)findViewById(R.id.btGoma);
        btGoma.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //------------menu principal--------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.guardar: {
                final Dialog dialogo = new Dialog(this);
                dialogo.setTitle("Nombre dibujo:");
                dialogo.setContentView(R.layout.archivo);
                Button pincelGrande = (Button) dialogo.findViewById(R.id.btarchivo);
                pincelGrande.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText edt = (EditText)dialogo.findViewById(R.id.editText);
              //metodo de internet
                         vista.setDrawingCacheEnabled(true);

                         MediaStore.Images.Media.insertImage(
                                getContentResolver(), vista.getDrawingCache(),
                                edt.toString() + ".png", "drawing");

                        String nombre =edt.getText().toString();
//                        try {
//                          //  vista.guardarImagen(nombre);
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
                        dialogo.dismiss();
                    }
                });
                dialogo.show();
                return true;
            }
            case R.id.Abrir: {
              FotoInternas();
                return true;
            }
            case R.id.Multitouch: {
                setContentView(new Multitouch(this));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public  static final int REQUEST_IMAGE_GET = 1;
    public void FotoInternas(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode ==
                RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                vista.ponerFotoBuscada(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//------------onclick del view al implementarle el onclick view------------------------
    @Override
    public void onClick(View v) {
            String color ="";
        switch (v.getId()) {
            case R.id.btColor:
                  this.listaColores();
                break;
            case R.id.btNuevo:
                this.nuevoDibujo();
                break;
            case R.id.btTamanoPincel:
                this.tamanoPincel();
                break;
            case R.id.btfiguras:
                this.figuras();
                break;
            case R.id.btGoma:
                 vista.borrado(true);
                break;
        }
    }
//------------------------------------------------------------------------------------
//metodos de todas las opciones

    public void figuras(){
        final Dialog dialogo = new Dialog(this);
        dialogo.setTitle("Formas Geometricas:");
        dialogo.setContentView(R.layout.figuras);
        //figura cuadrado
        Button figur = (Button) dialogo.findViewById(R.id.btCuadrado);
        figur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cuadrado
                vista.dibujarCuadrado();
                dialogo.dismiss();
            }
        });
        //figura circulo
        Button figura = (Button) dialogo.findViewById(R.id.btCirculo);
        figura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cuadrado
                vista.dibujarCirculo();
                dialogo.dismiss();
            }
        });
        //figura linea
        Button figure = (Button) dialogo.findViewById(R.id.btLinea);
        figure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cuadrado
                vista.dibujarLinea();
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
    public void tamanoPincel(){
        final Dialog dialogo = new Dialog(this);
        dialogo.setTitle("Tamaño del pincel:");
        dialogo.setContentView(R.layout.pinceles);
        //pincel grande
        Button pincelGrande = (Button) dialogo.findViewById(R.id.btPincelGrandeX);
        pincelGrande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                float a =300;
                EditText edt = (EditText)dialogo.findViewById(R.id.etGrosorPincel);
                System.out.println("luis22"+edt.getText().toString());
                a=Float.parseFloat(edt.getText().toString());
                vista.tamanoPincel(a);
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
    public void nuevoDibujo(){
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("Dibujo Nuevo");
        newDialog.setMessage("¿Comenzar nuevo dibujo perderas el dibujo actual?");
        newDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                vista.nuevoDibujo();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        newDialog.show();
    }
    public void listaColores() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Color de pintura:");
        dialog.setContentView(R.layout.colores);

        //color rojo
        Button smallBtn = (Button) dialog.findViewById(R.id.btRojo);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos

                vista.setColor(0xffff0000);
                Toast.makeText(getApplicationContext(), "ddddddddddddddd", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        //color amarrillo
        Button btAmarillo = (Button) dialog.findViewById(R.id.btAmarillo);
        btAmarillo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0xffffff00);
                dialog.dismiss();
            }
        });
        //color negro
        Button btNegro = (Button) dialog.findViewById(R.id.btNegro);
        btNegro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0xff000000);
                dialog.dismiss();
            }
        });
        //color blanco
        Button btblanco = (Button) dialog.findViewById(R.id.btBlanco);
        btblanco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0xffffffff);
                dialog.dismiss();
            }
        });
        //color gris
        Button btGris = (Button) dialog.findViewById(R.id.btGrris);
        btGris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0xff888888);
                dialog.dismiss();
            }
        });
        //color azul
        Button btAzul = (Button) dialog.findViewById(R.id.butbtAzlton5);
        btAzul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0xff0000ff);
                dialog.dismiss();
            }
        });
        //color verde
        Button btverde = (Button) dialog.findViewById(R.id.btVerde);
        btverde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0xff00ff00);
                dialog.dismiss();
            }
        });
        //color naranja
        Button btnaran = (Button) dialog.findViewById(R.id.btNaranja);
        btnaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entra aqui a elejido color lo recojemos
                vista.setColor(0x01060018);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    }

