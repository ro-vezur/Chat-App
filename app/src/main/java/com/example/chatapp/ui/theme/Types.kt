package com.example.chatapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.chatapp.R


val ChatBotFontFamily = FontFamily(
    Font(R.font.inter, FontWeight.Normal),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_medium, FontWeight.Medium)
)

// Typography for small screens (phones)
val SmallScreenTypography = Typography(
    headlineLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 36.sp),
    headlineMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 34.sp,),
    headlineSmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 32.sp,),
    displayLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp,),
    displayMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp,),
    displaySmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp,),
    bodyLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp,),
    bodyMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp,),
    bodySmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Light, fontSize = 12.sp,),
    labelLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp,),
    labelMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp,),
)

// Typography for medium screens (tablets, foldables)
val MediumScreenTypography = Typography(
    headlineLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 42.sp),
    headlineMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 40.sp,),
    headlineSmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 38.sp,),
    displayLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 36.sp,),
    displayMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp,),
    displaySmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 28.sp,),
    bodyLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp,),
    bodyMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Normal, fontSize = 20.sp,),
    bodySmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Light, fontSize = 16.sp,),
    labelLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 26.sp,),
    labelMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp,),
)

// Typography for large screens (tablets in landscape mode, desktops)
val LargeScreenTypography = Typography(
    headlineLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 50.sp),
    headlineMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 48.sp,),
    headlineSmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 46.sp,),
    displayLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 44.sp,),
    displayMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 40.sp,),
    displaySmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 36.sp,),
    bodyLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Normal, fontSize = 22.sp,),
    bodyMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Normal, fontSize = 20.sp),
    bodySmall = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Light, fontSize = 18.sp),
    labelLarge = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp),
    labelMedium = TextStyle(fontFamily = ChatBotFontFamily, fontWeight = FontWeight.Bold, fontSize = 26.sp,),
)