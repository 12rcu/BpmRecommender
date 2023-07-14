package de.matthiasklenz.recommend.comparer

interface SimilarityMeasure {
    /**
     * compares 2 data maps and returns a similarity measure,
     * note: comparing the result of this function only works within
     * the same Similarity Measure!
     *
     * @param dataA the first data set
     * @param dataB the second data set
     * @param allItems all the items referenced in data1 & 2
     */
    fun compare(
        dataA: Map<String, Int>,
        dataB: Map<String, Int>,
        allItems: List<String>
    ): Number
}