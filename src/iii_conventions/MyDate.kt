package iii_conventions

import java.time.temporal.TemporalAmount

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate) = when {
        year != other.year -> year - other.year
        month != other.month -> month - other.month
        else -> dayOfMonth - other.dayOfMonth
    }

    infix operator fun plus(timeInterval: TimeInterval): MyDate {
        return parseTimeInterval(timeInterval)
    }

    infix operator fun plus(timeIntervalTimes: TimeIntervalTimes): MyDate {
        return parseTimeInterval(timeIntervalTimes.timeInterval, timeIntervalTimes.amount)
    }

    private fun parseTimeInterval(timeInterval: TimeInterval, i: Int = 1): MyDate {
        return when (timeInterval) {
            TimeInterval.YEAR -> this.addTimeIntervals(TimeInterval.YEAR, i)
            TimeInterval.WEEK -> this.addTimeIntervals(TimeInterval.WEEK, i)
            else -> this.addTimeIntervals(TimeInterval.DAY, i)
        }
    }
}

operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

enum class TimeInterval {
    DAY,
    WEEK,
    YEAR;

    infix operator fun times(i: Int): TimeIntervalTimes = TimeIntervalTimes(this, i)
}

class TimeIntervalTimes(val timeInterval: TimeInterval, val amount: Int)

class DateRange(val start: MyDate, val endInclusive: MyDate) : Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate> = DateIterator(this)

    infix operator fun contains(date: MyDate): Boolean {
        return date >= start && date <= endInclusive
    }
}

class DateIterator(val dateRange: DateRange) : Iterator<MyDate> {
    var current: MyDate = dateRange.start
    override fun next(): MyDate {
        val result = current
        current = current.nextDay()
        return result
    }
    override fun hasNext(): Boolean = current <= dateRange.endInclusive
}
