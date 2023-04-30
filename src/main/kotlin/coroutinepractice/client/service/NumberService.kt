package coroutinepractice.client.service

import coroutinepractice.client.adaptor.ServerAdaptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class NumberService(
    private val serverAdaptor: ServerAdaptor,
) {
    fun callServerSync(count: Int) {
        val result = mutableListOf<String>()
        for (i: Int in 1..count) {
            println("Starting request#$i in ${Thread.currentThread().name}")
            serverAdaptor.callGetNumberAPI(i).also {
                result.add(it)
            }
            println("Finishing request#$i in ${Thread.currentThread().name}")
        }
        println(result)
    }

    suspend fun callServerByCoroutine(count: Int, dispatcher: CoroutineDispatcher) {
        val requests = mutableListOf<Deferred<String>>()
        val result = mutableListOf<String>()

        println("Using CoroutineDispatcher : $dispatcher\n")

        for (i: Int in 1..count) {
            requests.add(
                GlobalScope.async(dispatcher) {
                    println("Starting request#$i in ${Thread.currentThread().name}\t\tCoroutineScope: $this.")
                    val value = serverAdaptor.callGetNumberAPI(i)
                    println("Finishing request#$i in ${Thread.currentThread().name}\t\tCoroutineScope: $this")
                    value
                }
            )
        }
        requests.forEach{
            it.await().also {
                result.add(it)
            }
        }

        println(result)
    }
}