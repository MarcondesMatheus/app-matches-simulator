package com.example.matchessimulator.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//data class classe de transferencia de dados, cria getters e setters automaticamente
@Parcelize
data class Team (
        @SerializedName("nome")
        val name: String,
        @SerializedName("qualidade")
        val star: Int,
        @SerializedName("imagem")
        val image: String,
        var score: Int?
        ) : Parcelable