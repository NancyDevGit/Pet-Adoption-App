package com.example.fundacionalbornozjimenez.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AdministradorPreferencias {

    Context context;

    public AdministradorPreferencias(Context context) {
        this.context = context;
    }


    public void guardarSoloCorreo(String password){

        String encriptar=null;
        try{
            encriptar=MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }  catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences=null;

        try {
            sharedPreferences= EncryptedSharedPreferences.create("loginEncrypted",encriptar,context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sharedPreferences.edit().putString("pss",password).apply();

    }

    public String correo() {

        String encriptar=null;
        try{
            encriptar=MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }  catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences=null;

        try {
            sharedPreferences= EncryptedSharedPreferences.create("loginEncrypted",encriptar,context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sharedPreferences.getString("correo", "");
    }

    public String pss() {

        String encriptar=null;
        try{
            encriptar=MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }  catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences=null;

        try {
            sharedPreferences= EncryptedSharedPreferences.create("loginEncrypted",encriptar,context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sharedPreferences.getString("pss", "");
    }

    public boolean checarSesionCerrada() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginEncrypted", Context.MODE_PRIVATE);

        boolean isEmailEmpty = correo().isEmpty();
        boolean isPasswordEmpty = pss().isEmpty();

        return isEmailEmpty || isPasswordEmpty;
    }

    public void sharedEncrypted (String correo, String pss){
        String encriptar=null;
        try{
            encriptar=MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        }  catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences=null;

        try {
            sharedPreferences= EncryptedSharedPreferences.create("loginEncrypted",encriptar,context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sharedPreferences.edit().putString("correo",correo).putString("pss",pss).apply();

    }


}
