package com.example.pam_project.repositories.lists

import com.example.pam_project.database.lists.ListDao
import com.example.pam_project.database.lists.ListEntity
import com.example.pam_project.database.lists.ListMapper
import com.example.pam_project.database.relationships.ListsWithTasks
import com.example.pam_project.features.lists.list.ListInformation
import io.reactivex.Completable
import io.reactivex.Flowable

class RoomListsRepository(private val listDao: ListDao?, private val mapper: ListMapper?) : ListsRepository {
    override fun getList(listId: Long): Flowable<ListInformation?> {
        return listDao!!.getListById(listId).map { entity: ListEntity? -> mapper!!.toModel(entity) }
    }

    override fun getListWithTasks(listId: Long): Flowable<ListInformation?> {
        return listDao!!.getListsWithTasks(listId).map { entity: ListsWithTasks? -> mapper!!.toListWithTasksModel(entity) }
    }

    override fun insertList(name: String, categoryId: Long): Completable {
        val listEntity = ListEntity(name, categoryId)
        return Completable.fromAction { listDao!!.insertList(listEntity) }
    }

    override fun updateList(id: Long, name: String, categoryId: Long): Completable {
        val listEntity = ListEntity(id, name, categoryId)
        return Completable.fromAction { listDao!!.updateList(listEntity) }
    }

    override fun deleteList(id: Long): Completable {
        val listEntity = listDao!!.getListById(id).blockingFirst()
        return Completable.fromAction { listDao.deleteList(listEntity) }
    }
}