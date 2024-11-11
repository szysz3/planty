package szysz3.planty.domain.usecase.base

abstract class BaseUseCase<Input, Output> {
    abstract suspend operator fun invoke(input: Input): Output
}

class NoParams