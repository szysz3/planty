package szysz3.planty.domain.util

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.model.remote.Family
import szysz3.planty.domain.model.remote.Genus
import szysz3.planty.domain.model.remote.IdentificationQuery
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.model.remote.Result
import szysz3.planty.domain.model.remote.Species

object TestPlantIdResponseFactory {

    fun createSamplePlantIdResponse(): PlantIdResponse {
        return PlantIdResponse(
            query = createSampleIdentificationQuery(),
            language = "en",
            preferedReferential = "GBIF",
            bestMatch = "Ficus lyrata",
            results = listOf(
                createSampleResult(
                    speciesScientificName = "Ficus lyrata",
                    plant = TestPlantFactory.createPlant(
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
                ),
                createSampleResult(
                    speciesScientificName = "Monstera deliciosa",
                    plant = TestPlantFactory.createPlant(
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
                )
            ),
            version = "1.0",
            remainingIdentificationRequests = 10
        )
    }

    fun createPlantIdResponseWithoutLocalMatches(): PlantIdResponse {
        return PlantIdResponse(
            query = createSampleIdentificationQuery(),
            language = "en",
            preferedReferential = "GBIF",
            bestMatch = "Unknown Species",
            results = listOf(
                createSampleResult(
                    speciesScientificName = "Unknown Species",
                    plant = null
                )
            ),
            version = "1.0",
            remainingIdentificationRequests = 9
        )
    }

    private fun createSampleIdentificationQuery(): IdentificationQuery {
        return IdentificationQuery(
            project = "planty_project",
            images = listOf("image1.jpg", "image2.jpg"),
            organs = listOf("leaf", "stem"),
            includeRelatedImages = true,
            noReject = false
        )
    }

    private fun createSampleResult(
        speciesScientificName: String,
        plant: Plant?
    ): Result {
        return Result(
            score = 95.0,
            species = createSampleSpecies(speciesScientificName),
            plant = plant,
            gbif = mapOf("gbifId" to "12345"),
            powo = mapOf("powoId" to "67890"),
            iucn = null
        )
    }

    private fun createSampleSpecies(scientificNameWithoutAuthor: String): Species {
        return Species(
            scientificNameWithoutAuthor = scientificNameWithoutAuthor,
            scientificNameAuthorship = "L.",
            scientificName = scientificNameWithoutAuthor,
            genus = createSampleGenus(scientificNameWithoutAuthor.split(" ").first()),
            family = createSampleFamily("Moraceae"),
            commonNames = listOf("Fiddle Leaf Fig", "Swiss Cheese Plant")
        )
    }

    private fun createSampleGenus(genusName: String): Genus {
        return Genus(
            scientificNameWithoutAuthor = genusName,
            scientificNameAuthorship = "L.",
            scientificName = genusName
        )
    }

    private fun createSampleFamily(familyName: String): Family {
        return Family(
            scientificNameWithoutAuthor = familyName,
            scientificNameAuthorship = "L.",
            scientificName = familyName
        )
    }
}
