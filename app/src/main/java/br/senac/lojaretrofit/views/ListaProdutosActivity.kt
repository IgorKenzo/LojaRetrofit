package br.senac.lojaretrofit.views

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.senac.lojaretrofit.databinding.ActivityListaProdutosBinding
import br.senac.lojaretrofit.databinding.CardItemBinding
import br.senac.lojaretrofit.model.Produto
import br.senac.lojaretrofit.services.ProdutoService
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ListaProdutosActivity : AppCompatActivity() {
    lateinit var binding: ActivityListaProdutosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        atualizarProdutos()
    }
    fun atualizarProdutos() {
        val http = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        //android:usesCleartextTraffic="true">
        //caso o https não funcione, não usar em prod
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oficinacordova.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()

        val service = retrofit.create(ProdutoService::class.java)

        val call = service.list()

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if(response.isSuccessful) {
                    val listaProdutos = response.body()
                    attTela(listaProdutos)
                }
                else {
                    Snackbar.make(
                        binding.container,
                        "Quebrou",
                        Snackbar.LENGTH_LONG
                    ).show()

                    Log.e("Erro_Retrofit",response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Snackbar.make(
                    binding.container,
                    "Não deu certo",
                    Snackbar.LENGTH_LONG
                ).show()

                Log.e("Erro_Retrofit","OnFailure", t)
            }
        }

        call.enqueue(callback)

    }

    fun attTela(listaProdutos : List<Produto>?) {
        binding.container.removeAllViews()

        listaProdutos?.forEach {
            val cardBiding = CardItemBinding.inflate(layoutInflater)
            cardBiding.textNome.text = it.nomeProduto
            cardBiding.textPreco.text = it.precProduto.toString()

            Picasso.get()
                .load("https://oficinacordova.azurewebsites.net/android/rest/produto/image/${it.idProduto}")
                .into(cardBiding.imagem)

            binding.container.addView(cardBiding.root)
        }
    }
}