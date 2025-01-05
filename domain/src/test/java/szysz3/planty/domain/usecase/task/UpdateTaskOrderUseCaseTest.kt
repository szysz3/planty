package szysz3.planty.domain.usecase.task

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task
import szysz3.planty.domain.util.TestSubTaskFactory
import szysz3.planty.domain.util.TestTaskFactory

class UpdateTaskOrderUseCaseTest {

    private lateinit var useCase: UpdateTaskOrderUseCase

    @Before
    fun setUp() {
        useCase = UpdateTaskOrderUseCase()
    }

    private fun createTask(
        id: Long = 1,
        title: String = "Task $id",
        subTasks: List<SubTask> = TestSubTaskFactory.createSubTasks(startId = id, count = 2),
        isCompleted: Boolean = false,
        color: Int = 0xFFFFFF,
        index: Int = (id - 1).toInt()
    ): Task {
        return TestTaskFactory.createTask(
            id = id,
            title = title,
            subTasks = subTasks,
            isCompleted = isCompleted,
            color = color,
            index = index
        )
    }

    private fun createTasks(
        startId: Long = 1,
        count: Int = 3,
        subTaskCount: Int = 2,
        isCompleted: Boolean = false,
        color: Int = 0xFFFFFF
    ): List<Task> {
        return TestTaskFactory.createTasks(
            startId = startId,
            count = count,
            subTaskCount = subTaskCount,
            isCompleted = isCompleted,
            color = color
        )
    }

    @Test
    fun `invoke should reorder tasks correctly from lower to higher index`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 4)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 1,
            toIndex = 3
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = listOf(
            createTask(id = 1, index = 0),
            createTask(id = 3, index = 1),
            createTask(id = 4, index = 2),
            createTask(id = 2, index = 3)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should reorder tasks correctly from higher to lower index`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 4)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 3,
            toIndex = 1
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = listOf(
            createTask(id = 1, index = 0),
            createTask(id = 4, index = 1),
            createTask(id = 2, index = 2),
            createTask(id = 3, index = 3)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should handle moving task to the same index`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 3)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 1,
            toIndex = 1
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = createTasks(startId = 1, count = 3)
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should handle moving first task to last position`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 3)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 0,
            toIndex = 2
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = listOf(
            createTask(id = 2, index = 0),
            createTask(id = 3, index = 1),
            createTask(id = 1, index = 2)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should handle moving last task to first position`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 3)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 2,
            toIndex = 0
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = listOf(
            createTask(id = 3, index = 0),
            createTask(id = 1, index = 1),
            createTask(id = 2, index = 2)
        )
        assertEquals(expected, result)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `invoke should throw exception when fromIndex is out of bounds`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 2)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 5,
            toIndex = 1
        )

        // Act
        useCase.invoke(params)

        // Assert
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `invoke should throw exception when toIndex is out of bounds`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 2)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 0,
            toIndex = 5
        )

        // Act
        useCase.invoke(params)

        // Assert
    }

    @Test
    fun `invoke should handle empty task list`() = runTest {
        // Arrange
        val tasks = emptyList<Task>()
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 0,
            toIndex = 0
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = emptyList<Task>()
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should handle single task`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 1)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 0,
            toIndex = 0
        )

        // Act
        val result = useCase.invoke(params)

        // Assert
        val expected = createTasks(startId = 1, count = 1)
        assertEquals(expected, result)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `invoke should throw exception when fromIndex is negative`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 2)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = -1, // Negative index
            toIndex = 1
        )

        // Act
        useCase.invoke(params)

        // Assert
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun `invoke should throw exception when toIndex is negative`() = runTest {
        // Arrange
        val tasks = createTasks(startId = 1, count = 2)
        val params = UpdateTaskOrderUseCaseParams(
            tasks = tasks,
            fromIndex = 0,
            toIndex = -2 // Negative index
        )

        // Act
        useCase.invoke(params)

        // Assert
    }
}