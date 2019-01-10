package com.teda.chesstactics.data

import com.teda.chesstactics.data.entity.Position

object DataGenerator {

    fun generatePositions(): ArrayList<Position> {
        var positions = arrayListOf<Position>()
        var problem = Position()
        problem.whiteToPlay = true
        problem.initialPosition = "2k5/ppp4p/2n5/3N2B1/3P4/2n2PPK/P1r5/4R3"
        problem.pgn = "30. Re8+ Kd7 31. Nf6+ Kd6 32. Bf4+"
        problem.setMovements()
        positions.add(problem)
       /* var position = Position("1", "1", 1600, true)
        var position2 = Position("2", "2", 1700, true)
        var position3 = Position("3", "3", 1800, true)
        var position4 = Position("4", "4", 1900, false)
        var position5 = Position("5", "5", 2000, false)
        positions.add(position)
        positions.add(position2)
        positions.add(position3)
        positions.add(position4)
        positions.add(position5)*/
        return positions
    }

}