package szysz3.planty.domain.usecase.base

abstract class BaseUseCase<Input, Output> {
    abstract fun execute(input: Input): Output
}

class NoParams