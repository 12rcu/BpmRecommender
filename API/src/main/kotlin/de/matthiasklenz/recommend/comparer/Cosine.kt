package de.matthiasklenz.recommend.comparer

import kotlin.math.pow
import kotlin.math.sqrt

class Cosine : SimilarityMeasure {
    companion object {
        fun basicCalc(
            dataA: Map<String, Number>,
            dataB: Map<String, Number>,
            allItems: List<String>,
        ): Number {
            var sumATimesB = 0.0
            var sumASquared = 0.0
            var sumBSquared = 0.0

            allItems.forEach {
                sumATimesB +=
                    (dataA[it] ?: 0)
                        .toDouble()
                        .times((dataB[it] ?: 0).toDouble())
                sumASquared +=
                    (dataA[it] ?: 0)
                        .toDouble()
                        .pow(2)
                sumBSquared +=
                    (dataB[it] ?: 0)
                        .toDouble()
                        .pow(2)
            }

            return (sumATimesB / (sqrt(sumASquared * sumBSquared)))
        }
    }

    override fun compare(
        dataA: Map<String, Int>,
        dataB: Map<String, Int>,
        allItems: List<String>,
    ): Number {
        return basicCalc(dataA, dataB, allItems)
    }
}
