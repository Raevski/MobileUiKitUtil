package domain_layer.usecases
interface MobileUtilUseCase<Params, Result> {
    fun execute(params: Params): Result
}