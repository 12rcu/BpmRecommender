package de.matthiasklenz.recommend.comparer

import de.matthiasklenz.recommend.RecommenderTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SimMeasureTest {
    companion object Data {
        val items = RecommenderTest.items
        val userA = RecommenderTest.userRatings[1].ratings
        val userB = RecommenderTest.userRatings[3].ratings
    }

    @Test
    fun testEuklid() {
        val sim = Euklid().compare(userA, userB, items)
        assertEquals(0.21, sim.toDouble(), 0.1)
    }

    @Test
    fun testCousin() {
        val sim = Cosine().compare(userA, userB, items)
        assertEquals(0.87, sim.toDouble(), 0.1)
    }

    @Test
    fun testPearson() {
        val sim = Pearson().compare(userA, userB, items)
        assertEquals(0.89, sim.toDouble(), 0.1)
    }
}
