package com.example.pam_project.repositories.lists

import com.example.pam_project.features.lists.list.ListInformation
import io.reactivex.Completable
import io.reactivex.Flowable

interface ListsRepository {
    fun getList(id: Long): Flowable<ListInformation?>
    fun insertList(name: String, categoryId: Long): Completable
    fun updateList(id: Long, name: String, categoryId: Long): Completable
    fun deleteList(id: Long): Completable
    fun getListWithTasks(listId: Long): Flowable<ListInformation?>
}