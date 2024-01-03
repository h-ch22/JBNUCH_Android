package kr.ac.jbnu.ch.frameworks.helper

import android.net.Uri
import android.util.Log
import kr.ac.jbnu.ch.frameworks.models.LanguageModel
import kr.ac.jbnu.ch.frameworks.models.TranslationModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class TranslationManager {
    private val API_KEY = "AIzaSyAjNhcSTpsxZMkKBahfNpzdt8lEYKhuZsw"

    companion object{
        var languageList = ArrayList<LanguageModel>()
        var translatedText = ""
    }

    fun translate(textToTranslate : String, targetLanguage : String, completion : (Boolean) -> Unit){
        var requestParam = "?" + URLEncoder.encode("key") + "=" + URLEncoder.encode(API_KEY, "UTF-8")
        requestParam += "&" + URLEncoder.encode("q") + "=" + URLEncoder.encode(textToTranslate)
        requestParam += "&" + URLEncoder.encode("target") + "=" + URLEncoder.encode(targetLanguage, "UTF-8")
        requestParam += "&" + URLEncoder.encode("format") + "=" + URLEncoder.encode("text", "UTF-8")

        val mURL = URL("https://translation.googleapis.com/language/translate/v2" + requestParam)

        with(mURL.openConnection() as HttpURLConnection){
            requestMethod = "POST"

            Thread{
                println("Response Code : $responseCode")

                if(responseCode != 200){
                    completion(false)
                }

                BufferedReader(InputStreamReader(inputStream)).use{
                    val response = StringBuffer()
                    var inputLine = it.readLine()

                    while(inputLine != null){
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }

                    it.close()

                    println("Response : $response")

                    val jsonObject = JSONObject(response.toString())
                    val data = jsonObject.getJSONObject("data")
                    val languages = data.getJSONArray("translations")
                    var translated = ""

                    for(i in 0..languages.length() - 1){
                        val translatedText = languages.getJSONObject(i).getString("translatedText")

                        translated += translatedText
                    }

                    translatedText = translated

                    completion(true)
                }
            }.start()
        }
    }

    fun fetchSupportedLanguages(completion: (Boolean) -> Unit){
        var requestParam = "?" + URLEncoder.encode("key") + "=" + URLEncoder.encode(API_KEY, "UTF-8")
        requestParam += "&" + URLEncoder.encode("target") + "=" + URLEncoder.encode("ko", "UTF-8")

        val mURL = URL("https://translation.googleapis.com/language/translate/v2/languages/" + requestParam)

        with(mURL.openConnection() as HttpURLConnection){
            requestMethod = "GET"

            Thread {

                println("Response Code : $responseCode")

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()

                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }

                    it.close()

                    println("Response : $response")

                    val jsonObject = JSONObject(response.toString())
                    val data = jsonObject.getJSONObject("data")
                    val languages = data.getJSONArray("languages")

                    for(i in 0..languages.length() - 1){
                        val langCode = languages.getJSONObject(i).getString("language")
                        val name = languages.getJSONObject(i).getString("name")

                        languageList.add(LanguageModel(langCode, name))
                    }

                    completion(true)
                }
            }.start()

            }
        }
}