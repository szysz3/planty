package szysz3.planty.data.repository

import okhttp3.OkHttpClient
import okhttp3.Request
import szysz3.planty.domain.repository.PlantImageRepository
import javax.inject.Inject

class PlantImageRepositoryImpl @Inject constructor(
    private val client: OkHttpClient
) : PlantImageRepository {
    
    override fun getPlantImages(plantName: String): List<String> {
        return searchImages(plantName).mapNotNull {
            getImageUrl(it)
        }
    }

    private fun searchImages(plantName: String): List<String> {
        val searchUrl = "$BASE_URL$SEARCH_PATH?search=$plantName$SEARCH_QUERY_PARAMS"
        val request = Request.Builder().url(searchUrl).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) return emptyList()

        val body = response.body?.string() ?: return emptyList()

        return parseSearchResponse(body)
    }

    private fun parseSearchResponse(response: String): List<String> {
        val regex = Regex(FILE_TITLE_REGEX)
        return regex.findAll(response).map { it.value }.toList()
    }

    private fun getImageUrl(fileName: String): String? {
        val detailUrl = "$BASE_URL$DETAIL_PATH$fileName"
        val request = Request.Builder().url(detailUrl).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) return null

        val body = response.body?.string() ?: return null

        return extractImageUrl(body)
    }

    private fun extractImageUrl(response: String): String? {
        val regex = Regex(URL_REGEX)
        return regex.find(response)?.groupValues?.get(1)
    }

    companion object {
        private const val BASE_URL = "https://commons.wikimedia.org"
        private const val SEARCH_PATH = "/w/index.php"
        private const val SEARCH_QUERY_PARAMS =
            "?title=Special:Search&profile=advanced&fulltext=1&ns6=1"
        private const val DETAIL_PATH = "/core/v1/media/file/"
        private const val URL_REGEX = "\"url\":\"(https[^\"]+)"
        private const val FILE_TITLE_REGEX = "File:[^\"<]+"
    }
}