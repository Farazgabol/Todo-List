package com.example.todo_list1

data class Item(
    var task: String? = null,
    var description: String? = null
) {
    constructor() : this("", "")
}
