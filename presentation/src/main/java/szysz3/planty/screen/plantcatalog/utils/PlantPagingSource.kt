package szysz3.planty.screen.plantcatalog.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import szysz3.planty.core.model.Plant
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.usecase.plant.PlantSearchUseCase
import szysz3.planty.domain.usecase.plant.PlantSearchUseCaseParams

class PlantPagingSource(
    private val searchPlantUseCase: PlantSearchUseCase,
    private val query: String?
) : PagingSource<Int, Plant>() {
    override fun getRefreshKey(state: PagingState<Int, Plant>): Int? {
        return state.anchorPosition?.let { anchor ->
            val closestPage = state.closestPageToPosition(anchor)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> {
        val page = params.key ?: 1
        val startIndex = (page - 1) * 20
        val endIndex = startIndex + 19
        return try {
            val data = searchPlantUseCase(
                PlantSearchUseCaseParams(
                    query,
                    endIndex,
                    startIndex
                )
            ).toPresentationModel()
            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}