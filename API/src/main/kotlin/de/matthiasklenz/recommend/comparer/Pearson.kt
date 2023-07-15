package de.matthiasklenz.recommend.comparer

import de.matthiasklenz.recommend.comparer.shared.CorrelationCoefficient

class Pearson : SimilarityMeasure {
    override fun compare(
        dataA: Map<String, Int>,
        dataB: Map<String, Int>,
        allItems: List<String>,
    ): Number {
        val correlationCoefficientA =
            CorrelationCoefficient
                .calculate(dataA.map { it.value })
        val correlationCoefficientB =
            CorrelationCoefficient
                .calculate(dataB.map { it.value })

        return (
            Cosine.basicCalc(
                dataA.map { (key, value) -> key to value - correlationCoefficientA }
                    .toMap(),
                dataB.map { (key, value) -> key to value - correlationCoefficientB }
                    .toMap(),
                allItems
            ).toDouble() + 1
            ) / 2
    }
}
