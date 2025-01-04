package szysz3.planty.screen.tasklist.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import szysz3.planty.core.model.toPresentation
import szysz3.planty.domain.usecase.task.ObserveTasksUseCase
import szysz3.planty.domain.usecase.task.UpdateTaskOrderUseCase
import szysz3.planty.domain.usecase.task.UpdateTaskOrderUseCaseParams
import szysz3.planty.domain.usecase.task.UpdateTasksUseCase
import szysz3.planty.util.TestCoroutineRule
import szysz3.planty.domain.model.Task as TaskDomain

@ExperimentalCoroutinesApi
class TaskListViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observeTasksUseCase: ObserveTasksUseCase = mockk()
    private val updateTaskOrderUseCase: UpdateTaskOrderUseCase = mockk()
    private val updateTasksUseCase: UpdateTasksUseCase = mockk()

    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = TaskListViewModel(
            observeTasksUseCase = observeTasksUseCase,
            updateTaskOrderUseCase = updateTaskOrderUseCase,
            updateTasksUseCase = updateTasksUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `observeTasks collects tasks and updates uiState`() = runTest {
        val domainTasks = listOf(
            TaskDomain(
                id = 1,
                title = "Task 1",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFF000000.toInt(),
                index = 0
            ),
            TaskDomain(
                id = 2,
                title = "Task 2",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFFFFFFFF.toInt(),
                index = 1
            )
        )
        val presentationTasks = domainTasks.toPresentation()

        coEvery { observeTasksUseCase.invoke(any()) } returns flowOf(domainTasks)

        viewModel.observeTasks()

        advanceUntilIdle()

        assertEquals(presentationTasks, viewModel.uiState.value.tasks)
        coVerify { observeTasksUseCase.invoke(any()) }
    }

    @Test
    fun `moveTask calls UpdateTaskOrderUseCase and updates uiState`() = runTest {
        val domainTasks = listOf(
            TaskDomain(
                id = 1,
                title = "Task 1",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFF000000.toInt(),
                index = 0
            ),
            TaskDomain(
                id = 2,
                title = "Task 2",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFFFFFFFF.toInt(),
                index = 1
            ),
            TaskDomain(
                id = 3,
                title = "Task 3",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFF123456.toInt(),
                index = 2
            )
        )
        coEvery { observeTasksUseCase.invoke(any()) } returns flowOf(domainTasks)

        viewModel.observeTasks()
        advanceUntilIdle()

        val reorderedDomainTasks = listOf(
            domainTasks[0],
            domainTasks[2],
            domainTasks[1]
        )
        val reorderedPresentationTasks = reorderedDomainTasks.toPresentation()

        coEvery { updateTaskOrderUseCase.invoke(any()) } returns reorderedDomainTasks

        viewModel.moveTask(fromIndex = 1, toIndex = 2)
        advanceUntilIdle()

        assertEquals(reorderedPresentationTasks, viewModel.uiState.value.tasks)
        coVerify {
            updateTaskOrderUseCase.invoke(
                UpdateTaskOrderUseCaseParams(
                    domainTasks,
                    fromIndex = 1,
                    toIndex = 2
                )
            )
        }
    }

    @Test
    fun `onPersistTaskOrder calls UpdateTasksUseCase with current tasks`() = runTest {
        val domainTasks = listOf(
            TaskDomain(
                id = 1,
                title = "Task 1",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFF000000.toInt(),
                index = 0
            ),
            TaskDomain(
                id = 2,
                title = "Task 2",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFFFFFFFF.toInt(),
                index = 1
            )
        )
        coEvery { observeTasksUseCase.invoke(any()) } returns flowOf(domainTasks)

        viewModel.observeTasks()
        advanceUntilIdle()

        coEvery { updateTasksUseCase.invoke(any()) } just Runs

        viewModel.onPersistTaskOrder()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            updateTasksUseCase.invoke(domainTasks)
        }
    }

    @Test
    fun `moveTask updates uiState correctly when UpdateTaskOrderUseCase returns reordered tasks`() =
        runTest {
            val domainTasks = listOf(
                TaskDomain(
                    id = 1,
                    title = "Task 1",
                    subTasks = emptyList(),
                    isCompleted = false,
                    color = 0xFF000000.toInt(),
                    index = 0
                ),
                TaskDomain(
                    id = 2,
                    title = "Task 2",
                    subTasks = emptyList(),
                    isCompleted = false,
                    color = 0xFFFFFFFF.toInt(),
                    index = 1
                ),
                TaskDomain(
                    id = 3,
                    title = "Task 3",
                    subTasks = emptyList(),
                    isCompleted = false,
                    color = 0xFF123456.toInt(),
                    index = 2
                )
            )
            coEvery { observeTasksUseCase.invoke(any()) } returns flowOf(domainTasks)

            viewModel.observeTasks()
            advanceUntilIdle()

            val reorderedDomainTasks = listOf(
                domainTasks[2],
                domainTasks[0],
                domainTasks[1]
            )
            val reorderedPresentationTasks = reorderedDomainTasks.toPresentation()

            coEvery { updateTaskOrderUseCase.invoke(any()) } returns reorderedDomainTasks

            viewModel.moveTask(fromIndex = 2, toIndex = 0)
            advanceUntilIdle()

            assertEquals(reorderedPresentationTasks, viewModel.uiState.value.tasks)
            coVerify {
                updateTaskOrderUseCase.invoke(
                    UpdateTaskOrderUseCaseParams(
                        domainTasks,
                        fromIndex = 2,
                        toIndex = 0
                    )
                )
            }
        }

    @Test
    fun `observeTasks handles multiple emissions correctly`() = runTest {
        val initialDomainTasks = listOf(
            TaskDomain(
                id = 1,
                title = "Task 1",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFF000000.toInt(),
                index = 0
            )
        )
        val updatedDomainTasks = listOf(
            TaskDomain(
                id = 1,
                title = "Task 1",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFF000000.toInt(),
                index = 0
            ),
            TaskDomain(
                id = 2,
                title = "Task 2",
                subTasks = emptyList(),
                isCompleted = false,
                color = 0xFFFFFFFF.toInt(),
                index = 1
            )
        )
        val updatedPresentationTasks = updatedDomainTasks.toPresentation()

        coEvery { observeTasksUseCase.invoke(any()) } returns flow {
            emit(initialDomainTasks)
            emit(updatedDomainTasks)
        }

        viewModel.observeTasks()
        advanceUntilIdle()

        assertEquals(updatedPresentationTasks, viewModel.uiState.value.tasks)
        coVerify { observeTasksUseCase.invoke(any()) }
    }
}