package com.cluelin.app.db

data class Quest(val id: Int, val title: String, val term: Int, var lastCompletedTime: String?) {
    constructor(title: String, term: Int) : this(-1, title, term, null)
    constructor(title: String, term: Int, lastCompletedTime: String?) : this(-1, title, term, lastCompletedTime)

}