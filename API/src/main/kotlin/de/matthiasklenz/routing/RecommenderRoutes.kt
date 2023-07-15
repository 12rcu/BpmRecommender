package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import de.matthiasklenz.plugins.bpmnAuth
import de.matthiasklenz.recommend.RecommenderImpl
import de.matthiasklenz.recommend.comparer.Cosine
import de.matthiasklenz.recommend.comparer.Euklid
import de.matthiasklenz.recommend.comparer.Pearson
import de.matthiasklenz.recommend.comparer.SimilarityMeasure
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.ktorm.entity.map
import org.slf4j.Logger

class RecommenderRoutes(application: Application) :
    KoinComponent {
    companion object {
        fun koinModule(): Module = org.koin.dsl.module {
            single { Euklid() }
            single { Cosine() }
            single { Pearson() }

            single { RecommenderImpl() }
        }
    }

    private val euklid: Euklid by inject()
    private val cosine: Cosine by inject()
    private val pearson: Pearson by inject()

    private val database: BpmDatabase by inject()
    private val recommender: RecommenderImpl by inject()

    private val logger: Logger by inject()

    init {
        with(application) {
            routing {
                authenticate {
                    userBasedFiltering()
                    itemBasedFiltering()
                }
            }
        }
    }

    private fun Route.userBasedFiltering() {
        get("/userBased/similarities/{userid}") {
            val authenticated = bpmnAuth(
                routeInfo = "/userBased/similarities",
                logger
            )
            if (!authenticated) {
                return@get
            }

            val userid: Int = call
                .parameters["userid"]
                ?.toIntOrNull()
                ?: 0

            val similarityMeasure: SimilarityMeasure =
                simStringToObj(
                    str = call
                        .request
                        .queryParameters["SimilarityMeasure"]
                        ?: ""
                )

            val allRatings = database
                .userDao
                .getAllRatingsRecommender()

            val allItems = database
                .itemDao
                .getItems()
                .map { it.id.toString() }

            val similarities = recommender.getUserSimilaritiesOf(
                userid,
                similarityMeasure,
                allItems,
                allRatings
            )

            call.respond(similarities)
        }
        get("/userBased/recommendations/{userid}") {
            val authenticated = bpmnAuth(
                routeInfo = "/userBased/recommendations",
                logger
            )
            if (!authenticated) {
                return@get
            }

            val userid: Int = call
                .parameters["userid"]
                ?.toInt()
                ?: 0

            val similarityMeasure: SimilarityMeasure =
                simStringToObj(
                    call
                        .request
                        .queryParameters["SimilarityMeasure"]
                        ?: ""
                )

            val knn: Int = call
                .request
                .queryParameters["knn"]
                ?.toIntOrNull()
                ?: 2

            val weightedMean = call
                .request
                .queryParameters["weightedMean"]
                ?.toBooleanStrictOrNull()
                ?: true

            val allRatings = database
                .userDao
                .getAllRatingsRecommender()

            val allItems = database
                .itemDao
                .getItems()
                .map { it.id.toString() }

            val recommendations =
                recommender.recommendUserBasedItemFor(
                    userid,
                    similarityMeasure,
                    allItems,
                    allRatings,
                    knn,
                    weightedMean
                )

            call.respond(recommendations)
        }
    }

    private fun Route.itemBasedFiltering() {
        get("/itemBased/similarities/{itemId}") {
            val authenticated = bpmnAuth(
                routeInfo = "/itemBased/similarities",
                logger
            )
            if (!authenticated) {
                return@get
            }
            val itemId: Int =
                call.parameters["itemId"]?.toIntOrNull() ?: 0
            val similarityMeasure: SimilarityMeasure =
                simStringToObj(
                    str = call
                        .request
                        .queryParameters["SimilarityMeasure"]
                        ?: ""
                )

            val allItems = database.itemDao.getItems().map {
                it.id.toString()
            }
            val allRatings =
                database.userDao.getAllRatingsRecommender()

            val similarities =
                recommender.getItemSimilaritiesOf(
                    itemId.toString(),
                    similarityMeasure,
                    allItems,
                    allRatings
                )
            call.respond(similarities)
        }
        get("/itemBased/recommendations/{itemId}") {
            val authenticated = bpmnAuth(
                routeInfo = "/itemBased/recommendations",
                logger
            )
            if (!authenticated) {
                return@get
            }

            val itemId: Int = call
                .parameters["itemId"]
                ?.toIntOrNull()
                ?: 0

            val similarityMeasure: SimilarityMeasure =
                simStringToObj(
                    call.request.queryParameters["SimilarityMeasure"]
                        ?: ""
                )
        }
    }

    private fun simStringToObj(str: String) = when (str) {
        "euklid" -> euklid
        "cosine" -> cosine
        else -> pearson // also functions as adjusted cosine
    }
}
