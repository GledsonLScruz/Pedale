package com.gledsonluan.pedale.data.network.modelresponse

data class Posts(
    val features: List<Feature>,
    val query: Query,
    val type: String
)

data class Feature(
    val bbox: List<Double>,
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)

data class Query(
    val parsed: Parsed,
    val text: String
)

data class Geometry(
    val coordinates: List<Double>,
    val type: String
)

data class Properties(
    val address_line1: String,
    val address_line2: String,
    val category: String,
    val city: String,
    val country: String,
    val country_code: String,
    val county: String,
    val datasource: Datasource,
    val district: String,
    val formatted: String,
    val hamlet: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val place_id: String,
    val postcode: String,
    val rank: Rank,
    val result_type: String,
    val state: String,
    val state_code: String,
    val suburb: String,
    val village: String
)

data class Datasource(
    val attribution: String,
    val license: String,
    val sourcename: String,
    val url: String
)

data class Rank(
    val confidence: Int,
    val confidence_city_level: Int,
    val importance: Double,
    val match_type: String
)

data class Parsed(
    val city: String,
    val expected_type: String
)