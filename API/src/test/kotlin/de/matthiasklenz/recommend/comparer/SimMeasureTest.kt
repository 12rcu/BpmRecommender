package de.matthiasklenz.recommend.comparer

import kotlin.test.*

class SimMeasureTest {
    companion object Data {
        val items = listOf(
            "Cafe Rot",
            "Pizza Pronto",
            "Pasta und Pizza",
            "Earl of Sandwiches",
            "Grecos"
        )
        val userA = mapOf(
            "Cafe Rot" to 1,
            "Pizza Pronto" to 5,
            "Earl of Sandwiches" to 3,
        )

        val userB = mapOf(
            "Cafe Rot" to 1,
            "Pizza Pronto" to 5,
            "Pasta und Pizza" to 3,
            "Earl of Sandwiches" to 4,
            "Grecos" to 2
        )
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