/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer.wifi.accesspoint

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import com.vrem.wifianalyzer.wifi.model.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
@LooperMode(LooperMode.Mode.PAUSED)
class AccessPointDetailTest {
    private val ssid = "SSID"
    private val vendorName = "VendorName-VendorName-VendorName-VendorName-VendorName-VendorName"
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val fixture = AccessPointDetail()

    @Before
    fun setUp() {
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPLETE)
    }

    @After
    fun tearDown() {
        INSTANCE.restore()
    }

    @Test
    fun testMakeViewShouldCreateNewView() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testMakeViewShouldUseGivenView() {
        // setup
        val expected = mainActivity.layoutInflater.inflate(AccessPointViewType.COMPLETE.layout, null, false)
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(expected, null, wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testMakeViewCompleteWithTabGone() {
        // setup
        val wiFiAdditional = WiFiAdditional.EMPTY
        val wiFiDetail = withWiFiDetail(ssid, wiFiAdditional)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.tab).visibility)
    }

    @Test
    fun testMakeViewCompleteWithGroupIndicatorGone() {
        // setup
        val wiFiAdditional = WiFiAdditional.EMPTY
        val wiFiDetail = withWiFiDetail(ssid, wiFiAdditional)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.groupIndicator).visibility)
    }

    @Test
    fun testMakeViewCompleteWithVendorShortNotVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNull(actual.findViewById(R.id.vendorLong))
        assertEquals(View.GONE, actual.findViewById<View>(R.id.vendorShort).visibility)
    }

    @Test
    fun testMakeViewCompleteWithVendorShortVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNull(actual.findViewById(R.id.vendorLong))
        assertEquals(View.VISIBLE, actual.findViewById<View>(R.id.vendorShort).visibility)
    }

    @Test
    fun testMakeViewCompleteWithVendorShortMaximumSize() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValue(actual, vendorName.substring(0, 12), R.id.vendorShort)
    }

    @Test
    fun testMakeViewCompleteWithTabVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail, true)
        // validate
        assertEquals(View.VISIBLE, actual.findViewById<View>(R.id.tab).visibility)
    }

    @Test
    fun testMakeViewCompleteWithWiFiDetailAndEmptySSID() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesFullView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompleteWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesFullView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompleteWithTextNotSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertFalse(actual.findViewById<TextView>(R.id.ssid).isTextSelectable)
    }

    @Test
    fun testMakeViewCompactWithTabGone() {
        // setup
        val wiFiAdditional = WiFiAdditional.EMPTY
        val wiFiDetail = withWiFiDetail(ssid, wiFiAdditional)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.tab).visibility)
    }

    @Test
    fun testMakeViewCompactWithGroupIndicatorGone() {
        // setup
        val wiFiAdditional = WiFiAdditional.EMPTY
        val wiFiDetail = withWiFiDetail("SSID", wiFiAdditional)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.groupIndicator).visibility)
    }

    @Test
    fun testMakeViewCompactWithTabVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY, WiFiAdditional.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail, true)
        // validate
        assertEquals(View.VISIBLE, actual.findViewById<View>(R.id.tab).visibility)
    }

    @Test
    fun testMakeViewCompactWithWiFiDetailAndEmptySSID() {
        // setup
        val wiFiDetail = withWiFiDetail(String.EMPTY, WiFiAdditional.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompactWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        validateTextViewValuesCompactView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewCompactWithAttachPopup() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNotNull(actual.findViewById(R.id.attachPopup))
    }

    @Test
    fun testMakeViewCompactDoesNotHaveFullDetails() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertNull(actual.findViewById(R.id.channel_frequency_range))
        assertNull(actual.findViewById(R.id.width))
        assertNull(actual.findViewById(R.id.capabilities))
        assertNull(actual.findViewById(R.id.vendorShort))
    }

    @Test
    fun testMakeViewCompactWithTextNotSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        whenever(settings.accessPointView()).thenReturn(AccessPointViewType.COMPACT)
        // execute
        val actual = fixture.makeView(null, null, wiFiDetail)
        // validate
        assertFalse(actual.findViewById<TextView>(R.id.ssid).isTextSelectable)
    }

    @Test
    fun testMakeViewPopupWithWiFiDetail() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        validateTextViewValuesFullView(actual, wiFiDetail)
    }

    @Test
    fun testMakeViewDetailedWithVendorNotVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.vendorShort).visibility)
        assertEquals(View.GONE, actual.findViewById<View>(R.id.vendorLong).visibility)
    }

    @Test
    fun testMakeViewDetailedWithVendorVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional(vendorName, WiFiConnection.EMPTY))
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.vendorShort).visibility)
        assertEquals(View.VISIBLE, actual.findViewById<View>(R.id.vendorLong).visibility)
    }

    @Test
    fun testMakeViewDetailedWithTextSelectable() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertTrue(actual.findViewById<TextView>(R.id.ssid).isTextSelectable)
        assertTrue(actual.findViewById<TextView>(R.id.vendorLong).isTextSelectable)
    }

    @Test
    fun testMakeViewDetailedWith80211mcNotVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional.EMPTY)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertEquals(View.GONE, actual.findViewById<View>(R.id.flag80211mc).visibility)
    }

    @Test
    fun testMakeViewDetailedWith80211mcVisible() {
        // setup
        val wiFiDetail = withWiFiDetail(ssid, WiFiAdditional(vendorName, WiFiConnection.EMPTY), true)
        // execute
        val actual = fixture.makeViewDetailed(wiFiDetail)
        // validate
        assertEquals(View.VISIBLE, actual.findViewById<View>(R.id.flag80211mc).visibility)
    }

    private fun withWiFiDetail(SSID: String, wiFiAdditional: WiFiAdditional): WiFiDetail {
        return WiFiDetail(
                WiFiIdentifier(SSID, "BSSID"),
                "capabilities",
                WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2, false),
                wiFiAdditional)
    }

    private fun withWiFiDetail(SSID: String, wiFiAdditional: WiFiAdditional, is80211mc: Boolean): WiFiDetail {
        return WiFiDetail(
                WiFiIdentifier(SSID, "BSSID"),
                "capabilities",
                WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2, is80211mc),
                wiFiAdditional)
    }

    private fun validateTextViewValuesFullView(view: View, wiFiDetail: WiFiDetail) {
        validateTextViewValuesCompactView(view, wiFiDetail)
        val wiFiSignal = wiFiDetail.wiFiSignal
        validateTextViewValue(view, "${wiFiSignal.frequencyStart()} - ${wiFiSignal.frequencyEnd()}", R.id.channel_frequency_range)
        validateTextViewValue(view, "(${wiFiSignal.wiFiWidth.frequencyWidth}${WiFiSignal.FREQUENCY_UNITS})", R.id.width)
        validateTextViewValue(view, wiFiDetail.capabilities, R.id.capabilities)
    }

    private fun validateTextViewValuesCompactView(view: View, wiFiDetail: WiFiDetail) {
        val wiFiSignal = wiFiDetail.wiFiSignal
        validateTextViewValue(view, wiFiDetail.wiFiIdentifier.title(), R.id.ssid)
        validateTextViewValue(view, "${wiFiSignal.level}dBm", R.id.level)
        validateTextViewValue(view, wiFiSignal.channelDisplay(), R.id.channel)
        validateTextViewValue(view, "${wiFiSignal.primaryFrequency}${WiFiSignal.FREQUENCY_UNITS}", R.id.primaryFrequency)
        validateTextViewValue(view, wiFiSignal.distance(), R.id.distance)
    }

    private fun validateTextViewValue(view: View, expected: String, id: Int) {
        assertEquals(expected, view.findViewById<TextView>(id).text.toString())
    }

}