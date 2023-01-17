package domain_layer.usecases

import kotlinx.coroutines.runBlocking

interface MobileUtilUseCase<Params, Result> {
    suspend fun execute(params: Params): Result

    fun executeBlocking(params: Params): Result {
        return runBlocking { execute(params) }
    }
}