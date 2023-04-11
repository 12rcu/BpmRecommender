package de.matthiasklenz.recommend.comparer

interface Comparer {
    /**
     * compares 2 data maps and gives back a similarity measure, note: similarity measure can only be used with another
     * the same similarity measure.
     *
     * Used for calculation the similarity between 2 users
     *
     * @param dataA the first data set
     * @param dataB the second data set
     * @param allItems all the items referenced in data1 & 2
     */
    fun compare(dataA: Map<String, Int>, dataB: Map<String, Int>, allItems: List<String>): Number
}