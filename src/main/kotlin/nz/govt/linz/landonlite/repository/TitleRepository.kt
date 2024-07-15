package nz.govt.linz.landonlite.repository

import nz.govt.linz.landonlite.models.Title
import org.springframework.data.jpa.repository.JpaRepository


interface TitleRepository : JpaRepository<Title, Long>