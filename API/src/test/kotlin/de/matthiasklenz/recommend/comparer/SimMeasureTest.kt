package de.matthiasklenz.recommend.comparer

import de.matthiasklenz.recommend.RecommenderTest
import kotlin.test.*

class SimMeasureTest {
    companion object Data {
        val items = RecommenderTest.items
        val userA = RecommenderTest.userRatings[1].ratings
        val userB = RecommenderTest.userRatings[3].ratings
    }

    @Test
    fun testEuklid() {
        val sim = Euklid().compare(userA, userB, items)
        assertEquals(sim.toDouble(), 0.21, 0.1)
    }

    @Test
    fun testCousin() {
        val sim = Cosine().compare(userA, userB, items)
        assertEquals(sim.toDouble(), 0.87, 0.1)
    }

    @Test
    fun testPearson() {
        val sim = Pearson().compare(userA, userB, items)
        assertEquals(sim.toDouble(), 0.89, 0.1)
    }
}