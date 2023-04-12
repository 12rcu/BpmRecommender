package de.matthiasklenz.recommend

import de.matthiasklenz.recommend.comparer.Cosine
import de.matthiasklenz.recommend.comparer.Euklid
import de.matthiasklenz.recommend.comparer.Pearson
import de.matthiasklenz.recommend.comparer.SimilarityMeasure
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.Logger


class RecommenderImpl : KoinComponent, Recommender {
    private val logger: Logger by inject()

    override fun recommendUserBasedItemFor(
        userid: Int,
        similarityMeasure: SimilarityMeasure.Type,
        allItems: List<String>,
        ratings: List<Recommender.UserRatings>
    ): Map<String, Double> {
        TODO("Not yet implemented")
    }

    override fun getSimilaritiesOf(
        userid: Int,
        similarityMeasure: SimilarityMeasure.Type,
        allItems: List<String>,
        ratings: List<Recommender.UserRatings>
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
                    Cosine().compare(targetUserRatings.ratings, it.ratings, allItems)
                        .toDouble() //how similar as a double
                )

                SimilarityMeasure.Type.EUKLID -> Recommender.UserSimilarity(
                    it.userid,
                    Euklid().compare(targetUserRatings.ratings, it.ratings, allItems).toDouble()
                )

                SimilarityMeasure.Type.PEARSON -> Recommender.UserSimilarity(
                    it.userid,
                    Pearson().compare(targetUserRatings.ratings, it.ratings, allItems).toDouble()
                )

                SimilarityMeasure.Type.ADJUSTED_COSINE -> TODO()
            }
        }
    }
}