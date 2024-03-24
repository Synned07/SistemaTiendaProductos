package com.example.repository;

import java.util.List;

public interface IAcciones<T> {

    public List<T> listar();

    public T ListarId(String entrada);

    public T crear(T modelo);

    public T actualizar(T modelo);

    public T eliminar(int id);
}
