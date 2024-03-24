package com.example.repository;

import com.example.models.Producto;

import java.util.List;

public class ProductoRepository implements IAcciones<Producto>{
    @Override
    public List<Producto> listar() {
        return null;
    }

    @Override
    public Producto ListarId(String entrada) {
        return null;
    }

    @Override
    public Producto crear(Producto modelo) {
        return null;
    }

    @Override
    public Producto actualizar(Producto modelo) {
        return null;
    }

    @Override
    public Producto eliminar(int id) {
        return null;
    }
}
