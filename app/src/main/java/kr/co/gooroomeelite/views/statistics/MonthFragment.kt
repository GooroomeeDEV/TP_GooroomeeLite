package kr.co.gooroomeelite.views.statistics

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kr.co.gooroomeelite.R
import kr.co.gooroomeelite.adapter.DailySubjectAdapter
import kr.co.gooroomeelite.databinding.FragmentMonthBinding
import kr.co.gooroomeelite.viewmodel.SubjectViewModel
import kr.co.gooroomeelite.views.statistics.share.ShareActivity
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class MonthFragment : Fragment() {
    private lateinit var binding: FragmentMonthBinding
    private val viewModel: SubjectViewModel by viewModels()

    private lateinit var chart: BarChart
    private val monthlySubjectAdapter: DailySubjectAdapter by lazy { DailySubjectAdapter(emptyList()) }


//     LocalDate ???????????? ??????
//    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d")


    //??????,?????? ?????? ??????
    private val ContentColor by lazy {
        ContextCompat.getColor(this.requireContext(), R.color.content_black)
    }

    //????????? ?????? ???,??? (???????????? ??????)
    private val transparentBlackColor by lazy {
        ContextCompat.getColor(this.requireContext(), R.color.transparent_black)
    }

    var count : Int = 0
    var monthCount : Int = 0

//    private val customMarkerView by lazy {
//        CustomMarketView(this.requireContext(), R.layout.item_marker_view)
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthBinding.inflate(inflater,container,false)
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_month,container,false)
        binding.month = this

        binding.shareButton.setOnClickListener {
            requestPermission()
        }
        //??? ??????
        binding.monthBarChart.setNoDataText("")
        initChart(binding.monthBarChart)
        binding.monthBarChart.setVisibleXRangeMaximum(30f)
        binding.monthBarChart.moveViewToX(30f)

        monthlySubjectPieChart()
        binding.recyclerViewMonth.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = monthlySubjectAdapter
        }
        divideDataFromFirebase()
        moveCalendarByMonth()
        return binding.root
    }

    @SuppressLint("LongLogTag")
    private fun divideDataFromFirebase() {
        viewModel.list.observe(viewLifecycleOwner) {
            val textformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            //??????
            val dateNow: LocalDateTime = LocalDateTime.now()
            val dateNowFormat : String = dateNow.format(textformatter)
            Log.d("dafgsdfh",dateNowFormat)
            //?????? ?????? ????????? ?????? ??????
            val targetYearMonth: YearMonth = YearMonth.from(LocalDate.parse(dateNowFormat, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
            val end = targetYearMonth.atEndOfMonth()
            Log.d("dafgsdfh",end.toString())

            //5??? ????????? ??? ?????????
            val monDay5 = end.with(DayOfWeek.MONDAY) //???
            val tuesDay5 = end.with(DayOfWeek.TUESDAY) //???
            val wednesDay5 = end.with(DayOfWeek.WEDNESDAY) //???
            val thursDay5 = end.with(DayOfWeek.THURSDAY) //???
            val friDay5 = end.with(DayOfWeek.FRIDAY) //???
            val saturDay5 = end.with(DayOfWeek.SATURDAY) //???
            val sunDay5 = end.with(DayOfWeek.SUNDAY) //???

            var mondays5Value : Float = 0f
            var tuesdays5Value: Float = 0f
            var wednesdays5Value: Float = 0f
            var thursdays5Value: Float = 0f
            var fridays5Value: Float = 0f
            var saturdays5Value: Float = 0f
            var sundays5Value: Float = 0f

            var mondaySum5: Float = 0f
            var tuesdaySum5: Float = 0f
            var wednesdaySum5: Float = 0f
            var thursdaySum5: Float = 0f
            var fridaySum5: Float = 0f
            var saturdaySum5: Float = 0f
            var sundaySum5: Float = 0f
            var totalSum5: Float = 0f

            //4??? ????????? ??? ?????????
            val monDay4 = end.with(DayOfWeek.MONDAY) //???
            val tuesDay4 = end.with(DayOfWeek.TUESDAY) //???
            val wednesDay4 = end.with(DayOfWeek.WEDNESDAY) //???
            val thursDay4 = end.with(DayOfWeek.THURSDAY) //???
            val friDay4 = end.with(DayOfWeek.FRIDAY) //???
            val saturDay4 = end.with(DayOfWeek.SATURDAY) //???
            val sunDay4 = end.with(DayOfWeek.SUNDAY) //???

            var mondays4Value : Float = 0f
            var tuesdays4Value: Float = 0f
            var wednesdays4Value: Float = 0f
            var thursdays4Value: Float = 0f
            var fridays4Value: Float = 0f
            var saturdays4Value: Float = 0f
            var sundays4Value: Float = 0f

            var mondaySum4: Float = 0f
            var tuesdaySum4: Float = 0f
            var wednesdaySum4: Float = 0f
            var thursdaySum4: Float = 0f
            var fridaySum4: Float = 0f
            var saturdaySum4: Float = 0f
            var sundaySum4: Float = 0f
            var totalSum4: Float = 0f

            //3??? ????????? ??? ?????????
            val monDay3 = end.with(DayOfWeek.MONDAY) //???
            val tuesDay3 = end.with(DayOfWeek.TUESDAY) //???
            val wednesDay3 = end.with(DayOfWeek.WEDNESDAY) //???
            val thursDay3 = end.with(DayOfWeek.THURSDAY) //???
            val friDay3 = end.with(DayOfWeek.FRIDAY) //???
            val saturDay3 = end.with(DayOfWeek.SATURDAY) //???
            val sunDay3 = end.with(DayOfWeek.SUNDAY) //???

            var mondays3Value : Float = 0f
            var tuesdays3Value: Float = 0f
            var wednesdays3Value: Float = 0f
            var thursdays3Value: Float = 0f
            var fridays3Value: Float = 0f
            var saturdays3Value: Float = 0f
            var sundays3Value: Float = 0f

            var mondaySum3: Float = 0f
            var tuesdaySum3: Float = 0f
            var wednesdaySum3: Float = 0f
            var thursdaySum3: Float = 0f
            var fridaySum3: Float = 0f
            var saturdaySum3: Float = 0f
            var sundaySum3: Float = 0f
            var totalSum3: Float = 0f

            //2??? ????????? ??? ?????????
            val monDay2 = end.with(DayOfWeek.MONDAY) //???
            val tuesDay2 = end.with(DayOfWeek.TUESDAY) //???
            val wednesDay2 = end.with(DayOfWeek.WEDNESDAY) //???
            val thursDay2 = end.with(DayOfWeek.THURSDAY) //???
            val friDay2 = end.with(DayOfWeek.FRIDAY) //???
            val saturDay2 = end.with(DayOfWeek.SATURDAY) //???
            val sunDay2 = end.with(DayOfWeek.SUNDAY) //???

            var mondays2Value : Float = 0f
            var tuesdays2Value: Float = 0f
            var wednesdays2Value: Float = 0f
            var thursdays2Value: Float = 0f
            var fridays2Value: Float = 0f
            var saturdays2Value: Float = 0f
            var sundays2Value: Float = 0f

            var mondaySum2: Float = 0f
            var tuesdaySum2: Float = 0f
            var wednesdaySum2: Float = 0f
            var thursdaySum2: Float = 0f
            var fridaySum2: Float = 0f
            var saturdaySum2: Float = 0f
            var sundaySum2: Float = 0f
            var totalSum2: Float = 0f

            //1??? ????????? ??? ?????????
            val monDay1 = end.with(DayOfWeek.MONDAY) //???
            val tuesDay1 = end.with(DayOfWeek.TUESDAY) //???
            val wednesDay1 = end.with(DayOfWeek.WEDNESDAY) //???
            val thursDay1 = end.with(DayOfWeek.THURSDAY) //???
            val friDay1 = end.with(DayOfWeek.FRIDAY) //???
            val saturDay1 = end.with(DayOfWeek.SATURDAY) //???
            val sunDay1 = end.with(DayOfWeek.SUNDAY) //???

            var mondays1Value : Float = 0f //??????
            var tuesdays1Value: Float = 0f
            var wednesdays1Value: Float = 0f
            var thursdays1Value: Float = 0f
            var fridays1Value: Float = 0f
            var saturdays1Value: Float = 0f
            var sundays1Value: Float = 0f

            var mondaySum1: Float = 0f
            var tuesdaySum1: Float = 0f
            var wednesdaySum1: Float = 0f
            var thursdaySum1: Float = 0f
            var fridaySum1: Float = 0f
            var saturdaySum1: Float = 0f
            var sundaySum1: Float = 0f
            var totalSum1: Float = 0f

            var totalTime : Float = 0f

            //????????? ?????????
            //5??? ????????? ??? ?????????
            val ends = targetYearMonth.atEndOfMonth().minusMonths(1)

            val monDay5LastMonth = ends.with(DayOfWeek.MONDAY)//???
            val tuesDay5LastMonth = ends.with(DayOfWeek.TUESDAY) //???
            val wednesDay5LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
            val thursDay5LastMonth = ends.with(DayOfWeek.THURSDAY)//???
            val friDay5LastMonth = ends.with(DayOfWeek.FRIDAY) //???
            val saturDay5LastMonth = ends.with(DayOfWeek.SATURDAY)//???
            val sunDay5LastMonth = ends.with(DayOfWeek.SUNDAY) //???



            var mondays5ValueLastMonth : Float = 0f
            var tuesdays5ValueLastMonth: Float = 0f
            var wednesdays5ValueLastMonth: Float = 0f
            var thursdays5ValueLastMonth: Float = 0f
            var fridays5ValueLastMonth: Float = 0f
            var saturdays5ValueLastMonth: Float = 0f
            var sundays5ValueLastMonth: Float = 0f

            var mondaySum5LastMonth: Float = 0f
            var tuesdaySum5LastMonth: Float = 0f
            var wednesdaySum5LastMonth: Float = 0f
            var thursdaySum5LastMonth: Float = 0f
            var fridaySum5LastMonth: Float = 0f
            var saturdaySum5LastMonth: Float = 0f
            var sundaySum5LastMonth: Float = 0f

            var totalSum5LastMonth: Float = 0f

            //4??? ????????? ??? ?????????
            val monDay4LastMonth = ends.with(DayOfWeek.MONDAY)//???
            val tuesDay4LastMonth = ends.with(DayOfWeek.TUESDAY) //???
            val wednesDay4LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
            val thursDay4LastMonth = ends.with(DayOfWeek.THURSDAY) //???
            val friDay4LastMonth = ends.with(DayOfWeek.FRIDAY) //???
            val saturDay4LastMonth = ends.with(DayOfWeek.SATURDAY) //???
            val sunDay4LastMonth = ends.with(DayOfWeek.SUNDAY) //???

            var mondays4ValueLastMonth : Float = 0f
            var tuesdays4ValueLastMonth: Float = 0f
            var wednesdays4ValueLastMonth: Float = 0f
            var thursdays4ValueLastMonth: Float = 0f
            var fridays4ValueLastMonth: Float = 0f
            var saturdays4ValueLastMonth: Float = 0f
            var sundays4ValueLastMonth: Float = 0f

            var mondaySum4LastMonth: Float = 0f
            var tuesdaySum4LastMonth: Float = 0f
            var wednesdaySum4LastMonth: Float = 0f
            var thursdaySum4LastMonth: Float = 0f
            var fridaySum4LastMonth: Float = 0f
            var saturdaySum4LastMonth: Float = 0f
            var sundaySum4LastMonth: Float = 0f

            var totalSum4LastMonth: Float = 0f

            //3??? ????????? ??? ?????????
            val monDay3LastMonth = ends.with(DayOfWeek.MONDAY) //???
            val tuesDay3LastMonth = ends.with(DayOfWeek.TUESDAY) //???
            val wednesDay3LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
            val thursDay3LastMonth = ends.with(DayOfWeek.THURSDAY) //???
            val friDay3LastMonth = ends.with(DayOfWeek.FRIDAY)//???
            val saturDay3LastMonth = ends.with(DayOfWeek.SATURDAY) //???
            val sunDay3LastMonth = ends.with(DayOfWeek.SUNDAY) //???

            var mondays3ValueLastMonth : Float = 0f
            var tuesdays3ValueLastMonth: Float = 0f
            var wednesdays3ValueLastMonth: Float = 0f
            var thursdays3ValueLastMonth: Float = 0f
            var fridays3ValueLastMonth: Float = 0f
            var saturdays3ValueLastMonth: Float = 0f
            var sundays3ValueLastMonth: Float = 0f

            var mondaySum3LastMonth: Float = 0f
            var tuesdaySum3LastMonth: Float = 0f
            var wednesdaySum3LastMonth: Float = 0f
            var thursdaySum3LastMonth: Float = 0f
            var fridaySum3LastMonth: Float = 0f
            var saturdaySum3LastMonth: Float = 0f
            var sundaySum3LastMonth: Float = 0f

            var totalSum3LastMonth: Float = 0f

            //2??? ????????? ??? ?????????
            val monDay2LastMonth = ends.with(DayOfWeek.MONDAY) //???
            val tuesDay2LastMonth = ends.with(DayOfWeek.TUESDAY) //???
            val wednesDay2LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
            val thursDay2LastMonth = ends.with(DayOfWeek.THURSDAY) //???
            val friDay2LastMonth = ends.with(DayOfWeek.FRIDAY)//???
            val saturDay2LastMonth = ends.with(DayOfWeek.SATURDAY) //???
            val sunDay2LastMonth = ends.with(DayOfWeek.SUNDAY) //???

            var mondays2ValueLastMonth : Float = 0f
            var tuesdays2ValueLastMonth: Float = 0f
            var wednesdays2ValueLastMonth: Float = 0f
            var thursdays2ValueLastMonth: Float = 0f
            var fridays2ValueLastMonth: Float = 0f
            var saturdays2ValueLastMonth: Float = 0f
            var sundays2ValueLastMonth: Float = 0f

            var mondaySum2LastMonth: Float = 0f
            var tuesdaySum2LastMonth: Float = 0f
            var wednesdaySum2LastMonth: Float = 0f
            var thursdaySum2LastMonth: Float = 0f
            var fridaySum2LastMonth: Float = 0f
            var saturdaySum2LastMonth: Float = 0f
            var sundaySum2LastMonth: Float = 0f

            var totalSum2LastMonth: Float = 0f

            //1??? ????????? ??? ?????????
            val monDay1LastMonth = ends.with(DayOfWeek.MONDAY) //???
            val tuesDay1LastMonth = ends.with(DayOfWeek.TUESDAY) //???
            val wednesDay1LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
            val thursDay1LastMonth = ends.with(DayOfWeek.THURSDAY) //???
            val friDay1LastMonth = ends.with(DayOfWeek.FRIDAY) //???
            val saturDay1LastMonth = ends.with(DayOfWeek.SATURDAY) //???
            val sunDay1LastMonth = ends.with(DayOfWeek.SUNDAY)//???

            var mondays1ValueLastMonth : Float = 0f
            var tuesdays1ValueLastMonth: Float = 0f
            var wednesdays1ValueLastMonth: Float = 0f
            var thursdays1ValueLastMonth: Float = 0f
            var fridays1ValueLastMonth: Float = 0f
            var saturdays1ValueLastMonth: Float = 0f
            var sundays1ValueLastMonth: Float = 0f

            var mondaySum1LastMonth: Float = 0f
            var tuesdaySum1LastMonth: Float = 0f
            var wednesdaySum1LastMonth: Float = 0f
            var thursdaySum1LastMonth: Float = 0f
            var fridaySum1LastMonth: Float = 0f
            var saturdaySum1LastMonth: Float = 0f
            var sundaySum1LastMonth: Float = 0f

            var totalSum1LastMonth: Float = 0f

            var totalTimeLastMonth : Float = 0f

            //7???
            val cal = Calendar.getInstance()
            cal.get(Calendar.WEEK_OF_MONTH)
            Log.d("asdfadsf",cal.get(Calendar.WEEK_OF_MONTH).toString())//???????????? ????????? ????????? ??? ??? ?????????

            val sizeOfWeek: Int = end.get(ChronoField.ALIGNED_WEEK_OF_MONTH) //?????? ????????? ??????
            Log.d("asdfadsf",sizeOfWeek.toString()) //??????????????? ??? ????????? ????????? !!!!!~~~~!!!!!~~~~~~!!!!

            var its : Int = 0
            Log.d("asdgsdfg",its.toString())
            it.forEachIndexed { index, subject ->
                its = it.size
                val calen : Calendar = Calendar.getInstance()
                //???????????? ????????? ??????
                val dateFormat: DateFormat = SimpleDateFormat("yyyy.MM.dd")

                calen.add(Calendar.DATE,1)
                val serverDateFormat: String = dateFormat.format(subject.timestamp)
                val serverDateFormatPlus1: String = dateFormat.format(subject.timestamp?.day?.minus(1.toLong()))

                Log.d("calendar_day1_ss",serverDateFormat.toString())
                Log.d("calendar_day1_sssPlus",serverDateFormatPlus1.toString())


                for (it in 0..its) {
                    if (monDay5.format(textformatter) == serverDateFormat) {
                        mondays5Value = subject.studytimeCopy.toFloat()
                        mondaySum5 = mondaySum5 + mondays5Value
                        break
                    }else if(monDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        mondays4Value = subject.studytimeCopy.toFloat()
                        mondaySum4 = mondaySum4 + mondays4Value
                        break
                    }else if(monDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        mondays3Value = subject.studytimeCopy.toFloat()
                        mondaySum3 = mondaySum3 + mondays3Value
                        break
                    }else if(monDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        mondays2Value = subject.studytimeCopy.toFloat()
                        mondaySum2 = mondaySum2 + mondays2Value
                        break
                    }else if(monDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        mondays1Value = subject.studytimeCopy.toFloat()
                        mondaySum1 = mondaySum1 + mondays1Value
                        break
                    }
                    else if(monDay5LastMonth.format(textformatter) == serverDateFormat){
                        mondays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        mondaySum5LastMonth = mondaySum5LastMonth + mondays5ValueLastMonth
                        break
                    }else if(monDay4LastMonth.minusWeeks(1).format(textformatter) == serverDateFormat){
                        mondays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        mondaySum4LastMonth = mondaySum4LastMonth + mondays4ValueLastMonth
                        break
                    }else if(monDay3LastMonth.minusWeeks(2).format(textformatter) == serverDateFormat){
                        mondays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        mondaySum3LastMonth = mondaySum3LastMonth + mondays3ValueLastMonth
                        break
                    }else if(monDay2LastMonth.minusWeeks(3).format(textformatter) == serverDateFormat){
                        mondays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        mondaySum2LastMonth = mondaySum2LastMonth + mondays2ValueLastMonth
                        break
                    }else if(monDay1LastMonth.minusWeeks(4).format(textformatter) == serverDateFormat){
                        mondays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        mondaySum1LastMonth = mondaySum1LastMonth + mondays1ValueLastMonth
                        break
                    }
                }


                for (it in 0..its) {
                    if (tuesDay5.format(textformatter) == serverDateFormat) {
                        tuesdays5Value = subject.studytimeCopy.toFloat()
                        tuesdaySum5 = tuesdaySum5 + tuesdays5Value
                        break
                    }else if(tuesDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        tuesdays4Value = subject.studytimeCopy.toFloat()
                        tuesdaySum4 = tuesdaySum4 + tuesdays4Value
                        break
                    }else if(tuesDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        tuesdays3Value = subject.studytimeCopy.toFloat()
                        tuesdaySum3 = tuesdaySum3 + tuesdays3Value
                        break
                    }else if(tuesDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        tuesdays2Value = subject.studytimeCopy.toFloat()
                        tuesdaySum2 = tuesdaySum2 + tuesdays2Value
                        break
                    }else if(tuesDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        tuesdays1Value = subject.studytimeCopy.toFloat()
                        tuesdaySum1 = tuesdaySum1 + tuesdays1Value
                        break
                    }
                    else if(tuesDay5LastMonth.format(textformatter) == serverDateFormat){
                        tuesdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        tuesdaySum5LastMonth = tuesdaySum5LastMonth + tuesdays5ValueLastMonth
                        break
                    } else if(tuesDay4LastMonth.minusWeeks(1).minusWeeks(1).format(textformatter) == serverDateFormat){
                        tuesdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        tuesdaySum4LastMonth = tuesdaySum4LastMonth + tuesdays4ValueLastMonth
                        break
                    }else if(tuesDay3LastMonth.minusWeeks(2).minusWeeks(2).format(textformatter) == serverDateFormat){
                        tuesdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        tuesdaySum3LastMonth = tuesdaySum3LastMonth + tuesdays3ValueLastMonth
                        break
                    }else if(tuesDay2LastMonth.minusWeeks(3).minusWeeks(3).format(textformatter) == serverDateFormat){
                        tuesdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        tuesdaySum2LastMonth = tuesdaySum2LastMonth + tuesdays2ValueLastMonth
                        break
                    }else if(tuesDay1LastMonth.minusWeeks(4).minusWeeks(4).format(textformatter) == serverDateFormat){
                        tuesdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        tuesdaySum1LastMonth = tuesdaySum1LastMonth + tuesdays1ValueLastMonth
                        break
                    }
                }


                for (it in 0..its) {
                    if (wednesDay5.format(textformatter) == serverDateFormat) {
                        wednesdays5Value = subject.studytimeCopy.toFloat()
                        Log.d("??????", wednesdays5Value.toString() + " ???")//21
                        Log.d("??????", subject.name.toString() + " : name")
                        wednesdaySum5 = wednesdaySum5 + wednesdays5Value
                        break
                    }else if(wednesDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        wednesdays4Value = subject.studytimeCopy.toFloat()
                        wednesdaySum4 = wednesdaySum4 + wednesdays4Value
                    }else if(wednesDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        wednesdays3Value = subject.studytimeCopy.toFloat()
                        wednesdaySum3 = wednesdaySum3 + wednesdays3Value
                        break
                    }else if(wednesDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        wednesdays2Value = subject.studytimeCopy.toFloat()
                        wednesdaySum2 = wednesdaySum2 + wednesdays2Value
                        break
                    }else if(wednesDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        wednesdays1Value = subject.studytimeCopy.toFloat()
                        wednesdaySum1 = wednesdaySum1 + wednesdays1Value
                        break
                    }
                    else if(wednesDay5LastMonth.format(textformatter) == serverDateFormat){
                        wednesdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        wednesdaySum5LastMonth = wednesdaySum5LastMonth + wednesdays5ValueLastMonth
                        break
                    }else if(wednesDay4LastMonth.minusWeeks(1).format(textformatter) == serverDateFormat){
                        wednesdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        wednesdaySum4LastMonth = wednesdaySum4LastMonth + wednesdays4ValueLastMonth
                        break
                    }else if(wednesDay3LastMonth.minusWeeks(2).format(textformatter) == serverDateFormat){
                        wednesdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        wednesdaySum3LastMonth = wednesdaySum3LastMonth + wednesdays3ValueLastMonth
                        break
                    }else if(wednesDay2LastMonth.minusWeeks(3).format(textformatter) == serverDateFormat){
                        wednesdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        wednesdaySum2LastMonth = wednesdaySum2LastMonth + wednesdays2ValueLastMonth
                        break
                    }else if(wednesDay1LastMonth.minusWeeks(4).format(textformatter) == serverDateFormat){
                        wednesdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        wednesdaySum1LastMonth = wednesdaySum1LastMonth + wednesdays1ValueLastMonth
                        break
                    }
                }

                for (it in 0..its) {
                    if (thursDay5.format(textformatter) == serverDateFormat) {
                        thursdays5Value = subject.studytimeCopy.toFloat()
                        Log.d("??????", thursdays5Value.toString() + " ???")//22
                        thursdaySum5 = thursdaySum5 + thursdays5Value
                        break
                    }else if(thursDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        thursdays4Value = subject.studytimeCopy.toFloat()
                        thursdaySum4 = thursdaySum4 + thursdays4Value
                        break
                    }else if(thursDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        thursdays3Value = subject.studytimeCopy.toFloat()
                        thursdaySum3 = thursdaySum3 + thursdays3Value
                        break
                    }else if(thursDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        thursdays2Value = subject.studytimeCopy.toFloat()
                        thursdaySum2 = thursdaySum2 + thursdays2Value
                        break
                    }else if(thursDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        thursdays1Value = subject.studytimeCopy.toFloat()
                        thursdaySum1 = thursdaySum1 + thursdays1Value
                        break
                    }
                    else if(thursDay5LastMonth.format(textformatter) == serverDateFormat){
                        thursdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        thursdaySum5LastMonth = thursdaySum5LastMonth + thursdays5ValueLastMonth
                        break
                    }else if(thursDay4LastMonth.minusWeeks(1).format(textformatter) == serverDateFormat){
                        thursdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        thursdaySum4LastMonth = thursdaySum4LastMonth + thursdays4ValueLastMonth
                        break
                    }else if(thursDay3LastMonth.minusWeeks(2).format(textformatter) == serverDateFormat){
                        thursdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        thursdaySum3LastMonth = thursdaySum3LastMonth + thursdays3ValueLastMonth
                        break
                    }else if(thursDay2LastMonth.minusWeeks(3).format(textformatter) == serverDateFormat){
                        thursdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        thursdaySum2LastMonth = thursdaySum2LastMonth + thursdays2ValueLastMonth
                        break
                    }else if(thursDay1LastMonth.minusWeeks(4).format(textformatter) == serverDateFormat){
                        thursdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        thursdaySum1LastMonth = thursdaySum1LastMonth + thursdays1ValueLastMonth
                        break
                    }
                }

                for (it in 0..its) {
                    if (friDay5.format(textformatter) == serverDateFormat) {
                        fridays5Value = subject.studytimeCopy.toFloat()
                        Log.d("??????", fridays5Value.toString() + " ???")//23
                        fridaySum5 = fridaySum5 + fridays5Value
                        break
                    }else if(friDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        fridays4Value = subject.studytimeCopy.toFloat()
                        fridaySum4 = fridaySum4 + fridays4Value
                        break
                    }else if(friDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        fridays3Value = subject.studytimeCopy.toFloat()
                        fridaySum3 = fridaySum3 + fridays3Value
                        break
                    }else if(friDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        fridays2Value = subject.studytimeCopy.toFloat()
                        fridaySum2 = fridaySum2 + fridays2Value
                        break
                    }else if(friDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        fridays1Value = subject.studytimeCopy.toFloat()
                        fridaySum1 = fridaySum1 + fridays1Value
                        break
                    }
                    else if(friDay5LastMonth.format(textformatter) == serverDateFormat){
                        fridays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        fridaySum5LastMonth = fridaySum5LastMonth + fridays5ValueLastMonth
                        break
                    } else if(friDay4LastMonth.minusWeeks(1).format(textformatter) == serverDateFormat){
                        fridays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        fridaySum4LastMonth = fridaySum4LastMonth + fridays4ValueLastMonth
                        break
                    }else if(friDay3LastMonth.minusWeeks(2).format(textformatter) == serverDateFormat){
                        fridays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        fridaySum3LastMonth = fridaySum3LastMonth + fridays3ValueLastMonth
                        break
                    }else if(friDay2LastMonth.minusWeeks(3).format(textformatter) == serverDateFormat){
                        fridays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        fridaySum2LastMonth = fridaySum2LastMonth + fridays2ValueLastMonth
                        break
                    }else if(friDay1LastMonth.minusWeeks(4).format(textformatter) == serverDateFormat){
                        fridays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        fridaySum1LastMonth = fridaySum1LastMonth + fridays1ValueLastMonth
                        break
                    }
                }

                for (it in 0..its) {
                    if (saturDay5.format(textformatter)  == serverDateFormat) {
                        saturdays5Value = subject.studytimeCopy.toFloat()
                        Log.d("??????", saturdays5Value.toString() + " ???")//24
                        Log.d("??????", subject.name.toString() + " : name")
                        saturdaySum5 = saturdaySum5 + saturdays5Value
                        break
                    }else if(saturDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        saturdays4Value = subject.studytimeCopy.toFloat()
                        saturdaySum4 = saturdaySum4 + saturdays4Value
                        break
                    }else if(saturDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        saturdays3Value = subject.studytimeCopy.toFloat()
                        saturdaySum3 = saturdaySum3 + saturdays3Value
                        break
                    }else if(saturDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        saturdays2Value = subject.studytimeCopy.toFloat()
                        saturdaySum2 = saturdaySum2 + saturdays2Value
                        break
                    }else if(saturDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        saturdays1Value = subject.studytimeCopy.toFloat()
                        saturdaySum1 = saturdaySum1 + saturdays1Value
                        break
                    }
                    else if(saturDay5LastMonth.format(textformatter) == serverDateFormat){
                        saturdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        saturdaySum5LastMonth = saturdaySum5LastMonth + saturdays5ValueLastMonth
                        break
                    } else if(saturDay4LastMonth.minusWeeks(1).format(textformatter) == serverDateFormat){
                        saturdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        saturdaySum5LastMonth = saturdaySum4LastMonth + saturdays4ValueLastMonth
                        break
                    }else if(saturDay3LastMonth.minusWeeks(2).format(textformatter) == serverDateFormat){
                        saturdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        saturdaySum3LastMonth = saturdaySum3LastMonth + saturdays3ValueLastMonth
                        break
                    }else if(saturDay2LastMonth.minusWeeks(3).format(textformatter) == serverDateFormat){
                        saturdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        saturdaySum2LastMonth = saturdaySum2LastMonth + saturdays2ValueLastMonth
                        break
                    }else if(saturDay1LastMonth.minusWeeks(4).format(textformatter) == serverDateFormat){
                        saturdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        saturdaySum1LastMonth = saturdaySum1LastMonth + saturdays1ValueLastMonth
                        break
                    }
                }

                for (it in 0..its) {
                    if (sunDay5.format(textformatter) == serverDateFormat) {
                        sundays5Value = subject.studytimeCopy.toFloat()
                        Log.d("??????", sundays5Value.toString() + " --???--")//25
                        Log.d("??????", subject.name.toString() + " : name")
                        sundaySum5 = sundaySum5 + sundays5Value
                        break
                    }else if(sunDay4.minusWeeks(1).format(textformatter) == serverDateFormat){
                        sundays4Value = subject.studytimeCopy.toFloat()
                        sundaySum4 = sundaySum4 + sundays4Value
                        break
                    }else if(sunDay3.minusWeeks(2).format(textformatter) == serverDateFormat){
                        sundays3Value = subject.studytimeCopy.toFloat()
                        sundaySum3 = sundaySum3 + sundays3Value
                        break
                    }else if(sunDay2.minusWeeks(3).format(textformatter) == serverDateFormat){
                        sundays2Value = subject.studytimeCopy.toFloat()
                        sundaySum2 = sundaySum2 + sundays2Value
                        break
                    }else if(sunDay1.minusWeeks(4).format(textformatter) == serverDateFormat){
                        Log.d("totalSumMay", sunDay1.minusWeeks(4).format(textformatter).toString() ) //25
                        sundays1Value = subject.studytimeCopy.toFloat()
                        sundaySum1 = sundaySum1 + sundays1Value
                        break
                    }
                    else if(sunDay5LastMonth.format(textformatter) == serverDateFormat){
                        sundays5ValueLastMonth = subject.studytimeCopy.toFloat()
                        sundaySum5LastMonth = sundaySum5LastMonth + sundays5ValueLastMonth
                        break
                    }else if(sunDay4LastMonth.minusWeeks(1).format(textformatter) == serverDateFormat){
                        sundays4ValueLastMonth = subject.studytimeCopy.toFloat()
                        sundaySum4LastMonth = sundaySum4LastMonth + sundays4ValueLastMonth
                        break
                    }else if(sunDay3LastMonth.minusWeeks(2).format(textformatter) == serverDateFormat){
                        sundays3ValueLastMonth = subject.studytimeCopy.toFloat()
                        sundaySum3LastMonth = sundaySum3LastMonth + sundays3ValueLastMonth
                        break
                    }else if(sunDay2LastMonth.minusWeeks(3).format(textformatter) == serverDateFormat){
                        sundays2ValueLastMonth = subject.studytimeCopy.toFloat()
                        sundaySum2LastMonth = sundaySum2LastMonth + sundays2ValueLastMonth
                        break
                    }else if(sunDay1LastMonth.minusWeeks(4).format(textformatter) == serverDateFormat){
                        sundays1ValueLastMonth = subject.studytimeCopy.toFloat()
                        sundaySum1LastMonth = sundaySum1LastMonth + sundays1ValueLastMonth
                        break
                    }
                }
            }

            totalSum5 = mondaySum5 + tuesdaySum5 + wednesdaySum5 + thursdaySum5 + fridaySum5 + saturdaySum5 + sundaySum5
            totalSum4 = mondaySum4 + tuesdaySum4 + wednesdaySum4 + thursdaySum4 + fridaySum4 + saturdaySum4 + sundaySum4
            totalSum3 = mondaySum3 + tuesdaySum3 + wednesdaySum3 + thursdaySum3 + fridaySum3 + saturdaySum3 + sundaySum3
            totalSum2 = mondaySum2 + tuesdaySum2 + wednesdaySum2 + thursdaySum2 + fridaySum2 + saturdaySum2 + sundaySum2
            totalSum1 = mondaySum1 + tuesdaySum1 + wednesdaySum1 + thursdaySum1 + fridaySum1 + saturdaySum1 + sundaySum1
            totalTime = totalSum5 + totalSum4 + totalSum3 + totalSum2 + totalSum1

            //????????? ?????? ?????? ??????
            totalSum5LastMonth = mondaySum5LastMonth + tuesdaySum5LastMonth + wednesdaySum5LastMonth + thursdaySum5LastMonth + fridaySum5LastMonth + saturdaySum5LastMonth + sundaySum5LastMonth
            totalSum4LastMonth = mondaySum4LastMonth + tuesdaySum4LastMonth + wednesdaySum4LastMonth + thursdaySum4LastMonth + fridaySum4LastMonth + saturdaySum4LastMonth + sundaySum4LastMonth
            totalSum3LastMonth = mondaySum3LastMonth + tuesdaySum3LastMonth + wednesdaySum3LastMonth + thursdaySum3LastMonth + fridaySum3LastMonth + saturdaySum3LastMonth + sundaySum3LastMonth
            totalSum2LastMonth = mondaySum2LastMonth + tuesdaySum2LastMonth + wednesdaySum2LastMonth + thursdaySum2LastMonth + fridaySum2LastMonth + saturdaySum2LastMonth + sundaySum2LastMonth
            totalSum1LastMonth = mondaySum1LastMonth + tuesdaySum1LastMonth + wednesdaySum1LastMonth + thursdaySum1LastMonth + fridaySum1LastMonth + saturdaySum1LastMonth + sundaySum1LastMonth
            totalTimeLastMonth = totalSum5LastMonth + totalSum4LastMonth + totalSum3LastMonth + totalSum2LastMonth + totalSum1LastMonth

            binding.monthlyTotalTime.text =  "${(totalTime.toInt()) / 60}?????? ${(totalTime.toInt()) % 60}???"

            //???????????? ?????????
            var compareSum: Int = 0
            //17, 33
            if (totalTime > totalTimeLastMonth) {
                compareSum = totalTime.toInt() - totalTimeLastMonth.toInt() //text
                binding.comapreMonthTimeImage.setVisibility(true)
                val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                lp.setMargins(20, 15, 0, 0)
                binding.comapreMonthTimeImage.setLayoutParams(lp)
                binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_polygon_up)
                binding.comapreMonthTimeText.text  = "${compareSum / 60}??????"
                binding.comapreMonthTimeText.setTextColor(Color.parseColor("#F95849"))
            } else if (totalTime < totalTimeLastMonth) {
                compareSum = totalTimeLastMonth.toInt() - totalTime.toInt()
                 binding.comapreMonthTimeImage.setVisibility(true)
                val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                lp.setMargins(20, 15, 0, 0)
                binding.comapreMonthTimeImage.setLayoutParams(lp)
                binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_polygon_down)
                binding.comapreMonthTimeText.text  = "${compareSum / 60}??????"
                binding.comapreMonthTimeText.setTextColor(Color.parseColor("#0F8CFF"))
            } else {
                binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_linezero)
                val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                lp.setMargins(20, 25, 0, 0)
                binding.comapreMonthTimeImage.setLayoutParams(lp)
                binding.comapreMonthTimeText.text  = "0??????"
                binding.comapreMonthTimeText.setTextColor(Color.parseColor("#80000000"))
            }

            setData(totalSum1, totalSum2, totalSum3, totalSum4, totalSum5)
        }
    }

    private fun moveCalendarByMonth(){
//        calendarMonth:TextView, calRightBtn:ImageButton, calLeftBtn:ImageButton, title:TextView
//        binding.calendarMonth, binding.calRightBtn, binding.calLeftBtn, binding.titleMonth
        // ?????? ??????/?????? ????????????
        val dateNow: LocalDate = LocalDate.now()
        val textformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM")
        val titleformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("M?????????")

        var count : Int = 0
        var monthCount : Int = 0
        binding.calendarMonth.text = dateNow.format(textformatter) //?????? 2021.07.08

        dateNow.plusDays(count.toLong()) //??????????????? ???????????? ??? ?????? ????????? ?????? ??????
        binding.calRightBtn.setOnClickListener {
            count++
            if(count == 1){
                binding.calRightBtn.isEnabled = false
            }else {
                val dayPlus: LocalDate = dateNow.plusMonths(count.toLong())
                binding.calendarMonth.text = dayPlus.format(textformatter).toString()
                if (count == 0) {
                    binding.titleMonth.text = "?????? ??????"
                } else if (count == -1) {
                    binding.calRightBtn.isEnabled = true
                    binding.titleMonth.text = "?????? ??????"
                } else {
                    binding.titleMonth.text = dayPlus.format(titleformatter).toString()
                }
            }
            monthCount++
            //----------------------------------- monthCount++
            if (monthCount <= 0) {
                viewModel.list.observe(viewLifecycleOwner) {
                    val textformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                    //??????
                    val dateNow: LocalDateTime = LocalDateTime.now()//------------------monthCount
                    val dateNowFormat: String = dateNow.format(textformatter)
                    Log.d("dafgsdfh", dateNowFormat)
                    //?????? ?????? ????????? ?????? ??????
                    val targetYearMonth: YearMonth = YearMonth.from(
                        LocalDate.parse(
                            dateNowFormat,
                            DateTimeFormatter.ofPattern("yyyy.MM.dd")
                        )
                    )
                    val end = targetYearMonth.atEndOfMonth()
                        .plusMonths(monthCount.toLong())////------------------monthCount
                    //??????????????? ??? ????????? ?????????
//                val sizeOfWeeks :Int = end.get(ChronoField.ALIGNED_WEEK_OF_MONTH) //?????? ????????? ??????
//                Log.d("asdfadsfSize",sizeOfWeek.toString())
//                val weekOfMonthSize : Int = weekSize-1
//                Log.d("asdfadsfSize-Minus",weekOfMonthSize.toString())

                    //5??? ????????? ??? ?????????
                    val monDay5 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay5 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay5 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay5 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay5 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay5 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay5 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays5Value: Float = 0f
                    var tuesdays5Value: Float = 0f
                    var wednesdays5Value: Float = 0f
                    var thursdays5Value: Float = 0f
                    var fridays5Value: Float = 0f
                    var saturdays5Value: Float = 0f
                    var sundays5Value: Float = 0f

                    var mondaySum5: Float = 0f
                    var tuesdaySum5: Float = 0f
                    var wednesdaySum5: Float = 0f
                    var thursdaySum5: Float = 0f
                    var fridaySum5: Float = 0f
                    var saturdaySum5: Float = 0f
                    var sundaySum5: Float = 0f
                    var totalSum5: Float = 0f

                    //4??? ????????? ??? ?????????
                    val monDay4 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay4 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay4 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay4 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay4 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay4 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay4 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays4Value: Float = 0f
                    var tuesdays4Value: Float = 0f
                    var wednesdays4Value: Float = 0f
                    var thursdays4Value: Float = 0f
                    var fridays4Value: Float = 0f
                    var saturdays4Value: Float = 0f
                    var sundays4Value: Float = 0f

                    var mondaySum4: Float = 0f
                    var tuesdaySum4: Float = 0f
                    var wednesdaySum4: Float = 0f
                    var thursdaySum4: Float = 0f
                    var fridaySum4: Float = 0f
                    var saturdaySum4: Float = 0f
                    var sundaySum4: Float = 0f
                    var totalSum4: Float = 0f

                    //3??? ????????? ??? ?????????
                    val monDay3 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay3 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay3 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay3 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay3 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay3 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay3 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays3Value: Float = 0f
                    var tuesdays3Value: Float = 0f
                    var wednesdays3Value: Float = 0f
                    var thursdays3Value: Float = 0f
                    var fridays3Value: Float = 0f
                    var saturdays3Value: Float = 0f
                    var sundays3Value: Float = 0f

                    var mondaySum3: Float = 0f
                    var tuesdaySum3: Float = 0f
                    var wednesdaySum3: Float = 0f
                    var thursdaySum3: Float = 0f
                    var fridaySum3: Float = 0f
                    var saturdaySum3: Float = 0f
                    var sundaySum3: Float = 0f
                    var totalSum3: Float = 0f

                    //2??? ????????? ??? ?????????
                    val monDay2 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay2 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay2 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay2 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay2 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay2 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay2 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays2Value: Float = 0f
                    var tuesdays2Value: Float = 0f
                    var wednesdays2Value: Float = 0f
                    var thursdays2Value: Float = 0f
                    var fridays2Value: Float = 0f
                    var saturdays2Value: Float = 0f
                    var sundays2Value: Float = 0f

                    var mondaySum2: Float = 0f
                    var tuesdaySum2: Float = 0f
                    var wednesdaySum2: Float = 0f
                    var thursdaySum2: Float = 0f
                    var fridaySum2: Float = 0f
                    var saturdaySum2: Float = 0f
                    var sundaySum2: Float = 0f
                    var totalSum2: Float = 0f

                    //1??? ????????? ??? ?????????
                    val monDay1 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay1 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay1 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay1 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay1 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay1 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay1 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays1Value: Float = 0f //??????
                    var tuesdays1Value: Float = 0f
                    var wednesdays1Value: Float = 0f
                    var thursdays1Value: Float = 0f
                    var fridays1Value: Float = 0f
                    var saturdays1Value: Float = 0f
                    var sundays1Value: Float = 0f

                    var mondaySum1: Float = 0f
                    var tuesdaySum1: Float = 0f
                    var wednesdaySum1: Float = 0f
                    var thursdaySum1: Float = 0f
                    var fridaySum1: Float = 0f
                    var saturdaySum1: Float = 0f
                    var sundaySum1: Float = 0f
                    var totalSum1: Float = 0f

                    var totalTime: Float = 0f

                    //????????? ?????????
                    //5??? ????????? ??? ?????????
                    val ends = targetYearMonth.atEndOfMonth().minusMonths(1)
                        .plusMonths(monthCount.toLong())
                    Log.d("asdfsdgf", ends.toString())

                    val monDay5LastMonth = ends.with(DayOfWeek.MONDAY)//???
                    Log.d("asdfsdgf", monDay5LastMonth.toString())
                    val tuesDay5LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay5LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay5LastMonth = ends.with(DayOfWeek.THURSDAY)//???
                    val friDay5LastMonth = ends.with(DayOfWeek.FRIDAY) //???
                    val saturDay5LastMonth = ends.with(DayOfWeek.SATURDAY)//???
                    val sunDay5LastMonth = ends.with(DayOfWeek.SUNDAY) //???
                    Log.d("asdfsdgf", sunDay5LastMonth.toString())


                    var mondays5ValueLastMonth: Float = 0f
                    var tuesdays5ValueLastMonth: Float = 0f
                    var wednesdays5ValueLastMonth: Float = 0f
                    var thursdays5ValueLastMonth: Float = 0f
                    var fridays5ValueLastMonth: Float = 0f
                    var saturdays5ValueLastMonth: Float = 0f
                    var sundays5ValueLastMonth: Float = 0f

                    var mondaySum5LastMonth: Float = 0f
                    var tuesdaySum5LastMonth: Float = 0f
                    var wednesdaySum5LastMonth: Float = 0f
                    var thursdaySum5LastMonth: Float = 0f
                    var fridaySum5LastMonth: Float = 0f
                    var saturdaySum5LastMonth: Float = 0f
                    var sundaySum5LastMonth: Float = 0f

                    var totalSum5LastMonth: Float = 0f

                    //4??? ????????? ??? ?????????
                    val monDay4LastMonth = ends.with(DayOfWeek.MONDAY)//???
                    val tuesDay4LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay4LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay4LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay4LastMonth = ends.with(DayOfWeek.FRIDAY) //???
                    val saturDay4LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay4LastMonth = ends.with(DayOfWeek.SUNDAY) //???

                    var mondays4ValueLastMonth: Float = 0f
                    var tuesdays4ValueLastMonth: Float = 0f
                    var wednesdays4ValueLastMonth: Float = 0f
                    var thursdays4ValueLastMonth: Float = 0f
                    var fridays4ValueLastMonth: Float = 0f
                    var saturdays4ValueLastMonth: Float = 0f
                    var sundays4ValueLastMonth: Float = 0f

                    var mondaySum4LastMonth: Float = 0f
                    var tuesdaySum4LastMonth: Float = 0f
                    var wednesdaySum4LastMonth: Float = 0f
                    var thursdaySum4LastMonth: Float = 0f
                    var fridaySum4LastMonth: Float = 0f
                    var saturdaySum4LastMonth: Float = 0f
                    var sundaySum4LastMonth: Float = 0f

                    var totalSum4LastMonth: Float = 0f

                    //3??? ????????? ??? ?????????
                    val monDay3LastMonth = ends.with(DayOfWeek.MONDAY) //???
                    val tuesDay3LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay3LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay3LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay3LastMonth = ends.with(DayOfWeek.FRIDAY)//???
                    val saturDay3LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay3LastMonth = ends.with(DayOfWeek.SUNDAY) //???

                    var mondays3ValueLastMonth: Float = 0f
                    var tuesdays3ValueLastMonth: Float = 0f
                    var wednesdays3ValueLastMonth: Float = 0f
                    var thursdays3ValueLastMonth: Float = 0f
                    var fridays3ValueLastMonth: Float = 0f
                    var saturdays3ValueLastMonth: Float = 0f
                    var sundays3ValueLastMonth: Float = 0f

                    var mondaySum3LastMonth: Float = 0f
                    var tuesdaySum3LastMonth: Float = 0f
                    var wednesdaySum3LastMonth: Float = 0f
                    var thursdaySum3LastMonth: Float = 0f
                    var fridaySum3LastMonth: Float = 0f
                    var saturdaySum3LastMonth: Float = 0f
                    var sundaySum3LastMonth: Float = 0f

                    var totalSum3LastMonth: Float = 0f

                    //2??? ????????? ??? ?????????
                    val monDay2LastMonth = ends.with(DayOfWeek.MONDAY) //???
                    val tuesDay2LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay2LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay2LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay2LastMonth = ends.with(DayOfWeek.FRIDAY)//???
                    val saturDay2LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay2LastMonth = ends.with(DayOfWeek.SUNDAY) //???

                    var mondays2ValueLastMonth: Float = 0f
                    var tuesdays2ValueLastMonth: Float = 0f
                    var wednesdays2ValueLastMonth: Float = 0f
                    var thursdays2ValueLastMonth: Float = 0f
                    var fridays2ValueLastMonth: Float = 0f
                    var saturdays2ValueLastMonth: Float = 0f
                    var sundays2ValueLastMonth: Float = 0f

                    var mondaySum2LastMonth: Float = 0f
                    var tuesdaySum2LastMonth: Float = 0f
                    var wednesdaySum2LastMonth: Float = 0f
                    var thursdaySum2LastMonth: Float = 0f
                    var fridaySum2LastMonth: Float = 0f
                    var saturdaySum2LastMonth: Float = 0f
                    var sundaySum2LastMonth: Float = 0f

                    var totalSum2LastMonth: Float = 0f

                    //1??? ????????? ??? ?????????
                    val monDay1LastMonth = ends.with(DayOfWeek.MONDAY) //???
                    Log.d("asdfsdgf", monDay1LastMonth.toString())
                    val tuesDay1LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay1LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay1LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay1LastMonth = ends.with(DayOfWeek.FRIDAY) //???
                    val saturDay1LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay1LastMonth = ends.with(DayOfWeek.SUNDAY)//???
                    Log.d("asdfsdgf", sunDay1LastMonth.toString())

                    var mondays1ValueLastMonth: Float = 0f
                    var tuesdays1ValueLastMonth: Float = 0f
                    var wednesdays1ValueLastMonth: Float = 0f
                    var thursdays1ValueLastMonth: Float = 0f
                    var fridays1ValueLastMonth: Float = 0f
                    var saturdays1ValueLastMonth: Float = 0f
                    var sundays1ValueLastMonth: Float = 0f

                    var mondaySum1LastMonth: Float = 0f
                    var tuesdaySum1LastMonth: Float = 0f
                    var wednesdaySum1LastMonth: Float = 0f
                    var thursdaySum1LastMonth: Float = 0f
                    var fridaySum1LastMonth: Float = 0f
                    var saturdaySum1LastMonth: Float = 0f
                    var sundaySum1LastMonth: Float = 0f

                    var totalSum1LastMonth: Float = 0f

                    var totalTimeLastMonth: Float = 0f

                    //7???
                    val cal = Calendar.getInstance()
                    cal.get(Calendar.WEEK_OF_MONTH)
                    Log.d(
                        "asdfadsf",
                        cal.get(Calendar.WEEK_OF_MONTH).toString()
                    )//???????????? ????????? ????????? ??? ??? ?????????

                    val sizeOfWeek: Int = end.get(ChronoField.ALIGNED_WEEK_OF_MONTH) //?????? ????????? ??????
                    Log.d(
                        "asdfadsf",
                        sizeOfWeek.toString()
                    ) //??????????????? ??? ????????? ????????? !!!!!~~~~!!!!!~~~~~~!!!!

                    var its: Int = 0
                    Log.d("asdgsdfg", its.toString())
                    it.forEachIndexed { index, subject ->
                        its = it.size
                        val calen: Calendar = Calendar.getInstance()
                        //???????????? ????????? ??????
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy.MM.dd")

                        calen.add(Calendar.DATE, 1)
                        val serverDateFormat: String = dateFormat.format(subject.timestamp)
                        val serverDateFormatPlus1: String =
                            dateFormat.format(subject.timestamp?.day?.minus(1.toLong()))

                        Log.d("calendar_day1_ss", serverDateFormat.toString())
                        Log.d("calendar_day1_sssPlus", serverDateFormatPlus1.toString())


                        for (it in 0..its) {
                            if (monDay5.format(textformatter) == serverDateFormat) {
                                mondays5Value = subject.studytimeCopy.toFloat()
                                mondaySum5 = mondaySum5 + mondays5Value
                                break
                            } else if (monDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays4Value = subject.studytimeCopy.toFloat()
                                mondaySum4 = mondaySum4 + mondays4Value
                                break
                            } else if (monDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays3Value = subject.studytimeCopy.toFloat()
                                mondaySum3 = mondaySum3 + mondays3Value
                                break
                            } else if (monDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays2Value = subject.studytimeCopy.toFloat()
                                mondaySum2 = mondaySum2 + mondays2Value
                                break
                            } else if (monDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays1Value = subject.studytimeCopy.toFloat()
                                mondaySum1 = mondaySum1 + mondays1Value
                                break
                            } else if (monDay5LastMonth.format(textformatter) == serverDateFormat) {
                                mondays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum5LastMonth = mondaySum5LastMonth + mondays5ValueLastMonth
                                break
                            } else if (monDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum4LastMonth = mondaySum4LastMonth + mondays4ValueLastMonth
                                break
                            } else if (monDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum3LastMonth = mondaySum3LastMonth + mondays3ValueLastMonth
                                break
                            } else if (monDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum2LastMonth = mondaySum2LastMonth + mondays2ValueLastMonth
                                break
                            } else if (monDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum1LastMonth = mondaySum1LastMonth + mondays1ValueLastMonth
                                break
                            }
                        }


                        for (it in 0..its) {
                            if (tuesDay5.format(textformatter) == serverDateFormat) {
                                tuesdays5Value = subject.studytimeCopy.toFloat()
                                tuesdaySum5 = tuesdaySum5 + tuesdays5Value
                                break
                            } else if (tuesDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays4Value = subject.studytimeCopy.toFloat()
                                tuesdaySum4 = tuesdaySum4 + tuesdays4Value
                                break
                            } else if (tuesDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays3Value = subject.studytimeCopy.toFloat()
                                tuesdaySum3 = tuesdaySum3 + tuesdays3Value
                                break
                            } else if (tuesDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays2Value = subject.studytimeCopy.toFloat()
                                tuesdaySum2 = tuesdaySum2 + tuesdays2Value
                                break
                            } else if (tuesDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays1Value = subject.studytimeCopy.toFloat()
                                tuesdaySum1 = tuesdaySum1 + tuesdays1Value
                                break
                            } else if (tuesDay5LastMonth.format(textformatter) == serverDateFormat) {
                                tuesdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum5LastMonth =
                                    tuesdaySum5LastMonth + tuesdays5ValueLastMonth
                                break
                            } else if (tuesDay4LastMonth.minusWeeks(1).minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum4LastMonth =
                                    tuesdaySum4LastMonth + tuesdays4ValueLastMonth
                                break
                            } else if (tuesDay3LastMonth.minusWeeks(2).minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum3LastMonth =
                                    tuesdaySum3LastMonth + tuesdays3ValueLastMonth
                                break
                            } else if (tuesDay2LastMonth.minusWeeks(3).minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum2LastMonth =
                                    tuesdaySum2LastMonth + tuesdays2ValueLastMonth
                                break
                            } else if (tuesDay1LastMonth.minusWeeks(4).minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum1LastMonth =
                                    tuesdaySum1LastMonth + tuesdays1ValueLastMonth
                                break
                            }
                        }


                        for (it in 0..its) {
                            if (wednesDay5.format(textformatter) == serverDateFormat) {
                                wednesdays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", wednesdays5Value.toString() + " ???")//21
                                Log.d("??????", subject.name.toString() + " : name")
                                wednesdaySum5 = wednesdaySum5 + wednesdays5Value
                                break
                            } else if (wednesDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays4Value = subject.studytimeCopy.toFloat()
                                wednesdaySum4 = wednesdaySum4 + wednesdays4Value
                            } else if (wednesDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays3Value = subject.studytimeCopy.toFloat()
                                wednesdaySum3 = wednesdaySum3 + wednesdays3Value
                                break
                            } else if (wednesDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays2Value = subject.studytimeCopy.toFloat()
                                wednesdaySum2 = wednesdaySum2 + wednesdays2Value
                                break
                            } else if (wednesDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays1Value = subject.studytimeCopy.toFloat()
                                wednesdaySum1 = wednesdaySum1 + wednesdays1Value
                                break
                            } else if (wednesDay5LastMonth.format(textformatter) == serverDateFormat) {
                                wednesdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum5LastMonth =
                                    wednesdaySum5LastMonth + wednesdays5ValueLastMonth
                                break
                            } else if (wednesDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum4LastMonth =
                                    wednesdaySum4LastMonth + wednesdays4ValueLastMonth
                                break
                            } else if (wednesDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum3LastMonth =
                                    wednesdaySum3LastMonth + wednesdays3ValueLastMonth
                                break
                            } else if (wednesDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum2LastMonth =
                                    wednesdaySum2LastMonth + wednesdays2ValueLastMonth
                                break
                            } else if (wednesDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum1LastMonth =
                                    wednesdaySum1LastMonth + wednesdays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (thursDay5.format(textformatter) == serverDateFormat) {
                                thursdays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", thursdays5Value.toString() + " ???")//22
                                thursdaySum5 = thursdaySum5 + thursdays5Value
                                break
                            } else if (thursDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays4Value = subject.studytimeCopy.toFloat()
                                thursdaySum4 = thursdaySum4 + thursdays4Value
                                break
                            } else if (thursDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays3Value = subject.studytimeCopy.toFloat()
                                thursdaySum3 = thursdaySum3 + thursdays3Value
                                break
                            } else if (thursDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays2Value = subject.studytimeCopy.toFloat()
                                thursdaySum2 = thursdaySum2 + thursdays2Value
                                break
                            } else if (thursDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays1Value = subject.studytimeCopy.toFloat()
                                thursdaySum1 = thursdaySum1 + thursdays1Value
                                break
                            } else if (thursDay5LastMonth.format(textformatter) == serverDateFormat) {
                                thursdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum5LastMonth =
                                    thursdaySum5LastMonth + thursdays5ValueLastMonth
                                break
                            } else if (thursDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum4LastMonth =
                                    thursdaySum4LastMonth + thursdays4ValueLastMonth
                                break
                            } else if (thursDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum3LastMonth =
                                    thursdaySum3LastMonth + thursdays3ValueLastMonth
                                break
                            } else if (thursDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum2LastMonth =
                                    thursdaySum2LastMonth + thursdays2ValueLastMonth
                                break
                            } else if (thursDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum1LastMonth =
                                    thursdaySum1LastMonth + thursdays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (friDay5.format(textformatter) == serverDateFormat) {
                                fridays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", fridays5Value.toString() + " ???")//23
                                fridaySum5 = fridaySum5 + fridays5Value
                                break
                            } else if (friDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays4Value = subject.studytimeCopy.toFloat()
                                fridaySum4 = fridaySum4 + fridays4Value
                                break
                            } else if (friDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays3Value = subject.studytimeCopy.toFloat()
                                fridaySum3 = fridaySum3 + fridays3Value
                                break
                            } else if (friDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays2Value = subject.studytimeCopy.toFloat()
                                fridaySum2 = fridaySum2 + fridays2Value
                                break
                            } else if (friDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays1Value = subject.studytimeCopy.toFloat()
                                fridaySum1 = fridaySum1 + fridays1Value
                                break
                            } else if (friDay5LastMonth.format(textformatter) == serverDateFormat) {
                                fridays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum5LastMonth = fridaySum5LastMonth + fridays5ValueLastMonth
                                break
                            } else if (friDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum4LastMonth = fridaySum4LastMonth + fridays4ValueLastMonth
                                break
                            } else if (friDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum3LastMonth = fridaySum3LastMonth + fridays3ValueLastMonth
                                break
                            } else if (friDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum2LastMonth = fridaySum2LastMonth + fridays2ValueLastMonth
                                break
                            } else if (friDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum1LastMonth = fridaySum1LastMonth + fridays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (saturDay5.format(textformatter) == serverDateFormat) {
                                saturdays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", saturdays5Value.toString() + " ???")//24
                                Log.d("??????", subject.name.toString() + " : name")
                                saturdaySum5 = saturdaySum5 + saturdays5Value
                                break
                            } else if (saturDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays4Value = subject.studytimeCopy.toFloat()
                                saturdaySum4 = saturdaySum4 + saturdays4Value
                                break
                            } else if (saturDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays3Value = subject.studytimeCopy.toFloat()
                                saturdaySum3 = saturdaySum3 + saturdays3Value
                                break
                            } else if (saturDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays2Value = subject.studytimeCopy.toFloat()
                                saturdaySum2 = saturdaySum2 + saturdays2Value
                                break
                            } else if (saturDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays1Value = subject.studytimeCopy.toFloat()
                                saturdaySum1 = saturdaySum1 + saturdays1Value
                                break
                            } else if (saturDay5LastMonth.format(textformatter) == serverDateFormat) {
                                saturdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum5LastMonth =
                                    saturdaySum5LastMonth + saturdays5ValueLastMonth
                                break
                            } else if (saturDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum5LastMonth =
                                    saturdaySum4LastMonth + saturdays4ValueLastMonth
                                break
                            } else if (saturDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum3LastMonth =
                                    saturdaySum3LastMonth + saturdays3ValueLastMonth
                                break
                            } else if (saturDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum2LastMonth =
                                    saturdaySum2LastMonth + saturdays2ValueLastMonth
                                break
                            } else if (saturDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum1LastMonth =
                                    saturdaySum1LastMonth + saturdays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (sunDay5.format(textformatter) == serverDateFormat) {
                                sundays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", sundays5Value.toString() + " --???--")//25
                                Log.d("??????", subject.name.toString() + " : name")
                                sundaySum5 = sundaySum5 + sundays5Value
                                break
                            } else if (sunDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays4Value = subject.studytimeCopy.toFloat()
                                sundaySum4 = sundaySum4 + sundays4Value
                                break
                            } else if (sunDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays3Value = subject.studytimeCopy.toFloat()
                                sundaySum3 = sundaySum3 + sundays3Value
                                break
                            } else if (sunDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays2Value = subject.studytimeCopy.toFloat()
                                sundaySum2 = sundaySum2 + sundays2Value
                                break
                            } else if (sunDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                Log.d(
                                    "totalSumMay",
                                    sunDay1.minusWeeks(4).format(textformatter).toString()
                                ) //25
                                sundays1Value = subject.studytimeCopy.toFloat()
                                sundaySum1 = sundaySum1 + sundays1Value
                                break
                            } else if (sunDay5LastMonth.format(textformatter) == serverDateFormat) {
                                sundays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum5LastMonth = sundaySum5LastMonth + sundays5ValueLastMonth
                                break
                            } else if (sunDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum4LastMonth = sundaySum4LastMonth + sundays4ValueLastMonth
                                break
                            } else if (sunDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum3LastMonth = sundaySum3LastMonth + sundays3ValueLastMonth
                                break
                            } else if (sunDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum2LastMonth = sundaySum2LastMonth + sundays2ValueLastMonth
                                break
                            } else if (sunDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum1LastMonth = sundaySum1LastMonth + sundays1ValueLastMonth
                                break
                            }
                        }
                    }

                    totalSum5 =
                        mondaySum5 + tuesdaySum5 + wednesdaySum5 + thursdaySum5 + fridaySum5 + saturdaySum5 + sundaySum5
                    totalSum4 =
                        mondaySum4 + tuesdaySum4 + wednesdaySum4 + thursdaySum4 + fridaySum4 + saturdaySum4 + sundaySum4
                    totalSum3 =
                        mondaySum3 + tuesdaySum3 + wednesdaySum3 + thursdaySum3 + fridaySum3 + saturdaySum3 + sundaySum3
                    totalSum2 =
                        mondaySum2 + tuesdaySum2 + wednesdaySum2 + thursdaySum2 + fridaySum2 + saturdaySum2 + sundaySum2
                    totalSum1 =
                        mondaySum1 + tuesdaySum1 + wednesdaySum1 + thursdaySum1 + fridaySum1 + saturdaySum1 + sundaySum1
                    totalTime = totalSum5 + totalSum4 + totalSum3 + totalSum2 + totalSum1

                    //????????? ?????? ?????? ??????
                    totalSum5LastMonth =
                        mondaySum5LastMonth + tuesdaySum5LastMonth + wednesdaySum5LastMonth + thursdaySum5LastMonth + fridaySum5LastMonth + saturdaySum5LastMonth + sundaySum5LastMonth
                    totalSum4LastMonth =
                        mondaySum4LastMonth + tuesdaySum4LastMonth + wednesdaySum4LastMonth + thursdaySum4LastMonth + fridaySum4LastMonth + saturdaySum4LastMonth + sundaySum4LastMonth
                    totalSum3LastMonth =
                        mondaySum3LastMonth + tuesdaySum3LastMonth + wednesdaySum3LastMonth + thursdaySum3LastMonth + fridaySum3LastMonth + saturdaySum3LastMonth + sundaySum3LastMonth
                    totalSum2LastMonth =
                        mondaySum2LastMonth + tuesdaySum2LastMonth + wednesdaySum2LastMonth + thursdaySum2LastMonth + fridaySum2LastMonth + saturdaySum2LastMonth + sundaySum2LastMonth
                    totalSum1LastMonth =
                        mondaySum1LastMonth + tuesdaySum1LastMonth + wednesdaySum1LastMonth + thursdaySum1LastMonth + fridaySum1LastMonth + saturdaySum1LastMonth + sundaySum1LastMonth
                    totalTimeLastMonth =
                        totalSum5LastMonth + totalSum4LastMonth + totalSum3LastMonth + totalSum2LastMonth + totalSum1LastMonth

                    binding.monthlyTotalTime.text =
                        "${(totalTime.toInt()) / 60}?????? ${(totalTime.toInt()) % 60}???"

                    //???????????? ?????????
                    var compareSum: Int = 0
                    //17, 33
                    if (totalTime > totalTimeLastMonth) {
                        compareSum = totalTime.toInt() - totalTimeLastMonth.toInt() //text
                        binding.comapreMonthTimeImage.setVisibility(true)
                        val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                        lp.setMargins(20, 15, 0, 0)
                        binding.comapreMonthTimeImage.setLayoutParams(lp)
                        binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_polygon_up)
                        binding.comapreMonthTimeText.text = "${compareSum / 60}??????"
                        binding.comapreMonthTimeText.setTextColor(Color.parseColor("#F95849"))
                    } else if (totalTime < totalTimeLastMonth) {
                        compareSum = totalTimeLastMonth.toInt() - totalTime.toInt() //text
                        binding.comapreMonthTimeImage.setVisibility(true)
                        val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                        lp.setMargins(20, 15, 0, 0)
                        binding.comapreMonthTimeImage.setLayoutParams(lp)
                        binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_polygon_down)
                        binding.comapreMonthTimeText.text = "${compareSum / 60}??????"
                        binding.comapreMonthTimeText.setTextColor(Color.parseColor("#0F8CFF"))
                    } else {
                        binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_linezero)
                        val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                        lp.setMargins(20, 25, 0, 0)
                        binding.comapreMonthTimeImage.setLayoutParams(lp)
                        binding.comapreMonthTimeText.text = "0??????"
                        binding.comapreMonthTimeText.setTextColor(Color.parseColor("#80000000"))
                    }

                    setData(totalSum1, totalSum2, totalSum3, totalSum4, totalSum5)
                }
                                }
        }

        binding.calLeftBtn.setOnClickListener {
            count--
            val minusDay: LocalDate = dateNow.plusMonths(count.toLong())
            binding.calendarMonth.text =  minusDay.format(textformatter).toString()
            if (count == 0) {
                binding.titleMonth.text = "?????? ??????"
            } else if (count == -1) {
                binding.titleMonth.text = "?????? ??????"
                binding.calRightBtn.isEnabled = true
            } else {
                binding.titleMonth.text = minusDay.format(titleformatter).toString()
            }
            monthCount--
            //-----------------------------------  monthCount--
            if (monthCount <= 0) {
                viewModel.list.observe(viewLifecycleOwner) {
                    val textformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
                    //??????
                    val dateNow: LocalDateTime = LocalDateTime.now()//------------------monthCount
                    val dateNowFormat: String = dateNow.format(textformatter)
                    Log.d("dafgsdfh", dateNowFormat)
                    //?????? ?????? ????????? ?????? ??????
                    val targetYearMonth: YearMonth = YearMonth.from(
                        LocalDate.parse(
                            dateNowFormat,
                            DateTimeFormatter.ofPattern("yyyy.MM.dd")
                        )
                    )
                    val end = targetYearMonth.atEndOfMonth()
                        .plusMonths(monthCount.toLong())////------------------monthCount
                    Log.d("dafgsdfh", end.toString())
                    //??????????????? ??? ????????? ?????????
                    val sizeOfWeek: Int = end.get(ChronoField.ALIGNED_WEEK_OF_MONTH) //?????? ????????? ??????
                    Log.d("asdfadsfSize", sizeOfWeek.toString())
//                val weekOfMonthSize : Int = weekSize-1
//                Log.d("asdfadsfSize-Minus",weekOfMonthSize.toString())

                    //5??? ????????? ??? ?????????
                    val monDay5 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay5 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay5 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay5 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay5 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay5 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay5 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays5Value: Float = 0f
                    var tuesdays5Value: Float = 0f
                    var wednesdays5Value: Float = 0f
                    var thursdays5Value: Float = 0f
                    var fridays5Value: Float = 0f
                    var saturdays5Value: Float = 0f
                    var sundays5Value: Float = 0f

                    var mondaySum5: Float = 0f
                    var tuesdaySum5: Float = 0f
                    var wednesdaySum5: Float = 0f
                    var thursdaySum5: Float = 0f
                    var fridaySum5: Float = 0f
                    var saturdaySum5: Float = 0f
                    var sundaySum5: Float = 0f
                    var totalSum5: Float = 0f

                    //4??? ????????? ??? ?????????
                    val monDay4 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay4 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay4 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay4 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay4 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay4 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay4 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays4Value: Float = 0f
                    var tuesdays4Value: Float = 0f
                    var wednesdays4Value: Float = 0f
                    var thursdays4Value: Float = 0f
                    var fridays4Value: Float = 0f
                    var saturdays4Value: Float = 0f
                    var sundays4Value: Float = 0f

                    var mondaySum4: Float = 0f
                    var tuesdaySum4: Float = 0f
                    var wednesdaySum4: Float = 0f
                    var thursdaySum4: Float = 0f
                    var fridaySum4: Float = 0f
                    var saturdaySum4: Float = 0f
                    var sundaySum4: Float = 0f
                    var totalSum4: Float = 0f

                    //3??? ????????? ??? ?????????
                    val monDay3 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay3 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay3 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay3 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay3 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay3 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay3 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays3Value: Float = 0f
                    var tuesdays3Value: Float = 0f
                    var wednesdays3Value: Float = 0f
                    var thursdays3Value: Float = 0f
                    var fridays3Value: Float = 0f
                    var saturdays3Value: Float = 0f
                    var sundays3Value: Float = 0f

                    var mondaySum3: Float = 0f
                    var tuesdaySum3: Float = 0f
                    var wednesdaySum3: Float = 0f
                    var thursdaySum3: Float = 0f
                    var fridaySum3: Float = 0f
                    var saturdaySum3: Float = 0f
                    var sundaySum3: Float = 0f
                    var totalSum3: Float = 0f

                    //2??? ????????? ??? ?????????
                    val monDay2 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay2 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay2 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay2 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay2 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay2 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay2 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays2Value: Float = 0f
                    var tuesdays2Value: Float = 0f
                    var wednesdays2Value: Float = 0f
                    var thursdays2Value: Float = 0f
                    var fridays2Value: Float = 0f
                    var saturdays2Value: Float = 0f
                    var sundays2Value: Float = 0f

                    var mondaySum2: Float = 0f
                    var tuesdaySum2: Float = 0f
                    var wednesdaySum2: Float = 0f
                    var thursdaySum2: Float = 0f
                    var fridaySum2: Float = 0f
                    var saturdaySum2: Float = 0f
                    var sundaySum2: Float = 0f
                    var totalSum2: Float = 0f

                    //1??? ????????? ??? ?????????
                    val monDay1 = end.with(DayOfWeek.MONDAY) //???
                    val tuesDay1 = end.with(DayOfWeek.TUESDAY) //???
                    val wednesDay1 = end.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay1 = end.with(DayOfWeek.THURSDAY) //???
                    val friDay1 = end.with(DayOfWeek.FRIDAY) //???
                    val saturDay1 = end.with(DayOfWeek.SATURDAY) //???
                    val sunDay1 = end.with(DayOfWeek.SUNDAY) //???

                    var mondays1Value: Float = 0f //??????
                    var tuesdays1Value: Float = 0f
                    var wednesdays1Value: Float = 0f
                    var thursdays1Value: Float = 0f
                    var fridays1Value: Float = 0f
                    var saturdays1Value: Float = 0f
                    var sundays1Value: Float = 0f

                    var mondaySum1: Float = 0f
                    var tuesdaySum1: Float = 0f
                    var wednesdaySum1: Float = 0f
                    var thursdaySum1: Float = 0f
                    var fridaySum1: Float = 0f
                    var saturdaySum1: Float = 0f
                    var sundaySum1: Float = 0f
                    var totalSum1: Float = 0f

                    var totalTime: Float = 0f

                    //????????? ?????????
                    //5??? ????????? ??? ?????????
                    val ends = targetYearMonth.atEndOfMonth().minusMonths(1)
                        .plusMonths(monthCount.toLong())

                    val monDay5LastMonth = ends.with(DayOfWeek.MONDAY)//???
                    val tuesDay5LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay5LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay5LastMonth = ends.with(DayOfWeek.THURSDAY)//???
                    val friDay5LastMonth = ends.with(DayOfWeek.FRIDAY) //???
                    val saturDay5LastMonth = ends.with(DayOfWeek.SATURDAY)//???
                    val sunDay5LastMonth = ends.with(DayOfWeek.SUNDAY) //???
                    Log.d("asdfsdgf", sunDay5LastMonth.toString())


                    var mondays5ValueLastMonth: Float = 0f
                    var tuesdays5ValueLastMonth: Float = 0f
                    var wednesdays5ValueLastMonth: Float = 0f
                    var thursdays5ValueLastMonth: Float = 0f
                    var fridays5ValueLastMonth: Float = 0f
                    var saturdays5ValueLastMonth: Float = 0f
                    var sundays5ValueLastMonth: Float = 0f

                    var mondaySum5LastMonth: Float = 0f
                    var tuesdaySum5LastMonth: Float = 0f
                    var wednesdaySum5LastMonth: Float = 0f
                    var thursdaySum5LastMonth: Float = 0f
                    var fridaySum5LastMonth: Float = 0f
                    var saturdaySum5LastMonth: Float = 0f
                    var sundaySum5LastMonth: Float = 0f

                    var totalSum5LastMonth: Float = 0f

                    //4??? ????????? ??? ?????????
                    val monDay4LastMonth = ends.with(DayOfWeek.MONDAY)//???
                    val tuesDay4LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay4LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay4LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay4LastMonth = ends.with(DayOfWeek.FRIDAY) //???
                    val saturDay4LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay4LastMonth = ends.with(DayOfWeek.SUNDAY) //???

                    var mondays4ValueLastMonth: Float = 0f
                    var tuesdays4ValueLastMonth: Float = 0f
                    var wednesdays4ValueLastMonth: Float = 0f
                    var thursdays4ValueLastMonth: Float = 0f
                    var fridays4ValueLastMonth: Float = 0f
                    var saturdays4ValueLastMonth: Float = 0f
                    var sundays4ValueLastMonth: Float = 0f

                    var mondaySum4LastMonth: Float = 0f
                    var tuesdaySum4LastMonth: Float = 0f
                    var wednesdaySum4LastMonth: Float = 0f
                    var thursdaySum4LastMonth: Float = 0f
                    var fridaySum4LastMonth: Float = 0f
                    var saturdaySum4LastMonth: Float = 0f
                    var sundaySum4LastMonth: Float = 0f

                    var totalSum4LastMonth: Float = 0f

                    //3??? ????????? ??? ?????????
                    val monDay3LastMonth = ends.with(DayOfWeek.MONDAY) //???
                    val tuesDay3LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay3LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay3LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay3LastMonth = ends.with(DayOfWeek.FRIDAY)//???
                    val saturDay3LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay3LastMonth = ends.with(DayOfWeek.SUNDAY) //???

                    var mondays3ValueLastMonth: Float = 0f
                    var tuesdays3ValueLastMonth: Float = 0f
                    var wednesdays3ValueLastMonth: Float = 0f
                    var thursdays3ValueLastMonth: Float = 0f
                    var fridays3ValueLastMonth: Float = 0f
                    var saturdays3ValueLastMonth: Float = 0f
                    var sundays3ValueLastMonth: Float = 0f

                    var mondaySum3LastMonth: Float = 0f
                    var tuesdaySum3LastMonth: Float = 0f
                    var wednesdaySum3LastMonth: Float = 0f
                    var thursdaySum3LastMonth: Float = 0f
                    var fridaySum3LastMonth: Float = 0f
                    var saturdaySum3LastMonth: Float = 0f
                    var sundaySum3LastMonth: Float = 0f

                    var totalSum3LastMonth: Float = 0f

                    //2??? ????????? ??? ?????????
                    val monDay2LastMonth = ends.with(DayOfWeek.MONDAY) //???
                    val tuesDay2LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay2LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay2LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay2LastMonth = ends.with(DayOfWeek.FRIDAY)//???
                    val saturDay2LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay2LastMonth = ends.with(DayOfWeek.SUNDAY) //???

                    var mondays2ValueLastMonth: Float = 0f
                    var tuesdays2ValueLastMonth: Float = 0f
                    var wednesdays2ValueLastMonth: Float = 0f
                    var thursdays2ValueLastMonth: Float = 0f
                    var fridays2ValueLastMonth: Float = 0f
                    var saturdays2ValueLastMonth: Float = 0f
                    var sundays2ValueLastMonth: Float = 0f

                    var mondaySum2LastMonth: Float = 0f
                    var tuesdaySum2LastMonth: Float = 0f
                    var wednesdaySum2LastMonth: Float = 0f
                    var thursdaySum2LastMonth: Float = 0f
                    var fridaySum2LastMonth: Float = 0f
                    var saturdaySum2LastMonth: Float = 0f
                    var sundaySum2LastMonth: Float = 0f

                    var totalSum2LastMonth: Float = 0f

                    //1??? ????????? ??? ?????????
                    val monDay1LastMonth = ends.with(DayOfWeek.MONDAY) //???
                    Log.d("asdfsdgf", monDay1LastMonth.toString())
                    val tuesDay1LastMonth = ends.with(DayOfWeek.TUESDAY) //???
                    val wednesDay1LastMonth = ends.with(DayOfWeek.WEDNESDAY) //???
                    val thursDay1LastMonth = ends.with(DayOfWeek.THURSDAY) //???
                    val friDay1LastMonth = ends.with(DayOfWeek.FRIDAY) //???
                    val saturDay1LastMonth = ends.with(DayOfWeek.SATURDAY) //???
                    val sunDay1LastMonth = ends.with(DayOfWeek.SUNDAY)//???
                    Log.d("asdfsdgf", sunDay1LastMonth.toString())

                    var mondays1ValueLastMonth: Float = 0f
                    var tuesdays1ValueLastMonth: Float = 0f
                    var wednesdays1ValueLastMonth: Float = 0f
                    var thursdays1ValueLastMonth: Float = 0f
                    var fridays1ValueLastMonth: Float = 0f
                    var saturdays1ValueLastMonth: Float = 0f
                    var sundays1ValueLastMonth: Float = 0f

                    var mondaySum1LastMonth: Float = 0f
                    var tuesdaySum1LastMonth: Float = 0f
                    var wednesdaySum1LastMonth: Float = 0f
                    var thursdaySum1LastMonth: Float = 0f
                    var fridaySum1LastMonth: Float = 0f
                    var saturdaySum1LastMonth: Float = 0f
                    var sundaySum1LastMonth: Float = 0f

                    var totalSum1LastMonth: Float = 0f

                    var totalTimeLastMonth: Float = 0f

                    //7???
                    val cal = Calendar.getInstance()
                    cal.get(Calendar.WEEK_OF_MONTH)
                    Log.d(
                        "asdfadsf",
                        cal.get(Calendar.WEEK_OF_MONTH).toString()
                    )//???????????? ????????? ????????? ??? ??? ?????????

//                val sizeOfWeek: Int = end.get(ChronoField.ALIGNED_WEEK_OF_MONTH) //?????? ????????? ??????
//                Log.d("asdfadsf",sizeOfWeek.toString()) //??????????????? ??? ????????? ????????? !!!!!~~~~!!!!!~~~~~~!!!!

                    var its: Int = 0
                    Log.d("asdgsdfg", its.toString())
                    it.forEachIndexed { index, subject ->
                        its = it.size
                        val calen: Calendar = Calendar.getInstance()
                        //???????????? ????????? ??????
                        val dateFormat: DateFormat = SimpleDateFormat("yyyy.MM.dd")

                        calen.add(Calendar.DATE, 1)
                        val serverDateFormat: String = dateFormat.format(subject.timestamp)
                        val serverDateFormatPlus1: String =
                            dateFormat.format(subject.timestamp?.day?.minus(1.toLong()))

                        Log.d("calendar_day1_ss", serverDateFormat.toString())
                        Log.d("calendar_day1_sssPlus", serverDateFormatPlus1.toString())


                        for (it in 0..its) {
                            if (monDay5.format(textformatter) == serverDateFormat) {
                                mondays5Value = subject.studytimeCopy.toFloat()
                                mondaySum5 = mondaySum5 + mondays5Value
                                break
                            } else if (monDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays4Value = subject.studytimeCopy.toFloat()
                                mondaySum4 = mondaySum4 + mondays4Value
                                break
                            } else if (monDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays3Value = subject.studytimeCopy.toFloat()
                                mondaySum3 = mondaySum3 + mondays3Value
                                break
                            } else if (monDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays2Value = subject.studytimeCopy.toFloat()
                                mondaySum2 = mondaySum2 + mondays2Value
                                break
                            } else if (monDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays1Value = subject.studytimeCopy.toFloat()
                                mondaySum1 = mondaySum1 + mondays1Value
                                break
                            } else if (monDay5LastMonth.format(textformatter) == serverDateFormat) {
                                mondays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum5LastMonth = mondaySum5LastMonth + mondays5ValueLastMonth
                                break
                            } else if (monDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum4LastMonth = mondaySum4LastMonth + mondays4ValueLastMonth
                                break
                            } else if (monDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum3LastMonth = mondaySum3LastMonth + mondays3ValueLastMonth
                                break
                            } else if (monDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum2LastMonth = mondaySum2LastMonth + mondays2ValueLastMonth
                                break
                            } else if (monDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                mondays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                mondaySum1LastMonth = mondaySum1LastMonth + mondays1ValueLastMonth
                                break
                            }
                        }


                        for (it in 0..its) {
                            if (tuesDay5.format(textformatter) == serverDateFormat) {
                                tuesdays5Value = subject.studytimeCopy.toFloat()
                                tuesdaySum5 = tuesdaySum5 + tuesdays5Value
                                break
                            } else if (tuesDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays4Value = subject.studytimeCopy.toFloat()
                                tuesdaySum4 = tuesdaySum4 + tuesdays4Value
                                break
                            } else if (tuesDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays3Value = subject.studytimeCopy.toFloat()
                                tuesdaySum3 = tuesdaySum3 + tuesdays3Value
                                break
                            } else if (tuesDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays2Value = subject.studytimeCopy.toFloat()
                                tuesdaySum2 = tuesdaySum2 + tuesdays2Value
                                break
                            } else if (tuesDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays1Value = subject.studytimeCopy.toFloat()
                                tuesdaySum1 = tuesdaySum1 + tuesdays1Value
                                break
                            } else if (tuesDay5LastMonth.format(textformatter) == serverDateFormat) {
                                tuesdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum5LastMonth =
                                    tuesdaySum5LastMonth + tuesdays5ValueLastMonth
                                break
                            } else if (tuesDay4LastMonth.minusWeeks(1).minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum4LastMonth =
                                    tuesdaySum4LastMonth + tuesdays4ValueLastMonth
                                break
                            } else if (tuesDay3LastMonth.minusWeeks(2).minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum3LastMonth =
                                    tuesdaySum3LastMonth + tuesdays3ValueLastMonth
                                break
                            } else if (tuesDay2LastMonth.minusWeeks(3).minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum2LastMonth =
                                    tuesdaySum2LastMonth + tuesdays2ValueLastMonth
                                break
                            } else if (tuesDay1LastMonth.minusWeeks(4).minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                tuesdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                tuesdaySum1LastMonth =
                                    tuesdaySum1LastMonth + tuesdays1ValueLastMonth
                                break
                            }
                        }


                        for (it in 0..its) {
                            if (wednesDay5.format(textformatter) == serverDateFormat) {
                                wednesdays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", wednesdays5Value.toString() + " ???")//21
                                Log.d("??????", subject.name.toString() + " : name")
                                wednesdaySum5 = wednesdaySum5 + wednesdays5Value
                                break
                            } else if (wednesDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays4Value = subject.studytimeCopy.toFloat()
                                wednesdaySum4 = wednesdaySum4 + wednesdays4Value
                            } else if (wednesDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays3Value = subject.studytimeCopy.toFloat()
                                wednesdaySum3 = wednesdaySum3 + wednesdays3Value
                                break
                            } else if (wednesDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays2Value = subject.studytimeCopy.toFloat()
                                wednesdaySum2 = wednesdaySum2 + wednesdays2Value
                                break
                            } else if (wednesDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays1Value = subject.studytimeCopy.toFloat()
                                wednesdaySum1 = wednesdaySum1 + wednesdays1Value
                                break
                            } else if (wednesDay5LastMonth.format(textformatter) == serverDateFormat) {
                                wednesdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum5LastMonth =
                                    wednesdaySum5LastMonth + wednesdays5ValueLastMonth
                                break
                            } else if (wednesDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum4LastMonth =
                                    wednesdaySum4LastMonth + wednesdays4ValueLastMonth
                                break
                            } else if (wednesDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum3LastMonth =
                                    wednesdaySum3LastMonth + wednesdays3ValueLastMonth
                                break
                            } else if (wednesDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum2LastMonth =
                                    wednesdaySum2LastMonth + wednesdays2ValueLastMonth
                                break
                            } else if (wednesDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                wednesdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                wednesdaySum1LastMonth =
                                    wednesdaySum1LastMonth + wednesdays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (thursDay5.format(textformatter) == serverDateFormat) {
                                thursdays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", thursdays5Value.toString() + " ???")//22
                                thursdaySum5 = thursdaySum5 + thursdays5Value
                                break
                            } else if (thursDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays4Value = subject.studytimeCopy.toFloat()
                                thursdaySum4 = thursdaySum4 + thursdays4Value
                                break
                            } else if (thursDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays3Value = subject.studytimeCopy.toFloat()
                                thursdaySum3 = thursdaySum3 + thursdays3Value
                                break
                            } else if (thursDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays2Value = subject.studytimeCopy.toFloat()
                                thursdaySum2 = thursdaySum2 + thursdays2Value
                                break
                            } else if (thursDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays1Value = subject.studytimeCopy.toFloat()
                                thursdaySum1 = thursdaySum1 + thursdays1Value
                                break
                            } else if (thursDay5LastMonth.format(textformatter) == serverDateFormat) {
                                thursdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum5LastMonth =
                                    thursdaySum5LastMonth + thursdays5ValueLastMonth
                                break
                            } else if (thursDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum4LastMonth =
                                    thursdaySum4LastMonth + thursdays4ValueLastMonth
                                break
                            } else if (thursDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum3LastMonth =
                                    thursdaySum3LastMonth + thursdays3ValueLastMonth
                                break
                            } else if (thursDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum2LastMonth =
                                    thursdaySum2LastMonth + thursdays2ValueLastMonth
                                break
                            } else if (thursDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                thursdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                thursdaySum1LastMonth =
                                    thursdaySum1LastMonth + thursdays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (friDay5.format(textformatter) == serverDateFormat) {
                                fridays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", fridays5Value.toString() + " ???")//23
                                fridaySum5 = fridaySum5 + fridays5Value
                                break
                            } else if (friDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays4Value = subject.studytimeCopy.toFloat()
                                fridaySum4 = fridaySum4 + fridays4Value
                                break
                            } else if (friDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays3Value = subject.studytimeCopy.toFloat()
                                fridaySum3 = fridaySum3 + fridays3Value
                                break
                            } else if (friDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays2Value = subject.studytimeCopy.toFloat()
                                fridaySum2 = fridaySum2 + fridays2Value
                                break
                            } else if (friDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays1Value = subject.studytimeCopy.toFloat()
                                fridaySum1 = fridaySum1 + fridays1Value
                                break
                            } else if (friDay5LastMonth.format(textformatter) == serverDateFormat) {
                                fridays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum5LastMonth = fridaySum5LastMonth + fridays5ValueLastMonth
                                break
                            } else if (friDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum4LastMonth = fridaySum4LastMonth + fridays4ValueLastMonth
                                break
                            } else if (friDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum3LastMonth = fridaySum3LastMonth + fridays3ValueLastMonth
                                break
                            } else if (friDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum2LastMonth = fridaySum2LastMonth + fridays2ValueLastMonth
                                break
                            } else if (friDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                fridays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                fridaySum1LastMonth = fridaySum1LastMonth + fridays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (saturDay5.format(textformatter) == serverDateFormat) {
                                saturdays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", saturdays5Value.toString() + " ???")//24
                                Log.d("??????", subject.name.toString() + " : name")
                                saturdaySum5 = saturdaySum5 + saturdays5Value
                                break
                            } else if (saturDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays4Value = subject.studytimeCopy.toFloat()
                                saturdaySum4 = saturdaySum4 + saturdays4Value
                                break
                            } else if (saturDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays3Value = subject.studytimeCopy.toFloat()
                                saturdaySum3 = saturdaySum3 + saturdays3Value
                                break
                            } else if (saturDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays2Value = subject.studytimeCopy.toFloat()
                                saturdaySum2 = saturdaySum2 + saturdays2Value
                                break
                            } else if (saturDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays1Value = subject.studytimeCopy.toFloat()
                                saturdaySum1 = saturdaySum1 + saturdays1Value
                                break
                            } else if (saturDay5LastMonth.format(textformatter) == serverDateFormat) {
                                saturdays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum5LastMonth =
                                    saturdaySum5LastMonth + saturdays5ValueLastMonth
                                break
                            } else if (saturDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum5LastMonth =
                                    saturdaySum4LastMonth + saturdays4ValueLastMonth
                                break
                            } else if (saturDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum3LastMonth =
                                    saturdaySum3LastMonth + saturdays3ValueLastMonth
                                break
                            } else if (saturDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum2LastMonth =
                                    saturdaySum2LastMonth + saturdays2ValueLastMonth
                                break
                            } else if (saturDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                saturdays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                saturdaySum1LastMonth =
                                    saturdaySum1LastMonth + saturdays1ValueLastMonth
                                break
                            }
                        }

                        for (it in 0..its) {
                            if (sunDay5.format(textformatter) == serverDateFormat) {
                                sundays5Value = subject.studytimeCopy.toFloat()
                                Log.d("??????", sundays5Value.toString() + " --???--")//25
                                Log.d("??????", subject.name.toString() + " : name")
                                sundaySum5 = sundaySum5 + sundays5Value
                                break
                            } else if (sunDay4.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays4Value = subject.studytimeCopy.toFloat()
                                sundaySum4 = sundaySum4 + sundays4Value
                                break
                            } else if (sunDay3.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays3Value = subject.studytimeCopy.toFloat()
                                sundaySum3 = sundaySum3 + sundays3Value
                                break
                            } else if (sunDay2.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays2Value = subject.studytimeCopy.toFloat()
                                sundaySum2 = sundaySum2 + sundays2Value
                                break
                            } else if (sunDay1.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                Log.d(
                                    "totalSumMay",
                                    sunDay1.minusWeeks(4).format(textformatter).toString()
                                ) //25
                                sundays1Value = subject.studytimeCopy.toFloat()
                                sundaySum1 = sundaySum1 + sundays1Value
                                break
                            } else if (sunDay5LastMonth.format(textformatter) == serverDateFormat) {
                                sundays5ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum5LastMonth = sundaySum5LastMonth + sundays5ValueLastMonth
                                break
                            } else if (sunDay4LastMonth.minusWeeks(1)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays4ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum4LastMonth = sundaySum4LastMonth + sundays4ValueLastMonth
                                break
                            } else if (sunDay3LastMonth.minusWeeks(2)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays3ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum3LastMonth = sundaySum3LastMonth + sundays3ValueLastMonth
                                break
                            } else if (sunDay2LastMonth.minusWeeks(3)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays2ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum2LastMonth = sundaySum2LastMonth + sundays2ValueLastMonth
                                break
                            } else if (sunDay1LastMonth.minusWeeks(4)
                                    .format(textformatter) == serverDateFormat
                            ) {
                                sundays1ValueLastMonth = subject.studytimeCopy.toFloat()
                                sundaySum1LastMonth = sundaySum1LastMonth + sundays1ValueLastMonth
                                break
                            }
                        }
                    }

                    totalSum5 =
                        mondaySum5 + tuesdaySum5 + wednesdaySum5 + thursdaySum5 + fridaySum5 + saturdaySum5 + sundaySum5
                    totalSum4 =
                        mondaySum4 + tuesdaySum4 + wednesdaySum4 + thursdaySum4 + fridaySum4 + saturdaySum4 + sundaySum4
                    totalSum3 =
                        mondaySum3 + tuesdaySum3 + wednesdaySum3 + thursdaySum3 + fridaySum3 + saturdaySum3 + sundaySum3
                    totalSum2 =
                        mondaySum2 + tuesdaySum2 + wednesdaySum2 + thursdaySum2 + fridaySum2 + saturdaySum2 + sundaySum2
                    totalSum1 =
                        mondaySum1 + tuesdaySum1 + wednesdaySum1 + thursdaySum1 + fridaySum1 + saturdaySum1 + sundaySum1
                    totalTime = totalSum5 + totalSum4 + totalSum3 + totalSum2 + totalSum1

                    //????????? ?????? ?????? ??????
                    totalSum5LastMonth =
                        mondaySum5LastMonth + tuesdaySum5LastMonth + wednesdaySum5LastMonth + thursdaySum5LastMonth + fridaySum5LastMonth + saturdaySum5LastMonth + sundaySum5LastMonth
                    totalSum4LastMonth =
                        mondaySum4LastMonth + tuesdaySum4LastMonth + wednesdaySum4LastMonth + thursdaySum4LastMonth + fridaySum4LastMonth + saturdaySum4LastMonth + sundaySum4LastMonth
                    totalSum3LastMonth =
                        mondaySum3LastMonth + tuesdaySum3LastMonth + wednesdaySum3LastMonth + thursdaySum3LastMonth + fridaySum3LastMonth + saturdaySum3LastMonth + sundaySum3LastMonth
                    totalSum2LastMonth =
                        mondaySum2LastMonth + tuesdaySum2LastMonth + wednesdaySum2LastMonth + thursdaySum2LastMonth + fridaySum2LastMonth + saturdaySum2LastMonth + sundaySum2LastMonth
                    totalSum1LastMonth =
                        mondaySum1LastMonth + tuesdaySum1LastMonth + wednesdaySum1LastMonth + thursdaySum1LastMonth + fridaySum1LastMonth + saturdaySum1LastMonth + sundaySum1LastMonth

                    totalTimeLastMonth =
                        totalSum5LastMonth + totalSum4LastMonth + totalSum3LastMonth + totalSum2LastMonth + totalSum1LastMonth

                    Log.d("totalTime--", totalTime.toString()) //0
                    Log.d("totalTime5", totalSum5.toString()) //0
                    Log.d("totalTime5", totalSum4.toString()) //0
                    Log.d("totalTime5", totalSum3.toString()) //0
                    Log.d("totalTime5", totalSum2.toString()) //0
                    Log.d("totalTime5", totalSum1.toString()) //0
                    Log.d("totalTimeLast--", totalTimeLastMonth.toString()) //2000.0
                    Log.d("totalTime5", totalSum5LastMonth.toString()) //0
                    Log.d("totalTime4", totalSum4LastMonth.toString()) //0
                    Log.d("totalTime3", totalSum3LastMonth.toString()) //0
                    Log.d("totalTime2", totalSum2LastMonth.toString()) //0
                    Log.d("totalTime1", totalSum1LastMonth.toString()) //0


                    binding.monthlyTotalTime.text =
                        "${(totalTime.toInt()) / 60}?????? ${(totalTime.toInt()) % 60}???"

                    //???????????? ?????????
                    var compareSum: Int = 0 //2000
                    //17, 33
                    if (totalTime > totalTimeLastMonth) {
                        compareSum = totalTime.toInt() - totalTimeLastMonth.toInt() //text
                        binding.comapreMonthTimeImage.setVisibility(true)
                        val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                        lp.setMargins(20, 15, 0, 0)
                        binding.comapreMonthTimeImage.setLayoutParams(lp)
                        binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_polygon_up)
                        binding.comapreMonthTimeText.text = "${compareSum / 60}??????"
                        binding.comapreMonthTimeText.setTextColor(Color.parseColor("#F95849"))
                    } else if (totalTime < totalTimeLastMonth) {
                        compareSum = totalTimeLastMonth.toInt() - totalTime.toInt() //text
                        binding.comapreMonthTimeImage.setVisibility(true)
                        val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                        lp.setMargins(20, 15, 0, 0)
                        binding.comapreMonthTimeImage.setLayoutParams(lp)
                        binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_polygon_down)
                        binding.comapreMonthTimeText.text = "${compareSum / 60}??????"
                        binding.comapreMonthTimeText.setTextColor(Color.parseColor("#0F8CFF"))
                    } else {
                        binding.comapreMonthTimeImage.setImageResource(R.drawable.ic_linezero)
                        val lp = LinearLayout.LayoutParams(binding.comapreMonthTimeImage.getLayoutParams())
                        lp.setMargins(20, 25, 0, 0)
                        binding.comapreMonthTimeImage.setLayoutParams(lp)
                        binding.comapreMonthTimeText.text = "0??????"
                        binding.comapreMonthTimeText.setTextColor(Color.parseColor("#80000000"))
                    }
                    Log.d("totalCompare", compareSum.toString())

                    setData(totalSum1, totalSum2, totalSum3, totalSum4, totalSum5)
                }
            }
        }
    }

    private fun initChart(chart: BarChart) {
//        customMarkerView.chartView = chart
        with(chart) {
//            marker = customMarkerView
            description.isEnabled = false
            legend.isEnabled = false
            isDoubleTapToZoomEnabled = false

//            setFitBars(true) //X?????? ?????? ????????? ????????? ?????????.
            setPinchZoom(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            //?????? ????????? ??????
            val barChartRender = CustomBarChartRender(this, animator, viewPortHandler).apply {
//                setRadius(10)
            }
            renderer = barChartRender
        }
    }
    private fun setData(one: Float, two: Float, three:Float, four:Float, five:Float) {
        Log.d("totalSum one", (one/60).toString())
        Log.d("totalSum2", (two/60).toString())
        Log.d("totalSum3", (three/60).toString())
        Log.d("totalSum4", (four/60).toString())
        Log.d("totalSum5", (five/60).toString())

        val textformatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val dateNow: String? = LocalDateTime.now().format(textformatter) //??????
        val targetYearMonth: YearMonth = YearMonth.from(LocalDate.parse(dateNow.toString(), DateTimeFormatter.ofPattern("yyyy.MM.dd")))
        val end = targetYearMonth.atEndOfMonth() //????????? ????????? ???
        val weekOfMonth: Int = end.get(ChronoField.ALIGNED_WEEK_OF_MONTH) //?????? ????????? ??????
        Log.d("asdfadsf",weekOfMonth.toString()) //??????????????? ??? ????????? ?????????
        val weekOfMonthSize : Int = (weekOfMonth-1)
        Log.d("asdfadsf",weekOfMonthSize.toString()) //??????????????? ??? ????????? ?????????


        val weekOfMonthValue = mutableListOf<Float>((one/60),(two/60),(three/60), (four/60),(five/60))
        val values = mutableListOf<BarEntry>()
        for(i in 0..weekOfMonthSize) {
            values.add(BarEntry(i.toFloat(),weekOfMonthValue.get(i)))
        }
//        barData.forEachIndexed { index, chartData ->
//            values.add(BarEntry(index.toFloat(), chartData.value))
//        }


        //?????? ????????? ?????? ??????
        val barDataSet = BarDataSet(values, "").apply {
            //??? ???????????? ?????? ????????? ???????????? ???????????? ??????  (y??? ???????????? ??????????????? ????????? true??? ???????????? ????????? ????????? false??? ????????????.)
            setDrawValues(false)

            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#FF339BFF"))
            setColors(colors)
            highLightAlpha = 0
        }

        //?????? ????????? ?????? ??????
        val dataSets = mutableListOf(barDataSet)
        val data = BarData(dataSets as List<IBarDataSet>?).apply {
//            setValueTextSize(30F)
            barWidth = 0.1F
        }
        with(binding.monthBarChart) {
            animateY(1000)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                textColor = ContentColor
                //?????? ????????? ??????????????? x??? ?????? ??????
                isGranularityEnabled = true // ????????? ????????????
                val xAxisLabels = listOf("?????????", "?????????", "?????????", "?????????", "????????????")
                valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//                valueFormatter = object : ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return barData[value.toInt()].date
//                    }
//                }
            }

            //?????? ?????? ???, Y?????? ( ?????? ?????????,????????? )
            axisRight.apply {
                textColor = ContentColor
                setDrawAxisLine(false) //??????
                gridLineWidth = 1F
                gridColor = transparentBlackColor
                axisLineColor = transparentBlackColor //?????? ?????? ??????
                enableGridDashedLine(5f,5f,5f)

                axisMaximum = 168F
                granularity = 42F
                axisMinimum = 0F
                setLabelCount(4,true) //??? ????????????

                //y??? ?????? ?????????
                valueFormatter = object : ValueFormatter() {
                    private val mFormat: DecimalFormat = DecimalFormat("###")
                    override fun getFormattedValue(value: Float): String {
                        return mFormat.format(value) + "??????"
                    }
                }
            }

            //?????? ????????? ???, Y?????? false??????
            axisLeft.apply {
                isEnabled = false
                gridLineWidth = 1F
                gridColor = ContentColor
                axisLineColor = transparentBlackColor //?????? ?????? ??????
//                labelPosition = floatArrayOf(0f, 10f, 20f, 50f, 100f, 300f)
//                setSpecificLabelPositions(floatArrayOf(0f, 10f, 20f, 50f, 100f, 300f))
                axisMaximum = 168F
                granularity = 42F
                axisMinimum = 0F
                setLabelCount(4, true) //??? ????????????
            }

            notifyDataSetChanged()  //chart??? ??? ????????? ?????????
            this.data = data
            invalidate()
        }
    }
    private fun monthlySubjectPieChart(){
        viewModel.list.observe(viewLifecycleOwner) {
            val pieChart: PieChart = binding.monthlyPieChart
            pieChart.setUsePercentValues(true)
            val values = mutableListOf<PieEntry>()
            val colorItems = mutableListOf<Int>()
            it.forEach{
                values.add(PieEntry(it.studytime.toFloat(),it.name.toString()))
            }
            it.forEachIndexed { index, subject ->
                colorItems.add(index,Color.parseColor(subject.color))
            }

            val pieDataSet = PieDataSet(values, "")
            pieDataSet.colors = colorItems
            pieDataSet.apply {
//            valueTextColor = Color.BLACK
                setDrawValues(false) //????????? ???????????? ??? ?????????
                valueTextSize = 16f
            }
            //% : ????????? ?????? ????????? ????????? ??????
            val pieData = PieData(pieDataSet)
            pieChart.apply {
                data = pieData
                description.isEnabled = false //?????? ????????? ????????? ?????? ???????????? ????????? ????????????.
                isRotationEnabled = false //???????????? ??????????????? ?????? ??? ??????
//            centerText = "this is color" //????????? ??? ????????? ????????? ?????????
//            setEntryLabelColor(Color.RED) //????????? ???????????? ????????? ??? ??????
                isEnabled = false
                legend.isEnabled = false //?????? ?????????
                isDrawHoleEnabled = true //????????? ?????? ????????? ??????
                holeRadius = 50f //????????? ????????? ??? ?????????
                setDrawEntryLabels(false) //????????? ?????? ?????? ??????
                animateY(1400, Easing.EaseInOutQuad)
                animate()
            }
        }

    }
    //adapter??? ????????? ??????
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.subjectList.observe(viewLifecycleOwner) {
            monthlySubjectAdapter.setData(it)
        }
    }
    private fun requestPermission(): Boolean {
        var permissions = false
        TedPermission.with(context)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    permissions = true      //p0=response(??????)
                    val shareIntent = Intent(context, ShareActivity::class.java)
                    startActivity(shareIntent)
//                    finish()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    permissions = false
                }

            })
            .setDeniedMessage("?????? ??????????????? ????????? ????????????????????????.")
            .setPermissions(Manifest.permission.CAMERA)
            .check()
        return permissions
    }

}
