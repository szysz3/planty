package szysz3.planty.domain.usecase.base

/**
 * Base abstract class for use cases following clean architecture principles.
 *
 * @param Input The type of input parameters the use case expects
 * @param Output The type of result the use case produces
 */
abstract class BaseUseCase<Input, Output> {
    /**
     * Executes the use case with the given input parameters.
     *
     * @param input The input parameters for the use case
     * @return The result of the use case execution
     */
    abstract suspend operator fun invoke(input: Input): Output
}

/**
 * Utility class representing no parameters for use cases that don't require input.
 * Used as a replacement for Unit to maintain consistency in use case signatures.
 */
class NoParams