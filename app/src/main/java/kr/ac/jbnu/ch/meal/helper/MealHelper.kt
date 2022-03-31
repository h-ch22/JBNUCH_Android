package kr.ac.jbnu.ch.meal.helper

import android.util.Log
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.meal.models.MealDataModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MealHelper(val activity : MainActivity) {
    companion object{
        var mealList : HashMap<String, String> = hashMapOf()
    }

    private val coop_URL = "http://sobi.chonbuk.ac.kr/menu/week_menu.php"
    private val chambit_URL = "https://likehome.jbnu.ac.kr/home/main/inner.php?sMenu=B7200"
    private val Dorm_URL = "https://likehome.jbnu.ac.kr/home/main/inner.php?sMenu=B7100"
    private val iksan_URL = "https://likehome.jbnu.ac.kr/home/main/inner.php?sMenu=B7300"

    fun getDormMeal(day : String, completion : (Boolean) -> Unit){

    }

    fun getChambitMeal(day : String, completion : (Boolean) -> Unit){

    }

    fun getMedicalMeal(day : String, completion : (Boolean) -> Unit){
        val jsoup = Jsoup.connect(coop_URL)
        val doc : Document = jsoup.get()

    }

    fun getMeal(day : String, completion : (Boolean) -> Unit){

        val thread = Thread(Runnable {
            val jsoup = Jsoup.connect(coop_URL)
            val doc : Document = jsoup.get()

            val jsoup_chambit = Jsoup.connect(chambit_URL)
            val doc_chambit : Document = jsoup_chambit.get()

            val jsoup_Dorm = Jsoup.connect(Dorm_URL)
            val doc_Dorm : Document = jsoup_Dorm.get()

            val jsoup_iksan = Jsoup.connect(iksan_URL)
            val doc_iksan : Document = jsoup_iksan.get()

            var menu = ""

            when(day){
                "월" -> {
                    val jinsoo_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(3)").select("ul")

                    if(jinsoo_lunch.size == 0){
                        mealList.put("jinsoo_lunch", "메뉴 없음")
                    }

                    else{
                        for(element in jinsoo_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_lunch", menu)
                    }

                    val jinsoo_dinner : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(3)").select("ul")

                    if(jinsoo_dinner.size == 0){
                        mealList.put("jinsoo_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in jinsoo_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_dinner", menu)
                    }

                    val medical_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(284)").select("div.menu_scrollArea").select("div").select("table").select("tbody").select("tr:nth-child(1)").select("tr:nth-child(3)")

                    if(medical_lunch.size == 0){
                        mealList.put("medical_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in medical_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("medical_lunch", menu)
                    }

                    val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(3)").select("a")

                    if(dorm_breakfast.size == 0){
                        mealList.put("dorm_breakfast", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_breakfast){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_breakfast", menu)
                    }

                    val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(3)").select("a")

                    if(dorm_lunch.size == 0){
                        mealList.put("dorm_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_lunch", menu)
                    }

                    val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(3)").select("a")

                    if(dorm_dinner.size == 0){
                        mealList.put("dorm_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_dinner", menu)
                    }
                }

                "화" -> {
                    val jinsoo_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(4)")

                    if(jinsoo_lunch.size == 0){
                        Log.d("MealHelper", "No Data")

                        completion(false)
                    }

                    else{
                        for(element in jinsoo_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_lunch", menu)

                    }

                    val jinsoo_dinner : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(4)")

                    if(jinsoo_dinner.size == 0){
                        mealList.put("jinsoo_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in jinsoo_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_dinner", menu)
                    }

                    val medical_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(284)").select("div.menu_scrollArea").select("div").select("table").select("tbody").select("tr:nth-child(1)").select("tr:nth-child(4)")
                    if(medical_lunch.size == 0){
                        mealList.put("medical_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in medical_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("medical_lunch", menu)
                    }

                    val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(4)").select("a")

                    if(dorm_breakfast.size == 0){
                        mealList.put("dorm_breakfast", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_breakfast){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_breakfast", menu)
                    }

                    val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(4)").select("a")

                    if(dorm_lunch.size == 0){
                        mealList.put("dorm_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_lunch", menu)
                    }

                    val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(4)").select("a")

                    if(dorm_dinner.size == 0){
                        mealList.put("dorm_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_dinner", menu)
                    }
                }

                "수" -> {
                    val jinsoo_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(5)")

                    if(jinsoo_lunch.size == 0){
                        Log.d("MealHelper", "No Data")

                        completion(false)
                    }

                    else{
                        for(element in jinsoo_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_lunch", menu)

                    }

                    val jinsoo_dinner : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(5)")

                    if(jinsoo_dinner.size == 0){
                        mealList.put("jinsoo_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in jinsoo_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_dinner", menu)
                    }

                    val medical_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(284)").select("div.menu_scrollArea").select("div").select("table").select("tbody").select("tr:nth-child(1)").select("tr:nth-child(5)")

                    if(medical_lunch.size == 0){
                        mealList.put("medical_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in medical_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("medical_lunch", menu)
                    }

                    val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(5)").select("a")

                    if(dorm_breakfast.size == 0){
                        mealList.put("dorm_breakfast", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_breakfast){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_breakfast", menu)
                    }

                    val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(5)").select("a")

                    if(dorm_lunch.size == 0){
                        mealList.put("dorm_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_lunch", menu)
                    }

                    val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(5)").select("a")

                    if(dorm_dinner.size == 0){
                        mealList.put("dorm_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_dinner", menu)
                    }
                }

                "목" -> {
                    val jinsoo_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(6)")

                    if(jinsoo_lunch.size == 0){
                        Log.d("MealHelper", "No Data")

                        completion(false)
                    }

                    else{
                        for(element in jinsoo_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_lunch", menu)

                    }

                    val jinsoo_dinner : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(6)")

                    if(jinsoo_dinner.size == 0){
                        mealList.put("jinsoo_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in jinsoo_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_dinner", menu)
                    }

                    val medical_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(284)").select("div.menu_scrollArea").select("div").select("table").select("tbody").select("tr:nth-child(1)").select("tr:nth-child(6)")

                    if(medical_lunch.size == 0){
                        mealList.put("medical_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in medical_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("medical_lunch", menu)
                    }

                    val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(6)").select("a")

                    if(dorm_breakfast.size == 0){
                        mealList.put("dorm_breakfast", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_breakfast){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_breakfast", menu)
                    }

                    val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(6)").select("a")

                    if(dorm_lunch.size == 0){
                        mealList.put("dorm_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_lunch", menu)
                    }

                    val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(6)").select("a")

                    if(dorm_dinner.size == 0){
                        mealList.put("dorm_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_dinner", menu)
                    }
                }

                "금" -> {
                    val jinsoo_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(7)")

                    if(jinsoo_lunch.size == 0){
                        Log.d("MealHelper", "No Data")

                        completion(false)
                    }

                    else{
                        for(element in jinsoo_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_lunch", menu)

                    }

                    val jinsoo_dinner : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(283)").select("div.ttArea").select("span:nth-child(3)").select("span").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(7)")

                    if(jinsoo_dinner.size == 0){
                        mealList.put("jinsoo_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in jinsoo_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("jinsoo_dinner", menu)
                    }

                    val medical_lunch : Elements = doc.select("div.contentsArea.WeekMenu").select("div:nth-child(284)").select("div.menu_scrollArea").select("div").select("table").select("tbody").select("tr:nth-child(1)").select("tr:nth-child(7)")

                    if(medical_lunch.size == 0){
                        mealList.put("medical_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in medical_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("medical_lunch", menu)
                    }

                    val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(7)").select("a")

                    if(dorm_breakfast.size == 0){
                        mealList.put("dorm_breakfast", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_breakfast){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_breakfast", menu)
                    }

                    val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(7)").select("a")

                    if(dorm_lunch.size == 0){
                        mealList.put("dorm_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_lunch", menu)
                    }

                    val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(7)").select("a")

                    if(dorm_dinner.size == 0){
                        mealList.put("dorm_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_dinner", menu)
                    }
                }

                 "토" -> {
                     mealList.put("jinsoo_lunch", "미운영")
                     mealList.put("jinsoo_dinner", "미운영")
                     mealList.put("medical_lunch", "미운영")

                     val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(8)").select("a")

                     if(dorm_breakfast.size == 0){
                         mealList.put("dorm_breakfast", "메뉴 없음")

                     }

                     else{
                         for(element in dorm_breakfast){
                             element.run{
                                 menu += this.text()
                             }
                         }

                         mealList.put("dorm_breakfast", menu)
                     }

                     val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(8)").select("a")

                     if(dorm_lunch.size == 0){
                         mealList.put("dorm_lunch", "메뉴 없음")

                     }

                     else{
                         for(element in dorm_lunch){
                             element.run{
                                 menu += this.text()
                             }
                         }

                         mealList.put("dorm_lunch", menu)
                     }

                     val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(8)").select("a")

                     if(dorm_dinner.size == 0){
                         mealList.put("dorm_dinner", "메뉴 없음")

                     }

                     else{
                         for(element in dorm_dinner){
                             element.run{
                                 menu += this.text()
                             }
                         }

                         mealList.put("dorm_dinner", menu)
                     }
                 }

                "일" -> {
                    mealList.put("jinsoo_lunch", "미운영")
                    mealList.put("jinsoo_dinner", "미운영")
                    mealList.put("medical_lunch", "미운영")

                    val dorm_breakfast : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(1)").select("td:nth-child(2)").select("a")

                    if(dorm_breakfast.size == 0){
                        mealList.put("dorm_breakfast", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_breakfast){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_breakfast", menu)
                    }

                    val dorm_lunch : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(2)").select("td:nth-child(2)").select("a")

                    if(dorm_lunch.size == 0){
                        mealList.put("dorm_lunch", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_lunch){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_lunch", menu)
                    }

                    val dorm_dinner : Elements = doc_Dorm.select("calendar").select("table").select("tbody").select("tr:nth-child(3)").select("td:nth-child(2)").select("a")

                    if(dorm_dinner.size == 0){
                        mealList.put("dorm_dinner", "메뉴 없음")

                    }

                    else{
                        for(element in dorm_dinner){
                            element.run{
                                menu += this.text()
                            }
                        }

                        mealList.put("dorm_dinner", menu)
                    }
                }
            }

            Log.d("MealHelper", menu)

            completion(true)

        })

        thread.start()

    }
}