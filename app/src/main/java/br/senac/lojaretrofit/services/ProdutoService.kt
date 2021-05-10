package br.senac.lojaretrofit.services

import br.senac.lojaretrofit.model.Produto
import retrofit2.Call
import retrofit2.http.GET

interface ProdutoService {
    @GET("/android/rest/produto")
    fun list() : Call<List<Produto>>
}