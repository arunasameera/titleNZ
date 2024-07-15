package nz.govt.linz.landonlite

import io.ebean.DB
import nz.govt.linz.landonlite.models.Title
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
internal class SeedData : CommandLineRunner {

    override fun run(vararg strings: String) { // Create the seed titles if the database is empty
        if (DB.find(Title::class.java).findCount() == 0) {
            Title("Lot 1 on Block 1", "Jane Doe").save()
            Title("Lot 2 on Block 1", "Bob Smith").save()
        }
        // Print a list of titles in the database
        DB.find(Title::class.java).where().findList()
                .forEach { x -> println(x) }
    }
}