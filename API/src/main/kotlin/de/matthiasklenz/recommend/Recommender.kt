package de.matthiasklenz.recommend

import de.matthiasklenz.recommend.comparer.SimilarityMeasure

interface Recommender {
    data class UserSimilarity(
        val userid: Int,
        val ratings: Map<String, Int>,
        val similarity: Double
    )

    /**
     * @param userid the userid that has rated items
     * @param ratings the ratings (item to rating), if a user hasn't rated an item it shouldn't be in the map
     */
    data class UserRating(
        val userid: Int,
        val ratings: Map<String, Int>
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
        similarityMeasure: SimilarityMeasure.Type,
        allItems: List<String>,
        ratings: List<UserRating>,
        knn: Int = 2,
        weightedMean: Boolean = true
    ): Map<String, Double>

    /**
     * @param userid the user for whom an item should be recommended
     * @param similarityMeasure the algorithm which the recommendation should use
     * @param allItems all possible items, user don't need to rate all items
     * @param ratings a list of all ratings from all users, if users haven't rated an item it shouldn't be in the map
     */
    fun getSimilaritiesOf(
        userid: Int,
        similarityMeasure: SimilarityMeasure.Type,
        allItems: List<String>,
        ratings: List<UserRating>
    ): List<UserSimilarity>
}