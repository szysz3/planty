package szysz3.planty.screen.imagegallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import szysz3.planty.domain.usecase.plant.GetPlantUseCase
import szysz3.planty.screen.imagegallery.model.ImageGalleryScreenState
import szysz3.planty.util.TestCoroutineRule
import szysz3.planty.domain.model.Plant as PlantDomain

@ExperimentalCoroutinesApi
class ImageGalleryViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getPlantUseCase: GetPlantUseCase = mockk()

    private lateinit var viewModel: ImageGalleryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = ImageGalleryViewModel(
            getPlantUseCase = getPlantUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `updateImageUrls updates uiState with imageUrls when plant is found`() = runTest {
        val plantId = 1
        val listOfUrls = listOf("url1", "url2")
        val domainPlant = PlantDomain(
            id = plantId,
            latinName = "Latin Name",
            commonName = "Common Name",
            height = 5f,
            width = 0.5f,
            soil = "Loamy",
            ph = 6.5.toString(),
            growthRate = "Fast",
            deciduousEvergreen = "Evergreen",
            shade = "Partial",
            moisture = "High",
            wellDrained = true,
            poorSoil = false,
            drought = true,
            frostTender = false,
            cultivationDetails = "Details",
            ukHardiness = 1,
            imageUrls = listOfUrls
        )
        coEvery { getPlantUseCase(plantId) } returns domainPlant

        viewModel.updateImageUrls(plantId)
        advanceUntilIdle()

        val expectedState = ImageGalleryScreenState(
            imageUrls = listOfUrls
        )
        assertEquals(expectedState, viewModel.uiState.value)
        coVerify { getPlantUseCase(plantId) }
    }

    @Test
    fun `updateImageUrls updates uiState with empty list when plant is not found`() = runTest {
        val plantId = 2

        coEvery { getPlantUseCase(plantId) } returns null

        viewModel.updateImageUrls(plantId)
        advanceUntilIdle()

        val expectedState = ImageGalleryScreenState(
            imageUrls = emptyList()
        )
        assertEquals(expectedState, viewModel.uiState.value)
        coVerify { getPlantUseCase(plantId) }
    }
}
