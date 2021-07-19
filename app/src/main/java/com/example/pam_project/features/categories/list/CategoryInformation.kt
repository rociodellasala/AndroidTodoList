package com.example.pam_project.features.categories.list

import com.example.pam_project.utils.constants.AppColor
import java.io.Serializable

open class CategoryInformation(title: String, color: AppColor?) : Serializable, Comparable<CategoryInformation> {
    var title: String = title
        private set
    var color: AppColor? = color
        private set
    var id: Long = 0
        private set

    constructor(title: String, color: String?) : this(title, AppColor.fromName(color))
    constructor(id: Long, title: String, color: AppColor?) : this(title, color) {
        this.id = id
    }

    constructor(id: Long, title: String, color: String?) : this(id, title, AppColor.fromName(color))

    override fun compareTo(other: CategoryInformation): Int {
        return title.compareTo(other.title)
    }
}