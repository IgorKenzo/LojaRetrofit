package br.senac.lojaretrofit.model

data class Produto(
    var idProduto: Int,
    var nomeProduto: String,
    var descProduto: String?,
    var precProduto: Double,
    var descontoPromocao: Double,
    var qtdMinEstoque: Int,
    var idCategoria: Int,
    var ativoProduto: Boolean
)
