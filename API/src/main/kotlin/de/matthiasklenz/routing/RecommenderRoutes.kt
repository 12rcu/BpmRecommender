package de.matthiasklenz.routing

import de.matthiasklenz.database.BpmDatabase
import de.matthiasklenz.database.dao.ItemDao
import de.matthiasklenz.database.dao.UserDao
import de.matthiasklenz.plugins.bpmnAuth
import de.matthiasklenz.recommend.Recommender
import de.matthiasklenz.recommend.RecommenderImpl
import de.matthiasklenz.recommend.comparer.Cosine
import de.matthiasklenz.recommend.comparer.Euklid
import de.matthiasklenz.recommend.comparer.Pearson
import de.matthiasklenz.recommend.comparer.SimilarityMeasure
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.ktorm.entity.map
import org.slf4j.Logger
import java.lang.NumberFormatException

class RecommenderRoutes(application: Application) : KoinComponent {
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
                }
            }
        }
    }

    private fun Route.userBasedFiltering() {
        get("/userBased/similarities/{userid}") {
            if(!bpmnAuth("/userBased/similarities", logger))
                return@get

            val userid: Int = call.parameters["userid"]?.toInt() ?: 0
            val similarityMeasure: SimilarityMeasure = when (call.request.queryParameters["SimilarityMeasure"] ?: "") {
                "euklid" -> euklid
                "cosine" -> cosine
                else -> pearson
            }

            val allRatings = database.userDao.getAllRatingsRecommender()
            val allItems = database.itemDao.getItems().map { it.name }
            val similarities = recommender.getUserSimilaritiesOf(userid, similarityMeasure, allItems, allRatings)

            call.respond(similarities)
        }
        get("/userBased/recommendations/{userid}") {
            if(!bpmnAuth("/userBased/recommendations", logger))
                return@get

            val userid: Int = call.parameters["userid"]?.toInt() ?: 0
            val similarityMeasure: SimilarityMeasure = when (call.request.queryParameters["SimilarityMeasure"] ?: "") {
                "euklid" -> euklid
                "cosine" -> cosine
                else -> pearson
            }
            val knn: Int = try {
                call.request.queryParameters["knn"]?.toInt() ?: 2
            } catch (e: NumberFormatException) {
                logger.warn(
                    "NumberFormatException while calling userBased/recommendations, " +
                            "parameters: ${call.request.queryParameters}, while knn should be a number"
                )
                2
            }
            val weightedMean = call.request.queryParameters["weightedMean"]?.toBoolean() ?: true

            val allRatings = database.userDao.getAllRatingsRecommender()
            val allItems = database.itemDao.getItems().map { it.name }
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
}