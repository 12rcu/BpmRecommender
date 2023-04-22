package de.matthiasklenz.routing

import de.matthiasklenz.database.dao.ItemDao
import de.matthiasklenz.database.dao.UserDao
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

    private val userDao: UserDao by inject()
    private val itemDao: ItemDao by inject()
    private val recommender: Recommender by inject()

    init {
        with(application) {
            routing {
                authenticate {
                    userBasedFiltering()
                }
            }
        }
    }

    fun Route.userBasedFiltering() {
        get("/userBased/similarities/{userid}") {
            val userid: Int = call.parameters["userid"]?.toInt() ?: 0
            val similarityMeasure: SimilarityMeasure = when (call.request.queryParameters["SimilarityMeasure"] ?: "") {
                "euklid" -> euklid
                "cosine" -> cosine
                else -> pearson
            }

            val allRatings = userDao.getAllRatingsRecommender()
            val allItems = itemDao.getItems().map { it.name }
            val similarities = recommender.getUserSimilaritiesOf(userid, similarityMeasure, allItems, allRatings)

            call.respond(similarities)
        }
        get("/userBased/recommendations/{userid}") {
            val userid: Int = call.parameters["userid"]?.toInt() ?: 0
        }
    }
}