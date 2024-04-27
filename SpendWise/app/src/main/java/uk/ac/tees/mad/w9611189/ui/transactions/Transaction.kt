package uk.ac.tees.mad.w9611189.ui.transactions

data class Transaction(
    var id:String? = null,
    var isExpense:Boolean = true,
    var amount:String? = null,
    var category:String? = null,
    var timestamp:Long? = 0L,
    var latitude:Double? = 0.0,
    var longitude:Double? = 0.0,
    var imageUrl:String? = null
)
