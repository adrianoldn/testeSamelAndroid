package com.example.testesamel.dao;

import com.example.testesamel.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    private final static List<Usuario> usuarios = new ArrayList<>();

    public void salva(List<Usuario> usuariosNovos) {
        usuarios.addAll(usuariosNovos);
    }

    public List<Usuario> todos() {
        return new ArrayList<>(usuarios);

    }
}
