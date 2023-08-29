package de.matthiasklenz.recommend.select

import de.matthiasklenz.recommend.Recommender

object Knn {
    /**
     * calculate the k nearest neighbours
     *
     * @param neighbors all the neighbours that the [Recommender] has calculated
     * @param k select the k nearest
     * @return the k nearest neighbours
     */
    fun <T> calculate(
        neighbors: List<T>,
        k: Int,
    ): List<T> {
        return neighbors.sortedBy {
            it as Recommender.SimilarityData
            it.similarity
        }.takeLast(k)
    }
}
