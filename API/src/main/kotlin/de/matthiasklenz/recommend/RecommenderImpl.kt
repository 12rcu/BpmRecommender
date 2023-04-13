package de.matthiasklenz.recommend

import de.matthiasklenz.recommend.comparer.Cosine
import de.matthiasklenz.recommend.comparer.Euklid
import de.matthiasklenz.recommend.comparer.Pearson
import de.matthiasklenz.recommend.comparer.SimilarityMeasure
import de.matthiasklenz.recommend.select.Knn
import de.matthiasklenz.recommend.select.Mean
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger


class RecommenderImpl : KoinComponent, Recommender {
    private val logger: Logger by inject()

    override fun recommendUserBasedItemFor(
        userid: Int,
        similarityMeasure: SimilarityMeasure.Type,
        allItems: List<String>,
        ratings: List<Recommender.UserRating>,
        knn: Int,
        weightedMean: Boolean
    ): Map<String, Double> {
        if (ratings.find { it.userid == userid } == null) {
            logger.error("User $userid is not within the list provided! Returning empty map!")
            return mapOf()
        }
        val returnRecommendations = mutableMapOf<String, Double>()
        val similarities = getSimilaritiesOf(userid, similarityMeasure, allItems, ratings)
        //select items that are not rated
        val ratedItems = ratings.find { it.userid == userid }!!.ratings
        val unratedItems = allItems.filterNot { ratedItems.containsKey(it) }
        //select user that have rated an item and make a prediction what rating the user would give
        unratedItems.forEach { item ->
            //select only the neighbors that have rated the item
            val neighbors = similarities.filter { it.userid != userid && it.ratings.containsKey(item) }
            val nearestNeighbors = Knn.calculate(neighbors, knn)
            if(weightedMean)
                returnRecommendations[item] = Mean.weightedMean(nearestNeighbors, item)
            else
                returnRecommendations[item] = Mean.mean(nearestNeighbors, item)
        }
        return returnRecommendations
    }

    override fun getSimilaritiesOf(
        userid: Int,
        similarityMeasure: SimilarityMeasure.Type,
        allItems: List<String>,
        ratings: List<Recommender.UserRating>
    ): List<Recommender.UserSimilarity> {
        if (ratings.find { it.userid == userid } == null) {
            logger.error("User $userid is not within the list provided! Returning empty list!")
            return listOf()
        }
        val targetUserRatings = ratings.find { it.userid == userid }!!

        return ratings.filter { it.userid != userid }.map {
            when (similarityMeasure) {
                SimilarityMeasure.Type.COSINE -> Recommender.UserSimilarity(
                    it.userid,  //similarity to that user
                    it.ratings,
                    Cosine().compare(targetUserRatings.ratings, it.ratings, allItems)
                        .toDouble() //how similar as a double
                )

                SimilarityMeasure.Type.EUKLID -> Recommender.UserSimilarity(
                    it.userid,
                    it.ratings,
                    Euklid().compare(targetUserRatings.ratings, it.ratings, allItems).toDouble()
                )

                SimilarityMeasure.Type.PEARSON -> Recommender.UserSimilarity(
                    it.userid,
                    it.ratings,
                    Pearson().compare(targetUserRatings.ratings, it.ratings, allItems).toDouble()
                )

                SimilarityMeasure.Type.ADJUSTED_COSINE -> TODO()
            }
        }
    }
}