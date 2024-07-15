package nz.govt.linz.landonlite.service

import nz.govt.linz.landonlite.exception.ResourceNotFoundException
import nz.govt.linz.landonlite.models.OwnerNameHistory
import nz.govt.linz.landonlite.models.Title
import nz.govt.linz.landonlite.repository.TitleRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class TitleServiceTest {

    @Mock
    lateinit var titleRepository: TitleRepository

    @InjectMocks
    lateinit var titleService: TitleService

    @Test
    fun `updateOwnerName should update and return the title`() {
        val titleId = 1L
        val newOwnerName = "New Owner"
        val title = Title("Title Description", "Old Owner")

        `when`(titleRepository.findById(titleId)).thenReturn(Optional.of(title))
        `when`(titleRepository.save(title)).thenAnswer { invocation -> invocation.getArgument(0) }

        val updatedTitle = titleService.updateOwnerName(titleId, newOwnerName)

        assertEquals(newOwnerName, updatedTitle.ownerName)
        assertEquals(1, updatedTitle.ownerHistory.size)
        assertEquals("Old Owner", updatedTitle.ownerHistory[0].ownerName)
    }

    @Test
    fun `updateOwnerName should throw ResourceNotFoundException if title not found`() {
        val titleId = 1L
        val newOwnerName = "New Owner"

        `when`(titleRepository.findById(titleId)).thenReturn(Optional.empty())

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            titleService.updateOwnerName(titleId, newOwnerName)
        }

        assertEquals("Title not found", exception.message)
    }

    @Test
    fun `getTitleWithHistory should return title with owner history`() {
        val titleId = 1L
        val ownerNames = listOf("Owner1", "Owner2")
        val title = Title("Title Description", "Current Owner").apply {
            ownerHistory.addAll(ownerNames.map { ownerName -> OwnerNameHistory(ownerName, this) })
        }

        `when`(titleRepository.findById(titleId)).thenReturn(Optional.of(title))

        val titleWithHistory = titleService.getTitleWithHistory(titleId)

        assertEquals(title, titleWithHistory.title)
        assertEquals(listOf("Owner1", "Owner2", "Current Owner"), titleWithHistory.ownerNames)
    }

    @Test
    fun `getTitleWithHistory should throw ResourceNotFoundException if title not found`() {
        val titleId = 1L

        `when`(titleRepository.findById(titleId)).thenReturn(Optional.empty())

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            titleService.getTitleWithHistory(titleId)
        }

        assertEquals("Title not found", exception.message)
    }
}
