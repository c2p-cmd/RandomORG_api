package main

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import main.JDBC.myAPIKEY

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val DATA = "data"
const val BITS_USED = "bitsUsed"
const val BITS_LEFT = "bitsLeft"
const val REQUESTS_LEFT = "requestsLeft"

class RandomORG {
    private val apiKey = myAPIKEY
    private var data: JsonArray? = null
    private var result: JsonElement? = null
    private var numberOfRandoms = 0
    private var responseMap = HashMap<String, String>()

    @Throws(InvalidResponseException::class)
    fun generateString(
        n: Int = 1,
        length: Int = 8,
        charactersToUse: String = "abcdefghijklmnopqrstuvwxyz.ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        replacement: Boolean = false
    ) : String {
        val requestURL =
            "{" +
                    "\"jsonrpc\": \"2.0\",\n" +
                    "\"method\": \"generateStrings\",\n" +
                    "\"params\": {\n" +
                    "\"apiKey\": \"$apiKey\",\n" +
                    "\"n\": $n,\n" +
                    "\"length\": $length,\n" +
                    "\"characters\": \"$charactersToUse\",\n" +
                    "\"replacement\": $replacement,\n" +
                    "\"pregeneratedRandomization\": null\n" +
                    "},\n" +
                    "\"id\": 10645\n" +
            "}"
        doResponse(n, requestURL)
        return if (this.responseMap[DATA] == null) "" else this.responseMap[DATA]!!
    }

    @Throws(InvalidResponseException::class)
    fun generateIntegerSequence(
        n: Int = 1,
        length: Int = 8,
        min: Int = 0,
        max: Int = 10,
        replacement: Boolean = false,
        base: Int = 10
    ) : String {
        val requestURL =
            "{" +
                    "\"jsonrpc\": \"2.0\",\n" +
                    "\"method\": \"generateIntegerSequences\",\n" +
                    "\"params\": {\n" +
                    "\"apiKey\": \"411ef9b5-078e-4a57-8844-de83877f2505\",\n" +
                    "\"n\": $n,\n" +
                    "\"length\": $length,\n" +
                    "\"min\": $min,\n" +
                    "\"max\": $max,\n" +
                    "\"replacement\": $replacement,\n" +
                    "\"base\": $base,\n" +
                    "\"pregeneratedRandomization\": null\n" +
                    "},\n" +
                    "\"id\": 10645\n" +
                    "}"
        doResponse(n, requestURL)
        return if (this.responseMap[DATA] == null) "" else this.responseMap[DATA]!!
    }

    private fun doResponse(n: Int, requestURL: String) {
        val response = StringBuilder()
        val flag = 1
        numberOfRandoms = n
        var connection: HttpsURLConnection? = null
        var br: BufferedReader? = null

        try {
            val url = URL("https://api.random.org/json-rpc/4/invoke")
            connection = url.openConnection() as HttpsURLConnection

            connection.doInput = true
            connection.doOutput = true

            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            val outputStream = connection.outputStream

            outputStream.write(requestURL.toByteArray())
            outputStream.flush()
            outputStream.close()

            br = BufferedReader(InputStreamReader(connection.inputStream))

            var line: String?
            while (
                br.readLine().also { line = it} != null
            ) {
                response.append(line)
            }
        } catch (var22: IOException) {
            if (flag == 1) {
                throw InvalidResponseException("Host Unreachable", var22)
            }
            if (flag == 2) {
                throw InvalidResponseException("Null Response Received", var22)
            }
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (var21: IOException) {
                }
            }
            connection?.disconnect()
        }
        val parser = JsonParser()
        val root = parser.parse(response.toString())

        result = root.asJsonObject["result"]
        val random: JsonElement

        if (result == null) {
            random = root.asJsonObject["error"]
            val message = random.asJsonObject["message"]
            throw InvalidResponseException(message.toString())
        } else {
            random = result!!.asJsonObject["random"]
            val randomObj = random as JsonObject
            data = randomObj["data"].asJsonArray
        }

        // data array
        val array = ArrayList<String>(numberOfRandoms)

        if (numberOfRandoms == 0) {
            throw InvalidMethodCallException("Index does not exist")
        } else {
            for (i in 0 until numberOfRandoms) {
                val dataItem: String = try {
                    data!![i].toString()
                } catch (var5: NullPointerException) {
                    throw InvalidMethodCallException("Index does not exist", var5)
                }

                array.add(dataItem)
            }

            responseMap[DATA] = array.toString()
        }


        // bits used
        responseMap[BITS_USED] = this.result!!.asJsonObject.get(BITS_USED).asString

        // bits left
        responseMap[BITS_LEFT] = this.result!!.asJsonObject.get(BITS_LEFT).asString

        // requests left
        responseMap[REQUESTS_LEFT] = this.result!!.asJsonObject[REQUESTS_LEFT].asString
    }

    fun getAPIDetails(): Map<String, Int> = mapOf(
        "bitsUsed" to responseMap[BITS_USED]!!.toInt(),
        "bitsLeft" to responseMap[BITS_LEFT]!!.toInt(),
        "requestsLeft" to responseMap[REQUESTS_LEFT]!!.toInt()
    )
}