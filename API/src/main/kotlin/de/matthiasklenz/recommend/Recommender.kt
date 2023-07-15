package de.matthiasklenz.recommend

import de.matthiasklenz.recommend.comparer.SimilarityMeasure
import kotlinx.serialization.Serializable

interface Recommender {
    @Serializable
    data class UserSimilarity(
        val userid: Int,
        val ratings: Map<String, Int>,
        val similarity: Double,
    )

    @Serializable
    data class ItemSimilarity(
        val item: String,
        val similarity: Double,
    )

    /**
     * @param userid the userid that has rated items
     * @param ratings the ratings (item to rating), if a user hasn't rated an item it shouldn't be in the map
     */
    @Serializable
    data class UserRating(
        val userid: Int,
        val ratings: Map<String, Int>,
    )

    /**
     * @param userid the user for whom an item should be recommended
     * @param similarityMeasure the algorithm which the recommendation should use
     * @param allItems all possible items, user don't need to rate all items
     * @param ratings a list of all ratings from all users, if users haven't rated an item it shouldn't be in the map
     * @param knn indicates how many users should be compared
     * @param weightedMean if true, the ratings form the knn will be multiplied by the similarity
     * @return all items with the estimated rating the user would give
     */
    fun recommendUserBasedItemFor(
        userid: Int,
        similarityMeasure: SimilarityMeasure,
        allItems: List<String>,
        ratings: List<UserRating>,
        knn: Int = 2,
        weightedMean: Boolean = true,
    ): Map<String, Double>

    /**
     * @param userid the user for whom an item should be recommended
     * @param similarityMeasure the algorithm which the recommendation should use
     * @param allItems all possible items, user don't need to rate all items
     * @param ratings a list of all ratings from all users, if users haven't rated an item it shouldn't be in the map
     */
    fun getUserSimilaritiesOf(
        userid: Int,
        similarityMeasure: SimilarityMeasure,
        allItems: List<String>,
        ratings: List<UserRating>,
    ): List<UserSimilarity>

    /**
     * @param item the item for which a similar item is searched
     * @param similarityMeasure the algorithm which the recommendation should use
     * @param allItems all possible items, users don't need to rate all items
     * @param ratings a list of all ratings from all users, if users haven't rated an item it shouldn't be in the map
     */
    fun getItemSimilaritiesOf(
        item: String,
        similarityMeasure: SimilarityMeasure,
        allItems: List<String>,
        ratings: List<UserRating>,
    ): List<ItemSimilarity>
}
