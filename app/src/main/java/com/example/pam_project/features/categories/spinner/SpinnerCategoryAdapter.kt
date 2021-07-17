package com.example.pam_project.features.categories.spinner

import android.content.Context
import android.widget.ArrayAdapter
import com.example.pam_project.features.categories.list.CategoryInformation
import java.util.*

class SpinnerCategoryAdapter(context: Context?, layout: Int) {
    val categories: ArrayAdapter<String?>
    private val categoriesMap: MutableMap<String?, Long?>
    fun update(model: List<CategoryInformation?>?) {
        for (categoryInformation in model!!) {
            categories.add(categoryInformation.getTitle())
            categoriesMap[categoryInformation.getTitle()] = categoryInformation.getId()
        }
        categories.notifyDataSetChanged()
    }

    fun getCategoryById(id: Long): String? {
        for (categoryName in categoriesMap.keys) {
            val categoryId = categoriesMap[categoryName]
            if (categoryId != null && categoryId == id) return categoryName
        }
        return ""
    }

    fun getCategoriesMap(): Map<String?, Long?> {
        return categoriesMap
    }

    init {
        val categoriesNames: List<String?> = ArrayList()
        categoriesMap = HashMap()
        categories = ArrayAdapter(context!!, layout, categoriesNames)
    }
}