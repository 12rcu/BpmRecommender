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
    fun calculate(neighbors: List<Recommender.UserSimilarity>, k: Int): List<Recommender.UserSimilarity> {
        return neighbors.sortedBy { it.similarity }.takeLast(k)
    }
}