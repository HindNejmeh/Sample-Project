package com.myapplication.domain.base

abstract class Consumable {

    private var isConsumed = false

    fun consume(): Boolean {
        if (!isConsumed) {
            isConsumed = true

            return true
        }

        return false
    }
}