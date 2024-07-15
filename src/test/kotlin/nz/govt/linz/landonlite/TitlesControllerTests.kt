package nz.govt.linz.landonlite


import org.junit.Test
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [LandOnLiteApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TitlesControllerTests {

    @LocalServerPort
    private val port = 0

    var restTemplate = TestRestTemplate()
    var getHeaders = HttpHeaders()
    var postHeaders = HttpHeaders(LinkedMultiValueMap(mapOf(
        "Content-Type" to listOf("application/json")
    )))
    var entity = HttpEntity<String?>(null, getHeaders)

    @Test
    fun testGetTitle() {
        val response = get("/api/titles/1")
        JSONAssert.assertEquals("{id:1,ownerName:'Jane Doe'}", response.body, false)
    }

    @Test
    fun testUpdateTitleOwner() { // Check that the update request returns the expected response
        var response = post("/api/titles/1", "{\"id\":1,\"ownerName\":\"Brian Davies\"}")
        JSONAssert.assertEquals("{id:1,ownerName:'Brian Davies'}", response.body, false)
        // Check that a fetch returns the updated owner name, and the original description
        response = get("/api/titles/1")
        JSONAssert.assertEquals("{id:1,ownerName:'Brian Davies',description:'Lot 1 on Block 1'}", response.body, false)
    }

    private fun get(url: String): ResponseEntity<String> {
        val response = restTemplate.exchange(createURLWithPort(url),
            HttpMethod.GET, entity, String::class.java)
        println(response.body)
        return response
    }

    private fun post(url: String, body: String): ResponseEntity<String> {
        val response = restTemplate.exchange(createURLWithPort(url),
            HttpMethod.POST, HttpEntity(body, postHeaders), String::class.java)
        println(response.body)
        return response
    }

    private fun createURLWithPort(uri: String): String {
        return "http://localhost:$port$uri"
    }
}