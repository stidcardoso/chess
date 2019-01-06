package com.teda.chesstactics.data

class Solution() {

    var movements = ArrayList<Movement>()
    var answers = ArrayList<Movement>()

    constructor(movements: ArrayList<Movement>, answers: ArrayList<Movement>) : this() {
        this.movements = movements
        this.answers = answers
    }

}