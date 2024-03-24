package com.example.models;

public class Tabla {
    private int id;
    private String codigo;
    private String producto;
    private int cantidad;
    private double valorUnitario;

    public Tabla(){ }

    public Tabla(String codigo, String producto, int cantidad, double valorUnitario)
    {
        this.codigo = codigo;
        this.producto = producto;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    public Tabla(int id, String codigo, String producto, int cantidad, double valorUnitario)
    {
        this.id = id;
        this.codigo = codigo;
        this.producto = producto;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
    }

    public int getId() { return this.id; }
    public String getCodigo() { return this.codigo; }
    public String getProducto() { return this.producto; }
    public int getCantidad() { return this.cantidad; }
    public double getValorUnitario() { return this.valorUnitario; }

    public void setId(int id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setProducto(String producto) { this.producto = producto; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }

    @Override
    public boolean equals(Object obj){
        if(
            ((Tabla) obj).getProducto().equals(this.getProducto())
            ||
            ((Tabla) obj).getCodigo().equals(this.getCodigo())
        )
            return true;

        return false;
    }
}
