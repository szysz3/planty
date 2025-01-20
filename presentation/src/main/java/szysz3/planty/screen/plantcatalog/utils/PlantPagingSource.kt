package szysz3.planty.screen.plantcatalog.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import szysz3.planty.core.model.Plant
import szysz3.planty.core.model.toPresentationModel
import szysz3.planty.domain.usecase.plant.PlantSearchUseCase
import szysz3.planty.domain.usecase.plant.PlantSearchUseCaseParams

/**
 * PagingSource implementation for loading plant data in pages.
 *
 * @param searchPlantUseCase Use case for searching plants
 * @param query Optional search query to filter plants
 * @param pageSize Number of items to load per page, defaults to 20
 */
class PlantPagingSource(
    private val searchPlantUseCase: PlantSearchUseCase,
    private val query: String?,
    private val pageSize: Int = DEFAULT_PAGE_SIZE
) : PagingSource<Int, Plant>() {

    override fun getRefreshKey(state: PagingState<Int, Plant>): Int? {
        return ((state.anchorPosition ?: 0) / pageSize + 1).takeIf { it > 0 }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> = try {
        val page = params.key ?: INITIAL_PAGE
        val startIndex = (page - 1) * pageSize
        val endIndex = startIndex + (pageSize - 1)

        val data = searchPlantUseCase(
            PlantSearchUseCaseParams(
                query = query,
                limit = endIndex,
                offset = startIndex
            )
        ).toPresentationModel()

        LoadResult.Page(
            data = data,
            prevKey = if (page == INITIAL_PAGE) null else page - 1,
            nextKey = if (data.isEmpty()) null else page + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    companion object {
        private const val INITIAL_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 20
    }
}