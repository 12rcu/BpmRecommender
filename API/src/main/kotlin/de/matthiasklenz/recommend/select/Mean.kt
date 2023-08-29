package de.matthiasklenz.recommend.select

import de.matthiasklenz.recommend.Recommender

object Mean {
    fun itemBasedWeightedMean(
        itemRatingsWithSim: List<Pair<Int, Double>>,
    ) = itemRatingsWithSim.sumOf {
        it.first * it.second
    } / itemRatingsWithSim.sumOf { it.second }

    fun itemMean(
        itemRatings: List<Int>,
    ) = itemRatings.sumOf { it }.toDouble() / itemRatings.size


    fun weightedMean(
        userSimilarity: List<Recommender.UserSimilarity>,
        item: String,
    ): Double {
        return userSimilarity.sumOf {
            it.ratings[item]!! * it.similarity
        } / userSimilarity.sumOf { it.similarity }
    }

    fun mean(
        userSimilarity: List<Recommender.UserSimilarity>,
        item: String,
    ): Double {
        return userSimilarity.sumOf {
            it.ratings[item]!!
        }.toDouble() / userSimilarity.size
    }
}
