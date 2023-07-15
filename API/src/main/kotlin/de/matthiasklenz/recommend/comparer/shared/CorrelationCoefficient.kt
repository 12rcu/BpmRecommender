package de.matthiasklenz.recommend.comparer.shared

object CorrelationCoefficient {
    /**
     * calculates the Correlation Coefficient for one person
     * and their recommendations
     *
     * @param list the items a user has rated
     */
    fun calculate(list: List<Int>): Float {
        return list.sum() / list.size.toFloat()
    }
}
