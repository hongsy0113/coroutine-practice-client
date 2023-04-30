package coroutinepractice.client.controller

import coroutinepractice.client.adaptor.ServerAdaptor
import coroutinepractice.client.service.NumberService
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private const val count = 1000

@RestController
class NumberController(
    private val serverAdaptor: ServerAdaptor,
    private val service: NumberService,
) {

    @GetMapping("/numbers")
    fun numbers(
        @RequestParam(name = "use_coroutine_yn", defaultValue = "N")
        useCoroutineYn: String,
        @RequestParam(name = "dispatcher", defaultValue = "default")
        dispatcherName: String,
    ) = runBlocking {
        if (useCoroutineYn == "N") {
            service.callServerSync(count)
        } else {
            when (dispatcherName) {
                "default" -> service.callServerByCoroutine(count, Dispatchers.Default)
                "singleThread" -> service.callServerByCoroutine(count, newSingleThreadContext("myPool"))
                "threadpool" -> service.callServerByCoroutine(count, newFixedThreadPoolContext(4, "myPool"))
                "unconfined" -> service.callServerByCoroutine(count, Dispatchers.Unconfined)
                else -> error("Invalid dispatcher name :  $dispatcherName")
            }
        }
    }
}