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
                .calculate(dataA.values.toList())
        val correlationCoefficientB =
            CorrelationCoefficient
                .calculate(dataB.values.toList())

        return (
            Cosine.basicCalc(
                dataA.mapValues { (_, value) ->
                    value - correlationCoefficientA
                },
                dataB.mapValues { (_, value) ->
                    value - correlationCoefficientB
                }.toMap(),
                allItems
            ).toDouble() + 1
            ) / 2
    }
}
