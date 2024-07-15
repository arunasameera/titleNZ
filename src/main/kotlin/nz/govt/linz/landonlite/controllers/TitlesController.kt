package nz.govt.linz.landonlite.controllers

import nz.govt.linz.landonlite.models.Title
import nz.govt.linz.landonlite.service.TitleService
import nz.govt.linz.landonlite.service.TitleWithHistory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/titles")
class TitlesController (private val titleService: TitleService){


    @PutMapping("/{id}")
    fun updateOwnerName(@PathVariable id: Long, @RequestBody request: UpdateOwnerRequest): ResponseEntity<Title> {
        return ResponseEntity.ok().body(titleService.updateOwnerName(id, request.newOwnerName))
    }

    @GetMapping("/{id}")
    fun getTitleWithHistory(@PathVariable id: Long): TitleWithHistory {
        return titleService.getTitleWithHistory(id)
    }

    /*
    @GetMapping("/{id}")
    fun getTitle(@PathVariable id: Long): ResponseEntity<Title?> {
        val title = titleService.getTitleById(id);
        /*val title = DB.find(Title::class.java)
                .where()
                .eq("id", id)
                .findOne()
        return if (title == null)
            ResponseEntity.notFound().build()
        else*/
        return ResponseEntity.ok().body(title)
    }



    @PutMapping("/{id}")
    fun updateTitle(@PathVariable id: Long, @RequestBody body: Title): ResponseEntity<Title> {
        /*
        val title = DB.find(Title::class.java)
                .where()
                .eq("id", id)
                .findOne()
        if (title == null) {
            return ResponseEntity.notFound().build()
        }

        title.ownerName = body.ownerName
        title.save()
        */


        val existingTitle = titleService.getTitleById(id);
        //val updatedTitle = existingTitle.apply {ownerName = body.ownerName  }
        val updatedTitle = existingTitle.apply {ownerName = body.ownerName  }
        val savedTitle = titleService.updateTitle(id,updatedTitle)


        return ResponseEntity.ok().body(savedTitle)
    }
    */

}

data class UpdateOwnerRequest(val newOwnerName: String = "")
data class TitleWithHistory(val title: Title, val ownerNames: List<String>)