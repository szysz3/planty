package szysz3.planty.screen.plantaplant.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import szysz3.planty.domain.usecase.GetPlantsFromRangeParams
import szysz3.planty.domain.usecase.GetPlantsFromRangeUseCase
import szysz3.planty.screen.plantaplant.model.Plant
import toPresentationModel

class PlantPagingSource(
    private val getPlantFromRangeUseCase: GetPlantsFromRangeUseCase,
) : PagingSource<Int, Plant>() {
    override fun getRefreshKey(state: PagingState<Int, Plant>): Int? {
        return state.anchorPosition?.let { anchor ->
            val closestPage = state.closestPageToPosition(anchor)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> {
        val page = params.key ?: 1
        val startIndex = (page - 1) * 100
        val endIndex = startIndex + 99
        return try {
            val data = getPlantFromRangeUseCase(
                GetPlantsFromRangeParams(
                    startIndex,
                    endIndex
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