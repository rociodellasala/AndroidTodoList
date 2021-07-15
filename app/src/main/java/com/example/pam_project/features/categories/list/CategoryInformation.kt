package com.example.pam_project.features.categories.list

import com.example.pam_project.utils.constants.AppColor
import java.io.Serializable

class CategoryInformation(val title: String?, val color: AppColor?) : Serializable, Comparable<CategoryInformation> {
    var id: Long = 0
        private set

    constructor(title: String?, color: String?) : this(title, AppColor.Companion.fromName(color)) {}
    constructor(id: Long, title: String?, color: AppColor?) : this(title, color) {
        this.id = id
    }

    constructor(id: Long, title: String?, color: String?) : this(id, title, AppColor.Companion.fromName(color)) {}

    override fun compareTo(categoryInformation: CategoryInformation): Int {
        return title!!.compareTo(categoryInformation.title!!)
    }
}