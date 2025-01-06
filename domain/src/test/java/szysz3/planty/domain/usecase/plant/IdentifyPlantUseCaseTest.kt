package szysz3.planty.domain.usecase.plant

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.repository.PlantIdRepository
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.util.TestIdentifyPlantsParamsFactory
import szysz3.planty.domain.util.TestPlantFactory
import szysz3.planty.domain.util.TestPlantIdResponseFactory
import java.io.ByteArrayInputStream

class IdentifyPlantUseCaseTest {

    private val idRepo: PlantIdRepository = mockk()
    private val plantRepository: PlantRepository = mockk()
    private val context: Context = mockk()
    private val contentResolver: ContentResolver = mockk()

    private lateinit var useCase: IdentifyPlantUseCase

    private lateinit var ficusLyrata: Plant
    private lateinit var monsteraDeliciosa: Plant

    @Before
    fun setUp() {
        useCase = IdentifyPlantUseCase(idRepo, plantRepository, context)
        every { context.contentResolver } returns contentResolver

        ficusLyrata = TestPlantFactory.createPlant(
            id = 101,
            latinName = "Ficus lyrata",
            commonName = "Fiddle Leaf Fig",
            family = "Moraceae",
            habit = "Evergreen tree",
            deciduousEvergreen = "Evergreen",
            height = 3.0f,
            width = 2.0f,
            ukHardiness = 8,
            medicinal = "None",
            range = "Native to Western Africa",
            habitat = "Rainforests",
            soil = "Well-drained",
            shade = "Partial shade",
            moisture = "Moderate",
            wellDrained = true,
            growthRate = "Fast",
            wildlife = "Birds",
            pollinators = "Bees",
            selfFertile = true,
            knownHazards = "None",
            synonyms = "Ficus lyrata var. vera",
            cultivationDetails = "Requires bright, indirect light",
            edibleUses = "None",
            usesNotes = "Popular houseplant",
            propagation = "Stem cuttings",
            heavyClay = false,
            frostTender = true,
            scented = false,
            imageUrls = listOf("http://example.com/image1.jpg")
        )

        monsteraDeliciosa = TestPlantFactory.createPlant(
            id = 102,
            latinName = "Monstera deliciosa",
            commonName = "Swiss Cheese Plant",
            family = "Araceae",
            habit = "Climbing vine",
            deciduousEvergreen = "Evergreen",
            height = 2.5f,
            width = 1.5f,
            ukHardiness = 9,
            medicinal = "None",
            range = "Native to Central America",
            habitat = "Rainforests",
            soil = "Rich, well-drained",
            shade = "Partial shade",
            moisture = "High",
            wellDrained = true,
            growthRate = "Fast",
            wildlife = "Butterflies",
            pollinators = "Bees",
            selfFertile = true,
            knownHazards = "Toxic to pets",
            synonyms = "Monstera adansonii",
            cultivationDetails = "Requires high humidity",
            edibleUses = "Fruit is edible",
            usesNotes = "Popular for indoor decor",
            propagation = "Stem cuttings",
            heavyClay = false,
            edibilityRating = 3,
            frostTender = true,
            scented = false,
            imageUrls = listOf("http://example.com/image2.jpg")
        )
    }

    @After
    fun tearDown() {
        clearMocks(idRepo, plantRepository, context, contentResolver)
    }

    private fun mockImageUri(uri: Uri, imageData: ByteArray) {
        every { contentResolver.openInputStream(uri) } returns ByteArrayInputStream(imageData)
    }

    @Test
    fun `invoke should successfully identify plants and find matching local plants`() = runTest {
        val apiKey = "test_api_key"
        val testUri: Uri = mockk()
        val imageData = byteArrayOf(1, 2, 3, 4)

        mockImageUri(testUri, imageData)

        val params = TestIdentifyPlantsParamsFactory.createSampleParams(apiKey, testUri)

        val plantIdResponse: PlantIdResponse =
            TestPlantIdResponseFactory.createSamplePlantIdResponse()

        coEvery { idRepo.identifyPlant(imageData, apiKey) } returns plantIdResponse

        coEvery {
            plantRepository.searchPlants("Ficus lyrata", 1, 0)
        } returns listOf(ficusLyrata)

        coEvery {
            plantRepository.searchPlants("Monstera deliciosa", 1, 0)
        } returns listOf(monsteraDeliciosa)

        val result: PlantIdResponse? = useCase.invoke(params)

        assertNotNull(result)
        assertEquals(2, result?.results?.size)
        assertEquals("Fiddle Leaf Fig", result?.results?.get(0)?.plant?.commonName)
        assertEquals("Swiss Cheese Plant", result?.results?.get(1)?.plant?.commonName)

        coVerify(exactly = 1) { contentResolver.openInputStream(testUri) }
        coVerify(exactly = 1) { idRepo.identifyPlant(imageData, apiKey) }
        coVerify(exactly = 1) { plantRepository.searchPlants("Ficus lyrata", 1, 0) }
        coVerify(exactly = 1) { plantRepository.searchPlants("Monstera deliciosa", 1, 0) }

        confirmVerified(contentResolver, idRepo, plantRepository)
    }

    @Test
    fun `invoke should successfully identify plants but no matching local plants`() = runTest {
        val apiKey = "test_api_key"
        val testUri: Uri = mockk()
        val imageData = byteArrayOf(5, 6, 7, 8)

        mockImageUri(testUri, imageData)

        val params = TestIdentifyPlantsParamsFactory.createSampleParams(apiKey, testUri)

        val plantIdResponse: PlantIdResponse =
            TestPlantIdResponseFactory.createPlantIdResponseWithoutLocalMatches()

        coEvery { idRepo.identifyPlant(imageData, apiKey) } returns plantIdResponse

        coEvery {
            plantRepository.searchPlants("Unknown Species", 1, 0)
        } returns emptyList()

        val result: PlantIdResponse? = useCase.invoke(params)

        assertNotNull(result)
        assertEquals(1, result?.results?.size)
        assertNull(result?.results?.get(0)?.plant)

        coVerify(exactly = 1) { contentResolver.openInputStream(testUri) }
        coVerify(exactly = 1) { idRepo.identifyPlant(imageData, apiKey) }
        coVerify(exactly = 1) { plantRepository.searchPlants("Unknown Species", 1, 0) }

        confirmVerified(contentResolver, idRepo, plantRepository)
    }

    @Test
    fun `invoke should process only the first imageUri when multiple are provided`() = runTest {
        val apiKey = "test_api_key"
        val testUri1: Uri = mockk()
        val testUri2: Uri = mockk()
        val imageData1 = byteArrayOf(9, 10, 11, 12)
        val imageData2 = byteArrayOf(13, 14, 15, 16)

        mockImageUri(testUri1, imageData1)
        every { contentResolver.openInputStream(testUri2) } returns ByteArrayInputStream(imageData2)

        val params = TestIdentifyPlantsParamsFactory.createSampleParamsMultipleUris(
            apiKey,
            listOf(testUri1, testUri2)
        )

        val plantIdResponse: PlantIdResponse =
            TestPlantIdResponseFactory.createSamplePlantIdResponse()

        coEvery { idRepo.identifyPlant(imageData1, apiKey) } returns plantIdResponse

        coEvery {
            plantRepository.searchPlants("Ficus lyrata", 1, 0)
        } returns listOf(ficusLyrata)

        coEvery {
            plantRepository.searchPlants("Monstera deliciosa", 1, 0)
        } returns listOf(monsteraDeliciosa)

        val result: PlantIdResponse? = useCase.invoke(params)

        assertNotNull(result)
        assertEquals(2, result?.results?.size)
        assertEquals("Fiddle Leaf Fig", result?.results?.get(0)?.plant?.commonName)
        assertEquals("Swiss Cheese Plant", result?.results?.get(1)?.plant?.commonName)

        coVerify(exactly = 1) { contentResolver.openInputStream(testUri1) }
        coVerify(exactly = 0) { contentResolver.openInputStream(testUri2) }
        coVerify(exactly = 1) { idRepo.identifyPlant(imageData1, apiKey) }
        coVerify(exactly = 1) { plantRepository.searchPlants("Ficus lyrata", 1, 0) }
        coVerify(exactly = 1) { plantRepository.searchPlants("Monstera deliciosa", 1, 0) }

        confirmVerified(contentResolver, idRepo, plantRepository)
    }
}
