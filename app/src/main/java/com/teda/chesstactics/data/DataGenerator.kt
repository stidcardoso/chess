package com.teda.chesstactics.data

import com.teda.chesstactics.data.entity.Position

object DataGenerator {

    fun generatePositions(): ArrayList<Position> {
        val positions = arrayListOf<Position>()
        val problem = Position()
        problem.elo = 1600
        problem.whiteToPlay = true
        problem.initialPosition = "2k5/ppp4p/2n5/3N2B1/3P4/2n2PPK/P1r5/4R3"
        problem.pgn = "30. Re8+ Kd7 31. Nf6+ Kd6 32. Bf4+"
        problem.setMovements()
        positions.add(problem)
        val problem2 = Position()
        problem2.elo = 1600
        problem2.whiteToPlay = false
        problem2.initialPosition = "2kr3r/1pp4p/p7/2P2p2/N2qp1n1/3P4/PP2BPP1/R2QR1K1"
        problem2.pgn = "22. Qxf2+ 23. Kh1 Qh4+ 24. Kg1 Qh2+ 25. Kf1 Qh1#"
        problem2.setMovements()
        positions.add(problem)
        positions.add(problem2)
        val problem3 = Position()
        problem3.elo = 1600
        problem3.whiteToPlay = true
        problem3.initialPosition = "4kr2/q4N1N/2p1P1Q1/1P2R3/p7/8/Kn6/8"
        problem3.pgn = "1. e7 Qf2 2. exf8=N"
        problem3.setMovements()
        positions.add(problem3)
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