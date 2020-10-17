package com.example.constants

class AppConstants {
    object ProductUrl{
        const val BASE_URL = "api/"
        const val ADD_PRODUCT = "product/add"
        const val ALL_PRODUCT = "product/all"
        const val PRODUCT_BY_ID = "product/{productId}"
        const val PRODUCT_PAGINATION = "product/pagination"
    }
    object Error{
        const val UNAUTHRIZED = "Unauthorized api call"
        const val INTERNAL_SERVER_ERROR = "Internal server error"
        const val BAD_REQUEST = "Parameter mismatch"
    }
}