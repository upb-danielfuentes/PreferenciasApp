package com.example.preferenciasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(private val usuarios: List<Usuario>) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDocumento: TextView = view.findViewById(R.id.tvDocumento)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        // Inicializa otras vistas del item si es necesario
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.tvDocumento.text = usuario.documento
        holder.tvNombre.text = usuario.nombre
        // Configura otras vistas del item aquí
    }

    override fun getItemCount() = usuarios.size
}
