package com.example.models;

public class Producto {
    private int productoId;
    private String productoNombre;
    private int productoCantidad;
    private double productoPrecioUnitario;


    public Producto(int id, String nombre, int cantidad, double precioUnitario)
    {
        this.productoId = id;
        this.productoNombre = nombre;
        this.productoCantidad = cantidad;
        this.productoPrecioUnitario = precioUnitario;
    }

    public int getProductoId() { return this.productoId; }
    public String getProductoNombre() { return this.productoNombre; }
    public int getProductoCantidad() { return this.productoCantidad; }
    public double getProductoValorUnitario() { return this.productoPrecioUnitario; }

    public void setProductoId(int id) { this.productoId = id; }
    public void setProductoNombre(String nombre) {  this.productoNombre = nombre; }
    public void setProductoCantidad(int cantidad) {  this.productoCantidad = cantidad; }
    public void setProductoValorUnitario(double vu) {  this.productoPrecioUnitario = vu; }


    @Override
    public boolean equals(Object obj)
    {
        if(
            ( (Producto) obj ).getProductoNombre().equals(this.getProductoNombre())
            ||
            ( (Producto) obj ).getProductoId() == this.getProductoId()
        )
            return true;

        return false;
    }
}
