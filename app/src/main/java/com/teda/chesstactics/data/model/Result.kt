package com.teda.chesstactics.data.model

class Result {

    var averageElo: Double = 0.0
    var positions = 0
    var success= 0
    var error = 0

    constructor(averageElo: Double, positions: Int, success: Int, error: Int) {
        this.averageElo = averageElo
        this.positions = positions
        this.success = success
        this.error = error
    }
}