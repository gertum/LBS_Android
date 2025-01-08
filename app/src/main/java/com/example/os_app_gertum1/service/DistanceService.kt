package com.example.os_app_gertum1.service

import com.example.os_app_gertum1.data.database.SignalStrength
import kotlin.math.pow
import kotlin.math.sqrt

class DistanceService {

    fun findClosestMeasurement(
        userStrengths: List<Pair<String, Int>>,
        signalStrengths: List<SignalStrength>
    ): Pair<Int, Double> {
        var closestGridPointId = -1
        var minDistance = Double.MAX_VALUE

        val groupedSignalStrengths = signalStrengths.groupBy { it.measurement }

        for ((measurementId, strengths) in groupedSignalStrengths) {
            val strengthsMap = strengths.associateBy({ it.sensor }, { it.strength })

            val distance = computeDistance(userStrengths, strengthsMap)

            if (distance < minDistance) {
                minDistance = distance
                closestGridPointId = measurementId
            }
        }

        return Pair(closestGridPointId, minDistance)
    }

    private fun computeDistance(
        userStrengths: List<Pair<String, Int>>,
        strengthsMap: Map<String, Int?>
    ): Double {
        return sqrt(
            userStrengths.sumOf { (sensor, strength) ->
                val difference = strength - (strengthsMap[sensor] ?: 0)
                difference * difference.toDouble()
            }
        )
    }
}
