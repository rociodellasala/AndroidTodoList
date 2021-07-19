package com.example.pam_project.features.lists.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pam_project.R
import com.example.pam_project.features.categories.list.CategoryInformation
import java.util.*

class ListAdapter : RecyclerView.Adapter<ListViewHolder>(), Filterable {
    private val dataSet: MutableList<ListInformation?>
    private val hiddenItems: MutableList<ListInformation?>
    private val completeDataset: MutableList<ListInformation?>
    val previousSearchDataset: MutableList<ListInformation?>
    private var searchItems: List<ListInformation?>
    private val categoriesWithIds: MutableMap<Long, CategoryInformation?>
    private var listener: OnListClickedListener? = null
    var categories: List<CategoryInformation?>

    var sortIndex: Int
        get() = Companion.sortIndex
        set(sortIndex) {
            Companion.sortIndex = sortIndex
            sort()
            notifyDataSetChanged()
        }

    fun update(newDataSet: List<ListInformation?>?) {
        dataSet.clear()
        if (newDataSet != null) dataSet.addAll(newDataSet)
        filterSelections = Companion.filterSelections // sort and filter accordingly
        notifyDataSetChanged()
    }

    fun updateCategories(newCategories: List<CategoryInformation?>?) {
        categoriesWithIds.clear()
        if (newCategories != null) {
            for (category in newCategories) {
                categoriesWithIds[category!!.id] = category
            }
        }
        categories = ArrayList(categoriesWithIds.values)
        for (list in dataSet) {
            list?.category = categoriesWithIds[list?.categoryId]
        }
        if (Companion.filterSelections == null) return

        // reset category selections if one category has been deleted and was selected
        for (i in Companion.filterSelections!!.indices) {
            if (!categoriesWithIds.containsKey(filterSelections!![i].toLong())) {
                Companion.filterSelections = null
                return
            }
        }
    }

    fun setOnClickedListener(listener: OnListClickedListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_view_holder, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    // get ids from selected categories

    // hide items not in selected categories and remove them from dataSet

    // show items in selected categories and remove them from hidden items
    var filterSelections: MutableList<Int>?
        get() = Companion.filterSelections
        set(newFilterSelections) {
            if (newFilterSelections == null) {
                sort()
                return
            }
            val selectedCategoriesIds: MutableList<Long> = ArrayList(
                    newFilterSelections.size)
            // get ids from selected categories
            for (index in newFilterSelections) selectedCategoriesIds.add(categories[index]?.id!!)

            // hide items not in selected categories and remove them from dataSet
            moveToList(dataSet, hiddenItems) { id: Long -> !selectedCategoriesIds.contains(id) }

            // show items in selected categories and remove them from hidden items
            moveToList(hiddenItems, dataSet) { o: Long -> selectedCategoriesIds.contains(o) }
            removeDuplicates(dataSet)
            removeDuplicates(hiddenItems)
            sort()
            notifyDataSetChanged()
            setPreviousSearchDataset()
            Companion.filterSelections = newFilterSelections
        }

    fun setCompleteDataset(dataset: List<ListInformation?>?) {
        completeDataset.clear()
        completeDataset.addAll(dataset!!)
    }

    fun setPreviousSearchDataset() {
        previousSearchDataset.clear()
        previousSearchDataset.addAll(dataSet)
    }

    private fun sort() {
        Collections.sort(dataSet, ListInformation.getComparator(Companion.sortIndex))
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                searchItems = if (charString.isEmpty()) {
                    completeDataset
                } else {
                    val filteredList: MutableList<ListInformation?> = ArrayList()
                    for (listInformation in completeDataset) {
                        if (listInformation?.title?.lowercase()?.contains(charString.lowercase())!!) {
                            filteredList.add(listInformation)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchItems
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                val result = filterResults.values as List<*>
                val tempList = ArrayList<ListInformation?>()
                for (`object` in result) {
                    if (`object` is ListInformation) {
                        tempList.add(`object` as ListInformation?) // <-- add to temp
                    }
                }
                dataSet.clear()
                dataSet.addAll(tempList)
                notifyDataSetChanged()
            }
        }
    }

    internal fun interface ListCheck {
        fun condition(id: Long): Boolean
    }

    private fun moveToList(from: MutableList<ListInformation?>, to: MutableList<ListInformation?>,
                           function: ListCheck) {
        // show items in dataSet or hidden categories
        for (item in from) {
            if (function.condition(item?.categoryId!!)) {
                to.add(item)
            }
        }
        // remove shown items from hidden elements or dataSet
        for (item in to) from.remove(item)
    }

    private fun removeDuplicates(list: MutableList<ListInformation?>) {
        val listNoDuplicates: MutableList<ListInformation?> = ArrayList()
        val idSet: MutableSet<Long> = HashSet()
        for (listInformation in list) {
            if (!idSet.contains(listInformation?.id)) {
                idSet.add(listInformation?.id!!)
                listNoDuplicates.add(listInformation)
            }
        }
        list.clear()
        list.addAll(listNoDuplicates)
    }

    companion object {
        private var filterSelections: MutableList<Int>? = null
        private var sortIndex = 0
    }

    init {
        dataSet = ArrayList()
        categoriesWithIds = HashMap()
        categories = ArrayList(categoriesWithIds.values)
        hiddenItems = ArrayList()
        completeDataset = ArrayList()
        previousSearchDataset = ArrayList()
        searchItems = ArrayList()
    }
}