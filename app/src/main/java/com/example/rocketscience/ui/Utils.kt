package com.example.rocketscience.ui

import android.icu.text.CompactDecimalFormat
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun Instant.formatDateTime(
    dateStyle: FormatStyle = FormatStyle.MEDIUM,
    timeStyle: FormatStyle = FormatStyle.MEDIUM
): String =
    DateTimeFormatter
        .ofLocalizedDateTime(dateStyle, timeStyle)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
        .format(toJavaInstant())

fun Instant.formatDate(dateStyle: FormatStyle = FormatStyle.MEDIUM): String =
    DateTimeFormatter
        .ofLocalizedDate(dateStyle)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
        .format(toJavaInstant())

fun Instant.formatTime(timeStyle: FormatStyle = FormatStyle.MEDIUM): String =
    DateTimeFormatter
        .ofLocalizedTime(timeStyle)
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.systemDefault())
        .format(toJavaInstant())

fun Long.compactNumberFormat(
    locale: Locale = Locale.getDefault(),
    style: CompactDecimalFormat.CompactStyle = CompactDecimalFormat.CompactStyle.SHORT
): String =
    CompactDecimalFormat
        .getInstance(locale, style)
        .format(this)