package nz.govt.linz.landonlite.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class OwnerNameHistory(
    var ownerName: String,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id")
    var title: Title,

    @Column(nullable = false, updatable = false)
    val changedAt: LocalDateTime = LocalDateTime.now()


) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0


}