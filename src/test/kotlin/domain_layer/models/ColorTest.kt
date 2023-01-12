package domain_layer.models

import org.junit.jupiter.api.Test

class ColorTest {
    companion object {
        const val testName = "Dark/Text/Accent_Disable"
    }
    @Test
    fun testUnderScoreStyleInColorName() {
        val color = Color(testName,
            "",
            .0,
            .0,
            .0,
            .0)

        assert(color.nameForResource.contains("_"))
    }

    @Test
    fun testLowerCaseInColorName() {
        val color = Color(
            testName,
            "",
            .0,
            .0,
            .0,
            .0)

        assert(color.nameForResource[0] == testName.toLowerCase()[0])
    }

    @Test
    fun getNameForCode() {
        val color = Color("Dark/Text/Accent_Disable",
            "",
            .0,
            .0,
            .0,
            .0)
        assert(!color.nameForCode.contains("_"))
    }

    @Test
    fun testGetName() {
        val color = Color("Dark/Text/Accent_Disable",
            "",
            .0,
            .0,
            .0,
            .0)

        assert(!color.name.isNullOrEmpty())
    }

    @Test
    fun testGetOriginalName() {
        val color = Color("Dark/Text/Accent_Disable",
            "test",
            .0,
            .0,
            .0,
            .0)

        assert(!color.name.isNullOrEmpty())
    }

    @Test
    fun testGetRed() {
        val color = Color("Dark/Text/Accent_Disable",
            "test",
            .12,
            .12,
            .12,
            1.0)

        assert(color.red > 0)
    }

    @Test
    fun testGetGreen() {
        val color = Color("Dark/Text/Accent_Disable",
            "test",
            .12,
            .12,
            .12,
            1.0)

        assert(color.green > 0)
    }

    @Test
    fun testGetBlue() {
        val color = Color("Dark/Text/Accent_Disable",
            "test",
            .12,
            .12,
            .12,
            1.0)

        assert(color.blue > 0)
    }

    @Test
    fun testGetAlpha() {
        val color = Color("Dark/Text/Accent_Disable",
            "test",
            .12,
            .12,
            .12,
            1.0)

        assert(color.alpha > 0)
    }

    @Test
    fun testColorInHex() {
        val color = Color("Dark/Text/Accent_Disable",
            "test",
            0.3499999940395355,
            .047058820724487305,
            .11323533207178116,
            1.0)
        println(color.inHex())
        assert(color.inHex() == "#3F7DFE")
    }
}