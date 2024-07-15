package nz.govt.linz.landonlite.models

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import io.ebean.Model
import javax.persistence.*

@Entity
class Title (
    var description: String,
    var ownerName: String
): Model()
{
    @Id
    @GeneratedValue
    @JsonBackReference
    val id: Long = 0

    @JsonBackReference
    @OneToMany(mappedBy = "title", cascade = [CascadeType.ALL], orphanRemoval = true)
    val ownerHistory: MutableList<OwnerNameHistory> = mutableListOf()

    fun changeOwner(newOwnerName: String) {
        if (this.ownerName != newOwnerName) {
            ownerHistory.add(OwnerNameHistory(ownerName, this))
            ownerName = newOwnerName
        }
    }

    override fun toString(): String {
        return "Title(description='$description', ownerName='$ownerName', id=$id)"
    }
}
