package com.example.hello

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
public class Typography {
  public val Headline: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 16.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Overline: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 10.sp,
         lineHeight = 12.sp) {
      }
    }

  public val `4 columns`: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Normal,
         fontSize = 0.sp,
         lineHeight = 0.sp) {
      }
    }

  public val Title_2: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 22.sp,
         lineHeight = 28.sp) {
      }
    }

  public val Caption_2: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Normal,
         fontSize = 12.sp,
         lineHeight = 14.sp) {
      }
    }

  public val Boby_4: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 16.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Body_2: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Normal,
         fontSize = 16.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Large_Title_1: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Bold,
         fontSize = 32.sp,
         lineHeight = 40.sp) {
      }
    }

  public val Caption_1: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 12.sp,
         lineHeight = 16.sp) {
      }
    }

  public val Subhead_3: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Normal,
         fontSize = 14.sp,
         lineHeight = 20.sp) {
      }
    }

  public val Large_Title_2: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Bold,
         fontSize = 28.sp,
         lineHeight = 32.sp) {
      }
    }

  public val Subhead_1: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Bold,
         fontSize = 14.sp,
         lineHeight = 16.sp) {
      }
    }

  public val Subtitle_2: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 18.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Subtitle_1: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Bold,
         fontSize = 18.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Body_1: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 16.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Body_3: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Normal,
         fontSize = 16.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Subtitle_3: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Normal,
         fontSize = 18.sp,
         lineHeight = 24.sp) {
      }
    }

  public val Caption_3: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 12.sp,
         lineHeight = 14.sp) {
      }
    }

  public val Title_1: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Bold,
         fontSize = 22.sp,
         lineHeight = 28.sp) {
      }
    }

  public val Subhead_4: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 14.sp,
         lineHeight = 20.sp) {
      }
    }

  public val Subhead_2: TextStyle
    @Composable
    get() {
      TextStyle(
         fontFamily = FontFamily.Default,
         fontWeight = FontWeight.Medium,
         fontSize = 14.sp,
         lineHeight = 20.sp) {
      }
    }
}
