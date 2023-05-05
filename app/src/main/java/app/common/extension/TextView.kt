package app.common.extension

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.outcode.sweetappleacres.R
import java.util.regex.Pattern


/**
 * Method to color part of text with click action
 *
 * @param fullText    text in which part of it is to be colored
 * @param wordToColor text which is to be colored
 * @param color       color to be used
 * @param onClick     action to complete after clicking on colored part
 * @return spannable with part of text colored
 */
fun TextView.setSpannableText(
    fullText: String,
    wordToColor: String,
    color: Int,
    onClick: () -> Unit = {}
) {
    var startPosition = 0
    var endPosition = 0
    startPosition = fullText.indexOf(wordToColor)
    endPosition = startPosition + wordToColor.length


   // val face = ResourcesCompat.getFont(context, R.font.nanum_gothic_bold)


    val wordToSpan = SpannableString(fullText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = color
        //    ds.typeface = face
            ds.isUnderlineText = false

        }
    }

    if (startPosition < 0) {
    } else {
        wordToSpan.setSpan(
            clickableSpan,
            startPosition,
            endPosition,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    text = wordToSpan
    highlightColor = context.compatColor(R.color.colorPrimary)
    movementMethod = LinkMovementMethod.getInstance()

}

fun TextView.setSpannableTextWithBackgroundColor(
    fullText: String,
    wordToColor: String,
    color: Int,
    onClick: () -> Unit = {}
) {

    var startPosition = 0
    var endPosition = 0

    /*    val pattern = Pattern.compile(wordToColor)
     val matcher = pattern.matcher(fullText)
       while (matcher.find()) {
            startPosition = matcher.start()
            endPosition = matcher.end()
        }*/
    startPosition = fullText.indexOf(wordToColor)
    endPosition = startPosition + wordToColor.length

    val wordToSpan = SpannableString(fullText)

    if (startPosition < 0) {
    } else {
        wordToSpan.setSpan(
            BackgroundColorSpan(context.compatColor(R.color.colorPrimary)),
            startPosition,
            endPosition,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
    }

    text = wordToSpan
    highlightColor = Color.GREEN
    movementMethod = LinkMovementMethod.getInstance()

}


fun TextView.setDoubleSpannableText(
    fullText: String,
    firstWord: String,
    secondWord: String,
    color: Int,
    onFirstClick: () -> Unit = {},
    onSecondClick: () -> Unit = {}
) {
    val firstTextPattern = Pattern.compile(firstWord)
    val secondTextPattern = Pattern.compile(secondWord)

    val firstMatcher = firstTextPattern.matcher(fullText)
    val secondMatcher = secondTextPattern.matcher(fullText)

    var firstStartPosition = 0
    var secondStartPosition = 0

    var firstEndPosition = 0
    var secondEndPosition = 0

    while (firstMatcher.find()) {
        firstStartPosition = firstMatcher.start()
        firstEndPosition = firstMatcher.end()
    }

    while (secondMatcher.find()) {
        secondStartPosition = secondMatcher.start()
        secondEndPosition = secondMatcher.end()
    }

    val wordToSpan = SpannableString(fullText)


    val clickableFirstSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onFirstClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = color
            ds.isUnderlineText = false
        }
    }
    val clickableSecondSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onSecondClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = color
            ds.isUnderlineText = false
        }
    }
    wordToSpan.setSpan(
        clickableFirstSpan,
        firstStartPosition,
        firstEndPosition,
        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
    )

    wordToSpan.setSpan(
        clickableSecondSpan,
        secondStartPosition,
        secondEndPosition,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = wordToSpan
    highlightColor = Color.TRANSPARENT
    movementMethod = LinkMovementMethod.getInstance()
}