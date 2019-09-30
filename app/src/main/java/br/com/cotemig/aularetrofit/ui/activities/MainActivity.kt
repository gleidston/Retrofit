package br.com.cotemig.aularetrofit.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.cotemig.aularetrofit.R
import br.com.cotemig.aularetrofit.model.ListTime
import br.com.cotemig.aularetrofit.services.RetrofitInitializer
import br.com.cotemig.aularetrofit.ui.adapters.TimeAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getListaTime()
    }

    fun getListaTime(){

        var s = RetrofitInitializer().serviceTime()

        var call = s.getTime()

        call.enqueue(object: retrofit2.Callback<ListTime>{

            override fun onResponse(call: Call<ListTime>?, response: Response<ListTime>?) {

                response.let {

                    if(it!!.code() == 200){

                        lista.adapter = TimeAdapter(this@MainActivity, it!!.body().lista)

                    }

                }

            }

            override fun onFailure(call: Call<ListTime>?, t: Throwable?) {

                Toast.makeText(this@MainActivity, "Erro!!!", Toast.LENGTH_LONG).show()

            }

        })

    }
}
