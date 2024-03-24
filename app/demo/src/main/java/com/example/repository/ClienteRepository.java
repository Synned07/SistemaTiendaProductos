package com.example.repository;

import com.example.models.Cliente;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class ClienteRepository {

    public static Cliente VerificarCiudadano(String cedula)
    {
        try {
            OkHttpClient c = new OkHttpClient();
            String _url = "http://localhost:5260/obtenerCiudadano?cedula="+cedula;

            Request request = new Request.Builder()
                    .url(_url)
                    .build();

            Response response = c.newCall(request).execute();
            JSONObject objeto = new JSONObject(response.body().string());

            Cliente cliente = new Cliente();
            cliente.setCiudadano_Id(objeto.getInt("ciudadano_id"));
            cliente.setCiudadano_nombre(objeto.getString("ciudadano_nombre"));
            cliente.setCiudadano_apellido(objeto.getString("ciudadano_apellido"));
            cliente.setCiudadano_cedula(objeto.getString("ciudadano_cedula"));
            cliente.setCiudadano_celular(objeto.getString("ciudadano_celular"));
            cliente.setCiudadano_email(objeto.getString("ciudadano_email"));
            cliente.setCiudadano_fechaNacimiento(objeto.getString("ciudadano_fechaNacimiento"));
            cliente.setCiudadano_estado(objeto.getBoolean("ciudadano_estado"));

            if(cliente.getCiudadano_estado())
            {
                return cliente;
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(" ! Error en la conexion a la api de consulta de clientes... ");
            return null;
        }
        return null;
    }
}
