package nz.govt.linz.landonlite.service

import nz.govt.linz.landonlite.exception.ResourceNotFoundException
import nz.govt.linz.landonlite.models.Title
import nz.govt.linz.landonlite.repository.TitleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TitleService(private val titleRepository: TitleRepository) {

    @Transactional
    fun updateOwnerName(titleId: Long, newOwnerName: String): Title {
        val title = titleRepository.findById(titleId).orElseThrow { ResourceNotFoundException("Title not found") }
        title.changeOwner(newOwnerName)
        return titleRepository.save(title)
    }



    fun getTitleWithHistory(titleId: Long): TitleWithHistory {
        val title = titleRepository.findById(titleId).orElseThrow { ResourceNotFoundException("Title not found") }
        val ownerNames = title.ownerHistory.map { it.ownerName } + title.ownerName
        return TitleWithHistory(title, ownerNames)
    }


}

data class TitleWithHistory(val title: Title, val ownerNames: List<String>)