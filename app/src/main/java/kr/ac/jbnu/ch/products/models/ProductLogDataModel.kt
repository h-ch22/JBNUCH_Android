package kr.ac.jbnu.ch.products.models

data class ProductLogDataModel(val number : String,
                               val productName : String,
                                val isReturned : Boolean,
                                val dateTime : String)