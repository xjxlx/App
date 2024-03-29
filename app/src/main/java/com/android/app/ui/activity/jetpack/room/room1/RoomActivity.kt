package com.android.app.ui.activity.jetpack.room.room1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.app.R
import com.android.app.databinding.ActivityRoomBinding
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.helper.interfaces.room.RoomDeleteListener
import com.android.helper.interfaces.room.RoomInsertListener
import com.android.helper.interfaces.room.RoomQueryListener
import com.android.helper.interfaces.room.RoomUpdateListener
import com.android.helper.utils.ToastUtil
import com.android.helper.utils.room.RoomUtil
import com.android.helper.utils.room.SQLEntity

/**
 * room数据库的使用 使用逻辑：
 * ```
 *      一：Entity注解类： entity： [ˈentəti] 嗯特忒
 *         1：创建一个文件，使用entity去注解，标记为一个数据库为实体类,所有的字段都在表中存储着
 *
 *      二：创建数据库的操作方法，使用@Dao去注解
 * ```
 * 使用的好处：
 */
class RoomActivity : BaseBindingTitleActivity<ActivityRoomBinding>() {

  private lateinit var roomManager: RoomDataBaseHelper
  private val observer = Observer<RoomEntityLiveData> { t -> ToastUtil.show("返回的数据为：" + t) }
  private var versionRoom: RoomDataBaseHelper? = null
  private val mRoomUtil by lazy {
    return@lazy RoomUtil.getInstance()
  }

  override fun initListener() {
    super.initListener()
    setonClickListener(
        mBinding.btnAddSingle,
        mBinding.btnAddList,
        mBinding.btnDeleteSingle,
        mBinding.btnDeleteList,
        mBinding.btnUpdateId,
        mBinding.btnUpdateEntity,
        mBinding.btnQuerySingle,
        mBinding.btnQueryAll,
        mBinding.btnLiveDataInstall,
        mBinding.btnLiveDataDelete,
        mBinding.btnLiveDataUpdate,
        mBinding.btnLiveDataQuery,
        mBinding.btnRxjava,
        mBinding.btnDatabaseUpdate,
        mBinding.btnDatabaseUpdateInsert,
        mBinding.btnDatabaseUpdate,
        mBinding.btnDatabaseUpdateDataInsert
    )
  }

  override fun initData(savedInstanceState: Bundle?) {}

  override fun onClick(v: View?) {
    super.onClick(v)
    when (v?.id) {
      R.id.btn_add_single -> {
        val room = RoomEntity1()
        room.id = System.currentTimeMillis()
        room.name = "张三"

        mRoomUtil.insert(
            object : RoomInsertListener {
              override fun insert(): Long {
                return roomManager.dao1.roomInsert(room)
              }

              override fun onResult(success: Boolean, id: Long, errorMsg: String?) {
                ToastUtil.show("添加单个完成：$id")
              }
            }
        )
      }
      R.id.btn_add_list -> {
        val room = RoomEntityTest()
        room.uid = System.currentTimeMillis().toString()
        room.name = "王五"
        room.six = 1
        val insert = roomManager.daoTest.insert(room)
        LogUtil.e("insert:" + insert)
      }
      R.id.btn_delete_single -> {
        val room = RoomEntity1()
        room.id = 1624340340101
        mRoomUtil.delete(
            object : RoomDeleteListener {
              override fun delete(): Int {
                return roomManager.dao1.roomDelete(room)
              }

              override fun onResult(success: Boolean, deleteRow: Int, errorMsg: String?) {
                ToastUtil.show("删除单个对象成功：$deleteRow")
              }
            }
        )
      }
      R.id.btn_delete_list -> {
        val room = RoomEntityTest()
        room.uid = "1624350446562"
        val delete = roomManager.daoTest.delete(room)
        LogUtil.e("delete:" + delete)
      }
      R.id.btn_update_id -> {
        val room = RoomEntity1()
        room.id = 1624340347931
        room.name = "小飞飞"

        mRoomUtil.update(
            object : RoomUpdateListener {
              override fun update(): Int {
                return roomManager.dao1.roomUpdate(room)
              }

              override fun onResult(success: Boolean, updateRow: Int, errorMsg: String?) {
                ToastUtil.show("更新单个对象成功：$updateRow")
              }
            }
        )
      }
      R.id.btn_update_entity -> {}
      R.id.btn_query_single -> {
        mRoomUtil.query(
            object : RoomQueryListener<RoomEntity1> {
              override fun query(): RoomEntity1 {
                return roomManager.dao1.roomQuery(1624340347931)
              }

              override fun onResult(success: Boolean, t: RoomEntity1?, errorMsg: String?) {
                sequenceOf(ToastUtil.show("查询单个结果：$success querySingle:$t"))
              }
            }
        )
      }
      R.id.btn_query_all -> {
        val list = roomManager.dao1.roomQuery()
        ToastUtil.show("查询列表成功：$list")
      }
      /** **************************** LiveData */
      // 增加
      R.id.btn_live_data_install -> {
        val room = RoomEntityLiveData()
        room.id = System.currentTimeMillis()
        room.name = "王语嫣"
        val roomInsert = roomManager.liveData.roomInsert(room)
        ToastUtil.show("添加成功：$roomInsert")
      }
      // 删除
      R.id.btn_live_data_delete -> {
        val room1 = RoomEntityLiveData()
        room1.id = 1624364327773
        val room2 = RoomEntityLiveData()
        room2.id = 1624364328392
        val list = ArrayList<RoomEntityLiveData>()
        list.add(room1)
        list.add(room2)
        val roomDelete = roomManager.liveData.roomDelete(list)
        LogUtil.e("delete:$roomDelete")

        ToastUtil.show("删除成功：$$roomDelete")
      }
      // 更新
      R.id.btn_live_data_update -> {
        val room = RoomEntityLiveData()
        room.id = 1624364331827
        room.name = "李若彤"
        room.age = 18
        val roomUpdate = roomManager.liveData.roomUpdate(room)

        LogUtil.e("roomUpdate:$roomUpdate")

        ToastUtil.show("修改成功：$roomUpdate")
      }
      // 查询
      R.id.btn_live_data_query -> {
        val roomQuery = roomManager.liveData.roomQuery(1624199401956)
        ToastUtil.show("查询成功：${roomQuery}")
      }
      R.id.btn_rxjava -> {
        // rxjava 的查询
        mRoomUtil.insert(
            object : RoomInsertListener {
              override fun insert(): Long {
                val room = RoomEntityLiveData()
                room.id = System.currentTimeMillis()
                room.name = "王语嫣"

                return roomManager.liveData.roomInsert(room)
              }

              override fun onResult(success: Boolean, id: Long, errorMsg: String?) {
                ToastUtil.show("返回的结果为：$success   id：$id")
              }
            }
        )
      }
      R.id.btn_database_update -> {
        LogUtil.e("重新构建了对象!")
        //                val migration = object : Migration(3, 4) {
        //                    override fun migrate(database: SupportSQLiteDatabase) {
        //
        //                        val version = database.version
        //
        //                        LogUtil.e("version:$version")
        //
        //                        val sql = mRoomUtil.addColumn("room_test", "time",
        // RoomUtil.UNIT.TEXT)
        //
        //                        database.execSQL(sql)
        //
        //                    }
        //                }
        // 数据库更新
        //                versionRoom = Room
        //                        .databaseBuilder(mContext, RoomDataBaseHelper::class.java,
        // RoomDataBaseHelper.ROOM_DB_NAME)
        ////                        .addMigrations(migration)
        //                        .build()
      }
      R.id.btn_database_update_insert -> {
        //                val room = RoomEntityTest()
        //                room.uid = System.currentTimeMillis().toString()
        //                room.name = "哈哈"
        //
        //                mRoomUtil.insert(object : RoomInsertListener {
        //                    override fun insert(): Long {
        //                        return versionRoom?.daoTest!!.insert(room)
        //                    }
        //
        //                    override fun onResult(success: Boolean, id: Long, errorMsg: String?) {
        //
        //                        ToastUtil.show("插入成功：$success  插入的对象：$id  错误的原因：$errorMsg")
        //                    }
        //                })
      }
      // 增加数据库表格
      R.id.btn_database_update_data -> {
        LogUtil.e("开始添加数据库表格 --->")
        val map = hashMapOf<String, SQLEntity>()
        map["id"] = SQLEntity(RoomUtil.UNIT.INTEGER)
        map["name"] = SQLEntity(RoomUtil.UNIT.TEXT, "")
        map["age"] = SQLEntity(RoomUtil.UNIT.INTEGER, "0")
        val createTable = mRoomUtil.createTable("room_3", "id", RoomUtil.UNIT.INTEGER, false, map)
        val migration5 =
            object : Migration(3, 4) {
              override fun migrate(database: SupportSQLiteDatabase) {
                val version = database.version
                LogUtil.e("version ----->:$version  ")
                //                        database.execSQL(createTable)
                //  添加新的表
                //                        database.execSQL("CREATE TABLE IF NOT EXISTS Book
                // (bookName TEXT, studentId TEXT, bookId PRIMARY KEY )");
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS 'room_3'('id' INTEGER,'name' TEXT NOT NULL DEFAULT '','age'INTEGER  ) "
                )
              }
            }
        // 数据库更新
        versionRoom =
            Room.databaseBuilder(
                    mActivity,
                    RoomDataBaseHelper::class.java,
                    RoomDataBaseHelper.ROOM_DB_NAME
                )
                .addMigrations(migration5)
                .build()
      }
      // 增加数据库表格 --->  新增字段
      R.id.btn_database_update_data_insert -> {
        LogUtil.e("开始真正的添加数据 --->")
        val entity3 = RoomEntity3()
        entity3.id = System.currentTimeMillis()
        entity3.name = "小橙子"
        entity3.age = 6

        mRoomUtil.insert(
            object : RoomInsertListener {
              override fun insert(): Long {
                return versionRoom?.dao3!!.insert(entity3)
              }

              override fun onResult(success: Boolean, id: Long, errorMsg: String?) {
                val result = "插入成功：$success  插入的对象：$id  错误的原因：$errorMsg"
                ToastUtil.show(result)
              }
            }
        )
      }
    }
  }

  override fun getTitleContent(): String {
    return "Room"
  }

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): ActivityRoomBinding {
    return ActivityRoomBinding.inflate(layoutInflater, container, true)
  }
}
