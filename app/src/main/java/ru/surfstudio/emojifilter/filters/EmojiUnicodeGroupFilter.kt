package ru.surfstudio.emojifilter.filters

import android.text.InputFilter
import java.util.*

/**
 * Support Emoji 13.1 2020-09-12
 * @see https://unicode.org/Public/emoji/13.1/emoji-test.txt
 */
object EmojiUnicodeGroupFilter {

    private const val VARIATION_SELECTOR = "FE0F"
    private const val EMOJI_PARTS_GLUE = "200D"

    private val glueSymbols = arrayOf(
        VARIATION_SELECTOR,
        EMOJI_PARTS_GLUE
    )

    private val emojiUnicodeBlocks = arrayOf(
        Character.UnicodeBlock.EMOTICONS,
        Character.UnicodeBlock.MAHJONG_TILES,
        Character.UnicodeBlock.DOMINO_TILES,
        Character.UnicodeBlock.PLAYING_CARDS,
        Character.UnicodeBlock.ENCLOSED_ALPHANUMERICS,
        Character.UnicodeBlock.ENCLOSED_ALPHANUMERIC_SUPPLEMENT,
        Character.UnicodeBlock.ENCLOSED_IDEOGRAPHIC_SUPPLEMENT,
        Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS,
        Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS,
        Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_PICTOGRAPHS,
        Character.UnicodeBlock.MISCELLANEOUS_SYMBOLS_AND_ARROWS,
        Character.UnicodeBlock.MISCELLANEOUS_TECHNICAL,
        Character.UnicodeBlock.DINGBATS,
        Character.UnicodeBlock.TRANSPORT_AND_MAP_SYMBOLS,
        Character.UnicodeBlock.GEOMETRIC_SHAPES,
        Character.UnicodeBlock.ALCHEMICAL_SYMBOLS,
        Character.UnicodeBlock.ARROWS,
        Character.UnicodeBlock.SUPPLEMENTAL_ARROWS_A,
        Character.UnicodeBlock.SUPPLEMENTAL_ARROWS_B,
        Character.UnicodeBlock.COMBINING_MARKS_FOR_SYMBOLS,
        Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION,
        Character.UnicodeBlock.LETTERLIKE_SYMBOLS,
        Character.UnicodeBlock.TAGS
    )

    /**
     * Проверяет, что в стороке есть хотя бы 1 emoji
     */
    fun containsEmoji(str: String): Boolean {
        // unicode can be at most 2 Java Char(utf-16), use code point
        val cpCnt = str.codePointCount(0, str.length)
        for (index in 0 until cpCnt) {
            // i is index by code point
            val i = str.offsetByCodePoints(0, index)
            val codepoint = str.codePointAt(i)
            if (codepoint.isEmoji() || isPartOfComplexEmoji(index, str)) {
                return true
            }
        }
        return false
    }

    /**
     * Вырезает emoji из строки
     */
    fun removeEmoji(str: String): String {
        val sb = StringBuilder()
        // unicode can be at most 2 Java Char(utf-16), use code point
        val codePointsCount = str.codePointCount(0, str.length)
        for (codePointIndex in 0 until codePointsCount) {
            val indexInString = str.offsetByCodePoints(0, codePointIndex)
            val codePoint = str.codePointAt(indexInString)
            if (!codePoint.isEmoji() &&
                !codePoint.isGlueSymbol() &&
                !isPartOfComplexEmoji(codePointIndex, str)
            ) {
                sb.append(Character.toChars(codePoint))
            }
        }
        return sb.toString()
    }

    /**
     * Получить codePoint, следующий за codePoint с индексом = [index], в строке [str]
     */
    private fun getNextCodePoint(index: Int, str: String): Int? {
        val codePointsCount = str.codePointCount(0, str.length)
        val nextCodePointIndex = index + 1
        return if (nextCodePointIndex <= codePointsCount - 1) {
            val indexInString = str.offsetByCodePoints(0, nextCodePointIndex)
            str.codePointAt(indexInString)
        } else {
            null
        }
    }

    /**
     * Проверяет, что codePoint не входит в состав группы codePoint-ов,
     * вместе образующих эмоджи
     */
    private fun isPartOfComplexEmoji(index: Int, str: String): Boolean {
        val nextCodePoint = getNextCodePoint(index, str)
        return nextCodePoint?.isGlueSymbol() ?: false
    }

    /**
     * Проверяет, является ли codePoint связующим символом внутри группы символов
     */
    private fun Int.isGlueSymbol(): Boolean {
        val nextInHex = toHexStr(this)
        return nextInHex in glueSymbols
    }

    /**
     * Проверяет, относится ли codePoint к группе символов, характерной для emoji
     */
    private fun Int.isEmoji(): Boolean {
        val unicodeBlock = Character.UnicodeBlock.of(this)
        return unicodeBlock == null || unicodeBlock in emojiUnicodeBlocks
    }

    /**
     * Перевод codePoint в строковый эквивалент в верхнем регистре
     *
     * Пример (для 🤾): 129342 -> 1F93E
     */
    private fun toHexStr(c: Int): String {
        return Integer.toHexString(c).toUpperCase(Locale.ROOT)
    }
}
