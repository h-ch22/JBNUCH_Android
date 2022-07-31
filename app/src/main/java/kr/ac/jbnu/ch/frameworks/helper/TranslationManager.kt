package kr.ac.jbnu.ch.frameworks.helper

import android.net.Uri
import kr.ac.jbnu.ch.frameworks.models.TranslationModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class TranslationManager {
    private val API_KEY = "AIzaSyAjNhcSTpsxZMkKBahfNpzdt8lEYKhuZsw"

    fun translate(completion : (Boolean) -> Unit){

    }

    fun fetchSupportedLanguages(completion: (Boolean) -> Unit){
        var requestParam = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8")
        requestParam += "&" + URLEncoder.encode("target", "UTF-8") + "=" + URLEncoder.encode("kr", "UTF-8")

        var mURL = URL("https://translation.googleapis.com/language/translate/v2/languages" + requestParam)

        with(mURL.openConnection() as HttpURLConnection){
            requestMethod = "GET"
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use{
                val response = StringBuffer()
                var inputLine = it.readLine()

                while(inputLine != null){
                    response.append(inputLine)
                    inputLine = it.readLine()
                }

                it.close()

                println("Response : $response")
            }
        }
    }

    fun detectLanguage(text : String, completion : (Boolean) -> Unit){

    }

    private fun makeRequest(status : TranslationModel, urlParams : Map<String, String>, completion : (Boolean) -> Unit){
        var urlString = ""
        var httpMethod = ""

        when(status){
            TranslationModel.DETECT_LANGUAGE -> {
                urlString = "https://translation.googleapis.com/language/translate/v2/detect"
                httpMethod = "POST"
            }

            TranslationModel.TRANSLATE -> {
                urlString = "https://translation.googleapis.com/language/translate/v2"
                httpMethod = "POST"
            }

            TranslationModel.SUPPORTED_LANGUAGES -> {
                urlString = "https://translation.googleapis.com/language/translate/v2/languages"
                httpMethod = "GET"
            }
        }

        try{
            when(httpMethod){
                "GET" -> {

                }

                "POST" -> {

                }
            }
        } catch(e : Exception){
            e.printStackTrace()

            completion(false)
        }
    }
}