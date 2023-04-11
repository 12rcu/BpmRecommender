package de.matthiasklenz.recommend

import de.matthiasklenz.recommend.comparer.Cosine
import de.matthiasklenz.recommend.comparer.Euklid
import de.matthiasklenz.recommend.comparer.Pearson
import org.slf4j.LoggerFactory


class Recommender {
    private val logger = LoggerFactory.getLogger("api")

    enum class ComparingType {
        PEARSON,
        EUKLID,
        COSINE,
        ADJUSTED_COSINE
    }

    data class UserSimilarity(
        val userid: Int,
        val similarity: Double
    )

    fun calculateAllSimilaritiesUser(
        userid: Int,
        data: Map<Int, Map<String, Int>>,
        allItems: List<String>,
        comparison: ComparingType = ComparingType.PEARSON
    ): MutableList<UserSimilarity> {
        val userSimData = mutableListOf<UserSimilarity>()
        if (!data.containsKey(userid)) {
            logger.error("User $userid is not within the list provided!")
            return userSimData
        }
        data.forEach { (compareUserid, compareData) ->
            if (userid == compareUserid)
                return@forEach
            when (comparison) {
                ComparingType.COSINE -> userSimData.add(
                    UserSimilarity(
                        compareUserid,
                        Cosine().compare(data[userid]!!, compareData, allItems).toDouble()
                    )
                )

                ComparingType.EUKLID -> userSimData.add(
                    UserSimilarity(
                        compareUserid,
                        Euklid().compare(data[userid]!!, compareData, allItems).toDouble()
                    )
                )

                ComparingType.PEARSON -> userSimData.add(
                    UserSimilarity(
                        compareUserid,
                        Pearson().compare(data[userid]!!, compareData, allItems).toDouble()
                    )
                )

                ComparingType.ADJUSTED_COSINE -> TODO()
            }
        }
        return userSimData
    }
}