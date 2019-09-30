package br.com.cotemig.aularetrofit.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import br.com.cotemig.aularetrofit.R
import br.com.cotemig.aularetrofit.model.Time
import com.bumptech.glide.Glide

class TimeAdapter(var context: Context, var lista: List<Time>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        var view = inflater.inflate(R.layout.item_time, null)

        var escudo = view.findViewById<ImageView>(R.id.escudo)
        var nome = view.findViewById<TextView>(R.id.nome)
        var estado = view.findViewById<TextView>(R.id.estado)

        nome.text = lista[position].nome
        estado.text = lista[position].estado

        Glide.with(context)
            .load(lista[position].escudo)
            .into(escudo)

        return view

    }

    override fun getItem(position: Int): Any {
        return ""
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return lista.size
    }
}