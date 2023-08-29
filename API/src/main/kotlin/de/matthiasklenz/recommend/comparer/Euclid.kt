package de.matthiasklenz.recommend.comparer

import kotlin.math.pow
import kotlin.math.sqrt

class Euclid : SimilarityMeasure {
    override fun compare(
        dataA: Map<String, Int>,
        dataB: Map<String, Int>,
        allItems: List<String>,
    ): Number {
        val sum = allItems.sumOf {
            ((dataA[it] ?: 0) - (dataB[it] ?: 0))
                .toDouble().pow(2)
        }

        return 1 / (1 + sqrt(sum))
    }
}
