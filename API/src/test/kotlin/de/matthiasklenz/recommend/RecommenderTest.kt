package de.matthiasklenz.recommend

import de.matthiasklenz.recommend.comparer.Euklid
import org.koin.test.KoinTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RecommenderTest : KoinTest {
    val recommender: Recommender = RecommenderImpl()

    companion object {
        val items = listOf(
            "Cafe Rot",
            "Pizza Pronto",
            "Pasta und Pizza",
            "Earl of Sandwiches",
            "Grecos"
        )

        val userRatings: List<Recommender.UserRating> = listOf(
            Recommender.UserRating(
                userid = 1,
                mapOf(
                    "Cafe Rot" to 1,
                    "Pizza Pronto" to 1,
                    "Pasta und Pizza" to 4,
                    "Earl of Sandwiches" to 2,
                    "Grecos" to 5
                )
            ),
            Recommender.UserRating(
                userid = 2,
                mapOf(
                    "Cafe Rot" to 1,
                    "Pizza Pronto" to 5,
                    "Earl of Sandwiches" to 3
                )
            ),
            Recommender.UserRating(
                userid = 3,
                mapOf(
                    "Cafe Rot" to 3,
                    "Pizza Pronto" to 1,
                    "Pasta und Pizza" to 1,
                    "Earl of Sandwiches" to 4,
                    "Grecos" to 1
                )
            ),
            Recommender.UserRating(
                userid = 4,
                mapOf(
                    "Cafe Rot" to 1,
                    "Pizza Pronto" to 5,
                    "Pasta und Pizza" to 3,
                    "Earl of Sandwiches" to 4,
                    "Grecos" to 2
                )
            ),
            Recommender.UserRating(
                userid = 5,
                mapOf(
                    "Cafe Rot" to 2,
                    "Pizza Pronto" to 5,
                    "Pasta und Pizza" to 5,
                    "Earl of Sandwiches" to 3,
                    "Grecos" to 2
                )
            )
        )
    }

    @Test
    fun basicSimilaritiesEuklid() {
        val sim = recommender.getUserSimilaritiesOf(
            2,
            Euklid(),
            items,
            userRatings
        )
        assertEquals(4, sim.size)

        assertEquals(0.12, sim[0].similarity, 0.05)
        assertEquals(0.16, sim[1].similarity, 0.05)
        assertEquals(0.21, sim[2].similarity, 0.05)
        assertEquals(0.15, sim[3].similarity, 0.05)
    }

    @Test
    fun recommendedItemsTest() {
        val predictedRatings = recommender.recommendUserBasedItemFor(
            2,
            Euklid(),
            items,
            userRatings,
            weightedMean = false
        )
        assertEquals(predictedRatings.size, 2)
        assertEquals(2.0, predictedRatings["Pasta und Pizza"])
        assertEquals(1.5, predictedRatings["Grecos"])
    }

    @Test
    fun recommendedItemsWeightedTest() {
        val predictedRatings =
            recommender.recommendUserBasedItemFor(
                2,
                Euklid(),
                items,
                userRatings,
                weightedMean = true
            )
        assertEquals(2, predictedRatings.size)
        assertEquals(
            2.10,
            predictedRatings["Pasta und Pizza"]!!,
            0.05
        )
        assertEquals(1.55, predictedRatings["Grecos"]!!, 0.05)
    }

    @Test
    fun assertFailOnMissingUserid() {
        val predictedRatings = recommender.recommendUserBasedItemFor(
            6,
            Euklid(),
            items,
            userRatings,
            weightedMean = true
        )
        assertEquals(0, predictedRatings.size)
    }

    @Test
    fun assertFailOnMissingUserid2() {
        val sim = recommender.getUserSimilaritiesOf(
            6,
            Euklid(),
            items,
            userRatings
        )
        assertEquals(0, sim.size)
    }

    @Test
    fun testItemSimilarities() {
        val sim =
            recommender.getItemSimilaritiesOf(
                "Earl of Sandwiches",
                Euklid(),
                items,
                userRatings
            )
        assertEquals(0.2, sim[0].similarity, 0.05)
        assertEquals(0.18, sim[1].similarity, 0.05)
        assertEquals(0.16, sim[2].similarity, 0.05)
        assertEquals(0.15, sim[3].similarity, 0.05)
    }
}
