package de.matthiasklenz.recommend

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
        similarityMeasure: SimilarityMeasure,
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
        val similarities = getUserSimilaritiesOf(userid, similarityMeasure, allItems, ratings)
        //select items that are not rated
        val ratedItems = ratings.find { it.userid == userid }!!.ratings
        val unratedItems = allItems.filterNot { ratedItems.containsKey(it) }
        //select user that have rated an item and make a prediction what rating the user would give
        unratedItems.forEach { item ->
            //select only the neighbors that have rated the item
            val neighbors = similarities.filter { it.userid != userid && it.ratings.containsKey(item) }
            val nearestNeighbors = Knn.calculate(neighbors, knn)
            if (weightedMean)
                returnRecommendations[item] = Mean.weightedMean(nearestNeighbors, item)
            else
                returnRecommendations[item] = Mean.mean(nearestNeighbors, item)
        }
        return returnRecommendations
    }

    override fun getUserSimilaritiesOf(
        userid: Int,
        similarityMeasure: SimilarityMeasure,
        allItems: List<String>,
        ratings: List<Recommender.UserRating>
    ): List<Recommender.UserSimilarity> {
        if (ratings.find { it.userid == userid } == null) {
            logger.error("User $userid is not within the list provided! Returning empty list!")
            return listOf()
        }
        val targetUserRatings = ratings.find { it.userid == userid }!!

        return ratings.filter { it.userid != userid }.map {
            Recommender.UserSimilarity(
                it.userid,  //similarity to that user
                it.ratings,
                similarityMeasure.compare(targetUserRatings.ratings, it.ratings, allItems)
                    .toDouble() //how similar as a double
            )
        }
    }

    override fun getItemSimilaritiesOf(
        item: String,
        similarityMeasure: SimilarityMeasure,
        allItems: List<String>,
        ratings: List<Recommender.UserRating>
    ): List<Recommender.ItemSimilarity> {
        if (!allItems.contains(item)) {
            logger.error("Item $item is not within the list provided! Returning empty list!")
            return listOf()
        }

        val dataA = ratings
            .filter { it.ratings.containsKey(item) }
            .associate { it.userid.toString() to it.ratings[item]!! }

        return allItems
            .filter { it != item }
            .map {
                val dataB = ratings
                    .filter { rt -> rt.ratings.containsKey(it) }
                    .associate { rt -> rt.userid.toString() to rt.ratings[it]!! }
                Recommender.ItemSimilarity(
                    it,  //similarity to that user
                    similarityMeasure.compare(dataA, dataB, ratings.map { rating -> rating.userid.toString() })
                        .toDouble() //how similar as a double
                )
            }
    }
}