package nz.govt.linz.landonlite.response

import nz.govt.linz.landonlite.models.Title

class TitleResponse (
    val id: Long,
    var description: String,
    var ownerName: String
) {
    companion object {
        fun fromEntity(title: Title): TitleResponse =
            TitleResponse(
                id = title.id,
                description = title.description,
                ownerName = title.ownerName
            )
    }
}