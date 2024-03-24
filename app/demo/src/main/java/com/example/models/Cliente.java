package com.example.models;

public class Cliente {
    
    private int ciudadano_id;
    
    private String ciudadano_nombre;

    private String ciudadano_apellido;

    private String ciudadano_cedula;

    private String ciudadano_celular;
    
    private String ciudadano_email;

    private String ciudadano_fechaNacimiento;

    private boolean ciudadano_estado;


    public int getCiudadano_Id()
    {
        return this.ciudadano_id;
    }

    public void setCiudadano_Id(int id)
    {
        this.ciudadano_id = id;
    }
  


    public String getCiudadano_nombre()
    {
        return this.ciudadano_nombre;
    }

    public void setCiudadano_nombre(String nombre)
    {
        this.ciudadano_nombre = nombre;
    }




    public String getCiudadano_apellido()
    {
        return this.ciudadano_apellido;
    }

    public void setCiudadano_apellido(String apellido)
    {
        this.ciudadano_apellido = apellido;
    }




    public String getCiudadano_cedula()
    {
        return this.ciudadano_cedula;
    }

    public void setCiudadano_cedula(String cedula)
    {
        this.ciudadano_cedula = cedula;
    }



    public String getCiudadano_celular()
    {
        return this.ciudadano_celular;
    }

    public void setCiudadano_celular(String celular)
    {
        this.ciudadano_celular = celular;
    }


    
    public String getCiudadano_email()
    {
        return this.ciudadano_email;
    }

    public void setCiudadano_email(String email)
    {
         this.ciudadano_email = email;
    }




    public String getCiudadano_fechaNacimiento()
    {
        return this.ciudadano_fechaNacimiento;
    }

    public void setCiudadano_fechaNacimiento(String fechaNacimiento)
    {
        this.ciudadano_fechaNacimiento = fechaNacimiento;
    }



    public boolean getCiudadano_estado()
    {
        return this.ciudadano_estado;
    }

    public void setCiudadano_estado(boolean estado)
    {
        this.ciudadano_estado = estado;
    }

    
}


