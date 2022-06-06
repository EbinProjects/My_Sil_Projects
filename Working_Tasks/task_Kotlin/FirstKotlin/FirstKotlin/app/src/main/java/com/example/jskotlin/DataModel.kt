package com.example.jskotlin

data class DataModel(
	val chartName: String? = null,
	val bpi: Bpi? = null,
	val time: Time? = null,
	val disclaimer: String? = null
)

data class Bpi(
	val eUR: EUR? = null,
	val gBP: GBP? = null,
	val uSD: USD? = null
)

data class GBP(
	val symbol: String? = null,
	val rateFloat: Double? = null,
	val code: String? = null,
	val rate: String? = null,
	val description: String? = null
)

data class EUR(
	val symbol: String? = null,
	val rateFloat: Double? = null,
	val code: String? = null,
	val rate: String? = null,
	val description: String? = null
)

data class Time(
	val updateduk: String? = null,
	val updatedISO: String? = null,
	val updated: String? = null
)

data class USD(
	val symbol: String? = null,
	val rateFloat: Double? = null,
	val code: String? = null,
	val rate: String? = null,
	val description: String? = null
)

