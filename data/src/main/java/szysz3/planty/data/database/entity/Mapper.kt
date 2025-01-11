package szysz3.planty.data.database.entity

import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.model.MergedCell
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.model.SubGarden
import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task

fun GardenCellEntity.toDomain(plant: Plant?): GardenCell {
    return GardenCell(
        id = id,
        row = row,
        column = column,
        plant = plant,
        gardenId = garden_id
    )
}

fun GardenCell.toEntity(plantId: Long?): GardenCellEntity {
    return GardenCellEntity(
        id = id,
        row = row,
        column = column,
        plantId = plantId,
        garden_id = gardenId
    )
}

fun PlantEntity.toDomain(imageUrls: List<String>?): Plant {
    return Plant(
        id = this.id,
        latinName = this.latinName,
        family = this.family,
        commonName = this.commonName,
        habit = this.habit,
        deciduousEvergreen = this.deciduousEvergreen,
        height = this.height,
        width = this.width,
        ukHardiness = this.ukHardiness,
        medicinal = this.medicinal,
        range = this.range,
        habitat = this.habitat,
        soil = this.soil,
        shade = this.shade,
        moisture = this.moisture,
        wellDrained = this.wellDrained,
        nitrogenFixer = this.nitrogenFixer,
        ph = this.ph,
        acid = this.acid,
        alkaline = this.alkaline,
        saline = this.saline,
        wind = this.wind,
        growthRate = this.growthRate,
        pollution = this.pollution,
        poorSoil = this.poorSoil,
        drought = this.drought,
        wildlife = this.wildlife,
        pollinators = this.pollinators,
        selfFertile = this.selfFertile,
        knownHazards = this.knownHazards,
        synonyms = this.synonyms,
        cultivationDetails = this.cultivationDetails,
        edibleUses = this.edibleUses,
        usesNotes = this.usesNotes,
        propagation = this.propagation,
        heavyClay = this.heavyClay,
        edibilityRating = this.edibilityRating,
        frostTender = this.frostTender,
        scented = this.scented,
        medicinalRating = this.medicinalRating,
        imageUrls = imageUrls
    )
}

fun GardenPlantEntity.toDomain(): Plant {
    return Plant(
        id = id,
        latinName = latinName,
        family = family,
        commonName = commonName,
        habit = habit,
        deciduousEvergreen = deciduousEvergreen,
        height = height,
        width = width,
        ukHardiness = ukHardiness,
        medicinal = medicinal,
        range = range,
        habitat = habitat,
        soil = soil,
        shade = shade,
        moisture = moisture,
        wellDrained = wellDrained,
        nitrogenFixer = nitrogenFixer,
        ph = ph,
        acid = acid,
        alkaline = alkaline,
        saline = saline,
        wind = wind,
        growthRate = growthRate,
        pollution = pollution,
        poorSoil = poorSoil,
        drought = drought,
        wildlife = wildlife,
        pollinators = pollinators,
        selfFertile = selfFertile,
        knownHazards = knownHazards,
        synonyms = synonyms,
        cultivationDetails = cultivationDetails,
        edibleUses = edibleUses,
        usesNotes = usesNotes,
        propagation = propagation,
        heavyClay = heavyClay,
        edibilityRating = edibilityRating,
        frostTender = frostTender,
        scented = scented,
        medicinalRating = medicinalRating,
    )
}

fun Plant.toGardenPlantEntity(): GardenPlantEntity {
    return GardenPlantEntity(
        id = id,
        latinName = latinName,
        family = family,
        commonName = commonName,
        habit = habit,
        deciduousEvergreen = deciduousEvergreen,
        height = height,
        width = width,
        ukHardiness = ukHardiness,
        medicinal = medicinal,
        range = range,
        habitat = habitat,
        soil = soil,
        shade = shade,
        moisture = moisture,
        wellDrained = wellDrained,
        nitrogenFixer = nitrogenFixer,
        ph = ph,
        acid = acid,
        alkaline = alkaline,
        saline = saline,
        wind = wind,
        growthRate = growthRate,
        pollution = pollution,
        poorSoil = poorSoil,
        drought = drought,
        wildlife = wildlife,
        pollinators = pollinators,
        selfFertile = selfFertile,
        knownHazards = knownHazards,
        synonyms = synonyms,
        cultivationDetails = cultivationDetails,
        edibleUses = edibleUses,
        usesNotes = usesNotes,
        propagation = propagation,
        heavyClay = heavyClay,
        edibilityRating = edibilityRating,
        frostTender = frostTender,
        scented = scented,
        medicinalRating = medicinalRating,
        author = ""
    )
}

fun Plant.toEntity(): PlantEntity {
    return PlantEntity(
        latinName = this.latinName,
        family = this.family,
        commonName = this.commonName,
        habit = this.habit,
        deciduousEvergreen = this.deciduousEvergreen,
        height = this.height,
        width = this.width,
        ukHardiness = this.ukHardiness,
        medicinal = this.medicinal,
        range = this.range,
        habitat = this.habitat,
        soil = this.soil,
        shade = this.shade,
        moisture = this.moisture,
        wellDrained = this.wellDrained,
        nitrogenFixer = this.nitrogenFixer,
        ph = this.ph,
        acid = this.acid,
        alkaline = this.alkaline,
        saline = this.saline,
        wind = this.wind,
        growthRate = this.growthRate,
        pollution = this.pollution,
        poorSoil = this.poorSoil,
        drought = this.drought,
        wildlife = this.wildlife,
        pollinators = this.pollinators,
        selfFertile = this.selfFertile,
        knownHazards = this.knownHazards,
        synonyms = this.synonyms,
        cultivationDetails = this.cultivationDetails,
        edibleUses = this.edibleUses,
        usesNotes = this.usesNotes,
        propagation = this.propagation,
        heavyClay = this.heavyClay,
        edibilityRating = this.edibilityRating,
        frostTender = this.frostTender,
        scented = this.scented,
        medicinalRating = this.medicinalRating,
        author = ""
    )
}

fun TaskWithSubTasks.toDomain(): Task {
    return Task(
        id = task.id,
        title = task.title,
        subTasks = subTasks.map { subTask ->
            SubTask(
                id = subTask.id,
                description = subTask.description,
                isCompleted = subTask.isCompleted,
                cost = subTask.cost / 100f
            )
        },
        isCompleted = task.isCompleted,
        color = task.color,
        index = task.index
    )
}

fun SubTask.toEntity(taskId: Long): SubTaskEntity {
    return SubTaskEntity(
        id = id,
        taskId = taskId,
        description = description,
        isCompleted = isCompleted,
        cost = cost.times(100).toInt()
    )
}

fun SubTask.toEntity(taskId: Long, isCompleted: Boolean): SubTaskEntity {
    return SubTaskEntity(
        id = id,
        taskId = taskId,
        description = description,
        isCompleted = isCompleted,
        cost = cost.times(100).toInt()
    )
}

fun Task.toEntity(): Pair<TaskEntity, List<SubTaskEntity>> {
    val taskEntity = TaskEntity(
        id = id,
        title = title,
        isCompleted = isCompleted,
        color = color,
        index = index
    )
    val subTaskEntities = subTasks.map { subTask ->
        SubTaskEntity(
            id = subTask.id,
            taskId = 0, // Will be set when TaskEntity is inserted
            description = subTask.description,
            isCompleted = subTask.isCompleted,
            cost = subTask.cost.times(100).toInt()
        )
    }
    return taskEntity to subTaskEntities
}

fun MergedCellEntity.toDomain(): MergedCell {
    return MergedCell(
        id = id,
        parentGardenId = parent_garden_id,
        startRow = start_row,
        startColumn = start_column,
        endRow = end_row,
        endColumn = end_column,
        subGardenId = sub_garden_id
    )
}

fun MergedCell.toEntity(): MergedCellEntity {
    return MergedCellEntity(
        id = id,
        parent_garden_id = parentGardenId,
        start_row = startRow,
        start_column = startColumn,
        end_row = endRow,
        end_column = endColumn,
        sub_garden_id = subGardenId
    )
}

fun SubGardenEntity.toDomain(): SubGarden {
    return SubGarden(
        id = id,
        parentMergedCellId = parentMergedCellId,
        rows = rows,
        columns = columns
    )
}

fun SubGarden.toEntity(): SubGardenEntity {
    return SubGardenEntity(
        id = id,
        parentMergedCellId = parentMergedCellId,
        rows = rows,
        columns = columns
    )
}