package uk.ac.tees.mad.w9611189.ui.profile

data class User(
    var name:String?= null,
    var currency: String? = null,
    var expenseCategories: List<String> = emptyList(),
    var incomeCategories: List<String> = emptyList(),
)
