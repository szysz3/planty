package szysz3.planty.core.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import szysz3.planty.domain.model.Plant as PlantDomain
import szysz3.planty.domain.model.SubTask as SubTaskDomain
import szysz3.planty.domain.model.Task as TaskDomain

fun SubTaskDomain.toPresentation(): SubTask {
    return SubTask(
        id = this.id,
        description = this.description,
        isCompleted = this.isCompleted,
        cost = this.cost
    )
}

fun TaskDomain.toPresentation(): Task {
    return Task(
        id = this.id,
        title = this.title,
        subTasks = this.subTasks.map { it.toPresentation() },
        isCompleted = this.isCompleted,
        color = Color(color),
        index = this.index
    )
}

fun List<TaskDomain>.toPresentation(): List<Task> {
    return map { it.toPresentation() }
}

fun List<Task>.toDomain(): List<TaskDomain> {
    return map { it.toDomain() }
}

fun SubTask.toDomain(): SubTaskDomain {
    return SubTaskDomain(
        id = this.id,
        description = this.description,
        isCompleted = this.isCompleted,
        cost = this.cost
    )
}

fun Task.toDomain(): TaskDomain {
    return TaskDomain(
        id = this.id,
        title = this.title,
        subTasks = this.subTasks.map { it.toDomain() },
        isCompleted = this.isCompleted,
        color = this.color.toArgb(),
        index = this.index
    )
}

fun Plant.toDomain(): PlantDomain {
    return PlantDomain(
        id = this.id,
        latinName = this.latinName,
        commonName = this.commonName,
        height = this.height,
        width = this.width,
        soil = this.soil,
        ph = this.ph,
        growthRate = this.growthRate,
        deciduousEvergreen = this.deciduousEvergreen,
        shade = this.shade,
        moisture = this.moisture,
        wellDrained = this.wellDrained,
        poorSoil = this.poorSoil,
        drought = this.drought,
        frostTender = this.frostTender,
        cultivationDetails = this.cultivationDetails,
        ukHardiness = this.hardiness,
        imageUrls = this.imageUrls
    )
}

fun PlantDomain.toPresentationModel(): Plant {
    return Plant(
        id = this.id,
        latinName = this.latinName,
        commonName = this.commonName,
        height = this.height,
        width = this.width,
        soil = this.soil,
        ph = this.ph,
        growthRate = this.growthRate,
        deciduousEvergreen = this.deciduousEvergreen,
        shade = this.shade,
        moisture = this.moisture,
        wellDrained = this.wellDrained,
        poorSoil = this.poorSoil,
        drought = this.drought,
        frostTender = this.frostTender,
        cultivationDetails = this.cultivationDetails,
        hardiness = this.ukHardiness,
        imageUrls = this.imageUrls
    )
}

fun List<PlantDomain>.toPresentationModel(): List<Plant> {
    return map { plant ->
        plant.toPresentationModel()
    }
}

