package kr.ac.jbnu.ch.userManagement.view

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.awesomedialog.*
import kr.ac.jbnu.ch.R
import kr.ac.jbnu.ch.databinding.LayoutCountryselectionBinding
import kr.ac.jbnu.ch.frameworks.view.MainActivity
import kr.ac.jbnu.ch.frameworks.view.StartActivity
import kr.ac.jbnu.ch.userManagement.helper.UserManagement
import java.util.*

class CountrySelectionView : Fragment() {
    private val mainText = arrayOf("안녕하세요", "Hi", "你好", "おはようございます", "hân hạnh")
    private val subText = arrayOf("계속 진행하려면 국가를 선택해주세요.", "Please select a country to proceed.", "如果想继续，请选择国家。", "継続して進めるためには国家を選択してください。", "Nếu muốn tiếp tục tiến hành thì hãy chọn quốc gia.")
    private val countryList = arrayOf("대한민국 (Republic of Korea)", "United States", "中国 (China)", "日本 (Japan)", "Việt Nam (Vietnam)")

    private var currentIndex = 0
    var selectedCountry = MutableLiveData<String>()

    private lateinit var dialog : AlertDialog
    private lateinit var view : LayoutCountryselectionBinding

    init{
        selectedCountry.value = "SELECT COUNTRY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout : LayoutCountryselectionBinding = DataBindingUtil.inflate(inflater, R.layout.layout_countryselection, container, false)
        layout.view = this
        layout.lifecycleOwner = this

        this.view = layout

        layout.txtTitleMain.setFactory(mViewSwitcherFactory)
        layout.txtTitleSub.setFactory(ViewSwitcherFactory_subTitle)

        val timerTask : TimerTask = object : TimerTask(){
            override fun run() {
                currentIndex += 1

                if( currentIndex == 4){
                    currentIndex = 0
                }

                activity?.runOnUiThread(object : TimerTask(){
                    override fun run() {
                        layout.txtTitleMain.setText(mainText[currentIndex])
                        layout.txtTitleSub.setText(subText[currentIndex])
                    }

                })
            }
        }

        val timerCall = Timer()
        timerCall.schedule(timerTask, 0, 1500)

        return layout.root
    }

    private val mViewSwitcherFactory : ViewSwitcher.ViewFactory = object : ViewSwitcher.ViewFactory {
        override fun makeView(): View {
            val textView = TextView(activity as StartActivity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 18F
            textView.setTypeface(null, Typeface.BOLD)
            textView.setTextColor(resources.getColor(R.color.txtColor))

            return textView
        }
    }

    private val ViewSwitcherFactory_subTitle : ViewSwitcher.ViewFactory = object : ViewSwitcher.ViewFactory{
        override fun makeView(): View {
            val textView = TextView(activity as StartActivity)
            textView.gravity = Gravity.CENTER
            textView.textSize = 12F
            textView.setTextColor(resources.getColor(R.color.gray))

            return textView
        }

    }

    fun updateCountry(){
        var countryCode = ""
        val helper = UserManagement()

        when(selectedCountry.value){
            "대한민국 (Republic of Korea)" -> countryCode = "kr"
            "United States" -> countryCode = "us"
            "中国 (China)" -> countryCode = "cn"
            "日本 (Japan)" -> countryCode = "jp"
            "Việt Nam (Vietnam)" -> countryCode = "vn"
            else -> countryCode = ""
        }

        if(countryCode != ""){
            view.btnConfirm.visibility = View.GONE
            view.progressView.visibility = View.VISIBLE

            helper.updateCountry(countryCode){
                if(it){
                    val intent = Intent(activity as StartActivity, MainActivity :: class.java)
                    startActivity(intent)
                }

                else{
                    view.btnConfirm.visibility = View.VISIBLE
                    view.progressView.visibility = View.GONE

                    AwesomeDialog.build(activity as StartActivity)
                        .title("Error", null, resources.getColor(R.color.black))
                        .body("An error occurred while processing the operation.\n" +
                                "Please check the network status or try again later.", null, resources.getColor(R.color.black))
                        .icon(R.drawable.ic_warning)
                        .onPositive("OK")
                }
            }
        }

        else{
            AwesomeDialog.build(activity as StartActivity)
                .title("Empty Field", null, resources.getColor(R.color.black))
                .body("Please select your country.", null, resources.getColor(R.color.black))
                .icon(R.drawable.ic_warning)
                .onPositive("OK")
        }
    }

    fun showDialog(){
        dialog = AlertDialog.Builder(context)
            .setItems(countryList,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    selectedCountry.value = countryList[i]
                })
            .setTitle("Select Country")
            .create()

        dialog.show()
    }
}