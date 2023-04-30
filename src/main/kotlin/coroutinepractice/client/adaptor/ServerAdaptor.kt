package coroutinepractice.client.adaptor

import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI


@Component
class ServerAdaptor(
    private val restTemplate: RestTemplate
) {

     fun callGetNumberAPI(number: Int): String {
        val uri: URI = UriComponentsBuilder
                .fromUriString("http://localhost:9090") //http://localhost에 호출
                .path("/api/server/numbers/$number")
                .build()
                .toUri()

        return restTemplate.getForObject(uri, String::class.java) ?: "error"
    }
}