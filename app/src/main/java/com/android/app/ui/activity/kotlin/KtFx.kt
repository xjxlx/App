package com.android.app.ui.activity.kotlin

/**
 * @author : 流星
 * @CreateDate: 2022/1/20-3:13 下午
 * @Description:
 */
class KtFx {
//    val list: ArrayList<out Animal> = ArrayList<DogAnimal>()
//    val list2: ArrayList<in DogAnimal> = ArrayList<Animal>()

    var an: ArrayList2<Animal<String>> = ArrayList2<DogAnimal<String>>()
}

open class Animal<T> {

}

class DogAnimal<T> : Animal<T>() {

}

class ArrayList2<out T> {

}
