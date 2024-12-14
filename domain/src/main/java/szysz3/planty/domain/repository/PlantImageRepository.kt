package szysz3.planty.domain.repository

interface PlantImageRepository {
    fun getPlantImages(plantName: String): List<String>?
}