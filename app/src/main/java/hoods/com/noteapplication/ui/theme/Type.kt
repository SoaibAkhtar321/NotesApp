package hoods.com.noteapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(

    // 1. Main Headers (e.g., Screen Titles in TopAppBar)
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,           // Increased from 22.sp
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // 2. Sub-headers (e.g., Note titles in a list/grid)
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,           // Increased from 16.sp
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    // 3. Main body text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)