package com.spiderdt.mars.util

import groovy.time.DatumDependentDuration
import groovy.time.TimeCategory
/**
 * @Title:
 * @Package com.spiderdt.jupiter.util
 * @Description:
 * @author Kevin
 * @date 2017/6/5 10:45
 * @version V1.0
 */
class DateUtil {


    static void main(String[] args) {
        def result =  parseDateFromJobId('bolome_caizhuangtest_20150508_20160229')


        println result.finishDate == result.startDate

        println plusYears(result.finishDate, 1)
        println minusYears(result.finishDate, 1)

    }

    static parseDateFromJobId(String jobId) {

        String[] splitResult = jobId.split('_')

        String finishDateStr = splitResult.last()
        String startDateStr = splitResult.dropRight(1).last()

        [
                startDate : Date.parse('yyyyMMdd', startDateStr),
                finishDate: Date.parse('yyyyMMdd', finishDateStr)
        ]

    }

    static plusYears(Date dt, int year) {
        def period = new DatumDependentDuration(year,0,0,0,0,0,0)
        TimeCategory.plus(dt, period)
    }

    static minusYears(Date dt, int year) {
        def period = new DatumDependentDuration(year,0,0,0,0,0,0)
        TimeCategory.minus(dt, period)
    }

}
